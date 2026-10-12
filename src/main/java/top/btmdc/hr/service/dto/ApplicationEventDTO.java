package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.ApplicationEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationEventDTO implements Serializable {

    private Long id;

    private ApplicationStageType stageType;

    @NotNull
    @Size(max = 50)
    private String eventType;

    @Size(max = 100)
    private String previousValue;

    @Size(max = 100)
    private String newValue;

    @NotNull
    @Size(max = 100)
    private String actor;

    @NotNull
    private Instant occurredAt;

    @Lob
    private String reason;

    @Lob
    private String metadataJson;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return stageType;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
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
        if (!(o instanceof ApplicationEventDTO)) {
            return false;
        }

        ApplicationEventDTO applicationEventDTO = (ApplicationEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationEventDTO{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", previousValue='" + getPreviousValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", actor='" + getActor() + "'" +
            ", occurredAt='" + getOccurredAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", metadataJson='" + getMetadataJson() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
