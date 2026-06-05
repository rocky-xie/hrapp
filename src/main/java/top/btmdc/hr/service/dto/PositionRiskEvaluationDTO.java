package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PositionRiskEvaluation} entity.
 */
@Schema(description = "职位风险评价。对职位整体风险的定期评价快照。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionRiskEvaluationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate evaluationDate;

    @Min(value = 0)
    private Integer ownerCount;

    @Min(value = 0)
    private Integer substitutableOwnerCount;

    @NotNull
    private Boolean hasSubstitute;

    private DocumentStatus documentStatus;

    private ImportanceLevel customerOrSystemDependency;

    private ReadinessLevel successionReadiness;

    @NotNull
    private RiskLevel riskLevel;

    @Lob
    private String riskReason;

    @Lob
    private String recommendedAction;

    @NotNull
    private PositionDTO position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Integer getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(Integer ownerCount) {
        this.ownerCount = ownerCount;
    }

    public Integer getSubstitutableOwnerCount() {
        return substitutableOwnerCount;
    }

    public void setSubstitutableOwnerCount(Integer substitutableOwnerCount) {
        this.substitutableOwnerCount = substitutableOwnerCount;
    }

    public Boolean getHasSubstitute() {
        return hasSubstitute;
    }

    public void setHasSubstitute(Boolean hasSubstitute) {
        this.hasSubstitute = hasSubstitute;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public ImportanceLevel getCustomerOrSystemDependency() {
        return customerOrSystemDependency;
    }

    public void setCustomerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.customerOrSystemDependency = customerOrSystemDependency;
    }

    public ReadinessLevel getSuccessionReadiness() {
        return successionReadiness;
    }

    public void setSuccessionReadiness(ReadinessLevel successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskReason() {
        return riskReason;
    }

    public void setRiskReason(String riskReason) {
        this.riskReason = riskReason;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionRiskEvaluationDTO)) {
            return false;
        }

        PositionRiskEvaluationDTO positionRiskEvaluationDTO = (PositionRiskEvaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionRiskEvaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionRiskEvaluationDTO{" +
            "id=" + getId() +
            ", evaluationDate='" + getEvaluationDate() + "'" +
            ", ownerCount=" + getOwnerCount() +
            ", substitutableOwnerCount=" + getSubstitutableOwnerCount() +
            ", hasSubstitute='" + getHasSubstitute() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", customerOrSystemDependency='" + getCustomerOrSystemDependency() + "'" +
            ", successionReadiness='" + getSuccessionReadiness() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", riskReason='" + getRiskReason() + "'" +
            ", recommendedAction='" + getRecommendedAction() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
