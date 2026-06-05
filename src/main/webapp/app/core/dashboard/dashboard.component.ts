import { defineComponent, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import PositionRiskEvaluationService from '@/entities/position-risk-evaluation/position-risk-evaluation.service';
import StaffSubstitutionService from '@/entities/staff-substitution/staff-substitution.service';
import PersonSkillService from '@/entities/person-skill/person-skill.service';

export default defineComponent({
  name: 'Dashboard',
  setup() {
    const router = useRouter();

    const summaryLoading = ref(true);
    const positionCount = ref(0);
    const personCount = ref(0);
    const skillCount = ref(0);
    const highRiskCount = ref(0);
    const trainingCount = ref(0);
    const substitutionCount = ref(0);

    const riskLoading = ref(true);
    const riskError = ref(false);
    const highRiskPositions = ref<any[]>([]);

    const coverageLoading = ref(true);
    const coverageError = ref(false);
    const coverageGaps = ref<any[]>([]);

    const reviewLoading = ref(true);
    const reviewError = ref(false);
    const dueSkills = ref<any[]>([]);

    const successionLoading = ref(true);
    const successionError = ref(false);
    const successionMap = ref<any[]>([]);

    const positionRiskEvaluationService = new PositionRiskEvaluationService();
    const staffSubstitutionService = new StaffSubstitutionService();
    const personSkillService = new PersonSkillService();

    const loadSummary = async () => {
      try {
        const [pos, per, sk, hr, tr, sub] = await Promise.all([
          axios.get('api/positions/count'),
          axios.get('api/people/count'),
          axios.get('api/skills/count'),
          axios.get('api/position-risk-evaluations/count', { params: { 'riskLevel.equals': 'HIGH' } }),
          axios.get('api/training-records/count'),
          axios.get('api/staff-substitutions/count'),
        ]);
        positionCount.value = pos.data;
        personCount.value = per.data;
        skillCount.value = sk.data;
        highRiskCount.value = hr.data;
        trainingCount.value = tr.data;
        substitutionCount.value = sub.data;
      } catch {
        // fail silently
      } finally {
        summaryLoading.value = false;
      }
    };

    const loadHighRiskPositions = async () => {
      riskLoading.value = true;
      riskError.value = false;
      try {
        const res = await positionRiskEvaluationService.retrieve({
          'riskLevel.equals': 'HIGH',
          sort: ['evaluationDate,desc'],
          size: 50,
        });
        const seen = new Set<number>();
        highRiskPositions.value = (res.data as any[])
          .filter((item: any) => {
            if (!item.position?.id || seen.has(item.position.id)) return false;
            seen.add(item.position.id);
            return true;
          })
          .slice(0, 20);
      } catch {
        riskError.value = true;
      } finally {
        riskLoading.value = false;
      }
    };

    const loadCoverageGaps = async () => {
      coverageLoading.value = true;
      coverageError.value = false;
      try {
        const res = await staffSubstitutionService.retrieve({
          'substitutable.equals': false,
          sort: ['coverageRate,asc'],
          size: 50,
        });
        coverageGaps.value = res.data;
      } catch {
        coverageError.value = true;
      } finally {
        coverageLoading.value = false;
      }
    };

    const loadDueSkills = async () => {
      reviewLoading.value = true;
      reviewError.value = false;
      try {
        const today = new Date();
        const cutoff = new Date();
        cutoff.setDate(cutoff.getDate() + 30);
        const fmt = (d: Date) => d.toISOString().split('T')[0];
        const res = await personSkillService.retrieve({
          'nextReviewDate.specified': true,
          'nextReviewDate.lessOrEqual': fmt(cutoff),
          sort: ['nextReviewDate,asc'],
          size: 50,
        });
        dueSkills.value = res.data;
      } catch {
        reviewError.value = true;
      } finally {
        reviewLoading.value = false;
      }
    };

    const isOverdue = (dateStr: string): boolean => {
      if (!dateStr) return false;
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return new Date(dateStr) < today;
    };

    const getReviewStatus = (dateStr: string): { label: string; variant: string; days: number | null } => {
      if (!dateStr) return { label: '-', variant: 'secondary', days: null };
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const reviewDate = new Date(dateStr);
      reviewDate.setHours(0, 0, 0, 0);
      const diff = Math.round((reviewDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

      if (diff < 0) {
        return { label: 'dashboard.review.status.overdue', variant: 'danger', days: diff };
      }
      return { label: 'dashboard.review.status.daysRemaining', variant: diff <= 7 ? 'warning' : 'info', days: diff };
    };

    const navigateTo = (path: string) => {
      router.push(path);
    };

    const loadSuccessionMap = async () => {
      successionLoading.value = true;
      successionError.value = false;
      try {
        const res = await axios.get('api/reports/succession-map');
        successionMap.value = res.data;
      } catch {
        successionError.value = true;
      } finally {
        successionLoading.value = false;
      }
    };

    onMounted(() => {
      loadSummary();
      loadHighRiskPositions();
      loadCoverageGaps();
      loadDueSkills();
      loadSuccessionMap();
    });

    const readinessBadge = (level: string): string => {
      if (level === 'IMMEDIATE') return 'success';
      if (level === 'THREE_MONTHS') return 'info';
      if (level === 'SIX_TO_TWELVE_MONTHS') return 'warning';
      return 'secondary';
    };

    return {
      summaryLoading,
      positionCount,
      personCount,
      skillCount,
      highRiskCount,
      trainingCount,
      substitutionCount,
      highRiskPositions,
      riskLoading,
      riskError,
      coverageGaps,
      coverageLoading,
      coverageError,
      dueSkills,
      reviewLoading,
      reviewError,
      successionMap,
      successionLoading,
      successionError,
      isOverdue,
      getReviewStatus,
      navigateTo,
      readinessBadge,
    };
  },
});
