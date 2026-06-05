import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonRiskDetails from './person-risk-details.vue';

type PersonRiskDetailsComponentType = InstanceType<typeof PersonRiskDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personRiskSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PersonRisk Management Detail Component', () => {
    let personRiskServiceStub: any;
    let mountOptions: MountingOptions<PersonRiskDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      personRiskServiceStub = {
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
          personRiskService: () => personRiskServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        personRiskServiceStub.find.mockResolvedValue(personRiskSample);
        route = {
          params: {
            personRiskId: `${123}`,
          },
        };
        const wrapper = shallowMount(PersonRiskDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.personRisk).toMatchObject(personRiskSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personRiskServiceStub.find.mockResolvedValue(personRiskSample);
        const wrapper = shallowMount(PersonRiskDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
