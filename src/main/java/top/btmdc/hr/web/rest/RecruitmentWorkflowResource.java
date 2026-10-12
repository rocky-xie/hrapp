package top.btmdc.hr.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.DispatchReview;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RequisitionEditEvent;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.security.SecurityUtils;
import top.btmdc.hr.service.DispatchLifecycleService;
import top.btmdc.hr.service.RecruitmentConversionService;
import top.btmdc.hr.service.RecruitmentNotificationService;
import top.btmdc.hr.service.RecruitmentRequisitionWorkflowService;
import top.btmdc.hr.service.RecruitmentStageWorkflowService;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/** Explicit HR actions for publishing and reopening recruitment requirements. */
@RestController
@RequestMapping("/api/recruitment-workflow")
@PreAuthorize("hasAnyAuthority('ROLE_HR_RECRUITER', 'ROLE_ADMIN')")
public class RecruitmentWorkflowResource {

    private final RecruitmentRequisitionWorkflowService workflow;
    private final RecruitmentStageWorkflowService stageWorkflow;
    private final RecruitmentConversionService conversionWorkflow;
    private final RecruitmentNotificationService notificationWorkflow;
    private final DispatchLifecycleService dispatchWorkflow;

    public RecruitmentWorkflowResource(
        RecruitmentRequisitionWorkflowService workflow,
        RecruitmentStageWorkflowService stageWorkflow,
        RecruitmentConversionService conversionWorkflow,
        RecruitmentNotificationService notificationWorkflow,
        DispatchLifecycleService dispatchWorkflow
    ) {
        this.workflow = workflow;
        this.stageWorkflow = stageWorkflow;
        this.conversionWorkflow = conversionWorkflow;
        this.notificationWorkflow = notificationWorkflow;
        this.dispatchWorkflow = dispatchWorkflow;
    }

    @PostMapping("/requisitions/{id}/lock")
    public ResponseEntity<RequisitionWorkflowResult> lock(@PathVariable Long id) {
        return ResponseEntity.ok(RequisitionWorkflowResult.from(workflow.lock(id, currentActor())));
    }

    @PostMapping("/requisitions/{id}/unlock")
    public ResponseEntity<RequisitionWorkflowResult> unlock(@PathVariable Long id, @Valid @RequestBody UnlockRequest request) {
        return ResponseEntity.ok(RequisitionWorkflowResult.from(workflow.unlock(id, currentActor(), request.reason())));
    }

    @GetMapping("/requisitions/{id}/history")
    public ResponseEntity<java.util.List<RequisitionEditEventResult>> history(@PathVariable Long id) {
        return ResponseEntity.ok(workflow.history(id).stream().map(RequisitionEditEventResult::from).toList());
    }

    @PostMapping("/applications/{id}/stage-result")
    public ResponseEntity<ApplicationWorkflowResult> recordStageResult(
        @PathVariable Long id,
        @Valid @RequestBody StageResultRequest request
    ) {
        return ResponseEntity.ok(
            ApplicationWorkflowResult.from(
                stageWorkflow.recordStageResult(id, request.stage(), request.selected(), currentActor(), request.notes())
            )
        );
    }

    @PostMapping("/applications/{id}/final-decision")
    public ResponseEntity<ApplicationWorkflowResult> finalDecision(
        @PathVariable Long id,
        @Valid @RequestBody FinalDecisionRequest request
    ) {
        return ResponseEntity.ok(
            ApplicationWorkflowResult.from(
                stageWorkflow.recordMidCareerFinalDecision(id, request.decision(), request.reviewer(), currentActor(), request.notes())
            )
        );
    }

    @PostMapping("/applications/{id}/graduate-decision")
    public ResponseEntity<ApplicationWorkflowResult> graduateDecision(
        @PathVariable Long id,
        @Valid @RequestBody GraduateDecisionRequest request
    ) {
        return ResponseEntity.ok(
            ApplicationWorkflowResult.from(
                stageWorkflow.recordGraduateHiringDecision(id, request.decision(), currentActor(), request.notes())
            )
        );
    }

    @PostMapping("/applications/{id}/convert-graduate")
    public ResponseEntity<ApplicationWorkflowResult> convertGraduate(@PathVariable Long id) {
        return ResponseEntity.ok(ApplicationWorkflowResult.from(conversionWorkflow.convertGraduate(id, currentActor())));
    }

    @PostMapping("/applications/{id}/check-in")
    public ResponseEntity<ApplicationWorkflowResult> checkIn(@PathVariable Long id, @Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(ApplicationWorkflowResult.from(conversionWorkflow.checkIn(id, request.joinDate(), currentActor())));
    }

    @PostMapping("/applications/{id}/dispatch-start")
    public ResponseEntity<DispatchAssignmentResult> startDispatch(@PathVariable Long id, @Valid @RequestBody DispatchStartRequest request) {
        return ResponseEntity.ok(
            DispatchAssignmentResult.from(
                dispatchWorkflow.start(
                    id,
                    request.partnerOrganizationId(),
                    request.department(),
                    request.positionId(),
                    request.startDate(),
                    currentActor()
                )
            )
        );
    }

