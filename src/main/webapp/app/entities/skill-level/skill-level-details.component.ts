import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

import SkillLevelService from './skill-level.service';

export default defineComponent({
  name: 'SkillLevelDetails',
  setup() {
    const skillLevelService = inject('skillLevelService', () => new SkillLevelService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const skillLevel: Ref<ISkillLevel> = ref({});

    const retrieveSkillLevel = async skillLevelId => {
      try {
        const res = await skillLevelService().find(skillLevelId);
        skillLevel.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillLevelId) {
      retrieveSkillLevel(route.params.skillLevelId);
    }

    return {
      alertService,
      skillLevel,

      ...dataUtils,

      previousState,
    };
  },
});
