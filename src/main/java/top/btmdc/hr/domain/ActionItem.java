package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;

@Entity
@Table(name = "action_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private ActionSourceType sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @NotNull
    @Size(max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Size(max = 100)
    @Column(name = "assignee", length = 100)
    private String assignee;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ActionPriority priority;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "completed_at")
    private LocalDate completedAt;

    @Lob
    @Column(name = "note")
    private String note;

    public Long getId() {
        return this.id;
    }

    public ActionItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionSourceType getSourceType() {
        return this.sourceType;
    }

    public ActionItem sourceType(ActionSourceType sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(ActionSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public ActionItem sourceId(Long sourceId) {
        this.setSourceId(sourceId);
        return this;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDescription() {
        return this.description;
    }

    public ActionItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public ActionItem assignee(String assignee) {
        this.setAssignee(assignee);
        return this;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public ActionItem dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public ActionStatus getStatus() {
        return this.status;
    }

    public ActionItem status(ActionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public ActionPriority getPriority() {
        return this.priority;
    }

    public ActionItem priority(ActionPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(ActionPriority priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ActionItem createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCompletedAt() {
        return this.completedAt;
    }

    public ActionItem completedAt(LocalDate completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public String getNote() {
        return this.note;
    }

    public ActionItem note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionItem)) return false;
        return getId() != null && getId().equals(((ActionItem) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "ActionItem{" +
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
