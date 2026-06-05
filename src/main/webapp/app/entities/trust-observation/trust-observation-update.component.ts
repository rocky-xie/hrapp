import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { TrustStage } from '@/shared/model/enumerations/trust-stage.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ITrustObservation, TrustObservation } from '@/shared/model/trust-observation.model';

import TrustObservationService from './trust-observation.service';

export default defineComponent({
  name: 'TrustObservationUpdate',
  setup() {
    const trustObservationService = inject('trustObservationService', () => new TrustObservationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const trustObservation: Ref<ITrustObservation> = ref(new TrustObservation());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);
    const trustStageValues: Ref<string[]> = ref(Object.keys(TrustStage));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTrustObservation = async trustObservationId => {
      try {
        const res = await trustObservationService().find(trustObservationId);
        trustObservation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.trustObservationId) {
      retrieveTrustObservation(route.params.trustObservationId);
    }

    const initRelationships = () => {
      personService()
        .retrieve()
        .then(res => {
          people.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      observationDate: {
        required: validations.required('This field is required.'),
      },
      trustStage: {
        required: validations.required('This field is required.'),
      },
      observedBehavior: {},
      positiveSignal: {},
      riskSignal: {},
      nextObservationPoint: {},
      person: {
        required: validations.required('This field is required.'),
      },
      observer: {},
    };
    const v$ = useVuelidate(validationRules, trustObservation as any);
    v$.value.$validate();

    return {
      trustObservationService,
      alertService,
      trustObservation,
      previousState,
      trustStageValues,
      isSaving,
      currentLanguage,
      people,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.trustObservation.id) {
        this.trustObservationService()
          .update(this.trustObservation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A TrustObservation is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.trustObservationService()
          .create(this.trustObservation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A TrustObservation is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
