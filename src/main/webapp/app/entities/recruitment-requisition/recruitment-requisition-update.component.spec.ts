import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import RecruitmentRequisitionUpdate from './recruitment-requisition-update.vue';

type RecruitmentRequisitionUpdateComponentType = InstanceType<typeof RecruitmentRequisitionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentRequisitionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RecruitmentRequisitionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('RecruitmentRequisition Management Update Component', () => {
    let comp: RecruitmentRequisitionUpdateComponentType;
    let recruitmentRequisitionServiceStub: any;

    beforeEach(() => {
      route = {};
      recruitmentRequisitionServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      recruitmentRequisitionServiceStub.retrieve.mockResolvedValueOnce([]);

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
          recruitmentRequisitionService: () => recruitmentRequisitionServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(RecruitmentRequisitionUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(RecruitmentRequisitionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentRequisition = recruitmentRequisitionSample;
        recruitmentRequisitionServiceStub.update.mockResolvedValue(recruitmentRequisitionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionServiceStub.update).toHaveBeenCalledWith(recruitmentRequisitionSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        recruitmentRequisitionServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(RecruitmentRequisitionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentRequisition = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        recruitmentRequisitionServiceStub.find.mockResolvedValue(recruitmentRequisitionSample);
        recruitmentRequisitionServiceStub.retrieve.mockResolvedValue([recruitmentRequisitionSample]);

        // WHEN
        route = {
          params: {
            recruitmentRequisitionId: `${recruitmentRequisitionSample.id}`,
          },
        };
        const wrapper = shallowMount(RecruitmentRequisitionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentRequisition).toMatchObject(recruitmentRequisitionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentRequisitionServiceStub.find.mockResolvedValue(recruitmentRequisitionSample);
        const wrapper = shallowMount(RecruitmentRequisitionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
