import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentRequisitionVersion from './recruitment-requisition-version.vue';

type RecruitmentRequisitionVersionComponentType = InstanceType<typeof RecruitmentRequisitionVersion>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('RecruitmentRequisitionVersion Management Component', () => {
    let recruitmentRequisitionVersionServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentRequisitionVersionComponentType>['global'];

    beforeEach(() => {
      recruitmentRequisitionVersionServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      recruitmentRequisitionVersionServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          recruitmentRequisitionVersionService: () => recruitmentRequisitionVersionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentRequisitionVersionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(RecruitmentRequisitionVersion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.recruitmentRequisitionVersions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(RecruitmentRequisitionVersion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: RecruitmentRequisitionVersionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(RecruitmentRequisitionVersion, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        recruitmentRequisitionVersionServiceStub.retrieve.mockReset();
        recruitmentRequisitionVersionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        recruitmentRequisitionVersionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.recruitmentRequisitionVersions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        recruitmentRequisitionVersionServiceStub.retrieve.mockReset();
        recruitmentRequisitionVersionServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(recruitmentRequisitionVersionServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.recruitmentRequisitionVersions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        recruitmentRequisitionVersionServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeRecruitmentRequisitionVersion();
        await comp.$nextTick(); // clear components

        // THEN
        expect(recruitmentRequisitionVersionServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(recruitmentRequisitionVersionServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
