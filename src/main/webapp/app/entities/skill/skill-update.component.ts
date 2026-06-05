import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { EvidenceType } from '@/shared/model/enumerations/evidence-type.model';
import { SkillType } from '@/shared/model/enumerations/skill-type.model';
import { type ISkill, Skill } from '@/shared/model/skill.model';

import SkillService from './skill.service';

export default defineComponent({
  name: 'SkillUpdate',
  setup() {
    const skillService = inject('skillService', () => new SkillService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const skill: Ref<ISkill> = ref(new Skill());
    const skillTypeValues: Ref<string[]> = ref(Object.keys(SkillType));
    const evidenceTypeValues: Ref<string[]> = ref(Object.keys(EvidenceType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSkill = async skillId => {
      try {
        const res = await skillService().find(skillId);
        skill.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillId) {
      retrieveSkill(route.params.skillId);
    }

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      skillCode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      skillName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      skillType: {
        required: validations.required('This field is required.'),
      },
      measurableFlag: {},
      description: {},
      evidenceType: {},
    };
    const v$ = useVuelidate(validationRules, skill as any);
    v$.value.$validate();

    return {
      skillService,
      alertService,
      skill,
      previousState,
      skillTypeValues,
      evidenceTypeValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.skill.id) {
        this.skillService()
          .update(this.skill)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Skill is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.skillService()
          .create(this.skill)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Skill is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
