import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SuccessionCandidateDetails from './succession-candidate-details.vue';

type SuccessionCandidateDetailsComponentType = InstanceType<typeof SuccessionCandidateDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const successionCandidateSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('SuccessionCandidate Management Detail Component', () => {
    let successionCandidateServiceStub: any;
    let mountOptions: MountingOptions<SuccessionCandidateDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      successionCandidateServiceStub = {
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
          successionCandidateService: () => successionCandidateServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        successionCandidateServiceStub.find.mockResolvedValue(successionCandidateSample);
        route = {
          params: {
            successionCandidateId: `${123}`,
          },
        };
        const wrapper = shallowMount(SuccessionCandidateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.successionCandidate).toMatchObject(successionCandidateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        successionCandidateServiceStub.find.mockResolvedValue(successionCandidateSample);
        const wrapper = shallowMount(SuccessionCandidateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
