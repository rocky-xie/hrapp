import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillAssessmentUpdate from './skill-assessment-update.vue';

type SkillAssessmentUpdateComponentType = InstanceType<typeof SkillAssessmentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillAssessmentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SkillAssessmentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SkillAssessment Management Update Component', () => {
    let comp: SkillAssessmentUpdateComponentType;
    let skillAssessmentServiceStub: any;

    beforeEach(() => {
      route = {};
      skillAssessmentServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      skillAssessmentServiceStub.retrieve.mockResolvedValueOnce([]);

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
          skillAssessmentService: () => skillAssessmentServiceStub,
          personSkillService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          personService: () => ({
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
        const wrapper = shallowMount(SkillAssessmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillAssessment = skillAssessmentSample;
        skillAssessmentServiceStub.update.mockResolvedValue(skillAssessmentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.update).toHaveBeenCalledWith(skillAssessmentSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        skillAssessmentServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SkillAssessmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillAssessment = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        skillAssessmentServiceStub.find.mockResolvedValue(skillAssessmentSample);
        skillAssessmentServiceStub.retrieve.mockResolvedValue([skillAssessmentSample]);

        // WHEN
        route = {
          params: {
            skillAssessmentId: `${skillAssessmentSample.id}`,
          },
        };
        const wrapper = shallowMount(SkillAssessmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.skillAssessment).toMatchObject(skillAssessmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillAssessmentServiceStub.find.mockResolvedValue(skillAssessmentSample);
        const wrapper = shallowMount(SkillAssessmentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
