package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A RecruitmentEvaluation.
 */
@Entity
@Table(name = "recruitment_evaluation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentEvaluation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false)
    private ApplicationStageType stageType;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "template_version_id")
    private Long templateVersionId;

    @NotNull
    @Size(max = 100)
    @Column(name = "evaluator_username", length = 100, nullable = false)
    private String evaluatorUsername;

    @NotNull
    @Size(max = 100)
    @Column(name = "recorded_by_username", length = 100, nullable = false)
    private String recordedByUsername;

    @NotNull
    @Size(max = 30)
    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Size(max = 50)
    @Column(name = "overall_recommendation", length = 50)
    private String overallRecommendation;

    @DecimalMin(value = "0")
    @Column(name = "total_score", precision = 21, scale = 2)
    private BigDecimal totalScore;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Lob
    @Column(name = "answers_json")
    private String answersJson;

    @Column(name = "evaluated_at")
    private Instant evaluatedAt;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "correction_of_id")
    private Long correctionOfId;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private RecruitmentApplication application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentEvaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return this.stageType;
    }

    public RecruitmentEvaluation stageType(ApplicationStageType stageType) {
        this.setStageType(stageType);
        return this;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public RecruitmentEvaluation activityId(Long activityId) {
        this.setActivityId(activityId);
        return this;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getTemplateVersionId() {
        return this.templateVersionId;
    }

    public RecruitmentEvaluation templateVersionId(Long templateVersionId) {
        this.setTemplateVersionId(templateVersionId);
        return this;
    }

    public void setTemplateVersionId(Long templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public String getEvaluatorUsername() {
        return this.evaluatorUsername;
    }

    public RecruitmentEvaluation evaluatorUsername(String evaluatorUsername) {
        this.setEvaluatorUsername(evaluatorUsername);
        return this;
    }

    public void setEvaluatorUsername(String evaluatorUsername) {
        this.evaluatorUsername = evaluatorUsername;
    }

    public String getRecordedByUsername() {
        return this.recordedByUsername;
    }

    public RecruitmentEvaluation recordedByUsername(String recordedByUsername) {
        this.setRecordedByUsername(recordedByUsername);
        return this;
    }

    public void setRecordedByUsername(String recordedByUsername) {
        this.recordedByUsername = recordedByUsername;
    }

    public String getStatus() {
        return this.status;
    }

    public RecruitmentEvaluation status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOverallRecommendation() {
        return this.overallRecommendation;
    }

    public RecruitmentEvaluation overallRecommendation(String overallRecommendation) {
        this.setOverallRecommendation(overallRecommendation);
        return this;
    }

    public void setOverallRecommendation(String overallRecommendation) {
        this.overallRecommendation = overallRecommendation;
    }

    public BigDecimal getTotalScore() {
        return this.totalScore;
    }

    public RecruitmentEvaluation totalScore(BigDecimal totalScore) {
        this.setTotalScore(totalScore);
        return this;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public String getSummary() {
        return this.summary;
    }

    public RecruitmentEvaluation summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAnswersJson() {
        return this.answersJson;
    }

    public RecruitmentEvaluation answersJson(String answersJson) {
        this.setAnswersJson(answersJson);
        return this;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public Instant getEvaluatedAt() {
        return this.evaluatedAt;
    }

    public RecruitmentEvaluation evaluatedAt(Instant evaluatedAt) {
        this.setEvaluatedAt(evaluatedAt);
        return this;
    }

    public void setEvaluatedAt(Instant evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    public RecruitmentEvaluation submittedAt(Instant submittedAt) {
        this.setSubmittedAt(submittedAt);
        return this;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }

    public RecruitmentEvaluation publishedAt(Instant publishedAt) {
        this.setPublishedAt(publishedAt);
        return this;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Long getCorrectionOfId() {
        return this.correctionOfId;
    }

    public RecruitmentEvaluation correctionOfId(Long correctionOfId) {
        this.setCorrectionOfId(correctionOfId);
        return this;
    }

    public void setCorrectionOfId(Long correctionOfId) {
        this.correctionOfId = correctionOfId;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public RecruitmentEvaluation application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentEvaluation)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentEvaluation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentEvaluation{" +
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
            "}";
    }
}
