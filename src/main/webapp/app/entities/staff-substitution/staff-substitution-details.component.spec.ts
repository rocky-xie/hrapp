import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import StaffSubstitutionDetails from './staff-substitution-details.vue';

type StaffSubstitutionDetailsComponentType = InstanceType<typeof StaffSubstitutionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const staffSubstitutionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('StaffSubstitution Management Detail Component', () => {
    let staffSubstitutionServiceStub: any;
    let mountOptions: MountingOptions<StaffSubstitutionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      staffSubstitutionServiceStub = {
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
          staffSubstitutionService: () => staffSubstitutionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        staffSubstitutionServiceStub.find.mockResolvedValue(staffSubstitutionSample);
        route = {
          params: {
            staffSubstitutionId: `${123}`,
          },
        };
        const wrapper = shallowMount(StaffSubstitutionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.staffSubstitution).toMatchObject(staffSubstitutionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        staffSubstitutionServiceStub.find.mockResolvedValue(staffSubstitutionSample);
        const wrapper = shallowMount(StaffSubstitutionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
