package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;

/**
 * 人员风险。针对人员维度的流失/单点等风险评估。
 */
@Entity
@Table(name = "person_risk")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonRisk implements Serializable {

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
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonRisk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskType getRiskType() {
        return this.riskType;
    }

    public PersonRisk riskType(RiskType riskType) {
        this.setRiskType(riskType);
        return this;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public RiskLevel getRiskLevel() {
        return this.riskLevel;
    }

    public PersonRisk riskLevel(RiskLevel riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskDescription() {
        return this.riskDescription;
    }

    public PersonRisk riskDescription(String riskDescription) {
        this.setRiskDescription(riskDescription);
        return this;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getImprovementAction() {
        return this.improvementAction;
    }

    public PersonRisk improvementAction(String improvementAction) {
        this.setImprovementAction(improvementAction);
        return this;
    }

    public void setImprovementAction(String improvementAction) {
        this.improvementAction = improvementAction;
    }

    public LocalDate getIdentifiedDate() {
        return this.identifiedDate;
    }

    public PersonRisk identifiedDate(LocalDate identifiedDate) {
        this.setIdentifiedDate(identifiedDate);
        return this;
    }

    public void setIdentifiedDate(LocalDate identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public LocalDate getTargetDate() {
        return this.targetDate;
    }

    public PersonRisk targetDate(LocalDate targetDate) {
        this.setTargetDate(targetDate);
        return this;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getClosedDate() {
        return this.closedDate;
    }

    public PersonRisk closedDate(LocalDate closedDate) {
        this.setClosedDate(closedDate);
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonRisk person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PersonRisk position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonRisk)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonRisk) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonRisk{" +
            "id=" + getId() +
            ", riskType='" + getRiskType() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", riskDescription='" + getRiskDescription() + "'" +
            ", improvementAction='" + getImprovementAction() + "'" +
            ", identifiedDate='" + getIdentifiedDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            "}";
    }
}
