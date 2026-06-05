import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISuccessionCandidate } from '@/shared/model/succession-candidate.model';

import SuccessionCandidateService from './succession-candidate.service';

export default defineComponent({
  name: 'SuccessionCandidateDetails',
  setup() {
    const successionCandidateService = inject('successionCandidateService', () => new SuccessionCandidateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const successionCandidate: Ref<ISuccessionCandidate> = ref({});

    const retrieveSuccessionCandidate = async successionCandidateId => {
      try {
        const res = await successionCandidateService().find(successionCandidateId);
        successionCandidate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.successionCandidateId) {
      retrieveSuccessionCandidate(route.params.successionCandidateId);
    }

    return {
      alertService,
      successionCandidate,

      ...dataUtils,

      previousState,
    };
  },
});
