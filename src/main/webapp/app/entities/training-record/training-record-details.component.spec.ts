import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrainingRecordDetails from './training-record-details.vue';

type TrainingRecordDetailsComponentType = InstanceType<typeof TrainingRecordDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trainingRecordSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('TrainingRecord Management Detail Component', () => {
    let trainingRecordServiceStub: any;
    let mountOptions: MountingOptions<TrainingRecordDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      trainingRecordServiceStub = {
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
          trainingRecordService: () => trainingRecordServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        trainingRecordServiceStub.find.mockResolvedValue(trainingRecordSample);
        route = {
          params: {
            trainingRecordId: `${123}`,
          },
        };
        const wrapper = shallowMount(TrainingRecordDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.trainingRecord).toMatchObject(trainingRecordSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trainingRecordServiceStub.find.mockResolvedValue(trainingRecordSample);
        const wrapper = shallowMount(TrainingRecordDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
