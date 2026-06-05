package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.TrainingType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.TrainingRecord} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.TrainingRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /training-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingRecordCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TrainingType
     */
    public static class TrainingTypeFilter extends Filter<TrainingType> {

        public TrainingTypeFilter() {}

        public TrainingTypeFilter(TrainingTypeFilter filter) {
            super(filter);
        }

        @Override
        public TrainingTypeFilter copy() {
            return new TrainingTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter trainingDate;

    private TrainingTypeFilter trainingType;

    private StringFilter topic;

    private LongFilter personId;

    private LongFilter trainingGoalId;

    private LongFilter positionId;

    private LongFilter mentorId;

    private Boolean distinct;

    public TrainingRecordCriteria() {}

    public TrainingRecordCriteria(TrainingRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.trainingDate = other.optionalTrainingDate().map(LocalDateFilter::copy).orElse(null);
        this.trainingType = other.optionalTrainingType().map(TrainingTypeFilter::copy).orElse(null);
        this.topic = other.optionalTopic().map(StringFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.trainingGoalId = other.optionalTrainingGoalId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.mentorId = other.optionalMentorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrainingRecordCriteria copy() {
        return new TrainingRecordCriteria(this);
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

    public LocalDateFilter getTrainingDate() {
        return trainingDate;
    }

    public Optional<LocalDateFilter> optionalTrainingDate() {
        return Optional.ofNullable(trainingDate);
    }

    public LocalDateFilter trainingDate() {
        if (trainingDate == null) {
            setTrainingDate(new LocalDateFilter());
        }
        return trainingDate;
    }

    public void setTrainingDate(LocalDateFilter trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingTypeFilter getTrainingType() {
        return trainingType;
    }

    public Optional<TrainingTypeFilter> optionalTrainingType() {
        return Optional.ofNullable(trainingType);
    }

    public TrainingTypeFilter trainingType() {
        if (trainingType == null) {
            setTrainingType(new TrainingTypeFilter());
        }
        return trainingType;
    }

    public void setTrainingType(TrainingTypeFilter trainingType) {
        this.trainingType = trainingType;
    }

    public StringFilter getTopic() {
        return topic;
    }

    public Optional<StringFilter> optionalTopic() {
        return Optional.ofNullable(topic);
    }

    public StringFilter topic() {
        if (topic == null) {
            setTopic(new StringFilter());
        }
        return topic;
    }

    public void setTopic(StringFilter topic) {
        this.topic = topic;
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

    public LongFilter getTrainingGoalId() {
        return trainingGoalId;
    }

    public Optional<LongFilter> optionalTrainingGoalId() {
        return Optional.ofNullable(trainingGoalId);
    }

    public LongFilter trainingGoalId() {
        if (trainingGoalId == null) {
            setTrainingGoalId(new LongFilter());
        }
        return trainingGoalId;
    }

    public void setTrainingGoalId(LongFilter trainingGoalId) {
        this.trainingGoalId = trainingGoalId;
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

    public LongFilter getMentorId() {
        return mentorId;
    }

    public Optional<LongFilter> optionalMentorId() {
        return Optional.ofNullable(mentorId);
    }

    public LongFilter mentorId() {
        if (mentorId == null) {
            setMentorId(new LongFilter());
        }
        return mentorId;
    }

    public void setMentorId(LongFilter mentorId) {
        this.mentorId = mentorId;
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
        final TrainingRecordCriteria that = (TrainingRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(trainingDate, that.trainingDate) &&
            Objects.equals(trainingType, that.trainingType) &&
            Objects.equals(topic, that.topic) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(trainingGoalId, that.trainingGoalId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(mentorId, that.mentorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingDate, trainingType, topic, personId, trainingGoalId, positionId, mentorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingRecordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTrainingDate().map(f -> "trainingDate=" + f + ", ").orElse("") +
            optionalTrainingType().map(f -> "trainingType=" + f + ", ").orElse("") +
            optionalTopic().map(f -> "topic=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalTrainingGoalId().map(f -> "trainingGoalId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalMentorId().map(f -> "mentorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
