import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentRequisitionMemberDetails from './recruitment-requisition-member-details.vue';

type RecruitmentRequisitionMemberDetailsComponentType = InstanceType<typeof RecruitmentRequisitionMemberDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentRequisitionMemberSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('RecruitmentRequisitionMember Management Detail Component', () => {
    let recruitmentRequisitionMemberServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentRequisitionMemberDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      recruitmentRequisitionMemberServiceStub = {
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
          recruitmentRequisitionMemberService: () => recruitmentRequisitionMemberServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentRequisitionMemberServiceStub.find.mockResolvedValue(recruitmentRequisitionMemberSample);
        route = {
          params: {
            recruitmentRequisitionMemberId: `${123}`,
          },
        };
        const wrapper = shallowMount(RecruitmentRequisitionMemberDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentRequisitionMember).toMatchObject(recruitmentRequisitionMemberSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentRequisitionMemberServiceStub.find.mockResolvedValue(recruitmentRequisitionMemberSample);
        const wrapper = shallowMount(RecruitmentRequisitionMemberDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
