import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import Person from './person.vue';

type PersonComponentType = InstanceType<typeof Person>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Person Management Component', () => {
    let personServiceStub: any;
    let mountOptions: MountingOptions<PersonComponentType>['global'];

    beforeEach(() => {
      personServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      personServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          personService: () => personServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        personServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Person, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(personServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.people[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(Person, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(personServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PersonComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Person, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        personServiceStub.retrieve.mockReset();
        personServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        personServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(personServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.people[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(personServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        personServiceStub.retrieve.mockReset();
        personServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(personServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.people[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(personServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        personServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePerson();
        await comp.$nextTick(); // clear components

        // THEN
        expect(personServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(personServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
