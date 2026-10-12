import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import RecruitmentRequisitionMemberUpdate from './recruitment-requisition-member-update.vue';

type RecruitmentRequisitionMemberUpdateComponentType = InstanceType<typeof RecruitmentRequisitionMemberUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentRequisitionMemberSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RecruitmentRequisitionMemberUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('RecruitmentRequisitionMember Management Update Component', () => {
    let comp: RecruitmentRequisitionMemberUpdateComponentType;
    let recruitmentRequisitionMemberServiceStub: any;

    beforeEach(() => {
      route = {};
      recruitmentRequisitionMemberServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValueOnce([]);

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
          recruitmentRequisitionMemberService: () => recruitmentRequisitionMemberServiceStub,
          recruitmentRequisitionService: () => ({
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
        const wrapper = shallowMount(RecruitmentRequisitionMemberUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(RecruitmentRequisitionMemberUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentRequisitionMember = recruitmentRequisitionMemberSample;
        recruitmentRequisitionMemberServiceStub.update.mockResolvedValue(recruitmentRequisitionMemberSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.update).toHaveBeenCalledWith(recruitmentRequisitionMemberSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        recruitmentRequisitionMemberServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(RecruitmentRequisitionMemberUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentRequisitionMember = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentRequisitionMemberServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        recruitmentRequisitionMemberServiceStub.find.mockResolvedValue(recruitmentRequisitionMemberSample);
        recruitmentRequisitionMemberServiceStub.retrieve.mockResolvedValue([recruitmentRequisitionMemberSample]);

        // WHEN
        route = {
          params: {
            recruitmentRequisitionMemberId: `${recruitmentRequisitionMemberSample.id}`,
          },
        };
        const wrapper = shallowMount(RecruitmentRequisitionMemberUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentRequisitionMember).toMatchObject(recruitmentRequisitionMemberSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentRequisitionMemberServiceStub.find.mockResolvedValue(recruitmentRequisitionMemberSample);
        const wrapper = shallowMount(RecruitmentRequisitionMemberUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
