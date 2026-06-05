import { type Ref, defineComponent, inject, ref } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IDataQualityIssue } from './data-quality.service';
import DataQualityService from './data-quality.service';

export default defineComponent({
  name: 'DataQuality',
  setup() {
    const dataQualityService = inject('dataQualityService', () => new DataQualityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const issues: Ref<IDataQualityIssue[]> = ref([]);
    const isFetching = ref(false);
    const activeTab: Ref<string> = ref('all');

    const errorCount = () => issues.value.filter(i => i.severity === 'ERROR').length;
    const warningCount = () => issues.value.filter(i => i.severity === 'WARNING').length;
    const infoCount = () => issues.value.filter(i => i.severity === 'INFO').length;

    const filteredIssues = () => {
      if (activeTab.value === 'all') return issues.value;
      return issues.value.filter(i => i.severity === activeTab.value.toUpperCase());
    };

    const severityBadgeClass = (severity: string) => {
      switch (severity) {
        case 'ERROR':
          return 'bg-danger';
        case 'WARNING':
          return 'bg-warning text-dark';
        case 'INFO':
          return 'bg-info';
        default:
          return 'bg-secondary';
      }
    };

    const entityTypeIcon = (type: string) => {
      switch (type) {
        case 'Person':
          return 'user';
        case 'Position':
          return 'briefcase';
        case 'PositionAssignment':
          return 'user-tag';
        case 'TrainingGoal':
          return 'bullseye';
        case 'StaffSubstitution':
          return 'exchange-alt';
        case 'PersonSkill':
          return 'clipboard-list';
        case 'PositionRiskEvaluation':
          return 'exclamation-triangle';
        case 'SuccessionCandidate':
          return 'users';
        case 'Evaluation':
          return 'star';
        default:
          return 'circle';
      }
    };

    const runChecks = async () => {
      isFetching.value = true;
      try {
        const res = await dataQualityService().runChecks();
        issues.value = res;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    return {
      issues,
      isFetching,
      activeTab,
      errorCount,
      warningCount,
      infoCount,
      filteredIssues,
      severityBadgeClass,
      entityTypeIcon,
      runChecks,
    };
  },
});
