import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IRecruitmentRequisitionMember } from '@/shared/model/recruitment-requisition-member.model';

import RecruitmentRequisitionMemberService from './recruitment-requisition-member.service';

export default defineComponent({
  name: 'RecruitmentRequisitionMemberDetails',
  setup() {
    const dateFormat = useDateFormat();
    const recruitmentRequisitionMemberService = inject(
      'recruitmentRequisitionMemberService',
      () => new RecruitmentRequisitionMemberService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const recruitmentRequisitionMember: Ref<IRecruitmentRequisitionMember> = ref({});

    const retrieveRecruitmentRequisitionMember = async recruitmentRequisitionMemberId => {
      try {
        const res = await recruitmentRequisitionMemberService().find(recruitmentRequisitionMemberId);
        recruitmentRequisitionMember.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.recruitmentRequisitionMemberId) {
      retrieveRecruitmentRequisitionMember(route.params.recruitmentRequisitionMemberId);
    }

    return {
      ...dateFormat,
      alertService,
      recruitmentRequisitionMember,

      previousState,
    };
  },
});
