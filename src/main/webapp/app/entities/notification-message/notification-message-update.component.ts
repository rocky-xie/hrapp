import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { NotificationMessageStatus } from '@/shared/model/enumerations/notification-message-status.model';
import { type INotificationMessage, NotificationMessage } from '@/shared/model/notification-message.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import NotificationMessageService from './notification-message.service';

export default defineComponent({
  name: 'NotificationMessageUpdate',
  setup() {
    const notificationMessageService = inject('notificationMessageService', () => new NotificationMessageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const notificationMessage: Ref<INotificationMessage> = ref(new NotificationMessage());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const notificationMessageStatusValues: Ref<string[]> = ref(Object.keys(NotificationMessageStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveNotificationMessage = async notificationMessageId => {
      try {
        const res = await notificationMessageService().find(notificationMessageId);
        res.createdAt = new Date(res.createdAt);
        res.confirmedAt = new Date(res.confirmedAt);
        res.sentAt = new Date(res.sentAt);
        notificationMessage.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.notificationMessageId) {
      retrieveNotificationMessage(route.params.notificationMessageId);
    }

    const initRelationships = () => {
      recruitmentApplicationService()
        .retrieve()
        .then(res => {
          recruitmentApplications.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      eventType: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 60 characters.', 60),
      },
      templateId: {},
      status: {
        required: validations.required('This field is required.'),
      },
      sendMode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      finalSubject: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      finalBody: {
        required: validations.required('This field is required.'),
      },
      recipientsJson: {
        required: validations.required('This field is required.'),
      },
      retryCount: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      maxRetries: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      lastError: {},
      createdBy: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      confirmedBy: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      createdAt: {
        required: validations.required('This field is required.'),
      },
      confirmedAt: {},
      sentAt: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, notificationMessage as any);
    v$.value.$validate();

    return {
      notificationMessageService,
      alertService,
      notificationMessage,
      previousState,
      notificationMessageStatusValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: notificationMessage }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.notificationMessage.id) {
        this.notificationMessageService()
          .update(this.notificationMessage)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A NotificationMessage is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.notificationMessageService()
          .create(this.notificationMessage)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A NotificationMessage is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
