import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import RecruitmentEvaluationUpdate from './recruitment-evaluation-update.vue';

type RecruitmentEvaluationUpdateComponentType = InstanceType<typeof RecruitmentEvaluationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const recruitmentEvaluationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RecruitmentEvaluationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('RecruitmentEvaluation Management Update Component', () => {
    let comp: RecruitmentEvaluationUpdateComponentType;
    let recruitmentEvaluationServiceStub: any;

    beforeEach(() => {
      route = {};
      recruitmentEvaluationServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      recruitmentEvaluationServiceStub.retrieve.mockResolvedValueOnce([]);

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
          recruitmentEvaluationService: () => recruitmentEvaluationServiceStub,
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
        const wrapper = shallowMount(RecruitmentEvaluationUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(RecruitmentEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentEvaluation = recruitmentEvaluationSample;
        recruitmentEvaluationServiceStub.update.mockResolvedValue(recruitmentEvaluationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentEvaluationServiceStub.update).toHaveBeenCalledWith(recruitmentEvaluationSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        recruitmentEvaluationServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(RecruitmentEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.recruitmentEvaluation = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recruitmentEvaluationServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        recruitmentEvaluationServiceStub.find.mockResolvedValue(recruitmentEvaluationSample);
        recruitmentEvaluationServiceStub.retrieve.mockResolvedValue([recruitmentEvaluationSample]);

        // WHEN
        route = {
          params: {
            recruitmentEvaluationId: `${recruitmentEvaluationSample.id}`,
          },
        };
        const wrapper = shallowMount(RecruitmentEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.recruitmentEvaluation).toMatchObject(recruitmentEvaluationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        recruitmentEvaluationServiceStub.find.mockResolvedValue(recruitmentEvaluationSample);
        const wrapper = shallowMount(RecruitmentEvaluationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
