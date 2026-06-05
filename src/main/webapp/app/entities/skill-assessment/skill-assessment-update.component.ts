import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PersonSkillService from '@/entities/person-skill/person-skill.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { AssessmentResult } from '@/shared/model/enumerations/assessment-result.model';
import { type IPersonSkill } from '@/shared/model/person-skill.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkillAssessment, SkillAssessment } from '@/shared/model/skill-assessment.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

import SkillAssessmentService from './skill-assessment.service';

export default defineComponent({
  name: 'SkillAssessmentUpdate',
  setup() {
    const skillAssessmentService = inject('skillAssessmentService', () => new SkillAssessmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const skillAssessment: Ref<ISkillAssessment> = ref(new SkillAssessment());

    const personSkillService = inject('personSkillService', () => new PersonSkillService());

    const personSkills: Ref<IPersonSkill[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);

    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());

    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const assessmentResultValues: Ref<string[]> = ref(Object.keys(AssessmentResult));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSkillAssessment = async skillAssessmentId => {
      try {
        const res = await skillAssessmentService().find(skillAssessmentId);
        skillAssessment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillAssessmentId) {
      retrieveSkillAssessment(route.params.skillAssessmentId);
    }

    const initRelationships = () => {
      personSkillService()
        .retrieve()
        .then(res => {
          personSkills.value = res.data;
        });
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
      skillService()
        .retrieve()
        .then(res => {
          skills.value = res.data;
        });
      skillLevelService()
        .retrieve()
        .then(res => {
          skillLevels.value = res.data;
        });
    };

    initRelationships();

    const filteredSkills = computed(() => {
      const personId = skillAssessment.value.person?.id;
      if (!personId) {
        return skills.value;
      }

      const skillIds = new Set(
        personSkills.value
          .filter(personSkill => personSkill.person?.id === personId && personSkill.skill?.id)
          .map(personSkill => personSkill.skill!.id),
      );
      return skills.value.filter(skill => skill.id && skillIds.has(skill.id));
    });

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      assessmentDate: {
        required: validations.required('This field is required.'),
      },
      result: {
        required: validations.required('This field is required.'),
      },
      evidence: {},
      comment: {},
      person: {
        required: validations.required('This field is required.'),
      },
      skill: {
        required: validations.required('This field is required.'),
      },
      assessor: {},
      newLevel: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, skillAssessment as any);
    v$.value.$validate();

    return {
      skillAssessmentService,
      alertService,
      skillAssessment,
      previousState,
      assessmentResultValues,
      isSaving,
      currentLanguage,
      personSkills,
      filteredSkills,
      people,
      skillLevels,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    onPersonChange(): void {
      this.skillAssessment.skill = null;
    },
    save(): void {
      this.isSaving = true;
      if (this.skillAssessment.id) {
        this.skillAssessmentService()
          .update(this.skillAssessment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SkillAssessment is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.skillAssessmentService()
          .create(this.skillAssessment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SkillAssessment is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
