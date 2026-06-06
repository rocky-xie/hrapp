import { describe, expect, it, vi, beforeEach } from 'vitest';
import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import i18n from '@/shared/config/i18n';

import SkillGapReport from './skill-gap-report.vue';

type SkillGapReportComponentType = InstanceType<typeof SkillGapReport>;

const axiosGetSpy = vi.spyOn(axios, 'get');

describe('SkillGapReport Component', () => {
  let comp: SkillGapReportComponentType;

  beforeEach(() => {
    axiosGetSpy.mockReset();
    axiosGetSpy.mockResolvedValue({ data: [] });
  });

  const createComponent = async () => {
    const wrapper = shallowMount(SkillGapReport, {
      global: {
        plugins: [i18n],
        stubs: {
          'font-awesome-icon': true,
          'b-spinner': true,
        },
      },
    });
    comp = wrapper.vm;
    await comp.$nextTick();
    return comp;
  };

  it('should load all positions on mount', async () => {
    const mockPositions = [
      { id: 1, positionCode: 'P01', positionName: 'Manager' },
      { id: 2, positionCode: 'P02', positionName: 'Engineer' },
    ];
    axiosGetSpy.mockResolvedValue({ data: mockPositions });

    await createComponent();

    expect(axiosGetSpy).toHaveBeenCalledWith('api/positions', { params: { size: 200 } });
    expect(comp.allPositions).toEqual(mockPositions);
  });

  it('should auto-select all positions after loading', async () => {
    const mockPositions = [
      { id: 1, positionCode: 'P01', positionName: 'Manager' },
      { id: 2, positionCode: 'P02', positionName: 'Engineer' },
    ];
    axiosGetSpy.mockResolvedValue({ data: mockPositions });

    await createComponent();

    expect(comp.selectedPositionIds).toEqual([1, 2]);
  });

  it('should have initial default filter values', async () => {
    await createComponent();

    expect(comp.includeOwners).toBe(true);
    expect(comp.includeCandidates).toBe(true);
    expect(comp.minImportance).toBe('');
    expect(comp.loading).toBe(false);
    expect(comp.report).toBeNull();
    expect(comp.error).toBe(false);
  });
});
