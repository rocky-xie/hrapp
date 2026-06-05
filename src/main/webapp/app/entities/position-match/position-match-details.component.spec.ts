import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionMatchDetails from './position-match-details.vue';

type PositionMatchDetailsComponentType = InstanceType<typeof PositionMatchDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionMatchSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PositionMatch Management Detail Component', () => {
    let positionMatchServiceStub: any;
    let mountOptions: MountingOptions<PositionMatchDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionMatchServiceStub = {
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
          positionMatchService: () => positionMatchServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionMatchServiceStub.find.mockResolvedValue(positionMatchSample);
        route = {
          params: {
            positionMatchId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionMatchDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.positionMatch).toMatchObject(positionMatchSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionMatchServiceStub.find.mockResolvedValue(positionMatchSample);
        const wrapper = shallowMount(PositionMatchDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
