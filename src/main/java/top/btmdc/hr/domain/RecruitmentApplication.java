package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * A RecruitmentApplication.
 */
@Entity
@Table(name = "recruitment_application")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "application_code", length = 40, nullable = false, unique = true)
    private String applicationCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_type", nullable = false)
    private RecruitmentType recruitmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_stage")
    private ApplicationStageType currentStage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private ApplicationDecision decision;

    @NotNull
    @Size(max = 40)
    @Column(name = "source_type", length = 40, nullable = false)
    private String sourceType;

    @Size(max = 255)
    @Column(name = "source_detail", length = 255)
    private String sourceDetail;

    @Size(max = 150)
    @Column(name = "batch_name", length = 150)
    private String batchName;

    @NotNull
    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "retention_until")
    private LocalDate retentionUntil;

    @Column(name = "converted_person_id")
    private Long convertedPersonId;

    @Column(name = "converted_at")
    private Instant convertedAt;

    @Lob
    @Column(name = "close_reason")
    private String closeReason;

    @Lob
    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<ApplicationStage> stageses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<ApplicationEvent> eventses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<SelectionActivity> activitieses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<RecruitmentEvaluation> evaluationses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<HiringDecision> decisionses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<NotificationMessage> messageses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<PreboardingFollowUp> followUpses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @JsonIgnoreProperties(value = { "reviewses", "application" }, allowSetters = true)
    private Set<DispatchAssignment> dispatchAssignmentses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "resumeses", "applicationses" }, allowSetters = true)
    private ExternalCandidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "versionses", "memberses", "applicationses" }, allowSetters = true)
    private RecruitmentRequisition requisition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "applicationses", "requisition" }, allowSetters = true)
    private RecruitmentRequisitionVersion requisitionVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationCode() {
        return this.applicationCode;
    }

    public RecruitmentApplication applicationCode(String applicationCode) {
        this.setApplicationCode(applicationCode);
        return this;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public RecruitmentType getRecruitmentType() {
        return this.recruitmentType;
    }

    public RecruitmentApplication recruitmentType(RecruitmentType recruitmentType) {
        this.setRecruitmentType(recruitmentType);
        return this;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public ApplicationStageType getCurrentStage() {
        return this.currentStage;
    }

    public RecruitmentApplication currentStage(ApplicationStageType currentStage) {
        this.setCurrentStage(currentStage);
        return this;
    }

    public void setCurrentStage(ApplicationStageType currentStage) {
        this.currentStage = currentStage;
    }

    public ApplicationStatus getStatus() {
        return this.status;
    }

    public RecruitmentApplication status(ApplicationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationDecision getDecision() {
        return this.decision;
    }

    public RecruitmentApplication decision(ApplicationDecision decision) {
        this.setDecision(decision);
        return this;
    }

    public void setDecision(ApplicationDecision decision) {
        this.decision = decision;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public RecruitmentApplication sourceType(String sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceDetail() {
        return this.sourceDetail;
    }

    public RecruitmentApplication sourceDetail(String sourceDetail) {
        this.setSourceDetail(sourceDetail);
        return this;
    }

    public void setSourceDetail(String sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public RecruitmentApplication batchName(String batchName) {
        this.setBatchName(batchName);
        return this;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Instant getAppliedAt() {
        return this.appliedAt;
    }

    public RecruitmentApplication appliedAt(Instant appliedAt) {
        this.setAppliedAt(appliedAt);
        return this;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public RecruitmentApplication closedAt(Instant closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDate getRetentionUntil() {
        return this.retentionUntil;
    }

    public RecruitmentApplication retentionUntil(LocalDate retentionUntil) {
        this.setRetentionUntil(retentionUntil);
        return this;
    }

    public void setRetentionUntil(LocalDate retentionUntil) {
        this.retentionUntil = retentionUntil;
    }

    public Long getConvertedPersonId() {
        return this.convertedPersonId;
    }

    public RecruitmentApplication convertedPersonId(Long convertedPersonId) {
        this.setConvertedPersonId(convertedPersonId);
        return this;
    }

    public void setConvertedPersonId(Long convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
    }

    public Instant getConvertedAt() {
        return this.convertedAt;
    }

    public RecruitmentApplication convertedAt(Instant convertedAt) {
        this.setConvertedAt(convertedAt);
        return this;
    }

    public void setConvertedAt(Instant convertedAt) {
        this.convertedAt = convertedAt;
    }

    public String getCloseReason() {
        return this.closeReason;
    }

    public RecruitmentApplication closeReason(String closeReason) {
        this.setCloseReason(closeReason);
        return this;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public String getNote() {
        return this.note;
    }

    public RecruitmentApplication note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<ApplicationStage> getStageses() {
        return this.stageses;
    }

    public void setStageses(Set<ApplicationStage> applicationStages) {
        if (this.stageses != null) {
            this.stageses.forEach(i -> i.setApplication(null));
        }
        if (applicationStages != null) {
            applicationStages.forEach(i -> i.setApplication(this));
        }
        this.stageses = applicationStages;
    }

    public RecruitmentApplication stageses(Set<ApplicationStage> applicationStages) {
        this.setStageses(applicationStages);
        return this;
    }

    public RecruitmentApplication addStages(ApplicationStage applicationStage) {
        this.stageses.add(applicationStage);
        applicationStage.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeStages(ApplicationStage applicationStage) {
        this.stageses.remove(applicationStage);
        applicationStage.setApplication(null);
        return this;
    }

    public Set<ApplicationEvent> getEventses() {
        return this.eventses;
    }

    public void setEventses(Set<ApplicationEvent> applicationEvents) {
        if (this.eventses != null) {
            this.eventses.forEach(i -> i.setApplication(null));
        }
        if (applicationEvents != null) {
            applicationEvents.forEach(i -> i.setApplication(this));
        }
        this.eventses = applicationEvents;
    }

    public RecruitmentApplication eventses(Set<ApplicationEvent> applicationEvents) {
        this.setEventses(applicationEvents);
        return this;
    }

    public RecruitmentApplication addEvents(ApplicationEvent applicationEvent) {
        this.eventses.add(applicationEvent);
        applicationEvent.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeEvents(ApplicationEvent applicationEvent) {
        this.eventses.remove(applicationEvent);
        applicationEvent.setApplication(null);
        return this;
    }

    public Set<SelectionActivity> getActivitieses() {
        return this.activitieses;
    }

    public void setActivitieses(Set<SelectionActivity> selectionActivities) {
        if (this.activitieses != null) {
            this.activitieses.forEach(i -> i.setApplication(null));
        }
        if (selectionActivities != null) {
            selectionActivities.forEach(i -> i.setApplication(this));
        }
        this.activitieses = selectionActivities;
    }

    public RecruitmentApplication activitieses(Set<SelectionActivity> selectionActivities) {
        this.setActivitieses(selectionActivities);
        return this;
    }

    public RecruitmentApplication addActivities(SelectionActivity selectionActivity) {
        this.activitieses.add(selectionActivity);
        selectionActivity.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeActivities(SelectionActivity selectionActivity) {
        this.activitieses.remove(selectionActivity);
        selectionActivity.setApplication(null);
        return this;
    }

    public Set<RecruitmentEvaluation> getEvaluationses() {
        return this.evaluationses;
    }

    public void setEvaluationses(Set<RecruitmentEvaluation> recruitmentEvaluations) {
        if (this.evaluationses != null) {
            this.evaluationses.forEach(i -> i.setApplication(null));
        }
        if (recruitmentEvaluations != null) {
            recruitmentEvaluations.forEach(i -> i.setApplication(this));
        }
        this.evaluationses = recruitmentEvaluations;
    }

    public RecruitmentApplication evaluationses(Set<RecruitmentEvaluation> recruitmentEvaluations) {
        this.setEvaluationses(recruitmentEvaluations);
        return this;
    }

    public RecruitmentApplication addEvaluations(RecruitmentEvaluation recruitmentEvaluation) {
        this.evaluationses.add(recruitmentEvaluation);
        recruitmentEvaluation.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeEvaluations(RecruitmentEvaluation recruitmentEvaluation) {
        this.evaluationses.remove(recruitmentEvaluation);
        recruitmentEvaluation.setApplication(null);
        return this;
    }

    public Set<HiringDecision> getDecisionses() {
        return this.decisionses;
    }

    public void setDecisionses(Set<HiringDecision> hiringDecisions) {
        if (this.decisionses != null) {
            this.decisionses.forEach(i -> i.setApplication(null));
        }
        if (hiringDecisions != null) {
            hiringDecisions.forEach(i -> i.setApplication(this));
        }
        this.decisionses = hiringDecisions;
    }

    public RecruitmentApplication decisionses(Set<HiringDecision> hiringDecisions) {
        this.setDecisionses(hiringDecisions);
        return this;
    }

    public RecruitmentApplication addDecisions(HiringDecision hiringDecision) {
        this.decisionses.add(hiringDecision);
        hiringDecision.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeDecisions(HiringDecision hiringDecision) {
        this.decisionses.remove(hiringDecision);
        hiringDecision.setApplication(null);
        return this;
    }

    public Set<NotificationMessage> getMessageses() {
        return this.messageses;
    }

    public void setMessageses(Set<NotificationMessage> notificationMessages) {
        if (this.messageses != null) {
            this.messageses.forEach(i -> i.setApplication(null));
        }
        if (notificationMessages != null) {
            notificationMessages.forEach(i -> i.setApplication(this));
        }
        this.messageses = notificationMessages;
    }

    public RecruitmentApplication messageses(Set<NotificationMessage> notificationMessages) {
        this.setMessageses(notificationMessages);
        return this;
    }

    public RecruitmentApplication addMessages(NotificationMessage notificationMessage) {
        this.messageses.add(notificationMessage);
        notificationMessage.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeMessages(NotificationMessage notificationMessage) {
        this.messageses.remove(notificationMessage);
        notificationMessage.setApplication(null);
        return this;
    }

    public Set<PreboardingFollowUp> getFollowUpses() {
        return this.followUpses;
    }

    public void setFollowUpses(Set<PreboardingFollowUp> preboardingFollowUps) {
        if (this.followUpses != null) {
            this.followUpses.forEach(i -> i.setApplication(null));
        }
        if (preboardingFollowUps != null) {
            preboardingFollowUps.forEach(i -> i.setApplication(this));
        }
        this.followUpses = preboardingFollowUps;
    }

    public RecruitmentApplication followUpses(Set<PreboardingFollowUp> preboardingFollowUps) {
        this.setFollowUpses(preboardingFollowUps);
        return this;
    }

    public RecruitmentApplication addFollowUps(PreboardingFollowUp preboardingFollowUp) {
        this.followUpses.add(preboardingFollowUp);
        preboardingFollowUp.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeFollowUps(PreboardingFollowUp preboardingFollowUp) {
        this.followUpses.remove(preboardingFollowUp);
        preboardingFollowUp.setApplication(null);
        return this;
    }

    public Set<DispatchAssignment> getDispatchAssignmentses() {
        return this.dispatchAssignmentses;
    }

    public void setDispatchAssignmentses(Set<DispatchAssignment> dispatchAssignments) {
        if (this.dispatchAssignmentses != null) {
            this.dispatchAssignmentses.forEach(i -> i.setApplication(null));
        }
        if (dispatchAssignments != null) {
            dispatchAssignments.forEach(i -> i.setApplication(this));
        }
        this.dispatchAssignmentses = dispatchAssignments;
    }

    public RecruitmentApplication dispatchAssignmentses(Set<DispatchAssignment> dispatchAssignments) {
        this.setDispatchAssignmentses(dispatchAssignments);
        return this;
    }

    public RecruitmentApplication addDispatchAssignments(DispatchAssignment dispatchAssignment) {
        this.dispatchAssignmentses.add(dispatchAssignment);
        dispatchAssignment.setApplication(this);
        return this;
    }

    public RecruitmentApplication removeDispatchAssignments(DispatchAssignment dispatchAssignment) {
        this.dispatchAssignmentses.remove(dispatchAssignment);
        dispatchAssignment.setApplication(null);
        return this;
    }

    public ExternalCandidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(ExternalCandidate externalCandidate) {
        this.candidate = externalCandidate;
    }

    public RecruitmentApplication candidate(ExternalCandidate externalCandidate) {
        this.setCandidate(externalCandidate);
        return this;
    }

    public RecruitmentRequisition getRequisition() {
        return this.requisition;
    }

    public void setRequisition(RecruitmentRequisition recruitmentRequisition) {
        this.requisition = recruitmentRequisition;
    }

    public RecruitmentApplication requisition(RecruitmentRequisition recruitmentRequisition) {
        this.setRequisition(recruitmentRequisition);
        return this;
    }

    public RecruitmentRequisitionVersion getRequisitionVersion() {
        return this.requisitionVersion;
    }

    public void setRequisitionVersion(RecruitmentRequisitionVersion recruitmentRequisitionVersion) {
        this.requisitionVersion = recruitmentRequisitionVersion;
    }

    public RecruitmentApplication requisitionVersion(RecruitmentRequisitionVersion recruitmentRequisitionVersion) {
        this.setRequisitionVersion(recruitmentRequisitionVersion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentApplication)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentApplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentApplication{" +
            "id=" + getId() +
            ", applicationCode='" + getApplicationCode() + "'" +
            ", recruitmentType='" + getRecruitmentType() + "'" +
            ", currentStage='" + getCurrentStage() + "'" +
            ", status='" + getStatus() + "'" +
            ", decision='" + getDecision() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", sourceDetail='" + getSourceDetail() + "'" +
            ", batchName='" + getBatchName() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", retentionUntil='" + getRetentionUntil() + "'" +
            ", convertedPersonId=" + getConvertedPersonId() +
            ", convertedAt='" + getConvertedAt() + "'" +
            ", closeReason='" + getCloseReason() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
