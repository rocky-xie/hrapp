import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SuccessionCandidate from './succession-candidate.vue';

type SuccessionCandidateComponentType = InstanceType<typeof SuccessionCandidate>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SuccessionCandidate Management Component', () => {
    let successionCandidateServiceStub: any;
    let mountOptions: MountingOptions<SuccessionCandidateComponentType>['global'];

    beforeEach(() => {
      successionCandidateServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      successionCandidateServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          successionCandidateService: () => successionCandidateServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        successionCandidateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SuccessionCandidate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.successionCandidates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SuccessionCandidate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SuccessionCandidateComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SuccessionCandidate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        successionCandidateServiceStub.retrieve.mockReset();
        successionCandidateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        successionCandidateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.successionCandidates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(successionCandidateServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        successionCandidateServiceStub.retrieve.mockReset();
        successionCandidateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(successionCandidateServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.successionCandidates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        successionCandidateServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSuccessionCandidate();
        await comp.$nextTick(); // clear components

        // THEN
        expect(successionCandidateServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(successionCandidateServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
