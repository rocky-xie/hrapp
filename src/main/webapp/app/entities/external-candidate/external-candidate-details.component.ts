import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IExternalCandidate } from '@/shared/model/external-candidate.model';

import ExternalCandidateService from './external-candidate.service';

export default defineComponent({
  name: 'ExternalCandidateDetails',
  setup() {
    const dateFormat = useDateFormat();
    const externalCandidateService = inject('externalCandidateService', () => new ExternalCandidateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const externalCandidate: Ref<IExternalCandidate> = ref({});

    const retrieveExternalCandidate = async externalCandidateId => {
      try {
        const res = await externalCandidateService().find(externalCandidateId);
        externalCandidate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.externalCandidateId) {
      retrieveExternalCandidate(route.params.externalCandidateId);
    }

    return {
      ...dateFormat,
      alertService,
      externalCandidate,

      ...dataUtils,

      previousState,
    };
  },
});
