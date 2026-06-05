import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import KeyResponsibilityCategoryDetails from './key-responsibility-category-details.vue';

type KeyResponsibilityCategoryDetailsComponentType = InstanceType<typeof KeyResponsibilityCategoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const keyResponsibilityCategorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('KeyResponsibilityCategory Management Detail Component', () => {
    let keyResponsibilityCategoryServiceStub: any;
    let mountOptions: MountingOptions<KeyResponsibilityCategoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      keyResponsibilityCategoryServiceStub = {
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
          keyResponsibilityCategoryService: () => keyResponsibilityCategoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        keyResponsibilityCategoryServiceStub.find.mockResolvedValue(keyResponsibilityCategorySample);
        route = {
          params: {
            keyResponsibilityCategoryId: `${123}`,
          },
        };
        const wrapper = shallowMount(KeyResponsibilityCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.keyResponsibilityCategory).toMatchObject(keyResponsibilityCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        keyResponsibilityCategoryServiceStub.find.mockResolvedValue(keyResponsibilityCategorySample);
        const wrapper = shallowMount(KeyResponsibilityCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
