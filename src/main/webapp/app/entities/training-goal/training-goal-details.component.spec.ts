import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrainingGoalDetails from './training-goal-details.vue';

type TrainingGoalDetailsComponentType = InstanceType<typeof TrainingGoalDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trainingGoalSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('TrainingGoal Management Detail Component', () => {
    let trainingGoalServiceStub: any;
    let mountOptions: MountingOptions<TrainingGoalDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      trainingGoalServiceStub = {
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
          trainingGoalService: () => trainingGoalServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        trainingGoalServiceStub.find.mockResolvedValue(trainingGoalSample);
        route = {
          params: {
            trainingGoalId: `${123}`,
          },
        };
        const wrapper = shallowMount(TrainingGoalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.trainingGoal).toMatchObject(trainingGoalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trainingGoalServiceStub.find.mockResolvedValue(trainingGoalSample);
        const wrapper = shallowMount(TrainingGoalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
