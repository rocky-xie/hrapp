import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { SelectionActivityType } from '@/shared/model/enumerations/selection-activity-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';
import { type ISelectionActivity, SelectionActivity } from '@/shared/model/selection-activity.model';

import SelectionActivityService from './selection-activity.service';

export default defineComponent({
  name: 'SelectionActivityUpdate',
  setup() {
    const selectionActivityService = inject('selectionActivityService', () => new SelectionActivityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const selectionActivity: Ref<ISelectionActivity> = ref(new SelectionActivity());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const applicationStageTypeValues: Ref<string[]> = ref(Object.keys(ApplicationStageType));
    const selectionActivityTypeValues: Ref<string[]> = ref(Object.keys(SelectionActivityType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSelectionActivity = async selectionActivityId => {
      try {
        const res = await selectionActivityService().find(selectionActivityId);
        res.startAt = new Date(res.startAt);
        res.endAt = new Date(res.endAt);
        selectionActivity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.selectionActivityId) {
      retrieveSelectionActivity(route.params.selectionActivityId);
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
      stageType: {
        required: validations.required('This field is required.'),
      },
      activityType: {
        required: validations.required('This field is required.'),
      },
      startAt: {
        required: validations.required('This field is required.'),
      },
      endAt: {
        required: validations.required('This field is required.'),
      },
      meetingRoomId: {},
      location: {
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      status: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      scheduleVersion: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      changeReason: {},
      externalCalendarProvider: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      externalEventId: {
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      application: {},
    };
    const v$ = useVuelidate(validationRules, selectionActivity as any);
    v$.value.$validate();

    return {
      selectionActivityService,
      alertService,
      selectionActivity,
      previousState,
      applicationStageTypeValues,
      selectionActivityTypeValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: selectionActivity }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.selectionActivity.id) {
        this.selectionActivityService()
          .update(this.selectionActivity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SelectionActivity is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.selectionActivityService()
          .create(this.selectionActivity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SelectionActivity is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
