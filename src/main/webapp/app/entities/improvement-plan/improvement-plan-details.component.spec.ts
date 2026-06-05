import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ImprovementPlanDetails from './improvement-plan-details.vue';

type ImprovementPlanDetailsComponentType = InstanceType<typeof ImprovementPlanDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const improvementPlanSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('ImprovementPlan Management Detail Component', () => {
    let improvementPlanServiceStub: any;
    let mountOptions: MountingOptions<ImprovementPlanDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      improvementPlanServiceStub = {
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
          improvementPlanService: () => improvementPlanServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        improvementPlanServiceStub.find.mockResolvedValue(improvementPlanSample);
        route = {
          params: {
            improvementPlanId: `${123}`,
          },
        };
        const wrapper = shallowMount(ImprovementPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.improvementPlan).toMatchObject(improvementPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        improvementPlanServiceStub.find.mockResolvedValue(improvementPlanSample);
        const wrapper = shallowMount(ImprovementPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
