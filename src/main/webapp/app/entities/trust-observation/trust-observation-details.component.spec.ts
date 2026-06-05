import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrustObservationDetails from './trust-observation-details.vue';

type TrustObservationDetailsComponentType = InstanceType<typeof TrustObservationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trustObservationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('TrustObservation Management Detail Component', () => {
    let trustObservationServiceStub: any;
    let mountOptions: MountingOptions<TrustObservationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      trustObservationServiceStub = {
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
          trustObservationService: () => trustObservationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        trustObservationServiceStub.find.mockResolvedValue(trustObservationSample);
        route = {
          params: {
            trustObservationId: `${123}`,
          },
        };
        const wrapper = shallowMount(TrustObservationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.trustObservation).toMatchObject(trustObservationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trustObservationServiceStub.find.mockResolvedValue(trustObservationSample);
        const wrapper = shallowMount(TrustObservationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
