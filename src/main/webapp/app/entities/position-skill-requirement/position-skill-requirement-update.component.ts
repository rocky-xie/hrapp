import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PositionService from '@/entities/position/position.service';
import SkillService from '@/entities/skill/skill.service';
import SkillLevelService from '@/entities/skill-level/skill-level.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { RequirementImportance } from '@/shared/model/enumerations/requirement-importance.model';
import { type IPositionSkillRequirement, PositionSkillRequirement } from '@/shared/model/position-skill-requirement.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';

import PositionSkillRequirementService from './position-skill-requirement.service';

export default defineComponent({
  name: 'PositionSkillRequirementUpdate',
  setup() {
    const positionSkillRequirementService = inject('positionSkillRequirementService', () => new PositionSkillRequirementService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const positionSkillRequirement: Ref<IPositionSkillRequirement> = ref(new PositionSkillRequirement());

    const positionService = inject('positionService', () => new PositionService());

    const positions: Ref<IPosition[]> = ref([]);

    const skillService = inject('skillService', () => new SkillService());

    const skills: Ref<ISkill[]> = ref([]);

    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());

    const skillLevels: Ref<ISkillLevel[]> = ref([]);
    const requirementImportanceValues: Ref<string[]> = ref(Object.keys(RequirementImportance));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePositionSkillRequirement = async positionSkillRequirementId => {
      try {
        const res = await positionSkillRequirementService().find(positionSkillRequirementId);
        positionSkillRequirement.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.positionSkillRequirementId) {
      retrievePositionSkillRequirement(route.params.positionSkillRequirementId);
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
      skillLevelService()
        .retrieve()
        .then(res => {
          skillLevels.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      importance: {
        required: validations.required('This field is required.'),
      },
      remark: {},
      position: {
        required: validations.required('This field is required.'),
      },
      skill: {
        required: validations.required('This field is required.'),
      },
      requiredLevel: {
        required: validations.required('This field is required.'),
      },
      preferredLevel: {},
    };
    const v$ = useVuelidate(validationRules, positionSkillRequirement as any);
    v$.value.$validate();

    return {
      positionSkillRequirementService,
      alertService,
      positionSkillRequirement,
      previousState,
      requirementImportanceValues,
      isSaving,
      currentLanguage,
      positions,
      skills,
      skillLevels,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.positionSkillRequirement.id) {
        this.positionSkillRequirementService()
          .update(this.positionSkillRequirement)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A PositionSkillRequirement is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.positionSkillRequirementService()
          .create(this.positionSkillRequirement)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A PositionSkillRequirement is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
