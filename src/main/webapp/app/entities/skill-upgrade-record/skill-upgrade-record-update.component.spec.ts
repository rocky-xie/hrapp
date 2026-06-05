import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillUpgradeRecordUpdate from './skill-upgrade-record-update.vue';

type SkillUpgradeRecordUpdateComponentType = InstanceType<typeof SkillUpgradeRecordUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillUpgradeRecordSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SkillUpgradeRecordUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SkillUpgradeRecord Management Update Component', () => {
    let comp: SkillUpgradeRecordUpdateComponentType;
    let skillUpgradeRecordServiceStub: any;

    beforeEach(() => {
      route = {};
      skillUpgradeRecordServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      skillUpgradeRecordServiceStub.retrieve.mockResolvedValueOnce([]);

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
          skillUpgradeRecordService: () => skillUpgradeRecordServiceStub,
          personSkillService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillLevelService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          personService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          skillService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SkillUpgradeRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillUpgradeRecord = skillUpgradeRecordSample;
        skillUpgradeRecordServiceStub.update.mockResolvedValue(skillUpgradeRecordSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.update).toHaveBeenCalledWith(skillUpgradeRecordSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        skillUpgradeRecordServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(SkillUpgradeRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.skillUpgradeRecord = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(skillUpgradeRecordServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        skillUpgradeRecordServiceStub.find.mockResolvedValue(skillUpgradeRecordSample);
        skillUpgradeRecordServiceStub.retrieve.mockResolvedValue([skillUpgradeRecordSample]);

        // WHEN
        route = {
          params: {
            skillUpgradeRecordId: `${skillUpgradeRecordSample.id}`,
          },
        };
        const wrapper = shallowMount(SkillUpgradeRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.skillUpgradeRecord).toMatchObject(skillUpgradeRecordSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillUpgradeRecordServiceStub.find.mockResolvedValue(skillUpgradeRecordSample);
        const wrapper = shallowMount(SkillUpgradeRecordUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
