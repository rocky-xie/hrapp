import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillDetails from './skill-details.vue';

type SkillDetailsComponentType = InstanceType<typeof SkillDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Skill Management Detail Component', () => {
    let skillServiceStub: any;
    let mountOptions: MountingOptions<SkillDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      skillServiceStub = {
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
          skillService: () => skillServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillServiceStub.find.mockResolvedValue(skillSample);
        route = {
          params: {
            skillId: `${123}`,
          },
        };
        const wrapper = shallowMount(SkillDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.skill).toMatchObject(skillSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillServiceStub.find.mockResolvedValue(skillSample);
        const wrapper = shallowMount(SkillDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
