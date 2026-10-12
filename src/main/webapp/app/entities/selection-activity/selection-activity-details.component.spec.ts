import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SelectionActivityDetails from './selection-activity-details.vue';

type SelectionActivityDetailsComponentType = InstanceType<typeof SelectionActivityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const selectionActivitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('SelectionActivity Management Detail Component', () => {
    let selectionActivityServiceStub: any;
    let mountOptions: MountingOptions<SelectionActivityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      selectionActivityServiceStub = {
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
          selectionActivityService: () => selectionActivityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        selectionActivityServiceStub.find.mockResolvedValue(selectionActivitySample);
        route = {
          params: {
            selectionActivityId: `${123}`,
          },
        };
        const wrapper = shallowMount(SelectionActivityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.selectionActivity).toMatchObject(selectionActivitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        selectionActivityServiceStub.find.mockResolvedValue(selectionActivitySample);
        const wrapper = shallowMount(SelectionActivityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
