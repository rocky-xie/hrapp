package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.ApplicationStage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationStageDTO implements Serializable {

    private Long id;

    @NotNull
    private ApplicationStageType stageType;

    @NotNull
    @Min(value = 1)
    private Integer sequenceNumber;

    @NotNull
    @Size(max = 30)
    private String stageStatus;

    private Instant startedAt;

    private Instant completedAt;

    private Boolean selected;

    @Lob
    private String decisionReason;

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

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
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
        if (!(o instanceof ApplicationStageDTO)) {
            return false;
        }

        ApplicationStageDTO applicationStageDTO = (ApplicationStageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationStageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationStageDTO{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", stageStatus='" + getStageStatus() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", selected='" + getSelected() + "'" +
            ", decisionReason='" + getDecisionReason() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
