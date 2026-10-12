import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import HiringDecisionUpdate from './hiring-decision-update.vue';

type HiringDecisionUpdateComponentType = InstanceType<typeof HiringDecisionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const hiringDecisionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<HiringDecisionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('HiringDecision Management Update Component', () => {
    let comp: HiringDecisionUpdateComponentType;
    let hiringDecisionServiceStub: any;

    beforeEach(() => {
      route = {};
      hiringDecisionServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      hiringDecisionServiceStub.retrieve.mockResolvedValueOnce([]);

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
          hiringDecisionService: () => hiringDecisionServiceStub,
          recruitmentApplicationService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(HiringDecisionUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(HiringDecisionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.hiringDecision = hiringDecisionSample;
        hiringDecisionServiceStub.update.mockResolvedValue(hiringDecisionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(hiringDecisionServiceStub.update).toHaveBeenCalledWith(hiringDecisionSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        hiringDecisionServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(HiringDecisionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.hiringDecision = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(hiringDecisionServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        hiringDecisionServiceStub.find.mockResolvedValue(hiringDecisionSample);
        hiringDecisionServiceStub.retrieve.mockResolvedValue([hiringDecisionSample]);

        // WHEN
        route = {
          params: {
            hiringDecisionId: `${hiringDecisionSample.id}`,
          },
        };
        const wrapper = shallowMount(HiringDecisionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.hiringDecision).toMatchObject(hiringDecisionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        hiringDecisionServiceStub.find.mockResolvedValue(hiringDecisionSample);
        const wrapper = shallowMount(HiringDecisionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
