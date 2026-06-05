import { defineComponent, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import axios from 'axios';
import SkillGapReportService from './skill-gap-report.service';

export default defineComponent({
  name: 'SkillGapReport',
  setup() {
    const { t } = useI18n();
    const reportService = new SkillGapReportService();

    const loading = ref(false);
    const positions = ref<any[]>([]);
    const allPositions = ref<any[]>([]);
    const selectedPositionIds = ref<number[]>([]);
    const report = ref<any>(null);
    const suggestions = ref<any[]>([]);
    const suggestionLoading = ref(false);
    const error = ref(false);
    const positionRequired = ref(false);
    const includeOwners = ref(true);
    const includeCandidates = ref(true);
    const minImportance = ref('');

    const loadPositions = async () => {
      try {
        const res = await axios.get('api/positions', { params: { size: 200 } });
        allPositions.value = res.data;
      } catch {
        // fail silently
      }
    };

    const generateReport = async () => {
      positionRequired.value = selectedPositionIds.value.length === 0;
      if (positionRequired.value) return;
      loading.value = true;
      error.value = false;
      report.value = null;
      suggestions.value = [];
      try {
        const params: any = {
          includeOwners: includeOwners.value,
          includeCandidates: includeCandidates.value,
        };
        if (minImportance.value) {
          params.minImportance = minImportance.value;
        }
        const data = await reportService.getReport(selectedPositionIds.value, params);
        report.value = data;
        suggestionLoading.value = true;
        try {
          suggestions.value = (await reportService.getSuggestions(data.positions)) || [];
        } catch {
          suggestions.value = [];
        } finally {
          suggestionLoading.value = false;
        }
      } catch {
        error.value = true;
      } finally {
        loading.value = false;
      }
    };

    const createGoal = async (suggestion: any) => {
      try {
        suggestion.goalName = t('trainingSuggestion.goalName', {
          skillName: suggestion.skillName,
          personName: suggestion.personName,
        });
        suggestion.goalDescription = t('trainingSuggestion.goalDescription', {
          reason: suggestion.suggestionReason,
        });
        suggestion.targetLevelDescription = t('trainingSuggestion.targetLevelDescription', {
          level: suggestion.targetLevelCode,
        });
        const goal = await reportService.createTrainingGoalFromSuggestion(suggestion);
        suggestion.status = 'CONVERTED';
        return goal;
      } catch {
        return null;
      }
    };

    const hasMissingSkill = (person: any) => person.gaps && person.gaps.length > 0;

    const totalGaps = () => {
      if (!report.value?.positions) return 0;
      let count = 0;
      for (const pos of report.value.positions) {
        for (const person of [...(pos.owners || []), ...(pos.candidates || [])]) {
          count += person.gaps?.length || 0;
        }
      }
      return count;
    };

    const riskBadge = (level: string) => {
      if (level === 'HIGH') return 'bg-danger';
      if (level === 'MEDIUM') return 'bg-warning';
      return 'bg-info';
    };

    const coverageBadge = (rate: number) => {
      if (rate >= 80) return 'badge bg-success';
      if (rate >= 50) return 'badge bg-warning';
      return 'badge bg-danger';
    };

    const importanceBadge = (imp: string) => {
      if (imp === 'REQUIRED') return 'bg-danger';
      if (imp === 'IMPORTANT') return 'bg-warning';
      return 'bg-info';
    };

    const priorityBadge = (p: string) => {
      if (p === 'P0_CRITICAL') return 'bg-danger';
      if (p === 'P1_HIGH') return 'bg-warning';
      if (p === 'P2_MEDIUM') return 'bg-info';
      return 'bg-secondary';
    };

    watch(selectedPositionIds, () => {
      if (selectedPositionIds.value.length > 0) {
        positionRequired.value = false;
      }
    });

    loadPositions();

    return {
      loading,
      positions,
      allPositions,
      selectedPositionIds,
      report,
      suggestions,
      suggestionLoading,
      error,
      positionRequired,
      includeOwners,
      includeCandidates,
      minImportance,
      generateReport,
      createGoal,
      hasMissingSkill,
      totalGaps,
      riskBadge,
      coverageBadge,
      importanceBadge,
      priorityBadge,
    };
  },
});
