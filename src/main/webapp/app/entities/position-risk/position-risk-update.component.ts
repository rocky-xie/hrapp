import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import KeyResponsibilityCategoryService from '@/entities/key-responsibility-category/key-responsibility-category.service';
import PositionService from '@/entities/position/position.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { BackupStatus } from '@/shared/model/enumerations/backup-status.model';
import { DocumentStatus } from '@/shared/model/enumerations/document-status.model';
import { ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { RiskType } from '@/shared/model/enumerations/risk-type.model';
import { type IKeyResponsibilityCategory } from '@/shared/model/key-responsibility-category.model';
import { type IPositionRisk, PositionRisk } from '@/shared/model/position-risk.model';
import { type IPosition } from '@/shared/model/position.model';

import PositionRiskService from './position-risk.service';

export default defineComponent({
  name: 'PositionRiskUpdate',
  setup() {
    const positionRiskService = inject('positionRiskService', () => new PositionRiskService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const positionRisk: Ref<IPositionRisk> = ref(new PositionRisk());

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const keyResponsibilityCategoryService = inject('keyResponsibilityCategoryService', () => new KeyResponsibilityCategoryService());

    const keyResponsibilityCategories: Ref<IKeyResponsibilityCategory[]> = ref([]);
    const riskTypeValues: Ref<string[]> = ref(Object.keys(RiskType));
    const riskLevelValues: Ref<string[]> = ref(Object.keys(RiskLevel));
    const documentStatusValues: Ref<string[]> = ref(Object.keys(DocumentStatus));
    const backupStatusValues: Ref<string[]> = ref(Object.keys(BackupStatus));
    const importanceLevelValues: Ref<string[]> = ref(Object.keys(ImportanceLevel));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePositionRisk = async positionRiskId => {
      try {
        const res = await positionRiskService().find(positionRiskId);
        positionRisk.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionRiskId) {
      retrievePositionRisk(route.params.positionRiskId);
    }

    const initRelationships = () => {
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
      keyResponsibilityCategoryService()
        .retrieve()
        .then(res => {
          keyResponsibilityCategories.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      riskType: {
        required: validations.required('This field is required.'),
      },
      riskLevel: {
        required: validations.required('This field is required.'),
      },
      documentStatus: {},
      backupStatus: {},
      customerOrSystemDependency: {},
      riskDescription: {},
      improvementAction: {},
      identifiedDate: {
        required: validations.required('This field is required.'),
      },
      targetDate: {},
      closedDate: {},
      position: {
        required: validations.required('This field is required.'),
      },
      category: {},
    };
    const v$ = useVuelidate(validationRules, positionRisk as any);
    v$.value.$validate();

    return {
      positionRiskService,
      alertService,
      positionRisk,
      previousState,
      riskTypeValues,
      riskLevelValues,
      documentStatusValues,
      backupStatusValues,
      importanceLevelValues,
      isSaving,
      currentLanguage,
      positions,
      keyResponsibilityCategories,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.positionRisk.id) {
        this.positionRiskService()
          .update(this.positionRisk)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PositionRisk is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.positionRiskService()
          .create(this.positionRisk)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PositionRisk is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
