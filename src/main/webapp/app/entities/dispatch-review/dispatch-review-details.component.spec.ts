import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import DispatchReviewDetails from './dispatch-review-details.vue';

type DispatchReviewDetailsComponentType = InstanceType<typeof DispatchReviewDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dispatchReviewSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('DispatchReview Management Detail Component', () => {
    let dispatchReviewServiceStub: any;
    let mountOptions: MountingOptions<DispatchReviewDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dispatchReviewServiceStub = {
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
          dispatchReviewService: () => dispatchReviewServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dispatchReviewServiceStub.find.mockResolvedValue(dispatchReviewSample);
        route = {
          params: {
            dispatchReviewId: `${123}`,
          },
        };
        const wrapper = shallowMount(DispatchReviewDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dispatchReview).toMatchObject(dispatchReviewSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dispatchReviewServiceStub.find.mockResolvedValue(dispatchReviewSample);
        const wrapper = shallowMount(DispatchReviewDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
