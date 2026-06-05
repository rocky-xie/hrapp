package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.PositionType;
import top.btmdc.hr.domain.enumeration.ReviewCycle;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.Position} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /positions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PositionType
     */
    public static class PositionTypeFilter extends Filter<PositionType> {

        public PositionTypeFilter() {}

        public PositionTypeFilter(PositionTypeFilter filter) {
            super(filter);
        }

        @Override
        public PositionTypeFilter copy() {
            return new PositionTypeFilter(this);
        }
    }

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
     * Class for filtering ReviewCycle
     */
    public static class ReviewCycleFilter extends Filter<ReviewCycle> {

        public ReviewCycleFilter() {}

        public ReviewCycleFilter(ReviewCycleFilter filter) {
            super(filter);
        }

        @Override
        public ReviewCycleFilter copy() {
            return new ReviewCycleFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter positionCode;

    private StringFilter positionName;

    private PositionTypeFilter positionType;

    private ImportanceLevelFilter businessImportance;

    private BooleanFilter keyPosition;

    private IntegerFilter plannedHeadcount;

    private IntegerFilter minimumOwnerCount;

    private ReviewCycleFilter reviewCycle;

    private BooleanFilter active;

    private Boolean distinct;

    public PositionCriteria() {}

    public PositionCriteria(PositionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.positionCode = other.optionalPositionCode().map(StringFilter::copy).orElse(null);
        this.positionName = other.optionalPositionName().map(StringFilter::copy).orElse(null);
        this.positionType = other.optionalPositionType().map(PositionTypeFilter::copy).orElse(null);
        this.businessImportance = other.optionalBusinessImportance().map(ImportanceLevelFilter::copy).orElse(null);
        this.keyPosition = other.optionalKeyPosition().map(BooleanFilter::copy).orElse(null);
        this.plannedHeadcount = other.optionalPlannedHeadcount().map(IntegerFilter::copy).orElse(null);
        this.minimumOwnerCount = other.optionalMinimumOwnerCount().map(IntegerFilter::copy).orElse(null);
        this.reviewCycle = other.optionalReviewCycle().map(ReviewCycleFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionCriteria copy() {
        return new PositionCriteria(this);
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

    public StringFilter getPositionCode() {
        return positionCode;
    }

    public Optional<StringFilter> optionalPositionCode() {
        return Optional.ofNullable(positionCode);
    }

    public StringFilter positionCode() {
        if (positionCode == null) {
            setPositionCode(new StringFilter());
        }
        return positionCode;
    }

    public void setPositionCode(StringFilter positionCode) {
        this.positionCode = positionCode;
    }

    public StringFilter getPositionName() {
        return positionName;
    }

    public Optional<StringFilter> optionalPositionName() {
        return Optional.ofNullable(positionName);
    }

    public StringFilter positionName() {
        if (positionName == null) {
            setPositionName(new StringFilter());
        }
        return positionName;
    }

    public void setPositionName(StringFilter positionName) {
        this.positionName = positionName;
    }

    public PositionTypeFilter getPositionType() {
        return positionType;
    }

    public Optional<PositionTypeFilter> optionalPositionType() {
        return Optional.ofNullable(positionType);
    }

    public PositionTypeFilter positionType() {
        if (positionType == null) {
            setPositionType(new PositionTypeFilter());
        }
        return positionType;
    }

    public void setPositionType(PositionTypeFilter positionType) {
        this.positionType = positionType;
    }

    public ImportanceLevelFilter getBusinessImportance() {
        return businessImportance;
    }

    public Optional<ImportanceLevelFilter> optionalBusinessImportance() {
        return Optional.ofNullable(businessImportance);
    }

    public ImportanceLevelFilter businessImportance() {
        if (businessImportance == null) {
            setBusinessImportance(new ImportanceLevelFilter());
        }
        return businessImportance;
    }

    public void setBusinessImportance(ImportanceLevelFilter businessImportance) {
        this.businessImportance = businessImportance;
    }

    public BooleanFilter getKeyPosition() {
        return keyPosition;
    }

    public Optional<BooleanFilter> optionalKeyPosition() {
        return Optional.ofNullable(keyPosition);
    }

    public BooleanFilter keyPosition() {
        if (keyPosition == null) {
            setKeyPosition(new BooleanFilter());
        }
        return keyPosition;
    }

    public void setKeyPosition(BooleanFilter keyPosition) {
        this.keyPosition = keyPosition;
    }

    public IntegerFilter getPlannedHeadcount() {
        return plannedHeadcount;
    }

    public Optional<IntegerFilter> optionalPlannedHeadcount() {
        return Optional.ofNullable(plannedHeadcount);
    }

    public IntegerFilter plannedHeadcount() {
        if (plannedHeadcount == null) {
            setPlannedHeadcount(new IntegerFilter());
        }
        return plannedHeadcount;
    }

    public void setPlannedHeadcount(IntegerFilter plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public IntegerFilter getMinimumOwnerCount() {
        return minimumOwnerCount;
    }

    public Optional<IntegerFilter> optionalMinimumOwnerCount() {
        return Optional.ofNullable(minimumOwnerCount);
    }

    public IntegerFilter minimumOwnerCount() {
        if (minimumOwnerCount == null) {
            setMinimumOwnerCount(new IntegerFilter());
        }
        return minimumOwnerCount;
    }

    public void setMinimumOwnerCount(IntegerFilter minimumOwnerCount) {
        this.minimumOwnerCount = minimumOwnerCount;
    }

    public ReviewCycleFilter getReviewCycle() {
        return reviewCycle;
    }

    public Optional<ReviewCycleFilter> optionalReviewCycle() {
        return Optional.ofNullable(reviewCycle);
    }

    public ReviewCycleFilter reviewCycle() {
        if (reviewCycle == null) {
            setReviewCycle(new ReviewCycleFilter());
        }
        return reviewCycle;
    }

    public void setReviewCycle(ReviewCycleFilter reviewCycle) {
        this.reviewCycle = reviewCycle;
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
        final PositionCriteria that = (PositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(positionCode, that.positionCode) &&
            Objects.equals(positionName, that.positionName) &&
            Objects.equals(positionType, that.positionType) &&
            Objects.equals(businessImportance, that.businessImportance) &&
            Objects.equals(keyPosition, that.keyPosition) &&
            Objects.equals(plannedHeadcount, that.plannedHeadcount) &&
            Objects.equals(minimumOwnerCount, that.minimumOwnerCount) &&
            Objects.equals(reviewCycle, that.reviewCycle) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            positionCode,
            positionName,
            positionType,
            businessImportance,
            keyPosition,
            plannedHeadcount,
            minimumOwnerCount,
            reviewCycle,
            active,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPositionCode().map(f -> "positionCode=" + f + ", ").orElse("") +
            optionalPositionName().map(f -> "positionName=" + f + ", ").orElse("") +
            optionalPositionType().map(f -> "positionType=" + f + ", ").orElse("") +
            optionalBusinessImportance().map(f -> "businessImportance=" + f + ", ").orElse("") +
            optionalKeyPosition().map(f -> "keyPosition=" + f + ", ").orElse("") +
            optionalPlannedHeadcount().map(f -> "plannedHeadcount=" + f + ", ").orElse("") +
            optionalMinimumOwnerCount().map(f -> "minimumOwnerCount=" + f + ", ").orElse("") +
            optionalReviewCycle().map(f -> "reviewCycle=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
