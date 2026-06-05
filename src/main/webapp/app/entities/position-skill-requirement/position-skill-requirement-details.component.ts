import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPositionSkillRequirement } from '@/shared/model/position-skill-requirement.model';

import PositionSkillRequirementService from './position-skill-requirement.service';

export default defineComponent({
  name: 'PositionSkillRequirementDetails',
  setup() {
    const positionSkillRequirementService = inject('positionSkillRequirementService', () => new PositionSkillRequirementService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const positionSkillRequirement: Ref<IPositionSkillRequirement> = ref({});

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

    return {
      alertService,
      positionSkillRequirement,

      ...dataUtils,

      previousState,
    };
  },
});
