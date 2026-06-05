import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ConfidenceLevel } from '@/shared/model/enumerations/confidence-level.model';
import { type IPersonSkill, PersonSkill } from '@/shared/model/person-skill.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';

import PersonSkillService from './person-skill.service';

export default defineComponent({
  name: 'PersonSkillUpdate',
  setup() {
    const personSkillService = inject('personSkillService', () => new PersonSkillService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const personSkill: Ref<IPersonSkill> = ref(new PersonSkill());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);

    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());

    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const confidenceLevelValues: Ref<string[]> = ref(Object.keys(ConfidenceLevel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePersonSkill = async personSkillId => {
      try {
        const res = await personSkillService().find(personSkillId);
        personSkill.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personSkillId) {
      retrievePersonSkill(route.params.personSkillId);
    }

    const initRelationships = () => {
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

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      assessmentDate: {
        required: validations.required('This field is required.'),
      },
      nextReviewDate: {},
      evidence: {},
      confidence: {},
      growthDirection: {},
      person: {
        required: validations.required('This field is required.'),
      },
      skill: {
        required: validations.required('This field is required.'),
      },
      currentLevel: {
        required: validations.required('This field is required.'),
      },
      previousLevel: {},
    };
    const v$ = useVuelidate(validationRules, personSkill as any);
    v$.value.$validate();

    return {
      personSkillService,
      alertService,
      personSkill,
      previousState,
      confidenceLevelValues,
      isSaving,
      currentLanguage,
      people,
      skills,
      skillLevels,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.personSkill.id) {
        this.personSkillService()
          .update(this.personSkill)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PersonSkill is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.personSkillService()
          .create(this.personSkill)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PersonSkill is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
