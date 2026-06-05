import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { DocumentStatus } from '@/shared/model/enumerations/document-status.model';
import { ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type IPositionRiskEvaluation, PositionRiskEvaluation } from '@/shared/model/position-risk-evaluation.model';
import { type IPosition } from '@/shared/model/position.model';

import PositionRiskEvaluationService from './position-risk-evaluation.service';

export default defineComponent({
  name: 'PositionRiskEvaluationUpdate',
  setup() {
    const positionRiskEvaluationService = inject('positionRiskEvaluationService', () => new PositionRiskEvaluationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const positionRiskEvaluation: Ref<IPositionRiskEvaluation> = ref(new PositionRiskEvaluation());

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);
    const documentStatusValues: Ref<string[]> = ref(Object.keys(DocumentStatus));
    const importanceLevelValues: Ref<string[]> = ref(Object.keys(ImportanceLevel));
    const readinessLevelValues: Ref<string[]> = ref(Object.keys(ReadinessLevel));
    const riskLevelValues: Ref<string[]> = ref(Object.keys(RiskLevel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePositionRiskEvaluation = async positionRiskEvaluationId => {
      try {
        const res = await positionRiskEvaluationService().find(positionRiskEvaluationId);
        positionRiskEvaluation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionRiskEvaluationId) {
      retrievePositionRiskEvaluation(route.params.positionRiskEvaluationId);
    }

    const initRelationships = () => {
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
      evaluationDate: {},
      ownerCount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      substitutableOwnerCount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      hasSubstitute: {},
      documentStatus: {},
      customerOrSystemDependency: {},
      successionReadiness: {},
      riskLevel: {},
      riskReason: {},
      recommendedAction: {},
      position: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, positionRiskEvaluation as any);
    v$.value.$validate();

    return {
      positionRiskEvaluationService,
      alertService,
      positionRiskEvaluation,
      previousState,
      documentStatusValues,
      importanceLevelValues,
      readinessLevelValues,
      riskLevelValues,
      isSaving,
      currentLanguage,
      positions,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    clearCalculatedFields(): void {
      this.positionRiskEvaluation.evaluationDate = undefined;
      this.positionRiskEvaluation.ownerCount = undefined;
      this.positionRiskEvaluation.substitutableOwnerCount = undefined;
      this.positionRiskEvaluation.hasSubstitute = false;
      this.positionRiskEvaluation.riskLevel = undefined;
      this.positionRiskEvaluation.riskReason = null;
      this.positionRiskEvaluation.recommendedAction = null;
    },
    triggerEvaluate(preview: boolean = true): void {
      const positionId = this.positionRiskEvaluation.position?.id;
      if (!positionId) {
        return;
      }
      this.positionRiskEvaluationService()
        .evaluate(
          positionId,
          this.positionRiskEvaluation.documentStatus,
          this.positionRiskEvaluation.customerOrSystemDependency,
          this.positionRiskEvaluation.successionReadiness,
          preview,
        )
        .then(res => {
          this.positionRiskEvaluation.evaluationDate = res.evaluationDate;
          this.positionRiskEvaluation.ownerCount = res.ownerCount;
          this.positionRiskEvaluation.substitutableOwnerCount = res.substitutableOwnerCount;
          this.positionRiskEvaluation.hasSubstitute = res.hasSubstitute;
          this.positionRiskEvaluation.riskLevel = res.riskLevel;
          this.positionRiskEvaluation.riskReason = res.riskReason;
          this.positionRiskEvaluation.recommendedAction = res.recommendedAction;
          if (res.id) {
            this.positionRiskEvaluation.id = res.id;
          }
        })
        .catch(error => {});
    },
    onEvaluationInputChange(): void {
      this.clearCalculatedFields();
      this.triggerEvaluate(true);
    },
    onPositionChange(): void {
      this.clearCalculatedFields();
    },
    save(): void {
      this.isSaving = true;
      if (this.positionRiskEvaluation.id) {
        this.positionRiskEvaluationService()
          .update(this.positionRiskEvaluation)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PositionRiskEvaluation is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        const positionId = this.positionRiskEvaluation.position?.id;
        if (!positionId) {
          this.isSaving = false;
          return;
        }

        this.positionRiskEvaluationService()
          .evaluate(
            positionId,
            this.positionRiskEvaluation.documentStatus,
            this.positionRiskEvaluation.customerOrSystemDependency,
            this.positionRiskEvaluation.successionReadiness,
          )
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PositionRiskEvaluation is evaluated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
