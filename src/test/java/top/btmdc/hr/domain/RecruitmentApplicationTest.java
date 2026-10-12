package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.ApplicationEventTestSamples.*;
import static top.btmdc.hr.domain.ApplicationStageTestSamples.*;
import static top.btmdc.hr.domain.DispatchAssignmentTestSamples.*;
import static top.btmdc.hr.domain.ExternalCandidateTestSamples.*;
import static top.btmdc.hr.domain.HiringDecisionTestSamples.*;
import static top.btmdc.hr.domain.NotificationMessageTestSamples.*;
import static top.btmdc.hr.domain.PreboardingFollowUpTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentEvaluationTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionVersionTestSamples.*;
import static top.btmdc.hr.domain.SelectionActivityTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentApplication.class);
        RecruitmentApplication recruitmentApplication1 = getRecruitmentApplicationSample1();
        RecruitmentApplication recruitmentApplication2 = new RecruitmentApplication();
        assertThat(recruitmentApplication1).isNotEqualTo(recruitmentApplication2);

        recruitmentApplication2.setId(recruitmentApplication1.getId());
        assertThat(recruitmentApplication1).isEqualTo(recruitmentApplication2);

        recruitmentApplication2 = getRecruitmentApplicationSample2();
        assertThat(recruitmentApplication1).isNotEqualTo(recruitmentApplication2);
    }

    @Test
    void stagesTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        ApplicationStage applicationStageBack = getApplicationStageRandomSampleGenerator();

        recruitmentApplication.addStages(applicationStageBack);
        assertThat(recruitmentApplication.getStageses()).containsOnly(applicationStageBack);
        assertThat(applicationStageBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeStages(applicationStageBack);
        assertThat(recruitmentApplication.getStageses()).doesNotContain(applicationStageBack);
        assertThat(applicationStageBack.getApplication()).isNull();

        recruitmentApplication.stageses(new HashSet<>(Set.of(applicationStageBack)));
        assertThat(recruitmentApplication.getStageses()).containsOnly(applicationStageBack);
        assertThat(applicationStageBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setStageses(new HashSet<>());
        assertThat(recruitmentApplication.getStageses()).doesNotContain(applicationStageBack);
        assertThat(applicationStageBack.getApplication()).isNull();
    }

    @Test
    void eventsTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        ApplicationEvent applicationEventBack = getApplicationEventRandomSampleGenerator();

        recruitmentApplication.addEvents(applicationEventBack);
        assertThat(recruitmentApplication.getEventses()).containsOnly(applicationEventBack);
        assertThat(applicationEventBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeEvents(applicationEventBack);
        assertThat(recruitmentApplication.getEventses()).doesNotContain(applicationEventBack);
        assertThat(applicationEventBack.getApplication()).isNull();

        recruitmentApplication.eventses(new HashSet<>(Set.of(applicationEventBack)));
        assertThat(recruitmentApplication.getEventses()).containsOnly(applicationEventBack);
        assertThat(applicationEventBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setEventses(new HashSet<>());
        assertThat(recruitmentApplication.getEventses()).doesNotContain(applicationEventBack);
        assertThat(applicationEventBack.getApplication()).isNull();
    }

    @Test
    void activitiesTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        SelectionActivity selectionActivityBack = getSelectionActivityRandomSampleGenerator();

        recruitmentApplication.addActivities(selectionActivityBack);
        assertThat(recruitmentApplication.getActivitieses()).containsOnly(selectionActivityBack);
        assertThat(selectionActivityBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeActivities(selectionActivityBack);
        assertThat(recruitmentApplication.getActivitieses()).doesNotContain(selectionActivityBack);
        assertThat(selectionActivityBack.getApplication()).isNull();

        recruitmentApplication.activitieses(new HashSet<>(Set.of(selectionActivityBack)));
        assertThat(recruitmentApplication.getActivitieses()).containsOnly(selectionActivityBack);
        assertThat(selectionActivityBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setActivitieses(new HashSet<>());
        assertThat(recruitmentApplication.getActivitieses()).doesNotContain(selectionActivityBack);
        assertThat(selectionActivityBack.getApplication()).isNull();
    }

    @Test
    void evaluationsTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        RecruitmentEvaluation recruitmentEvaluationBack = getRecruitmentEvaluationRandomSampleGenerator();

        recruitmentApplication.addEvaluations(recruitmentEvaluationBack);
        assertThat(recruitmentApplication.getEvaluationses()).containsOnly(recruitmentEvaluationBack);
        assertThat(recruitmentEvaluationBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeEvaluations(recruitmentEvaluationBack);
        assertThat(recruitmentApplication.getEvaluationses()).doesNotContain(recruitmentEvaluationBack);
        assertThat(recruitmentEvaluationBack.getApplication()).isNull();

        recruitmentApplication.evaluationses(new HashSet<>(Set.of(recruitmentEvaluationBack)));
        assertThat(recruitmentApplication.getEvaluationses()).containsOnly(recruitmentEvaluationBack);
        assertThat(recruitmentEvaluationBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setEvaluationses(new HashSet<>());
        assertThat(recruitmentApplication.getEvaluationses()).doesNotContain(recruitmentEvaluationBack);
        assertThat(recruitmentEvaluationBack.getApplication()).isNull();
    }

    @Test
    void decisionsTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        HiringDecision hiringDecisionBack = getHiringDecisionRandomSampleGenerator();

        recruitmentApplication.addDecisions(hiringDecisionBack);
        assertThat(recruitmentApplication.getDecisionses()).containsOnly(hiringDecisionBack);
        assertThat(hiringDecisionBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeDecisions(hiringDecisionBack);
        assertThat(recruitmentApplication.getDecisionses()).doesNotContain(hiringDecisionBack);
        assertThat(hiringDecisionBack.getApplication()).isNull();

        recruitmentApplication.decisionses(new HashSet<>(Set.of(hiringDecisionBack)));
        assertThat(recruitmentApplication.getDecisionses()).containsOnly(hiringDecisionBack);
        assertThat(hiringDecisionBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setDecisionses(new HashSet<>());
        assertThat(recruitmentApplication.getDecisionses()).doesNotContain(hiringDecisionBack);
        assertThat(hiringDecisionBack.getApplication()).isNull();
    }

    @Test
    void messagesTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        NotificationMessage notificationMessageBack = getNotificationMessageRandomSampleGenerator();

        recruitmentApplication.addMessages(notificationMessageBack);
        assertThat(recruitmentApplication.getMessageses()).containsOnly(notificationMessageBack);
        assertThat(notificationMessageBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeMessages(notificationMessageBack);
        assertThat(recruitmentApplication.getMessageses()).doesNotContain(notificationMessageBack);
        assertThat(notificationMessageBack.getApplication()).isNull();

        recruitmentApplication.messageses(new HashSet<>(Set.of(notificationMessageBack)));
        assertThat(recruitmentApplication.getMessageses()).containsOnly(notificationMessageBack);
        assertThat(notificationMessageBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setMessageses(new HashSet<>());
        assertThat(recruitmentApplication.getMessageses()).doesNotContain(notificationMessageBack);
        assertThat(notificationMessageBack.getApplication()).isNull();
    }

    @Test
    void followUpsTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        PreboardingFollowUp preboardingFollowUpBack = getPreboardingFollowUpRandomSampleGenerator();

        recruitmentApplication.addFollowUps(preboardingFollowUpBack);
        assertThat(recruitmentApplication.getFollowUpses()).containsOnly(preboardingFollowUpBack);
        assertThat(preboardingFollowUpBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeFollowUps(preboardingFollowUpBack);
        assertThat(recruitmentApplication.getFollowUpses()).doesNotContain(preboardingFollowUpBack);
        assertThat(preboardingFollowUpBack.getApplication()).isNull();

        recruitmentApplication.followUpses(new HashSet<>(Set.of(preboardingFollowUpBack)));
        assertThat(recruitmentApplication.getFollowUpses()).containsOnly(preboardingFollowUpBack);
        assertThat(preboardingFollowUpBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setFollowUpses(new HashSet<>());
        assertThat(recruitmentApplication.getFollowUpses()).doesNotContain(preboardingFollowUpBack);
        assertThat(preboardingFollowUpBack.getApplication()).isNull();
    }

    @Test
    void dispatchAssignmentsTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        DispatchAssignment dispatchAssignmentBack = getDispatchAssignmentRandomSampleGenerator();

        recruitmentApplication.addDispatchAssignments(dispatchAssignmentBack);
        assertThat(recruitmentApplication.getDispatchAssignmentses()).containsOnly(dispatchAssignmentBack);
        assertThat(dispatchAssignmentBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.removeDispatchAssignments(dispatchAssignmentBack);
        assertThat(recruitmentApplication.getDispatchAssignmentses()).doesNotContain(dispatchAssignmentBack);
        assertThat(dispatchAssignmentBack.getApplication()).isNull();

        recruitmentApplication.dispatchAssignmentses(new HashSet<>(Set.of(dispatchAssignmentBack)));
        assertThat(recruitmentApplication.getDispatchAssignmentses()).containsOnly(dispatchAssignmentBack);
        assertThat(dispatchAssignmentBack.getApplication()).isEqualTo(recruitmentApplication);

        recruitmentApplication.setDispatchAssignmentses(new HashSet<>());
        assertThat(recruitmentApplication.getDispatchAssignmentses()).doesNotContain(dispatchAssignmentBack);
        assertThat(dispatchAssignmentBack.getApplication()).isNull();
    }

    @Test
    void candidateTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        ExternalCandidate externalCandidateBack = getExternalCandidateRandomSampleGenerator();

        recruitmentApplication.setCandidate(externalCandidateBack);
        assertThat(recruitmentApplication.getCandidate()).isEqualTo(externalCandidateBack);

        recruitmentApplication.candidate(null);
        assertThat(recruitmentApplication.getCandidate()).isNull();
    }

    @Test
    void requisitionTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        RecruitmentRequisition recruitmentRequisitionBack = getRecruitmentRequisitionRandomSampleGenerator();

        recruitmentApplication.setRequisition(recruitmentRequisitionBack);
        assertThat(recruitmentApplication.getRequisition()).isEqualTo(recruitmentRequisitionBack);

        recruitmentApplication.requisition(null);
        assertThat(recruitmentApplication.getRequisition()).isNull();
    }

    @Test
    void requisitionVersionTest() {
        RecruitmentApplication recruitmentApplication = getRecruitmentApplicationRandomSampleGenerator();
        RecruitmentRequisitionVersion recruitmentRequisitionVersionBack = getRecruitmentRequisitionVersionRandomSampleGenerator();

        recruitmentApplication.setRequisitionVersion(recruitmentRequisitionVersionBack);
        assertThat(recruitmentApplication.getRequisitionVersion()).isEqualTo(recruitmentRequisitionVersionBack);

        recruitmentApplication.requisitionVersion(null);
        assertThat(recruitmentApplication.getRequisitionVersion()).isNull();
    }
}
