import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import ExternalCandidateService from '@/entities/external-candidate/external-candidate.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { CandidateResume, type ICandidateResume } from '@/shared/model/candidate-resume.model';
import { type IExternalCandidate } from '@/shared/model/external-candidate.model';

import CandidateResumeService from './candidate-resume.service';

export default defineComponent({
  name: 'CandidateResumeUpdate',
  setup() {
    const candidateResumeService = inject('candidateResumeService', () => new CandidateResumeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const candidateResume: Ref<ICandidateResume> = ref(new CandidateResume());

    const externalCandidateService = inject('externalCandidateService', () => new ExternalCandidateService());

    const externalCandidates: Ref<IExternalCandidate[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCandidateResume = async candidateResumeId => {
      try {
        const res = await candidateResumeService().find(candidateResumeId);
        res.receivedAt = new Date(res.receivedAt);
        candidateResume.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.candidateResumeId) {
      retrieveCandidateResume(route.params.candidateResumeId);
    }

    const initRelationships = () => {
      externalCandidateService()
        .retrieve()
        .then(res => {
          externalCandidates.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      versionNumber: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      receivedAt: {
        required: validations.required('This field is required.'),
      },
      originalFileName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 255 characters.', 255),
      },
      mediaType: {
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      fileSize: {
        required: validations.required('This field is required.'),
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      sha256: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 64 characters.', 64),
      },
      storageKey: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 120 characters.', 120),
      },
      structuredSummary: {},
      activeVersion: {
        required: validations.required('This field is required.'),
      },
      uploadedBy: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      candidate: {},
    };
    const v$ = useVuelidate(validationRules, candidateResume as any);
    v$.value.$validate();

    return {
      candidateResumeService,
      alertService,
      candidateResume,
      previousState,
      isSaving,
      currentLanguage,
      externalCandidates,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: candidateResume }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.candidateResume.id) {
        this.candidateResumeService()
          .update(this.candidateResume)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A CandidateResume is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.candidateResumeService()
          .create(this.candidateResume)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A CandidateResume is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
