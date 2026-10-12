import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import NotificationMessageUpdate from './notification-message-update.vue';

type NotificationMessageUpdateComponentType = InstanceType<typeof NotificationMessageUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const notificationMessageSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<NotificationMessageUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('NotificationMessage Management Update Component', () => {
    let comp: NotificationMessageUpdateComponentType;
    let notificationMessageServiceStub: any;

    beforeEach(() => {
      route = {};
      notificationMessageServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      notificationMessageServiceStub.retrieve.mockResolvedValueOnce([]);

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
          notificationMessageService: () => notificationMessageServiceStub,
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
        const wrapper = shallowMount(NotificationMessageUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(NotificationMessageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.notificationMessage = notificationMessageSample;
        notificationMessageServiceStub.update.mockResolvedValue(notificationMessageSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(notificationMessageServiceStub.update).toHaveBeenCalledWith(notificationMessageSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        notificationMessageServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(NotificationMessageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.notificationMessage = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(notificationMessageServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        notificationMessageServiceStub.find.mockResolvedValue(notificationMessageSample);
        notificationMessageServiceStub.retrieve.mockResolvedValue([notificationMessageSample]);

        // WHEN
        route = {
          params: {
            notificationMessageId: `${notificationMessageSample.id}`,
          },
        };
        const wrapper = shallowMount(NotificationMessageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.notificationMessage).toMatchObject(notificationMessageSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        notificationMessageServiceStub.find.mockResolvedValue(notificationMessageSample);
        const wrapper = shallowMount(NotificationMessageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
