import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonSkillUpdate from './person-skill-update.vue';

type PersonSkillUpdateComponentType = InstanceType<typeof PersonSkillUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personSkillSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PersonSkillUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PersonSkill Management Update Component', () => {
    let comp: PersonSkillUpdateComponentType;
    let personSkillServiceStub: any;

    beforeEach(() => {
      route = {};
      personSkillServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      personSkillServiceStub.retrieve.mockResolvedValueOnce([]);

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
          personSkillService: () => personSkillServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillLevelService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PersonSkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.personSkill = personSkillSample;
        personSkillServiceStub.update.mockResolvedValue(personSkillSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personSkillServiceStub.update).toHaveBeenCalledWith(personSkillSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        personSkillServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PersonSkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.personSkill = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personSkillServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        personSkillServiceStub.find.mockResolvedValue(personSkillSample);
        personSkillServiceStub.retrieve.mockResolvedValue([personSkillSample]);

        // WHEN
        route = {
          params: {
            personSkillId: `${personSkillSample.id}`,
          },
        };
        const wrapper = shallowMount(PersonSkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.personSkill).toMatchObject(personSkillSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personSkillServiceStub.find.mockResolvedValue(personSkillSample);
        const wrapper = shallowMount(PersonSkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
