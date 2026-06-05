package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.TrustStage;

/**
 * 信任观察。对人员信任阶段的阶段性评估。
 */
@Entity
@Table(name = "trust_observation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrustObservation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "observation_date", nullable = false)
    private LocalDate observationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "trust_stage", nullable = false)
    private TrustStage trustStage;

    @Lob
    @Column(name = "observed_behavior")
    private String observedBehavior;

    @Lob
    @Column(name = "positive_signal")
    private String positiveSignal;

    @Lob
    @Column(name = "risk_signal")
    private String riskSignal;

    @Lob
    @Column(name = "next_observation_point")
    private String nextObservationPoint;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person observer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrustObservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getObservationDate() {
        return this.observationDate;
    }

    public TrustObservation observationDate(LocalDate observationDate) {
        this.setObservationDate(observationDate);
        return this;
    }

    public void setObservationDate(LocalDate observationDate) {
        this.observationDate = observationDate;
    }

    public TrustStage getTrustStage() {
        return this.trustStage;
    }

    public TrustObservation trustStage(TrustStage trustStage) {
        this.setTrustStage(trustStage);
        return this;
    }

    public void setTrustStage(TrustStage trustStage) {
        this.trustStage = trustStage;
    }

    public String getObservedBehavior() {
        return this.observedBehavior;
    }

    public TrustObservation observedBehavior(String observedBehavior) {
        this.setObservedBehavior(observedBehavior);
        return this;
    }

    public void setObservedBehavior(String observedBehavior) {
        this.observedBehavior = observedBehavior;
    }

    public String getPositiveSignal() {
        return this.positiveSignal;
    }

    public TrustObservation positiveSignal(String positiveSignal) {
        this.setPositiveSignal(positiveSignal);
        return this;
    }

    public void setPositiveSignal(String positiveSignal) {
        this.positiveSignal = positiveSignal;
    }

    public String getRiskSignal() {
        return this.riskSignal;
    }

    public TrustObservation riskSignal(String riskSignal) {
        this.setRiskSignal(riskSignal);
        return this;
    }

    public void setRiskSignal(String riskSignal) {
        this.riskSignal = riskSignal;
    }

    public String getNextObservationPoint() {
        return this.nextObservationPoint;
    }

    public TrustObservation nextObservationPoint(String nextObservationPoint) {
        this.setNextObservationPoint(nextObservationPoint);
        return this;
    }

    public void setNextObservationPoint(String nextObservationPoint) {
        this.nextObservationPoint = nextObservationPoint;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TrustObservation person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Person getObserver() {
        return this.observer;
    }

    public void setObserver(Person person) {
        this.observer = person;
    }

    public TrustObservation observer(Person person) {
        this.setObserver(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrustObservation)) {
            return false;
        }
        return getId() != null && getId().equals(((TrustObservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrustObservation{" +
            "id=" + getId() +
            ", observationDate='" + getObservationDate() + "'" +
            ", trustStage='" + getTrustStage() + "'" +
            ", observedBehavior='" + getObservedBehavior() + "'" +
            ", positiveSignal='" + getPositiveSignal() + "'" +
            ", riskSignal='" + getRiskSignal() + "'" +
            ", nextObservationPoint='" + getNextObservationPoint() + "'" +
            "}";
    }
}
