import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { LevelCode } from '@/shared/model/enumerations/level-code.model';
import { type ISkillLevel, SkillLevel } from '@/shared/model/skill-level.model';

import SkillLevelService from './skill-level.service';

export default defineComponent({
  name: 'SkillLevelUpdate',
  setup() {
    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const skillLevel: Ref<ISkillLevel> = ref(new SkillLevel());
    const levelCodeValues: Ref<string[]> = ref(Object.keys(LevelCode));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSkillLevel = async skillLevelId => {
      try {
        const res = await skillLevelService().find(skillLevelId);
        skillLevel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillLevelId) {
      retrieveSkillLevel(route.params.skillLevelId);
    }

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      code: {
        required: validations.required('This field is required.'),
      },
      levelName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      definition: {},
      observableEvidence: {},
      sortOrder: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
    };
    const v$ = useVuelidate(validationRules, skillLevel as any);
    v$.value.$validate();

    return {
      skillLevelService,
      alertService,
      skillLevel,
      previousState,
      levelCodeValues,
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
      if (this.skillLevel.id) {
        this.skillLevelService()
          .update(this.skillLevel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SkillLevel is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.skillLevelService()
          .create(this.skillLevel)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SkillLevel is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
