import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import ExternalCandidateUpdate from './external-candidate-update.vue';

type ExternalCandidateUpdateComponentType = InstanceType<typeof ExternalCandidateUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalCandidateSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ExternalCandidateUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ExternalCandidate Management Update Component', () => {
    let comp: ExternalCandidateUpdateComponentType;
    let externalCandidateServiceStub: any;

    beforeEach(() => {
      route = {};
      externalCandidateServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      externalCandidateServiceStub.retrieve.mockResolvedValueOnce([]);

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
          externalCandidateService: () => externalCandidateServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ExternalCandidateUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ExternalCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalCandidate = externalCandidateSample;
        externalCandidateServiceStub.update.mockResolvedValue(externalCandidateSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalCandidateServiceStub.update).toHaveBeenCalledWith(externalCandidateSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        externalCandidateServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(ExternalCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalCandidate = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalCandidateServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        externalCandidateServiceStub.find.mockResolvedValue(externalCandidateSample);
        externalCandidateServiceStub.retrieve.mockResolvedValue([externalCandidateSample]);

        // WHEN
        route = {
          params: {
            externalCandidateId: `${externalCandidateSample.id}`,
          },
        };
        const wrapper = shallowMount(ExternalCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.externalCandidate).toMatchObject(externalCandidateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalCandidateServiceStub.find.mockResolvedValue(externalCandidateSample);
        const wrapper = shallowMount(ExternalCandidateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
