import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import StaffSubstitutionUpdate from './staff-substitution-update.vue';

type StaffSubstitutionUpdateComponentType = InstanceType<typeof StaffSubstitutionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const staffSubstitutionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<StaffSubstitutionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('StaffSubstitution Management Update Component', () => {
    let comp: StaffSubstitutionUpdateComponentType;
    let staffSubstitutionServiceStub: any;

    beforeEach(() => {
      route = {};
      staffSubstitutionServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
        calculate: vi.fn(),
      };
      staffSubstitutionServiceStub.retrieve.mockResolvedValueOnce([]);

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
          staffSubstitutionService: () => staffSubstitutionServiceStub,
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          positionAssignmentService: () => ({
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
        const wrapper = shallowMount(StaffSubstitutionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.staffSubstitution = staffSubstitutionSample;
        staffSubstitutionServiceStub.update.mockResolvedValue(staffSubstitutionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(staffSubstitutionServiceStub.update).toHaveBeenCalledWith(staffSubstitutionSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call calculate service on save for new entity', async () => {
        // GIVEN
        const entity = {
          position: { id: 1 },
          candidatePerson: { id: 2 },
          thresholdRate: 80,
        };
        staffSubstitutionServiceStub.calculate.mockResolvedValue(entity);
        const wrapper = shallowMount(StaffSubstitutionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.staffSubstitution = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(staffSubstitutionServiceStub.calculate).toHaveBeenCalledWith(1, 2, 80);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        staffSubstitutionServiceStub.find.mockResolvedValue(staffSubstitutionSample);
        staffSubstitutionServiceStub.retrieve.mockResolvedValue([staffSubstitutionSample]);

        // WHEN
        route = {
          params: {
            staffSubstitutionId: `${staffSubstitutionSample.id}`,
          },
        };
        const wrapper = shallowMount(StaffSubstitutionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.staffSubstitution).toMatchObject(staffSubstitutionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        staffSubstitutionServiceStub.find.mockResolvedValue(staffSubstitutionSample);
        const wrapper = shallowMount(StaffSubstitutionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
