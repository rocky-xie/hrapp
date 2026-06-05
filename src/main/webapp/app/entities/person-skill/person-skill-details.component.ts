import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPersonSkill } from '@/shared/model/person-skill.model';

import PersonSkillService from './person-skill.service';

export default defineComponent({
  name: 'PersonSkillDetails',
  setup() {
    const personSkillService = inject('personSkillService', () => new PersonSkillService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const personSkill: Ref<IPersonSkill> = ref({});

    const retrievePersonSkill = async personSkillId => {
      try {
        const res = await personSkillService().find(personSkillId);
        personSkill.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personSkillId) {
      retrievePersonSkill(route.params.personSkillId);
    }

    return {
      alertService,
      personSkill,

      ...dataUtils,

      previousState,
    };
  },
});
