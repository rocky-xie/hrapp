import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IStaffSubstitution } from '@/shared/model/staff-substitution.model';

import StaffSubstitutionService from './staff-substitution.service';

export default defineComponent({
  name: 'StaffSubstitutionDetails',
  setup() {
    const staffSubstitutionService = inject('staffSubstitutionService', () => new StaffSubstitutionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const staffSubstitution: Ref<IStaffSubstitution> = ref({});

    const retrieveStaffSubstitution = async staffSubstitutionId => {
      try {
        const res = await staffSubstitutionService().find(staffSubstitutionId);
        staffSubstitution.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.staffSubstitutionId) {
      retrieveStaffSubstitution(route.params.staffSubstitutionId);
    }

    return {
      alertService,
      staffSubstitution,

      ...dataUtils,

      previousState,
    };
  },
});
