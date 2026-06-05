import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import CandidateProfileUpdate from './candidate-profile-update.vue';

type CandidateProfileUpdateComponentType = InstanceType<typeof CandidateProfileUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const candidateProfileSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CandidateProfileUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CandidateProfile Management Update Component', () => {
    let comp: CandidateProfileUpdateComponentType;
    let candidateProfileServiceStub: any;

    beforeEach(() => {
      route = {};
      candidateProfileServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      candidateProfileServiceStub.retrieve.mockResolvedValueOnce([]);

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          candidateProfileService: () => candidateProfileServiceStub,
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CandidateProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.candidateProfile = candidateProfileSample;
        candidateProfileServiceStub.update.mockResolvedValue(candidateProfileSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.update).toHaveBeenCalledWith(candidateProfileSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        candidateProfileServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(CandidateProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.candidateProfile = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(candidateProfileServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        candidateProfileServiceStub.find.mockResolvedValue(candidateProfileSample);
        candidateProfileServiceStub.retrieve.mockResolvedValue([candidateProfileSample]);

        // WHEN
        route = {
          params: {
            candidateProfileId: `${candidateProfileSample.id}`,
          },
        };
        const wrapper = shallowMount(CandidateProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.candidateProfile).toMatchObject(candidateProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        candidateProfileServiceStub.find.mockResolvedValue(candidateProfileSample);
        const wrapper = shallowMount(CandidateProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
