package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.DispatchAssignment} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.DispatchAssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dispatch-assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchAssignmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter candidateId;

    private LongFilter personId;

    private LongFilter partnerOrganizationId;

    private StringFilter department;

    private LongFilter positionId;

    private LocalDateFilter startDate;

    private LocalDateFilter expectedEndDate;

    private IntegerFilter observationDays;

    private StringFilter status;

    private StringFilter finalDecision;

    private LongFilter reviewsId;

    private LongFilter applicationId;

    private Boolean distinct;

    public DispatchAssignmentCriteria() {}

    public DispatchAssignmentCriteria(DispatchAssignmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.candidateId = other.optionalCandidateId().map(LongFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.partnerOrganizationId = other.optionalPartnerOrganizationId().map(LongFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.expectedEndDate = other.optionalExpectedEndDate().map(LocalDateFilter::copy).orElse(null);
        this.observationDays = other.optionalObservationDays().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.finalDecision = other.optionalFinalDecision().map(StringFilter::copy).orElse(null);
        this.reviewsId = other.optionalReviewsId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DispatchAssignmentCriteria copy() {
        return new DispatchAssignmentCriteria(this);
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

    public LongFilter getCandidateId() {
        return candidateId;
    }

    public Optional<LongFilter> optionalCandidateId() {
        return Optional.ofNullable(candidateId);
    }

    public LongFilter candidateId() {
        if (candidateId == null) {
            setCandidateId(new LongFilter());
        }
        return candidateId;
    }

    public void setCandidateId(LongFilter candidateId) {
        this.candidateId = candidateId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public Optional<LongFilter> optionalPersonId() {
        return Optional.ofNullable(personId);
    }

    public LongFilter personId() {
        if (personId == null) {
            setPersonId(new LongFilter());
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getPartnerOrganizationId() {
        return partnerOrganizationId;
    }

    public Optional<LongFilter> optionalPartnerOrganizationId() {
        return Optional.ofNullable(partnerOrganizationId);
    }

    public LongFilter partnerOrganizationId() {
        if (partnerOrganizationId == null) {
            setPartnerOrganizationId(new LongFilter());
        }
        return partnerOrganizationId;
    }

    public void setPartnerOrganizationId(LongFilter partnerOrganizationId) {
        this.partnerOrganizationId = partnerOrganizationId;
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getExpectedEndDate() {
        return expectedEndDate;
    }

    public Optional<LocalDateFilter> optionalExpectedEndDate() {
        return Optional.ofNullable(expectedEndDate);
    }

    public LocalDateFilter expectedEndDate() {
        if (expectedEndDate == null) {
            setExpectedEndDate(new LocalDateFilter());
        }
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDateFilter expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public IntegerFilter getObservationDays() {
        return observationDays;
    }

    public Optional<IntegerFilter> optionalObservationDays() {
        return Optional.ofNullable(observationDays);
    }

    public IntegerFilter observationDays() {
        if (observationDays == null) {
            setObservationDays(new IntegerFilter());
        }
        return observationDays;
    }

    public void setObservationDays(IntegerFilter observationDays) {
        this.observationDays = observationDays;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getFinalDecision() {
        return finalDecision;
    }

    public Optional<StringFilter> optionalFinalDecision() {
        return Optional.ofNullable(finalDecision);
    }

    public StringFilter finalDecision() {
        if (finalDecision == null) {
            setFinalDecision(new StringFilter());
        }
        return finalDecision;
    }

    public void setFinalDecision(StringFilter finalDecision) {
        this.finalDecision = finalDecision;
    }

    public LongFilter getReviewsId() {
        return reviewsId;
    }

    public Optional<LongFilter> optionalReviewsId() {
        return Optional.ofNullable(reviewsId);
    }

    public LongFilter reviewsId() {
        if (reviewsId == null) {
            setReviewsId(new LongFilter());
        }
        return reviewsId;
    }

    public void setReviewsId(LongFilter reviewsId) {
        this.reviewsId = reviewsId;
    }

    public LongFilter getApplicationId() {
        return applicationId;
    }

    public Optional<LongFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public LongFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new LongFilter());
        }
        return applicationId;
    }

    public void setApplicationId(LongFilter applicationId) {
        this.applicationId = applicationId;
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
        final DispatchAssignmentCriteria that = (DispatchAssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(partnerOrganizationId, that.partnerOrganizationId) &&
            Objects.equals(department, that.department) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(expectedEndDate, that.expectedEndDate) &&
            Objects.equals(observationDays, that.observationDays) &&
            Objects.equals(status, that.status) &&
            Objects.equals(finalDecision, that.finalDecision) &&
            Objects.equals(reviewsId, that.reviewsId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            candidateId,
            personId,
            partnerOrganizationId,
            department,
            positionId,
            startDate,
            expectedEndDate,
            observationDays,
            status,
            finalDecision,
            reviewsId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchAssignmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCandidateId().map(f -> "candidateId=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPartnerOrganizationId().map(f -> "partnerOrganizationId=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalExpectedEndDate().map(f -> "expectedEndDate=" + f + ", ").orElse("") +
            optionalObservationDays().map(f -> "observationDays=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalFinalDecision().map(f -> "finalDecision=" + f + ", ").orElse("") +
            optionalReviewsId().map(f -> "reviewsId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
