import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PositionMatchUpdate from './position-match-update.vue';

type PositionMatchUpdateComponentType = InstanceType<typeof PositionMatchUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const positionMatchSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PositionMatchUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PositionMatch Management Update Component', () => {
    let comp: PositionMatchUpdateComponentType;
    let positionMatchServiceStub: any;

    beforeEach(() => {
      route = {};
      positionMatchServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      positionMatchServiceStub.retrieve.mockResolvedValueOnce([]);

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
          positionMatchService: () => positionMatchServiceStub,
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
        const wrapper = shallowMount(PositionMatchUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionMatch = positionMatchSample;
        positionMatchServiceStub.update.mockResolvedValue(positionMatchSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionMatchServiceStub.update).toHaveBeenCalledWith(positionMatchSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        positionMatchServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PositionMatchUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.positionMatch = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(positionMatchServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        positionMatchServiceStub.find.mockResolvedValue(positionMatchSample);
        positionMatchServiceStub.retrieve.mockResolvedValue([positionMatchSample]);

        // WHEN
        route = {
          params: {
            positionMatchId: `${positionMatchSample.id}`,
          },
        };
        const wrapper = shallowMount(PositionMatchUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.positionMatch).toMatchObject(positionMatchSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        positionMatchServiceStub.find.mockResolvedValue(positionMatchSample);
        const wrapper = shallowMount(PositionMatchUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
