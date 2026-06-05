import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonSkillDetails from './person-skill-details.vue';

type PersonSkillDetailsComponentType = InstanceType<typeof PersonSkillDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personSkillSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('PersonSkill Management Detail Component', () => {
    let personSkillServiceStub: any;
    let mountOptions: MountingOptions<PersonSkillDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      personSkillServiceStub = {
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
          personSkillService: () => personSkillServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        personSkillServiceStub.find.mockResolvedValue(personSkillSample);
        route = {
          params: {
            personSkillId: `${123}`,
          },
        };
        const wrapper = shallowMount(PersonSkillDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.personSkill).toMatchObject(personSkillSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personSkillServiceStub.find.mockResolvedValue(personSkillSample);
        const wrapper = shallowMount(PersonSkillDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
