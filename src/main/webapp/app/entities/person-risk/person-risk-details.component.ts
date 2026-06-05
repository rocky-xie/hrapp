import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPersonRisk } from '@/shared/model/person-risk.model';

import PersonRiskService from './person-risk.service';

export default defineComponent({
  name: 'PersonRiskDetails',
  setup() {
    const personRiskService = inject('personRiskService', () => new PersonRiskService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const personRisk: Ref<IPersonRisk> = ref({});

    const retrievePersonRisk = async personRiskId => {
      try {
        const res = await personRiskService().find(personRiskId);
        personRisk.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personRiskId) {
      retrievePersonRisk(route.params.personRiskId);
    }

    return {
      alertService,
      personRisk,

      ...dataUtils,

      previousState,
    };
  },
});
