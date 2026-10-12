package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;

/**
 * A DTO for the {@link top.btmdc.hr.domain.RecruitmentRequisition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String requisitionCode;

    @NotNull
    @Size(max = 150)
    private String title;

    @NotNull
    @Size(max = 100)
    private String department;

    private Long positionId;

    @NotNull
    private RecruitmentType recruitmentType;

    @NotNull
    private RequisitionLifecycle lifecycle;

    @NotNull
    private RequisitionEditStatus editStatus;

    @NotNull
    @Min(value = 1)
    private Integer plannedHeadcount;

    @NotNull
    @Min(value = 0)
    private Integer hiredCount;

    @NotNull
    @Min(value = 0)
    private Integer checkedInCount;

    @Size(max = 30)
    private String priority;

    @Lob
    private String reason;

    private LocalDate targetJoinDate;

    private LocalDate desiredStartDate;

    private LocalDate desiredEndDate;

    @NotNull
    @Min(value = 0)
    private Integer currentVersion;

    private Instant lockedAt;

    @Size(max = 100)
    private String lockedBy;

    private Instant unlockedAt;

    @Lob
    private String unlockReason;

    @Lob
    private String requirementsJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public RecruitmentType getRecruitmentType() {
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public RequisitionLifecycle getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(RequisitionLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public RequisitionEditStatus getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(RequisitionEditStatus editStatus) {
        this.editStatus = editStatus;
    }

    public Integer getPlannedHeadcount() {
        return plannedHeadcount;
    }

    public void setPlannedHeadcount(Integer plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public Integer getHiredCount() {
        return hiredCount;
    }

    public void setHiredCount(Integer hiredCount) {
        this.hiredCount = hiredCount;
    }

    public Integer getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(Integer checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getTargetJoinDate() {
        return targetJoinDate;
    }

    public void setTargetJoinDate(LocalDate targetJoinDate) {
        this.targetJoinDate = targetJoinDate;
    }

    public LocalDate getDesiredStartDate() {
        return desiredStartDate;
    }

    public void setDesiredStartDate(LocalDate desiredStartDate) {
        this.desiredStartDate = desiredStartDate;
    }

    public LocalDate getDesiredEndDate() {
        return desiredEndDate;
    }

    public void setDesiredEndDate(LocalDate desiredEndDate) {
        this.desiredEndDate = desiredEndDate;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Instant lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Instant getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(Instant unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public String getUnlockReason() {
        return unlockReason;
    }

    public void setUnlockReason(String unlockReason) {
        this.unlockReason = unlockReason;
    }

    public String getRequirementsJson() {
        return requirementsJson;
    }

    public void setRequirementsJson(String requirementsJson) {
        this.requirementsJson = requirementsJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionDTO)) {
            return false;
        }

        RecruitmentRequisitionDTO recruitmentRequisitionDTO = (RecruitmentRequisitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentRequisitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionDTO{" +
            "id=" + getId() +
            ", requisitionCode='" + getRequisitionCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", department='" + getDepartment() + "'" +
            ", positionId=" + getPositionId() +
            ", recruitmentType='" + getRecruitmentType() + "'" +
            ", lifecycle='" + getLifecycle() + "'" +
            ", editStatus='" + getEditStatus() + "'" +
            ", plannedHeadcount=" + getPlannedHeadcount() +
            ", hiredCount=" + getHiredCount() +
            ", checkedInCount=" + getCheckedInCount() +
            ", priority='" + getPriority() + "'" +
            ", reason='" + getReason() + "'" +
            ", targetJoinDate='" + getTargetJoinDate() + "'" +
            ", desiredStartDate='" + getDesiredStartDate() + "'" +
            ", desiredEndDate='" + getDesiredEndDate() + "'" +
            ", currentVersion=" + getCurrentVersion() +
            ", lockedAt='" + getLockedAt() + "'" +
            ", lockedBy='" + getLockedBy() + "'" +
            ", unlockedAt='" + getUnlockedAt() + "'" +
            ", unlockReason='" + getUnlockReason() + "'" +
            ", requirementsJson='" + getRequirementsJson() + "'" +
            "}";
    }
}
