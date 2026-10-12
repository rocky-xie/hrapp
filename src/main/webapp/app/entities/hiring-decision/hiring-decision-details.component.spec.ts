import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import HiringDecisionDetails from './hiring-decision-details.vue';

type HiringDecisionDetailsComponentType = InstanceType<typeof HiringDecisionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const hiringDecisionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('HiringDecision Management Detail Component', () => {
    let hiringDecisionServiceStub: any;
    let mountOptions: MountingOptions<HiringDecisionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      hiringDecisionServiceStub = {
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
          hiringDecisionService: () => hiringDecisionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        hiringDecisionServiceStub.find.mockResolvedValue(hiringDecisionSample);
        route = {
          params: {
            hiringDecisionId: `${123}`,
          },
        };
        const wrapper = shallowMount(HiringDecisionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.hiringDecision).toMatchObject(hiringDecisionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        hiringDecisionServiceStub.find.mockResolvedValue(hiringDecisionSample);
        const wrapper = shallowMount(HiringDecisionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
