package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.DispatchReview} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.DispatchReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dispatch-reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchReviewCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter reviewedAt;

    private StringFilter reviewerUsername;

    private StringFilter recommendation;

    private LocalDateFilter extensionEndDate;

    private StringFilter finalDecision;

    private LongFilter dispatchAssignmentId;

    private Boolean distinct;

    public DispatchReviewCriteria() {}

    public DispatchReviewCriteria(DispatchReviewCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.reviewedAt = other.optionalReviewedAt().map(InstantFilter::copy).orElse(null);
        this.reviewerUsername = other.optionalReviewerUsername().map(StringFilter::copy).orElse(null);
        this.recommendation = other.optionalRecommendation().map(StringFilter::copy).orElse(null);
        this.extensionEndDate = other.optionalExtensionEndDate().map(LocalDateFilter::copy).orElse(null);
        this.finalDecision = other.optionalFinalDecision().map(StringFilter::copy).orElse(null);
        this.dispatchAssignmentId = other.optionalDispatchAssignmentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DispatchReviewCriteria copy() {
        return new DispatchReviewCriteria(this);
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

    public InstantFilter getReviewedAt() {
        return reviewedAt;
    }

    public Optional<InstantFilter> optionalReviewedAt() {
        return Optional.ofNullable(reviewedAt);
    }

    public InstantFilter reviewedAt() {
        if (reviewedAt == null) {
            setReviewedAt(new InstantFilter());
        }
        return reviewedAt;
    }

    public void setReviewedAt(InstantFilter reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public StringFilter getReviewerUsername() {
        return reviewerUsername;
    }

    public Optional<StringFilter> optionalReviewerUsername() {
        return Optional.ofNullable(reviewerUsername);
    }

    public StringFilter reviewerUsername() {
        if (reviewerUsername == null) {
            setReviewerUsername(new StringFilter());
        }
        return reviewerUsername;
    }

    public void setReviewerUsername(StringFilter reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public StringFilter getRecommendation() {
        return recommendation;
    }

    public Optional<StringFilter> optionalRecommendation() {
        return Optional.ofNullable(recommendation);
    }

    public StringFilter recommendation() {
        if (recommendation == null) {
            setRecommendation(new StringFilter());
        }
        return recommendation;
    }

    public void setRecommendation(StringFilter recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateFilter getExtensionEndDate() {
        return extensionEndDate;
    }

    public Optional<LocalDateFilter> optionalExtensionEndDate() {
        return Optional.ofNullable(extensionEndDate);
    }

    public LocalDateFilter extensionEndDate() {
        if (extensionEndDate == null) {
            setExtensionEndDate(new LocalDateFilter());
        }
        return extensionEndDate;
    }

    public void setExtensionEndDate(LocalDateFilter extensionEndDate) {
        this.extensionEndDate = extensionEndDate;
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

    public LongFilter getDispatchAssignmentId() {
        return dispatchAssignmentId;
    }

    public Optional<LongFilter> optionalDispatchAssignmentId() {
        return Optional.ofNullable(dispatchAssignmentId);
    }

    public LongFilter dispatchAssignmentId() {
        if (dispatchAssignmentId == null) {
            setDispatchAssignmentId(new LongFilter());
        }
        return dispatchAssignmentId;
    }

    public void setDispatchAssignmentId(LongFilter dispatchAssignmentId) {
        this.dispatchAssignmentId = dispatchAssignmentId;
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
        final DispatchReviewCriteria that = (DispatchReviewCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reviewedAt, that.reviewedAt) &&
            Objects.equals(reviewerUsername, that.reviewerUsername) &&
            Objects.equals(recommendation, that.recommendation) &&
            Objects.equals(extensionEndDate, that.extensionEndDate) &&
            Objects.equals(finalDecision, that.finalDecision) &&
            Objects.equals(dispatchAssignmentId, that.dispatchAssignmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reviewedAt,
            reviewerUsername,
            recommendation,
            extensionEndDate,
            finalDecision,
            dispatchAssignmentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchReviewCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReviewedAt().map(f -> "reviewedAt=" + f + ", ").orElse("") +
            optionalReviewerUsername().map(f -> "reviewerUsername=" + f + ", ").orElse("") +
            optionalRecommendation().map(f -> "recommendation=" + f + ", ").orElse("") +
            optionalExtensionEndDate().map(f -> "extensionEndDate=" + f + ", ").orElse("") +
            optionalFinalDecision().map(f -> "finalDecision=" + f + ", ").orElse("") +
            optionalDispatchAssignmentId().map(f -> "dispatchAssignmentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
