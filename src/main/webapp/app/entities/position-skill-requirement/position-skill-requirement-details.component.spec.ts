import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionSkillRequirementDetails from './position-skill-requirement-details.vue';

type PositionSkillRequirementDetailsComponentType = InstanceType<typeof PositionSkillRequirementDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionSkillRequirementSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PositionSkillRequirement Management Detail Component', () => {
    let positionSkillRequirementServiceStub: any;
    let mountOptions: MountingOptions<PositionSkillRequirementDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionSkillRequirementServiceStub = {
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
          positionSkillRequirementService: () => positionSkillRequirementServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionSkillRequirementServiceStub.find.mockResolvedValue(positionSkillRequirementSample);
        route = {
          params: {
            positionSkillRequirementId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionSkillRequirementDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.positionSkillRequirement).toMatchObject(positionSkillRequirementSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionSkillRequirementServiceStub.find.mockResolvedValue(positionSkillRequirementSample);
        const wrapper = shallowMount(PositionSkillRequirementDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
