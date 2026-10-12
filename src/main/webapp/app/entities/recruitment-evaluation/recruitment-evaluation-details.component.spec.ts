import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentEvaluationDetails from './recruitment-evaluation-details.vue';

type RecruitmentEvaluationDetailsComponentType = InstanceType<typeof RecruitmentEvaluationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentEvaluationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('RecruitmentEvaluation Management Detail Component', () => {
    let recruitmentEvaluationServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentEvaluationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      recruitmentEvaluationServiceStub = {
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
          recruitmentEvaluationService: () => recruitmentEvaluationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentEvaluationServiceStub.find.mockResolvedValue(recruitmentEvaluationSample);
        route = {
          params: {
            recruitmentEvaluationId: `${123}`,
          },
        };
        const wrapper = shallowMount(RecruitmentEvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentEvaluation).toMatchObject(recruitmentEvaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentEvaluationServiceStub.find.mockResolvedValue(recruitmentEvaluationSample);
        const wrapper = shallowMount(RecruitmentEvaluationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
