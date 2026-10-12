import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PreboardingFollowUpDetails from './preboarding-follow-up-details.vue';

type PreboardingFollowUpDetailsComponentType = InstanceType<typeof PreboardingFollowUpDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const preboardingFollowUpSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PreboardingFollowUp Management Detail Component', () => {
    let preboardingFollowUpServiceStub: any;
    let mountOptions: MountingOptions<PreboardingFollowUpDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      preboardingFollowUpServiceStub = {
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
          preboardingFollowUpService: () => preboardingFollowUpServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        preboardingFollowUpServiceStub.find.mockResolvedValue(preboardingFollowUpSample);
        route = {
          params: {
            preboardingFollowUpId: `${123}`,
          },
        };
        const wrapper = shallowMount(PreboardingFollowUpDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.preboardingFollowUp).toMatchObject(preboardingFollowUpSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        preboardingFollowUpServiceStub.find.mockResolvedValue(preboardingFollowUpSample);
        const wrapper = shallowMount(PreboardingFollowUpDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
