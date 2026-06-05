import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionSkillRequirement from './position-skill-requirement.vue';

type PositionSkillRequirementComponentType = InstanceType<typeof PositionSkillRequirement>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PositionSkillRequirement Management Component', () => {
    let positionSkillRequirementServiceStub: any;
    let mountOptions: MountingOptions<PositionSkillRequirementComponentType>['global'];

    beforeEach(() => {
      positionSkillRequirementServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      positionSkillRequirementServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          positionSkillRequirementService: () => positionSkillRequirementServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        positionSkillRequirementServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PositionSkillRequirement, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.positionSkillRequirements[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PositionSkillRequirement, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PositionSkillRequirementComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PositionSkillRequirement, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        positionSkillRequirementServiceStub.retrieve.mockReset();
        positionSkillRequirementServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        positionSkillRequirementServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.positionSkillRequirements[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(positionSkillRequirementServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        positionSkillRequirementServiceStub.retrieve.mockReset();
        positionSkillRequirementServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(positionSkillRequirementServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.positionSkillRequirements[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(positionSkillRequirementServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        positionSkillRequirementServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePositionSkillRequirement();
        await comp.$nextTick(); // clear components

        // THEN
        expect(positionSkillRequirementServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(positionSkillRequirementServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
