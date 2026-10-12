import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
import { RequisitionEditStatus } from '@/shared/model/enumerations/requisition-edit-status.model';
import { RequisitionLifecycle } from '@/shared/model/enumerations/requisition-lifecycle.model';
import { type IRecruitmentRequisition, RecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentRequisitionService from './recruitment-requisition.service';

export default defineComponent({
  name: 'RecruitmentRequisitionUpdate',
  setup() {
    const recruitmentRequisitionService = inject('recruitmentRequisitionService', () => new RecruitmentRequisitionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const recruitmentRequisition: Ref<IRecruitmentRequisition> = ref(new RecruitmentRequisition());
    const recruitmentTypeValues: Ref<string[]> = ref(Object.keys(RecruitmentType));
    const requisitionLifecycleValues: Ref<string[]> = ref(Object.keys(RequisitionLifecycle));
    const requisitionEditStatusValues: Ref<string[]> = ref(Object.keys(RequisitionEditStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRecruitmentRequisition = async recruitmentRequisitionId => {
      try {
        const res = await recruitmentRequisitionService().find(recruitmentRequisitionId);
        res.lockedAt = new Date(res.lockedAt);
        res.unlockedAt = new Date(res.unlockedAt);
        recruitmentRequisition.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionId) {
      retrieveRecruitmentRequisition(route.params.recruitmentRequisitionId);
    }

    const initRelationships = () => {};

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      requisitionCode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      title: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      department: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      positionId: {},
      recruitmentType: {
        required: validations.required('This field is required.'),
      },
      lifecycle: {
        required: validations.required('This field is required.'),
      },
      editStatus: {
        required: validations.required('This field is required.'),
      },
      plannedHeadcount: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      hiredCount: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      checkedInCount: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      priority: {
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      reason: {},
      targetJoinDate: {},
      desiredStartDate: {},
      desiredEndDate: {},
      currentVersion: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      lockedAt: {},
      lockedBy: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      unlockedAt: {},
      unlockReason: {},
      requirementsJson: {},
      versionses: {},
      memberses: {},
      applicationses: {},
    };
    const v$ = useVuelidate(validationRules, recruitmentRequisition as any);
    v$.value.$validate();

    return {
      recruitmentRequisitionService,
      alertService,
      recruitmentRequisition,
      previousState,
      recruitmentTypeValues,
      requisitionLifecycleValues,
      requisitionEditStatusValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: recruitmentRequisition }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.recruitmentRequisition.id) {
        this.recruitmentRequisitionService()
          .update(this.recruitmentRequisition)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A RecruitmentRequisition is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.recruitmentRequisitionService()
          .create(this.recruitmentRequisition)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A RecruitmentRequisition is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
