import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ImprovementPlanUpdate from './improvement-plan-update.vue';

type ImprovementPlanUpdateComponentType = InstanceType<typeof ImprovementPlanUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const improvementPlanSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ImprovementPlanUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ImprovementPlan Management Update Component', () => {
    let comp: ImprovementPlanUpdateComponentType;
    let improvementPlanServiceStub: any;

    beforeEach(() => {
      route = {};
      improvementPlanServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      improvementPlanServiceStub.retrieve.mockResolvedValueOnce([]);

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
          improvementPlanService: () => improvementPlanServiceStub,
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillService: () => ({
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
        const wrapper = shallowMount(ImprovementPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.improvementPlan = improvementPlanSample;
        improvementPlanServiceStub.update.mockResolvedValue(improvementPlanSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.update).toHaveBeenCalledWith(improvementPlanSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        improvementPlanServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(ImprovementPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.improvementPlan = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        improvementPlanServiceStub.find.mockResolvedValue(improvementPlanSample);
        improvementPlanServiceStub.retrieve.mockResolvedValue([improvementPlanSample]);

        // WHEN
        route = {
          params: {
            improvementPlanId: `${improvementPlanSample.id}`,
          },
        };
        const wrapper = shallowMount(ImprovementPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.improvementPlan).toMatchObject(improvementPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        improvementPlanServiceStub.find.mockResolvedValue(improvementPlanSample);
        const wrapper = shallowMount(ImprovementPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
