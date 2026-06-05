import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IImprovementPlan } from '@/shared/model/improvement-plan.model';

import ImprovementPlanService from './improvement-plan.service';

export default defineComponent({
  name: 'ImprovementPlanDetails',
  setup() {
    const improvementPlanService = inject('improvementPlanService', () => new ImprovementPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const improvementPlan: Ref<IImprovementPlan> = ref({});

    const retrieveImprovementPlan = async improvementPlanId => {
      try {
        const res = await improvementPlanService().find(improvementPlanId);
        improvementPlan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.improvementPlanId) {
      retrieveImprovementPlan(route.params.improvementPlanId);
    }

    return {
      alertService,
      improvementPlan,

      ...dataUtils,

      previousState,
    };
  },
});
