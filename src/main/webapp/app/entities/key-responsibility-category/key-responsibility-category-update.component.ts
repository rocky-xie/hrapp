import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IKeyResponsibilityCategory, KeyResponsibilityCategory } from '@/shared/model/key-responsibility-category.model';

import KeyResponsibilityCategoryService from './key-responsibility-category.service';

export default defineComponent({
  name: 'KeyResponsibilityCategoryUpdate',
  setup() {
    const keyResponsibilityCategoryService = inject('keyResponsibilityCategoryService', () => new KeyResponsibilityCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const keyResponsibilityCategory: Ref<IKeyResponsibilityCategory> = ref(new KeyResponsibilityCategory());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveKeyResponsibilityCategory = async keyResponsibilityCategoryId => {
      try {
        const res = await keyResponsibilityCategoryService().find(keyResponsibilityCategoryId);
        keyResponsibilityCategory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.keyResponsibilityCategoryId) {
      retrieveKeyResponsibilityCategory(route.params.keyResponsibilityCategoryId);
    }

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      categoryName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      examples: {},
      riskFocus: {},
    };
    const v$ = useVuelidate(validationRules, keyResponsibilityCategory as any);
    v$.value.$validate();

    return {
      keyResponsibilityCategoryService,
      alertService,
      keyResponsibilityCategory,
      previousState,
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
      if (this.keyResponsibilityCategory.id) {
        this.keyResponsibilityCategoryService()
          .update(this.keyResponsibilityCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A KeyResponsibilityCategory is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.keyResponsibilityCategoryService()
          .create(this.keyResponsibilityCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A KeyResponsibilityCategory is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
