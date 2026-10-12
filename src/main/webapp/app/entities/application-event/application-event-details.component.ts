import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IApplicationEvent } from '@/shared/model/application-event.model';

import ApplicationEventService from './application-event.service';

export default defineComponent({
  name: 'ApplicationEventDetails',
  setup() {
    const dateFormat = useDateFormat();
    const applicationEventService = inject('applicationEventService', () => new ApplicationEventService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const applicationEvent: Ref<IApplicationEvent> = ref({});

    const retrieveApplicationEvent = async applicationEventId => {
      try {
        const res = await applicationEventService().find(applicationEventId);
        applicationEvent.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationEventId) {
      retrieveApplicationEvent(route.params.applicationEventId);
    }

    return {
      ...dateFormat,
      alertService,
      applicationEvent,

      ...dataUtils,

      previousState,
    };
  },
});
