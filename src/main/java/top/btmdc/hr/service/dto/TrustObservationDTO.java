package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.TrustStage;

/**
 * A DTO for the {@link top.btmdc.hr.domain.TrustObservation} entity.
 */
@Schema(description = "信任观察。对人员信任阶段的阶段性评估。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrustObservationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate observationDate;

    @NotNull
    private TrustStage trustStage;

    @Lob
    private String observedBehavior;

    @Lob
    private String positiveSignal;

    @Lob
    private String riskSignal;

    @Lob
    private String nextObservationPoint;

    @NotNull
    private PersonDTO person;

    private PersonDTO observer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(LocalDate observationDate) {
        this.observationDate = observationDate;
    }

    public TrustStage getTrustStage() {
        return trustStage;
    }

    public void setTrustStage(TrustStage trustStage) {
        this.trustStage = trustStage;
    }

    public String getObservedBehavior() {
        return observedBehavior;
    }

    public void setObservedBehavior(String observedBehavior) {
        this.observedBehavior = observedBehavior;
    }

    public String getPositiveSignal() {
        return positiveSignal;
    }

    public void setPositiveSignal(String positiveSignal) {
        this.positiveSignal = positiveSignal;
    }

    public String getRiskSignal() {
        return riskSignal;
    }

    public void setRiskSignal(String riskSignal) {
        this.riskSignal = riskSignal;
    }

    public String getNextObservationPoint() {
        return nextObservationPoint;
    }

    public void setNextObservationPoint(String nextObservationPoint) {
        this.nextObservationPoint = nextObservationPoint;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public PersonDTO getObserver() {
        return observer;
    }

    public void setObserver(PersonDTO observer) {
        this.observer = observer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrustObservationDTO)) {
            return false;
        }

        TrustObservationDTO trustObservationDTO = (TrustObservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trustObservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrustObservationDTO{" +
            "id=" + getId() +
            ", observationDate='" + getObservationDate() + "'" +
            ", trustStage='" + getTrustStage() + "'" +
            ", observedBehavior='" + getObservedBehavior() + "'" +
            ", positiveSignal='" + getPositiveSignal() + "'" +
            ", riskSignal='" + getRiskSignal() + "'" +
            ", nextObservationPoint='" + getNextObservationPoint() + "'" +
            ", person=" + getPerson() +
            ", observer=" + getObserver() +
            "}";
    }
}
