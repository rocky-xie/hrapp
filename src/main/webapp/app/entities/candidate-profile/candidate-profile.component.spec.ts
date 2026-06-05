import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import CandidateProfile from './candidate-profile.vue';

type CandidateProfileComponentType = InstanceType<typeof CandidateProfile>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CandidateProfile Management Component', () => {
    let candidateProfileServiceStub: any;
    let mountOptions: MountingOptions<CandidateProfileComponentType>['global'];

    beforeEach(() => {
      candidateProfileServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      candidateProfileServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          candidateProfileService: () => candidateProfileServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        candidateProfileServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CandidateProfile, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.candidateProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CandidateProfile, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CandidateProfileComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CandidateProfile, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        candidateProfileServiceStub.retrieve.mockReset();
        candidateProfileServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        candidateProfileServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.candidateProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(candidateProfileServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        candidateProfileServiceStub.retrieve.mockReset();
        candidateProfileServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(candidateProfileServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.candidateProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        candidateProfileServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCandidateProfile();
        await comp.$nextTick(); // clear components

        // THEN
        expect(candidateProfileServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(candidateProfileServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
