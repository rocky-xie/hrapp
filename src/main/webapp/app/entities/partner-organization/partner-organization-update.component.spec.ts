import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PartnerOrganizationUpdate from './partner-organization-update.vue';

type PartnerOrganizationUpdateComponentType = InstanceType<typeof PartnerOrganizationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const partnerOrganizationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PartnerOrganizationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PartnerOrganization Management Update Component', () => {
    let comp: PartnerOrganizationUpdateComponentType;
    let partnerOrganizationServiceStub: any;

    beforeEach(() => {
      route = {};
      partnerOrganizationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      partnerOrganizationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          partnerOrganizationService: () => partnerOrganizationServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PartnerOrganizationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.partnerOrganization = partnerOrganizationSample;
        partnerOrganizationServiceStub.update.mockResolvedValue(partnerOrganizationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.update).toHaveBeenCalledWith(partnerOrganizationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        partnerOrganizationServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PartnerOrganizationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.partnerOrganization = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(partnerOrganizationServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        partnerOrganizationServiceStub.find.mockResolvedValue(partnerOrganizationSample);
        partnerOrganizationServiceStub.retrieve.mockResolvedValue([partnerOrganizationSample]);

        // WHEN
        route = {
          params: {
            partnerOrganizationId: `${partnerOrganizationSample.id}`,
          },
        };
        const wrapper = shallowMount(PartnerOrganizationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.partnerOrganization).toMatchObject(partnerOrganizationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        partnerOrganizationServiceStub.find.mockResolvedValue(partnerOrganizationSample);
        const wrapper = shallowMount(PartnerOrganizationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
