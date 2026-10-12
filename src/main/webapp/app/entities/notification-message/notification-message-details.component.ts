import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type INotificationMessage } from '@/shared/model/notification-message.model';

import NotificationMessageService from './notification-message.service';

export default defineComponent({
  name: 'NotificationMessageDetails',
  setup() {
    const dateFormat = useDateFormat();
    const notificationMessageService = inject('notificationMessageService', () => new NotificationMessageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const notificationMessage: Ref<INotificationMessage> = ref({});

    const retrieveNotificationMessage = async notificationMessageId => {
      try {
        const res = await notificationMessageService().find(notificationMessageId);
        notificationMessage.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.notificationMessageId) {
      retrieveNotificationMessage(route.params.notificationMessageId);
    }

    return {
      ...dateFormat,
      alertService,
      notificationMessage,

      ...dataUtils,

      previousState,
    };
  },
});
