import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IRecruitmentEvaluation } from '@/shared/model/recruitment-evaluation.model';

import RecruitmentEvaluationService from './recruitment-evaluation.service';

export default defineComponent({
  name: 'RecruitmentEvaluationDetails',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentEvaluationService = inject('recruitmentEvaluationService', () => new RecruitmentEvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const recruitmentEvaluation: Ref<IRecruitmentEvaluation> = ref({});

    const retrieveRecruitmentEvaluation = async recruitmentEvaluationId => {
      try {
        const res = await recruitmentEvaluationService().find(recruitmentEvaluationId);
        recruitmentEvaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentEvaluationId) {
      retrieveRecruitmentEvaluation(route.params.recruitmentEvaluationId);
    }

    return {
      ...dateFormat,
      alertService,
      recruitmentEvaluation,

      ...dataUtils,

      previousState,
    };
  },
});
