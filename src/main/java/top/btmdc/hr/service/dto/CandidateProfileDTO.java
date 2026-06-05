package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.CandidateJudgement;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;

/**
 * A DTO for the {@link top.btmdc.hr.domain.CandidateProfile} entity.
 */
@Schema(description = "候选人画像。对候选人多维度的评估记录。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate candidateDate;

    @Size(max = 150)
    private String cultivateDirection;

    private ImportanceLevel stability;

    private ImportanceLevel learningAbility;

    private ImportanceLevel communicationCoordination;

    private ImportanceLevel businessUnderstanding;

    private ImportanceLevel responsibility;

    private ImportanceLevel riskAwareness;

    @NotNull
    private CandidateJudgement judgement;

    @Lob
    private String evidence;

    @NotNull
    private PersonDTO person;

    private PositionDTO position;

    private PersonDTO observer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCandidateDate() {
        return candidateDate;
    }

    public void setCandidateDate(LocalDate candidateDate) {
        this.candidateDate = candidateDate;
    }

    public String getCultivateDirection() {
        return cultivateDirection;
    }

    public void setCultivateDirection(String cultivateDirection) {
        this.cultivateDirection = cultivateDirection;
    }

    public ImportanceLevel getStability() {
        return stability;
    }

    public void setStability(ImportanceLevel stability) {
        this.stability = stability;
    }

    public ImportanceLevel getLearningAbility() {
        return learningAbility;
    }

    public void setLearningAbility(ImportanceLevel learningAbility) {
        this.learningAbility = learningAbility;
    }

    public ImportanceLevel getCommunicationCoordination() {
        return communicationCoordination;
    }

    public void setCommunicationCoordination(ImportanceLevel communicationCoordination) {
        this.communicationCoordination = communicationCoordination;
    }

    public ImportanceLevel getBusinessUnderstanding() {
        return businessUnderstanding;
    }

    public void setBusinessUnderstanding(ImportanceLevel businessUnderstanding) {
        this.businessUnderstanding = businessUnderstanding;
    }

    public ImportanceLevel getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(ImportanceLevel responsibility) {
        this.responsibility = responsibility;
    }

    public ImportanceLevel getRiskAwareness() {
        return riskAwareness;
    }

    public void setRiskAwareness(ImportanceLevel riskAwareness) {
        this.riskAwareness = riskAwareness;
    }

    public CandidateJudgement getJudgement() {
        return judgement;
    }

    public void setJudgement(CandidateJudgement judgement) {
        this.judgement = judgement;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
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
        if (!(o instanceof CandidateProfileDTO)) {
            return false;
        }

        CandidateProfileDTO candidateProfileDTO = (CandidateProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidateProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateProfileDTO{" +
            "id=" + getId() +
            ", candidateDate='" + getCandidateDate() + "'" +
            ", cultivateDirection='" + getCultivateDirection() + "'" +
            ", stability='" + getStability() + "'" +
            ", learningAbility='" + getLearningAbility() + "'" +
            ", communicationCoordination='" + getCommunicationCoordination() + "'" +
            ", businessUnderstanding='" + getBusinessUnderstanding() + "'" +
            ", responsibility='" + getResponsibility() + "'" +
            ", riskAwareness='" + getRiskAwareness() + "'" +
            ", judgement='" + getJudgement() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            ", observer=" + getObserver() +
            "}";
    }
}
