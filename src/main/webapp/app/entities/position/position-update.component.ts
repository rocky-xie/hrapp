import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

import { useVuelidate } from '@vuelidate/core';

import PersonService from '@/entities/person/person.service';
import PositionAssignmentService from '@/entities/position-assignment/position-assignment.service';
import PositionRiskEvaluationService from '@/entities/position-risk-evaluation/position-risk-evaluation.service';
import PositionSkillRequirementService from '@/entities/position-skill-requirement/position-skill-requirement.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { DocumentStatus } from '@/shared/model/enumerations/document-status.model';
import { ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { PositionType } from '@/shared/model/enumerations/position-type.model';
import { ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { RequirementImportance } from '@/shared/model/enumerations/requirement-importance.model';
import { ReviewCycle } from '@/shared/model/enumerations/review-cycle.model';
import { RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition, Position } from '@/shared/model/position.model';
import { type IPositionAssignment, PositionAssignment } from '@/shared/model/position-assignment.model';
import { type IPositionRiskEvaluation, PositionRiskEvaluation } from '@/shared/model/position-risk-evaluation.model';
import { type IPositionSkillRequirement, PositionSkillRequirement } from '@/shared/model/position-skill-requirement.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

import PositionService from './position.service';

export default defineComponent({
  name: 'PositionUpdate',
  setup() {
    const positionService = inject('positionService', () => new PositionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const position: Ref<IPosition> = ref(new Position());
    const positionRiskEvaluationService = inject('positionRiskEvaluationService', () => new PositionRiskEvaluationService());
    const activeTab = ref('basic');
    const positionRiskEvaluation: Ref<IPositionRiskEvaluation> = ref(new PositionRiskEvaluation());
    const documentStatusValue: Ref<string | null> = ref(null);
    const customerOrSystemDependencyValue: Ref<string | null> = ref(null);
    const successionReadinessValue: Ref<string | null> = ref(null);
    const isEvaluating = ref(false);

    const documentStatusValues: Ref<string[]> = ref(Object.keys(DocumentStatus));
    const readinessLevelValues: Ref<string[]> = ref(Object.keys(ReadinessLevel));
    const riskLevelValues: Ref<string[]> = ref(Object.keys(RiskLevel));

    watch(activeTab, tab => {
      if (tab === 'risk' && position.value.id && !positionRiskEvaluation.value.evaluationDate) {
        loadLatestEvaluation();
      }
    });

    const clearEvaluationResult = () => {
      positionRiskEvaluation.value = new PositionRiskEvaluation();
    };

    const loadLatestEvaluation = async () => {
      const posId = position.value.id;
      if (!posId) return;
      try {
        const res = await positionRiskEvaluationService().retrieve({
          'positionId.equals': posId,
          sort: ['evaluationDate,desc', 'id,desc'],
          size: 1,
        });
        const data = (res.data ?? []) as IPositionRiskEvaluation[];
        if (data.length > 0) {
          const latest = data[0];
          positionRiskEvaluation.value = latest;
          documentStatusValue.value = latest.documentStatus ?? null;
          customerOrSystemDependencyValue.value = latest.customerOrSystemDependency ?? null;
          successionReadinessValue.value = latest.successionReadiness ?? null;
        }
      } catch (_error) {
        // ignore
      }
    };

    const positionSkillRequirementService = inject('positionSkillRequirementService', () => new PositionSkillRequirementService());
    const positionSkillRequirements: Ref<IPositionSkillRequirement[]> = ref([]);
    const deletedPositionSkillRequirementIds: Ref<number[]> = ref([]);
    const positionAssignmentService = inject('positionAssignmentService', () => new PositionAssignmentService());
    const positionAssignments: Ref<IPositionAssignment[]> = ref([]);
    const deletedPositionAssignmentIds: Ref<number[]> = ref([]);
    const skillService = inject('skillService', () => new SkillService());
    const skills: Ref<ISkill[]> = ref([]);
    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());
    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const personService = inject('personService', () => new PersonService());
    const people: Ref<IPerson[]> = ref([]);
    const positionTypeValues: Ref<string[]> = ref(Object.keys(PositionType));
    const importanceLevelValues: Ref<string[]> = ref(Object.keys(ImportanceLevel));
    const requirementImportanceValues: Ref<string[]> = ref(Object.keys(RequirementImportance));
    const reviewCycleValues: Ref<string[]> = ref(Object.keys(ReviewCycle));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePosition = async positionId => {
      try {
        const res = await positionService().find(positionId);
        position.value = res;
        await Promise.all([retrievePositionSkillRequirements(res.id), retrievePositionAssignments(res.id)]);
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const retrievePositionSkillRequirements = async positionId => {
      if (!positionId) {
        return;
      }
      const res = await positionSkillRequirementService().retrieve({ 'positionId.equals': positionId, size: 1000, sort: ['id,asc'] });
      positionSkillRequirements.value = res.data ?? [];
    };

    const retrievePositionAssignments = async positionId => {
      if (!positionId) {
        return;
      }
      const res = await positionAssignmentService().retrieve({ 'positionId.equals': positionId, size: 1000, sort: ['id,asc'] });
      positionAssignments.value = res.data ?? [];
    };

    if (route.params?.positionId) {
      retrievePosition(route.params.positionId);
    }

    const initRelationships = () => {
      skillService()
        .retrieve({ size: 1000, sort: ['skillName,asc'] })
        .then(res => {
          skills.value = res.data ?? [];
        });
      skillLevelService()
        .retrieve({ size: 1000, sort: ['sortOrder,asc'] })
        .then(res => {
          skillLevels.value = res.data ?? [];
        });
      personService()
        .retrieve({ size: 1000, sort: ['personName,asc'] })
        .then(res => {
          people.value = res.data ?? [];
        });
    };

    initRelationships();

    const successionCandidates = ref<any[]>([]);
    const successionLoading = ref(false);

    const loadSuccessionCandidates = async (positionId: number) => {
      successionLoading.value = true;
      try {
        const res = await axios.get('api/succession-candidates', {
          params: { 'positionId.equals': positionId, sort: ['priority,asc'], size: 100 },
        });
        successionCandidates.value = res.data ?? [];
      } catch {
        successionCandidates.value = [];
      } finally {
        successionLoading.value = false;
      }
    };

    watch(activeTab, tab => {
      if (tab === 'succession' && position.value.id && successionCandidates.value.length === 0) {
        loadSuccessionCandidates(position.value.id);
      }
    });

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      positionCode: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 50 characters.', 50),
      },
      positionName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      positionType: {
        required: validations.required('This field is required.'),
      },
      businessImportance: {
        required: validations.required('This field is required.'),
      },
      keyPosition: {
        required: validations.required('This field is required.'),
      },
      description: {},
      plannedHeadcount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      minimumOwnerCount: {
        integer: validations.integer('This field should be a number.'),
        min: validations.minValue('This field should be at least 0.', 0),
      },
      reviewCycle: {},
      active: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, position as any);
    v$.value.$validate();

    return {
      positionService,
      alertService,
      position,
      previousState,
      positionTypeValues,
      importanceLevelValues,
      requirementImportanceValues,
      reviewCycleValues,
      isSaving,
      currentLanguage,
      positionRiskEvaluationService,
      activeTab,
      positionRiskEvaluation,
      documentStatusValue,
      customerOrSystemDependencyValue,
      successionReadinessValue,
      isEvaluating,
      documentStatusValues,
      readinessLevelValues,
      riskLevelValues,
      clearEvaluationResult,
      positionSkillRequirementService,
      positionSkillRequirements,
      deletedPositionSkillRequirementIds,
      positionAssignmentService,
      positionAssignments,
      deletedPositionAssignmentIds,
      skills,
      skillLevels,
      people,
      successionCandidates,
      successionLoading,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    addPositionSkillRequirement(): void {
      this.positionSkillRequirements.push(
        new PositionSkillRequirement(undefined, 'REQUIRED', null, this.position, undefined, undefined, null),
      );
    },
    removePositionSkillRequirement(index: number): void {
      const row = this.positionSkillRequirements[index];
      if (row?.id) {
        this.deletedPositionSkillRequirementIds.push(row.id);
      }
      this.positionSkillRequirements.splice(index, 1);
    },
    addPositionAssignment(): void {
      this.positionAssignments.push(new PositionAssignment(undefined, false, null, null, null, true, undefined, this.position));
    },
    removePositionAssignment(index: number): void {
      const row = this.positionAssignments[index];
      if (row?.id) {
        this.deletedPositionAssignmentIds.push(row.id);
      }
      this.positionAssignments.splice(index, 1);
    },
    async syncPositionSkillRequirements(savedPosition: IPosition): Promise<void> {
      await Promise.all(this.deletedPositionSkillRequirementIds.map(id => this.positionSkillRequirementService().delete(id)));
      const validRows = this.positionSkillRequirements.filter(row => row.skill?.id && row.requiredLevel?.id && row.importance);
      await Promise.all(
        validRows.map(row => {
          const payload = { ...row, position: savedPosition };
          return payload.id
            ? this.positionSkillRequirementService().update(payload)
            : this.positionSkillRequirementService().create(payload);
        }),
      );
      this.deletedPositionSkillRequirementIds = [];
    },
    async syncPositionAssignments(savedPosition: IPosition): Promise<void> {
      await Promise.all(this.deletedPositionAssignmentIds.map(id => this.positionAssignmentService().delete(id)));
      const validRows = this.positionAssignments.filter(row => row.person?.id);
      await Promise.all(
        validRows.map(row => {
          const payload = { ...row, position: savedPosition, primaryOwner: row.primaryOwner ?? false, active: row.active ?? true };
          return payload.id ? this.positionAssignmentService().update(payload) : this.positionAssignmentService().create(payload);
        }),
      );
      this.deletedPositionAssignmentIds = [];
    },
    async syncPositionChildren(savedPosition: IPosition): Promise<void> {
      await Promise.all([this.syncPositionSkillRequirements(savedPosition), this.syncPositionAssignments(savedPosition)]);
    },
    onRiskInputChange(): void {
      this.clearEvaluationResult();
      const posId = this.position.id;
      if (!posId) return;
      this.isEvaluating = true;
      this.positionRiskEvaluationService()
        .evaluate(posId, this.documentStatusValue, this.customerOrSystemDependencyValue, this.successionReadinessValue, true)
        .then(res => {
          this.positionRiskEvaluation.evaluationDate = res.evaluationDate;
          this.positionRiskEvaluation.ownerCount = res.ownerCount;
          this.positionRiskEvaluation.substitutableOwnerCount = res.substitutableOwnerCount;
          this.positionRiskEvaluation.hasSubstitute = res.hasSubstitute;
          this.positionRiskEvaluation.riskLevel = res.riskLevel;
          this.positionRiskEvaluation.riskReason = res.riskReason;
          this.positionRiskEvaluation.recommendedAction = res.recommendedAction;
        })
        .catch(() => {})
        .finally(() => {
          this.isEvaluating = false;
        });
    },
    evaluateAndSave(): void {
      const posId = this.position.id;
      if (!posId) return;
      this.isEvaluating = true;
      this.positionRiskEvaluationService()
        .evaluate(posId, this.documentStatusValue, this.customerOrSystemDependencyValue, this.successionReadinessValue, false)
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
          this.alertService.showInfo('Position risk evaluation saved.');
        })
        .catch(error => {
          this.alertService.showHttpError(error.response);
        })
        .finally(() => {
          this.isEvaluating = false;
        });
    },
    save(): void {
      this.isSaving = true;
      if (this.position.id) {
        this.positionService()
          .update(this.position)
          .then(async param => {
            await this.syncPositionChildren(param);
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Position is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.positionService()
          .create(this.position)
          .then(async param => {
            await this.syncPositionChildren(param);
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Position is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
