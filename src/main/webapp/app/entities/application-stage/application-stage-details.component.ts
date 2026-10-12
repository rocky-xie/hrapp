import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IApplicationStage } from '@/shared/model/application-stage.model';

import ApplicationStageService from './application-stage.service';

export default defineComponent({
  name: 'ApplicationStageDetails',
  setup() {
    const dateFormat = useDateFormat();
    const applicationStageService = inject('applicationStageService', () => new ApplicationStageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const applicationStage: Ref<IApplicationStage> = ref({});

    const retrieveApplicationStage = async applicationStageId => {
      try {
        const res = await applicationStageService().find(applicationStageId);
        applicationStage.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationStageId) {
      retrieveApplicationStage(route.params.applicationStageId);
    }

    return {
      ...dateFormat,
      alertService,
      applicationStage,

      ...dataUtils,

      previousState,
    };
  },
});
