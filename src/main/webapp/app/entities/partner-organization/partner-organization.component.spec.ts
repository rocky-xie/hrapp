import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PartnerOrganization from './partner-organization.vue';

type PartnerOrganizationComponentType = InstanceType<typeof PartnerOrganization>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PartnerOrganization Management Component', () => {
    let partnerOrganizationServiceStub: any;
    let mountOptions: MountingOptions<PartnerOrganizationComponentType>['global'];

    beforeEach(() => {
      partnerOrganizationServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      partnerOrganizationServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          partnerOrganizationService: () => partnerOrganizationServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        partnerOrganizationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PartnerOrganization, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.partnerOrganizations[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PartnerOrganization, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PartnerOrganizationComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PartnerOrganization, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        partnerOrganizationServiceStub.retrieve.mockReset();
        partnerOrganizationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        partnerOrganizationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.partnerOrganizations[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(partnerOrganizationServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        partnerOrganizationServiceStub.retrieve.mockReset();
        partnerOrganizationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(partnerOrganizationServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.partnerOrganizations[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        partnerOrganizationServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePartnerOrganization();
        await comp.$nextTick(); // clear components

        // THEN
        expect(partnerOrganizationServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(partnerOrganizationServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
