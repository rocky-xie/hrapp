package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.Recommendation;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PositionMatch} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PositionMatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /position-matches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionMatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReadinessLevel
     */
    public static class ReadinessLevelFilter extends Filter<ReadinessLevel> {

        public ReadinessLevelFilter() {}

        public ReadinessLevelFilter(ReadinessLevelFilter filter) {
            super(filter);
        }

        @Override
        public ReadinessLevelFilter copy() {
            return new ReadinessLevelFilter(this);
        }
    }

    /**
     * Class for filtering Recommendation
     */
    public static class RecommendationFilter extends Filter<Recommendation> {

        public RecommendationFilter() {}

        public RecommendationFilter(RecommendationFilter filter) {
            super(filter);
        }

        @Override
        public RecommendationFilter copy() {
            return new RecommendationFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter matchScore;

    private ReadinessLevelFilter readiness;

    private RecommendationFilter recommendation;

    private LocalDateFilter analysisDate;

    private LongFilter personId;

    private LongFilter positionId;

    private Boolean distinct;

    public PositionMatchCriteria() {}

    public PositionMatchCriteria(PositionMatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.matchScore = other.optionalMatchScore().map(IntegerFilter::copy).orElse(null);
        this.readiness = other.optionalReadiness().map(ReadinessLevelFilter::copy).orElse(null);
        this.recommendation = other.optionalRecommendation().map(RecommendationFilter::copy).orElse(null);
        this.analysisDate = other.optionalAnalysisDate().map(LocalDateFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionMatchCriteria copy() {
        return new PositionMatchCriteria(this);
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

    public IntegerFilter getMatchScore() {
        return matchScore;
    }

    public Optional<IntegerFilter> optionalMatchScore() {
        return Optional.ofNullable(matchScore);
    }

    public IntegerFilter matchScore() {
        if (matchScore == null) {
            setMatchScore(new IntegerFilter());
        }
        return matchScore;
    }

    public void setMatchScore(IntegerFilter matchScore) {
        this.matchScore = matchScore;
    }

    public ReadinessLevelFilter getReadiness() {
        return readiness;
    }

    public Optional<ReadinessLevelFilter> optionalReadiness() {
        return Optional.ofNullable(readiness);
    }

    public ReadinessLevelFilter readiness() {
        if (readiness == null) {
            setReadiness(new ReadinessLevelFilter());
        }
        return readiness;
    }

    public void setReadiness(ReadinessLevelFilter readiness) {
        this.readiness = readiness;
    }

    public RecommendationFilter getRecommendation() {
        return recommendation;
    }

    public Optional<RecommendationFilter> optionalRecommendation() {
        return Optional.ofNullable(recommendation);
    }

    public RecommendationFilter recommendation() {
        if (recommendation == null) {
            setRecommendation(new RecommendationFilter());
        }
        return recommendation;
    }

    public void setRecommendation(RecommendationFilter recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateFilter getAnalysisDate() {
        return analysisDate;
    }

    public Optional<LocalDateFilter> optionalAnalysisDate() {
        return Optional.ofNullable(analysisDate);
    }

    public LocalDateFilter analysisDate() {
        if (analysisDate == null) {
            setAnalysisDate(new LocalDateFilter());
        }
        return analysisDate;
    }

    public void setAnalysisDate(LocalDateFilter analysisDate) {
        this.analysisDate = analysisDate;
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
        final PositionMatchCriteria that = (PositionMatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matchScore, that.matchScore) &&
            Objects.equals(readiness, that.readiness) &&
            Objects.equals(recommendation, that.recommendation) &&
            Objects.equals(analysisDate, that.analysisDate) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchScore, readiness, recommendation, analysisDate, personId, positionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionMatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMatchScore().map(f -> "matchScore=" + f + ", ").orElse("") +
            optionalReadiness().map(f -> "readiness=" + f + ", ").orElse("") +
            optionalRecommendation().map(f -> "recommendation=" + f + ", ").orElse("") +
            optionalAnalysisDate().map(f -> "analysisDate=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
