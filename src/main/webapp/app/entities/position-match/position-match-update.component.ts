import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { Recommendation } from '@/shared/model/enumerations/recommendation.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPositionMatch, PositionMatch } from '@/shared/model/position-match.model';
import { type IPosition } from '@/shared/model/position.model';

import PositionMatchService from './position-match.service';

export default defineComponent({
  name: 'PositionMatchUpdate',
  setup() {
    const positionMatchService = inject('positionMatchService', () => new PositionMatchService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const positionMatch: Ref<IPositionMatch> = ref(new PositionMatch());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const readinessLevelValues: Ref<string[]> = ref(Object.keys(ReadinessLevel));
    const recommendationValues: Ref<string[]> = ref(Object.keys(Recommendation));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePositionMatch = async positionMatchId => {
      try {
        const res = await positionMatchService().find(positionMatchId);
        positionMatch.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionMatchId) {
      retrievePositionMatch(route.params.positionMatchId);
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
      matchScore: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
        max: validations.maxValue('This field cannot be more than 100.', 100),
      },
      matchedSkills: {},
      gapSkills: {},
      readiness: {
        required: validations.required('This field is required.'),
      },
      recommendation: {
        required: validations.required('This field is required.'),
      },
      analysisDate: {
        required: validations.required('This field is required.'),
      },
      remark: {},
      person: {
        required: validations.required('This field is required.'),
      },
      position: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, positionMatch as any);
    v$.value.$validate();

    return {
      positionMatchService,
      alertService,
      positionMatch,
      previousState,
      readinessLevelValues,
      recommendationValues,
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
      if (this.positionMatch.id) {
        this.positionMatchService()
          .update(this.positionMatch)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PositionMatch is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.positionMatchService()
          .create(this.positionMatch)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PositionMatch is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
