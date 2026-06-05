import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionAssignmentDetails from './position-assignment-details.vue';

type PositionAssignmentDetailsComponentType = InstanceType<typeof PositionAssignmentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionAssignmentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PositionAssignment Management Detail Component', () => {
    let positionAssignmentServiceStub: any;
    let mountOptions: MountingOptions<PositionAssignmentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      positionAssignmentServiceStub = {
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
          positionAssignmentService: () => positionAssignmentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionAssignmentServiceStub.find.mockResolvedValue(positionAssignmentSample);
        route = {
          params: {
            positionAssignmentId: `${123}`,
          },
        };
        const wrapper = shallowMount(PositionAssignmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.positionAssignment).toMatchObject(positionAssignmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionAssignmentServiceStub.find.mockResolvedValue(positionAssignmentSample);
        const wrapper = shallowMount(PositionAssignmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
