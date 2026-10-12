import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPreboardingFollowUp } from '@/shared/model/preboarding-follow-up.model';

import PreboardingFollowUpService from './preboarding-follow-up.service';

export default defineComponent({
  name: 'PreboardingFollowUpDetails',
  setup() {
    const dateFormat = useDateFormat();
    const preboardingFollowUpService = inject('preboardingFollowUpService', () => new PreboardingFollowUpService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const preboardingFollowUp: Ref<IPreboardingFollowUp> = ref({});

    const retrievePreboardingFollowUp = async preboardingFollowUpId => {
      try {
        const res = await preboardingFollowUpService().find(preboardingFollowUpId);
        preboardingFollowUp.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.preboardingFollowUpId) {
      retrievePreboardingFollowUp(route.params.preboardingFollowUpId);
    }

    return {
      ...dateFormat,
      alertService,
      preboardingFollowUp,

      ...dataUtils,

      previousState,
    };
  },
});
