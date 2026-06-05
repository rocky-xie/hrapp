package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PositionAssignment} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PositionAssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /position-assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionAssignmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter primaryOwner;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter active;

    private LongFilter personId;

    private LongFilter positionId;

    private Boolean distinct;

    public PositionAssignmentCriteria() {}

    public PositionAssignmentCriteria(PositionAssignmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.primaryOwner = other.optionalPrimaryOwner().map(BooleanFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionAssignmentCriteria copy() {
        return new PositionAssignmentCriteria(this);
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

    public BooleanFilter getPrimaryOwner() {
        return primaryOwner;
    }

    public Optional<BooleanFilter> optionalPrimaryOwner() {
        return Optional.ofNullable(primaryOwner);
    }

    public BooleanFilter primaryOwner() {
        if (primaryOwner == null) {
            setPrimaryOwner(new BooleanFilter());
        }
        return primaryOwner;
    }

    public void setPrimaryOwner(BooleanFilter primaryOwner) {
        this.primaryOwner = primaryOwner;
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

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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
        final PositionAssignmentCriteria that = (PositionAssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(primaryOwner, that.primaryOwner) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, primaryOwner, startDate, endDate, active, personId, positionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionAssignmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrimaryOwner().map(f -> "primaryOwner=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
