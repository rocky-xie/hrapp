import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillUpdate from './skill-update.vue';

type SkillUpdateComponentType = InstanceType<typeof SkillUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SkillUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Skill Management Update Component', () => {
    let comp: SkillUpdateComponentType;
    let skillServiceStub: any;

    beforeEach(() => {
      route = {};
      skillServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      skillServiceStub.retrieve.mockResolvedValueOnce([]);

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
          skillService: () => skillServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skill = skillSample;
        skillServiceStub.update.mockResolvedValue(skillSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillServiceStub.update).toHaveBeenCalledWith(skillSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        skillServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skill = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        skillServiceStub.find.mockResolvedValue(skillSample);
        skillServiceStub.retrieve.mockResolvedValue([skillSample]);

        // WHEN
        route = {
          params: {
            skillId: `${skillSample.id}`,
          },
        };
        const wrapper = shallowMount(SkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.skill).toMatchObject(skillSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillServiceStub.find.mockResolvedValue(skillSample);
        const wrapper = shallowMount(SkillUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
