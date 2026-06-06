import { describe, expect, it, vi, beforeEach } from 'vitest';
import { shallowMount } from '@vue/test-utils';

import DataQuality from './data-quality.vue';

type DataQualityComponentType = InstanceType<typeof DataQuality>;

describe('DataQuality Component', () => {
  let comp: DataQualityComponentType;

  beforeEach(() => {
    const wrapper = shallowMount(DataQuality, {
      global: {
        stubs: {
          'font-awesome-icon': true,
        },
        mocks: {
          $t: (key: string) => key,
        },
        provide: {
          alertService: { showInfo: vi.fn(), showError: vi.fn(), showHttpError: vi.fn() },
        },
      },
    });
    comp = wrapper.vm;
  });

  it('should start with empty issues and no fetching', () => {
    expect(comp.issues).toHaveLength(0);
    expect(comp.isFetching).toBe(false);
    expect(comp.activeTab).toBe('all');
  });

  it('should compute filtered issues for active tab', () => {
    comp.issues = [
      { severity: 'ERROR', entityType: 'Person', message: 'test1' },
      { severity: 'WARNING', entityType: 'Position', message: 'test2' },
    ];

    comp.activeTab = 'all';
    expect(comp.filteredIssues()).toHaveLength(2);

    comp.activeTab = 'error';
    expect(comp.filteredIssues()).toHaveLength(1);
    expect(comp.filteredIssues()[0].severity).toBe('ERROR');
  });

  it('should return correct error counts', () => {
    comp.issues = [{ severity: 'ERROR' }, { severity: 'ERROR' }, { severity: 'WARNING' }, { severity: 'INFO' }];

    expect(comp.errorCount()).toBe(2);
    expect(comp.warningCount()).toBe(1);
    expect(comp.infoCount()).toBe(1);
  });

  it('should return correct severity badge class', () => {
    expect(comp.severityBadgeClass('ERROR')).toBe('bg-danger');
    expect(comp.severityBadgeClass('WARNING')).toBe('bg-warning text-dark');
    expect(comp.severityBadgeClass('INFO')).toBe('bg-info');
    expect(comp.severityBadgeClass('UNKNOWN')).toBe('bg-secondary');
  });
});
