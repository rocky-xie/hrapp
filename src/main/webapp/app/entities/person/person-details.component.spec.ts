import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonDetails from './person-details.vue';

type PersonDetailsComponentType = InstanceType<typeof PersonDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Person Management Detail Component', () => {
    let personServiceStub: any;
    let mountOptions: MountingOptions<PersonDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      personServiceStub = {
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
          personService: () => personServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        personServiceStub.find.mockResolvedValue(personSample);
        route = {
          params: {
            personId: `${123}`,
          },
        };
        const wrapper = shallowMount(PersonDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.person).toMatchObject(personSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personServiceStub.find.mockResolvedValue(personSample);
        const wrapper = shallowMount(PersonDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
