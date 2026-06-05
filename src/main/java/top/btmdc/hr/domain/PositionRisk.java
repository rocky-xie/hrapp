package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.BackupStatus;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;

/**
 * 岗位风险。针对岗位维度的风险评估。
 */
@Entity
@Table(name = "position_risk")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionRisk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type", nullable = false)
    private RiskType riskType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    private DocumentStatus documentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "backup_status")
    private BackupStatus backupStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_or_system_dependency")
    private ImportanceLevel customerOrSystemDependency;

    @Lob
    @Column(name = "risk_description")
    private String riskDescription;

    @Lob
    @Column(name = "improvement_action")
    private String improvementAction;

    @NotNull
    @Column(name = "identified_date", nullable = false)
    private LocalDate identifiedDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private KeyResponsibilityCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionRisk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskType getRiskType() {
        return this.riskType;
    }

    public PositionRisk riskType(RiskType riskType) {
        this.setRiskType(riskType);
        return this;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public RiskLevel getRiskLevel() {
        return this.riskLevel;
    }

    public PositionRisk riskLevel(RiskLevel riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public PositionRisk documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public BackupStatus getBackupStatus() {
        return this.backupStatus;
    }

    public PositionRisk backupStatus(BackupStatus backupStatus) {
        this.setBackupStatus(backupStatus);
        return this;
    }

    public void setBackupStatus(BackupStatus backupStatus) {
        this.backupStatus = backupStatus;
    }

    public ImportanceLevel getCustomerOrSystemDependency() {
        return this.customerOrSystemDependency;
    }

    public PositionRisk customerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.setCustomerOrSystemDependency(customerOrSystemDependency);
        return this;
    }

    public void setCustomerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.customerOrSystemDependency = customerOrSystemDependency;
    }

    public String getRiskDescription() {
        return this.riskDescription;
    }

    public PositionRisk riskDescription(String riskDescription) {
        this.setRiskDescription(riskDescription);
        return this;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getImprovementAction() {
        return this.improvementAction;
    }

    public PositionRisk improvementAction(String improvementAction) {
        this.setImprovementAction(improvementAction);
        return this;
    }

    public void setImprovementAction(String improvementAction) {
        this.improvementAction = improvementAction;
    }

    public LocalDate getIdentifiedDate() {
        return this.identifiedDate;
    }

    public PositionRisk identifiedDate(LocalDate identifiedDate) {
        this.setIdentifiedDate(identifiedDate);
        return this;
    }

    public void setIdentifiedDate(LocalDate identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public LocalDate getTargetDate() {
        return this.targetDate;
    }

    public PositionRisk targetDate(LocalDate targetDate) {
        this.setTargetDate(targetDate);
        return this;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getClosedDate() {
        return this.closedDate;
    }

    public PositionRisk closedDate(LocalDate closedDate) {
        this.setClosedDate(closedDate);
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionRisk position(Position position) {
        this.setPosition(position);
        return this;
    }

    public KeyResponsibilityCategory getCategory() {
        return this.category;
    }

    public void setCategory(KeyResponsibilityCategory keyResponsibilityCategory) {
        this.category = keyResponsibilityCategory;
    }

    public PositionRisk category(KeyResponsibilityCategory keyResponsibilityCategory) {
        this.setCategory(keyResponsibilityCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionRisk)) {
            return false;
        }
        return getId() != null && getId().equals(((PositionRisk) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionRisk{" +
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
            "}";
    }
}
