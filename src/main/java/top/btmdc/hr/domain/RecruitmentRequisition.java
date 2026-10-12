package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;

/**
 * A RecruitmentRequisition.
 */
@Entity
@Table(name = "recruitment_requisition")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "requisition_code", length = 40, nullable = false, unique = true)
    private String requisitionCode;

    @NotNull
    @Size(max = 150)
    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)
    @Column(name = "department", length = 100, nullable = false)
    private String department;

    @Column(name = "position_id")
    private Long positionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_type", nullable = false)
    private RecruitmentType recruitmentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle", nullable = false)
    private RequisitionLifecycle lifecycle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "edit_status", nullable = false)
    private RequisitionEditStatus editStatus;

    @NotNull
    @Min(value = 1)
    @Column(name = "planned_headcount", nullable = false)
    private Integer plannedHeadcount;

    @NotNull
    @Min(value = 0)
    @Column(name = "hired_count", nullable = false)
    private Integer hiredCount;

    @NotNull
    @Min(value = 0)
    @Column(name = "checked_in_count", nullable = false)
    private Integer checkedInCount;

    @Size(max = 30)
    @Column(name = "priority", length = 30)
    private String priority;

    @Lob
    @Column(name = "reason")
    private String reason;

    @Column(name = "target_join_date")
    private LocalDate targetJoinDate;

    @Column(name = "desired_start_date")
    private LocalDate desiredStartDate;

    @Column(name = "desired_end_date")
    private LocalDate desiredEndDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "current_version", nullable = false)
    private Integer currentVersion;

    @Column(name = "locked_at")
    private Instant lockedAt;

    @Size(max = 100)
    @Column(name = "locked_by", length = 100)
    private String lockedBy;

    @Column(name = "unlocked_at")
    private Instant unlockedAt;

    @Lob
    @Column(name = "unlock_reason")
    private String unlockReason;

    @Lob
    @Column(name = "requirements_json")
    private String requirementsJson;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requisition")
    @JsonIgnoreProperties(value = { "applicationses", "requisition" }, allowSetters = true)
    private Set<RecruitmentRequisitionVersion> versionses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requisition")
    @JsonIgnoreProperties(value = { "requisition" }, allowSetters = true)
    private Set<RecruitmentRequisitionMember> memberses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requisition")
    @JsonIgnoreProperties(
        value = {
            "stageses",
            "eventses",
            "activitieses",
            "evaluationses",
            "decisionses",
            "messageses",
            "followUpses",
            "dispatchAssignmentses",
            "candidate",
            "requisition",
            "requisitionVersion",
        },
        allowSetters = true
    )
    private Set<RecruitmentApplication> applicationses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionCode() {
        return this.requisitionCode;
    }

    public RecruitmentRequisition requisitionCode(String requisitionCode) {
        this.setRequisitionCode(requisitionCode);
        return this;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public String getTitle() {
        return this.title;
    }

    public RecruitmentRequisition title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return this.department;
    }

    public RecruitmentRequisition department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getPositionId() {
        return this.positionId;
    }

    public RecruitmentRequisition positionId(Long positionId) {
        this.setPositionId(positionId);
        return this;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public RecruitmentType getRecruitmentType() {
        return this.recruitmentType;
    }

    public RecruitmentRequisition recruitmentType(RecruitmentType recruitmentType) {
        this.setRecruitmentType(recruitmentType);
        return this;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public RequisitionLifecycle getLifecycle() {
        return this.lifecycle;
    }

    public RecruitmentRequisition lifecycle(RequisitionLifecycle lifecycle) {
        this.setLifecycle(lifecycle);
        return this;
    }

    public void setLifecycle(RequisitionLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public RequisitionEditStatus getEditStatus() {
        return this.editStatus;
    }

    public RecruitmentRequisition editStatus(RequisitionEditStatus editStatus) {
        this.setEditStatus(editStatus);
        return this;
    }

    public void setEditStatus(RequisitionEditStatus editStatus) {
        this.editStatus = editStatus;
    }

    public Integer getPlannedHeadcount() {
        return this.plannedHeadcount;
    }

    public RecruitmentRequisition plannedHeadcount(Integer plannedHeadcount) {
        this.setPlannedHeadcount(plannedHeadcount);
        return this;
    }

    public void setPlannedHeadcount(Integer plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public Integer getHiredCount() {
        return this.hiredCount;
    }

    public RecruitmentRequisition hiredCount(Integer hiredCount) {
        this.setHiredCount(hiredCount);
        return this;
    }

    public void setHiredCount(Integer hiredCount) {
        this.hiredCount = hiredCount;
    }

    public Integer getCheckedInCount() {
        return this.checkedInCount;
    }

    public RecruitmentRequisition checkedInCount(Integer checkedInCount) {
        this.setCheckedInCount(checkedInCount);
        return this;
    }

    public void setCheckedInCount(Integer checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public String getPriority() {
        return this.priority;
    }

    public RecruitmentRequisition priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return this.reason;
    }

    public RecruitmentRequisition reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getTargetJoinDate() {
        return this.targetJoinDate;
    }

    public RecruitmentRequisition targetJoinDate(LocalDate targetJoinDate) {
        this.setTargetJoinDate(targetJoinDate);
        return this;
    }

    public void setTargetJoinDate(LocalDate targetJoinDate) {
        this.targetJoinDate = targetJoinDate;
    }

    public LocalDate getDesiredStartDate() {
        return this.desiredStartDate;
    }

    public RecruitmentRequisition desiredStartDate(LocalDate desiredStartDate) {
        this.setDesiredStartDate(desiredStartDate);
        return this;
    }

    public void setDesiredStartDate(LocalDate desiredStartDate) {
        this.desiredStartDate = desiredStartDate;
    }

    public LocalDate getDesiredEndDate() {
        return this.desiredEndDate;
    }

    public RecruitmentRequisition desiredEndDate(LocalDate desiredEndDate) {
        this.setDesiredEndDate(desiredEndDate);
        return this;
    }

    public void setDesiredEndDate(LocalDate desiredEndDate) {
        this.desiredEndDate = desiredEndDate;
    }

    public Integer getCurrentVersion() {
        return this.currentVersion;
    }

    public RecruitmentRequisition currentVersion(Integer currentVersion) {
        this.setCurrentVersion(currentVersion);
        return this;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Instant getLockedAt() {
        return this.lockedAt;
    }

    public RecruitmentRequisition lockedAt(Instant lockedAt) {
        this.setLockedAt(lockedAt);
        return this;
    }

    public void setLockedAt(Instant lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return this.lockedBy;
    }

    public RecruitmentRequisition lockedBy(String lockedBy) {
        this.setLockedBy(lockedBy);
        return this;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Instant getUnlockedAt() {
        return this.unlockedAt;
    }

    public RecruitmentRequisition unlockedAt(Instant unlockedAt) {
        this.setUnlockedAt(unlockedAt);
        return this;
    }

    public void setUnlockedAt(Instant unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public String getUnlockReason() {
        return this.unlockReason;
    }

    public RecruitmentRequisition unlockReason(String unlockReason) {
        this.setUnlockReason(unlockReason);
        return this;
    }

    public void setUnlockReason(String unlockReason) {
        this.unlockReason = unlockReason;
    }

    public String getRequirementsJson() {
        return this.requirementsJson;
    }

    public RecruitmentRequisition requirementsJson(String requirementsJson) {
        this.setRequirementsJson(requirementsJson);
        return this;
    }

    public void setRequirementsJson(String requirementsJson) {
        this.requirementsJson = requirementsJson;
    }

    public Set<RecruitmentRequisitionVersion> getVersionses() {
        return this.versionses;
    }

    public void setVersionses(Set<RecruitmentRequisitionVersion> recruitmentRequisitionVersions) {
        if (this.versionses != null) {
            this.versionses.forEach(i -> i.setRequisition(null));
        }
        if (recruitmentRequisitionVersions != null) {
            recruitmentRequisitionVersions.forEach(i -> i.setRequisition(this));
        }
        this.versionses = recruitmentRequisitionVersions;
    }

    public RecruitmentRequisition versionses(Set<RecruitmentRequisitionVersion> recruitmentRequisitionVersions) {
        this.setVersionses(recruitmentRequisitionVersions);
        return this;
    }

    public RecruitmentRequisition addVersions(RecruitmentRequisitionVersion recruitmentRequisitionVersion) {
        this.versionses.add(recruitmentRequisitionVersion);
        recruitmentRequisitionVersion.setRequisition(this);
        return this;
    }

    public RecruitmentRequisition removeVersions(RecruitmentRequisitionVersion recruitmentRequisitionVersion) {
        this.versionses.remove(recruitmentRequisitionVersion);
        recruitmentRequisitionVersion.setRequisition(null);
        return this;
    }

    public Set<RecruitmentRequisitionMember> getMemberses() {
        return this.memberses;
    }

    public void setMemberses(Set<RecruitmentRequisitionMember> recruitmentRequisitionMembers) {
        if (this.memberses != null) {
            this.memberses.forEach(i -> i.setRequisition(null));
        }
        if (recruitmentRequisitionMembers != null) {
            recruitmentRequisitionMembers.forEach(i -> i.setRequisition(this));
        }
        this.memberses = recruitmentRequisitionMembers;
    }

    public RecruitmentRequisition memberses(Set<RecruitmentRequisitionMember> recruitmentRequisitionMembers) {
        this.setMemberses(recruitmentRequisitionMembers);
        return this;
    }

    public RecruitmentRequisition addMembers(RecruitmentRequisitionMember recruitmentRequisitionMember) {
        this.memberses.add(recruitmentRequisitionMember);
        recruitmentRequisitionMember.setRequisition(this);
        return this;
    }

    public RecruitmentRequisition removeMembers(RecruitmentRequisitionMember recruitmentRequisitionMember) {
        this.memberses.remove(recruitmentRequisitionMember);
        recruitmentRequisitionMember.setRequisition(null);
        return this;
    }

    public Set<RecruitmentApplication> getApplicationses() {
        return this.applicationses;
    }

    public void setApplicationses(Set<RecruitmentApplication> recruitmentApplications) {
        if (this.applicationses != null) {
            this.applicationses.forEach(i -> i.setRequisition(null));
        }
        if (recruitmentApplications != null) {
            recruitmentApplications.forEach(i -> i.setRequisition(this));
        }
        this.applicationses = recruitmentApplications;
    }

    public RecruitmentRequisition applicationses(Set<RecruitmentApplication> recruitmentApplications) {
        this.setApplicationses(recruitmentApplications);
        return this;
    }

    public RecruitmentRequisition addApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.add(recruitmentApplication);
        recruitmentApplication.setRequisition(this);
        return this;
    }

    public RecruitmentRequisition removeApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.remove(recruitmentApplication);
        recruitmentApplication.setRequisition(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisition)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentRequisition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisition{" +
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
