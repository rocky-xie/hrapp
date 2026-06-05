import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PositionService from '@/entities/position/position.service';
import SkillService from '@/entities/skill/skill.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { PlanStatus } from '@/shared/model/enumerations/plan-status.model';
import { type IImprovementPlan, ImprovementPlan } from '@/shared/model/improvement-plan.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkill } from '@/shared/model/skill.model';

import ImprovementPlanService from './improvement-plan.service';

export default defineComponent({
  name: 'ImprovementPlanUpdate',
  setup() {
    const improvementPlanService = inject('improvementPlanService', () => new ImprovementPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const improvementPlan: Ref<IImprovementPlan> = ref(new ImprovementPlan());

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);
    const planStatusValues: Ref<string[]> = ref(Object.keys(PlanStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveImprovementPlan = async improvementPlanId => {
      try {
        const res = await improvementPlanService().find(improvementPlanId);
        improvementPlan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.improvementPlanId) {
      retrieveImprovementPlan(route.params.improvementPlanId);
    }

    const initRelationships = () => {
      positionService()
        .retrieve()
        .then(res => {
          positions.value = res.data;
        });
      skillService()
        .retrieve()
        .then(res => {
          skills.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      planName: {
        required: validations.required('This field is required.'),
        maxLength: validations.maxLength('This field cannot be longer than 150 characters.', 150),
      },
      planStatus: {
        required: validations.required('This field is required.'),
      },
      problemSummary: {},
      improvementAction: {},
      ownerName: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      startDate: {},
      targetDate: {},
      completionDate: {},
      reviewResult: {},
      position: {},
      skill: {},
    };
    const v$ = useVuelidate(validationRules, improvementPlan as any);
    v$.value.$validate();

    return {
      improvementPlanService,
      alertService,
      improvementPlan,
      previousState,
      planStatusValues,
      isSaving,
      currentLanguage,
      positions,
      skills,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.improvementPlan.id) {
        this.improvementPlanService()
          .update(this.improvementPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ImprovementPlan is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.improvementPlanService()
          .create(this.improvementPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ImprovementPlan is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
