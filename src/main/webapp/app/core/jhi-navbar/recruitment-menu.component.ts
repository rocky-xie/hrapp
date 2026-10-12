import { defineComponent, inject, onMounted, ref } from 'vue';
import type AccountService from '@/account/account.service';
import { Authority } from '@/shared/jhipster/constants';

export default defineComponent({
  name: 'RecruitmentMenu',
  setup() {
    const accountService = inject<AccountService>('accountService');
    const canAccessRecruitment = ref(false);
    onMounted(async () => {
      canAccessRecruitment.value = (await accountService?.hasAnyAuthorityAndCheckAuth([Authority.HR_RECRUITER, Authority.ADMIN])) ?? false;
    });
    return { canAccessRecruitment };
  },
});
