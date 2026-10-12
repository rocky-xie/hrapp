package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/** Append-only audit record for HR locking and reopening a requisition. */
@Entity
@Table(name = "requisition_edit_event")
public class RequisitionEditEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "event_type", length = 20, nullable = false)
    private String eventType;

    @NotNull
    @Size(max = 100)
    @Column(name = "actor", length = 100, nullable = false)
    private String actor;

    @NotNull
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Lob
    @Column(name = "reason")
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "versionses", "memberses", "applicationses" }, allowSetters = true)
    private RecruitmentRequisition requisition;

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

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RecruitmentRequisition getRequisition() {
        return requisition;
    }

    public void setRequisition(RecruitmentRequisition requisition) {
        this.requisition = requisition;
    }

    public RequisitionEditEvent eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public RequisitionEditEvent actor(String actor) {
        this.actor = actor;
        return this;
    }

    public RequisitionEditEvent occurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

    public RequisitionEditEvent versionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
        return this;
    }

    public RequisitionEditEvent reason(String reason) {
        this.reason = reason;
        return this;
    }

    public RequisitionEditEvent requisition(RecruitmentRequisition requisition) {
        this.requisition = requisition;
        return this;
    }
}
