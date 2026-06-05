package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * A DTO for the {@link top.btmdc.hr.domain.SuccessionCandidate} entity.
 */
@Schema(description = "继任候选人。记录某职位候选人的接替评估。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuccessionCandidateDTO implements Serializable {

    private Long id;

    @NotNull
    private ReadinessLevel successionReadiness;

    @Lob
    private String requiredTraining;

    @Size(max = 100)
    private String estimatedTimeToReady;

    private RiskLevel riskAfterTraining;

    private LocalDate reviewDate;

    @Min(value = 1)
    private Integer priority;

    @NotNull
    private PositionDTO position;

    private PersonDTO currentOwner;

    @NotNull
    private PersonDTO candidate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReadinessLevel getSuccessionReadiness() {
        return successionReadiness;
    }

    public void setSuccessionReadiness(ReadinessLevel successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public String getRequiredTraining() {
        return requiredTraining;
    }

    public void setRequiredTraining(String requiredTraining) {
        this.requiredTraining = requiredTraining;
    }

    public String getEstimatedTimeToReady() {
        return estimatedTimeToReady;
    }

    public void setEstimatedTimeToReady(String estimatedTimeToReady) {
        this.estimatedTimeToReady = estimatedTimeToReady;
    }

    public RiskLevel getRiskAfterTraining() {
        return riskAfterTraining;
    }

    public void setRiskAfterTraining(RiskLevel riskAfterTraining) {
        this.riskAfterTraining = riskAfterTraining;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public PersonDTO getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(PersonDTO currentOwner) {
        this.currentOwner = currentOwner;
    }

    public PersonDTO getCandidate() {
        return candidate;
    }

    public void setCandidate(PersonDTO candidate) {
        this.candidate = candidate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuccessionCandidateDTO)) {
            return false;
        }

        SuccessionCandidateDTO successionCandidateDTO = (SuccessionCandidateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, successionCandidateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuccessionCandidateDTO{" +
            "id=" + getId() +
            ", successionReadiness='" + getSuccessionReadiness() + "'" +
            ", requiredTraining='" + getRequiredTraining() + "'" +
            ", estimatedTimeToReady='" + getEstimatedTimeToReady() + "'" +
            ", riskAfterTraining='" + getRiskAfterTraining() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", priority=" + getPriority() +
            ", position=" + getPosition() +
            ", currentOwner=" + getCurrentOwner() +
            ", candidate=" + getCandidate() +
            "}";
    }
}
