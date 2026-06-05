package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.PlanStatus;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.TrainingGoal} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.TrainingGoalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /training-goals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingGoalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PlanStatus
     */
    public static class PlanStatusFilter extends Filter<PlanStatus> {

        public PlanStatusFilter() {}

        public PlanStatusFilter(PlanStatusFilter filter) {
            super(filter);
        }

        @Override
        public PlanStatusFilter copy() {
            return new PlanStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter goalName;

    private LocalDateFilter startDate;

    private LocalDateFilter targetDate;

    private PlanStatusFilter status;

    private LongFilter personId;

    private LongFilter positionId;

    private LongFilter skillId;

    private LongFilter targetLevelId;

    private Boolean distinct;

    public TrainingGoalCriteria() {}

    public TrainingGoalCriteria(TrainingGoalCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.goalName = other.optionalGoalName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.targetDate = other.optionalTargetDate().map(LocalDateFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PlanStatusFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.targetLevelId = other.optionalTargetLevelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrainingGoalCriteria copy() {
        return new TrainingGoalCriteria(this);
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

    public StringFilter getGoalName() {
        return goalName;
    }

    public Optional<StringFilter> optionalGoalName() {
        return Optional.ofNullable(goalName);
    }

    public StringFilter goalName() {
        if (goalName == null) {
            setGoalName(new StringFilter());
        }
        return goalName;
    }

    public void setGoalName(StringFilter goalName) {
        this.goalName = goalName;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getTargetDate() {
        return targetDate;
    }

    public Optional<LocalDateFilter> optionalTargetDate() {
        return Optional.ofNullable(targetDate);
    }

    public LocalDateFilter targetDate() {
        if (targetDate == null) {
            setTargetDate(new LocalDateFilter());
        }
        return targetDate;
    }

    public void setTargetDate(LocalDateFilter targetDate) {
        this.targetDate = targetDate;
    }

    public PlanStatusFilter getStatus() {
        return status;
    }

    public Optional<PlanStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PlanStatusFilter status() {
        if (status == null) {
            setStatus(new PlanStatusFilter());
        }
        return status;
    }

    public void setStatus(PlanStatusFilter status) {
        this.status = status;
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

    public LongFilter getTargetLevelId() {
        return targetLevelId;
    }

    public Optional<LongFilter> optionalTargetLevelId() {
        return Optional.ofNullable(targetLevelId);
    }

    public LongFilter targetLevelId() {
        if (targetLevelId == null) {
            setTargetLevelId(new LongFilter());
        }
        return targetLevelId;
    }

    public void setTargetLevelId(LongFilter targetLevelId) {
        this.targetLevelId = targetLevelId;
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
        final TrainingGoalCriteria that = (TrainingGoalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(goalName, that.goalName) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(targetDate, that.targetDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(targetLevelId, that.targetLevelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goalName, startDate, targetDate, status, personId, positionId, skillId, targetLevelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingGoalCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalGoalName().map(f -> "goalName=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalTargetDate().map(f -> "targetDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalTargetLevelId().map(f -> "targetLevelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
