import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import RecruitmentRequisitionMember from './recruitment-requisition-member.vue';

type RecruitmentRequisitionMemberComponentType = InstanceType<typeof RecruitmentRequisitionMember>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('RecruitmentRequisitionMember Management Component', () => {
    let recruitmentRequisitionMemberServiceStub: any;
    let mountOptions: MountingOptions<RecruitmentRequisitionMemberComponentType>['global'];

    beforeEach(() => {
      recruitmentRequisitionMemberServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          recruitmentRequisitionMemberService: () => recruitmentRequisitionMemberServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(RecruitmentRequisitionMember, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.recruitmentRequisitionMembers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(RecruitmentRequisitionMember, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: RecruitmentRequisitionMemberComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(RecruitmentRequisitionMember, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        recruitmentRequisitionMemberServiceStub.retrieve.mockReset();
        recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.recruitmentRequisitionMembers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        recruitmentRequisitionMemberServiceStub.retrieve.mockReset();
        recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(recruitmentRequisitionMemberServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.recruitmentRequisitionMembers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        recruitmentRequisitionMemberServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeRecruitmentRequisitionMember();
        await comp.$nextTick(); // clear components

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(recruitmentRequisitionMemberServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
