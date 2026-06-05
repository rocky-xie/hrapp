package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.BackupStatus;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PositionRisk} entity.
 */
@Schema(description = "岗位风险。针对岗位维度的风险评估。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionRiskDTO implements Serializable {

    private Long id;

    @NotNull
    private RiskType riskType;

    @NotNull
    private RiskLevel riskLevel;

    private DocumentStatus documentStatus;

    private BackupStatus backupStatus;

    private ImportanceLevel customerOrSystemDependency;

    @Lob
    private String riskDescription;

    @Lob
    private String improvementAction;

    @NotNull
    private LocalDate identifiedDate;

    private LocalDate targetDate;

    private LocalDate closedDate;

    @NotNull
    private PositionDTO position;

    private KeyResponsibilityCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public BackupStatus getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(BackupStatus backupStatus) {
        this.backupStatus = backupStatus;
    }

    public ImportanceLevel getCustomerOrSystemDependency() {
        return customerOrSystemDependency;
    }

    public void setCustomerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.customerOrSystemDependency = customerOrSystemDependency;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getImprovementAction() {
        return improvementAction;
    }

    public void setImprovementAction(String improvementAction) {
        this.improvementAction = improvementAction;
    }

    public LocalDate getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(LocalDate identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public KeyResponsibilityCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(KeyResponsibilityCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionRiskDTO)) {
            return false;
        }

        PositionRiskDTO positionRiskDTO = (PositionRiskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionRiskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionRiskDTO{" +
            "id=" + getId() +
            ", riskType='" + getRiskType() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", backupStatus='" + getBackupStatus() + "'" +
            ", customerOrSystemDependency='" + getCustomerOrSystemDependency() + "'" +
            ", riskDescription='" + getRiskDescription() + "'" +
            ", improvementAction='" + getImprovementAction() + "'" +
            ", identifiedDate='" + getIdentifiedDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", position=" + getPosition() +
            ", category=" + getCategory() +
            "}";
    }
}
