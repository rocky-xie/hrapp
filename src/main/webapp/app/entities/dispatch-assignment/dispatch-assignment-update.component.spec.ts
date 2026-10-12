import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import DispatchAssignmentUpdate from './dispatch-assignment-update.vue';

type DispatchAssignmentUpdateComponentType = InstanceType<typeof DispatchAssignmentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dispatchAssignmentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DispatchAssignmentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DispatchAssignment Management Update Component', () => {
    let comp: DispatchAssignmentUpdateComponentType;
    let dispatchAssignmentServiceStub: any;

    beforeEach(() => {
      route = {};
      dispatchAssignmentServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      dispatchAssignmentServiceStub.retrieve.mockResolvedValueOnce([]);

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
          dispatchAssignmentService: () => dispatchAssignmentServiceStub,
          recruitmentApplicationService: () => ({
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
        const wrapper = shallowMount(DispatchAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dispatchAssignment = dispatchAssignmentSample;
        dispatchAssignmentServiceStub.update.mockResolvedValue(dispatchAssignmentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dispatchAssignmentServiceStub.update).toHaveBeenCalledWith(dispatchAssignmentSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dispatchAssignmentServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(DispatchAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dispatchAssignment = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dispatchAssignmentServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dispatchAssignmentServiceStub.find.mockResolvedValue(dispatchAssignmentSample);
        dispatchAssignmentServiceStub.retrieve.mockResolvedValue([dispatchAssignmentSample]);

        // WHEN
        route = {
          params: {
            dispatchAssignmentId: `${dispatchAssignmentSample.id}`,
          },
        };
        const wrapper = shallowMount(DispatchAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dispatchAssignment).toMatchObject(dispatchAssignmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dispatchAssignmentServiceStub.find.mockResolvedValue(dispatchAssignmentSample);
        const wrapper = shallowMount(DispatchAssignmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
