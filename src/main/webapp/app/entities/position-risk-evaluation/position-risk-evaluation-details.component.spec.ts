import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionRiskEvaluationDetails from './position-risk-evaluation-details.vue';

type PositionRiskEvaluationDetailsComponentType = InstanceType<typeof PositionRiskEvaluationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionRiskEvaluationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PositionRiskEvaluation Management Detail Component', () => {
    let positionRiskEvaluationServiceStub: any;
    let mountOptions: MountingOptions<PositionRiskEvaluationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionRiskEvaluationServiceStub = {
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
          positionRiskEvaluationService: () => positionRiskEvaluationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionRiskEvaluationServiceStub.find.mockResolvedValue(positionRiskEvaluationSample);
        route = {
          params: {
            positionRiskEvaluationId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionRiskEvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.positionRiskEvaluation).toMatchObject(positionRiskEvaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionRiskEvaluationServiceStub.find.mockResolvedValue(positionRiskEvaluationSample);
        const wrapper = shallowMount(PositionRiskEvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
