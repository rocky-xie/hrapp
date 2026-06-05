import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import EvaluationUpdate from './evaluation-update.vue';

type EvaluationUpdateComponentType = InstanceType<typeof EvaluationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const evaluationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EvaluationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Evaluation Management Update Component', () => {
    let comp: EvaluationUpdateComponentType;
    let evaluationServiceStub: any;

    beforeEach(() => {
      route = {};
      evaluationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      evaluationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          evaluationService: () => evaluationServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          trainingGoalService: () => ({
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
        const wrapper = shallowMount(EvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.evaluation = evaluationSample;
        evaluationServiceStub.update.mockResolvedValue(evaluationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(evaluationServiceStub.update).toHaveBeenCalledWith(evaluationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        evaluationServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(EvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.evaluation = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(evaluationServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        evaluationServiceStub.find.mockResolvedValue(evaluationSample);
        evaluationServiceStub.retrieve.mockResolvedValue([evaluationSample]);

        // WHEN
        route = {
          params: {
            evaluationId: `${evaluationSample.id}`,
          },
        };
        const wrapper = shallowMount(EvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.evaluation).toMatchObject(evaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        evaluationServiceStub.find.mockResolvedValue(evaluationSample);
        const wrapper = shallowMount(EvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
