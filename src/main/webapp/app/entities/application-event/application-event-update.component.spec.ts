import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import ApplicationEventUpdate from './application-event-update.vue';

type ApplicationEventUpdateComponentType = InstanceType<typeof ApplicationEventUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationEventSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApplicationEventUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApplicationEvent Management Update Component', () => {
    let comp: ApplicationEventUpdateComponentType;
    let applicationEventServiceStub: any;

    beforeEach(() => {
      route = {};
      applicationEventServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      applicationEventServiceStub.retrieve.mockResolvedValueOnce([]);

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
          applicationEventService: () => applicationEventServiceStub,
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
        const wrapper = shallowMount(ApplicationEventUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ApplicationEventUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationEvent = applicationEventSample;
        applicationEventServiceStub.update.mockResolvedValue(applicationEventSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationEventServiceStub.update).toHaveBeenCalledWith(applicationEventSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        applicationEventServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(ApplicationEventUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationEvent = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationEventServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        applicationEventServiceStub.find.mockResolvedValue(applicationEventSample);
        applicationEventServiceStub.retrieve.mockResolvedValue([applicationEventSample]);

        // WHEN
        route = {
          params: {
            applicationEventId: `${applicationEventSample.id}`,
          },
        };
        const wrapper = shallowMount(ApplicationEventUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.applicationEvent).toMatchObject(applicationEventSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationEventServiceStub.find.mockResolvedValue(applicationEventSample);
        const wrapper = shallowMount(ApplicationEventUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
