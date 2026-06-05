import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillLevelUpdate from './skill-level-update.vue';

type SkillLevelUpdateComponentType = InstanceType<typeof SkillLevelUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillLevelSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SkillLevelUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SkillLevel Management Update Component', () => {
    let comp: SkillLevelUpdateComponentType;
    let skillLevelServiceStub: any;

    beforeEach(() => {
      route = {};
      skillLevelServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      skillLevelServiceStub.retrieve.mockResolvedValueOnce([]);

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
        const wrapper = shallowMount(SkillLevelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillLevel = skillLevelSample;
        skillLevelServiceStub.update.mockResolvedValue(skillLevelSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillLevelServiceStub.update).toHaveBeenCalledWith(skillLevelSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        skillLevelServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SkillLevelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillLevel = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillLevelServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        skillLevelServiceStub.find.mockResolvedValue(skillLevelSample);
        skillLevelServiceStub.retrieve.mockResolvedValue([skillLevelSample]);

        // WHEN
        route = {
          params: {
            skillLevelId: `${skillLevelSample.id}`,
          },
        };
        const wrapper = shallowMount(SkillLevelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.skillLevel).toMatchObject(skillLevelSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillLevelServiceStub.find.mockResolvedValue(skillLevelSample);
        const wrapper = shallowMount(SkillLevelUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
