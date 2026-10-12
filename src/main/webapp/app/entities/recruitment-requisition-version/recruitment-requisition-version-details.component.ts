import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IRecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';

import RecruitmentRequisitionVersionService from './recruitment-requisition-version.service';

export default defineComponent({
  name: 'RecruitmentRequisitionVersionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentRequisitionVersionService = inject(
      'recruitmentRequisitionVersionService',
      () => new RecruitmentRequisitionVersionService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const recruitmentRequisitionVersion: Ref<IRecruitmentRequisitionVersion> = ref({});

    const retrieveRecruitmentRequisitionVersion = async recruitmentRequisitionVersionId => {
      try {
        const res = await recruitmentRequisitionVersionService().find(recruitmentRequisitionVersionId);
        recruitmentRequisitionVersion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionVersionId) {
      retrieveRecruitmentRequisitionVersion(route.params.recruitmentRequisitionVersionId);
    }

    return {
      ...dateFormat,
      alertService,
      recruitmentRequisitionVersion,

      ...dataUtils,

      previousState,
    };
  },
});
