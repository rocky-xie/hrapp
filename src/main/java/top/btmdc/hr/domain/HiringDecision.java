package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;

/**
 * A HiringDecision.
 */
@Entity
@Table(name = "hiring_decision")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiringDecision implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private ApplicationDecision decision;

    @NotNull
    @Size(max = 50)
    @Column(name = "decision_source", length = 50, nullable = false)
    private String decisionSource;

    @NotNull
    @Size(max = 100)
    @Column(name = "decided_by", length = 100, nullable = false)
    private String decidedBy;

    @NotNull
    @Column(name = "decided_at", nullable = false)
    private Instant decidedAt;

    @Lob
    @Column(name = "reason")
    private String reason;

    @Column(name = "correction_of_id")
    private Long correctionOfId;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

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

    public HiringDecision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationDecision getDecision() {
        return this.decision;
    }

    public HiringDecision decision(ApplicationDecision decision) {
        this.setDecision(decision);
        return this;
    }

    public void setDecision(ApplicationDecision decision) {
        this.decision = decision;
    }

    public String getDecisionSource() {
        return this.decisionSource;
    }

    public HiringDecision decisionSource(String decisionSource) {
        this.setDecisionSource(decisionSource);
        return this;
    }

    public void setDecisionSource(String decisionSource) {
        this.decisionSource = decisionSource;
    }

    public String getDecidedBy() {
        return this.decidedBy;
    }

    public HiringDecision decidedBy(String decidedBy) {
        this.setDecidedBy(decidedBy);
        return this;
    }

    public void setDecidedBy(String decidedBy) {
        this.decidedBy = decidedBy;
    }

    public Instant getDecidedAt() {
        return this.decidedAt;
    }

    public HiringDecision decidedAt(Instant decidedAt) {
        this.setDecidedAt(decidedAt);
        return this;
    }

    public void setDecidedAt(Instant decidedAt) {
        this.decidedAt = decidedAt;
    }

    public String getReason() {
        return this.reason;
    }

    public HiringDecision reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCorrectionOfId() {
        return this.correctionOfId;
    }

    public HiringDecision correctionOfId(Long correctionOfId) {
        this.setCorrectionOfId(correctionOfId);
        return this;
    }

    public void setCorrectionOfId(Long correctionOfId) {
        this.correctionOfId = correctionOfId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public HiringDecision active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public HiringDecision application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HiringDecision)) {
            return false;
        }
        return getId() != null && getId().equals(((HiringDecision) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HiringDecision{" +
            "id=" + getId() +
            ", decision='" + getDecision() + "'" +
            ", decisionSource='" + getDecisionSource() + "'" +
            ", decidedBy='" + getDecidedBy() + "'" +
            ", decidedAt='" + getDecidedAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", correctionOfId=" + getCorrectionOfId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
