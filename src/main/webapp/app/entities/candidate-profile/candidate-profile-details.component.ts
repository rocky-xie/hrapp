import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ICandidateProfile } from '@/shared/model/candidate-profile.model';

import CandidateProfileService from './candidate-profile.service';

export default defineComponent({
  name: 'CandidateProfileDetails',
  setup() {
    const candidateProfileService = inject('candidateProfileService', () => new CandidateProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const candidateProfile: Ref<ICandidateProfile> = ref({});

    const retrieveCandidateProfile = async candidateProfileId => {
      try {
        const res = await candidateProfileService().find(candidateProfileId);
        candidateProfile.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.candidateProfileId) {
      retrieveCandidateProfile(route.params.candidateProfileId);
    }

    return {
      alertService,
      candidateProfile,

      ...dataUtils,

      previousState,
    };
  },
});
