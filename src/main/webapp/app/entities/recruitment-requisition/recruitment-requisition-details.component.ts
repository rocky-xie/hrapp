import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentRequisitionService from './recruitment-requisition.service';

export default defineComponent({
  name: 'RecruitmentRequisitionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentRequisitionService = inject('recruitmentRequisitionService', () => new RecruitmentRequisitionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const recruitmentRequisition: Ref<IRecruitmentRequisition> = ref({});

    const retrieveRecruitmentRequisition = async recruitmentRequisitionId => {
      try {
        const res = await recruitmentRequisitionService().find(recruitmentRequisitionId);
        recruitmentRequisition.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionId) {
      retrieveRecruitmentRequisition(route.params.recruitmentRequisitionId);
    }

    return {
      ...dateFormat,
      alertService,
      recruitmentRequisition,

      ...dataUtils,

      previousState,
    };
  },
});
