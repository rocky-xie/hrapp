import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionDetails from './position-details.vue';

type PositionDetailsComponentType = InstanceType<typeof PositionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Position Management Detail Component', () => {
    let positionServiceStub: any;
    let mountOptions: MountingOptions<PositionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionServiceStub = {
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
          positionService: () => positionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionServiceStub.find.mockResolvedValue(positionSample);
        route = {
          params: {
            positionId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.position).toMatchObject(positionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionServiceStub.find.mockResolvedValue(positionSample);
        const wrapper = shallowMount(PositionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
