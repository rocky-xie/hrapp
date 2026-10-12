import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentApplicationDetails from './recruitment-application-details.vue';

type RecruitmentApplicationDetailsComponentType = InstanceType<typeof RecruitmentApplicationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentApplicationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('RecruitmentApplication Management Detail Component', () => {
    let recruitmentApplicationServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentApplicationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      recruitmentApplicationServiceStub = {
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
          recruitmentApplicationService: () => recruitmentApplicationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentApplicationServiceStub.find.mockResolvedValue(recruitmentApplicationSample);
        route = {
          params: {
            recruitmentApplicationId: `${123}`,
          },
        };
        const wrapper = shallowMount(RecruitmentApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentApplication).toMatchObject(recruitmentApplicationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentApplicationServiceStub.find.mockResolvedValue(recruitmentApplicationSample);
        const wrapper = shallowMount(RecruitmentApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
