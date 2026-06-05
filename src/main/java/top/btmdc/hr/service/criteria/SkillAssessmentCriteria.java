package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.AssessmentResult;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.SkillAssessment} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SkillAssessmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skill-assessments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillAssessmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AssessmentResult
     */
    public static class AssessmentResultFilter extends Filter<AssessmentResult> {

        public AssessmentResultFilter() {}

        public AssessmentResultFilter(AssessmentResultFilter filter) {
            super(filter);
        }

        @Override
        public AssessmentResultFilter copy() {
            return new AssessmentResultFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter assessmentDate;

    private AssessmentResultFilter result;

    private LongFilter personId;

    private LongFilter skillId;

    private LongFilter assessorId;

    private LongFilter newLevelId;

    private Boolean distinct;

    public SkillAssessmentCriteria() {}

    public SkillAssessmentCriteria(SkillAssessmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assessmentDate = other.optionalAssessmentDate().map(LocalDateFilter::copy).orElse(null);
        this.result = other.optionalResult().map(AssessmentResultFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.assessorId = other.optionalAssessorId().map(LongFilter::copy).orElse(null);
        this.newLevelId = other.optionalNewLevelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SkillAssessmentCriteria copy() {
        return new SkillAssessmentCriteria(this);
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

    public AssessmentResultFilter getResult() {
        return result;
    }

    public Optional<AssessmentResultFilter> optionalResult() {
        return Optional.ofNullable(result);
    }

    public AssessmentResultFilter result() {
        if (result == null) {
            setResult(new AssessmentResultFilter());
        }
        return result;
    }

    public void setResult(AssessmentResultFilter result) {
        this.result = result;
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

    public LongFilter getAssessorId() {
        return assessorId;
    }

    public Optional<LongFilter> optionalAssessorId() {
        return Optional.ofNullable(assessorId);
    }

    public LongFilter assessorId() {
        if (assessorId == null) {
            setAssessorId(new LongFilter());
        }
        return assessorId;
    }

    public void setAssessorId(LongFilter assessorId) {
        this.assessorId = assessorId;
    }

    public LongFilter getNewLevelId() {
        return newLevelId;
    }

    public Optional<LongFilter> optionalNewLevelId() {
        return Optional.ofNullable(newLevelId);
    }

    public LongFilter newLevelId() {
        if (newLevelId == null) {
            setNewLevelId(new LongFilter());
        }
        return newLevelId;
    }

    public void setNewLevelId(LongFilter newLevelId) {
        this.newLevelId = newLevelId;
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
        final SkillAssessmentCriteria that = (SkillAssessmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assessmentDate, that.assessmentDate) &&
            Objects.equals(result, that.result) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(assessorId, that.assessorId) &&
            Objects.equals(newLevelId, that.newLevelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assessmentDate, result, personId, skillId, assessorId, newLevelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillAssessmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssessmentDate().map(f -> "assessmentDate=" + f + ", ").orElse("") +
            optionalResult().map(f -> "result=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalAssessorId().map(f -> "assessorId=" + f + ", ").orElse("") +
            optionalNewLevelId().map(f -> "newLevelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
