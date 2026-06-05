import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillUpgradeRecord from './skill-upgrade-record.vue';

type SkillUpgradeRecordComponentType = InstanceType<typeof SkillUpgradeRecord>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SkillUpgradeRecord Management Component', () => {
    let skillUpgradeRecordServiceStub: any;
    let mountOptions: MountingOptions<SkillUpgradeRecordComponentType>['global'];

    beforeEach(() => {
      skillUpgradeRecordServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      skillUpgradeRecordServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          skillUpgradeRecordService: () => skillUpgradeRecordServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillUpgradeRecordServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SkillUpgradeRecord, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.skillUpgradeRecords[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SkillUpgradeRecord, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SkillUpgradeRecordComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SkillUpgradeRecord, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        skillUpgradeRecordServiceStub.retrieve.mockReset();
        skillUpgradeRecordServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        skillUpgradeRecordServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.skillUpgradeRecords[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(skillUpgradeRecordServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        skillUpgradeRecordServiceStub.retrieve.mockReset();
        skillUpgradeRecordServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(skillUpgradeRecordServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.skillUpgradeRecords[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        skillUpgradeRecordServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSkillUpgradeRecord();
        await comp.$nextTick(); // clear components

        // THEN
        expect(skillUpgradeRecordServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(skillUpgradeRecordServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
