import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentRequisitionDetails from './recruitment-requisition-details.vue';

type RecruitmentRequisitionDetailsComponentType = InstanceType<typeof RecruitmentRequisitionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentRequisitionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('RecruitmentRequisition Management Detail Component', () => {
    let recruitmentRequisitionServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentRequisitionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      recruitmentRequisitionServiceStub = {
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
          recruitmentRequisitionService: () => recruitmentRequisitionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentRequisitionServiceStub.find.mockResolvedValue(recruitmentRequisitionSample);
        route = {
          params: {
            recruitmentRequisitionId: `${123}`,
          },
        };
        const wrapper = shallowMount(RecruitmentRequisitionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentRequisition).toMatchObject(recruitmentRequisitionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentRequisitionServiceStub.find.mockResolvedValue(recruitmentRequisitionSample);
        const wrapper = shallowMount(RecruitmentRequisitionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
