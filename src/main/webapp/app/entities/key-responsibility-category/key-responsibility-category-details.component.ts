import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IKeyResponsibilityCategory } from '@/shared/model/key-responsibility-category.model';

import KeyResponsibilityCategoryService from './key-responsibility-category.service';

export default defineComponent({
  name: 'KeyResponsibilityCategoryDetails',
  setup() {
    const keyResponsibilityCategoryService = inject('keyResponsibilityCategoryService', () => new KeyResponsibilityCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const keyResponsibilityCategory: Ref<IKeyResponsibilityCategory> = ref({});

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

    return {
      alertService,
      keyResponsibilityCategory,

      ...dataUtils,

      previousState,
    };
  },
});
