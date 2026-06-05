import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrainingRecordUpdate from './training-record-update.vue';

type TrainingRecordUpdateComponentType = InstanceType<typeof TrainingRecordUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trainingRecordSample = { id: 123 };
const personSample = { id: 1, personName: 'person1' };
const anotherPersonSample = { id: 2, personName: 'person2' };
const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TrainingRecordUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TrainingRecord Management Update Component', () => {
    let comp: TrainingRecordUpdateComponentType;
    let trainingRecordServiceStub: any;

    beforeEach(() => {
      route = {};
      trainingRecordServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      trainingRecordServiceStub.retrieve.mockResolvedValue({ data: [] });

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
          trainingRecordService: () => trainingRecordServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [personSample, anotherPersonSample] }),
          }),
          trainingGoalService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
          }),
          positionService: () => ({
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
        const wrapper = shallowMount(TrainingRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingRecord = trainingRecordSample;
        comp.selectedPeople = [personSample];
        trainingRecordServiceStub.update.mockResolvedValue(trainingRecordSample);

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingRecordServiceStub.update).toHaveBeenCalledWith({ ...trainingRecordSample, person: personSample });
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        trainingRecordServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TrainingRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingRecord = entity;
        comp.selectedPeople = [personSample];

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingRecordServiceStub.create).toHaveBeenCalledWith({ ...entity, person: personSample });
        expect(comp.isSaving).toEqual(false);
      });

      it('Should create one training record per selected person', async () => {
        // GIVEN
        const entity = { topic: 'topic' };
        trainingRecordServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TrainingRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trainingRecord = entity;
        comp.selectedPeople = [personSample, anotherPersonSample];

        // WHEN
        comp.save();
        await flushPromises();

        // THEN
        expect(trainingRecordServiceStub.create).toHaveBeenCalledTimes(2);
        expect(trainingRecordServiceStub.create).toHaveBeenNthCalledWith(1, { ...entity, person: personSample });
        expect(trainingRecordServiceStub.create).toHaveBeenNthCalledWith(2, { ...entity, person: anotherPersonSample });
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        trainingRecordServiceStub.find.mockResolvedValue({ ...trainingRecordSample, person: personSample });
        trainingRecordServiceStub.retrieve.mockResolvedValue([trainingRecordSample]);

        // WHEN
        route = {
          params: {
            trainingRecordId: `${trainingRecordSample.id}`,
          },
        };
        const wrapper = shallowMount(TrainingRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await flushPromises();

        // THEN
        expect(comp.trainingRecord).toMatchObject(trainingRecordSample);
        expect(comp.selectedPeople).toEqual([personSample]);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trainingRecordServiceStub.find.mockResolvedValue(trainingRecordSample);
        const wrapper = shallowMount(TrainingRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
