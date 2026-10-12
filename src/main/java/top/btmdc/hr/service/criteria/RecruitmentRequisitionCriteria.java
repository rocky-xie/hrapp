package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.RecruitmentRequisition} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.RecruitmentRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recruitment-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RecruitmentType
     */
    public static class RecruitmentTypeFilter extends Filter<RecruitmentType> {

        public RecruitmentTypeFilter() {}

        public RecruitmentTypeFilter(RecruitmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public RecruitmentTypeFilter copy() {
            return new RecruitmentTypeFilter(this);
        }
    }

    /**
     * Class for filtering RequisitionLifecycle
     */
    public static class RequisitionLifecycleFilter extends Filter<RequisitionLifecycle> {

        public RequisitionLifecycleFilter() {}

        public RequisitionLifecycleFilter(RequisitionLifecycleFilter filter) {
            super(filter);
        }

        @Override
        public RequisitionLifecycleFilter copy() {
            return new RequisitionLifecycleFilter(this);
        }
    }

    /**
     * Class for filtering RequisitionEditStatus
     */
    public static class RequisitionEditStatusFilter extends Filter<RequisitionEditStatus> {

        public RequisitionEditStatusFilter() {}

        public RequisitionEditStatusFilter(RequisitionEditStatusFilter filter) {
            super(filter);
        }

        @Override
        public RequisitionEditStatusFilter copy() {
            return new RequisitionEditStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter requisitionCode;

    private StringFilter title;

    private StringFilter department;

    private LongFilter positionId;

    private RecruitmentTypeFilter recruitmentType;

    private RequisitionLifecycleFilter lifecycle;

    private RequisitionEditStatusFilter editStatus;

    private IntegerFilter plannedHeadcount;

    private IntegerFilter hiredCount;

    private IntegerFilter checkedInCount;

    private StringFilter priority;

    private LocalDateFilter targetJoinDate;

    private LocalDateFilter desiredStartDate;

    private LocalDateFilter desiredEndDate;

    private IntegerFilter currentVersion;

    private InstantFilter lockedAt;

    private StringFilter lockedBy;

    private InstantFilter unlockedAt;

    private LongFilter versionsId;

    private LongFilter membersId;

    private LongFilter applicationsId;

    private Boolean distinct;

    public RecruitmentRequisitionCriteria() {}

    public RecruitmentRequisitionCriteria(RecruitmentRequisitionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.requisitionCode = other.optionalRequisitionCode().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.recruitmentType = other.optionalRecruitmentType().map(RecruitmentTypeFilter::copy).orElse(null);
        this.lifecycle = other.optionalLifecycle().map(RequisitionLifecycleFilter::copy).orElse(null);
        this.editStatus = other.optionalEditStatus().map(RequisitionEditStatusFilter::copy).orElse(null);
        this.plannedHeadcount = other.optionalPlannedHeadcount().map(IntegerFilter::copy).orElse(null);
        this.hiredCount = other.optionalHiredCount().map(IntegerFilter::copy).orElse(null);
        this.checkedInCount = other.optionalCheckedInCount().map(IntegerFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(StringFilter::copy).orElse(null);
        this.targetJoinDate = other.optionalTargetJoinDate().map(LocalDateFilter::copy).orElse(null);
        this.desiredStartDate = other.optionalDesiredStartDate().map(LocalDateFilter::copy).orElse(null);
        this.desiredEndDate = other.optionalDesiredEndDate().map(LocalDateFilter::copy).orElse(null);
        this.currentVersion = other.optionalCurrentVersion().map(IntegerFilter::copy).orElse(null);
        this.lockedAt = other.optionalLockedAt().map(InstantFilter::copy).orElse(null);
        this.lockedBy = other.optionalLockedBy().map(StringFilter::copy).orElse(null);
        this.unlockedAt = other.optionalUnlockedAt().map(InstantFilter::copy).orElse(null);
        this.versionsId = other.optionalVersionsId().map(LongFilter::copy).orElse(null);
        this.membersId = other.optionalMembersId().map(LongFilter::copy).orElse(null);
        this.applicationsId = other.optionalApplicationsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecruitmentRequisitionCriteria copy() {
        return new RecruitmentRequisitionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRequisitionCode() {
        return requisitionCode;
    }

    public Optional<StringFilter> optionalRequisitionCode() {
        return Optional.ofNullable(requisitionCode);
    }

    public StringFilter requisitionCode() {
        if (requisitionCode == null) {
            setRequisitionCode(new StringFilter());
        }
        return requisitionCode;
    }

    public void setRequisitionCode(StringFilter requisitionCode) {
        this.requisitionCode = requisitionCode;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public Optional<LongFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public LongFilter positionId() {
        if (positionId == null) {
            setPositionId(new LongFilter());
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public RecruitmentTypeFilter getRecruitmentType() {
        return recruitmentType;
    }

    public Optional<RecruitmentTypeFilter> optionalRecruitmentType() {
        return Optional.ofNullable(recruitmentType);
    }

    public RecruitmentTypeFilter recruitmentType() {
        if (recruitmentType == null) {
            setRecruitmentType(new RecruitmentTypeFilter());
        }
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentTypeFilter recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public RequisitionLifecycleFilter getLifecycle() {
        return lifecycle;
    }

    public Optional<RequisitionLifecycleFilter> optionalLifecycle() {
        return Optional.ofNullable(lifecycle);
    }

    public RequisitionLifecycleFilter lifecycle() {
        if (lifecycle == null) {
            setLifecycle(new RequisitionLifecycleFilter());
        }
        return lifecycle;
    }

    public void setLifecycle(RequisitionLifecycleFilter lifecycle) {
        this.lifecycle = lifecycle;
    }

    public RequisitionEditStatusFilter getEditStatus() {
        return editStatus;
    }

    public Optional<RequisitionEditStatusFilter> optionalEditStatus() {
        return Optional.ofNullable(editStatus);
    }

    public RequisitionEditStatusFilter editStatus() {
        if (editStatus == null) {
            setEditStatus(new RequisitionEditStatusFilter());
        }
        return editStatus;
    }

    public void setEditStatus(RequisitionEditStatusFilter editStatus) {
        this.editStatus = editStatus;
    }

    public IntegerFilter getPlannedHeadcount() {
        return plannedHeadcount;
    }

    public Optional<IntegerFilter> optionalPlannedHeadcount() {
        return Optional.ofNullable(plannedHeadcount);
    }

    public IntegerFilter plannedHeadcount() {
        if (plannedHeadcount == null) {
            setPlannedHeadcount(new IntegerFilter());
        }
        return plannedHeadcount;
    }

    public void setPlannedHeadcount(IntegerFilter plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public IntegerFilter getHiredCount() {
        return hiredCount;
    }

    public Optional<IntegerFilter> optionalHiredCount() {
        return Optional.ofNullable(hiredCount);
    }

    public IntegerFilter hiredCount() {
        if (hiredCount == null) {
            setHiredCount(new IntegerFilter());
        }
        return hiredCount;
    }

    public void setHiredCount(IntegerFilter hiredCount) {
        this.hiredCount = hiredCount;
    }

    public IntegerFilter getCheckedInCount() {
        return checkedInCount;
    }

    public Optional<IntegerFilter> optionalCheckedInCount() {
        return Optional.ofNullable(checkedInCount);
    }

    public IntegerFilter checkedInCount() {
        if (checkedInCount == null) {
            setCheckedInCount(new IntegerFilter());
        }
        return checkedInCount;
    }

    public void setCheckedInCount(IntegerFilter checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public StringFilter getPriority() {
        return priority;
    }

    public Optional<StringFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public StringFilter priority() {
        if (priority == null) {
            setPriority(new StringFilter());
        }
        return priority;
    }

    public void setPriority(StringFilter priority) {
        this.priority = priority;
    }

    public LocalDateFilter getTargetJoinDate() {
        return targetJoinDate;
    }

    public Optional<LocalDateFilter> optionalTargetJoinDate() {
        return Optional.ofNullable(targetJoinDate);
    }

    public LocalDateFilter targetJoinDate() {
        if (targetJoinDate == null) {
            setTargetJoinDate(new LocalDateFilter());
        }
        return targetJoinDate;
    }

    public void setTargetJoinDate(LocalDateFilter targetJoinDate) {
        this.targetJoinDate = targetJoinDate;
    }

    public LocalDateFilter getDesiredStartDate() {
        return desiredStartDate;
    }

    public Optional<LocalDateFilter> optionalDesiredStartDate() {
        return Optional.ofNullable(desiredStartDate);
    }

    public LocalDateFilter desiredStartDate() {
        if (desiredStartDate == null) {
            setDesiredStartDate(new LocalDateFilter());
        }
        return desiredStartDate;
    }

    public void setDesiredStartDate(LocalDateFilter desiredStartDate) {
        this.desiredStartDate = desiredStartDate;
    }

    public LocalDateFilter getDesiredEndDate() {
        return desiredEndDate;
    }

    public Optional<LocalDateFilter> optionalDesiredEndDate() {
        return Optional.ofNullable(desiredEndDate);
    }

    public LocalDateFilter desiredEndDate() {
        if (desiredEndDate == null) {
            setDesiredEndDate(new LocalDateFilter());
        }
        return desiredEndDate;
    }

    public void setDesiredEndDate(LocalDateFilter desiredEndDate) {
        this.desiredEndDate = desiredEndDate;
    }

    public IntegerFilter getCurrentVersion() {
        return currentVersion;
    }

    public Optional<IntegerFilter> optionalCurrentVersion() {
        return Optional.ofNullable(currentVersion);
    }

    public IntegerFilter currentVersion() {
        if (currentVersion == null) {
            setCurrentVersion(new IntegerFilter());
        }
        return currentVersion;
    }

    public void setCurrentVersion(IntegerFilter currentVersion) {
        this.currentVersion = currentVersion;
    }

    public InstantFilter getLockedAt() {
        return lockedAt;
    }

    public Optional<InstantFilter> optionalLockedAt() {
        return Optional.ofNullable(lockedAt);
    }

    public InstantFilter lockedAt() {
        if (lockedAt == null) {
            setLockedAt(new InstantFilter());
        }
        return lockedAt;
    }

    public void setLockedAt(InstantFilter lockedAt) {
        this.lockedAt = lockedAt;
    }

    public StringFilter getLockedBy() {
        return lockedBy;
    }

    public Optional<StringFilter> optionalLockedBy() {
        return Optional.ofNullable(lockedBy);
    }

    public StringFilter lockedBy() {
        if (lockedBy == null) {
            setLockedBy(new StringFilter());
        }
        return lockedBy;
    }

    public void setLockedBy(StringFilter lockedBy) {
        this.lockedBy = lockedBy;
    }

    public InstantFilter getUnlockedAt() {
        return unlockedAt;
    }

    public Optional<InstantFilter> optionalUnlockedAt() {
        return Optional.ofNullable(unlockedAt);
    }

    public InstantFilter unlockedAt() {
        if (unlockedAt == null) {
            setUnlockedAt(new InstantFilter());
        }
        return unlockedAt;
    }

    public void setUnlockedAt(InstantFilter unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public LongFilter getVersionsId() {
        return versionsId;
    }

    public Optional<LongFilter> optionalVersionsId() {
        return Optional.ofNullable(versionsId);
    }

    public LongFilter versionsId() {
        if (versionsId == null) {
            setVersionsId(new LongFilter());
        }
        return versionsId;
    }

    public void setVersionsId(LongFilter versionsId) {
        this.versionsId = versionsId;
    }

    public LongFilter getMembersId() {
        return membersId;
    }

    public Optional<LongFilter> optionalMembersId() {
        return Optional.ofNullable(membersId);
    }

    public LongFilter membersId() {
        if (membersId == null) {
            setMembersId(new LongFilter());
        }
        return membersId;
    }

    public void setMembersId(LongFilter membersId) {
        this.membersId = membersId;
    }

    public LongFilter getApplicationsId() {
        return applicationsId;
    }

    public Optional<LongFilter> optionalApplicationsId() {
        return Optional.ofNullable(applicationsId);
    }

    public LongFilter applicationsId() {
        if (applicationsId == null) {
            setApplicationsId(new LongFilter());
        }
        return applicationsId;
    }

    public void setApplicationsId(LongFilter applicationsId) {
        this.applicationsId = applicationsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecruitmentRequisitionCriteria that = (RecruitmentRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requisitionCode, that.requisitionCode) &&
            Objects.equals(title, that.title) &&
            Objects.equals(department, that.department) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(recruitmentType, that.recruitmentType) &&
            Objects.equals(lifecycle, that.lifecycle) &&
            Objects.equals(editStatus, that.editStatus) &&
            Objects.equals(plannedHeadcount, that.plannedHeadcount) &&
            Objects.equals(hiredCount, that.hiredCount) &&
            Objects.equals(checkedInCount, that.checkedInCount) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(targetJoinDate, that.targetJoinDate) &&
            Objects.equals(desiredStartDate, that.desiredStartDate) &&
            Objects.equals(desiredEndDate, that.desiredEndDate) &&
            Objects.equals(currentVersion, that.currentVersion) &&
            Objects.equals(lockedAt, that.lockedAt) &&
            Objects.equals(lockedBy, that.lockedBy) &&
            Objects.equals(unlockedAt, that.unlockedAt) &&
            Objects.equals(versionsId, that.versionsId) &&
            Objects.equals(membersId, that.membersId) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            requisitionCode,
            title,
            department,
            positionId,
            recruitmentType,
            lifecycle,
            editStatus,
            plannedHeadcount,
            hiredCount,
            checkedInCount,
            priority,
            targetJoinDate,
            desiredStartDate,
            desiredEndDate,
            currentVersion,
            lockedAt,
            lockedBy,
            unlockedAt,
            versionsId,
            membersId,
            applicationsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRequisitionCode().map(f -> "requisitionCode=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalRecruitmentType().map(f -> "recruitmentType=" + f + ", ").orElse("") +
            optionalLifecycle().map(f -> "lifecycle=" + f + ", ").orElse("") +
            optionalEditStatus().map(f -> "editStatus=" + f + ", ").orElse("") +
            optionalPlannedHeadcount().map(f -> "plannedHeadcount=" + f + ", ").orElse("") +
            optionalHiredCount().map(f -> "hiredCount=" + f + ", ").orElse("") +
            optionalCheckedInCount().map(f -> "checkedInCount=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalTargetJoinDate().map(f -> "targetJoinDate=" + f + ", ").orElse("") +
            optionalDesiredStartDate().map(f -> "desiredStartDate=" + f + ", ").orElse("") +
            optionalDesiredEndDate().map(f -> "desiredEndDate=" + f + ", ").orElse("") +
            optionalCurrentVersion().map(f -> "currentVersion=" + f + ", ").orElse("") +
            optionalLockedAt().map(f -> "lockedAt=" + f + ", ").orElse("") +
            optionalLockedBy().map(f -> "lockedBy=" + f + ", ").orElse("") +
            optionalUnlockedAt().map(f -> "unlockedAt=" + f + ", ").orElse("") +
            optionalVersionsId().map(f -> "versionsId=" + f + ", ").orElse("") +
            optionalMembersId().map(f -> "membersId=" + f + ", ").orElse("") +
            optionalApplicationsId().map(f -> "applicationsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
