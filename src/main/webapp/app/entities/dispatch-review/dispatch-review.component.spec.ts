import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import DispatchReview from './dispatch-review.vue';

type DispatchReviewComponentType = InstanceType<typeof DispatchReview>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('DispatchReview Management Component', () => {
    let dispatchReviewServiceStub: any;
    let mountOptions: MountingOptions<DispatchReviewComponentType>['global'];

    beforeEach(() => {
      dispatchReviewServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      dispatchReviewServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          dispatchReviewService: () => dispatchReviewServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dispatchReviewServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(DispatchReview, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.dispatchReviews[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(DispatchReview, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: DispatchReviewComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(DispatchReview, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        dispatchReviewServiceStub.retrieve.mockReset();
        dispatchReviewServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        dispatchReviewServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.dispatchReviews[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(dispatchReviewServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        dispatchReviewServiceStub.retrieve.mockReset();
        dispatchReviewServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(dispatchReviewServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.dispatchReviews[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        dispatchReviewServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDispatchReview();
        await comp.$nextTick(); // clear components

        // THEN
        expect(dispatchReviewServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(dispatchReviewServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
