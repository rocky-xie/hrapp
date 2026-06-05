package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.StaffSubstitution} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.StaffSubstitutionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /staff-substitutions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaffSubstitutionCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter coverageRate;

    private BigDecimalFilter thresholdRate;

    private IntegerFilter totalSkillCount;

    private IntegerFilter coveredSkillCount;

    private BooleanFilter substitutable;

    private LocalDateFilter evaluationDate;

    private LongFilter positionId;

    private LongFilter candidatePersonId;

    private Boolean distinct;

    public StaffSubstitutionCriteria() {}

    public StaffSubstitutionCriteria(StaffSubstitutionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.coverageRate = other.optionalCoverageRate().map(BigDecimalFilter::copy).orElse(null);
        this.thresholdRate = other.optionalThresholdRate().map(BigDecimalFilter::copy).orElse(null);
        this.totalSkillCount = other.optionalTotalSkillCount().map(IntegerFilter::copy).orElse(null);
        this.coveredSkillCount = other.optionalCoveredSkillCount().map(IntegerFilter::copy).orElse(null);
        this.substitutable = other.optionalSubstitutable().map(BooleanFilter::copy).orElse(null);
        this.evaluationDate = other.optionalEvaluationDate().map(LocalDateFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.candidatePersonId = other.optionalCandidatePersonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StaffSubstitutionCriteria copy() {
        return new StaffSubstitutionCriteria(this);
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

    public BigDecimalFilter getCoverageRate() {
        return coverageRate;
    }

    public Optional<BigDecimalFilter> optionalCoverageRate() {
        return Optional.ofNullable(coverageRate);
    }

    public BigDecimalFilter coverageRate() {
        if (coverageRate == null) {
            setCoverageRate(new BigDecimalFilter());
        }
        return coverageRate;
    }

    public void setCoverageRate(BigDecimalFilter coverageRate) {
        this.coverageRate = coverageRate;
    }

    public BigDecimalFilter getThresholdRate() {
        return thresholdRate;
    }

    public Optional<BigDecimalFilter> optionalThresholdRate() {
        return Optional.ofNullable(thresholdRate);
    }

    public BigDecimalFilter thresholdRate() {
        if (thresholdRate == null) {
            setThresholdRate(new BigDecimalFilter());
        }
        return thresholdRate;
    }

    public void setThresholdRate(BigDecimalFilter thresholdRate) {
        this.thresholdRate = thresholdRate;
    }

    public IntegerFilter getTotalSkillCount() {
        return totalSkillCount;
    }

    public Optional<IntegerFilter> optionalTotalSkillCount() {
        return Optional.ofNullable(totalSkillCount);
    }

    public IntegerFilter totalSkillCount() {
        if (totalSkillCount == null) {
            setTotalSkillCount(new IntegerFilter());
        }
        return totalSkillCount;
    }

    public void setTotalSkillCount(IntegerFilter totalSkillCount) {
        this.totalSkillCount = totalSkillCount;
    }

    public IntegerFilter getCoveredSkillCount() {
        return coveredSkillCount;
    }

    public Optional<IntegerFilter> optionalCoveredSkillCount() {
        return Optional.ofNullable(coveredSkillCount);
    }

    public IntegerFilter coveredSkillCount() {
        if (coveredSkillCount == null) {
            setCoveredSkillCount(new IntegerFilter());
        }
        return coveredSkillCount;
    }

    public void setCoveredSkillCount(IntegerFilter coveredSkillCount) {
        this.coveredSkillCount = coveredSkillCount;
    }

    public BooleanFilter getSubstitutable() {
        return substitutable;
    }

    public Optional<BooleanFilter> optionalSubstitutable() {
        return Optional.ofNullable(substitutable);
    }

    public BooleanFilter substitutable() {
        if (substitutable == null) {
            setSubstitutable(new BooleanFilter());
        }
        return substitutable;
    }

    public void setSubstitutable(BooleanFilter substitutable) {
        this.substitutable = substitutable;
    }

    public LocalDateFilter getEvaluationDate() {
        return evaluationDate;
    }

    public Optional<LocalDateFilter> optionalEvaluationDate() {
        return Optional.ofNullable(evaluationDate);
    }

    public LocalDateFilter evaluationDate() {
        if (evaluationDate == null) {
            setEvaluationDate(new LocalDateFilter());
        }
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateFilter evaluationDate) {
        this.evaluationDate = evaluationDate;
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

    public LongFilter getCandidatePersonId() {
        return candidatePersonId;
    }

    public Optional<LongFilter> optionalCandidatePersonId() {
        return Optional.ofNullable(candidatePersonId);
    }

    public LongFilter candidatePersonId() {
        if (candidatePersonId == null) {
            setCandidatePersonId(new LongFilter());
        }
        return candidatePersonId;
    }

    public void setCandidatePersonId(LongFilter candidatePersonId) {
        this.candidatePersonId = candidatePersonId;
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
        final StaffSubstitutionCriteria that = (StaffSubstitutionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(coverageRate, that.coverageRate) &&
            Objects.equals(thresholdRate, that.thresholdRate) &&
            Objects.equals(totalSkillCount, that.totalSkillCount) &&
            Objects.equals(coveredSkillCount, that.coveredSkillCount) &&
            Objects.equals(substitutable, that.substitutable) &&
            Objects.equals(evaluationDate, that.evaluationDate) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(candidatePersonId, that.candidatePersonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            coverageRate,
            thresholdRate,
            totalSkillCount,
            coveredSkillCount,
            substitutable,
            evaluationDate,
            positionId,
            candidatePersonId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffSubstitutionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCoverageRate().map(f -> "coverageRate=" + f + ", ").orElse("") +
            optionalThresholdRate().map(f -> "thresholdRate=" + f + ", ").orElse("") +
            optionalTotalSkillCount().map(f -> "totalSkillCount=" + f + ", ").orElse("") +
            optionalCoveredSkillCount().map(f -> "coveredSkillCount=" + f + ", ").orElse("") +
            optionalSubstitutable().map(f -> "substitutable=" + f + ", ").orElse("") +
            optionalEvaluationDate().map(f -> "evaluationDate=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalCandidatePersonId().map(f -> "candidatePersonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
