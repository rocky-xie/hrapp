import { describe, expect, it, vi, beforeEach } from 'vitest';
import { shallowMount } from '@vue/test-utils';
import i18n from '@/shared/config/i18n';

import BatchImportExport from './batch.vue';

type BatchImportExportComponentType = InstanceType<typeof BatchImportExport>;

describe('BatchImportExport Component', () => {
  let comp: BatchImportExportComponentType;

  beforeEach(() => {
    const wrapper = shallowMount(BatchImportExport, {
      global: {
        plugins: [i18n],
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

  it('should define three import/export entities', () => {
    expect(comp.entities).toHaveLength(3);
    expect(comp.entities.map((e: any) => e.key)).toEqual(['persons', 'positions', 'skills']);
  });

  it('should have initial state with no loading or result', () => {
    expect(comp.isExporting).toBe(false);
    expect(comp.isImporting).toBe(false);
    expect(comp.importResult).toBeNull();
  });
});
