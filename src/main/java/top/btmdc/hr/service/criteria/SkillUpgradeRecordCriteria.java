package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.SkillChangeType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.SkillUpgradeRecord} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SkillUpgradeRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skill-upgrade-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillUpgradeRecordCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SkillChangeType
     */
    public static class SkillChangeTypeFilter extends Filter<SkillChangeType> {

        public SkillChangeTypeFilter() {}

        public SkillChangeTypeFilter(SkillChangeTypeFilter filter) {
            super(filter);
        }

        @Override
        public SkillChangeTypeFilter copy() {
            return new SkillChangeTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SkillChangeTypeFilter changeType;

    private LocalDateFilter changeDate;

    private StringFilter reason;

    private StringFilter beforeLevelLabel;

    private StringFilter afterLevelLabel;

    private LongFilter personId;

    private LongFilter skillId;

    private LongFilter oldLevelId;

    private LongFilter newLevelId;

    private LongFilter assessorId;

    private Boolean distinct;

    public SkillUpgradeRecordCriteria() {}

    public SkillUpgradeRecordCriteria(SkillUpgradeRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.changeType = other.optionalChangeType().map(SkillChangeTypeFilter::copy).orElse(null);
        this.changeDate = other.optionalChangeDate().map(LocalDateFilter::copy).orElse(null);
        this.reason = other.optionalReason().map(StringFilter::copy).orElse(null);
        this.beforeLevelLabel = other.optionalBeforeLevelLabel().map(StringFilter::copy).orElse(null);
        this.afterLevelLabel = other.optionalAfterLevelLabel().map(StringFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.oldLevelId = other.optionalOldLevelId().map(LongFilter::copy).orElse(null);
        this.newLevelId = other.optionalNewLevelId().map(LongFilter::copy).orElse(null);
        this.assessorId = other.optionalAssessorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SkillUpgradeRecordCriteria copy() {
        return new SkillUpgradeRecordCriteria(this);
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

    public SkillChangeTypeFilter getChangeType() {
        return changeType;
    }

    public Optional<SkillChangeTypeFilter> optionalChangeType() {
        return Optional.ofNullable(changeType);
    }

    public SkillChangeTypeFilter changeType() {
        if (changeType == null) {
            setChangeType(new SkillChangeTypeFilter());
        }
        return changeType;
    }

    public void setChangeType(SkillChangeTypeFilter changeType) {
        this.changeType = changeType;
    }

    public LocalDateFilter getChangeDate() {
        return changeDate;
    }

    public Optional<LocalDateFilter> optionalChangeDate() {
        return Optional.ofNullable(changeDate);
    }

    public LocalDateFilter changeDate() {
        if (changeDate == null) {
            setChangeDate(new LocalDateFilter());
        }
        return changeDate;
    }

    public void setChangeDate(LocalDateFilter changeDate) {
        this.changeDate = changeDate;
    }

    public StringFilter getReason() {
        return reason;
    }

    public Optional<StringFilter> optionalReason() {
        return Optional.ofNullable(reason);
    }

    public StringFilter reason() {
        if (reason == null) {
            setReason(new StringFilter());
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public StringFilter getBeforeLevelLabel() {
        return beforeLevelLabel;
    }

    public Optional<StringFilter> optionalBeforeLevelLabel() {
        return Optional.ofNullable(beforeLevelLabel);
    }

    public StringFilter beforeLevelLabel() {
        if (beforeLevelLabel == null) {
            setBeforeLevelLabel(new StringFilter());
        }
        return beforeLevelLabel;
    }

    public void setBeforeLevelLabel(StringFilter beforeLevelLabel) {
        this.beforeLevelLabel = beforeLevelLabel;
    }

    public StringFilter getAfterLevelLabel() {
        return afterLevelLabel;
    }

    public Optional<StringFilter> optionalAfterLevelLabel() {
        return Optional.ofNullable(afterLevelLabel);
    }

    public StringFilter afterLevelLabel() {
        if (afterLevelLabel == null) {
            setAfterLevelLabel(new StringFilter());
        }
        return afterLevelLabel;
    }

    public void setAfterLevelLabel(StringFilter afterLevelLabel) {
        this.afterLevelLabel = afterLevelLabel;
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

    public LongFilter getOldLevelId() {
        return oldLevelId;
    }

    public Optional<LongFilter> optionalOldLevelId() {
        return Optional.ofNullable(oldLevelId);
    }

    public LongFilter oldLevelId() {
        if (oldLevelId == null) {
            setOldLevelId(new LongFilter());
        }
        return oldLevelId;
    }

    public void setOldLevelId(LongFilter oldLevelId) {
        this.oldLevelId = oldLevelId;
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
        final SkillUpgradeRecordCriteria that = (SkillUpgradeRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(changeType, that.changeType) &&
            Objects.equals(changeDate, that.changeDate) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(beforeLevelLabel, that.beforeLevelLabel) &&
            Objects.equals(afterLevelLabel, that.afterLevelLabel) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(oldLevelId, that.oldLevelId) &&
            Objects.equals(newLevelId, that.newLevelId) &&
            Objects.equals(assessorId, that.assessorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            changeType,
            changeDate,
            reason,
            beforeLevelLabel,
            afterLevelLabel,
            personId,
            skillId,
            oldLevelId,
            newLevelId,
            assessorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillUpgradeRecordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalChangeType().map(f -> "changeType=" + f + ", ").orElse("") +
            optionalChangeDate().map(f -> "changeDate=" + f + ", ").orElse("") +
            optionalReason().map(f -> "reason=" + f + ", ").orElse("") +
            optionalBeforeLevelLabel().map(f -> "beforeLevelLabel=" + f + ", ").orElse("") +
            optionalAfterLevelLabel().map(f -> "afterLevelLabel=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalOldLevelId().map(f -> "oldLevelId=" + f + ", ").orElse("") +
            optionalNewLevelId().map(f -> "newLevelId=" + f + ", ").orElse("") +
            optionalAssessorId().map(f -> "assessorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
