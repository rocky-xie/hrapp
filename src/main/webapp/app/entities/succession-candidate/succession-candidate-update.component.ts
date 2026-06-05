import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISuccessionCandidate, SuccessionCandidate } from '@/shared/model/succession-candidate.model';

import SuccessionCandidateService from './succession-candidate.service';

export default defineComponent({
  name: 'SuccessionCandidateUpdate',
  setup() {
    const successionCandidateService = inject('successionCandidateService', () => new SuccessionCandidateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const successionCandidate: Ref<ISuccessionCandidate> = ref(new SuccessionCandidate());

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);
    const readinessLevelValues: Ref<string[]> = ref(Object.keys(ReadinessLevel));
    const riskLevelValues: Ref<string[]> = ref(Object.keys(RiskLevel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSuccessionCandidate = async successionCandidateId => {
      try {
        const res = await successionCandidateService().find(successionCandidateId);
        successionCandidate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.successionCandidateId) {
      retrieveSuccessionCandidate(route.params.successionCandidateId);
    }

    const initRelationships = () => {
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
    };

    initRelationships();

    const availableCurrentOwners = computed(() => {
      const candidateId = successionCandidate.value.candidate?.id;
      if (!candidateId) {
        return people.value;
      }
      return people.value.filter(person => person.id !== candidateId);
    });

    const availableCandidates = computed(() => {
      const currentOwnerId = successionCandidate.value.currentOwner?.id;
      if (!currentOwnerId) {
        return people.value;
      }
      return people.value.filter(person => person.id !== currentOwnerId);
    });

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      successionReadiness: {
        required: validations.required('This field is required.'),
      },
      requiredTraining: {},
      estimatedTimeToReady: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      riskAfterTraining: {},
      reviewDate: {},
      priority: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 1.', 1),
      },
      position: {
        required: validations.required('This field is required.'),
      },
      currentOwner: {},
      candidate: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, successionCandidate as any);
    v$.value.$validate();

    return {
      successionCandidateService,
      alertService,
      successionCandidate,
      previousState,
      readinessLevelValues,
      riskLevelValues,
      isSaving,
      currentLanguage,
      positions,
      people,
      availableCurrentOwners,
      availableCandidates,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      if (
        this.successionCandidate.currentOwner?.id &&
        this.successionCandidate.candidate?.id === this.successionCandidate.currentOwner.id
      ) {
        this.alertService.showError('Candidate cannot be the same person as current owner.');
        return;
      }
      this.isSaving = true;
      if (this.successionCandidate.id) {
        this.successionCandidateService()
          .update(this.successionCandidate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SuccessionCandidate is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.successionCandidateService()
          .create(this.successionCandidate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SuccessionCandidate is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
