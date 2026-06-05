import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ITrainingGoal } from '@/shared/model/training-goal.model';

import TrainingGoalService from './training-goal.service';

export default defineComponent({
  name: 'TrainingGoalDetails',
  setup() {
    const trainingGoalService = inject('trainingGoalService', () => new TrainingGoalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const trainingGoal: Ref<ITrainingGoal> = ref({});

    const retrieveTrainingGoal = async trainingGoalId => {
      try {
        const res = await trainingGoalService().find(trainingGoalId);
        trainingGoal.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const completeTrainingGoal = async () => {
      if (!trainingGoal.value.id) return;
      try {
        const res = await trainingGoalService().complete(trainingGoal.value.id);
        trainingGoal.value = res;
        alertService.showInfo('A TrainingGoal is completed with identifier ' + res.id, { variant: 'success' });
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.trainingGoalId) {
      retrieveTrainingGoal(route.params.trainingGoalId);
    }

    return {
      alertService,
      trainingGoal,
      completeTrainingGoal,

      ...dataUtils,

      previousState,
    };
  },
});
