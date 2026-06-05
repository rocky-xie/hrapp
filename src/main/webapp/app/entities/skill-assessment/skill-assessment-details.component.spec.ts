import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import SkillAssessmentDetails from './skill-assessment-details.vue';

type SkillAssessmentDetailsComponentType = InstanceType<typeof SkillAssessmentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const skillAssessmentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('SkillAssessment Management Detail Component', () => {
    let skillAssessmentServiceStub: any;
    let mountOptions: MountingOptions<SkillAssessmentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      skillAssessmentServiceStub = {
        find: vi.fn(),
      };

      alertService = new AlertService({
        toast: {
          show: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          skillAssessmentService: () => skillAssessmentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        skillAssessmentServiceStub.find.mockResolvedValue(skillAssessmentSample);
        route = {
          params: {
            skillAssessmentId: `${123}`,
          },
        };
        const wrapper = shallowMount(SkillAssessmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.skillAssessment).toMatchObject(skillAssessmentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        skillAssessmentServiceStub.find.mockResolvedValue(skillAssessmentSample);
        const wrapper = shallowMount(SkillAssessmentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
