import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonUpdate from './person-update.vue';

type PersonUpdateComponentType = InstanceType<typeof PersonUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personSample = { id: 123 };
const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PersonUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponentType;
    let personServiceStub: any;
    let personSkillServiceStub: any;
    let skillServiceStub: any;
    let skillLevelServiceStub: any;

    beforeEach(() => {
      route = {};
      personServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      personServiceStub.retrieve.mockResolvedValue({ data: [] });
      personSkillServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
        create: vi.fn().mockResolvedValue({}),
        update: vi.fn().mockResolvedValue({}),
        delete: vi.fn().mockResolvedValue({}),
      };
      skillServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
      };
      skillLevelServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
      };

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          personService: () => personServiceStub,
          personSkillService: () => personSkillServiceStub,
          skillService: () => skillServiceStub,
          skillLevelService: () => skillLevelServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PersonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.person = personSample;
        personServiceStub.update.mockResolvedValue(personSample);

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(personServiceStub.update).toHaveBeenCalledWith(personSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        personServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PersonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.person = entity;

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(personServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        personServiceStub.find.mockResolvedValue(personSample);
        personServiceStub.retrieve.mockResolvedValue([personSample]);

        // WHEN
        route = {
          params: {
            personId: `${personSample.id}`,
          },
        };
        const wrapper = shallowMount(PersonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await flushPromises();

        // THEN
        expect(comp.person).toMatchObject(personSample);
        expect(personSkillServiceStub.retrieve).toHaveBeenCalledWith({ 'personId.equals': personSample.id, size: 1000, sort: ['id,asc'] });
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personServiceStub.find.mockResolvedValue(personSample);
        const wrapper = shallowMount(PersonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
