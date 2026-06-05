import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionRiskDetails from './position-risk-details.vue';

type PositionRiskDetailsComponentType = InstanceType<typeof PositionRiskDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionRiskSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PositionRisk Management Detail Component', () => {
    let positionRiskServiceStub: any;
    let mountOptions: MountingOptions<PositionRiskDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionRiskServiceStub = {
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
          positionRiskService: () => positionRiskServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionRiskServiceStub.find.mockResolvedValue(positionRiskSample);
        route = {
          params: {
            positionRiskId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionRiskDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.positionRisk).toMatchObject(positionRiskSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionRiskServiceStub.find.mockResolvedValue(positionRiskSample);
        const wrapper = shallowMount(PositionRiskDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
