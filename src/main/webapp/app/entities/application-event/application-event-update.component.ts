import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationEvent, type IApplicationEvent } from '@/shared/model/application-event.model';
import { ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import ApplicationEventService from './application-event.service';

export default defineComponent({
  name: 'ApplicationEventUpdate',
  setup() {
    const applicationEventService = inject('applicationEventService', () => new ApplicationEventService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationEvent: Ref<IApplicationEvent> = ref(new ApplicationEvent());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const applicationStageTypeValues: Ref<string[]> = ref(Object.keys(ApplicationStageType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveApplicationEvent = async applicationEventId => {
      try {
        const res = await applicationEventService().find(applicationEventId);
        res.occurredAt = new Date(res.occurredAt);
        applicationEvent.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationEventId) {
      retrieveApplicationEvent(route.params.applicationEventId);
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
      stageType: {},
      eventType: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      previousValue: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      newValue: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      actor: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      occurredAt: {
        required: validations.required('This field is required.'),
      },
      reason: {},
      metadataJson: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, applicationEvent as any);
    v$.value.$validate();

    return {
      applicationEventService,
      alertService,
      applicationEvent,
      previousState,
      applicationStageTypeValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: applicationEvent }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.applicationEvent.id) {
        this.applicationEventService()
          .update(this.applicationEvent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ApplicationEvent is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.applicationEventService()
          .create(this.applicationEvent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ApplicationEvent is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
