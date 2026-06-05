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
import { SkillChangeType } from '@/shared/model/enumerations/skill-change-type.model';
import { type IPersonSkill } from '@/shared/model/person-skill.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkillUpgradeRecord, SkillUpgradeRecord } from '@/shared/model/skill-upgrade-record.model';

import SkillUpgradeRecordService from './skill-upgrade-record.service';

export default defineComponent({
  name: 'SkillUpgradeRecordUpdate',
  setup() {
    const skillUpgradeRecordService = inject('skillUpgradeRecordService', () => new SkillUpgradeRecordService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const skillUpgradeRecord: Ref<ISkillUpgradeRecord> = ref(new SkillUpgradeRecord());

    const personSkillService = inject('personSkillService', () => new PersonSkillService());

    const personSkills: Ref<IPersonSkill[]> = ref([]);

    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());

    const skillLevels: Ref<ISkillLevel[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);
    const skillChangeTypeValues: Ref<string[]> = ref(Object.keys(SkillChangeType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSkillUpgradeRecord = async skillUpgradeRecordId => {
      try {
        const res = await skillUpgradeRecordService().find(skillUpgradeRecordId);
        skillUpgradeRecord.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillUpgradeRecordId) {
      retrieveSkillUpgradeRecord(route.params.skillUpgradeRecordId);
    }

    const initRelationships = () => {
      personSkillService()
        .retrieve()
        .then(res => {
          personSkills.value = res.data;
        });
      skillLevelService()
        .retrieve()
        .then(res => {
          skillLevels.value = res.data;
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
    };

    initRelationships();

    const filteredSkills = computed(() => {
      const personId = skillUpgradeRecord.value.person?.id;
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
      changeType: {
        required: validations.required('This field is required.'),
      },
      changeDate: {
        required: validations.required('This field is required.'),
      },
      reason: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 200 characters.', 200),
      },
      beforeLevelLabel: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      afterLevelLabel: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      evidence: {},
      comment: {},
      person: {
        required: validations.required('This field is required.'),
      },
      skill: {
        required: validations.required('This field is required.'),
      },
      oldLevel: {},
      newLevel: {
        required: validations.required('This field is required.'),
      },
      assessor: {},
    };
    const v$ = useVuelidate(validationRules, skillUpgradeRecord as any);
    v$.value.$validate();

    return {
      skillUpgradeRecordService,
      alertService,
      skillUpgradeRecord,
      previousState,
      skillChangeTypeValues,
      isSaving,
      currentLanguage,
      personSkills,
      filteredSkills,
      skillLevels,
      people,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    onPersonChange(): void {
      this.skillUpgradeRecord.skill = undefined;
      this.skillUpgradeRecord.oldLevel = null;
      this.skillUpgradeRecord.beforeLevelLabel = null;
    },
    onSkillChange(): void {
      const personSkill = this.personSkills.find(
        item => item.person?.id === this.skillUpgradeRecord.person?.id && item.skill?.id === this.skillUpgradeRecord.skill?.id,
      );
      this.skillUpgradeRecord.oldLevel = personSkill?.currentLevel ?? null;
      this.skillUpgradeRecord.beforeLevelLabel = personSkill?.currentLevel?.code ?? null;
    },
    save(): void {
      this.isSaving = true;
      if (this.skillUpgradeRecord.id) {
        this.skillUpgradeRecordService()
          .update(this.skillUpgradeRecord)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SkillUpgradeRecord is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.skillUpgradeRecordService()
          .create(this.skillUpgradeRecord)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SkillUpgradeRecord is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
