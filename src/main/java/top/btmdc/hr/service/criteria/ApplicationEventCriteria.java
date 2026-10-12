package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.ApplicationEvent} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.ApplicationEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationEventCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ApplicationStageType
     */
    public static class ApplicationStageTypeFilter extends Filter<ApplicationStageType> {

        public ApplicationStageTypeFilter() {}

        public ApplicationStageTypeFilter(ApplicationStageTypeFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationStageTypeFilter copy() {
            return new ApplicationStageTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApplicationStageTypeFilter stageType;

    private StringFilter eventType;

    private StringFilter previousValue;

    private StringFilter newValue;

    private StringFilter actor;

    private InstantFilter occurredAt;

    private LongFilter applicationId;

    private Boolean distinct;

    public ApplicationEventCriteria() {}

    public ApplicationEventCriteria(ApplicationEventCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stageType = other.optionalStageType().map(ApplicationStageTypeFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(StringFilter::copy).orElse(null);
        this.previousValue = other.optionalPreviousValue().map(StringFilter::copy).orElse(null);
        this.newValue = other.optionalNewValue().map(StringFilter::copy).orElse(null);
        this.actor = other.optionalActor().map(StringFilter::copy).orElse(null);
        this.occurredAt = other.optionalOccurredAt().map(InstantFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationEventCriteria copy() {
        return new ApplicationEventCriteria(this);
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

    public ApplicationStageTypeFilter getStageType() {
        return stageType;
    }

    public Optional<ApplicationStageTypeFilter> optionalStageType() {
        return Optional.ofNullable(stageType);
    }

    public ApplicationStageTypeFilter stageType() {
        if (stageType == null) {
            setStageType(new ApplicationStageTypeFilter());
        }
        return stageType;
    }

    public void setStageType(ApplicationStageTypeFilter stageType) {
        this.stageType = stageType;
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

    public StringFilter getPreviousValue() {
        return previousValue;
    }

    public Optional<StringFilter> optionalPreviousValue() {
        return Optional.ofNullable(previousValue);
    }

    public StringFilter previousValue() {
        if (previousValue == null) {
            setPreviousValue(new StringFilter());
        }
        return previousValue;
    }

    public void setPreviousValue(StringFilter previousValue) {
        this.previousValue = previousValue;
    }

    public StringFilter getNewValue() {
        return newValue;
    }

    public Optional<StringFilter> optionalNewValue() {
        return Optional.ofNullable(newValue);
    }

    public StringFilter newValue() {
        if (newValue == null) {
            setNewValue(new StringFilter());
        }
        return newValue;
    }

    public void setNewValue(StringFilter newValue) {
        this.newValue = newValue;
    }

    public StringFilter getActor() {
        return actor;
    }

    public Optional<StringFilter> optionalActor() {
        return Optional.ofNullable(actor);
    }

    public StringFilter actor() {
        if (actor == null) {
            setActor(new StringFilter());
        }
        return actor;
    }

    public void setActor(StringFilter actor) {
        this.actor = actor;
    }

    public InstantFilter getOccurredAt() {
        return occurredAt;
    }

    public Optional<InstantFilter> optionalOccurredAt() {
        return Optional.ofNullable(occurredAt);
    }

    public InstantFilter occurredAt() {
        if (occurredAt == null) {
            setOccurredAt(new InstantFilter());
        }
        return occurredAt;
    }

    public void setOccurredAt(InstantFilter occurredAt) {
        this.occurredAt = occurredAt;
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
        final ApplicationEventCriteria that = (ApplicationEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stageType, that.stageType) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(previousValue, that.previousValue) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(actor, that.actor) &&
            Objects.equals(occurredAt, that.occurredAt) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageType, eventType, previousValue, newValue, actor, occurredAt, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationEventCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStageType().map(f -> "stageType=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalPreviousValue().map(f -> "previousValue=" + f + ", ").orElse("") +
            optionalNewValue().map(f -> "newValue=" + f + ", ").orElse("") +
            optionalActor().map(f -> "actor=" + f + ", ").orElse("") +
            optionalOccurredAt().map(f -> "occurredAt=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
