import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import KeyResponsibilityCategory from './key-responsibility-category.vue';

type KeyResponsibilityCategoryComponentType = InstanceType<typeof KeyResponsibilityCategory>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('KeyResponsibilityCategory Management Component', () => {
    let keyResponsibilityCategoryServiceStub: any;
    let mountOptions: MountingOptions<KeyResponsibilityCategoryComponentType>['global'];

    beforeEach(() => {
      keyResponsibilityCategoryServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          keyResponsibilityCategoryService: () => keyResponsibilityCategoryServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(KeyResponsibilityCategory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.keyResponsibilityCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(KeyResponsibilityCategory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: KeyResponsibilityCategoryComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(KeyResponsibilityCategory, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        keyResponsibilityCategoryServiceStub.retrieve.mockReset();
        keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.keyResponsibilityCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(keyResponsibilityCategoryServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        keyResponsibilityCategoryServiceStub.retrieve.mockReset();
        keyResponsibilityCategoryServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(keyResponsibilityCategoryServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.keyResponsibilityCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(keyResponsibilityCategoryServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        keyResponsibilityCategoryServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeKeyResponsibilityCategory();
        await comp.$nextTick(); // clear components

        // THEN
        expect(keyResponsibilityCategoryServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(keyResponsibilityCategoryServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
