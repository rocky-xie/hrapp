package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.CandidateJudgement;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;

/**
 * 候选人画像。对候选人多维度的评估记录。
 */
@Entity
@Table(name = "candidate_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "candidate_date", nullable = false)
    private LocalDate candidateDate;

    @Size(max = 150)
    @Column(name = "cultivate_direction", length = 150)
    private String cultivateDirection;

    @Enumerated(EnumType.STRING)
    @Column(name = "stability")
    private ImportanceLevel stability;

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_ability")
    private ImportanceLevel learningAbility;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_coordination")
    private ImportanceLevel communicationCoordination;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_understanding")
    private ImportanceLevel businessUnderstanding;

    @Enumerated(EnumType.STRING)
    @Column(name = "responsibility")
    private ImportanceLevel responsibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_awareness")
    private ImportanceLevel riskAwareness;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "judgement", nullable = false)
    private CandidateJudgement judgement;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person observer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CandidateProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCandidateDate() {
        return this.candidateDate;
    }

    public CandidateProfile candidateDate(LocalDate candidateDate) {
        this.setCandidateDate(candidateDate);
        return this;
    }

    public void setCandidateDate(LocalDate candidateDate) {
        this.candidateDate = candidateDate;
    }

    public String getCultivateDirection() {
        return this.cultivateDirection;
    }

    public CandidateProfile cultivateDirection(String cultivateDirection) {
        this.setCultivateDirection(cultivateDirection);
        return this;
    }

    public void setCultivateDirection(String cultivateDirection) {
        this.cultivateDirection = cultivateDirection;
    }

    public ImportanceLevel getStability() {
        return this.stability;
    }

    public CandidateProfile stability(ImportanceLevel stability) {
        this.setStability(stability);
        return this;
    }

    public void setStability(ImportanceLevel stability) {
        this.stability = stability;
    }

    public ImportanceLevel getLearningAbility() {
        return this.learningAbility;
    }

    public CandidateProfile learningAbility(ImportanceLevel learningAbility) {
        this.setLearningAbility(learningAbility);
        return this;
    }

    public void setLearningAbility(ImportanceLevel learningAbility) {
        this.learningAbility = learningAbility;
    }

    public ImportanceLevel getCommunicationCoordination() {
        return this.communicationCoordination;
    }

    public CandidateProfile communicationCoordination(ImportanceLevel communicationCoordination) {
        this.setCommunicationCoordination(communicationCoordination);
        return this;
    }

    public void setCommunicationCoordination(ImportanceLevel communicationCoordination) {
        this.communicationCoordination = communicationCoordination;
    }

    public ImportanceLevel getBusinessUnderstanding() {
        return this.businessUnderstanding;
    }

    public CandidateProfile businessUnderstanding(ImportanceLevel businessUnderstanding) {
        this.setBusinessUnderstanding(businessUnderstanding);
        return this;
    }

    public void setBusinessUnderstanding(ImportanceLevel businessUnderstanding) {
        this.businessUnderstanding = businessUnderstanding;
    }

    public ImportanceLevel getResponsibility() {
        return this.responsibility;
    }

    public CandidateProfile responsibility(ImportanceLevel responsibility) {
        this.setResponsibility(responsibility);
        return this;
    }

    public void setResponsibility(ImportanceLevel responsibility) {
        this.responsibility = responsibility;
    }

    public ImportanceLevel getRiskAwareness() {
        return this.riskAwareness;
    }

    public CandidateProfile riskAwareness(ImportanceLevel riskAwareness) {
        this.setRiskAwareness(riskAwareness);
        return this;
    }

    public void setRiskAwareness(ImportanceLevel riskAwareness) {
        this.riskAwareness = riskAwareness;
    }

    public CandidateJudgement getJudgement() {
        return this.judgement;
    }

    public CandidateProfile judgement(CandidateJudgement judgement) {
        this.setJudgement(judgement);
        return this;
    }

    public void setJudgement(CandidateJudgement judgement) {
        this.judgement = judgement;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public CandidateProfile evidence(String evidence) {
        this.setEvidence(evidence);
        return this;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public CandidateProfile person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public CandidateProfile position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Person getObserver() {
        return this.observer;
    }

    public void setObserver(Person person) {
        this.observer = person;
    }

    public CandidateProfile observer(Person person) {
        this.setObserver(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((CandidateProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateProfile{" +
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
            "}";
    }
}
