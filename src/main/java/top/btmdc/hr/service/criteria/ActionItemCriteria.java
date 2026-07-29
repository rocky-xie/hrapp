package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.ActionItem} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionItemCriteria implements Serializable, Criteria {

    public static class ActionSourceTypeFilter extends Filter<ActionSourceType> {

        public ActionSourceTypeFilter() {}

        public ActionSourceTypeFilter(ActionSourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActionSourceTypeFilter copy() {
            return new ActionSourceTypeFilter(this);
        }
    }

    public static class ActionStatusFilter extends Filter<ActionStatus> {

        public ActionStatusFilter() {}

        public ActionStatusFilter(ActionStatusFilter filter) {
            super(filter);
        }

        @Override
        public ActionStatusFilter copy() {
            return new ActionStatusFilter(this);
        }
    }

    public static class ActionPriorityFilter extends Filter<ActionPriority> {

        public ActionPriorityFilter() {}

        public ActionPriorityFilter(ActionPriorityFilter filter) {
            super(filter);
        }

        @Override
        public ActionPriorityFilter copy() {
            return new ActionPriorityFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ActionSourceTypeFilter sourceType;

    private LongFilter sourceId;

    private StringFilter sourceEntityType;

    private StringFilter description;

    private StringFilter assignee;

    private LocalDateFilter dueDate;

    private ActionStatusFilter status;

    private ActionPriorityFilter priority;

    private LocalDateFilter createdAt;

    private LocalDateFilter completedAt;

    private Boolean distinct;

    public ActionItemCriteria() {}

    public ActionItemCriteria(ActionItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sourceType = other.optionalSourceType().map(ActionSourceTypeFilter::copy).orElse(null);
        this.sourceId = other.optionalSourceId().map(LongFilter::copy).orElse(null);
        this.sourceEntityType = other.optionalSourceEntityType().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.assignee = other.optionalAssignee().map(StringFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(LocalDateFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ActionStatusFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(ActionPriorityFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ActionItemCriteria copy() {
        return new ActionItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) setId(new LongFilter());
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ActionSourceTypeFilter getSourceType() {
        return sourceType;
    }

    public Optional<ActionSourceTypeFilter> optionalSourceType() {
        return Optional.ofNullable(sourceType);
    }

    public ActionSourceTypeFilter sourceType() {
        if (sourceType == null) setSourceType(new ActionSourceTypeFilter());
        return sourceType;
    }

    public void setSourceType(ActionSourceTypeFilter sourceType) {
        this.sourceType = sourceType;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public Optional<LongFilter> optionalSourceId() {
        return Optional.ofNullable(sourceId);
    }

    public LongFilter sourceId() {
        if (sourceId == null) setSourceId(new LongFilter());
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
    }

    public StringFilter getSourceEntityType() {
        return sourceEntityType;
    }

    public Optional<StringFilter> optionalSourceEntityType() {
        return Optional.ofNullable(sourceEntityType);
    }

    public StringFilter sourceEntityType() {
        if (sourceEntityType == null) setSourceEntityType(new StringFilter());
        return sourceEntityType;
    }

    public void setSourceEntityType(StringFilter sourceEntityType) {
        this.sourceEntityType = sourceEntityType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) setDescription(new StringFilter());
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getAssignee() {
        return assignee;
    }

    public Optional<StringFilter> optionalAssignee() {
        return Optional.ofNullable(assignee);
    }

    public StringFilter assignee() {
        if (assignee == null) setAssignee(new StringFilter());
        return assignee;
    }

    public void setAssignee(StringFilter assignee) {
        this.assignee = assignee;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public Optional<LocalDateFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public LocalDateFilter dueDate() {
        if (dueDate == null) setDueDate(new LocalDateFilter());
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public ActionStatusFilter getStatus() {
        return status;
    }

    public Optional<ActionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ActionStatusFilter status() {
        if (status == null) setStatus(new ActionStatusFilter());
        return status;
    }

    public void setStatus(ActionStatusFilter status) {
        this.status = status;
    }

    public ActionPriorityFilter getPriority() {
        return priority;
    }

    public Optional<ActionPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public ActionPriorityFilter priority() {
        if (priority == null) setPriority(new ActionPriorityFilter());
        return priority;
    }

    public void setPriority(ActionPriorityFilter priority) {
        this.priority = priority;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) setCreatedAt(new LocalDateFilter());
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<LocalDateFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public LocalDateFilter completedAt() {
        if (completedAt == null) setCompletedAt(new LocalDateFilter());
        return completedAt;
    }

    public void setCompletedAt(LocalDateFilter completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) setDistinct(true);
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ActionItemCriteria that = (ActionItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceType, that.sourceType) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(sourceEntityType, that.sourceEntityType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(assignee, that.assignee) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sourceType,
            sourceId,
            sourceEntityType,
            description,
            assignee,
            dueDate,
            status,
            priority,
            createdAt,
            completedAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "ActionItemCriteria{" +
            optionalId()
                .map(f -> "id=" + f + ", ")
                .orElse("") +
            optionalSourceType()
                .map(f -> "sourceType=" + f + ", ")
                .orElse("") +
            optionalSourceId()
                .map(f -> "sourceId=" + f + ", ")
                .orElse("") +
            optionalSourceEntityType()
                .map(f -> "sourceEntityType=" + f + ", ")
                .orElse("") +
            optionalDescription()
                .map(f -> "description=" + f + ", ")
                .orElse("") +
            optionalAssignee()
                .map(f -> "assignee=" + f + ", ")
                .orElse("") +
            optionalDueDate()
                .map(f -> "dueDate=" + f + ", ")
                .orElse("") +
            optionalStatus()
                .map(f -> "status=" + f + ", ")
                .orElse("") +
            optionalPriority()
                .map(f -> "priority=" + f + ", ")
                .orElse("") +
            optionalCreatedAt()
                .map(f -> "createdAt=" + f + ", ")
                .orElse("") +
            optionalCompletedAt()
                .map(f -> "completedAt=" + f + ", ")
                .orElse("") +
            optionalDistinct()
                .map(f -> "distinct=" + f + ", ")
                .orElse("") +
            "}"
        );
    }
}
