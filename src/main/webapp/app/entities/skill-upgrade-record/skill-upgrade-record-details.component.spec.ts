import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillUpgradeRecordDetails from './skill-upgrade-record-details.vue';

type SkillUpgradeRecordDetailsComponentType = InstanceType<typeof SkillUpgradeRecordDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillUpgradeRecordSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('SkillUpgradeRecord Management Detail Component', () => {
    let skillUpgradeRecordServiceStub: any;
    let mountOptions: MountingOptions<SkillUpgradeRecordDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      skillUpgradeRecordServiceStub = {
        find: vi.fn(),
      };

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          skillUpgradeRecordService: () => skillUpgradeRecordServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillUpgradeRecordServiceStub.find.mockResolvedValue(skillUpgradeRecordSample);
        route = {
          params: {
            skillUpgradeRecordId: `${123}`,
          },
        };
        const wrapper = shallowMount(SkillUpgradeRecordDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.skillUpgradeRecord).toMatchObject(skillUpgradeRecordSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillUpgradeRecordServiceStub.find.mockResolvedValue(skillUpgradeRecordSample);
        const wrapper = shallowMount(SkillUpgradeRecordDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
