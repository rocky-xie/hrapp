import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionAssignmentUpdate from './position-assignment-update.vue';

type PositionAssignmentUpdateComponentType = InstanceType<typeof PositionAssignmentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionAssignmentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionAssignmentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PositionAssignment Management Update Component', () => {
    let comp: PositionAssignmentUpdateComponentType;
    let positionAssignmentServiceStub: any;

    beforeEach(() => {
      route = {};
      positionAssignmentServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      positionAssignmentServiceStub.retrieve.mockResolvedValueOnce([]);

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
          positionAssignmentService: () => positionAssignmentServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
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
        const wrapper = shallowMount(PositionAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionAssignment = positionAssignmentSample;
        positionAssignmentServiceStub.update.mockResolvedValue(positionAssignmentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.update).toHaveBeenCalledWith(positionAssignmentSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        positionAssignmentServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionAssignment = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionAssignmentServiceStub.find.mockResolvedValue(positionAssignmentSample);
        positionAssignmentServiceStub.retrieve.mockResolvedValue([positionAssignmentSample]);

        // WHEN
        route = {
          params: {
            positionAssignmentId: `${positionAssignmentSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.positionAssignment).toMatchObject(positionAssignmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionAssignmentServiceStub.find.mockResolvedValue(positionAssignmentSample);
        const wrapper = shallowMount(PositionAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
