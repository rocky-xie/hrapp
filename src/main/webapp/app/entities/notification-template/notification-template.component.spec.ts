import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import NotificationTemplate from './notification-template.vue';

type NotificationTemplateComponentType = InstanceType<typeof NotificationTemplate>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('NotificationTemplate Management Component', () => {
    let notificationTemplateServiceStub: any;
    let mountOptions: MountingOptions<NotificationTemplateComponentType>['global'];

    beforeEach(() => {
      notificationTemplateServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      notificationTemplateServiceStub.retrieve.mockResolvedValue({ headers: {} });

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          notificationTemplateService: () => notificationTemplateServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        notificationTemplateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(NotificationTemplate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.notificationTemplates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(NotificationTemplate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: NotificationTemplateComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(NotificationTemplate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        notificationTemplateServiceStub.retrieve.mockReset();
        notificationTemplateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        notificationTemplateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.retrieve).toHaveBeenCalled();
        expect(comp.notificationTemplates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(notificationTemplateServiceStub.retrieve).not.toHaveBeenCalled();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        notificationTemplateServiceStub.retrieve.mockReset();
        notificationTemplateServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(notificationTemplateServiceStub.retrieve).toHaveBeenCalledTimes(1);
        expect(comp.notificationTemplates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(notificationTemplateServiceStub.retrieve.mock.lastCall?.[0]).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        notificationTemplateServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeNotificationTemplate();
        await comp.$nextTick(); // clear components

        // THEN
        expect(notificationTemplateServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(notificationTemplateServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});
