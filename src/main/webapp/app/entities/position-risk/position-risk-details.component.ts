import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionRisk } from '@/shared/model/position-risk.model';

import PositionRiskService from './position-risk.service';

export default defineComponent({
  name: 'PositionRiskDetails',
  setup() {
    const positionRiskService = inject('positionRiskService', () => new PositionRiskService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const positionRisk: Ref<IPositionRisk> = ref({});

    const retrievePositionRisk = async positionRiskId => {
      try {
        const res = await positionRiskService().find(positionRiskId);
        positionRisk.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionRiskId) {
      retrievePositionRisk(route.params.positionRiskId);
    }

    return {
      alertService,
      positionRisk,

      ...dataUtils,

      previousState,
    };
  },
});
