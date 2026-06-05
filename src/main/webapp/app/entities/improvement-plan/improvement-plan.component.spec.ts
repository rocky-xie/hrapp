import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ImprovementPlan from './improvement-plan.vue';

type ImprovementPlanComponentType = InstanceType<typeof ImprovementPlan>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ImprovementPlan Management Component', () => {
    let improvementPlanServiceStub: any;
    let mountOptions: MountingOptions<ImprovementPlanComponentType>['global'];

    beforeEach(() => {
      improvementPlanServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      improvementPlanServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          improvementPlanService: () => improvementPlanServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        improvementPlanServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ImprovementPlan, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.improvementPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ImprovementPlan, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ImprovementPlanComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ImprovementPlan, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        improvementPlanServiceStub.retrieve.mockReset();
        improvementPlanServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        improvementPlanServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.improvementPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(improvementPlanServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        improvementPlanServiceStub.retrieve.mockReset();
        improvementPlanServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(improvementPlanServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.improvementPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(improvementPlanServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        improvementPlanServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeImprovementPlan();
        await comp.$nextTick(); // clear components

        // THEN
        expect(improvementPlanServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(improvementPlanServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
