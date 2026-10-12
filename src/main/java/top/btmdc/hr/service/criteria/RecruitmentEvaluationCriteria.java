package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.RecruitmentEvaluation} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.RecruitmentEvaluationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recruitment-evaluations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentEvaluationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ApplicationStageType
     */
    public static class ApplicationStageTypeFilter extends Filter<ApplicationStageType> {

        public ApplicationStageTypeFilter() {}

        public ApplicationStageTypeFilter(ApplicationStageTypeFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationStageTypeFilter copy() {
            return new ApplicationStageTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApplicationStageTypeFilter stageType;

    private LongFilter activityId;

    private LongFilter templateVersionId;

    private StringFilter evaluatorUsername;

    private StringFilter recordedByUsername;

    private StringFilter status;

    private StringFilter overallRecommendation;

    private BigDecimalFilter totalScore;

    private InstantFilter evaluatedAt;

    private InstantFilter submittedAt;

    private InstantFilter publishedAt;

    private LongFilter correctionOfId;

    private LongFilter applicationId;

    private Boolean distinct;

    public RecruitmentEvaluationCriteria() {}

    public RecruitmentEvaluationCriteria(RecruitmentEvaluationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stageType = other.optionalStageType().map(ApplicationStageTypeFilter::copy).orElse(null);
        this.activityId = other.optionalActivityId().map(LongFilter::copy).orElse(null);
        this.templateVersionId = other.optionalTemplateVersionId().map(LongFilter::copy).orElse(null);
        this.evaluatorUsername = other.optionalEvaluatorUsername().map(StringFilter::copy).orElse(null);
        this.recordedByUsername = other.optionalRecordedByUsername().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.overallRecommendation = other.optionalOverallRecommendation().map(StringFilter::copy).orElse(null);
        this.totalScore = other.optionalTotalScore().map(BigDecimalFilter::copy).orElse(null);
        this.evaluatedAt = other.optionalEvaluatedAt().map(InstantFilter::copy).orElse(null);
        this.submittedAt = other.optionalSubmittedAt().map(InstantFilter::copy).orElse(null);
        this.publishedAt = other.optionalPublishedAt().map(InstantFilter::copy).orElse(null);
        this.correctionOfId = other.optionalCorrectionOfId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecruitmentEvaluationCriteria copy() {
        return new RecruitmentEvaluationCriteria(this);
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

    public ApplicationStageTypeFilter getStageType() {
        return stageType;
    }

    public Optional<ApplicationStageTypeFilter> optionalStageType() {
        return Optional.ofNullable(stageType);
    }

    public ApplicationStageTypeFilter stageType() {
        if (stageType == null) {
            setStageType(new ApplicationStageTypeFilter());
        }
        return stageType;
    }

    public void setStageType(ApplicationStageTypeFilter stageType) {
        this.stageType = stageType;
    }

    public LongFilter getActivityId() {
        return activityId;
    }

    public Optional<LongFilter> optionalActivityId() {
        return Optional.ofNullable(activityId);
    }

    public LongFilter activityId() {
        if (activityId == null) {
            setActivityId(new LongFilter());
        }
        return activityId;
    }

    public void setActivityId(LongFilter activityId) {
        this.activityId = activityId;
    }

    public LongFilter getTemplateVersionId() {
        return templateVersionId;
    }

    public Optional<LongFilter> optionalTemplateVersionId() {
        return Optional.ofNullable(templateVersionId);
    }

    public LongFilter templateVersionId() {
        if (templateVersionId == null) {
            setTemplateVersionId(new LongFilter());
        }
        return templateVersionId;
    }

    public void setTemplateVersionId(LongFilter templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public StringFilter getEvaluatorUsername() {
        return evaluatorUsername;
    }

    public Optional<StringFilter> optionalEvaluatorUsername() {
        return Optional.ofNullable(evaluatorUsername);
    }

    public StringFilter evaluatorUsername() {
        if (evaluatorUsername == null) {
            setEvaluatorUsername(new StringFilter());
        }
        return evaluatorUsername;
    }

    public void setEvaluatorUsername(StringFilter evaluatorUsername) {
        this.evaluatorUsername = evaluatorUsername;
    }

    public StringFilter getRecordedByUsername() {
        return recordedByUsername;
    }

    public Optional<StringFilter> optionalRecordedByUsername() {
        return Optional.ofNullable(recordedByUsername);
    }

    public StringFilter recordedByUsername() {
        if (recordedByUsername == null) {
            setRecordedByUsername(new StringFilter());
        }
        return recordedByUsername;
    }

    public void setRecordedByUsername(StringFilter recordedByUsername) {
        this.recordedByUsername = recordedByUsername;
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

    public StringFilter getOverallRecommendation() {
        return overallRecommendation;
    }

    public Optional<StringFilter> optionalOverallRecommendation() {
        return Optional.ofNullable(overallRecommendation);
    }

    public StringFilter overallRecommendation() {
        if (overallRecommendation == null) {
            setOverallRecommendation(new StringFilter());
        }
        return overallRecommendation;
    }

    public void setOverallRecommendation(StringFilter overallRecommendation) {
        this.overallRecommendation = overallRecommendation;
    }

    public BigDecimalFilter getTotalScore() {
        return totalScore;
    }

    public Optional<BigDecimalFilter> optionalTotalScore() {
        return Optional.ofNullable(totalScore);
    }

    public BigDecimalFilter totalScore() {
        if (totalScore == null) {
            setTotalScore(new BigDecimalFilter());
        }
        return totalScore;
    }

    public void setTotalScore(BigDecimalFilter totalScore) {
        this.totalScore = totalScore;
    }

    public InstantFilter getEvaluatedAt() {
        return evaluatedAt;
    }

    public Optional<InstantFilter> optionalEvaluatedAt() {
        return Optional.ofNullable(evaluatedAt);
    }

    public InstantFilter evaluatedAt() {
        if (evaluatedAt == null) {
            setEvaluatedAt(new InstantFilter());
        }
        return evaluatedAt;
    }

    public void setEvaluatedAt(InstantFilter evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public InstantFilter getSubmittedAt() {
        return submittedAt;
    }

    public Optional<InstantFilter> optionalSubmittedAt() {
        return Optional.ofNullable(submittedAt);
    }

    public InstantFilter submittedAt() {
        if (submittedAt == null) {
            setSubmittedAt(new InstantFilter());
        }
        return submittedAt;
    }

    public void setSubmittedAt(InstantFilter submittedAt) {
        this.submittedAt = submittedAt;
    }

    public InstantFilter getPublishedAt() {
        return publishedAt;
    }

    public Optional<InstantFilter> optionalPublishedAt() {
        return Optional.ofNullable(publishedAt);
    }

    public InstantFilter publishedAt() {
        if (publishedAt == null) {
            setPublishedAt(new InstantFilter());
        }
        return publishedAt;
    }

    public void setPublishedAt(InstantFilter publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LongFilter getCorrectionOfId() {
        return correctionOfId;
    }

    public Optional<LongFilter> optionalCorrectionOfId() {
        return Optional.ofNullable(correctionOfId);
    }

    public LongFilter correctionOfId() {
        if (correctionOfId == null) {
            setCorrectionOfId(new LongFilter());
        }
        return correctionOfId;
    }

    public void setCorrectionOfId(LongFilter correctionOfId) {
        this.correctionOfId = correctionOfId;
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
        final RecruitmentEvaluationCriteria that = (RecruitmentEvaluationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stageType, that.stageType) &&
            Objects.equals(activityId, that.activityId) &&
            Objects.equals(templateVersionId, that.templateVersionId) &&
            Objects.equals(evaluatorUsername, that.evaluatorUsername) &&
            Objects.equals(recordedByUsername, that.recordedByUsername) &&
            Objects.equals(status, that.status) &&
            Objects.equals(overallRecommendation, that.overallRecommendation) &&
            Objects.equals(totalScore, that.totalScore) &&
            Objects.equals(evaluatedAt, that.evaluatedAt) &&
            Objects.equals(submittedAt, that.submittedAt) &&
            Objects.equals(publishedAt, that.publishedAt) &&
            Objects.equals(correctionOfId, that.correctionOfId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stageType,
            activityId,
            templateVersionId,
            evaluatorUsername,
            recordedByUsername,
            status,
            overallRecommendation,
            totalScore,
            evaluatedAt,
            submittedAt,
            publishedAt,
            correctionOfId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentEvaluationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStageType().map(f -> "stageType=" + f + ", ").orElse("") +
            optionalActivityId().map(f -> "activityId=" + f + ", ").orElse("") +
            optionalTemplateVersionId().map(f -> "templateVersionId=" + f + ", ").orElse("") +
            optionalEvaluatorUsername().map(f -> "evaluatorUsername=" + f + ", ").orElse("") +
            optionalRecordedByUsername().map(f -> "recordedByUsername=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalOverallRecommendation().map(f -> "overallRecommendation=" + f + ", ").orElse("") +
            optionalTotalScore().map(f -> "totalScore=" + f + ", ").orElse("") +
            optionalEvaluatedAt().map(f -> "evaluatedAt=" + f + ", ").orElse("") +
            optionalSubmittedAt().map(f -> "submittedAt=" + f + ", ").orElse("") +
            optionalPublishedAt().map(f -> "publishedAt=" + f + ", ").orElse("") +
            optionalCorrectionOfId().map(f -> "correctionOfId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
