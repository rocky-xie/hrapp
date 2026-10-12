import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
import { type INotificationTemplate, NotificationTemplate } from '@/shared/model/notification-template.model';

import NotificationTemplateService from './notification-template.service';

export default defineComponent({
  name: 'NotificationTemplateUpdate',
  setup() {
    const notificationTemplateService = inject('notificationTemplateService', () => new NotificationTemplateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const notificationTemplate: Ref<INotificationTemplate> = ref(new NotificationTemplate());
    const recruitmentTypeValues: Ref<string[]> = ref(Object.keys(RecruitmentType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      eventType: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 60 characters.', 60),
      },
      recruitmentType: {},
      language: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 10 characters.', 10),
      },
      versionNumber: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      subjectTemplate: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      bodyTemplate: {
        required: validations.required('This field is required.'),
      },
      externalSafe: {
        required: validations.required('This field is required.'),
      },
      published: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, notificationTemplate as any);
    v$.value.$validate();

    return {
      notificationTemplateService,
      alertService,
      notificationTemplate,
      previousState,
      recruitmentTypeValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.notificationTemplate.id) {
        this.notificationTemplateService()
          .update(this.notificationTemplate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A NotificationTemplate is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.notificationTemplateService()
          .create(this.notificationTemplate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A NotificationTemplate is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
