package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A ApplicationStage.
 */
@Entity
@Table(name = "application_stage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationStage implements Serializable {

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

    @NotNull
    @Min(value = 1)
    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    @NotNull
    @Size(max = 30)
    @Column(name = "stage_status", length = 30, nullable = false)
    private String stageStatus;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "selected")
    private Boolean selected;

    @Lob
    @Column(name = "decision_reason")
    private String decisionReason;

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

    public ApplicationStage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return this.stageType;
    }

    public ApplicationStage stageType(ApplicationStageType stageType) {
        this.setStageType(stageType);
        return this;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public ApplicationStage sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getStageStatus() {
        return this.stageStatus;
    }

    public ApplicationStage stageStatus(String stageStatus) {
        this.setStageStatus(stageStatus);
        return this;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public ApplicationStage startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public ApplicationStage completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public ApplicationStage selected(Boolean selected) {
        this.setSelected(selected);
        return this;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getDecisionReason() {
        return this.decisionReason;
    }

    public ApplicationStage decisionReason(String decisionReason) {
        this.setDecisionReason(decisionReason);
        return this;
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public ApplicationStage application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationStage)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationStage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationStage{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", stageStatus='" + getStageStatus() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", selected='" + getSelected() + "'" +
            ", decisionReason='" + getDecisionReason() + "'" +
            "}";
    }
}
