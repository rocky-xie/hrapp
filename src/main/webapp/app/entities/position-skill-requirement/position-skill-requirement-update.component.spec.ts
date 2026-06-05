import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionSkillRequirementUpdate from './position-skill-requirement-update.vue';

type PositionSkillRequirementUpdateComponentType = InstanceType<typeof PositionSkillRequirementUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionSkillRequirementSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionSkillRequirementUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PositionSkillRequirement Management Update Component', () => {
    let comp: PositionSkillRequirementUpdateComponentType;
    let positionSkillRequirementServiceStub: any;

    beforeEach(() => {
      route = {};
      positionSkillRequirementServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      positionSkillRequirementServiceStub.retrieve.mockResolvedValueOnce([]);

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
          positionSkillRequirementService: () => positionSkillRequirementServiceStub,
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillLevelService: () => ({
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
        const wrapper = shallowMount(PositionSkillRequirementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionSkillRequirement = positionSkillRequirementSample;
        positionSkillRequirementServiceStub.update.mockResolvedValue(positionSkillRequirementSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.update).toHaveBeenCalledWith(positionSkillRequirementSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        positionSkillRequirementServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionSkillRequirementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionSkillRequirement = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionSkillRequirementServiceStub.find.mockResolvedValue(positionSkillRequirementSample);
        positionSkillRequirementServiceStub.retrieve.mockResolvedValue([positionSkillRequirementSample]);

        // WHEN
        route = {
          params: {
            positionSkillRequirementId: `${positionSkillRequirementSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionSkillRequirementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.positionSkillRequirement).toMatchObject(positionSkillRequirementSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionSkillRequirementServiceStub.find.mockResolvedValue(positionSkillRequirementSample);
        const wrapper = shallowMount(PositionSkillRequirementUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
