package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionItemDTO implements Serializable {

    private Long id;

    private ActionSourceType sourceType;

    private Long sourceId;

    @Size(max = 100)
    private String sourceEntityType;

    @NotNull
    @Size(max = 200)
    private String description;

    @Size(max = 100)
    private String assignee;

    private LocalDate dueDate;

    private ActionStatus status;

    private ActionPriority priority;

    private LocalDate createdAt;

    private LocalDate completedAt;

    @Lob
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ActionSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(String sourceEntityType) {
        this.sourceEntityType = sourceEntityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public ActionPriority getPriority() {
        return priority;
    }

    public void setPriority(ActionPriority priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionItemDTO)) return false;
        ActionItemDTO that = (ActionItemDTO) o;
        if (this.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "ActionItemDTO{" +
            "id=" +
            getId() +
            ", sourceType='" +
            getSourceType() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", assignee='" +
            getAssignee() +
            "'" +
            ", dueDate='" +
            getDueDate() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", priority='" +
            getPriority() +
            "'" +
            "}"
        );
    }
}
