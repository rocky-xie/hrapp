package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.SuccessionCandidate} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SuccessionCandidateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /succession-candidates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuccessionCandidateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReadinessLevel
     */
    public static class ReadinessLevelFilter extends Filter<ReadinessLevel> {

        public ReadinessLevelFilter() {}

        public ReadinessLevelFilter(ReadinessLevelFilter filter) {
            super(filter);
        }

        @Override
        public ReadinessLevelFilter copy() {
            return new ReadinessLevelFilter(this);
        }
    }

    /**
     * Class for filtering RiskLevel
     */
    public static class RiskLevelFilter extends Filter<RiskLevel> {

        public RiskLevelFilter() {}

        public RiskLevelFilter(RiskLevelFilter filter) {
            super(filter);
        }

        @Override
        public RiskLevelFilter copy() {
            return new RiskLevelFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ReadinessLevelFilter successionReadiness;

    private StringFilter estimatedTimeToReady;

    private RiskLevelFilter riskAfterTraining;

    private LocalDateFilter reviewDate;

    private IntegerFilter priority;

    private LongFilter positionId;

    private LongFilter currentOwnerId;

    private LongFilter candidateId;

    private Boolean distinct;

    public SuccessionCandidateCriteria() {}

    public SuccessionCandidateCriteria(SuccessionCandidateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.successionReadiness = other.optionalSuccessionReadiness().map(ReadinessLevelFilter::copy).orElse(null);
        this.estimatedTimeToReady = other.optionalEstimatedTimeToReady().map(StringFilter::copy).orElse(null);
        this.riskAfterTraining = other.optionalRiskAfterTraining().map(RiskLevelFilter::copy).orElse(null);
        this.reviewDate = other.optionalReviewDate().map(LocalDateFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.currentOwnerId = other.optionalCurrentOwnerId().map(LongFilter::copy).orElse(null);
        this.candidateId = other.optionalCandidateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SuccessionCandidateCriteria copy() {
        return new SuccessionCandidateCriteria(this);
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

    public ReadinessLevelFilter getSuccessionReadiness() {
        return successionReadiness;
    }

    public Optional<ReadinessLevelFilter> optionalSuccessionReadiness() {
        return Optional.ofNullable(successionReadiness);
    }

    public ReadinessLevelFilter successionReadiness() {
        if (successionReadiness == null) {
            setSuccessionReadiness(new ReadinessLevelFilter());
        }
        return successionReadiness;
    }

    public void setSuccessionReadiness(ReadinessLevelFilter successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public StringFilter getEstimatedTimeToReady() {
        return estimatedTimeToReady;
    }

    public Optional<StringFilter> optionalEstimatedTimeToReady() {
        return Optional.ofNullable(estimatedTimeToReady);
    }

    public StringFilter estimatedTimeToReady() {
        if (estimatedTimeToReady == null) {
            setEstimatedTimeToReady(new StringFilter());
        }
        return estimatedTimeToReady;
    }

    public void setEstimatedTimeToReady(StringFilter estimatedTimeToReady) {
        this.estimatedTimeToReady = estimatedTimeToReady;
    }

    public RiskLevelFilter getRiskAfterTraining() {
        return riskAfterTraining;
    }

    public Optional<RiskLevelFilter> optionalRiskAfterTraining() {
        return Optional.ofNullable(riskAfterTraining);
    }

    public RiskLevelFilter riskAfterTraining() {
        if (riskAfterTraining == null) {
            setRiskAfterTraining(new RiskLevelFilter());
        }
        return riskAfterTraining;
    }

    public void setRiskAfterTraining(RiskLevelFilter riskAfterTraining) {
        this.riskAfterTraining = riskAfterTraining;
    }

    public LocalDateFilter getReviewDate() {
        return reviewDate;
    }

    public Optional<LocalDateFilter> optionalReviewDate() {
        return Optional.ofNullable(reviewDate);
    }

    public LocalDateFilter reviewDate() {
        if (reviewDate == null) {
            setReviewDate(new LocalDateFilter());
        }
        return reviewDate;
    }

    public void setReviewDate(LocalDateFilter reviewDate) {
        this.reviewDate = reviewDate;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
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

    public LongFilter getCurrentOwnerId() {
        return currentOwnerId;
    }

    public Optional<LongFilter> optionalCurrentOwnerId() {
        return Optional.ofNullable(currentOwnerId);
    }

    public LongFilter currentOwnerId() {
        if (currentOwnerId == null) {
            setCurrentOwnerId(new LongFilter());
        }
        return currentOwnerId;
    }

    public void setCurrentOwnerId(LongFilter currentOwnerId) {
        this.currentOwnerId = currentOwnerId;
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
        final SuccessionCandidateCriteria that = (SuccessionCandidateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(successionReadiness, that.successionReadiness) &&
            Objects.equals(estimatedTimeToReady, that.estimatedTimeToReady) &&
            Objects.equals(riskAfterTraining, that.riskAfterTraining) &&
            Objects.equals(reviewDate, that.reviewDate) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(currentOwnerId, that.currentOwnerId) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            successionReadiness,
            estimatedTimeToReady,
            riskAfterTraining,
            reviewDate,
            priority,
            positionId,
            currentOwnerId,
            candidateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuccessionCandidateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSuccessionReadiness().map(f -> "successionReadiness=" + f + ", ").orElse("") +
            optionalEstimatedTimeToReady().map(f -> "estimatedTimeToReady=" + f + ", ").orElse("") +
            optionalRiskAfterTraining().map(f -> "riskAfterTraining=" + f + ", ").orElse("") +
            optionalReviewDate().map(f -> "reviewDate=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalCurrentOwnerId().map(f -> "currentOwnerId=" + f + ", ").orElse("") +
            optionalCandidateId().map(f -> "candidateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
