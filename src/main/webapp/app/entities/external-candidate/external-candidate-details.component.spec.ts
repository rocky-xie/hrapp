import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ExternalCandidateDetails from './external-candidate-details.vue';

type ExternalCandidateDetailsComponentType = InstanceType<typeof ExternalCandidateDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalCandidateSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('ExternalCandidate Management Detail Component', () => {
    let externalCandidateServiceStub: any;
    let mountOptions: MountingOptions<ExternalCandidateDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      externalCandidateServiceStub = {
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
          externalCandidateService: () => externalCandidateServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalCandidateServiceStub.find.mockResolvedValue(externalCandidateSample);
        route = {
          params: {
            externalCandidateId: `${123}`,
          },
        };
        const wrapper = shallowMount(ExternalCandidateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.externalCandidate).toMatchObject(externalCandidateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalCandidateServiceStub.find.mockResolvedValue(externalCandidateSample);
        const wrapper = shallowMount(ExternalCandidateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
