import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ITrustObservation } from '@/shared/model/trust-observation.model';

import TrustObservationService from './trust-observation.service';

export default defineComponent({
  name: 'TrustObservationDetails',
  setup() {
    const trustObservationService = inject('trustObservationService', () => new TrustObservationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const trustObservation: Ref<ITrustObservation> = ref({});

    const retrieveTrustObservation = async trustObservationId => {
      try {
        const res = await trustObservationService().find(trustObservationId);
        trustObservation.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.trustObservationId) {
      retrieveTrustObservation(route.params.trustObservationId);
    }

    return {
      alertService,
      trustObservation,

      ...dataUtils,

      previousState,
    };
  },
});
