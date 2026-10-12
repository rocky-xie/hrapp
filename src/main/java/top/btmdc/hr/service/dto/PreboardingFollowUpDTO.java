package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PreboardingFollowUp} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreboardingFollowUpDTO implements Serializable {

    private Long id;

    @NotNull
    private Long candidateId;

    private Long personId;

    private Instant plannedAt;

    private Instant contactedAt;

    @NotNull
    @Size(max = 100)
    private String ownerUsername;

    @Size(max = 40)
    private String contactMethod;

    @Lob
    private String result;

    @Size(max = 40)
    private String candidateIntent;

    @Size(max = 30)
    private String riskLevel;

    @Lob
    private String nextAction;

    private Instant nextContactAt;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Instant getPlannedAt() {
        return plannedAt;
    }

    public void setPlannedAt(Instant plannedAt) {
        this.plannedAt = plannedAt;
    }

    public Instant getContactedAt() {
        return contactedAt;
    }

    public void setContactedAt(Instant contactedAt) {
        this.contactedAt = contactedAt;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCandidateIntent() {
        return candidateIntent;
    }

    public void setCandidateIntent(String candidateIntent) {
        this.candidateIntent = candidateIntent;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public Instant getNextContactAt() {
        return nextContactAt;
    }

    public void setNextContactAt(Instant nextContactAt) {
        this.nextContactAt = nextContactAt;
    }

    public RecruitmentApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(RecruitmentApplicationDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreboardingFollowUpDTO)) {
            return false;
        }

        PreboardingFollowUpDTO preboardingFollowUpDTO = (PreboardingFollowUpDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, preboardingFollowUpDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreboardingFollowUpDTO{" +
            "id=" + getId() +
            ", candidateId=" + getCandidateId() +
            ", personId=" + getPersonId() +
            ", plannedAt='" + getPlannedAt() + "'" +
            ", contactedAt='" + getContactedAt() + "'" +
            ", ownerUsername='" + getOwnerUsername() + "'" +
            ", contactMethod='" + getContactMethod() + "'" +
            ", result='" + getResult() + "'" +
            ", candidateIntent='" + getCandidateIntent() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", nextAction='" + getNextAction() + "'" +
            ", nextContactAt='" + getNextContactAt() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
