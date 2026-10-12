import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import ExternalCandidateService from '@/entities/external-candidate/external-candidate.service';
import RecruitmentRequisitionService from '@/entities/recruitment-requisition/recruitment-requisition.service';
import RecruitmentRequisitionVersionService from '@/entities/recruitment-requisition-version/recruitment-requisition-version.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationDecision } from '@/shared/model/enumerations/application-decision.model';
import { ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { ApplicationStatus } from '@/shared/model/enumerations/application-status.model';
import { RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
import { type IExternalCandidate } from '@/shared/model/external-candidate.model';
import { type IRecruitmentApplication, RecruitmentApplication } from '@/shared/model/recruitment-application.model';
import { type IRecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';
import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentApplicationService from './recruitment-application.service';

export default defineComponent({
  name: 'RecruitmentApplicationUpdate',
  setup() {
    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const recruitmentApplication: Ref<IRecruitmentApplication> = ref(new RecruitmentApplication());

    const externalCandidateService = inject('externalCandidateService', () => new ExternalCandidateService());

    const externalCandidates: Ref<IExternalCandidate[]> = ref([]);

    const recruitmentRequisitionService = inject('recruitmentRequisitionService', () => new RecruitmentRequisitionService());

    const recruitmentRequisitions: Ref<IRecruitmentRequisition[]> = ref([]);
    const selectedRequisitionIds = ref<number[]>([]);
    const selectedRequisitionId = ref<number | null>(null);
    const availableRecruitmentRequisitions = computed(() =>
      (recruitmentRequisitions.value ?? []).filter(
        requisition => requisition.lifecycle === 'ACTIVE' && requisition.editStatus === 'LOCKED',
      ),
    );
    watch(selectedRequisitionId, id => {
      if (recruitmentApplication.value.recruitmentType !== 'GRADUATE') selectedRequisitionIds.value = id == null ? [] : [id];
    });
    watch(selectedRequisitionIds, ids => {
      if (recruitmentApplication.value.recruitmentType !== 'GRADUATE' && ids.length) selectedRequisitionId.value = ids[0];
    });
    watch(
      () => recruitmentApplication.value.recruitmentType,
      type => {
        if (type !== 'GRADUATE' && selectedRequisitionIds.value.length > 1) {
          selectedRequisitionIds.value = selectedRequisitionIds.value.slice(0, 1);
          selectedRequisitionId.value = selectedRequisitionIds.value[0] ?? null;
        } else if (type === 'GRADUATE' && selectedRequisitionIds.value.length === 0 && selectedRequisitionId.value != null) {
          selectedRequisitionIds.value = [selectedRequisitionId.value];
        }
      },
    );

    const recruitmentRequisitionVersionService = inject(
      'recruitmentRequisitionVersionService',
      () => new RecruitmentRequisitionVersionService(),
    );

    const recruitmentRequisitionVersions: Ref<IRecruitmentRequisitionVersion[]> = ref([]);
    const recruitmentTypeValues: Ref<string[]> = ref(Object.keys(RecruitmentType));
    const sourceTypeValues = computed(() =>
      recruitmentApplication.value.recruitmentType === 'GRADUATE' ? ['SCHOOL', 'AGENCY', 'WEBSITE', 'REFERRAL'] : ['AGENCY', 'WEBSITE'],
    );
    const applicationStageTypeValues: Ref<string[]> = ref(Object.keys(ApplicationStageType));
    const applicationStatusValues: Ref<string[]> = ref(Object.keys(ApplicationStatus));
    const applicationDecisionValues: Ref<string[]> = ref(Object.keys(ApplicationDecision));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRecruitmentApplication = async recruitmentApplicationId => {
      try {
        const res = await recruitmentApplicationService().find(recruitmentApplicationId);
        res.appliedAt = new Date(res.appliedAt);
        res.closedAt = new Date(res.closedAt);
        res.convertedAt = new Date(res.convertedAt);
        recruitmentApplication.value = res;
        selectedRequisitionIds.value = res.requisition?.id ? [res.requisition.id] : [];
        selectedRequisitionId.value = res.requisition?.id ?? null;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentApplicationId) {
      retrieveRecruitmentApplication(route.params.recruitmentApplicationId);
    }

    const initRelationships = () => {
      externalCandidateService()
        .retrieve()
        .then(res => {
          externalCandidates.value = res.data ?? [];
        });
      recruitmentRequisitionService()
        .retrieve()
        .then(res => {
          recruitmentRequisitions.value = res.data ?? [];
        });
      recruitmentRequisitionVersionService()
        .retrieve()
        .then(res => {
          recruitmentRequisitionVersions.value = res.data ?? [];
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      applicationCode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      recruitmentType: {
        required: validations.required('This field is required.'),
      },
      currentStage: {},
      status: {
        required: validations.required('This field is required.'),
      },
      decision: {},
      sourceType: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      sourceDetail: {
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      batchName: {
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      appliedAt: {
        required: validations.required('This field is required.'),
      },
      closedAt: {},
      retentionUntil: {},
      convertedPersonId: {},
      convertedAt: {},
      closeReason: {},
      note: {},
      stageses: {},
      eventses: {},
      activitieses: {},
      evaluationses: {},
      decisionses: {},
      messageses: {},
      followUpses: {},
      dispatchAssignmentses: {},
      candidate: {},
      requisition: {},
      requisitionVersion: {},
    };
    const v$ = useVuelidate(validationRules, recruitmentApplication as any);
    v$.value.$validate();

    return {
      recruitmentApplicationService,
      alertService,
      recruitmentApplication,
      previousState,
      recruitmentTypeValues,
      sourceTypeValues,
      applicationStageTypeValues,
      applicationStatusValues,
      applicationDecisionValues,
      isSaving,
      currentLanguage,
      externalCandidates,
      recruitmentRequisitions,
      availableRecruitmentRequisitions,
      selectedRequisitionIds,
      selectedRequisitionId,
      recruitmentRequisitionVersions,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: recruitmentApplication }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (!this.recruitmentApplication.id && this.selectedRequisitionIds.length > 0) {
        const selectedIds = [...new Set(this.selectedRequisitionIds)];
        const requisitions = this.recruitmentRequisitions.filter(requisition => selectedIds.includes(requisition.id!));
        const baseCode = this.recruitmentApplication.applicationCode ?? '';
        const createApplications = requisitions.map(requisition => {
          const suffix = `-P${requisition.id}`;
          const applicationCode = requisitions.length > 1 ? `${baseCode.slice(0, 40 - suffix.length)}${suffix}` : baseCode;
          const version = this.recruitmentRequisitionVersions
            .filter(item => item.requisition?.id === requisition.id)
            .sort((left, right) => (right.versionNumber ?? 0) - (left.versionNumber ?? 0))[0];
          return this.recruitmentApplicationService().create({
            ...this.recruitmentApplication,
            applicationCode,
            requisition,
            requisitionVersion: version ?? null,
          });
        });
        Promise.all(createApplications)
          .then(() => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.$t('recruitment.application.createdPositionApplications'));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
        return;
      }
      if (this.recruitmentApplication.id) {
        this.recruitmentApplicationService()
          .update(this.recruitmentApplication)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A RecruitmentApplication is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.recruitmentApplicationService()
          .create(this.recruitmentApplication)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A RecruitmentApplication is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
