import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentRequisitionVersionDetails from './recruitment-requisition-version-details.vue';

type RecruitmentRequisitionVersionDetailsComponentType = InstanceType<typeof RecruitmentRequisitionVersionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentRequisitionVersionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('RecruitmentRequisitionVersion Management Detail Component', () => {
    let recruitmentRequisitionVersionServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentRequisitionVersionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      recruitmentRequisitionVersionServiceStub = {
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
          recruitmentRequisitionVersionService: () => recruitmentRequisitionVersionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentRequisitionVersionServiceStub.find.mockResolvedValue(recruitmentRequisitionVersionSample);
        route = {
          params: {
            recruitmentRequisitionVersionId: `${123}`,
          },
        };
        const wrapper = shallowMount(RecruitmentRequisitionVersionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentRequisitionVersion).toMatchObject(recruitmentRequisitionVersionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentRequisitionVersionServiceStub.find.mockResolvedValue(recruitmentRequisitionVersionSample);
        const wrapper = shallowMount(RecruitmentRequisitionVersionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
