package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.NotificationMessage} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.NotificationMessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationMessageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationMessageStatus
     */
    public static class NotificationMessageStatusFilter extends Filter<NotificationMessageStatus> {

        public NotificationMessageStatusFilter() {}

        public NotificationMessageStatusFilter(NotificationMessageStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationMessageStatusFilter copy() {
            return new NotificationMessageStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventType;

    private LongFilter templateId;

    private NotificationMessageStatusFilter status;

    private StringFilter sendMode;

    private StringFilter finalSubject;

    private IntegerFilter retryCount;

    private IntegerFilter maxRetries;

    private StringFilter createdBy;

    private StringFilter confirmedBy;

    private InstantFilter createdAt;

    private InstantFilter confirmedAt;

    private InstantFilter sentAt;

    private LongFilter applicationId;

    private Boolean distinct;

    public NotificationMessageCriteria() {}

    public NotificationMessageCriteria(NotificationMessageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(StringFilter::copy).orElse(null);
        this.templateId = other.optionalTemplateId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(NotificationMessageStatusFilter::copy).orElse(null);
        this.sendMode = other.optionalSendMode().map(StringFilter::copy).orElse(null);
        this.finalSubject = other.optionalFinalSubject().map(StringFilter::copy).orElse(null);
        this.retryCount = other.optionalRetryCount().map(IntegerFilter::copy).orElse(null);
        this.maxRetries = other.optionalMaxRetries().map(IntegerFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.confirmedBy = other.optionalConfirmedBy().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.confirmedAt = other.optionalConfirmedAt().map(InstantFilter::copy).orElse(null);
        this.sentAt = other.optionalSentAt().map(InstantFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationMessageCriteria copy() {
        return new NotificationMessageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventType() {
        return eventType;
    }

    public Optional<StringFilter> optionalEventType() {
        return Optional.ofNullable(eventType);
    }

    public StringFilter eventType() {
        if (eventType == null) {
            setEventType(new StringFilter());
        }
        return eventType;
    }

    public void setEventType(StringFilter eventType) {
        this.eventType = eventType;
    }

    public LongFilter getTemplateId() {
        return templateId;
    }

    public Optional<LongFilter> optionalTemplateId() {
        return Optional.ofNullable(templateId);
    }

    public LongFilter templateId() {
        if (templateId == null) {
            setTemplateId(new LongFilter());
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public NotificationMessageStatusFilter getStatus() {
        return status;
    }

    public Optional<NotificationMessageStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public NotificationMessageStatusFilter status() {
        if (status == null) {
            setStatus(new NotificationMessageStatusFilter());
        }
        return status;
    }

    public void setStatus(NotificationMessageStatusFilter status) {
        this.status = status;
    }

    public StringFilter getSendMode() {
        return sendMode;
    }

    public Optional<StringFilter> optionalSendMode() {
        return Optional.ofNullable(sendMode);
    }

    public StringFilter sendMode() {
        if (sendMode == null) {
            setSendMode(new StringFilter());
        }
        return sendMode;
    }

    public void setSendMode(StringFilter sendMode) {
        this.sendMode = sendMode;
    }

    public StringFilter getFinalSubject() {
        return finalSubject;
    }

    public Optional<StringFilter> optionalFinalSubject() {
        return Optional.ofNullable(finalSubject);
    }

    public StringFilter finalSubject() {
        if (finalSubject == null) {
            setFinalSubject(new StringFilter());
        }
        return finalSubject;
    }

    public void setFinalSubject(StringFilter finalSubject) {
        this.finalSubject = finalSubject;
    }

    public IntegerFilter getRetryCount() {
        return retryCount;
    }

    public Optional<IntegerFilter> optionalRetryCount() {
        return Optional.ofNullable(retryCount);
    }

    public IntegerFilter retryCount() {
        if (retryCount == null) {
            setRetryCount(new IntegerFilter());
        }
        return retryCount;
    }

    public void setRetryCount(IntegerFilter retryCount) {
        this.retryCount = retryCount;
    }

    public IntegerFilter getMaxRetries() {
        return maxRetries;
    }

    public Optional<IntegerFilter> optionalMaxRetries() {
        return Optional.ofNullable(maxRetries);
    }

    public IntegerFilter maxRetries() {
        if (maxRetries == null) {
            setMaxRetries(new IntegerFilter());
        }
        return maxRetries;
    }

    public void setMaxRetries(IntegerFilter maxRetries) {
        this.maxRetries = maxRetries;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public StringFilter getConfirmedBy() {
        return confirmedBy;
    }

    public Optional<StringFilter> optionalConfirmedBy() {
        return Optional.ofNullable(confirmedBy);
    }

    public StringFilter confirmedBy() {
        if (confirmedBy == null) {
            setConfirmedBy(new StringFilter());
        }
        return confirmedBy;
    }

    public void setConfirmedBy(StringFilter confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getConfirmedAt() {
        return confirmedAt;
    }

    public Optional<InstantFilter> optionalConfirmedAt() {
        return Optional.ofNullable(confirmedAt);
    }

    public InstantFilter confirmedAt() {
        if (confirmedAt == null) {
            setConfirmedAt(new InstantFilter());
        }
        return confirmedAt;
    }

    public void setConfirmedAt(InstantFilter confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public InstantFilter getSentAt() {
        return sentAt;
    }

    public Optional<InstantFilter> optionalSentAt() {
        return Optional.ofNullable(sentAt);
    }

    public InstantFilter sentAt() {
        if (sentAt == null) {
            setSentAt(new InstantFilter());
        }
        return sentAt;
    }

    public void setSentAt(InstantFilter sentAt) {
        this.sentAt = sentAt;
    }

    public LongFilter getApplicationId() {
        return applicationId;
    }

    public Optional<LongFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public LongFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new LongFilter());
        }
        return applicationId;
    }

    public void setApplicationId(LongFilter applicationId) {
        this.applicationId = applicationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationMessageCriteria that = (NotificationMessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(sendMode, that.sendMode) &&
            Objects.equals(finalSubject, that.finalSubject) &&
            Objects.equals(retryCount, that.retryCount) &&
            Objects.equals(maxRetries, that.maxRetries) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(confirmedBy, that.confirmedBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(confirmedAt, that.confirmedAt) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            eventType,
            templateId,
            status,
            sendMode,
            finalSubject,
            retryCount,
            maxRetries,
            createdBy,
            confirmedBy,
            createdAt,
            confirmedAt,
            sentAt,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationMessageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalTemplateId().map(f -> "templateId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSendMode().map(f -> "sendMode=" + f + ", ").orElse("") +
            optionalFinalSubject().map(f -> "finalSubject=" + f + ", ").orElse("") +
            optionalRetryCount().map(f -> "retryCount=" + f + ", ").orElse("") +
            optionalMaxRetries().map(f -> "maxRetries=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalConfirmedBy().map(f -> "confirmedBy=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalConfirmedAt().map(f -> "confirmedAt=" + f + ", ").orElse("") +
            optionalSentAt().map(f -> "sentAt=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
