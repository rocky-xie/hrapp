import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISkillAssessment } from '@/shared/model/skill-assessment.model';

import SkillAssessmentService from './skill-assessment.service';

export default defineComponent({
  name: 'SkillAssessmentDetails',
  setup() {
    const skillAssessmentService = inject('skillAssessmentService', () => new SkillAssessmentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const skillAssessment: Ref<ISkillAssessment> = ref({});

    const retrieveSkillAssessment = async skillAssessmentId => {
      try {
        const res = await skillAssessmentService().find(skillAssessmentId);
        skillAssessment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillAssessmentId) {
      retrieveSkillAssessment(route.params.skillAssessmentId);
    }

    return {
      alertService,
      skillAssessment,

      ...dataUtils,

      previousState,
    };
  },
});
