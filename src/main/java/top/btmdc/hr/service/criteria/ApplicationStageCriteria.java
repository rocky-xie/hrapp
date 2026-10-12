package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.ApplicationStage} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.ApplicationStageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-stages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationStageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ApplicationStageType
     */
    public static class ApplicationStageTypeFilter extends Filter<ApplicationStageType> {

        public ApplicationStageTypeFilter() {}

        public ApplicationStageTypeFilter(ApplicationStageTypeFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationStageTypeFilter copy() {
            return new ApplicationStageTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApplicationStageTypeFilter stageType;

    private IntegerFilter sequenceNumber;

    private StringFilter stageStatus;

    private InstantFilter startedAt;

    private InstantFilter completedAt;

    private BooleanFilter selected;

    private LongFilter applicationId;

    private Boolean distinct;

    public ApplicationStageCriteria() {}

    public ApplicationStageCriteria(ApplicationStageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stageType = other.optionalStageType().map(ApplicationStageTypeFilter::copy).orElse(null);
        this.sequenceNumber = other.optionalSequenceNumber().map(IntegerFilter::copy).orElse(null);
        this.stageStatus = other.optionalStageStatus().map(StringFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.selected = other.optionalSelected().map(BooleanFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationStageCriteria copy() {
        return new ApplicationStageCriteria(this);
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

    public ApplicationStageTypeFilter getStageType() {
        return stageType;
    }

    public Optional<ApplicationStageTypeFilter> optionalStageType() {
        return Optional.ofNullable(stageType);
    }

    public ApplicationStageTypeFilter stageType() {
        if (stageType == null) {
            setStageType(new ApplicationStageTypeFilter());
        }
        return stageType;
    }

    public void setStageType(ApplicationStageTypeFilter stageType) {
        this.stageType = stageType;
    }

    public IntegerFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public Optional<IntegerFilter> optionalSequenceNumber() {
        return Optional.ofNullable(sequenceNumber);
    }

    public IntegerFilter sequenceNumber() {
        if (sequenceNumber == null) {
            setSequenceNumber(new IntegerFilter());
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(IntegerFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public StringFilter getStageStatus() {
        return stageStatus;
    }

    public Optional<StringFilter> optionalStageStatus() {
        return Optional.ofNullable(stageStatus);
    }

    public StringFilter stageStatus() {
        if (stageStatus == null) {
            setStageStatus(new StringFilter());
        }
        return stageStatus;
    }

    public void setStageStatus(StringFilter stageStatus) {
        this.stageStatus = stageStatus;
    }

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public BooleanFilter getSelected() {
        return selected;
    }

    public Optional<BooleanFilter> optionalSelected() {
        return Optional.ofNullable(selected);
    }

    public BooleanFilter selected() {
        if (selected == null) {
            setSelected(new BooleanFilter());
        }
        return selected;
    }

    public void setSelected(BooleanFilter selected) {
        this.selected = selected;
    }

    public LongFilter getApplicationId() {
        return applicationId;
    }

    public Optional<LongFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public LongFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new LongFilter());
        }
        return applicationId;
    }

    public void setApplicationId(LongFilter applicationId) {
        this.applicationId = applicationId;
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
        final ApplicationStageCriteria that = (ApplicationStageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stageType, that.stageType) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(stageStatus, that.stageStatus) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(selected, that.selected) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageType, sequenceNumber, stageStatus, startedAt, completedAt, selected, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationStageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStageType().map(f -> "stageType=" + f + ", ").orElse("") +
            optionalSequenceNumber().map(f -> "sequenceNumber=" + f + ", ").orElse("") +
            optionalStageStatus().map(f -> "stageStatus=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalSelected().map(f -> "selected=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
