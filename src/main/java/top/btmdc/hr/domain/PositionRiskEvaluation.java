package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * 职位风险评价。对职位整体风险的定期评价快照。
 */
@Entity
@Table(name = "position_risk_evaluation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionRiskEvaluation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Min(value = 0)
    @Column(name = "owner_count")
    private Integer ownerCount;

    @Min(value = 0)
    @Column(name = "substitutable_owner_count")
    private Integer substitutableOwnerCount;

    @NotNull
    @Column(name = "has_substitute", nullable = false)
    private Boolean hasSubstitute;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    private DocumentStatus documentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_or_system_dependency")
    private ImportanceLevel customerOrSystemDependency;

    @Enumerated(EnumType.STRING)
    @Column(name = "succession_readiness")
    private ReadinessLevel successionReadiness;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Lob
    @Column(name = "risk_reason")
    private String riskReason;

    @Lob
    @Column(name = "recommended_action")
    private String recommendedAction;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionRiskEvaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEvaluationDate() {
        return this.evaluationDate;
    }

    public PositionRiskEvaluation evaluationDate(LocalDate evaluationDate) {
        this.setEvaluationDate(evaluationDate);
        return this;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Integer getOwnerCount() {
        return this.ownerCount;
    }

    public PositionRiskEvaluation ownerCount(Integer ownerCount) {
        this.setOwnerCount(ownerCount);
        return this;
    }

    public void setOwnerCount(Integer ownerCount) {
        this.ownerCount = ownerCount;
    }

    public Integer getSubstitutableOwnerCount() {
        return this.substitutableOwnerCount;
    }

    public PositionRiskEvaluation substitutableOwnerCount(Integer substitutableOwnerCount) {
        this.setSubstitutableOwnerCount(substitutableOwnerCount);
        return this;
    }

    public void setSubstitutableOwnerCount(Integer substitutableOwnerCount) {
        this.substitutableOwnerCount = substitutableOwnerCount;
    }

    public Boolean getHasSubstitute() {
        return this.hasSubstitute;
    }

    public PositionRiskEvaluation hasSubstitute(Boolean hasSubstitute) {
        this.setHasSubstitute(hasSubstitute);
        return this;
    }

    public void setHasSubstitute(Boolean hasSubstitute) {
        this.hasSubstitute = hasSubstitute;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public PositionRiskEvaluation documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public ImportanceLevel getCustomerOrSystemDependency() {
        return this.customerOrSystemDependency;
    }

    public PositionRiskEvaluation customerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.setCustomerOrSystemDependency(customerOrSystemDependency);
        return this;
    }

    public void setCustomerOrSystemDependency(ImportanceLevel customerOrSystemDependency) {
        this.customerOrSystemDependency = customerOrSystemDependency;
    }

    public ReadinessLevel getSuccessionReadiness() {
        return this.successionReadiness;
    }

    public PositionRiskEvaluation successionReadiness(ReadinessLevel successionReadiness) {
        this.setSuccessionReadiness(successionReadiness);
        return this;
    }

    public void setSuccessionReadiness(ReadinessLevel successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public RiskLevel getRiskLevel() {
        return this.riskLevel;
    }

    public PositionRiskEvaluation riskLevel(RiskLevel riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskReason() {
        return this.riskReason;
    }

    public PositionRiskEvaluation riskReason(String riskReason) {
        this.setRiskReason(riskReason);
        return this;
    }

    public void setRiskReason(String riskReason) {
        this.riskReason = riskReason;
    }

    public String getRecommendedAction() {
        return this.recommendedAction;
    }

    public PositionRiskEvaluation recommendedAction(String recommendedAction) {
        this.setRecommendedAction(recommendedAction);
        return this;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionRiskEvaluation position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionRiskEvaluation)) {
            return false;
        }
        return getId() != null && getId().equals(((PositionRiskEvaluation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionRiskEvaluation{" +
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
            "}";
    }
}
