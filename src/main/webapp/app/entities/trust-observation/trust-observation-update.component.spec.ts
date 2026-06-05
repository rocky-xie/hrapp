import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TrustObservationUpdate from './trust-observation-update.vue';

type TrustObservationUpdateComponentType = InstanceType<typeof TrustObservationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const trustObservationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TrustObservationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TrustObservation Management Update Component', () => {
    let comp: TrustObservationUpdateComponentType;
    let trustObservationServiceStub: any;

    beforeEach(() => {
      route = {};
      trustObservationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      trustObservationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          trustObservationService: () => trustObservationServiceStub,
          personService: () => ({
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
        const wrapper = shallowMount(TrustObservationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trustObservation = trustObservationSample;
        trustObservationServiceStub.update.mockResolvedValue(trustObservationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(trustObservationServiceStub.update).toHaveBeenCalledWith(trustObservationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        trustObservationServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TrustObservationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trustObservation = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(trustObservationServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        trustObservationServiceStub.find.mockResolvedValue(trustObservationSample);
        trustObservationServiceStub.retrieve.mockResolvedValue([trustObservationSample]);

        // WHEN
        route = {
          params: {
            trustObservationId: `${trustObservationSample.id}`,
          },
        };
        const wrapper = shallowMount(TrustObservationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.trustObservation).toMatchObject(trustObservationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        trustObservationServiceStub.find.mockResolvedValue(trustObservationSample);
        const wrapper = shallowMount(TrustObservationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
