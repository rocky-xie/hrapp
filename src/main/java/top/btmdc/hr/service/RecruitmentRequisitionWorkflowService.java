package top.btmdc.hr.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.domain.RequisitionEditEvent;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.repository.RequisitionEditEventRepository;

/** HR finalization and immutable version history for collaborative requisition editing. */
@Service
@Transactional
public class RecruitmentRequisitionWorkflowService {

    private final RecruitmentRequisitionRepository requisitions;
    private final RecruitmentRequisitionVersionRepository versions;
    private final RequisitionEditEventRepository editEvents;

    public RecruitmentRequisitionWorkflowService(
        RecruitmentRequisitionRepository requisitions,
        RecruitmentRequisitionVersionRepository versions,
        RequisitionEditEventRepository editEvents
    ) {
        this.requisitions = requisitions;
        this.versions = versions;
        this.editEvents = editEvents;
    }

    public RecruitmentRequisition lock(Long id, String actor) {
        RecruitmentRequisition requisition = get(id);
        if (requisition.getEditStatus() == RequisitionEditStatus.LOCKED) {
            throw new IllegalStateException("Requisition is already locked");
        }
        int versionNumber = requisition.getCurrentVersion() + 1;
        Instant now = Instant.now();
        RecruitmentRequisitionVersion version = new RecruitmentRequisitionVersion()
            .versionNumber(versionNumber)
            .snapshotJson(snapshot(requisition))
            .lockedAt(now)
            .lockedBy(actor)
            .requisition(requisition);
        versions.save(version);
        editEvents.save(
            new RequisitionEditEvent()
                .eventType(versionNumber == 1 ? "LOCK" : "RELOCK")
                .actor(actor)
                .occurredAt(now)
                .versionNumber(versionNumber)
                .reason(requisition.getUnlockReason())
                .requisition(requisition)
        );
        requisition.setCurrentVersion(versionNumber);
        requisition.setEditStatus(RequisitionEditStatus.LOCKED);
        requisition.setLockedAt(now);
        requisition.setLockedBy(actor);
        requisition.setUnlockedAt(null);
        requisition.setUnlockReason(null);
        if (requisition.getLifecycle() == RequisitionLifecycle.PREPARING) {
            requisition.setLifecycle(RequisitionLifecycle.ACTIVE);
        }
        return requisitions.save(requisition);
    }

    public RecruitmentRequisition unlock(Long id, String actor, String reason) {
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Unlock reason is required");
        RecruitmentRequisition requisition = get(id);
        if (requisition.getEditStatus() != RequisitionEditStatus.LOCKED) {
            throw new IllegalStateException("Only a locked requisition can be reopened");
        }
        requisition.setEditStatus(RequisitionEditStatus.EDITABLE);
        requisition.setUnlockedAt(Instant.now());
        requisition.setUnlockReason("" + actor + ": " + reason.trim());
        editEvents.save(
            new RequisitionEditEvent()
                .eventType("UNLOCK")
                .actor(actor)
                .occurredAt(requisition.getUnlockedAt())
                .versionNumber(requisition.getCurrentVersion())
                .reason(reason.trim())
                .requisition(requisition)
        );
        return requisitions.save(requisition);
    }

    @Transactional(readOnly = true)
    public java.util.List<RequisitionEditEvent> history(Long id) {
        get(id);
        return editEvents.findAllByRequisition_IdOrderByOccurredAtDesc(id);
    }

    private RecruitmentRequisition get(Long id) {
        return requisitions.findById(id).orElseThrow(() -> new NoSuchElementException("Requisition not found: " + id));
    }

    private String snapshot(RecruitmentRequisition r) {
        // The source fields are stored separately so they remain queryable; this is a stable snapshot payload.
        return (
            "{\"requisitionCode\":\"" +
            escape(r.getRequisitionCode()) +
            "\",\"title\":\"" +
            escape(r.getTitle()) +
            "\",\"department\":\"" +
            escape(r.getDepartment()) +
            "\",\"recruitmentType\":\"" +
            r.getRecruitmentType() +
            "\",\"plannedHeadcount\":" +
            r.getPlannedHeadcount() +
            ",\"requirements\":\"" +
            escape(r.getRequirementsJson()) +
            "\"}"
        );
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
