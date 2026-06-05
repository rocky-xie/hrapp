import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillAssessment from './skill-assessment.vue';

type SkillAssessmentComponentType = InstanceType<typeof SkillAssessment>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SkillAssessment Management Component', () => {
    let skillAssessmentServiceStub: any;
    let mountOptions: MountingOptions<SkillAssessmentComponentType>['global'];

    beforeEach(() => {
      skillAssessmentServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      skillAssessmentServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          skillAssessmentService: () => skillAssessmentServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillAssessmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SkillAssessment, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.skillAssessments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SkillAssessment, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SkillAssessmentComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SkillAssessment, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        skillAssessmentServiceStub.retrieve.mockReset();
        skillAssessmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        skillAssessmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.skillAssessments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(skillAssessmentServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        skillAssessmentServiceStub.retrieve.mockReset();
        skillAssessmentServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(skillAssessmentServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.skillAssessments[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(skillAssessmentServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        skillAssessmentServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSkillAssessment();
        await comp.$nextTick(); // clear components

        // THEN
        expect(skillAssessmentServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(skillAssessmentServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
