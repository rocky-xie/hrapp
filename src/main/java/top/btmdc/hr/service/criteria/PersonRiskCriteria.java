package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PersonRisk} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PersonRiskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /person-risks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonRiskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RiskType
     */
    public static class RiskTypeFilter extends Filter<RiskType> {

        public RiskTypeFilter() {}

        public RiskTypeFilter(RiskTypeFilter filter) {
            super(filter);
        }

        @Override
        public RiskTypeFilter copy() {
            return new RiskTypeFilter(this);
        }
    }

    /**
     * Class for filtering RiskLevel
     */
    public static class RiskLevelFilter extends Filter<RiskLevel> {

        public RiskLevelFilter() {}

        public RiskLevelFilter(RiskLevelFilter filter) {
            super(filter);
        }

        @Override
        public RiskLevelFilter copy() {
            return new RiskLevelFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RiskTypeFilter riskType;

    private RiskLevelFilter riskLevel;

    private LocalDateFilter identifiedDate;

    private LocalDateFilter targetDate;

    private LocalDateFilter closedDate;

    private LongFilter personId;

    private LongFilter positionId;

    private Boolean distinct;

    public PersonRiskCriteria() {}

    public PersonRiskCriteria(PersonRiskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.riskType = other.optionalRiskType().map(RiskTypeFilter::copy).orElse(null);
        this.riskLevel = other.optionalRiskLevel().map(RiskLevelFilter::copy).orElse(null);
        this.identifiedDate = other.optionalIdentifiedDate().map(LocalDateFilter::copy).orElse(null);
        this.targetDate = other.optionalTargetDate().map(LocalDateFilter::copy).orElse(null);
        this.closedDate = other.optionalClosedDate().map(LocalDateFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PersonRiskCriteria copy() {
        return new PersonRiskCriteria(this);
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

    public RiskTypeFilter getRiskType() {
        return riskType;
    }

    public Optional<RiskTypeFilter> optionalRiskType() {
        return Optional.ofNullable(riskType);
    }

    public RiskTypeFilter riskType() {
        if (riskType == null) {
            setRiskType(new RiskTypeFilter());
        }
        return riskType;
    }

    public void setRiskType(RiskTypeFilter riskType) {
        this.riskType = riskType;
    }

    public RiskLevelFilter getRiskLevel() {
        return riskLevel;
    }

    public Optional<RiskLevelFilter> optionalRiskLevel() {
        return Optional.ofNullable(riskLevel);
    }

    public RiskLevelFilter riskLevel() {
        if (riskLevel == null) {
            setRiskLevel(new RiskLevelFilter());
        }
        return riskLevel;
    }

    public void setRiskLevel(RiskLevelFilter riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateFilter getIdentifiedDate() {
        return identifiedDate;
    }

    public Optional<LocalDateFilter> optionalIdentifiedDate() {
        return Optional.ofNullable(identifiedDate);
    }

    public LocalDateFilter identifiedDate() {
        if (identifiedDate == null) {
            setIdentifiedDate(new LocalDateFilter());
        }
        return identifiedDate;
    }

    public void setIdentifiedDate(LocalDateFilter identifiedDate) {
        this.identifiedDate = identifiedDate;
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

    public LocalDateFilter getClosedDate() {
        return closedDate;
    }

    public Optional<LocalDateFilter> optionalClosedDate() {
        return Optional.ofNullable(closedDate);
    }

    public LocalDateFilter closedDate() {
        if (closedDate == null) {
            setClosedDate(new LocalDateFilter());
        }
        return closedDate;
    }

    public void setClosedDate(LocalDateFilter closedDate) {
        this.closedDate = closedDate;
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
        final PersonRiskCriteria that = (PersonRiskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(riskType, that.riskType) &&
            Objects.equals(riskLevel, that.riskLevel) &&
            Objects.equals(identifiedDate, that.identifiedDate) &&
            Objects.equals(targetDate, that.targetDate) &&
            Objects.equals(closedDate, that.closedDate) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, riskType, riskLevel, identifiedDate, targetDate, closedDate, personId, positionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonRiskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRiskType().map(f -> "riskType=" + f + ", ").orElse("") +
            optionalRiskLevel().map(f -> "riskLevel=" + f + ", ").orElse("") +
            optionalIdentifiedDate().map(f -> "identifiedDate=" + f + ", ").orElse("") +
            optionalTargetDate().map(f -> "targetDate=" + f + ", ").orElse("") +
            optionalClosedDate().map(f -> "closedDate=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
