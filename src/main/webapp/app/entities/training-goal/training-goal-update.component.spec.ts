import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrainingGoalUpdate from './training-goal-update.vue';

type TrainingGoalUpdateComponentType = InstanceType<typeof TrainingGoalUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trainingGoalSample = { id: 123 };
const personSample = { id: 1, personName: 'person1' };
const anotherPersonSample = { id: 2, personName: 'person2' };
const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TrainingGoalUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TrainingGoal Management Update Component', () => {
    let comp: TrainingGoalUpdateComponentType;
    let trainingGoalServiceStub: any;

    beforeEach(() => {
      route = {};
      trainingGoalServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      trainingGoalServiceStub.retrieve.mockResolvedValue({ data: [] });

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
          trainingGoalService: () => trainingGoalServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [personSample, anotherPersonSample] }),
          }),
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
          }),
          skillService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
          }),
          skillLevelService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
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
        const wrapper = shallowMount(TrainingGoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingGoal = trainingGoalSample;
        comp.selectedPeople = [personSample];
        trainingGoalServiceStub.update.mockResolvedValue(trainingGoalSample);

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingGoalServiceStub.update).toHaveBeenCalledWith({ ...trainingGoalSample, person: personSample });
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        trainingGoalServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TrainingGoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingGoal = entity;
        comp.selectedPeople = [personSample];

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingGoalServiceStub.create).toHaveBeenCalledWith({ ...entity, person: personSample });
        expect(comp.isSaving).toEqual(false);
      });

      it('Should create one training goal per selected person', async () => {
        // GIVEN
        const entity = { goalName: 'goal' };
        trainingGoalServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TrainingGoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingGoal = entity;
        comp.selectedPeople = [personSample, anotherPersonSample];

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingGoalServiceStub.create).toHaveBeenCalledTimes(2);
        expect(trainingGoalServiceStub.create).toHaveBeenNthCalledWith(1, { ...entity, person: personSample });
        expect(trainingGoalServiceStub.create).toHaveBeenNthCalledWith(2, { ...entity, person: anotherPersonSample });
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        trainingGoalServiceStub.find.mockResolvedValue({ ...trainingGoalSample, person: personSample });
        trainingGoalServiceStub.retrieve.mockResolvedValue([trainingGoalSample]);

        // WHEN
        route = {
          params: {
            trainingGoalId: `${trainingGoalSample.id}`,
          },
        };
        const wrapper = shallowMount(TrainingGoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await flushPromises();

        // THEN
        expect(comp.trainingGoal).toMatchObject(trainingGoalSample);
        expect(comp.selectedPeople).toEqual([personSample]);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trainingGoalServiceStub.find.mockResolvedValue(trainingGoalSample);
        const wrapper = shallowMount(TrainingGoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
