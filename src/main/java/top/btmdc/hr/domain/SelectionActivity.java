package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.SelectionActivityType;

/**
 * A SelectionActivity.
 */
@Entity
@Table(name = "selection_activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SelectionActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false)
    private ApplicationStageType stageType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private SelectionActivityType activityType;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Column(name = "meeting_room_id")
    private Long meetingRoomId;

    @Size(max = 255)
    @Column(name = "location", length = 255)
    private String location;

    @NotNull
    @Size(max = 30)
    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @NotNull
    @Min(value = 1)
    @Column(name = "schedule_version", nullable = false)
    private Integer scheduleVersion;

    @Lob
    @Column(name = "change_reason")
    private String changeReason;

    @Size(max = 50)
    @Column(name = "external_calendar_provider", length = 50)
    private String externalCalendarProvider;

    @Size(max = 255)
    @Column(name = "external_event_id", length = 255)
    private String externalEventId;

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

    public SelectionActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStageType getStageType() {
        return this.stageType;
    }

    public SelectionActivity stageType(ApplicationStageType stageType) {
        this.setStageType(stageType);
        return this;
    }

    public void setStageType(ApplicationStageType stageType) {
        this.stageType = stageType;
    }

    public SelectionActivityType getActivityType() {
        return this.activityType;
    }

    public SelectionActivity activityType(SelectionActivityType activityType) {
        this.setActivityType(activityType);
        return this;
    }

    public void setActivityType(SelectionActivityType activityType) {
        this.activityType = activityType;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public SelectionActivity startAt(Instant startAt) {
        this.setStartAt(startAt);
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public SelectionActivity endAt(Instant endAt) {
        this.setEndAt(endAt);
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public Long getMeetingRoomId() {
        return this.meetingRoomId;
    }

    public SelectionActivity meetingRoomId(Long meetingRoomId) {
        this.setMeetingRoomId(meetingRoomId);
        return this;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getLocation() {
        return this.location;
    }

    public SelectionActivity location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return this.status;
    }

    public SelectionActivity status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScheduleVersion() {
        return this.scheduleVersion;
    }

    public SelectionActivity scheduleVersion(Integer scheduleVersion) {
        this.setScheduleVersion(scheduleVersion);
        return this;
    }

    public void setScheduleVersion(Integer scheduleVersion) {
        this.scheduleVersion = scheduleVersion;
    }

    public String getChangeReason() {
        return this.changeReason;
    }

    public SelectionActivity changeReason(String changeReason) {
        this.setChangeReason(changeReason);
        return this;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getExternalCalendarProvider() {
        return this.externalCalendarProvider;
    }

    public SelectionActivity externalCalendarProvider(String externalCalendarProvider) {
        this.setExternalCalendarProvider(externalCalendarProvider);
        return this;
    }

    public void setExternalCalendarProvider(String externalCalendarProvider) {
        this.externalCalendarProvider = externalCalendarProvider;
    }

    public String getExternalEventId() {
        return this.externalEventId;
    }

    public SelectionActivity externalEventId(String externalEventId) {
        this.setExternalEventId(externalEventId);
        return this;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public SelectionActivity application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectionActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((SelectionActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelectionActivity{" +
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
            "}";
    }
}
