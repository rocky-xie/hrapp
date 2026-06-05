import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionService from '@/entities/position/position.service';
import TrainingGoalService from '@/entities/training-goal/training-goal.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { AssessmentResult } from '@/shared/model/enumerations/assessment-result.model';
import { ProgressStatus } from '@/shared/model/enumerations/progress-status.model';
import { Evaluation, type IEvaluation } from '@/shared/model/evaluation.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ITrainingGoal } from '@/shared/model/training-goal.model';

import EvaluationService from './evaluation.service';

export default defineComponent({
  name: 'EvaluationUpdate',
  setup() {
    const evaluationService = inject('evaluationService', () => new EvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const evaluation: Ref<IEvaluation> = ref(new Evaluation());

    const personService = inject('personService', () => new PersonService());

    const people: Ref<IPerson[]> = ref([]);

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const trainingGoalService = inject('trainingGoalService', () => new TrainingGoalService());

    const trainingGoals: Ref<ITrainingGoal[]> = ref([]);
    const progressStatusValues: Ref<string[]> = ref(Object.keys(ProgressStatus));
    const assessmentResultValues: Ref<string[]> = ref(Object.keys(AssessmentResult));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEvaluation = async evaluationId => {
      try {
        const res = await evaluationService().find(evaluationId);
        evaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.evaluationId) {
      retrieveEvaluation(route.params.evaluationId);
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
      trainingGoalService()
        .retrieve()
        .then(res => {
          trainingGoals.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      evaluationName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      evaluationDate: {
        required: validations.required('This field is required.'),
      },
      periodLabel: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      progressStatus: {},
      result: {},
      strengths: {},
      weaknesses: {},
      supportNeeded: {},
      nextTrainingFocus: {},
      positionAdjustmentNeeded: {},
      person: {
        required: validations.required('This field is required.'),
      },
      position: {},
      trainingGoal: {},
      evaluator: {},
    };
    const v$ = useVuelidate(validationRules, evaluation as any);
    v$.value.$validate();

    return {
      evaluationService,
      alertService,
      evaluation,
      previousState,
      progressStatusValues,
      assessmentResultValues,
      isSaving,
      currentLanguage,
      people,
      positions,
      trainingGoals,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.evaluation.id) {
        this.evaluationService()
          .update(this.evaluation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Evaluation is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.evaluationService()
          .create(this.evaluation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Evaluation is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
