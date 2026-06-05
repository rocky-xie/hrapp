import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrainingGoal from './training-goal.vue';

type TrainingGoalComponentType = InstanceType<typeof TrainingGoal>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('TrainingGoal Management Component', () => {
    let trainingGoalServiceStub: any;
    let mountOptions: MountingOptions<TrainingGoalComponentType>['global'];

    beforeEach(() => {
      trainingGoalServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      trainingGoalServiceStub.retrieve.mockResolvedValue({ headers: {} });

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          trainingGoalService: () => trainingGoalServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        trainingGoalServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(TrainingGoal, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(trainingGoalServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.trainingGoals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(TrainingGoal, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(trainingGoalServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: TrainingGoalComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(TrainingGoal, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        trainingGoalServiceStub.retrieve.mockReset();
        trainingGoalServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        trainingGoalServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(trainingGoalServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.trainingGoals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(trainingGoalServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        trainingGoalServiceStub.retrieve.mockReset();
        trainingGoalServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(trainingGoalServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.trainingGoals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(trainingGoalServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        trainingGoalServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTrainingGoal();
        await comp.$nextTick(); // clear components

        // THEN
        expect(trainingGoalServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(trainingGoalServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
