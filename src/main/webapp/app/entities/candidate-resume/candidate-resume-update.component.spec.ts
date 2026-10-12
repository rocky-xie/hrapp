import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import CandidateResumeUpdate from './candidate-resume-update.vue';

type CandidateResumeUpdateComponentType = InstanceType<typeof CandidateResumeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const candidateResumeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CandidateResumeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CandidateResume Management Update Component', () => {
    let comp: CandidateResumeUpdateComponentType;
    let candidateResumeServiceStub: any;

    beforeEach(() => {
      route = {};
      candidateResumeServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      candidateResumeServiceStub.retrieve.mockResolvedValueOnce([]);

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
          candidateResumeService: () => candidateResumeServiceStub,
          externalCandidateService: () => ({
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
        const wrapper = shallowMount(CandidateResumeUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(CandidateResumeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.candidateResume = candidateResumeSample;
        candidateResumeServiceStub.update.mockResolvedValue(candidateResumeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(candidateResumeServiceStub.update).toHaveBeenCalledWith(candidateResumeSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        candidateResumeServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(CandidateResumeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.candidateResume = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(candidateResumeServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        candidateResumeServiceStub.find.mockResolvedValue(candidateResumeSample);
        candidateResumeServiceStub.retrieve.mockResolvedValue([candidateResumeSample]);

        // WHEN
        route = {
          params: {
            candidateResumeId: `${candidateResumeSample.id}`,
          },
        };
        const wrapper = shallowMount(CandidateResumeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.candidateResume).toMatchObject(candidateResumeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        candidateResumeServiceStub.find.mockResolvedValue(candidateResumeSample);
        const wrapper = shallowMount(CandidateResumeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
