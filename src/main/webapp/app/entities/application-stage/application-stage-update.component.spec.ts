import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import ApplicationStageUpdate from './application-stage-update.vue';

type ApplicationStageUpdateComponentType = InstanceType<typeof ApplicationStageUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationStageSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApplicationStageUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApplicationStage Management Update Component', () => {
    let comp: ApplicationStageUpdateComponentType;
    let applicationStageServiceStub: any;

    beforeEach(() => {
      route = {};
      applicationStageServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      applicationStageServiceStub.retrieve.mockResolvedValueOnce([]);

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
          applicationStageService: () => applicationStageServiceStub,
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
        const wrapper = shallowMount(ApplicationStageUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ApplicationStageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationStage = applicationStageSample;
        applicationStageServiceStub.update.mockResolvedValue(applicationStageSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationStageServiceStub.update).toHaveBeenCalledWith(applicationStageSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        applicationStageServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(ApplicationStageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationStage = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationStageServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        applicationStageServiceStub.find.mockResolvedValue(applicationStageSample);
        applicationStageServiceStub.retrieve.mockResolvedValue([applicationStageSample]);

        // WHEN
        route = {
          params: {
            applicationStageId: `${applicationStageSample.id}`,
          },
        };
        const wrapper = shallowMount(ApplicationStageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.applicationStage).toMatchObject(applicationStageSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationStageServiceStub.find.mockResolvedValue(applicationStageSample);
        const wrapper = shallowMount(ApplicationStageUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
