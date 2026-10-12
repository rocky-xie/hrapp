package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.RecruitmentEvaluation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentEvaluationDTO implements Serializable {

    private Long id;

    @NotNull
    private ApplicationStageType stageType;

    private Long activityId;

    private Long templateVersionId;

    @NotNull
    @Size(max = 100)
    private String evaluatorUsername;

    @NotNull
    @Size(max = 100)
    private String recordedByUsername;

    @NotNull
    @Size(max = 30)
    private String status;

    @Size(max = 50)
    private String overallRecommendation;

    @DecimalMin(value = "0")
    private BigDecimal totalScore;

    @Lob
    private String summary;

    @Lob
    private String answersJson;

    private Instant evaluatedAt;

    private Instant submittedAt;

    private Instant publishedAt;

    private Long correctionOfId;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return stageType;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getTemplateVersionId() {
        return templateVersionId;
    }

    public void setTemplateVersionId(Long templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public String getEvaluatorUsername() {
        return evaluatorUsername;
    }

    public void setEvaluatorUsername(String evaluatorUsername) {
        this.evaluatorUsername = evaluatorUsername;
    }

    public String getRecordedByUsername() {
        return recordedByUsername;
    }

    public void setRecordedByUsername(String recordedByUsername) {
        this.recordedByUsername = recordedByUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOverallRecommendation() {
        return overallRecommendation;
    }

    public void setOverallRecommendation(String overallRecommendation) {
        this.overallRecommendation = overallRecommendation;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public Instant getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Instant evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Long getCorrectionOfId() {
        return correctionOfId;
    }

    public void setCorrectionOfId(Long correctionOfId) {
        this.correctionOfId = correctionOfId;
    }

    public RecruitmentApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(RecruitmentApplicationDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentEvaluationDTO)) {
            return false;
        }

        RecruitmentEvaluationDTO recruitmentEvaluationDTO = (RecruitmentEvaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentEvaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentEvaluationDTO{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", activityId=" + getActivityId() +
            ", templateVersionId=" + getTemplateVersionId() +
            ", evaluatorUsername='" + getEvaluatorUsername() + "'" +
            ", recordedByUsername='" + getRecordedByUsername() + "'" +
            ", status='" + getStatus() + "'" +
            ", overallRecommendation='" + getOverallRecommendation() + "'" +
            ", totalScore=" + getTotalScore() +
            ", summary='" + getSummary() + "'" +
            ", answersJson='" + getAnswersJson() + "'" +
            ", evaluatedAt='" + getEvaluatedAt() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", correctionOfId=" + getCorrectionOfId() +
            ", application=" + getApplication() +
            "}";
    }
}
