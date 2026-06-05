import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionRiskEvaluation } from '@/shared/model/position-risk-evaluation.model';

import PositionRiskEvaluationService from './position-risk-evaluation.service';

export default defineComponent({
  name: 'PositionRiskEvaluationDetails',
  setup() {
    const positionRiskEvaluationService = inject('positionRiskEvaluationService', () => new PositionRiskEvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const positionRiskEvaluation: Ref<IPositionRiskEvaluation> = ref({});

    const retrievePositionRiskEvaluation = async positionRiskEvaluationId => {
      try {
        const res = await positionRiskEvaluationService().find(positionRiskEvaluationId);
        positionRiskEvaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionRiskEvaluationId) {
      retrievePositionRiskEvaluation(route.params.positionRiskEvaluationId);
    }

    return {
      alertService,
      positionRiskEvaluation,

      ...dataUtils,

      previousState,
    };
  },
});
