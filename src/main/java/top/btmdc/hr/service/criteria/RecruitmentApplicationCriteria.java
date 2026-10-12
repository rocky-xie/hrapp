package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.RecruitmentApplication} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.RecruitmentApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recruitment-applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentApplicationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RecruitmentType
     */
    public static class RecruitmentTypeFilter extends Filter<RecruitmentType> {

        public RecruitmentTypeFilter() {}

        public RecruitmentTypeFilter(RecruitmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public RecruitmentTypeFilter copy() {
            return new RecruitmentTypeFilter(this);
        }
    }

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
     * Class for filtering ApplicationStatus
     */
    public static class ApplicationStatusFilter extends Filter<ApplicationStatus> {

        public ApplicationStatusFilter() {}

        public ApplicationStatusFilter(ApplicationStatusFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationStatusFilter copy() {
            return new ApplicationStatusFilter(this);
        }
    }

    /**
     * Class for filtering ApplicationDecision
     */
    public static class ApplicationDecisionFilter extends Filter<ApplicationDecision> {

        public ApplicationDecisionFilter() {}

        public ApplicationDecisionFilter(ApplicationDecisionFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationDecisionFilter copy() {
            return new ApplicationDecisionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter applicationCode;

    private RecruitmentTypeFilter recruitmentType;

    private ApplicationStageTypeFilter currentStage;

    private ApplicationStatusFilter status;

    private ApplicationDecisionFilter decision;

    private StringFilter sourceType;

    private StringFilter sourceDetail;

    private StringFilter batchName;

    private InstantFilter appliedAt;

    private InstantFilter closedAt;

    private LocalDateFilter retentionUntil;

    private LongFilter convertedPersonId;

    private InstantFilter convertedAt;

    private LongFilter stagesId;

    private LongFilter eventsId;

    private LongFilter activitiesId;

    private LongFilter evaluationsId;

    private LongFilter decisionsId;

    private LongFilter messagesId;

    private LongFilter followUpsId;

    private LongFilter dispatchAssignmentsId;

    private LongFilter candidateId;

    private LongFilter requisitionId;

    private LongFilter requisitionVersionId;

    private Boolean distinct;

    public RecruitmentApplicationCriteria() {}

    public RecruitmentApplicationCriteria(RecruitmentApplicationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.applicationCode = other.optionalApplicationCode().map(StringFilter::copy).orElse(null);
        this.recruitmentType = other.optionalRecruitmentType().map(RecruitmentTypeFilter::copy).orElse(null);
        this.currentStage = other.optionalCurrentStage().map(ApplicationStageTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ApplicationStatusFilter::copy).orElse(null);
        this.decision = other.optionalDecision().map(ApplicationDecisionFilter::copy).orElse(null);
        this.sourceType = other.optionalSourceType().map(StringFilter::copy).orElse(null);
        this.sourceDetail = other.optionalSourceDetail().map(StringFilter::copy).orElse(null);
        this.batchName = other.optionalBatchName().map(StringFilter::copy).orElse(null);
        this.appliedAt = other.optionalAppliedAt().map(InstantFilter::copy).orElse(null);
        this.closedAt = other.optionalClosedAt().map(InstantFilter::copy).orElse(null);
        this.retentionUntil = other.optionalRetentionUntil().map(LocalDateFilter::copy).orElse(null);
        this.convertedPersonId = other.optionalConvertedPersonId().map(LongFilter::copy).orElse(null);
        this.convertedAt = other.optionalConvertedAt().map(InstantFilter::copy).orElse(null);
        this.stagesId = other.optionalStagesId().map(LongFilter::copy).orElse(null);
        this.eventsId = other.optionalEventsId().map(LongFilter::copy).orElse(null);
        this.activitiesId = other.optionalActivitiesId().map(LongFilter::copy).orElse(null);
        this.evaluationsId = other.optionalEvaluationsId().map(LongFilter::copy).orElse(null);
        this.decisionsId = other.optionalDecisionsId().map(LongFilter::copy).orElse(null);
        this.messagesId = other.optionalMessagesId().map(LongFilter::copy).orElse(null);
        this.followUpsId = other.optionalFollowUpsId().map(LongFilter::copy).orElse(null);
        this.dispatchAssignmentsId = other.optionalDispatchAssignmentsId().map(LongFilter::copy).orElse(null);
        this.candidateId = other.optionalCandidateId().map(LongFilter::copy).orElse(null);
        this.requisitionId = other.optionalRequisitionId().map(LongFilter::copy).orElse(null);
        this.requisitionVersionId = other.optionalRequisitionVersionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecruitmentApplicationCriteria copy() {
        return new RecruitmentApplicationCriteria(this);
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

    public StringFilter getApplicationCode() {
        return applicationCode;
    }

    public Optional<StringFilter> optionalApplicationCode() {
        return Optional.ofNullable(applicationCode);
    }

    public StringFilter applicationCode() {
        if (applicationCode == null) {
            setApplicationCode(new StringFilter());
        }
        return applicationCode;
    }

    public void setApplicationCode(StringFilter applicationCode) {
        this.applicationCode = applicationCode;
    }

    public RecruitmentTypeFilter getRecruitmentType() {
        return recruitmentType;
    }

    public Optional<RecruitmentTypeFilter> optionalRecruitmentType() {
        return Optional.ofNullable(recruitmentType);
    }

    public RecruitmentTypeFilter recruitmentType() {
        if (recruitmentType == null) {
            setRecruitmentType(new RecruitmentTypeFilter());
        }
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentTypeFilter recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public ApplicationStageTypeFilter getCurrentStage() {
        return currentStage;
    }

    public Optional<ApplicationStageTypeFilter> optionalCurrentStage() {
        return Optional.ofNullable(currentStage);
    }

    public ApplicationStageTypeFilter currentStage() {
        if (currentStage == null) {
            setCurrentStage(new ApplicationStageTypeFilter());
        }
        return currentStage;
    }

    public void setCurrentStage(ApplicationStageTypeFilter currentStage) {
        this.currentStage = currentStage;
    }

    public ApplicationStatusFilter getStatus() {
        return status;
    }

    public Optional<ApplicationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ApplicationStatusFilter status() {
        if (status == null) {
            setStatus(new ApplicationStatusFilter());
        }
        return status;
    }

    public void setStatus(ApplicationStatusFilter status) {
        this.status = status;
    }

    public ApplicationDecisionFilter getDecision() {
        return decision;
    }

    public Optional<ApplicationDecisionFilter> optionalDecision() {
        return Optional.ofNullable(decision);
    }

    public ApplicationDecisionFilter decision() {
        if (decision == null) {
            setDecision(new ApplicationDecisionFilter());
        }
        return decision;
    }

    public void setDecision(ApplicationDecisionFilter decision) {
        this.decision = decision;
    }

    public StringFilter getSourceType() {
        return sourceType;
    }

    public Optional<StringFilter> optionalSourceType() {
        return Optional.ofNullable(sourceType);
    }

    public StringFilter sourceType() {
        if (sourceType == null) {
            setSourceType(new StringFilter());
        }
        return sourceType;
    }

    public void setSourceType(StringFilter sourceType) {
        this.sourceType = sourceType;
    }

    public StringFilter getSourceDetail() {
        return sourceDetail;
    }

    public Optional<StringFilter> optionalSourceDetail() {
        return Optional.ofNullable(sourceDetail);
    }

    public StringFilter sourceDetail() {
        if (sourceDetail == null) {
            setSourceDetail(new StringFilter());
        }
        return sourceDetail;
    }

    public void setSourceDetail(StringFilter sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public StringFilter getBatchName() {
        return batchName;
    }

    public Optional<StringFilter> optionalBatchName() {
        return Optional.ofNullable(batchName);
    }

    public StringFilter batchName() {
        if (batchName == null) {
            setBatchName(new StringFilter());
        }
        return batchName;
    }

    public void setBatchName(StringFilter batchName) {
        this.batchName = batchName;
    }

    public InstantFilter getAppliedAt() {
        return appliedAt;
    }

    public Optional<InstantFilter> optionalAppliedAt() {
        return Optional.ofNullable(appliedAt);
    }

    public InstantFilter appliedAt() {
        if (appliedAt == null) {
            setAppliedAt(new InstantFilter());
        }
        return appliedAt;
    }

    public void setAppliedAt(InstantFilter appliedAt) {
        this.appliedAt = appliedAt;
    }

    public InstantFilter getClosedAt() {
        return closedAt;
    }

    public Optional<InstantFilter> optionalClosedAt() {
        return Optional.ofNullable(closedAt);
    }

    public InstantFilter closedAt() {
        if (closedAt == null) {
            setClosedAt(new InstantFilter());
        }
        return closedAt;
    }

    public void setClosedAt(InstantFilter closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDateFilter getRetentionUntil() {
        return retentionUntil;
    }

    public Optional<LocalDateFilter> optionalRetentionUntil() {
        return Optional.ofNullable(retentionUntil);
    }

    public LocalDateFilter retentionUntil() {
        if (retentionUntil == null) {
            setRetentionUntil(new LocalDateFilter());
        }
        return retentionUntil;
    }

    public void setRetentionUntil(LocalDateFilter retentionUntil) {
        this.retentionUntil = retentionUntil;
    }

    public LongFilter getConvertedPersonId() {
        return convertedPersonId;
    }

    public Optional<LongFilter> optionalConvertedPersonId() {
        return Optional.ofNullable(convertedPersonId);
    }

    public LongFilter convertedPersonId() {
        if (convertedPersonId == null) {
            setConvertedPersonId(new LongFilter());
        }
        return convertedPersonId;
    }

    public void setConvertedPersonId(LongFilter convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
    }

    public InstantFilter getConvertedAt() {
        return convertedAt;
    }

    public Optional<InstantFilter> optionalConvertedAt() {
        return Optional.ofNullable(convertedAt);
    }

    public InstantFilter convertedAt() {
        if (convertedAt == null) {
            setConvertedAt(new InstantFilter());
        }
        return convertedAt;
    }

    public void setConvertedAt(InstantFilter convertedAt) {
        this.convertedAt = convertedAt;
    }

    public LongFilter getStagesId() {
        return stagesId;
    }

    public Optional<LongFilter> optionalStagesId() {
        return Optional.ofNullable(stagesId);
    }

    public LongFilter stagesId() {
        if (stagesId == null) {
            setStagesId(new LongFilter());
        }
        return stagesId;
    }

    public void setStagesId(LongFilter stagesId) {
        this.stagesId = stagesId;
    }

    public LongFilter getEventsId() {
        return eventsId;
    }

    public Optional<LongFilter> optionalEventsId() {
        return Optional.ofNullable(eventsId);
    }

    public LongFilter eventsId() {
        if (eventsId == null) {
            setEventsId(new LongFilter());
        }
        return eventsId;
    }

    public void setEventsId(LongFilter eventsId) {
        this.eventsId = eventsId;
    }

    public LongFilter getActivitiesId() {
        return activitiesId;
    }

    public Optional<LongFilter> optionalActivitiesId() {
        return Optional.ofNullable(activitiesId);
    }

    public LongFilter activitiesId() {
        if (activitiesId == null) {
            setActivitiesId(new LongFilter());
        }
        return activitiesId;
    }

    public void setActivitiesId(LongFilter activitiesId) {
        this.activitiesId = activitiesId;
    }

    public LongFilter getEvaluationsId() {
        return evaluationsId;
    }

    public Optional<LongFilter> optionalEvaluationsId() {
        return Optional.ofNullable(evaluationsId);
    }

    public LongFilter evaluationsId() {
        if (evaluationsId == null) {
            setEvaluationsId(new LongFilter());
        }
        return evaluationsId;
    }

    public void setEvaluationsId(LongFilter evaluationsId) {
        this.evaluationsId = evaluationsId;
    }

    public LongFilter getDecisionsId() {
        return decisionsId;
    }

    public Optional<LongFilter> optionalDecisionsId() {
        return Optional.ofNullable(decisionsId);
    }

    public LongFilter decisionsId() {
        if (decisionsId == null) {
            setDecisionsId(new LongFilter());
        }
        return decisionsId;
    }

    public void setDecisionsId(LongFilter decisionsId) {
        this.decisionsId = decisionsId;
    }

    public LongFilter getMessagesId() {
        return messagesId;
    }

    public Optional<LongFilter> optionalMessagesId() {
        return Optional.ofNullable(messagesId);
    }

    public LongFilter messagesId() {
        if (messagesId == null) {
            setMessagesId(new LongFilter());
        }
        return messagesId;
    }

    public void setMessagesId(LongFilter messagesId) {
        this.messagesId = messagesId;
    }

    public LongFilter getFollowUpsId() {
        return followUpsId;
    }

    public Optional<LongFilter> optionalFollowUpsId() {
        return Optional.ofNullable(followUpsId);
    }

    public LongFilter followUpsId() {
        if (followUpsId == null) {
            setFollowUpsId(new LongFilter());
        }
        return followUpsId;
    }

    public void setFollowUpsId(LongFilter followUpsId) {
        this.followUpsId = followUpsId;
    }

    public LongFilter getDispatchAssignmentsId() {
        return dispatchAssignmentsId;
    }

    public Optional<LongFilter> optionalDispatchAssignmentsId() {
        return Optional.ofNullable(dispatchAssignmentsId);
    }

    public LongFilter dispatchAssignmentsId() {
        if (dispatchAssignmentsId == null) {
            setDispatchAssignmentsId(new LongFilter());
        }
        return dispatchAssignmentsId;
    }

    public void setDispatchAssignmentsId(LongFilter dispatchAssignmentsId) {
        this.dispatchAssignmentsId = dispatchAssignmentsId;
    }

    public LongFilter getCandidateId() {
        return candidateId;
    }

    public Optional<LongFilter> optionalCandidateId() {
        return Optional.ofNullable(candidateId);
    }

    public LongFilter candidateId() {
        if (candidateId == null) {
            setCandidateId(new LongFilter());
        }
        return candidateId;
    }

    public void setCandidateId(LongFilter candidateId) {
        this.candidateId = candidateId;
    }

    public LongFilter getRequisitionId() {
        return requisitionId;
    }

    public Optional<LongFilter> optionalRequisitionId() {
        return Optional.ofNullable(requisitionId);
    }

    public LongFilter requisitionId() {
        if (requisitionId == null) {
            setRequisitionId(new LongFilter());
        }
        return requisitionId;
    }

    public void setRequisitionId(LongFilter requisitionId) {
        this.requisitionId = requisitionId;
    }

    public LongFilter getRequisitionVersionId() {
        return requisitionVersionId;
    }

    public Optional<LongFilter> optionalRequisitionVersionId() {
        return Optional.ofNullable(requisitionVersionId);
    }

    public LongFilter requisitionVersionId() {
        if (requisitionVersionId == null) {
            setRequisitionVersionId(new LongFilter());
        }
        return requisitionVersionId;
    }

    public void setRequisitionVersionId(LongFilter requisitionVersionId) {
        this.requisitionVersionId = requisitionVersionId;
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
        final RecruitmentApplicationCriteria that = (RecruitmentApplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(applicationCode, that.applicationCode) &&
            Objects.equals(recruitmentType, that.recruitmentType) &&
            Objects.equals(currentStage, that.currentStage) &&
            Objects.equals(status, that.status) &&
            Objects.equals(decision, that.decision) &&
            Objects.equals(sourceType, that.sourceType) &&
            Objects.equals(sourceDetail, that.sourceDetail) &&
            Objects.equals(batchName, that.batchName) &&
            Objects.equals(appliedAt, that.appliedAt) &&
            Objects.equals(closedAt, that.closedAt) &&
            Objects.equals(retentionUntil, that.retentionUntil) &&
            Objects.equals(convertedPersonId, that.convertedPersonId) &&
            Objects.equals(convertedAt, that.convertedAt) &&
            Objects.equals(stagesId, that.stagesId) &&
            Objects.equals(eventsId, that.eventsId) &&
            Objects.equals(activitiesId, that.activitiesId) &&
            Objects.equals(evaluationsId, that.evaluationsId) &&
            Objects.equals(decisionsId, that.decisionsId) &&
            Objects.equals(messagesId, that.messagesId) &&
            Objects.equals(followUpsId, that.followUpsId) &&
            Objects.equals(dispatchAssignmentsId, that.dispatchAssignmentsId) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(requisitionId, that.requisitionId) &&
            Objects.equals(requisitionVersionId, that.requisitionVersionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            applicationCode,
            recruitmentType,
            currentStage,
            status,
            decision,
            sourceType,
            sourceDetail,
            batchName,
            appliedAt,
            closedAt,
            retentionUntil,
            convertedPersonId,
            convertedAt,
            stagesId,
            eventsId,
            activitiesId,
            evaluationsId,
            decisionsId,
            messagesId,
            followUpsId,
            dispatchAssignmentsId,
            candidateId,
            requisitionId,
            requisitionVersionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentApplicationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalApplicationCode().map(f -> "applicationCode=" + f + ", ").orElse("") +
            optionalRecruitmentType().map(f -> "recruitmentType=" + f + ", ").orElse("") +
            optionalCurrentStage().map(f -> "currentStage=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDecision().map(f -> "decision=" + f + ", ").orElse("") +
            optionalSourceType().map(f -> "sourceType=" + f + ", ").orElse("") +
            optionalSourceDetail().map(f -> "sourceDetail=" + f + ", ").orElse("") +
            optionalBatchName().map(f -> "batchName=" + f + ", ").orElse("") +
            optionalAppliedAt().map(f -> "appliedAt=" + f + ", ").orElse("") +
            optionalClosedAt().map(f -> "closedAt=" + f + ", ").orElse("") +
            optionalRetentionUntil().map(f -> "retentionUntil=" + f + ", ").orElse("") +
            optionalConvertedPersonId().map(f -> "convertedPersonId=" + f + ", ").orElse("") +
            optionalConvertedAt().map(f -> "convertedAt=" + f + ", ").orElse("") +
            optionalStagesId().map(f -> "stagesId=" + f + ", ").orElse("") +
            optionalEventsId().map(f -> "eventsId=" + f + ", ").orElse("") +
            optionalActivitiesId().map(f -> "activitiesId=" + f + ", ").orElse("") +
            optionalEvaluationsId().map(f -> "evaluationsId=" + f + ", ").orElse("") +
            optionalDecisionsId().map(f -> "decisionsId=" + f + ", ").orElse("") +
            optionalMessagesId().map(f -> "messagesId=" + f + ", ").orElse("") +
            optionalFollowUpsId().map(f -> "followUpsId=" + f + ", ").orElse("") +
            optionalDispatchAssignmentsId().map(f -> "dispatchAssignmentsId=" + f + ", ").orElse("") +
            optionalCandidateId().map(f -> "candidateId=" + f + ", ").orElse("") +
            optionalRequisitionId().map(f -> "requisitionId=" + f + ", ").orElse("") +
            optionalRequisitionVersionId().map(f -> "requisitionVersionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
