import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationStage, type IApplicationStage } from '@/shared/model/application-stage.model';
import { ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import ApplicationStageService from './application-stage.service';

export default defineComponent({
  name: 'ApplicationStageUpdate',
  setup() {
    const applicationStageService = inject('applicationStageService', () => new ApplicationStageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationStage: Ref<IApplicationStage> = ref(new ApplicationStage());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const applicationStageTypeValues: Ref<string[]> = ref(Object.keys(ApplicationStageType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveApplicationStage = async applicationStageId => {
      try {
        const res = await applicationStageService().find(applicationStageId);
        res.startedAt = new Date(res.startedAt);
        res.completedAt = new Date(res.completedAt);
        applicationStage.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationStageId) {
      retrieveApplicationStage(route.params.applicationStageId);
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
      sequenceNumber: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      stageStatus: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      startedAt: {},
      completedAt: {},
      selected: {},
      decisionReason: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, applicationStage as any);
    v$.value.$validate();

    return {
      applicationStageService,
      alertService,
      applicationStage,
      previousState,
      applicationStageTypeValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: applicationStage }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.applicationStage.id) {
        this.applicationStageService()
          .update(this.applicationStage)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ApplicationStage is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.applicationStageService()
          .create(this.applicationStage)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ApplicationStage is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
