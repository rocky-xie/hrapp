import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import NotificationTemplateUpdate from './notification-template-update.vue';

type NotificationTemplateUpdateComponentType = InstanceType<typeof NotificationTemplateUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const notificationTemplateSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<NotificationTemplateUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('NotificationTemplate Management Update Component', () => {
    let comp: NotificationTemplateUpdateComponentType;
    let notificationTemplateServiceStub: any;

    beforeEach(() => {
      route = {};
      notificationTemplateServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      notificationTemplateServiceStub.retrieve.mockResolvedValueOnce([]);

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
          notificationTemplateService: () => notificationTemplateServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(NotificationTemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.notificationTemplate = notificationTemplateSample;
        notificationTemplateServiceStub.update.mockResolvedValue(notificationTemplateSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.update).toHaveBeenCalledWith(notificationTemplateSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        notificationTemplateServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(NotificationTemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.notificationTemplate = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        notificationTemplateServiceStub.find.mockResolvedValue(notificationTemplateSample);
        notificationTemplateServiceStub.retrieve.mockResolvedValue([notificationTemplateSample]);

        // WHEN
        route = {
          params: {
            notificationTemplateId: `${notificationTemplateSample.id}`,
          },
        };
        const wrapper = shallowMount(NotificationTemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.notificationTemplate).toMatchObject(notificationTemplateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        notificationTemplateServiceStub.find.mockResolvedValue(notificationTemplateSample);
        const wrapper = shallowMount(NotificationTemplateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
