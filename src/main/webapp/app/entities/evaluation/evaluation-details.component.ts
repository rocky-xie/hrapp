import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IEvaluation } from '@/shared/model/evaluation.model';

import EvaluationService from './evaluation.service';

export default defineComponent({
  name: 'EvaluationDetails',
  setup() {
    const evaluationService = inject('evaluationService', () => new EvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const evaluation: Ref<IEvaluation> = ref({});

    const retrieveEvaluation = async evaluationId => {
      try {
        const res = await evaluationService().find(evaluationId);
        evaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.evaluationId) {
      retrieveEvaluation(route.params.evaluationId);
    }

    return {
      alertService,
      evaluation,

      ...dataUtils,

      previousState,
    };
  },
});