    @PostMapping("/dispatch-assignments/{id}/review")
    public ResponseEntity<DispatchReviewResult> reviewDispatch(@PathVariable Long id, @Valid @RequestBody DispatchReviewRequest request) {
        return ResponseEntity.ok(
            DispatchReviewResult.from(
                dispatchWorkflow.review(id, request.decision(), request.result(), request.extensionEndDate(), currentActor())
            )
        );
    }

    @PostMapping("/notifications/drafts")
    public ResponseEntity<NotificationMessageDTO> createNotificationDraft(@Valid @RequestBody NotificationDraftRequest request) {
        try {
            return ResponseEntity.ok(
                notificationWorkflow.createDraft(
                    request.templateId(),
                    request.applicationId(),
                    request.recipients(),
                    request.variables(),
                    currentActor()
                )
            );
        } catch (IllegalArgumentException exception) {
            throw new BadRequestAlertException(exception.getMessage(), "notificationMessage", "draftinvalid");
        }
    }

    @PostMapping("/notifications/{id}/send")
    public ResponseEntity<NotificationMessageDTO> confirmAndSendNotification(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notificationWorkflow.confirmAndSend(id, currentActor()));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestAlertException(exception.getMessage(), "notificationMessage", "sendinvalid");
        }
    }

    public record UnlockRequest(@jakarta.validation.constraints.NotBlank String reason) {}

    public record StageResultRequest(@NotNull ApplicationStageType stage, boolean selected, String notes) {}

    public record FinalDecisionRequest(
        @NotNull ApplicationDecision decision,
        @jakarta.validation.constraints.NotBlank String reviewer,
        String notes
    ) {}

    public record GraduateDecisionRequest(@NotNull ApplicationDecision decision, String notes) {}

    public record CheckInRequest(@NotNull java.time.LocalDate joinDate) {}

    public record DispatchStartRequest(
        @NotNull Long partnerOrganizationId,
        @jakarta.validation.constraints.NotBlank String department,
        Long positionId,
        @NotNull java.time.LocalDate startDate
    ) {}

    public record DispatchReviewRequest(@NotNull String decision, String result, java.time.LocalDate extensionEndDate) {}

    public record DispatchAssignmentResult(
        Long id,
        Long personId,
        String status,
        String finalDecision,
        java.time.LocalDate expectedEndDate
    ) {
        static DispatchAssignmentResult from(DispatchAssignment assignment) {
            return new DispatchAssignmentResult(
                assignment.getId(),
                assignment.getPersonId(),
                assignment.getStatus(),
                assignment.getFinalDecision(),
                assignment.getExpectedEndDate()
            );
        }
    }

    public record DispatchReviewResult(Long id, String decision, String reviewer, java.time.Instant reviewedAt) {
        static DispatchReviewResult from(DispatchReview review) {
            return new DispatchReviewResult(
                review.getId(),
                review.getFinalDecision(),
                review.getReviewerUsername(),
                review.getReviewedAt()
            );
        }
    }

    public record NotificationDraftRequest(
        @NotNull Long templateId,
        Long applicationId,
        @NotNull java.util.List<String> recipients,
        java.util.Map<String, String> variables
    ) {}

    public record RequisitionEditEventResult(
        Long id,
        String eventType,
        String actor,
        java.time.Instant occurredAt,
        Integer versionNumber,
        String reason
    ) {
        static RequisitionEditEventResult from(RequisitionEditEvent event) {
            return new RequisitionEditEventResult(
                event.getId(),
                event.getEventType(),
                event.getActor(),
                event.getOccurredAt(),
                event.getVersionNumber(),
                event.getReason()
            );
        }
    }

    public record RequisitionWorkflowResult(Long id, Integer version, String editStatus, String lifecycle, String actor, String reason) {
        static RequisitionWorkflowResult from(RecruitmentRequisition requisition) {
            return new RequisitionWorkflowResult(
                requisition.getId(),
                requisition.getCurrentVersion(),
                requisition.getEditStatus().name(),
                requisition.getLifecycle().name(),
                requisition.getLockedBy(),
                requisition.getUnlockReason()
            );
        }
    }

    public record ApplicationWorkflowResult(
        Long id,
        String status,
        String currentStage,
        String decision,
        java.time.LocalDate retentionUntil
    ) {
        static ApplicationWorkflowResult from(RecruitmentApplication application) {
            return new ApplicationWorkflowResult(
                application.getId(),
                application.getStatus().name(),
                application.getCurrentStage() == null ? null : application.getCurrentStage().name(),
                application.getDecision() == null ? null : application.getDecision().name(),
                application.getRetentionUntil()
            );
        }
    }

    private String currentActor() {
        return SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("Authenticated actor is required"));
    }
}
