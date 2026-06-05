import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import Position from './position.vue';

type PositionComponentType = InstanceType<typeof Position>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Position Management Component', () => {
    let positionServiceStub: any;
    let mountOptions: MountingOptions<PositionComponentType>['global'];

    beforeEach(() => {
      positionServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      positionServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          positionService: () => positionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Position, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.positions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(Position, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PositionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Position, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        positionServiceStub.retrieve.mockReset();
        positionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        positionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(positionServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.positions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(positionServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        positionServiceStub.retrieve.mockReset();
        positionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(positionServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.positions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(positionServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        positionServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePosition();
        await comp.$nextTick(); // clear components

        // THEN
        expect(positionServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(positionServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
