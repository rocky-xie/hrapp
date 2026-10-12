import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type INotificationTemplate } from '@/shared/model/notification-template.model';

import NotificationTemplateService from './notification-template.service';

export default defineComponent({
  name: 'NotificationTemplateDetails',
  setup() {
    const notificationTemplateService = inject('notificationTemplateService', () => new NotificationTemplateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const notificationTemplate: Ref<INotificationTemplate> = ref({});

    const retrieveNotificationTemplate = async notificationTemplateId => {
      try {
        const res = await notificationTemplateService().find(notificationTemplateId);
        notificationTemplate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.notificationTemplateId) {
      retrieveNotificationTemplate(route.params.notificationTemplateId);
    }

    return {
      alertService,
      notificationTemplate,

      ...dataUtils,

      previousState,
    };
  },
});
