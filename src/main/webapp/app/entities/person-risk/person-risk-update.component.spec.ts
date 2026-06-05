import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PersonRiskUpdate from './person-risk-update.vue';

type PersonRiskUpdateComponentType = InstanceType<typeof PersonRiskUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personRiskSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PersonRiskUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PersonRisk Management Update Component', () => {
    let comp: PersonRiskUpdateComponentType;
    let personRiskServiceStub: any;

    beforeEach(() => {
      route = {};
      personRiskServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      personRiskServiceStub.retrieve.mockResolvedValueOnce([]);

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
          personRiskService: () => personRiskServiceStub,
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
        const wrapper = shallowMount(PersonRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.personRisk = personRiskSample;
        personRiskServiceStub.update.mockResolvedValue(personRiskSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personRiskServiceStub.update).toHaveBeenCalledWith(personRiskSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        personRiskServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PersonRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.personRisk = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personRiskServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        personRiskServiceStub.find.mockResolvedValue(personRiskSample);
        personRiskServiceStub.retrieve.mockResolvedValue([personRiskSample]);

        // WHEN
        route = {
          params: {
            personRiskId: `${personRiskSample.id}`,
          },
        };
        const wrapper = shallowMount(PersonRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.personRisk).toMatchObject(personRiskSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personRiskServiceStub.find.mockResolvedValue(personRiskSample);
        const wrapper = shallowMount(PersonRiskUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
