package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * 继任候选人。记录某职位候选人的接替评估。
 */
@Entity
@Table(name = "succession_candidate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuccessionCandidate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "succession_readiness", nullable = false)
    private ReadinessLevel successionReadiness;

    @Lob
    @Column(name = "required_training")
    private String requiredTraining;

    @Size(max = 100)
    @Column(name = "estimated_time_to_ready", length = 100)
    private String estimatedTimeToReady;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_after_training")
    private RiskLevel riskAfterTraining;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Min(value = 1)
    @Column(name = "priority")
    private Integer priority;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person currentOwner;

    @ManyToOne(optional = false)
    @NotNull
    private Person candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuccessionCandidate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReadinessLevel getSuccessionReadiness() {
        return this.successionReadiness;
    }

    public SuccessionCandidate successionReadiness(ReadinessLevel successionReadiness) {
        this.setSuccessionReadiness(successionReadiness);
        return this;
    }

    public void setSuccessionReadiness(ReadinessLevel successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public String getRequiredTraining() {
        return this.requiredTraining;
    }

    public SuccessionCandidate requiredTraining(String requiredTraining) {
        this.setRequiredTraining(requiredTraining);
        return this;
    }

    public void setRequiredTraining(String requiredTraining) {
        this.requiredTraining = requiredTraining;
    }

    public String getEstimatedTimeToReady() {
        return this.estimatedTimeToReady;
    }

    public SuccessionCandidate estimatedTimeToReady(String estimatedTimeToReady) {
        this.setEstimatedTimeToReady(estimatedTimeToReady);
        return this;
    }

    public void setEstimatedTimeToReady(String estimatedTimeToReady) {
        this.estimatedTimeToReady = estimatedTimeToReady;
    }

    public RiskLevel getRiskAfterTraining() {
        return this.riskAfterTraining;
    }

    public SuccessionCandidate riskAfterTraining(RiskLevel riskAfterTraining) {
        this.setRiskAfterTraining(riskAfterTraining);
        return this;
    }

    public void setRiskAfterTraining(RiskLevel riskAfterTraining) {
        this.riskAfterTraining = riskAfterTraining;
    }

    public LocalDate getReviewDate() {
        return this.reviewDate;
    }

    public SuccessionCandidate reviewDate(LocalDate reviewDate) {
        this.setReviewDate(reviewDate);
        return this;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public SuccessionCandidate priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public SuccessionCandidate position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Person getCurrentOwner() {
        return this.currentOwner;
    }

    public void setCurrentOwner(Person person) {
        this.currentOwner = person;
    }

    public SuccessionCandidate currentOwner(Person person) {
        this.setCurrentOwner(person);
        return this;
    }

    public Person getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Person person) {
        this.candidate = person;
    }

    public SuccessionCandidate candidate(Person person) {
        this.setCandidate(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuccessionCandidate)) {
            return false;
        }
        return getId() != null && getId().equals(((SuccessionCandidate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuccessionCandidate{" +
            "id=" + getId() +
            ", successionReadiness='" + getSuccessionReadiness() + "'" +
            ", requiredTraining='" + getRequiredTraining() + "'" +
            ", estimatedTimeToReady='" + getEstimatedTimeToReady() + "'" +
            ", riskAfterTraining='" + getRiskAfterTraining() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
