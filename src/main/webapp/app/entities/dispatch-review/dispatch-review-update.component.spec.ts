import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import DispatchReviewUpdate from './dispatch-review-update.vue';

type DispatchReviewUpdateComponentType = InstanceType<typeof DispatchReviewUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dispatchReviewSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DispatchReviewUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DispatchReview Management Update Component', () => {
    let comp: DispatchReviewUpdateComponentType;
    let dispatchReviewServiceStub: any;

    beforeEach(() => {
      route = {};
      dispatchReviewServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      dispatchReviewServiceStub.retrieve.mockResolvedValueOnce([]);

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
          dispatchReviewService: () => dispatchReviewServiceStub,
          dispatchAssignmentService: () => ({
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
        const wrapper = shallowMount(DispatchReviewUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(DispatchReviewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dispatchReview = dispatchReviewSample;
        dispatchReviewServiceStub.update.mockResolvedValue(dispatchReviewSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.update).toHaveBeenCalledWith(dispatchReviewSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dispatchReviewServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(DispatchReviewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dispatchReview = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dispatchReviewServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dispatchReviewServiceStub.find.mockResolvedValue(dispatchReviewSample);
        dispatchReviewServiceStub.retrieve.mockResolvedValue([dispatchReviewSample]);

        // WHEN
        route = {
          params: {
            dispatchReviewId: `${dispatchReviewSample.id}`,
          },
        };
        const wrapper = shallowMount(DispatchReviewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dispatchReview).toMatchObject(dispatchReviewSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dispatchReviewServiceStub.find.mockResolvedValue(dispatchReviewSample);
        const wrapper = shallowMount(DispatchReviewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
