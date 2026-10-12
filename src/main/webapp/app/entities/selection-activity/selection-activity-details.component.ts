import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISelectionActivity } from '@/shared/model/selection-activity.model';

import SelectionActivityService from './selection-activity.service';

export default defineComponent({
  name: 'SelectionActivityDetails',
  setup() {
    const dateFormat = useDateFormat();
    const selectionActivityService = inject('selectionActivityService', () => new SelectionActivityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const selectionActivity: Ref<ISelectionActivity> = ref({});

    const retrieveSelectionActivity = async selectionActivityId => {
      try {
        const res = await selectionActivityService().find(selectionActivityId);
        selectionActivity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.selectionActivityId) {
      retrieveSelectionActivity(route.params.selectionActivityId);
    }

    return {
      ...dateFormat,
      alertService,
      selectionActivity,

      ...dataUtils,

      previousState,
    };
  },
});
