package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;

/**
 * A NotificationMessage.
 */
@Entity
@Table(name = "notification_message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "event_type", length = 60, nullable = false)
    private String eventType;

    @Column(name = "template_id")
    private Long templateId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationMessageStatus status;

    @NotNull
    @Size(max = 30)
    @Column(name = "send_mode", length = 30, nullable = false)
    private String sendMode;

    @NotNull
    @Size(max = 255)
    @Column(name = "final_subject", length = 255, nullable = false)
    private String finalSubject;

    @Lob
    @Column(name = "final_body", nullable = false)
    private String finalBody;

    @Lob
    @Column(name = "recipients_json", nullable = false)
    private String recipientsJson;

    @NotNull
    @Min(value = 0)
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @NotNull
    @Min(value = 0)
    @Column(name = "max_retries", nullable = false)
    private Integer maxRetries;

    @Lob
    @Column(name = "last_error")
    private String lastError;

    @NotNull
    @Size(max = 100)
    @Column(name = "created_by", length = 100, nullable = false)
    private String createdBy;

    @Size(max = 100)
    @Column(name = "confirmed_by", length = 100)
    private String confirmedBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

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

    public NotificationMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public NotificationMessage eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTemplateId() {
        return this.templateId;
    }

    public NotificationMessage templateId(Long templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public NotificationMessageStatus getStatus() {
        return this.status;
    }

    public NotificationMessage status(NotificationMessageStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationMessageStatus status) {
        this.status = status;
    }

    public String getSendMode() {
        return this.sendMode;
    }

    public NotificationMessage sendMode(String sendMode) {
        this.setSendMode(sendMode);
        return this;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    public String getFinalSubject() {
        return this.finalSubject;
    }

    public NotificationMessage finalSubject(String finalSubject) {
        this.setFinalSubject(finalSubject);
        return this;
    }

    public void setFinalSubject(String finalSubject) {
        this.finalSubject = finalSubject;
    }

    public String getFinalBody() {
        return this.finalBody;
    }

    public NotificationMessage finalBody(String finalBody) {
        this.setFinalBody(finalBody);
        return this;
    }

    public void setFinalBody(String finalBody) {
        this.finalBody = finalBody;
    }

    public String getRecipientsJson() {
        return this.recipientsJson;
    }

    public NotificationMessage recipientsJson(String recipientsJson) {
        this.setRecipientsJson(recipientsJson);
        return this;
    }

    public void setRecipientsJson(String recipientsJson) {
        this.recipientsJson = recipientsJson;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public NotificationMessage retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public NotificationMessage maxRetries(Integer maxRetries) {
        this.setMaxRetries(maxRetries);
        return this;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getLastError() {
        return this.lastError;
    }

    public NotificationMessage lastError(String lastError) {
        this.setLastError(lastError);
        return this;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public NotificationMessage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getConfirmedBy() {
        return this.confirmedBy;
    }

    public NotificationMessage confirmedBy(String confirmedBy) {
        this.setConfirmedBy(confirmedBy);
        return this;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public NotificationMessage createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getConfirmedAt() {
        return this.confirmedAt;
    }

    public NotificationMessage confirmedAt(Instant confirmedAt) {
        this.setConfirmedAt(confirmedAt);
        return this;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public NotificationMessage sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public NotificationMessage application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationMessage{" +
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
            "}";
    }
}
