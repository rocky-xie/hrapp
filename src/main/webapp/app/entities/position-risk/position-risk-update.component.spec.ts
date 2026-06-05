import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionRiskUpdate from './position-risk-update.vue';

type PositionRiskUpdateComponentType = InstanceType<typeof PositionRiskUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionRiskSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionRiskUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PositionRisk Management Update Component', () => {
    let comp: PositionRiskUpdateComponentType;
    let positionRiskServiceStub: any;

    beforeEach(() => {
      route = {};
      positionRiskServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      positionRiskServiceStub.retrieve.mockResolvedValueOnce([]);

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
          positionRiskService: () => positionRiskServiceStub,
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          keyResponsibilityCategoryService: () => ({
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
        const wrapper = shallowMount(PositionRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionRisk = positionRiskSample;
        positionRiskServiceStub.update.mockResolvedValue(positionRiskSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionRiskServiceStub.update).toHaveBeenCalledWith(positionRiskSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        positionRiskServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionRisk = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionRiskServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionRiskServiceStub.find.mockResolvedValue(positionRiskSample);
        positionRiskServiceStub.retrieve.mockResolvedValue([positionRiskSample]);

        // WHEN
        route = {
          params: {
            positionRiskId: `${positionRiskSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.positionRisk).toMatchObject(positionRiskSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionRiskServiceStub.find.mockResolvedValue(positionRiskSample);
        const wrapper = shallowMount(PositionRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
