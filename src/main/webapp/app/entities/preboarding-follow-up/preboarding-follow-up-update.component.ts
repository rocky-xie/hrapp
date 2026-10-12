import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPreboardingFollowUp, PreboardingFollowUp } from '@/shared/model/preboarding-follow-up.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import PreboardingFollowUpService from './preboarding-follow-up.service';

export default defineComponent({
  name: 'PreboardingFollowUpUpdate',
  setup() {
    const preboardingFollowUpService = inject('preboardingFollowUpService', () => new PreboardingFollowUpService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const preboardingFollowUp: Ref<IPreboardingFollowUp> = ref(new PreboardingFollowUp());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePreboardingFollowUp = async preboardingFollowUpId => {
      try {
        const res = await preboardingFollowUpService().find(preboardingFollowUpId);
        res.plannedAt = new Date(res.plannedAt);
        res.contactedAt = new Date(res.contactedAt);
        res.nextContactAt = new Date(res.nextContactAt);
        preboardingFollowUp.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.preboardingFollowUpId) {
      retrievePreboardingFollowUp(route.params.preboardingFollowUpId);
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
      plannedAt: {},
      contactedAt: {},
      ownerUsername: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      contactMethod: {
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      result: {},
      candidateIntent: {
        maxLength: validations.maxLength('This field cannot be longer than 40 characters.', 40),
      },
      riskLevel: {
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      nextAction: {},
      nextContactAt: {},
      application: {},
    };
    const v$ = useVuelidate(validationRules, preboardingFollowUp as any);
    v$.value.$validate();

    return {
      preboardingFollowUpService,
      alertService,
      preboardingFollowUp,
      previousState,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: preboardingFollowUp }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.preboardingFollowUp.id) {
        this.preboardingFollowUpService()
          .update(this.preboardingFollowUp)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PreboardingFollowUp is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.preboardingFollowUpService()
          .create(this.preboardingFollowUp)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PreboardingFollowUp is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
