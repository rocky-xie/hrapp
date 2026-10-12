package top.btmdc.hr.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.domain.ApplicationStage;
import top.btmdc.hr.domain.HiringDecision;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;

/** Records stage outcomes and enforces the graduate and mid-career selection paths. */
@Service
@Transactional
public class RecruitmentStageWorkflowService {

    private static final int RETENTION_YEARS = 5;
    private final RecruitmentApplicationRepository applications;
    private final ApplicationStageRepository stages;
    private final ApplicationEventRepository events;
    private final HiringDecisionRepository decisions;

    public RecruitmentStageWorkflowService(
        RecruitmentApplicationRepository applications,
        ApplicationStageRepository stages,
        ApplicationEventRepository events,
        HiringDecisionRepository decisions
    ) {
        this.applications = applications;
        this.stages = stages;
        this.events = events;
        this.decisions = decisions;
    }

    public RecruitmentApplication recordStageResult(
        Long applicationId,
        ApplicationStageType completedStage,
        boolean selected,
        String actor,
        String notes
    ) {
        RecruitmentApplication application = applications
            .findById(applicationId)
            .orElseThrow(() -> new NoSuchElementException("Application not found: " + applicationId));
        if (application.getStatus() != ApplicationStatus.IN_PROGRESS) throw new IllegalStateException("Application is not in progress");
        ApplicationStageType expected = nextStage(application.getRecruitmentType(), application.getCurrentStage());
        if (expected != completedStage) throw new IllegalStateException("Expected stage " + expected + " but received " + completedStage);

        Instant now = Instant.now();
        ApplicationStage stage = new ApplicationStage()
            .stageType(completedStage)
            .sequenceNumber(sequence(application.getRecruitmentType(), completedStage))
            .stageStatus("COMPLETED")
            .startedAt(now)
            .completedAt(now)
            .selected(isUnscreenedMidCareerWrittenTest(application, completedStage) ? null : selected)
            .decisionReason(notes)
            .application(application);
        stages.save(stage);

        boolean proceedToNextStage = selected || isUnscreenedMidCareerWrittenTest(application, completedStage);
        if (!proceedToNextStage) {
            application.setStatus(ApplicationStatus.CLOSED);
            application.setDecision(ApplicationDecision.NOT_HIRED);
            close(application, now);
        } else if (isTerminalSelectionStage(application.getRecruitmentType(), completedStage)) {
            application.setCurrentStage(completedStage);
            application.setStatus(ApplicationStatus.SELECTION_COMPLETED);
        } else {
            application.setCurrentStage(completedStage);
        }
        events.save(
            new ApplicationEvent()
                .stageType(completedStage)
                .eventType("STAGE_RESULT")
                .previousValue("IN_PROGRESS")
                .newValue(
                    isUnscreenedMidCareerWrittenTest(application, completedStage)
                        ? "RESULT_RECORDED"
                        : selected
                            ? "SELECTED"
                            : "NOT_SELECTED"
                )
                .actor(actor)
                .occurredAt(now)
                .reason(notes)
                .application(application)
        );
        return applications.save(application);
    }

    private boolean isUnscreenedMidCareerWrittenTest(RecruitmentApplication application, ApplicationStageType stage) {
        return application.getRecruitmentType() == RecruitmentType.MID_CAREER && stage == ApplicationStageType.WRITTEN_TEST;
    }

    public RecruitmentApplication recordMidCareerFinalDecision(
        Long applicationId,
        ApplicationDecision decision,
        String reviewer,
        String recordedBy,
        String notes
    ) {
        RecruitmentApplication application = applications
            .findById(applicationId)
            .orElseThrow(() -> new NoSuchElementException("Application not found: " + applicationId));
        if (
            application.getRecruitmentType() != RecruitmentType.MID_CAREER ||
            application.getCurrentStage() != ApplicationStageType.FINAL_INTERVIEW
        ) {
            throw new IllegalStateException("Only mid-career final interviews can record the executive decision");
        }
        if (
            !ApplicationDecision.HIRED.equals(decision) &&
            !ApplicationDecision.NOT_HIRED.equals(decision) &&
            !ApplicationDecision.ACCEPT_AS_DISPATCHED.equals(decision)
        ) {
            throw new IllegalArgumentException("Unsupported final decision");
        }
        Instant now = Instant.now();
        application.setDecision(decision);
        application.setStatus(ApplicationStatus.DECIDED);
        close(application, now);
        decisions.save(
            new HiringDecision()
                .decision(decision)
                .decisionSource("EXECUTIVE_REVIEW_RECORDED_BY_HR:" + recordedBy)
                .decidedBy(reviewer)
                .decidedAt(now)
                .reason(notes)
                .active(true)
                .application(application)
        );
        events.save(
            new ApplicationEvent()
                .stageType(ApplicationStageType.FINAL_INTERVIEW)
                .eventType("EXECUTIVE_DECISION")
                .newValue(decision.name())
                .actor(recordedBy)
                .occurredAt(now)
                .reason(notes)
                .application(application)
        );
        return applications.save(application);
    }

