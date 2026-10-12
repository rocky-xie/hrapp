package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.RecruitmentApplication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String applicationCode;

    @NotNull
    private RecruitmentType recruitmentType;

    private ApplicationStageType currentStage;

    @NotNull
    private ApplicationStatus status;

    private ApplicationDecision decision;

    @NotNull
    @Size(max = 40)
    private String sourceType;

    @Size(max = 255)
    private String sourceDetail;

    @Size(max = 150)
    private String batchName;

    @NotNull
    private Instant appliedAt;

    private Instant closedAt;

    private LocalDate retentionUntil;

    private Long convertedPersonId;

    private Instant convertedAt;

    @Lob
    private String closeReason;

    @Lob
    private String note;

    private ExternalCandidateDTO candidate;

    private RecruitmentRequisitionDTO requisition;

    private RecruitmentRequisitionVersionDTO requisitionVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public RecruitmentType getRecruitmentType() {
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public ApplicationStageType getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(ApplicationStageType currentStage) {
        this.currentStage = currentStage;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationDecision getDecision() {
        return decision;
    }

    public void setDecision(ApplicationDecision decision) {
        this.decision = decision;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceDetail() {
        return sourceDetail;
    }

    public void setSourceDetail(String sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDate getRetentionUntil() {
        return retentionUntil;
    }

    public void setRetentionUntil(LocalDate retentionUntil) {
        this.retentionUntil = retentionUntil;
    }

    public Long getConvertedPersonId() {
        return convertedPersonId;
    }

    public void setConvertedPersonId(Long convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
    }

    public Instant getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(Instant convertedAt) {
        this.convertedAt = convertedAt;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ExternalCandidateDTO getCandidate() {
        return candidate;
    }

    public void setCandidate(ExternalCandidateDTO candidate) {
        this.candidate = candidate;
    }

    public RecruitmentRequisitionDTO getRequisition() {
        return requisition;
    }

    public void setRequisition(RecruitmentRequisitionDTO requisition) {
        this.requisition = requisition;
    }

    public RecruitmentRequisitionVersionDTO getRequisitionVersion() {
        return requisitionVersion;
    }

    public void setRequisitionVersion(RecruitmentRequisitionVersionDTO requisitionVersion) {
        this.requisitionVersion = requisitionVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentApplicationDTO)) {
            return false;
        }

        RecruitmentApplicationDTO recruitmentApplicationDTO = (RecruitmentApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentApplicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentApplicationDTO{" +
            "id=" + getId() +
            ", applicationCode='" + getApplicationCode() + "'" +
            ", recruitmentType='" + getRecruitmentType() + "'" +
            ", currentStage='" + getCurrentStage() + "'" +
            ", status='" + getStatus() + "'" +
            ", decision='" + getDecision() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", sourceDetail='" + getSourceDetail() + "'" +
            ", batchName='" + getBatchName() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", retentionUntil='" + getRetentionUntil() + "'" +
            ", convertedPersonId=" + getConvertedPersonId() +
            ", convertedAt='" + getConvertedAt() + "'" +
            ", closeReason='" + getCloseReason() + "'" +
            ", note='" + getNote() + "'" +
            ", candidate=" + getCandidate() +
            ", requisition=" + getRequisition() +
            ", requisitionVersion=" + getRequisitionVersion() +
            "}";
    }
}
