import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISkill } from '@/shared/model/skill.model';

import SkillService from './skill.service';

export default defineComponent({
  name: 'SkillDetails',
  setup() {
    const skillService = inject('skillService', () => new SkillService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const skill: Ref<ISkill> = ref({});

    const retrieveSkill = async skillId => {
      try {
        const res = await skillService().find(skillId);
        skill.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillId) {
      retrieveSkill(route.params.skillId);
    }

    return {
      alertService,
      skill,

      ...dataUtils,

      previousState,
    };
  },
});
