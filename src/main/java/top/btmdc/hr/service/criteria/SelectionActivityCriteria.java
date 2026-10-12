package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.SelectionActivityType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.SelectionActivity} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SelectionActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /selection-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectionActivityCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering SelectionActivityType
     */
    public static class SelectionActivityTypeFilter extends Filter<SelectionActivityType> {

        public SelectionActivityTypeFilter() {}

        public SelectionActivityTypeFilter(SelectionActivityTypeFilter filter) {
            super(filter);
        }

        @Override
        public SelectionActivityTypeFilter copy() {
            return new SelectionActivityTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApplicationStageTypeFilter stageType;

    private SelectionActivityTypeFilter activityType;

    private InstantFilter startAt;

    private InstantFilter endAt;

    private LongFilter meetingRoomId;

    private StringFilter location;

    private StringFilter status;

    private IntegerFilter scheduleVersion;

    private StringFilter externalCalendarProvider;

    private StringFilter externalEventId;

    private LongFilter applicationId;

    private Boolean distinct;

    public SelectionActivityCriteria() {}

    public SelectionActivityCriteria(SelectionActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stageType = other.optionalStageType().map(ApplicationStageTypeFilter::copy).orElse(null);
        this.activityType = other.optionalActivityType().map(SelectionActivityTypeFilter::copy).orElse(null);
        this.startAt = other.optionalStartAt().map(InstantFilter::copy).orElse(null);
        this.endAt = other.optionalEndAt().map(InstantFilter::copy).orElse(null);
        this.meetingRoomId = other.optionalMeetingRoomId().map(LongFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.scheduleVersion = other.optionalScheduleVersion().map(IntegerFilter::copy).orElse(null);
        this.externalCalendarProvider = other.optionalExternalCalendarProvider().map(StringFilter::copy).orElse(null);
        this.externalEventId = other.optionalExternalEventId().map(StringFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SelectionActivityCriteria copy() {
        return new SelectionActivityCriteria(this);
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

    public SelectionActivityTypeFilter getActivityType() {
        return activityType;
    }

    public Optional<SelectionActivityTypeFilter> optionalActivityType() {
        return Optional.ofNullable(activityType);
    }

    public SelectionActivityTypeFilter activityType() {
        if (activityType == null) {
            setActivityType(new SelectionActivityTypeFilter());
        }
        return activityType;
    }

    public void setActivityType(SelectionActivityTypeFilter activityType) {
        this.activityType = activityType;
    }

    public InstantFilter getStartAt() {
        return startAt;
    }

    public Optional<InstantFilter> optionalStartAt() {
        return Optional.ofNullable(startAt);
    }

    public InstantFilter startAt() {
        if (startAt == null) {
            setStartAt(new InstantFilter());
        }
        return startAt;
    }

    public void setStartAt(InstantFilter startAt) {
        this.startAt = startAt;
    }

    public InstantFilter getEndAt() {
        return endAt;
    }

    public Optional<InstantFilter> optionalEndAt() {
        return Optional.ofNullable(endAt);
    }

    public InstantFilter endAt() {
        if (endAt == null) {
            setEndAt(new InstantFilter());
        }
        return endAt;
    }

    public void setEndAt(InstantFilter endAt) {
        this.endAt = endAt;
    }

    public LongFilter getMeetingRoomId() {
        return meetingRoomId;
    }

    public Optional<LongFilter> optionalMeetingRoomId() {
        return Optional.ofNullable(meetingRoomId);
    }

    public LongFilter meetingRoomId() {
        if (meetingRoomId == null) {
            setMeetingRoomId(new LongFilter());
        }
        return meetingRoomId;
    }

    public void setMeetingRoomId(LongFilter meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public StringFilter getLocation() {
        return location;
    }

    public Optional<StringFilter> optionalLocation() {
        return Optional.ofNullable(location);
    }

    public StringFilter location() {
        if (location == null) {
            setLocation(new StringFilter());
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public IntegerFilter getScheduleVersion() {
        return scheduleVersion;
    }

    public Optional<IntegerFilter> optionalScheduleVersion() {
        return Optional.ofNullable(scheduleVersion);
    }

    public IntegerFilter scheduleVersion() {
        if (scheduleVersion == null) {
            setScheduleVersion(new IntegerFilter());
        }
        return scheduleVersion;
    }

    public void setScheduleVersion(IntegerFilter scheduleVersion) {
        this.scheduleVersion = scheduleVersion;
    }

    public StringFilter getExternalCalendarProvider() {
        return externalCalendarProvider;
    }

    public Optional<StringFilter> optionalExternalCalendarProvider() {
        return Optional.ofNullable(externalCalendarProvider);
    }

    public StringFilter externalCalendarProvider() {
        if (externalCalendarProvider == null) {
            setExternalCalendarProvider(new StringFilter());
        }
        return externalCalendarProvider;
    }

    public void setExternalCalendarProvider(StringFilter externalCalendarProvider) {
        this.externalCalendarProvider = externalCalendarProvider;
    }

    public StringFilter getExternalEventId() {
        return externalEventId;
    }

    public Optional<StringFilter> optionalExternalEventId() {
        return Optional.ofNullable(externalEventId);
    }

    public StringFilter externalEventId() {
        if (externalEventId == null) {
            setExternalEventId(new StringFilter());
        }
        return externalEventId;
    }

    public void setExternalEventId(StringFilter externalEventId) {
        this.externalEventId = externalEventId;
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
        final SelectionActivityCriteria that = (SelectionActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stageType, that.stageType) &&
            Objects.equals(activityType, that.activityType) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(meetingRoomId, that.meetingRoomId) &&
            Objects.equals(location, that.location) &&
            Objects.equals(status, that.status) &&
            Objects.equals(scheduleVersion, that.scheduleVersion) &&
            Objects.equals(externalCalendarProvider, that.externalCalendarProvider) &&
            Objects.equals(externalEventId, that.externalEventId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stageType,
            activityType,
            startAt,
            endAt,
            meetingRoomId,
            location,
            status,
            scheduleVersion,
            externalCalendarProvider,
            externalEventId,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectionActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStageType().map(f -> "stageType=" + f + ", ").orElse("") +
            optionalActivityType().map(f -> "activityType=" + f + ", ").orElse("") +
            optionalStartAt().map(f -> "startAt=" + f + ", ").orElse("") +
            optionalEndAt().map(f -> "endAt=" + f + ", ").orElse("") +
            optionalMeetingRoomId().map(f -> "meetingRoomId=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalScheduleVersion().map(f -> "scheduleVersion=" + f + ", ").orElse("") +
            optionalExternalCalendarProvider().map(f -> "externalCalendarProvider=" + f + ", ").orElse("") +
            optionalExternalEventId().map(f -> "externalEventId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
