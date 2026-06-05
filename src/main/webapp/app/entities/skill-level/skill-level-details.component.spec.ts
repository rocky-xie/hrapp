import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillLevelDetails from './skill-level-details.vue';

type SkillLevelDetailsComponentType = InstanceType<typeof SkillLevelDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillLevelSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('SkillLevel Management Detail Component', () => {
    let skillLevelServiceStub: any;
    let mountOptions: MountingOptions<SkillLevelDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      skillLevelServiceStub = {
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
          skillLevelService: () => skillLevelServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillLevelServiceStub.find.mockResolvedValue(skillLevelSample);
        route = {
          params: {
            skillLevelId: `${123}`,
          },
        };
        const wrapper = shallowMount(SkillLevelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.skillLevel).toMatchObject(skillLevelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillLevelServiceStub.find.mockResolvedValue(skillLevelSample);
        const wrapper = shallowMount(SkillLevelDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
