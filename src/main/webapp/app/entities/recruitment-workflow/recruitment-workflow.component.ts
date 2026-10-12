import { defineComponent, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import RecruitmentWorkflowService from './recruitment-workflow.service';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export default defineComponent({
  setup() {
    const { t } = useI18n();
    const service = new RecruitmentWorkflowService();
    const applications = ref<IRecruitmentApplication[]>([]);
    const notes = ref('');
    const reviewer = ref('');
    const joinDate = ref('');
    const error = ref('');
    const notice = ref('');
    const load = async () => {
      error.value = '';
      try {
        applications.value = await service.listApplications();
      } catch {
        error.value = t('recruitment.workflow.loadError');
      }
    };
    const run = async (operation: () => Promise<void>) => {
      error.value = '';
      notice.value = '';
      try {
        await operation();
        notice.value = t('recruitment.workflow.saved');
        notes.value = '';
        await load();
      } catch {
        error.value = t('recruitment.workflow.actionError');
      }
    };
    const nextStage = (application: IRecruitmentApplication): string => {
      if (!application.currentStage) return 'RESUME_SCREENING';
      if (application.currentStage === 'RESUME_SCREENING') return 'WRITTEN_TEST';
      if (application.currentStage === 'WRITTEN_TEST') return 'FIRST_INTERVIEW';
      return application.recruitmentType === 'GRADUATE' ? 'SECOND_INTERVIEW' : 'FINAL_INTERVIEW';
    };
    const stageAction = (application: IRecruitmentApplication) =>
      t('recruitment.workflow.continue', { stage: t(`recruitment.enums.${nextStage(application)}`) });
    const recordStage = (application: IRecruitmentApplication, selected: boolean) =>
      run(() => service.recordStage(application.id!, nextStage(application), selected, notes.value));
    const decide = (application: IRecruitmentApplication, decision: string) => {
      if (application.recruitmentType === 'GRADUATE') return run(() => service.graduateDecision(application.id!, decision, notes.value));
      if (!reviewer.value.trim()) {
        error.value = t('recruitment.workflow.reviewerRequired');
        return Promise.resolve();
      }
      return run(() => service.finalDecision(application.id!, decision, reviewer.value.trim(), notes.value));
    };
    const convert = (application: IRecruitmentApplication) => run(() => service.convertGraduate(application.id!));
    const checkIn = (application: IRecruitmentApplication) => {
      if (!joinDate.value) {
        error.value = t('recruitment.workflow.joinDateRequired');
        return Promise.resolve();
      }
      return run(() => service.checkIn(application.id!, joinDate.value));
    };
    onMounted(load);
    return { applications, notes, reviewer, joinDate, error, notice, load, stageAction, recordStage, decide, convert, checkIn };
  },
});
