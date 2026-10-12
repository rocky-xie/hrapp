import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PartnerOrganizationDetails from './partner-organization-details.vue';

type PartnerOrganizationDetailsComponentType = InstanceType<typeof PartnerOrganizationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const partnerOrganizationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PartnerOrganization Management Detail Component', () => {
    let partnerOrganizationServiceStub: any;
    let mountOptions: MountingOptions<PartnerOrganizationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      partnerOrganizationServiceStub = {
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
          partnerOrganizationService: () => partnerOrganizationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        partnerOrganizationServiceStub.find.mockResolvedValue(partnerOrganizationSample);
        route = {
          params: {
            partnerOrganizationId: `${123}`,
          },
        };
        const wrapper = shallowMount(PartnerOrganizationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.partnerOrganization).toMatchObject(partnerOrganizationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        partnerOrganizationServiceStub.find.mockResolvedValue(partnerOrganizationSample);
        const wrapper = shallowMount(PartnerOrganizationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
