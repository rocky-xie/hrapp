import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import SelectionActivityUpdate from './selection-activity-update.vue';

type SelectionActivityUpdateComponentType = InstanceType<typeof SelectionActivityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const selectionActivitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SelectionActivityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SelectionActivity Management Update Component', () => {
    let comp: SelectionActivityUpdateComponentType;
    let selectionActivityServiceStub: any;

    beforeEach(() => {
      route = {};
      selectionActivityServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      selectionActivityServiceStub.retrieve.mockResolvedValueOnce([]);

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
          selectionActivityService: () => selectionActivityServiceStub,
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
        const wrapper = shallowMount(SelectionActivityUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(SelectionActivityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.selectionActivity = selectionActivitySample;
        selectionActivityServiceStub.update.mockResolvedValue(selectionActivitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(selectionActivityServiceStub.update).toHaveBeenCalledWith(selectionActivitySample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        selectionActivityServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SelectionActivityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.selectionActivity = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(selectionActivityServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        selectionActivityServiceStub.find.mockResolvedValue(selectionActivitySample);
        selectionActivityServiceStub.retrieve.mockResolvedValue([selectionActivitySample]);

        // WHEN
        route = {
          params: {
            selectionActivityId: `${selectionActivitySample.id}`,
          },
        };
        const wrapper = shallowMount(SelectionActivityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.selectionActivity).toMatchObject(selectionActivitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        selectionActivityServiceStub.find.mockResolvedValue(selectionActivitySample);
        const wrapper = shallowMount(SelectionActivityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
