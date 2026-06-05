import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ITrainingRecord } from '@/shared/model/training-record.model';

import TrainingRecordService from './training-record.service';

export default defineComponent({
  name: 'TrainingRecordDetails',
  setup() {
    const trainingRecordService = inject('trainingRecordService', () => new TrainingRecordService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const trainingRecord: Ref<ITrainingRecord> = ref({});

    const retrieveTrainingRecord = async trainingRecordId => {
      try {
        const res = await trainingRecordService().find(trainingRecordId);
        trainingRecord.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.trainingRecordId) {
      retrieveTrainingRecord(route.params.trainingRecordId);
    }

    return {
      alertService,
      trainingRecord,

      ...dataUtils,

      previousState,
    };
  },
});
