package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.RecruitmentRequisitionVersion} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.RecruitmentRequisitionVersionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recruitment-requisition-versions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionVersionCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter versionNumber;

    private InstantFilter lockedAt;

    private StringFilter lockedBy;

    private LongFilter applicationsId;

    private LongFilter requisitionId;

    private Boolean distinct;

    public RecruitmentRequisitionVersionCriteria() {}

    public RecruitmentRequisitionVersionCriteria(RecruitmentRequisitionVersionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.versionNumber = other.optionalVersionNumber().map(IntegerFilter::copy).orElse(null);
        this.lockedAt = other.optionalLockedAt().map(InstantFilter::copy).orElse(null);
        this.lockedBy = other.optionalLockedBy().map(StringFilter::copy).orElse(null);
        this.applicationsId = other.optionalApplicationsId().map(LongFilter::copy).orElse(null);
        this.requisitionId = other.optionalRequisitionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecruitmentRequisitionVersionCriteria copy() {
        return new RecruitmentRequisitionVersionCriteria(this);
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

    public IntegerFilter getVersionNumber() {
        return versionNumber;
    }

    public Optional<IntegerFilter> optionalVersionNumber() {
        return Optional.ofNullable(versionNumber);
    }

    public IntegerFilter versionNumber() {
        if (versionNumber == null) {
            setVersionNumber(new IntegerFilter());
        }
        return versionNumber;
    }

    public void setVersionNumber(IntegerFilter versionNumber) {
        this.versionNumber = versionNumber;
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

    public LongFilter getRequisitionId() {
        return requisitionId;
    }

    public Optional<LongFilter> optionalRequisitionId() {
        return Optional.ofNullable(requisitionId);
    }

    public LongFilter requisitionId() {
        if (requisitionId == null) {
            setRequisitionId(new LongFilter());
        }
        return requisitionId;
    }

    public void setRequisitionId(LongFilter requisitionId) {
        this.requisitionId = requisitionId;
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
        final RecruitmentRequisitionVersionCriteria that = (RecruitmentRequisitionVersionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(versionNumber, that.versionNumber) &&
            Objects.equals(lockedAt, that.lockedAt) &&
            Objects.equals(lockedBy, that.lockedBy) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(requisitionId, that.requisitionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, versionNumber, lockedAt, lockedBy, applicationsId, requisitionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionVersionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVersionNumber().map(f -> "versionNumber=" + f + ", ").orElse("") +
            optionalLockedAt().map(f -> "lockedAt=" + f + ", ").orElse("") +
            optionalLockedBy().map(f -> "lockedBy=" + f + ", ").orElse("") +
            optionalApplicationsId().map(f -> "applicationsId=" + f + ", ").orElse("") +
            optionalRequisitionId().map(f -> "requisitionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
