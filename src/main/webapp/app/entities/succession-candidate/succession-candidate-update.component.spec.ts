import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SuccessionCandidateUpdate from './succession-candidate-update.vue';

type SuccessionCandidateUpdateComponentType = InstanceType<typeof SuccessionCandidateUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const successionCandidateSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SuccessionCandidateUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SuccessionCandidate Management Update Component', () => {
    let comp: SuccessionCandidateUpdateComponentType;
    let successionCandidateServiceStub: any;

    beforeEach(() => {
      route = {};
      successionCandidateServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      successionCandidateServiceStub.retrieve.mockResolvedValue({ data: [] });

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
          successionCandidateService: () => successionCandidateServiceStub,
          positionService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
          }),
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({ data: [] }),
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
        const wrapper = shallowMount(SuccessionCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.successionCandidate = successionCandidateSample;
        successionCandidateServiceStub.update.mockResolvedValue(successionCandidateSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.update).toHaveBeenCalledWith(successionCandidateSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        successionCandidateServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SuccessionCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.successionCandidate = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should reject same current owner and candidate', async () => {
        // GIVEN
        const person = { id: 1, personName: 'owner' };
        const entity = { currentOwner: person, candidate: person };
        const wrapper = shallowMount(SuccessionCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.successionCandidate = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(successionCandidateServiceStub.create).not.toHaveBeenCalled();
        expect(successionCandidateServiceStub.update).not.toHaveBeenCalled();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        successionCandidateServiceStub.find.mockResolvedValue(successionCandidateSample);
        successionCandidateServiceStub.retrieve.mockResolvedValue([successionCandidateSample]);

        // WHEN
        route = {
          params: {
            successionCandidateId: `${successionCandidateSample.id}`,
          },
        };
        const wrapper = shallowMount(SuccessionCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.successionCandidate).toMatchObject(successionCandidateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        successionCandidateServiceStub.find.mockResolvedValue(successionCandidateSample);
        const wrapper = shallowMount(SuccessionCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
