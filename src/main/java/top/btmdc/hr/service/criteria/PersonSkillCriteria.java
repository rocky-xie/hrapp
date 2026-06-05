package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ConfidenceLevel;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PersonSkill} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PersonSkillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /person-skills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonSkillCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ConfidenceLevel
     */
    public static class ConfidenceLevelFilter extends Filter<ConfidenceLevel> {

        public ConfidenceLevelFilter() {}

        public ConfidenceLevelFilter(ConfidenceLevelFilter filter) {
            super(filter);
        }

        @Override
        public ConfidenceLevelFilter copy() {
            return new ConfidenceLevelFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter assessmentDate;

    private LocalDateFilter nextReviewDate;

    private ConfidenceLevelFilter confidence;

    private LongFilter personId;

    private LongFilter skillId;

    private LongFilter currentLevelId;

    private LongFilter previousLevelId;

    private Boolean distinct;

    public PersonSkillCriteria() {}

    public PersonSkillCriteria(PersonSkillCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assessmentDate = other.optionalAssessmentDate().map(LocalDateFilter::copy).orElse(null);
        this.nextReviewDate = other.optionalNextReviewDate().map(LocalDateFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(ConfidenceLevelFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.currentLevelId = other.optionalCurrentLevelId().map(LongFilter::copy).orElse(null);
        this.previousLevelId = other.optionalPreviousLevelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PersonSkillCriteria copy() {
        return new PersonSkillCriteria(this);
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

    public LocalDateFilter getAssessmentDate() {
        return assessmentDate;
    }

    public Optional<LocalDateFilter> optionalAssessmentDate() {
        return Optional.ofNullable(assessmentDate);
    }

    public LocalDateFilter assessmentDate() {
        if (assessmentDate == null) {
            setAssessmentDate(new LocalDateFilter());
        }
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDateFilter assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public LocalDateFilter getNextReviewDate() {
        return nextReviewDate;
    }

    public Optional<LocalDateFilter> optionalNextReviewDate() {
        return Optional.ofNullable(nextReviewDate);
    }

    public LocalDateFilter nextReviewDate() {
        if (nextReviewDate == null) {
            setNextReviewDate(new LocalDateFilter());
        }
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDateFilter nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public ConfidenceLevelFilter getConfidence() {
        return confidence;
    }

    public Optional<ConfidenceLevelFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public ConfidenceLevelFilter confidence() {
        if (confidence == null) {
            setConfidence(new ConfidenceLevelFilter());
        }
        return confidence;
    }

    public void setConfidence(ConfidenceLevelFilter confidence) {
        this.confidence = confidence;
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

    public LongFilter getSkillId() {
        return skillId;
    }

    public Optional<LongFilter> optionalSkillId() {
        return Optional.ofNullable(skillId);
    }

    public LongFilter skillId() {
        if (skillId == null) {
            setSkillId(new LongFilter());
        }
        return skillId;
    }

    public void setSkillId(LongFilter skillId) {
        this.skillId = skillId;
    }

    public LongFilter getCurrentLevelId() {
        return currentLevelId;
    }

    public Optional<LongFilter> optionalCurrentLevelId() {
        return Optional.ofNullable(currentLevelId);
    }

    public LongFilter currentLevelId() {
        if (currentLevelId == null) {
            setCurrentLevelId(new LongFilter());
        }
        return currentLevelId;
    }

    public void setCurrentLevelId(LongFilter currentLevelId) {
        this.currentLevelId = currentLevelId;
    }

    public LongFilter getPreviousLevelId() {
        return previousLevelId;
    }

    public Optional<LongFilter> optionalPreviousLevelId() {
        return Optional.ofNullable(previousLevelId);
    }

    public LongFilter previousLevelId() {
        if (previousLevelId == null) {
            setPreviousLevelId(new LongFilter());
        }
        return previousLevelId;
    }

    public void setPreviousLevelId(LongFilter previousLevelId) {
        this.previousLevelId = previousLevelId;
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
        final PersonSkillCriteria that = (PersonSkillCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assessmentDate, that.assessmentDate) &&
            Objects.equals(nextReviewDate, that.nextReviewDate) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(currentLevelId, that.currentLevelId) &&
            Objects.equals(previousLevelId, that.previousLevelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assessmentDate, nextReviewDate, confidence, personId, skillId, currentLevelId, previousLevelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonSkillCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssessmentDate().map(f -> "assessmentDate=" + f + ", ").orElse("") +
            optionalNextReviewDate().map(f -> "nextReviewDate=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalCurrentLevelId().map(f -> "currentLevelId=" + f + ", ").orElse("") +
            optionalPreviousLevelId().map(f -> "previousLevelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
