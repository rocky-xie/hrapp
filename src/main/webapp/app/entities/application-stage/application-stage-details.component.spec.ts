import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ApplicationStageDetails from './application-stage-details.vue';

type ApplicationStageDetailsComponentType = InstanceType<typeof ApplicationStageDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationStageSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('ApplicationStage Management Detail Component', () => {
    let applicationStageServiceStub: any;
    let mountOptions: MountingOptions<ApplicationStageDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      applicationStageServiceStub = {
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
          applicationStageService: () => applicationStageServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationStageServiceStub.find.mockResolvedValue(applicationStageSample);
        route = {
          params: {
            applicationStageId: `${123}`,
          },
        };
        const wrapper = shallowMount(ApplicationStageDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.applicationStage).toMatchObject(applicationStageSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationStageServiceStub.find.mockResolvedValue(applicationStageSample);
        const wrapper = shallowMount(ApplicationStageDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
