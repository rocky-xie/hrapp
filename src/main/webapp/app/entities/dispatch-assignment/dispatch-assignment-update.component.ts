import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { DispatchAssignment, type IDispatchAssignment } from '@/shared/model/dispatch-assignment.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import DispatchAssignmentService from './dispatch-assignment.service';

export default defineComponent({
  name: 'DispatchAssignmentUpdate',
  setup() {
    const dispatchAssignmentService = inject('dispatchAssignmentService', () => new DispatchAssignmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dispatchAssignment: Ref<IDispatchAssignment> = ref(new DispatchAssignment());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDispatchAssignment = async dispatchAssignmentId => {
      try {
        const res = await dispatchAssignmentService().find(dispatchAssignmentId);
        dispatchAssignment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dispatchAssignmentId) {
      retrieveDispatchAssignment(route.params.dispatchAssignmentId);
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
      candidateId: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
      },
      personId: {},
      partnerOrganizationId: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
      },
      department: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      positionId: {},
      startDate: {},
      expectedEndDate: {},
      observationDays: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      status: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      finalDecision: {
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      note: {},
      reviewses: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, dispatchAssignment as any);
    v$.value.$validate();

    return {
      dispatchAssignmentService,
      alertService,
      dispatchAssignment,
      previousState,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dispatchAssignment.id) {
        this.dispatchAssignmentService()
          .update(this.dispatchAssignment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A DispatchAssignment is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dispatchAssignmentService()
          .create(this.dispatchAssignment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A DispatchAssignment is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
