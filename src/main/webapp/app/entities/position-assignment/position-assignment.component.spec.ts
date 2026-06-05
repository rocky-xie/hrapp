import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionAssignment from './position-assignment.vue';

type PositionAssignmentComponentType = InstanceType<typeof PositionAssignment>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PositionAssignment Management Component', () => {
    let positionAssignmentServiceStub: any;
    let mountOptions: MountingOptions<PositionAssignmentComponentType>['global'];

    beforeEach(() => {
      positionAssignmentServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      positionAssignmentServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          positionAssignmentService: () => positionAssignmentServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionAssignmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PositionAssignment, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.positionAssignments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PositionAssignment, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PositionAssignmentComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PositionAssignment, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        positionAssignmentServiceStub.retrieve.mockReset();
        positionAssignmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        positionAssignmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.positionAssignments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(positionAssignmentServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        positionAssignmentServiceStub.retrieve.mockReset();
        positionAssignmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(positionAssignmentServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.positionAssignments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(positionAssignmentServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        positionAssignmentServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePositionAssignment();
        await comp.$nextTick(); // clear components

        // THEN
        expect(positionAssignmentServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(positionAssignmentServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
