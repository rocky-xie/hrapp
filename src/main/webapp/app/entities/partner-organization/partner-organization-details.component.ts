import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPartnerOrganization } from '@/shared/model/partner-organization.model';

import PartnerOrganizationService from './partner-organization.service';

export default defineComponent({
  name: 'PartnerOrganizationDetails',
  setup() {
    const partnerOrganizationService = inject('partnerOrganizationService', () => new PartnerOrganizationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const partnerOrganization: Ref<IPartnerOrganization> = ref({});

    const retrievePartnerOrganization = async partnerOrganizationId => {
      try {
        const res = await partnerOrganizationService().find(partnerOrganizationId);
        partnerOrganization.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.partnerOrganizationId) {
      retrievePartnerOrganization(route.params.partnerOrganizationId);
    }

    return {
      alertService,
      partnerOrganization,

      ...dataUtils,

      previousState,
    };
  },
});
