import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import DispatchAssignmentDetails from './dispatch-assignment-details.vue';

type DispatchAssignmentDetailsComponentType = InstanceType<typeof DispatchAssignmentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dispatchAssignmentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('DispatchAssignment Management Detail Component', () => {
    let dispatchAssignmentServiceStub: any;
    let mountOptions: MountingOptions<DispatchAssignmentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dispatchAssignmentServiceStub = {
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
          dispatchAssignmentService: () => dispatchAssignmentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dispatchAssignmentServiceStub.find.mockResolvedValue(dispatchAssignmentSample);
        route = {
          params: {
            dispatchAssignmentId: `${123}`,
          },
        };
        const wrapper = shallowMount(DispatchAssignmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dispatchAssignment).toMatchObject(dispatchAssignmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dispatchAssignmentServiceStub.find.mockResolvedValue(dispatchAssignmentSample);
        const wrapper = shallowMount(DispatchAssignmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
