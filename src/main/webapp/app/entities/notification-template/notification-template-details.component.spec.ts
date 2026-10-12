import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import NotificationTemplateDetails from './notification-template-details.vue';

type NotificationTemplateDetailsComponentType = InstanceType<typeof NotificationTemplateDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const notificationTemplateSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('NotificationTemplate Management Detail Component', () => {
    let notificationTemplateServiceStub: any;
    let mountOptions: MountingOptions<NotificationTemplateDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      notificationTemplateServiceStub = {
        find: vi.fn(),
      };

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          notificationTemplateService: () => notificationTemplateServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        notificationTemplateServiceStub.find.mockResolvedValue(notificationTemplateSample);
        route = {
          params: {
            notificationTemplateId: `${123}`,
          },
        };
        const wrapper = shallowMount(NotificationTemplateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.notificationTemplate).toMatchObject(notificationTemplateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        notificationTemplateServiceStub.find.mockResolvedValue(notificationTemplateSample);
        const wrapper = shallowMount(NotificationTemplateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
