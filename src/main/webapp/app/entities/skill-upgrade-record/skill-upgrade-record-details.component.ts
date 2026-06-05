import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISkillUpgradeRecord } from '@/shared/model/skill-upgrade-record.model';

import SkillUpgradeRecordService from './skill-upgrade-record.service';

export default defineComponent({
  name: 'SkillUpgradeRecordDetails',
  setup() {
    const skillUpgradeRecordService = inject('skillUpgradeRecordService', () => new SkillUpgradeRecordService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const skillUpgradeRecord: Ref<ISkillUpgradeRecord> = ref({});

    const retrieveSkillUpgradeRecord = async skillUpgradeRecordId => {
      try {
        const res = await skillUpgradeRecordService().find(skillUpgradeRecordId);
        skillUpgradeRecord.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.skillUpgradeRecordId) {
      retrieveSkillUpgradeRecord(route.params.skillUpgradeRecordId);
    }

    return {
      alertService,
      skillUpgradeRecord,

      ...dataUtils,

      previousState,
    };
  },
});
