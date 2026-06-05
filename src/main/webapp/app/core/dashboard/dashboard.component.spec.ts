import { beforeEach, describe, expect, it, vi } from 'vitest';
import { shallowMount } from '@vue/test-utils';
import axios from 'axios';

import Dashboard from './dashboard.vue';

vi.mock('axios');

type DashboardComponentType = InstanceType<typeof Dashboard>;

describe('Dashboard', () => {
  let comp: DashboardComponentType;

  beforeEach(() => {
    vi.clearAllMocks();
    (axios.get as any).mockReset();
    (axios.get as any).mockResolvedValue({ data: 0 });
  });

  const mountComponent = async () => {
    const wrapper = shallowMount(Dashboard, {
      global: {
        stubs: {
          'font-awesome-icon': true,
          'b-spinner': true,
          'b-badge': true,
          'b-form-checkbox': true,
        },
      },
    });
    comp = wrapper.vm;
    await comp.$nextTick();
    return wrapper;
  };

  describe('isOverdue', () => {
    it('should return true for past dates', async () => {
      await mountComponent();
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      expect(comp.isOverdue(yesterday.toISOString().split('T')[0])).toBe(true);
    });

    it('should return false for future dates', async () => {
      await mountComponent();
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      expect(comp.isOverdue(tomorrow.toISOString().split('T')[0])).toBe(false);
    });

    it('should return false for null or empty', async () => {
      await mountComponent();
      expect(comp.isOverdue(null)).toBe(false);
      expect(comp.isOverdue('')).toBe(false);
    });
  });

  describe('data loading', () => {
    it('should call count endpoints on mount', async () => {
      (axios.get as any).mockResolvedValue({ data: 0 });
      await mountComponent();

      const countCalls = [
        'api/positions/count',
        'api/people/count',
        'api/skills/count',
        'api/position-risk-evaluations/count',
        'api/training-records/count',
        'api/staff-substitutions/count',
      ];
      for (const url of countCalls) {
        const calls = (axios.get as any).mock.calls as Array<Array<any>>;
        expect(calls.some(([callUrl]) => callUrl === url)).toBe(true);
      }
    });

    it('should set summary counts from API response', async () => {
      const mockGet = vi.fn((url: string) => {
        const counts: Record<string, number> = {
          'api/positions/count': 10,
          'api/people/count': 48,
          'api/skills/count': 32,
          'api/position-risk-evaluations/count': 3,
          'api/training-records/count': 120,
          'api/staff-substitutions/count': 36,
        };
        const key = Object.keys(counts).find(k => url.startsWith(k));
        return Promise.resolve({ data: counts[key] ?? 0 });
      });
      (axios.get as any).mockImplementation(mockGet);

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.positionCount).toBe(10);
      expect(comp.personCount).toBe(48);
      expect(comp.skillCount).toBe(32);
      expect(comp.highRiskCount).toBe(3);
      expect(comp.trainingCount).toBe(120);
      expect(comp.substitutionCount).toBe(36);
    });

    it('should handle summary load failure gracefully', async () => {
      (axios.get as any).mockRejectedValue(new Error('Network error'));
      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.summaryLoading).toBe(false);
      expect(comp.positionCount).toBe(0);
    });
  });

  describe('risk overview', () => {
    it('should load and deduplicate high risk positions', async () => {
      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/position-risk-evaluations')) {
          return Promise.resolve({
            data: [
              { id: 1, position: { id: 1, positionName: 'Pos A' }, ownerCount: 1, hasSubstitute: false, evaluationDate: '2026-06-01' },
              { id: 2, position: { id: 1, positionName: 'Pos A' }, ownerCount: 1, hasSubstitute: false, evaluationDate: '2026-05-01' },
              { id: 3, position: { id: 2, positionName: 'Pos B' }, ownerCount: 0, hasSubstitute: false, evaluationDate: '2026-06-01' },
            ],
          });
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.highRiskPositions).toHaveLength(2);
      expect(comp.highRiskPositions[0].position.positionName).toBe('Pos A');
      expect(comp.highRiskPositions[1].position.positionName).toBe('Pos B');
    });

    it('should set riskError on failure', async () => {
      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/position-risk-evaluations')) {
          return Promise.reject(new Error('API error'));
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.riskError).toBe(true);
      expect(comp.riskLoading).toBe(false);
    });
  });

  describe('coverage gaps', () => {
    it('should load coverage gaps', async () => {
      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/staff-substitutions')) {
          return Promise.resolve({
            data: [
              {
                id: 1,
                position: { positionName: 'Pos A' },
                candidatePerson: { personName: 'John' },
                coverageRate: 45,
                thresholdRate: 80,
                missingSkills: 'Skill X, Skill Y',
              },
            ],
          });
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.coverageGaps).toHaveLength(1);
      expect(comp.coverageGaps[0].coverageRate).toBe(45);
      expect(comp.coverageGaps[0].position.positionName).toBe('Pos A');
    });

    it('should set coverageError on failure', async () => {
      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/staff-substitutions')) {
          return Promise.reject(new Error('API error'));
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.coverageError).toBe(true);
      expect(comp.coverageLoading).toBe(false);
    });
  });

  describe('skill review due', () => {
    it('should load due skills', async () => {
      const today = new Date().toISOString().split('T')[0];

      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/person-skills')) {
          return Promise.resolve({
            data: [
              {
                id: 1,
                person: { id: 1, personName: 'Alice' },
                skill: { skillName: 'Java' },
                currentLevel: { code: 'L3' },
                nextReviewDate: today,
              },
            ],
          });
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.dueSkills).toHaveLength(1);
      expect(comp.dueSkills[0].person.personName).toBe('Alice');
      expect(comp.dueSkills[0].skill.skillName).toBe('Java');
    });

    it('should set reviewError on failure', async () => {
      (axios.get as any).mockImplementation((url: string) => {
        if (url.startsWith('api/person-skills')) {
          return Promise.reject(new Error('API error'));
        }
        return Promise.resolve({ data: 0 });
      });

      await mountComponent();
      await new Promise(r => setTimeout(r, 50));

      expect(comp.reviewError).toBe(true);
      expect(comp.reviewLoading).toBe(false);
    });
  });
});
