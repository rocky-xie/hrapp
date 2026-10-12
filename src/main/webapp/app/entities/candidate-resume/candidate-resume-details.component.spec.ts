import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import CandidateResumeDetails from './candidate-resume-details.vue';

type CandidateResumeDetailsComponentType = InstanceType<typeof CandidateResumeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const candidateResumeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('CandidateResume Management Detail Component', () => {
    let candidateResumeServiceStub: any;
    let mountOptions: MountingOptions<CandidateResumeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      candidateResumeServiceStub = {
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
          candidateResumeService: () => candidateResumeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        candidateResumeServiceStub.find.mockResolvedValue(candidateResumeSample);
        route = {
          params: {
            candidateResumeId: `${123}`,
          },
        };
        const wrapper = shallowMount(CandidateResumeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.candidateResume).toMatchObject(candidateResumeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        candidateResumeServiceStub.find.mockResolvedValue(candidateResumeSample);
        const wrapper = shallowMount(CandidateResumeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
