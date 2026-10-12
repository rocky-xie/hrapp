import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IHiringDecision } from '@/shared/model/hiring-decision.model';

import HiringDecisionService from './hiring-decision.service';

export default defineComponent({
  name: 'HiringDecisionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const hiringDecisionService = inject('hiringDecisionService', () => new HiringDecisionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const hiringDecision: Ref<IHiringDecision> = ref({});

    const retrieveHiringDecision = async hiringDecisionId => {
      try {
        const res = await hiringDecisionService().find(hiringDecisionId);
        hiringDecision.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.hiringDecisionId) {
      retrieveHiringDecision(route.params.hiringDecisionId);
    }

    return {
      ...dateFormat,
      alertService,
      hiringDecision,

      ...dataUtils,

      previousState,
    };
  },
});
