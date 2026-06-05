package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PositionSkillRequirement} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PositionSkillRequirementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /position-skill-requirements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionSkillRequirementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RequirementImportance
     */
    public static class RequirementImportanceFilter extends Filter<RequirementImportance> {

        public RequirementImportanceFilter() {}

        public RequirementImportanceFilter(RequirementImportanceFilter filter) {
            super(filter);
        }

        @Override
        public RequirementImportanceFilter copy() {
            return new RequirementImportanceFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RequirementImportanceFilter importance;

    private LongFilter positionId;

    private LongFilter skillId;

    private LongFilter requiredLevelId;

    private LongFilter preferredLevelId;

    private Boolean distinct;

    public PositionSkillRequirementCriteria() {}

    public PositionSkillRequirementCriteria(PositionSkillRequirementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.importance = other.optionalImportance().map(RequirementImportanceFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.requiredLevelId = other.optionalRequiredLevelId().map(LongFilter::copy).orElse(null);
        this.preferredLevelId = other.optionalPreferredLevelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionSkillRequirementCriteria copy() {
        return new PositionSkillRequirementCriteria(this);
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

    public RequirementImportanceFilter getImportance() {
        return importance;
    }

    public Optional<RequirementImportanceFilter> optionalImportance() {
        return Optional.ofNullable(importance);
    }

    public RequirementImportanceFilter importance() {
        if (importance == null) {
            setImportance(new RequirementImportanceFilter());
        }
        return importance;
    }

    public void setImportance(RequirementImportanceFilter importance) {
        this.importance = importance;
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

    public LongFilter getRequiredLevelId() {
        return requiredLevelId;
    }

    public Optional<LongFilter> optionalRequiredLevelId() {
        return Optional.ofNullable(requiredLevelId);
    }

    public LongFilter requiredLevelId() {
        if (requiredLevelId == null) {
            setRequiredLevelId(new LongFilter());
        }
        return requiredLevelId;
    }

    public void setRequiredLevelId(LongFilter requiredLevelId) {
        this.requiredLevelId = requiredLevelId;
    }

    public LongFilter getPreferredLevelId() {
        return preferredLevelId;
    }

    public Optional<LongFilter> optionalPreferredLevelId() {
        return Optional.ofNullable(preferredLevelId);
    }

    public LongFilter preferredLevelId() {
        if (preferredLevelId == null) {
            setPreferredLevelId(new LongFilter());
        }
        return preferredLevelId;
    }

    public void setPreferredLevelId(LongFilter preferredLevelId) {
        this.preferredLevelId = preferredLevelId;
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
        final PositionSkillRequirementCriteria that = (PositionSkillRequirementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(importance, that.importance) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(requiredLevelId, that.requiredLevelId) &&
            Objects.equals(preferredLevelId, that.preferredLevelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, importance, positionId, skillId, requiredLevelId, preferredLevelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionSkillRequirementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalImportance().map(f -> "importance=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalRequiredLevelId().map(f -> "requiredLevelId=" + f + ", ").orElse("") +
            optionalPreferredLevelId().map(f -> "preferredLevelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
