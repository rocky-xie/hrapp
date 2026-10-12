import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

import RecruitmentApplicationService from './recruitment-application.service';

export default defineComponent({
  name: 'RecruitmentApplicationDetails',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentApplicationService = inject('recruitmentApplicationService', () => new RecruitmentApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const recruitmentApplication: Ref<IRecruitmentApplication> = ref({});

    const retrieveRecruitmentApplication = async recruitmentApplicationId => {
      try {
        const res = await recruitmentApplicationService().find(recruitmentApplicationId);
        recruitmentApplication.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentApplicationId) {
      retrieveRecruitmentApplication(route.params.recruitmentApplicationId);
    }

    return {
      ...dateFormat,
      alertService,
      recruitmentApplication,

      ...dataUtils,

      previousState,
    };
  },
});
