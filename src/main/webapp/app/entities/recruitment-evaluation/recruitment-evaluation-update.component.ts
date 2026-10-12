import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';
import { type IRecruitmentEvaluation, RecruitmentEvaluation } from '@/shared/model/recruitment-evaluation.model';

import RecruitmentEvaluationService from './recruitment-evaluation.service';

export default defineComponent({
  name: 'RecruitmentEvaluationUpdate',
  setup() {
    const recruitmentEvaluationService = inject('recruitmentEvaluationService', () => new RecruitmentEvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const recruitmentEvaluation: Ref<IRecruitmentEvaluation> = ref(new RecruitmentEvaluation());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const applicationStageTypeValues: Ref<string[]> = ref(Object.keys(ApplicationStageType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRecruitmentEvaluation = async recruitmentEvaluationId => {
      try {
        const res = await recruitmentEvaluationService().find(recruitmentEvaluationId);
        res.evaluatedAt = new Date(res.evaluatedAt);
        res.submittedAt = new Date(res.submittedAt);
        res.publishedAt = new Date(res.publishedAt);
        recruitmentEvaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentEvaluationId) {
      retrieveRecruitmentEvaluation(route.params.recruitmentEvaluationId);
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
      activityId: {},
      templateVersionId: {},
      evaluatorUsername: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      recordedByUsername: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      status: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      overallRecommendation: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      totalScore: {
        min: validations.minValue('This field should be at least 0.', 0),
      },
      summary: {},
      answersJson: {},
      evaluatedAt: {},
      submittedAt: {},
      publishedAt: {},
      correctionOfId: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, recruitmentEvaluation as any);
    v$.value.$validate();

    return {
      recruitmentEvaluationService,
      alertService,
      recruitmentEvaluation,
      previousState,
      applicationStageTypeValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: recruitmentEvaluation }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.recruitmentEvaluation.id) {
        this.recruitmentEvaluationService()
          .update(this.recruitmentEvaluation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A RecruitmentEvaluation is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.recruitmentEvaluationService()
          .create(this.recruitmentEvaluation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A RecruitmentEvaluation is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
