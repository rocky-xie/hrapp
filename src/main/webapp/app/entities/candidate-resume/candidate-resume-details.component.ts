import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ICandidateResume } from '@/shared/model/candidate-resume.model';

import CandidateResumeService from './candidate-resume.service';

export default defineComponent({
  name: 'CandidateResumeDetails',
  setup() {
    const dateFormat = useDateFormat();
    const candidateResumeService = inject('candidateResumeService', () => new CandidateResumeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const candidateResume: Ref<ICandidateResume> = ref({});

    const retrieveCandidateResume = async candidateResumeId => {
      try {
        const res = await candidateResumeService().find(candidateResumeId);
        candidateResume.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.candidateResumeId) {
      retrieveCandidateResume(route.params.candidateResumeId);
    }

    return {
      ...dateFormat,
      alertService,
      candidateResume,

      ...dataUtils,

      previousState,
    };
  },
});
