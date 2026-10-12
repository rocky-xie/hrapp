import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import PreboardingFollowUpUpdate from './preboarding-follow-up-update.vue';

type PreboardingFollowUpUpdateComponentType = InstanceType<typeof PreboardingFollowUpUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const preboardingFollowUpSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PreboardingFollowUpUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PreboardingFollowUp Management Update Component', () => {
    let comp: PreboardingFollowUpUpdateComponentType;
    let preboardingFollowUpServiceStub: any;

    beforeEach(() => {
      route = {};
      preboardingFollowUpServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      preboardingFollowUpServiceStub.retrieve.mockResolvedValueOnce([]);

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
          preboardingFollowUpService: () => preboardingFollowUpServiceStub,
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
        const wrapper = shallowMount(PreboardingFollowUpUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(PreboardingFollowUpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.preboardingFollowUp = preboardingFollowUpSample;
        preboardingFollowUpServiceStub.update.mockResolvedValue(preboardingFollowUpSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(preboardingFollowUpServiceStub.update).toHaveBeenCalledWith(preboardingFollowUpSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        preboardingFollowUpServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PreboardingFollowUpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.preboardingFollowUp = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(preboardingFollowUpServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        preboardingFollowUpServiceStub.find.mockResolvedValue(preboardingFollowUpSample);
        preboardingFollowUpServiceStub.retrieve.mockResolvedValue([preboardingFollowUpSample]);

        // WHEN
        route = {
          params: {
            preboardingFollowUpId: `${preboardingFollowUpSample.id}`,
          },
        };
        const wrapper = shallowMount(PreboardingFollowUpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.preboardingFollowUp).toMatchObject(preboardingFollowUpSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        preboardingFollowUpServiceStub.find.mockResolvedValue(preboardingFollowUpSample);
        const wrapper = shallowMount(PreboardingFollowUpUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
