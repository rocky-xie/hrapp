import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { CandidateProfile, type ICandidateProfile } from '@/shared/model/candidate-profile.model';
import { CandidateJudgement } from '@/shared/model/enumerations/candidate-judgement.model';
import { ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

import CandidateProfileService from './candidate-profile.service';

export default defineComponent({
  name: 'CandidateProfileUpdate',
  setup() {
    const candidateProfileService = inject('candidateProfileService', () => new CandidateProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const candidateProfile: Ref<ICandidateProfile> = ref(new CandidateProfile());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const importanceLevelValues: Ref<string[]> = ref(Object.keys(ImportanceLevel));
    const candidateJudgementValues: Ref<string[]> = ref(Object.keys(CandidateJudgement));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCandidateProfile = async candidateProfileId => {
      try {
        const res = await candidateProfileService().find(candidateProfileId);
        candidateProfile.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.candidateProfileId) {
      retrieveCandidateProfile(route.params.candidateProfileId);
    }

    const initRelationships = () => {
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      candidateDate: {
        required: validations.required('This field is required.'),
      },
      cultivateDirection: {
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      stability: {},
      learningAbility: {},
      communicationCoordination: {},
      businessUnderstanding: {},
      responsibility: {},
      riskAwareness: {},
      judgement: {
        required: validations.required('This field is required.'),
      },
      evidence: {},
      person: {
        required: validations.required('This field is required.'),
      },
      position: {},
      observer: {},
    };
    const v$ = useVuelidate(validationRules, candidateProfile as any);
    v$.value.$validate();

    return {
      candidateProfileService,
      alertService,
      candidateProfile,
      previousState,
      importanceLevelValues,
      candidateJudgementValues,
      isSaving,
      currentLanguage,
      people,
      positions,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.candidateProfile.id) {
        this.candidateProfileService()
          .update(this.candidateProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A CandidateProfile is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.candidateProfileService()
          .create(this.candidateProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A CandidateProfile is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
