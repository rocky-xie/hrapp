package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;

/**
 * A DTO for the {@link top.btmdc.hr.domain.HiringDecision} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiringDecisionDTO implements Serializable {

    private Long id;

    @NotNull
    private ApplicationDecision decision;

    @NotNull
    @Size(max = 50)
    private String decisionSource;

    @NotNull
    @Size(max = 100)
    private String decidedBy;

    @NotNull
    private Instant decidedAt;

    @Lob
    private String reason;

    private Long correctionOfId;

    @NotNull
    private Boolean active;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationDecision getDecision() {
        return decision;
    }

    public void setDecision(ApplicationDecision decision) {
        this.decision = decision;
    }

    public String getDecisionSource() {
        return decisionSource;
    }

    public void setDecisionSource(String decisionSource) {
        this.decisionSource = decisionSource;
    }

    public String getDecidedBy() {
        return decidedBy;
    }

    public void setDecidedBy(String decidedBy) {
        this.decidedBy = decidedBy;
    }

    public Instant getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(Instant decidedAt) {
        this.decidedAt = decidedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCorrectionOfId() {
        return correctionOfId;
    }

    public void setCorrectionOfId(Long correctionOfId) {
        this.correctionOfId = correctionOfId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof HiringDecisionDTO)) {
            return false;
        }

        HiringDecisionDTO hiringDecisionDTO = (HiringDecisionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hiringDecisionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HiringDecisionDTO{" +
            "id=" + getId() +
            ", decision='" + getDecision() + "'" +
            ", decisionSource='" + getDecisionSource() + "'" +
            ", decidedBy='" + getDecidedBy() + "'" +
            ", decidedAt='" + getDecidedAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", correctionOfId=" + getCorrectionOfId() +
            ", active='" + getActive() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
