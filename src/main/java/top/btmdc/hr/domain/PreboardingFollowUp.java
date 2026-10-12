package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PreboardingFollowUp.
 */
@Entity
@Table(name = "preboarding_follow_up")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreboardingFollowUp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "planned_at")
    private Instant plannedAt;

    @Column(name = "contacted_at")
    private Instant contactedAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "owner_username", length = 100, nullable = false)
    private String ownerUsername;

    @Size(max = 40)
    @Column(name = "contact_method", length = 40)
    private String contactMethod;

    @Lob
    @Column(name = "result")
    private String result;

    @Size(max = 40)
    @Column(name = "candidate_intent", length = 40)
    private String candidateIntent;

    @Size(max = 30)
    @Column(name = "risk_level", length = 30)
    private String riskLevel;

    @Lob
    @Column(name = "next_action")
    private String nextAction;

    @Column(name = "next_contact_at")
    private Instant nextContactAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "stageses",
            "eventses",
            "activitieses",
            "evaluationses",
            "decisionses",
            "messageses",
            "followUpses",
            "dispatchAssignmentses",
            "candidate",
            "requisition",
            "requisitionVersion",
        },
        allowSetters = true
    )
    private RecruitmentApplication application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PreboardingFollowUp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return this.candidateId;
    }

    public PreboardingFollowUp candidateId(Long candidateId) {
        this.setCandidateId(candidateId);
        return this;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getPersonId() {
        return this.personId;
    }

    public PreboardingFollowUp personId(Long personId) {
        this.setPersonId(personId);
        return this;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Instant getPlannedAt() {
        return this.plannedAt;
    }

    public PreboardingFollowUp plannedAt(Instant plannedAt) {
        this.setPlannedAt(plannedAt);
        return this;
    }

    public void setPlannedAt(Instant plannedAt) {
        this.plannedAt = plannedAt;
    }

    public Instant getContactedAt() {
        return this.contactedAt;
    }

    public PreboardingFollowUp contactedAt(Instant contactedAt) {
        this.setContactedAt(contactedAt);
        return this;
    }

    public void setContactedAt(Instant contactedAt) {
        this.contactedAt = contactedAt;
    }

    public String getOwnerUsername() {
        return this.ownerUsername;
    }

    public PreboardingFollowUp ownerUsername(String ownerUsername) {
        this.setOwnerUsername(ownerUsername);
        return this;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getContactMethod() {
        return this.contactMethod;
    }

    public PreboardingFollowUp contactMethod(String contactMethod) {
        this.setContactMethod(contactMethod);
        return this;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getResult() {
        return this.result;
    }

    public PreboardingFollowUp result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCandidateIntent() {
        return this.candidateIntent;
    }

    public PreboardingFollowUp candidateIntent(String candidateIntent) {
        this.setCandidateIntent(candidateIntent);
        return this;
    }

    public void setCandidateIntent(String candidateIntent) {
        this.candidateIntent = candidateIntent;
    }

    public String getRiskLevel() {
        return this.riskLevel;
    }

    public PreboardingFollowUp riskLevel(String riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getNextAction() {
        return this.nextAction;
    }

    public PreboardingFollowUp nextAction(String nextAction) {
        this.setNextAction(nextAction);
        return this;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public Instant getNextContactAt() {
        return this.nextContactAt;
    }

    public PreboardingFollowUp nextContactAt(Instant nextContactAt) {
        this.setNextContactAt(nextContactAt);
        return this;
    }

    public void setNextContactAt(Instant nextContactAt) {
        this.nextContactAt = nextContactAt;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public PreboardingFollowUp application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreboardingFollowUp)) {
            return false;
        }
        return getId() != null && getId().equals(((PreboardingFollowUp) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreboardingFollowUp{" +
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
            "}";
    }
}
