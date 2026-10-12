import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import RecruitmentApplicationService from '@/entities/recruitment-application/recruitment-application.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ApplicationDecision } from '@/shared/model/enumerations/application-decision.model';
import { HiringDecision, type IHiringDecision } from '@/shared/model/hiring-decision.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import HiringDecisionService from './hiring-decision.service';

export default defineComponent({
  name: 'HiringDecisionUpdate',
  setup() {
    const hiringDecisionService = inject('hiringDecisionService', () => new HiringDecisionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const hiringDecision: Ref<IHiringDecision> = ref(new HiringDecision());

    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());

    const recruitmentApplications: Ref<IRecruitmentApplication[]> = ref([]);
    const applicationDecisionValues: Ref<string[]> = ref(Object.keys(ApplicationDecision));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveHiringDecision = async hiringDecisionId => {
      try {
        const res = await hiringDecisionService().find(hiringDecisionId);
        res.decidedAt = new Date(res.decidedAt);
        hiringDecision.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.hiringDecisionId) {
      retrieveHiringDecision(route.params.hiringDecisionId);
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
      decision: {
        required: validations.required('This field is required.'),
      },
      decisionSource: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      decidedBy: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      decidedAt: {
        required: validations.required('This field is required.'),
      },
      reason: {},
      correctionOfId: {},
      active: {
        required: validations.required('This field is required.'),
      },
      application: {},
    };
    const v$ = useVuelidate(validationRules, hiringDecision as any);
    v$.value.$validate();

    return {
      hiringDecisionService,
      alertService,
      hiringDecision,
      previousState,
      applicationDecisionValues,
      isSaving,
      currentLanguage,
      recruitmentApplications,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: hiringDecision }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.hiringDecision.id) {
        this.hiringDecisionService()
          .update(this.hiringDecision)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A HiringDecision is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.hiringDecisionService()
          .create(this.hiringDecision)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A HiringDecision is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
