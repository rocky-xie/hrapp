import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import CandidateProfileDetails from './candidate-profile-details.vue';

type CandidateProfileDetailsComponentType = InstanceType<typeof CandidateProfileDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const candidateProfileSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('CandidateProfile Management Detail Component', () => {
    let candidateProfileServiceStub: any;
    let mountOptions: MountingOptions<CandidateProfileDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      candidateProfileServiceStub = {
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
          candidateProfileService: () => candidateProfileServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        candidateProfileServiceStub.find.mockResolvedValue(candidateProfileSample);
        route = {
          params: {
            candidateProfileId: `${123}`,
          },
        };
        const wrapper = shallowMount(CandidateProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.candidateProfile).toMatchObject(candidateProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        candidateProfileServiceStub.find.mockResolvedValue(candidateProfileSample);
        const wrapper = shallowMount(CandidateProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
