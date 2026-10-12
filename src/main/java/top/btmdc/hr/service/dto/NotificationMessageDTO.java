package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;

/**
 * A DTO for the {@link top.btmdc.hr.domain.NotificationMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationMessageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String eventType;

    private Long templateId;

    @NotNull
    private NotificationMessageStatus status;

    @NotNull
    @Size(max = 30)
    private String sendMode;

    @NotNull
    @Size(max = 255)
    private String finalSubject;

    @Lob
    private String finalBody;

    @Lob
    private String recipientsJson;

    @NotNull
    @Min(value = 0)
    private Integer retryCount;

    @NotNull
    @Min(value = 0)
    private Integer maxRetries;

    @Lob
    private String lastError;

    @NotNull
    @Size(max = 100)
    private String createdBy;

    @Size(max = 100)
    private String confirmedBy;

    @NotNull
    private Instant createdAt;

    private Instant confirmedAt;

    private Instant sentAt;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public NotificationMessageStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationMessageStatus status) {
        this.status = status;
    }

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    public String getFinalSubject() {
        return finalSubject;
    }

    public void setFinalSubject(String finalSubject) {
        this.finalSubject = finalSubject;
    }

    public String getFinalBody() {
        return finalBody;
    }

    public void setFinalBody(String finalBody) {
        this.finalBody = finalBody;
    }

    public String getRecipientsJson() {
        return recipientsJson;
    }

    public void setRecipientsJson(String recipientsJson) {
        this.recipientsJson = recipientsJson;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
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
        if (!(o instanceof NotificationMessageDTO)) {
            return false;
        }

        NotificationMessageDTO notificationMessageDTO = (NotificationMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationMessageDTO{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", templateId=" + getTemplateId() +
            ", status='" + getStatus() + "'" +
            ", sendMode='" + getSendMode() + "'" +
            ", finalSubject='" + getFinalSubject() + "'" +
            ", finalBody='" + getFinalBody() + "'" +
            ", recipientsJson='" + getRecipientsJson() + "'" +
            ", retryCount=" + getRetryCount() +
            ", maxRetries=" + getMaxRetries() +
            ", lastError='" + getLastError() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", confirmedBy='" + getConfirmedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", confirmedAt='" + getConfirmedAt() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
