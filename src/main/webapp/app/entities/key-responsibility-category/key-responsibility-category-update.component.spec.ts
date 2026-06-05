import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import KeyResponsibilityCategoryUpdate from './key-responsibility-category-update.vue';

type KeyResponsibilityCategoryUpdateComponentType = InstanceType<typeof KeyResponsibilityCategoryUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const keyResponsibilityCategorySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<KeyResponsibilityCategoryUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('KeyResponsibilityCategory Management Update Component', () => {
    let comp: KeyResponsibilityCategoryUpdateComponentType;
    let keyResponsibilityCategoryServiceStub: any;

    beforeEach(() => {
      route = {};
      keyResponsibilityCategoryServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValueOnce([]);

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
          keyResponsibilityCategoryService: () => keyResponsibilityCategoryServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(KeyResponsibilityCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.keyResponsibilityCategory = keyResponsibilityCategorySample;
        keyResponsibilityCategoryServiceStub.update.mockResolvedValue(keyResponsibilityCategorySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.update).toHaveBeenCalledWith(keyResponsibilityCategorySample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        keyResponsibilityCategoryServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(KeyResponsibilityCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.keyResponsibilityCategory = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        keyResponsibilityCategoryServiceStub.find.mockResolvedValue(keyResponsibilityCategorySample);
        keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue([keyResponsibilityCategorySample]);

        // WHEN
        route = {
          params: {
            keyResponsibilityCategoryId: `${keyResponsibilityCategorySample.id}`,
          },
        };
        const wrapper = shallowMount(KeyResponsibilityCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.keyResponsibilityCategory).toMatchObject(keyResponsibilityCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        keyResponsibilityCategoryServiceStub.find.mockResolvedValue(keyResponsibilityCategorySample);
        const wrapper = shallowMount(KeyResponsibilityCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