    public RecruitmentApplication recordGraduateHiringDecision(
        Long applicationId,
        ApplicationDecision decision,
        String hrActor,
        String notes
    ) {
        RecruitmentApplication application = applications
            .findById(applicationId)
            .orElseThrow(() -> new NoSuchElementException("Application not found: " + applicationId));
        if (
            application.getRecruitmentType() != RecruitmentType.GRADUATE || application.getStatus() != ApplicationStatus.SELECTION_COMPLETED
        ) {
            throw new IllegalStateException("Graduate decision is available after the second interview only");
        }
        if (decision != ApplicationDecision.HIRED && decision != ApplicationDecision.NOT_HIRED && decision != ApplicationDecision.RESERVE) {
            throw new IllegalArgumentException("Unsupported graduate decision");
        }
        Instant now = Instant.now();
        application.setDecision(decision);
        application.setStatus(ApplicationStatus.DECIDED);
        if (decision != ApplicationDecision.HIRED) close(application, now);
        decisions.save(
            new HiringDecision()
                .decision(decision)
                .decisionSource("EXTERNAL_APPROVAL_RECORDED_BY_HR")
                .decidedBy(hrActor)
                .decidedAt(now)
                .reason(notes)
                .active(true)
                .application(application)
        );
        events.save(
            new ApplicationEvent()
                .eventType("EXTERNAL_APPROVAL_RESULT")
                .newValue(decision.name())
                .actor(hrActor)
                .occurredAt(now)
                .reason(notes)
                .application(application)
        );
        return applications.save(application);
    }

    private void close(RecruitmentApplication application, Instant at) {
        application.setClosedAt(at);
        application.setRetentionUntil(LocalDate.ofInstant(at, java.time.ZoneOffset.UTC).plusYears(RETENTION_YEARS));
    }

    private ApplicationStageType nextStage(RecruitmentType type, ApplicationStageType current) {
        if (current == null) return ApplicationStageType.RESUME_SCREENING;
        if (current == ApplicationStageType.RESUME_SCREENING) return ApplicationStageType.WRITTEN_TEST;
        if (current == ApplicationStageType.WRITTEN_TEST) return ApplicationStageType.FIRST_INTERVIEW;
        if (
            type == RecruitmentType.GRADUATE && current == ApplicationStageType.FIRST_INTERVIEW
        ) return ApplicationStageType.SECOND_INTERVIEW;
        if (
            type == RecruitmentType.MID_CAREER && current == ApplicationStageType.FIRST_INTERVIEW
        ) return ApplicationStageType.FINAL_INTERVIEW;
        throw new IllegalStateException("No further selection stage is defined for " + type + " at " + current);
    }

    private boolean isTerminalSelectionStage(RecruitmentType type, ApplicationStageType stage) {
        return (
            (type == RecruitmentType.GRADUATE && stage == ApplicationStageType.SECOND_INTERVIEW) ||
            (type == RecruitmentType.MID_CAREER && stage == ApplicationStageType.FINAL_INTERVIEW)
        );
    }

    private int sequence(RecruitmentType type, ApplicationStageType stage) {
        return switch (stage) {
            case RESUME_SCREENING -> 1;
            case WRITTEN_TEST -> 2;
            case FIRST_INTERVIEW -> 3;
            case SECOND_INTERVIEW -> 4;
            case FINAL_INTERVIEW -> type == RecruitmentType.MID_CAREER ? 4 : 5;
        };
    }
}
