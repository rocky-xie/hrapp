import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionRiskEvaluationUpdate from './position-risk-evaluation-update.vue';

type PositionRiskEvaluationUpdateComponentType = InstanceType<typeof PositionRiskEvaluationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionRiskEvaluationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionRiskEvaluationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PositionRiskEvaluation Management Update Component', () => {
    let comp: PositionRiskEvaluationUpdateComponentType;
    let positionRiskEvaluationServiceStub: any;

    beforeEach(() => {
      route = {};
      positionRiskEvaluationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
        evaluate: vi.fn(),
      };
      positionRiskEvaluationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          positionRiskEvaluationService: () => positionRiskEvaluationServiceStub,
          positionService: () => ({
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
        const wrapper = shallowMount(PositionRiskEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionRiskEvaluation = positionRiskEvaluationSample;
        positionRiskEvaluationServiceStub.update.mockResolvedValue(positionRiskEvaluationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionRiskEvaluationServiceStub.update).toHaveBeenCalledWith(positionRiskEvaluationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call evaluate service on save for new entity', async () => {
        // GIVEN
        const entity = {
          position: { id: 1 },
          documentStatus: 'AVAILABLE',
          customerOrSystemDependency: 'LOW',
          successionReadiness: 'IMMEDIATE',
        };
        positionRiskEvaluationServiceStub.evaluate.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionRiskEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionRiskEvaluation = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionRiskEvaluationServiceStub.evaluate).toHaveBeenCalledWith(1, 'AVAILABLE', 'LOW', 'IMMEDIATE');
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionRiskEvaluationServiceStub.find.mockResolvedValue(positionRiskEvaluationSample);
        positionRiskEvaluationServiceStub.retrieve.mockResolvedValue([positionRiskEvaluationSample]);

        // WHEN
        route = {
          params: {
            positionRiskEvaluationId: `${positionRiskEvaluationSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionRiskEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.positionRiskEvaluation).toMatchObject(positionRiskEvaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionRiskEvaluationServiceStub.find.mockResolvedValue(positionRiskEvaluationSample);
        const wrapper = shallowMount(PositionRiskEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
