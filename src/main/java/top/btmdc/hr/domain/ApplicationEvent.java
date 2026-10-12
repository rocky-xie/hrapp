package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A ApplicationEvent.
 */
@Entity
@Table(name = "application_event")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type")
    private ApplicationStageType stageType;

    @NotNull
    @Size(max = 50)
    @Column(name = "event_type", length = 50, nullable = false)
    private String eventType;

    @Size(max = 100)
    @Column(name = "previous_value", length = 100)
    private String previousValue;

    @Size(max = 100)
    @Column(name = "new_value", length = 100)
    private String newValue;

    @NotNull
    @Size(max = 100)
    @Column(name = "actor", length = 100, nullable = false)
    private String actor;

    @NotNull
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Lob
    @Column(name = "reason")
    private String reason;

    @Lob
    @Column(name = "metadata_json")
    private String metadataJson;

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

    public ApplicationEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return this.stageType;
    }

    public ApplicationEvent stageType(ApplicationStageType stageType) {
        this.setStageType(stageType);
        return this;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public String getEventType() {
        return this.eventType;
    }

    public ApplicationEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPreviousValue() {
        return this.previousValue;
    }

    public ApplicationEvent previousValue(String previousValue) {
        this.setPreviousValue(previousValue);
        return this;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public ApplicationEvent newValue(String newValue) {
        this.setNewValue(newValue);
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getActor() {
        return this.actor;
    }

    public ApplicationEvent actor(String actor) {
        this.setActor(actor);
        return this;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Instant getOccurredAt() {
        return this.occurredAt;
    }

    public ApplicationEvent occurredAt(Instant occurredAt) {
        this.setOccurredAt(occurredAt);
        return this;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return this.reason;
    }

    public ApplicationEvent reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMetadataJson() {
        return this.metadataJson;
    }

    public ApplicationEvent metadataJson(String metadataJson) {
        this.setMetadataJson(metadataJson);
        return this;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public ApplicationEvent application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationEvent{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", previousValue='" + getPreviousValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", actor='" + getActor() + "'" +
            ", occurredAt='" + getOccurredAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", metadataJson='" + getMetadataJson() + "'" +
            "}";
    }
}
