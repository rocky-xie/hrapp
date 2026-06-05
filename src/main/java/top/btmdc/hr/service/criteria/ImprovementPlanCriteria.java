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
 * Criteria class for the {@link top.btmdc.hr.domain.ImprovementPlan} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.ImprovementPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /improvement-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImprovementPlanCriteria implements Serializable, Criteria {

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

    private StringFilter planName;

    private PlanStatusFilter planStatus;

    private StringFilter ownerName;

    private LocalDateFilter startDate;

    private LocalDateFilter targetDate;

    private LocalDateFilter completionDate;

    private LongFilter positionId;

    private LongFilter skillId;

    private Boolean distinct;

    public ImprovementPlanCriteria() {}

    public ImprovementPlanCriteria(ImprovementPlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.planName = other.optionalPlanName().map(StringFilter::copy).orElse(null);
        this.planStatus = other.optionalPlanStatus().map(PlanStatusFilter::copy).orElse(null);
        this.ownerName = other.optionalOwnerName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.targetDate = other.optionalTargetDate().map(LocalDateFilter::copy).orElse(null);
        this.completionDate = other.optionalCompletionDate().map(LocalDateFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImprovementPlanCriteria copy() {
        return new ImprovementPlanCriteria(this);
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

    public StringFilter getPlanName() {
        return planName;
    }

    public Optional<StringFilter> optionalPlanName() {
        return Optional.ofNullable(planName);
    }

    public StringFilter planName() {
        if (planName == null) {
            setPlanName(new StringFilter());
        }
        return planName;
    }

    public void setPlanName(StringFilter planName) {
        this.planName = planName;
    }

    public PlanStatusFilter getPlanStatus() {
        return planStatus;
    }

    public Optional<PlanStatusFilter> optionalPlanStatus() {
        return Optional.ofNullable(planStatus);
    }

    public PlanStatusFilter planStatus() {
        if (planStatus == null) {
            setPlanStatus(new PlanStatusFilter());
        }
        return planStatus;
    }

    public void setPlanStatus(PlanStatusFilter planStatus) {
        this.planStatus = planStatus;
    }

    public StringFilter getOwnerName() {
        return ownerName;
    }

    public Optional<StringFilter> optionalOwnerName() {
        return Optional.ofNullable(ownerName);
    }

    public StringFilter ownerName() {
        if (ownerName == null) {
            setOwnerName(new StringFilter());
        }
        return ownerName;
    }

    public void setOwnerName(StringFilter ownerName) {
        this.ownerName = ownerName;
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

    public LocalDateFilter getCompletionDate() {
        return completionDate;
    }

    public Optional<LocalDateFilter> optionalCompletionDate() {
        return Optional.ofNullable(completionDate);
    }

    public LocalDateFilter completionDate() {
        if (completionDate == null) {
            setCompletionDate(new LocalDateFilter());
        }
        return completionDate;
    }

    public void setCompletionDate(LocalDateFilter completionDate) {
        this.completionDate = completionDate;
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
        final ImprovementPlanCriteria that = (ImprovementPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(planStatus, that.planStatus) &&
            Objects.equals(ownerName, that.ownerName) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(targetDate, that.targetDate) &&
            Objects.equals(completionDate, that.completionDate) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planName, planStatus, ownerName, startDate, targetDate, completionDate, positionId, skillId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImprovementPlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPlanName().map(f -> "planName=" + f + ", ").orElse("") +
            optionalPlanStatus().map(f -> "planStatus=" + f + ", ").orElse("") +
            optionalOwnerName().map(f -> "ownerName=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalTargetDate().map(f -> "targetDate=" + f + ", ").orElse("") +
            optionalCompletionDate().map(f -> "completionDate=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
