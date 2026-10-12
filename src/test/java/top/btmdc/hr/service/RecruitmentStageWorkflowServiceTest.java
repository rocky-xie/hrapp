package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;

@ExtendWith(MockitoExtension.class)
class RecruitmentStageWorkflowServiceTest {

    @Mock
    private RecruitmentApplicationRepository applications;

    @Mock
    private ApplicationStageRepository stages;

    @Mock
    private ApplicationEventRepository events;

    @Mock
    private HiringDecisionRepository decisions;

    private RecruitmentStageWorkflowService service;

    @BeforeEach
    void setUp() {
        service = new RecruitmentStageWorkflowService(applications, stages, events, decisions);
    }

    @Test
    void graduateSecondInterviewCompletesSelectionWithoutCreatingApproval() {
        RecruitmentApplication application = application(RecruitmentType.GRADUATE, ApplicationStageType.FIRST_INTERVIEW);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication result = service.recordStageResult(3L, ApplicationStageType.SECOND_INTERVIEW, true, "hr", "Strong fit");

        assertThat(result.getCurrentStage()).isEqualTo(ApplicationStageType.SECOND_INTERVIEW);
        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.SELECTION_COMPLETED);
        assertThat(result.getDecision()).isNull();
        verify(stages).save(any());
        verify(events).save(any());
    }

    @Test
    void stageSequenceCannotSkipWrittenTest() {
        RecruitmentApplication application = application(RecruitmentType.MID_CAREER, null);
        when(applications.findById(3L)).thenReturn(Optional.of(application));

        assertThatThrownBy(() -> service.recordStageResult(3L, ApplicationStageType.FIRST_INTERVIEW, true, "hr", null))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("RESUME_SCREENING");
        verifyNoInteractions(stages, events, decisions);
    }

    @Test
    void midCareerWrittenTestIsRecordedButDoesNotCreateASeparateScreeningDecision() {
        RecruitmentApplication application = application(RecruitmentType.MID_CAREER, ApplicationStageType.RESUME_SCREENING);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication result = service.recordStageResult(3L, ApplicationStageType.WRITTEN_TEST, false, "hr", "Raw test result");

        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.IN_PROGRESS);
        assertThat(result.getCurrentStage()).isEqualTo(ApplicationStageType.WRITTEN_TEST);
        verify(stages).save(argThat(stage -> stage.getSelected() == null));
        verify(events).save(argThat(event -> "RESULT_RECORDED".equals(event.getNewValue())));
    }

    @Test
    void rejectedStageClosesApplicationWithFiveYearRetention() {
        RecruitmentApplication application = application(RecruitmentType.GRADUATE, null);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication result = service.recordStageResult(
            3L,
            ApplicationStageType.RESUME_SCREENING,
            false,
            "hr",
            "Insufficient evidence"
        );

        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.CLOSED);
        assertThat(result.getDecision()).isEqualTo(ApplicationDecision.NOT_HIRED);
        assertThat(result.getRetentionUntil()).isEqualTo(LocalDate.now().plusYears(5));
    }

    @Test
    void dispatchDecisionDoesNotDependOnAPartnerNameHardcodedInTheWorkflow() {
        RecruitmentApplication application = application(RecruitmentType.MID_CAREER, ApplicationStageType.FINAL_INTERVIEW);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication result = service.recordMidCareerFinalDecision(
            3L,
            ApplicationDecision.ACCEPT_AS_DISPATCHED,
            "executive",
            "hr",
            null
        );

        assertThat(result.getDecision()).isEqualTo(ApplicationDecision.ACCEPT_AS_DISPATCHED);
        verify(events).save(any());
        verify(decisions).save(any());
    }

    @Test
    void graduateExternalApprovalResultIsRecordedByHrAfterSelectionEnds() {
        RecruitmentApplication application = application(RecruitmentType.GRADUATE, ApplicationStageType.SECOND_INTERVIEW).status(
            ApplicationStatus.SELECTION_COMPLETED
        );
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication result = service.recordGraduateHiringDecision(3L, ApplicationDecision.HIRED, "hr", "Approval recorded");

        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.DECIDED);
        assertThat(result.getDecision()).isEqualTo(ApplicationDecision.HIRED);
        assertThat(result.getRetentionUntil()).isNull();
        verify(decisions).save(any());
        verify(events).save(any());
    }

    private RecruitmentApplication application(RecruitmentType type, ApplicationStageType current) {
        return new RecruitmentApplication()
            .id(3L)
            .recruitmentType(type)
            .currentStage(current)
            .status(ApplicationStatus.IN_PROGRESS)
            .sourceDetail("job board");
    }
}
