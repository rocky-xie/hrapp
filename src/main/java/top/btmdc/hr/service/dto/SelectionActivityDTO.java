package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.SelectionActivityType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.SelectionActivity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectionActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private ApplicationStageType stageType;

    @NotNull
    private SelectionActivityType activityType;

    @NotNull
    private Instant startAt;

    @NotNull
    private Instant endAt;

    private Long meetingRoomId;

    @Size(max = 255)
    private String location;

    @NotNull
    @Size(max = 30)
    private String status;

    @NotNull
    @Min(value = 1)
    private Integer scheduleVersion;

    @Lob
    private String changeReason;

    @Size(max = 50)
    private String externalCalendarProvider;

    @Size(max = 255)
    private String externalEventId;

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

    public SelectionActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(SelectionActivityType activityType) {
        this.activityType = activityType;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScheduleVersion() {
        return scheduleVersion;
    }

    public void setScheduleVersion(Integer scheduleVersion) {
        this.scheduleVersion = scheduleVersion;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getExternalCalendarProvider() {
        return externalCalendarProvider;
    }

    public void setExternalCalendarProvider(String externalCalendarProvider) {
        this.externalCalendarProvider = externalCalendarProvider;
    }

    public String getExternalEventId() {
        return externalEventId;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
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
        if (!(o instanceof SelectionActivityDTO)) {
            return false;
        }

        SelectionActivityDTO selectionActivityDTO = (SelectionActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, selectionActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectionActivityDTO{" +
            "id=" + getId() +
            ", stageType='" + getStageType() + "'" +
            ", activityType='" + getActivityType() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", meetingRoomId=" + getMeetingRoomId() +
            ", location='" + getLocation() + "'" +
            ", status='" + getStatus() + "'" +
            ", scheduleVersion=" + getScheduleVersion() +
            ", changeReason='" + getChangeReason() + "'" +
            ", externalCalendarProvider='" + getExternalCalendarProvider() + "'" +
            ", externalEventId='" + getExternalEventId() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
