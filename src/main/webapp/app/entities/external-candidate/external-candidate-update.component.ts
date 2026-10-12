import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ExternalCandidate, type IExternalCandidate } from '@/shared/model/external-candidate.model';

import ExternalCandidateService from './external-candidate.service';

export default defineComponent({
  name: 'ExternalCandidateUpdate',
  setup() {
    const externalCandidateService = inject('externalCandidateService', () => new ExternalCandidateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalCandidate: Ref<IExternalCandidate> = ref(new ExternalCandidate());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveExternalCandidate = async externalCandidateId => {
      try {
        const res = await externalCandidateService().find(externalCandidateId);
        res.createdAt = new Date(res.createdAt);
        res.updatedAt = new Date(res.updatedAt);
        externalCandidate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.externalCandidateId) {
      retrieveExternalCandidate(route.params.externalCandidateId);
    }

    const initRelationships = () => {};

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      fullName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      nameReading: {
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      englishName: {
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      birthDate: {},
      gender: {
        maxLength: validations.maxLength('This field cannot be longer than 30 characters.', 30),
      },
      phone: {
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      email: {
        maxLength: validations.maxLength('This field cannot be longer than 254 characters.', 254),
      },
      address: {},
      educationSummary: {},
      workSummary: {},
      convertedPersonId: {},
      createdAt: {
        required: validations.required('This field is required.'),
      },
      updatedAt: {},
      resumeses: {},
      applicationses: {},
    };
    const v$ = useVuelidate(validationRules, externalCandidate as any);
    v$.value.$validate();

    return {
      externalCandidateService,
      alertService,
      externalCandidate,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: externalCandidate }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.externalCandidate.id) {
        this.externalCandidateService()
          .update(this.externalCandidate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ExternalCandidate is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.externalCandidateService()
          .create(this.externalCandidate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ExternalCandidate is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
