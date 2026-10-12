import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import DispatchAssignmentService from '@/entities/dispatch-assignment/dispatch-assignment.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IDispatchAssignment } from '@/shared/model/dispatch-assignment.model';
import { DispatchReview, type IDispatchReview } from '@/shared/model/dispatch-review.model';

import DispatchReviewService from './dispatch-review.service';

export default defineComponent({
  name: 'DispatchReviewUpdate',
  setup() {
    const dispatchReviewService = inject('dispatchReviewService', () => new DispatchReviewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dispatchReview: Ref<IDispatchReview> = ref(new DispatchReview());

    const dispatchAssignmentService = inject('dispatchAssignmentService', () => new DispatchAssignmentService());

    const dispatchAssignments: Ref<IDispatchAssignment[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDispatchReview = async dispatchReviewId => {
      try {
        const res = await dispatchReviewService().find(dispatchReviewId);
        res.reviewedAt = new Date(res.reviewedAt);
        dispatchReview.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dispatchReviewId) {
      retrieveDispatchReview(route.params.dispatchReviewId);
    }

    const initRelationships = () => {
      dispatchAssignmentService()
        .retrieve()
        .then(res => {
          dispatchAssignments.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      reviewedAt: {
        required: validations.required('This field is required.'),
      },
      reviewerUsername: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      result: {},
      recommendation: {
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      extensionEndDate: {},
      finalDecision: {
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      dispatchAssignment: {},
    };
    const v$ = useVuelidate(validationRules, dispatchReview as any);
    v$.value.$validate();

    return {
      dispatchReviewService,
      alertService,
      dispatchReview,
      previousState,
      isSaving,
      currentLanguage,
      dispatchAssignments,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: dispatchReview }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dispatchReview.id) {
        this.dispatchReviewService()
          .update(this.dispatchReview)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A DispatchReview is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dispatchReviewService()
          .create(this.dispatchReview)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A DispatchReview is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
