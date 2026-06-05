package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.TrustStage;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.TrustObservation} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.TrustObservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trust-observations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrustObservationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TrustStage
     */
    public static class TrustStageFilter extends Filter<TrustStage> {

        public TrustStageFilter() {}

        public TrustStageFilter(TrustStageFilter filter) {
            super(filter);
        }

        @Override
        public TrustStageFilter copy() {
            return new TrustStageFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter observationDate;

    private TrustStageFilter trustStage;

    private LongFilter personId;

    private LongFilter observerId;

    private Boolean distinct;

    public TrustObservationCriteria() {}

    public TrustObservationCriteria(TrustObservationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.observationDate = other.optionalObservationDate().map(LocalDateFilter::copy).orElse(null);
        this.trustStage = other.optionalTrustStage().map(TrustStageFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.observerId = other.optionalObserverId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrustObservationCriteria copy() {
        return new TrustObservationCriteria(this);
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

    public LocalDateFilter getObservationDate() {
        return observationDate;
    }

    public Optional<LocalDateFilter> optionalObservationDate() {
        return Optional.ofNullable(observationDate);
    }

    public LocalDateFilter observationDate() {
        if (observationDate == null) {
            setObservationDate(new LocalDateFilter());
        }
        return observationDate;
    }

    public void setObservationDate(LocalDateFilter observationDate) {
        this.observationDate = observationDate;
    }

    public TrustStageFilter getTrustStage() {
        return trustStage;
    }

    public Optional<TrustStageFilter> optionalTrustStage() {
        return Optional.ofNullable(trustStage);
    }

    public TrustStageFilter trustStage() {
        if (trustStage == null) {
            setTrustStage(new TrustStageFilter());
        }
        return trustStage;
    }

    public void setTrustStage(TrustStageFilter trustStage) {
        this.trustStage = trustStage;
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

    public LongFilter getObserverId() {
        return observerId;
    }

    public Optional<LongFilter> optionalObserverId() {
        return Optional.ofNullable(observerId);
    }

    public LongFilter observerId() {
        if (observerId == null) {
            setObserverId(new LongFilter());
        }
        return observerId;
    }

    public void setObserverId(LongFilter observerId) {
        this.observerId = observerId;
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
        final TrustObservationCriteria that = (TrustObservationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(observationDate, that.observationDate) &&
            Objects.equals(trustStage, that.trustStage) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(observerId, that.observerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, observationDate, trustStage, personId, observerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrustObservationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalObservationDate().map(f -> "observationDate=" + f + ", ").orElse("") +
            optionalTrustStage().map(f -> "trustStage=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalObserverId().map(f -> "observerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
