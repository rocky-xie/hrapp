package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.domain.enumeration.ProgressStatus;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.Evaluation} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.EvaluationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evaluations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProgressStatus
     */
    public static class ProgressStatusFilter extends Filter<ProgressStatus> {

        public ProgressStatusFilter() {}

        public ProgressStatusFilter(ProgressStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProgressStatusFilter copy() {
            return new ProgressStatusFilter(this);
        }
    }

    /**
     * Class for filtering AssessmentResult
     */
    public static class AssessmentResultFilter extends Filter<AssessmentResult> {

        public AssessmentResultFilter() {}

        public AssessmentResultFilter(AssessmentResultFilter filter) {
            super(filter);
        }

        @Override
        public AssessmentResultFilter copy() {
            return new AssessmentResultFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter evaluationName;

    private LocalDateFilter evaluationDate;

    private StringFilter periodLabel;

    private ProgressStatusFilter progressStatus;

    private AssessmentResultFilter result;

    private BooleanFilter positionAdjustmentNeeded;

    private LongFilter personId;

    private LongFilter positionId;

    private LongFilter trainingGoalId;

    private LongFilter evaluatorId;

    private Boolean distinct;

    public EvaluationCriteria() {}

    public EvaluationCriteria(EvaluationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.evaluationName = other.optionalEvaluationName().map(StringFilter::copy).orElse(null);
        this.evaluationDate = other.optionalEvaluationDate().map(LocalDateFilter::copy).orElse(null);
        this.periodLabel = other.optionalPeriodLabel().map(StringFilter::copy).orElse(null);
        this.progressStatus = other.optionalProgressStatus().map(ProgressStatusFilter::copy).orElse(null);
        this.result = other.optionalResult().map(AssessmentResultFilter::copy).orElse(null);
        this.positionAdjustmentNeeded = other.optionalPositionAdjustmentNeeded().map(BooleanFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.trainingGoalId = other.optionalTrainingGoalId().map(LongFilter::copy).orElse(null);
        this.evaluatorId = other.optionalEvaluatorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EvaluationCriteria copy() {
        return new EvaluationCriteria(this);
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

    public StringFilter getEvaluationName() {
        return evaluationName;
    }

    public Optional<StringFilter> optionalEvaluationName() {
        return Optional.ofNullable(evaluationName);
    }

    public StringFilter evaluationName() {
        if (evaluationName == null) {
            setEvaluationName(new StringFilter());
        }
        return evaluationName;
    }

    public void setEvaluationName(StringFilter evaluationName) {
        this.evaluationName = evaluationName;
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

    public StringFilter getPeriodLabel() {
        return periodLabel;
    }

    public Optional<StringFilter> optionalPeriodLabel() {
        return Optional.ofNullable(periodLabel);
    }

    public StringFilter periodLabel() {
        if (periodLabel == null) {
            setPeriodLabel(new StringFilter());
        }
        return periodLabel;
    }

    public void setPeriodLabel(StringFilter periodLabel) {
        this.periodLabel = periodLabel;
    }

    public ProgressStatusFilter getProgressStatus() {
        return progressStatus;
    }

    public Optional<ProgressStatusFilter> optionalProgressStatus() {
        return Optional.ofNullable(progressStatus);
    }

    public ProgressStatusFilter progressStatus() {
        if (progressStatus == null) {
            setProgressStatus(new ProgressStatusFilter());
        }
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatusFilter progressStatus) {
        this.progressStatus = progressStatus;
    }

    public AssessmentResultFilter getResult() {
        return result;
    }

    public Optional<AssessmentResultFilter> optionalResult() {
        return Optional.ofNullable(result);
    }

    public AssessmentResultFilter result() {
        if (result == null) {
            setResult(new AssessmentResultFilter());
        }
        return result;
    }

    public void setResult(AssessmentResultFilter result) {
        this.result = result;
    }

    public BooleanFilter getPositionAdjustmentNeeded() {
        return positionAdjustmentNeeded;
    }

    public Optional<BooleanFilter> optionalPositionAdjustmentNeeded() {
        return Optional.ofNullable(positionAdjustmentNeeded);
    }

    public BooleanFilter positionAdjustmentNeeded() {
        if (positionAdjustmentNeeded == null) {
            setPositionAdjustmentNeeded(new BooleanFilter());
        }
        return positionAdjustmentNeeded;
    }

    public void setPositionAdjustmentNeeded(BooleanFilter positionAdjustmentNeeded) {
        this.positionAdjustmentNeeded = positionAdjustmentNeeded;
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

    public LongFilter getTrainingGoalId() {
        return trainingGoalId;
    }

    public Optional<LongFilter> optionalTrainingGoalId() {
        return Optional.ofNullable(trainingGoalId);
    }

    public LongFilter trainingGoalId() {
        if (trainingGoalId == null) {
            setTrainingGoalId(new LongFilter());
        }
        return trainingGoalId;
    }

    public void setTrainingGoalId(LongFilter trainingGoalId) {
        this.trainingGoalId = trainingGoalId;
    }

    public LongFilter getEvaluatorId() {
        return evaluatorId;
    }

    public Optional<LongFilter> optionalEvaluatorId() {
        return Optional.ofNullable(evaluatorId);
    }

    public LongFilter evaluatorId() {
        if (evaluatorId == null) {
            setEvaluatorId(new LongFilter());
        }
        return evaluatorId;
    }

    public void setEvaluatorId(LongFilter evaluatorId) {
        this.evaluatorId = evaluatorId;
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
        final EvaluationCriteria that = (EvaluationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(evaluationName, that.evaluationName) &&
            Objects.equals(evaluationDate, that.evaluationDate) &&
            Objects.equals(periodLabel, that.periodLabel) &&
            Objects.equals(progressStatus, that.progressStatus) &&
            Objects.equals(result, that.result) &&
            Objects.equals(positionAdjustmentNeeded, that.positionAdjustmentNeeded) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(trainingGoalId, that.trainingGoalId) &&
            Objects.equals(evaluatorId, that.evaluatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            evaluationName,
            evaluationDate,
            periodLabel,
            progressStatus,
            result,
            positionAdjustmentNeeded,
            personId,
            positionId,
            trainingGoalId,
            evaluatorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEvaluationName().map(f -> "evaluationName=" + f + ", ").orElse("") +
            optionalEvaluationDate().map(f -> "evaluationDate=" + f + ", ").orElse("") +
            optionalPeriodLabel().map(f -> "periodLabel=" + f + ", ").orElse("") +
            optionalProgressStatus().map(f -> "progressStatus=" + f + ", ").orElse("") +
            optionalResult().map(f -> "result=" + f + ", ").orElse("") +
            optionalPositionAdjustmentNeeded().map(f -> "positionAdjustmentNeeded=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalTrainingGoalId().map(f -> "trainingGoalId=" + f + ", ").orElse("") +
            optionalEvaluatorId().map(f -> "evaluatorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
