package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PersonRisk} entity.
 */
@Schema(description = "人员风险。针对人员维度的流失/单点等风险评估。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonRiskDTO implements Serializable {

    private Long id;

    @NotNull
    private RiskType riskType;

    @NotNull
    private RiskLevel riskLevel;

    @Lob
    private String riskDescription;

    @Lob
    private String improvementAction;

    @NotNull
    private LocalDate identifiedDate;

    private LocalDate targetDate;

    private LocalDate closedDate;

    @NotNull
    private PersonDTO person;

    private PositionDTO position;

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

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
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
        if (!(o instanceof PersonRiskDTO)) {
            return false;
        }

        PersonRiskDTO personRiskDTO = (PersonRiskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personRiskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonRiskDTO{" +
            "id=" + getId() +
            ", riskType='" + getRiskType() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", riskDescription='" + getRiskDescription() + "'" +
            ", improvementAction='" + getImprovementAction() + "'" +
            ", identifiedDate='" + getIdentifiedDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            "}";
    }
}
