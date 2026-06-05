import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import EvaluationDetails from './evaluation-details.vue';

type EvaluationDetailsComponentType = InstanceType<typeof EvaluationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const evaluationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Evaluation Management Detail Component', () => {
    let evaluationServiceStub: any;
    let mountOptions: MountingOptions<EvaluationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      evaluationServiceStub = {
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
          evaluationService: () => evaluationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        evaluationServiceStub.find.mockResolvedValue(evaluationSample);
        route = {
          params: {
            evaluationId: `${123}`,
          },
        };
        const wrapper = shallowMount(EvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.evaluation).toMatchObject(evaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        evaluationServiceStub.find.mockResolvedValue(evaluationSample);
        const wrapper = shallowMount(EvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
