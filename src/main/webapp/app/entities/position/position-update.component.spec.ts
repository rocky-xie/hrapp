import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionUpdate from './position-update.vue';

type PositionUpdateComponentType = InstanceType<typeof PositionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionSample = { id: 123 };
const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Position Management Update Component', () => {
    let comp: PositionUpdateComponentType;
    let positionServiceStub: any;
    let positionSkillRequirementServiceStub: any;
    let positionAssignmentServiceStub: any;
    let skillServiceStub: any;
    let skillLevelServiceStub: any;
    let personServiceStub: any;

    beforeEach(() => {
      route = {};
      positionServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      positionServiceStub.retrieve.mockResolvedValue({ data: [] });
      positionSkillRequirementServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
        create: vi.fn().mockResolvedValue({}),
        update: vi.fn().mockResolvedValue({}),
        delete: vi.fn().mockResolvedValue({}),
      };
      positionAssignmentServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
        create: vi.fn().mockResolvedValue({}),
        update: vi.fn().mockResolvedValue({}),
        delete: vi.fn().mockResolvedValue({}),
      };
      skillServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
      };
      skillLevelServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
      };
      personServiceStub = {
        retrieve: vi.fn().mockResolvedValue({ data: [] }),
      };

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
          positionService: () => positionServiceStub,
          positionSkillRequirementService: () => positionSkillRequirementServiceStub,
          positionAssignmentService: () => positionAssignmentServiceStub,
          skillService: () => skillServiceStub,
          skillLevelService: () => skillLevelServiceStub,
          personService: () => personServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PositionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.position = positionSample;
        positionServiceStub.update.mockResolvedValue(positionSample);

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(positionServiceStub.update).toHaveBeenCalledWith(positionSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        positionServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.position = entity;

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(positionServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionServiceStub.find.mockResolvedValue(positionSample);
        positionServiceStub.retrieve.mockResolvedValue([positionSample]);

        // WHEN
        route = {
          params: {
            positionId: `${positionSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await flushPromises();

        // THEN
        expect(comp.position).toMatchObject(positionSample);
        expect(positionSkillRequirementServiceStub.retrieve).toHaveBeenCalledWith({
          'positionId.equals': positionSample.id,
          size: 1000,
          sort: ['id,asc'],
        });
        expect(positionAssignmentServiceStub.retrieve).toHaveBeenCalledWith({
          'positionId.equals': positionSample.id,
          size: 1000,
          sort: ['id,asc'],
        });
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionServiceStub.find.mockResolvedValue(positionSample);
        const wrapper = shallowMount(PositionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
