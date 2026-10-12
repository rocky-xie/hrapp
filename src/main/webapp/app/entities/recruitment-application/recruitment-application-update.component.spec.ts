import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, flushPromises, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import RecruitmentApplicationUpdate from './recruitment-application-update.vue';

type RecruitmentApplicationUpdateComponentType = InstanceType<typeof RecruitmentApplicationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentApplicationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RecruitmentApplicationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('RecruitmentApplication Management Update Component', () => {
    let comp: RecruitmentApplicationUpdateComponentType;
    let recruitmentApplicationServiceStub: any;

    beforeEach(() => {
      route = {};
      recruitmentApplicationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      recruitmentApplicationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          recruitmentApplicationService: () => recruitmentApplicationServiceStub,
          externalCandidateService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          recruitmentRequisitionService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          recruitmentRequisitionVersionService: () => ({
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
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentApplication = recruitmentApplicationSample;
        recruitmentApplicationServiceStub.update.mockResolvedValue(recruitmentApplicationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentApplicationServiceStub.update).toHaveBeenCalledWith(recruitmentApplicationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        recruitmentApplicationServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentApplication = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentApplicationServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });

      it('creates an independent graduate application for each selected position', async () => {
        recruitmentApplicationServiceStub.create.mockResolvedValue({});
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentApplication = { applicationCode: 'APP-GRAD-001', recruitmentType: 'GRADUATE' } as any;
        comp.selectedRequisitionIds = [101, 202];
        comp.recruitmentRequisitions = [
          { id: 101, title: 'Software Engineer', lifecycle: 'ACTIVE', editStatus: 'LOCKED' },
          { id: 202, title: 'Project Engineer', lifecycle: 'ACTIVE', editStatus: 'LOCKED' },
        ] as any;
        comp.recruitmentRequisitionVersions = [
          { id: 501, versionNumber: 1, requisition: { id: 101 } },
          { id: 502, versionNumber: 2, requisition: { id: 202 } },
        ] as any;

        comp.save();
        await flushPromises();

        expect(recruitmentApplicationServiceStub.create).toHaveBeenCalledTimes(2);
        expect(recruitmentApplicationServiceStub.create.mock.calls[0][0]).toMatchObject({
          applicationCode: 'APP-GRAD-001-P101',
          requisition: { id: 101 },
          requisitionVersion: { id: 501 },
        });
        expect(recruitmentApplicationServiceStub.create.mock.calls[1][0]).toMatchObject({
          applicationCode: 'APP-GRAD-001-P202',
          requisition: { id: 202 },
          requisitionVersion: { id: 502 },
        });
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        recruitmentApplicationServiceStub.find.mockResolvedValue(recruitmentApplicationSample);
        recruitmentApplicationServiceStub.retrieve.mockResolvedValue([recruitmentApplicationSample]);

        // WHEN
        route = {
          params: {
            recruitmentApplicationId: `${recruitmentApplicationSample.id}`,
          },
        };
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentApplication).toMatchObject(recruitmentApplicationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentApplicationServiceStub.find.mockResolvedValue(recruitmentApplicationSample);
        const wrapper = shallowMount(RecruitmentApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
