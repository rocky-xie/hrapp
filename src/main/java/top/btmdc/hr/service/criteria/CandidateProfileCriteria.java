package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.CandidateJudgement;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.CandidateProfile} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.CandidateProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /candidate-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ImportanceLevel
     */
    public static class ImportanceLevelFilter extends Filter<ImportanceLevel> {

        public ImportanceLevelFilter() {}

        public ImportanceLevelFilter(ImportanceLevelFilter filter) {
            super(filter);
        }

        @Override
        public ImportanceLevelFilter copy() {
            return new ImportanceLevelFilter(this);
        }
    }

    /**
     * Class for filtering CandidateJudgement
     */
    public static class CandidateJudgementFilter extends Filter<CandidateJudgement> {

        public CandidateJudgementFilter() {}

        public CandidateJudgementFilter(CandidateJudgementFilter filter) {
            super(filter);
        }

        @Override
        public CandidateJudgementFilter copy() {
            return new CandidateJudgementFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter candidateDate;

    private StringFilter cultivateDirection;

    private ImportanceLevelFilter stability;

    private ImportanceLevelFilter learningAbility;

    private ImportanceLevelFilter communicationCoordination;

    private ImportanceLevelFilter businessUnderstanding;

    private ImportanceLevelFilter responsibility;

    private ImportanceLevelFilter riskAwareness;

    private CandidateJudgementFilter judgement;

    private LongFilter personId;

    private LongFilter positionId;

    private LongFilter observerId;

    private Boolean distinct;

    public CandidateProfileCriteria() {}

    public CandidateProfileCriteria(CandidateProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.candidateDate = other.optionalCandidateDate().map(LocalDateFilter::copy).orElse(null);
        this.cultivateDirection = other.optionalCultivateDirection().map(StringFilter::copy).orElse(null);
        this.stability = other.optionalStability().map(ImportanceLevelFilter::copy).orElse(null);
        this.learningAbility = other.optionalLearningAbility().map(ImportanceLevelFilter::copy).orElse(null);
        this.communicationCoordination = other.optionalCommunicationCoordination().map(ImportanceLevelFilter::copy).orElse(null);
        this.businessUnderstanding = other.optionalBusinessUnderstanding().map(ImportanceLevelFilter::copy).orElse(null);
        this.responsibility = other.optionalResponsibility().map(ImportanceLevelFilter::copy).orElse(null);
        this.riskAwareness = other.optionalRiskAwareness().map(ImportanceLevelFilter::copy).orElse(null);
        this.judgement = other.optionalJudgement().map(CandidateJudgementFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.observerId = other.optionalObserverId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CandidateProfileCriteria copy() {
        return new CandidateProfileCriteria(this);
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

    public LocalDateFilter getCandidateDate() {
        return candidateDate;
    }

    public Optional<LocalDateFilter> optionalCandidateDate() {
        return Optional.ofNullable(candidateDate);
    }

    public LocalDateFilter candidateDate() {
        if (candidateDate == null) {
            setCandidateDate(new LocalDateFilter());
        }
        return candidateDate;
    }

    public void setCandidateDate(LocalDateFilter candidateDate) {
        this.candidateDate = candidateDate;
    }

    public StringFilter getCultivateDirection() {
        return cultivateDirection;
    }

    public Optional<StringFilter> optionalCultivateDirection() {
        return Optional.ofNullable(cultivateDirection);
    }

    public StringFilter cultivateDirection() {
        if (cultivateDirection == null) {
            setCultivateDirection(new StringFilter());
        }
        return cultivateDirection;
    }

    public void setCultivateDirection(StringFilter cultivateDirection) {
        this.cultivateDirection = cultivateDirection;
    }

    public ImportanceLevelFilter getStability() {
        return stability;
    }

    public Optional<ImportanceLevelFilter> optionalStability() {
        return Optional.ofNullable(stability);
    }

    public ImportanceLevelFilter stability() {
        if (stability == null) {
            setStability(new ImportanceLevelFilter());
        }
        return stability;
    }

    public void setStability(ImportanceLevelFilter stability) {
        this.stability = stability;
    }

    public ImportanceLevelFilter getLearningAbility() {
        return learningAbility;
    }

    public Optional<ImportanceLevelFilter> optionalLearningAbility() {
        return Optional.ofNullable(learningAbility);
    }

    public ImportanceLevelFilter learningAbility() {
        if (learningAbility == null) {
            setLearningAbility(new ImportanceLevelFilter());
        }
        return learningAbility;
    }

    public void setLearningAbility(ImportanceLevelFilter learningAbility) {
        this.learningAbility = learningAbility;
    }

    public ImportanceLevelFilter getCommunicationCoordination() {
        return communicationCoordination;
    }

    public Optional<ImportanceLevelFilter> optionalCommunicationCoordination() {
        return Optional.ofNullable(communicationCoordination);
    }

    public ImportanceLevelFilter communicationCoordination() {
        if (communicationCoordination == null) {
            setCommunicationCoordination(new ImportanceLevelFilter());
        }
        return communicationCoordination;
    }

    public void setCommunicationCoordination(ImportanceLevelFilter communicationCoordination) {
        this.communicationCoordination = communicationCoordination;
    }

    public ImportanceLevelFilter getBusinessUnderstanding() {
        return businessUnderstanding;
    }

    public Optional<ImportanceLevelFilter> optionalBusinessUnderstanding() {
        return Optional.ofNullable(businessUnderstanding);
    }

    public ImportanceLevelFilter businessUnderstanding() {
        if (businessUnderstanding == null) {
            setBusinessUnderstanding(new ImportanceLevelFilter());
        }
        return businessUnderstanding;
    }

    public void setBusinessUnderstanding(ImportanceLevelFilter businessUnderstanding) {
        this.businessUnderstanding = businessUnderstanding;
    }

    public ImportanceLevelFilter getResponsibility() {
        return responsibility;
    }

    public Optional<ImportanceLevelFilter> optionalResponsibility() {
        return Optional.ofNullable(responsibility);
    }

    public ImportanceLevelFilter responsibility() {
        if (responsibility == null) {
            setResponsibility(new ImportanceLevelFilter());
        }
        return responsibility;
    }

    public void setResponsibility(ImportanceLevelFilter responsibility) {
        this.responsibility = responsibility;
    }

    public ImportanceLevelFilter getRiskAwareness() {
        return riskAwareness;
    }

    public Optional<ImportanceLevelFilter> optionalRiskAwareness() {
        return Optional.ofNullable(riskAwareness);
    }

    public ImportanceLevelFilter riskAwareness() {
        if (riskAwareness == null) {
            setRiskAwareness(new ImportanceLevelFilter());
        }
        return riskAwareness;
    }

    public void setRiskAwareness(ImportanceLevelFilter riskAwareness) {
        this.riskAwareness = riskAwareness;
    }

    public CandidateJudgementFilter getJudgement() {
        return judgement;
    }

    public Optional<CandidateJudgementFilter> optionalJudgement() {
        return Optional.ofNullable(judgement);
    }

    public CandidateJudgementFilter judgement() {
        if (judgement == null) {
            setJudgement(new CandidateJudgementFilter());
        }
        return judgement;
    }

    public void setJudgement(CandidateJudgementFilter judgement) {
        this.judgement = judgement;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public Optional<LongFilter> optionalPersonId() {
        return Optional.ofNullable(personId);
    }

    public LongFilter personId() {
        if (personId == null) {
            setPersonId(new LongFilter());
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public Optional<LongFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public LongFilter positionId() {
        if (positionId == null) {
            setPositionId(new LongFilter());
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public LongFilter getObserverId() {
        return observerId;
    }

    public Optional<LongFilter> optionalObserverId() {
        return Optional.ofNullable(observerId);
    }

    public LongFilter observerId() {
        if (observerId == null) {
            setObserverId(new LongFilter());
        }
        return observerId;
    }

    public void setObserverId(LongFilter observerId) {
        this.observerId = observerId;
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
        final CandidateProfileCriteria that = (CandidateProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(candidateDate, that.candidateDate) &&
            Objects.equals(cultivateDirection, that.cultivateDirection) &&
            Objects.equals(stability, that.stability) &&
            Objects.equals(learningAbility, that.learningAbility) &&
            Objects.equals(communicationCoordination, that.communicationCoordination) &&
            Objects.equals(businessUnderstanding, that.businessUnderstanding) &&
            Objects.equals(responsibility, that.responsibility) &&
            Objects.equals(riskAwareness, that.riskAwareness) &&
            Objects.equals(judgement, that.judgement) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(observerId, that.observerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            candidateDate,
            cultivateDirection,
            stability,
            learningAbility,
            communicationCoordination,
            businessUnderstanding,
            responsibility,
            riskAwareness,
            judgement,
            personId,
            positionId,
            observerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCandidateDate().map(f -> "candidateDate=" + f + ", ").orElse("") +
            optionalCultivateDirection().map(f -> "cultivateDirection=" + f + ", ").orElse("") +
            optionalStability().map(f -> "stability=" + f + ", ").orElse("") +
            optionalLearningAbility().map(f -> "learningAbility=" + f + ", ").orElse("") +
            optionalCommunicationCoordination().map(f -> "communicationCoordination=" + f + ", ").orElse("") +
            optionalBusinessUnderstanding().map(f -> "businessUnderstanding=" + f + ", ").orElse("") +
            optionalResponsibility().map(f -> "responsibility=" + f + ", ").orElse("") +
            optionalRiskAwareness().map(f -> "riskAwareness=" + f + ", ").orElse("") +
            optionalJudgement().map(f -> "judgement=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalObserverId().map(f -> "observerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
