import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IDispatchReview } from '@/shared/model/dispatch-review.model';

import DispatchReviewService from './dispatch-review.service';

export default defineComponent({
  name: 'DispatchReviewDetails',
  setup() {
    const dateFormat = useDateFormat();
    const dispatchReviewService = inject('dispatchReviewService', () => new DispatchReviewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dispatchReview: Ref<IDispatchReview> = ref({});

    const retrieveDispatchReview = async dispatchReviewId => {
      try {
        const res = await dispatchReviewService().find(dispatchReviewId);
        dispatchReview.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dispatchReviewId) {
      retrieveDispatchReview(route.params.dispatchReviewId);
    }

    return {
      ...dateFormat,
      alertService,
      dispatchReview,

      ...dataUtils,

      previousState,
    };
  },
});
