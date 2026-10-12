package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.HiringDecision} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.HiringDecisionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hiring-decisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiringDecisionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ApplicationDecision
     */
    public static class ApplicationDecisionFilter extends Filter<ApplicationDecision> {

        public ApplicationDecisionFilter() {}

        public ApplicationDecisionFilter(ApplicationDecisionFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationDecisionFilter copy() {
            return new ApplicationDecisionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApplicationDecisionFilter decision;

    private StringFilter decisionSource;

    private StringFilter decidedBy;

    private InstantFilter decidedAt;

    private LongFilter correctionOfId;

    private BooleanFilter active;

    private LongFilter applicationId;

    private Boolean distinct;

    public HiringDecisionCriteria() {}

    public HiringDecisionCriteria(HiringDecisionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.decision = other.optionalDecision().map(ApplicationDecisionFilter::copy).orElse(null);
        this.decisionSource = other.optionalDecisionSource().map(StringFilter::copy).orElse(null);
        this.decidedBy = other.optionalDecidedBy().map(StringFilter::copy).orElse(null);
        this.decidedAt = other.optionalDecidedAt().map(InstantFilter::copy).orElse(null);
        this.correctionOfId = other.optionalCorrectionOfId().map(LongFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HiringDecisionCriteria copy() {
        return new HiringDecisionCriteria(this);
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

    public ApplicationDecisionFilter getDecision() {
        return decision;
    }

    public Optional<ApplicationDecisionFilter> optionalDecision() {
        return Optional.ofNullable(decision);
    }

    public ApplicationDecisionFilter decision() {
        if (decision == null) {
            setDecision(new ApplicationDecisionFilter());
        }
        return decision;
    }

    public void setDecision(ApplicationDecisionFilter decision) {
        this.decision = decision;
    }

    public StringFilter getDecisionSource() {
        return decisionSource;
    }

    public Optional<StringFilter> optionalDecisionSource() {
        return Optional.ofNullable(decisionSource);
    }

    public StringFilter decisionSource() {
        if (decisionSource == null) {
            setDecisionSource(new StringFilter());
        }
        return decisionSource;
    }

    public void setDecisionSource(StringFilter decisionSource) {
        this.decisionSource = decisionSource;
    }

    public StringFilter getDecidedBy() {
        return decidedBy;
    }

    public Optional<StringFilter> optionalDecidedBy() {
        return Optional.ofNullable(decidedBy);
    }

    public StringFilter decidedBy() {
        if (decidedBy == null) {
            setDecidedBy(new StringFilter());
        }
        return decidedBy;
    }

    public void setDecidedBy(StringFilter decidedBy) {
        this.decidedBy = decidedBy;
    }

    public InstantFilter getDecidedAt() {
        return decidedAt;
    }

    public Optional<InstantFilter> optionalDecidedAt() {
        return Optional.ofNullable(decidedAt);
    }

    public InstantFilter decidedAt() {
        if (decidedAt == null) {
            setDecidedAt(new InstantFilter());
        }
        return decidedAt;
    }

    public void setDecidedAt(InstantFilter decidedAt) {
        this.decidedAt = decidedAt;
    }

    public LongFilter getCorrectionOfId() {
        return correctionOfId;
    }

    public Optional<LongFilter> optionalCorrectionOfId() {
        return Optional.ofNullable(correctionOfId);
    }

    public LongFilter correctionOfId() {
        if (correctionOfId == null) {
            setCorrectionOfId(new LongFilter());
        }
        return correctionOfId;
    }

    public void setCorrectionOfId(LongFilter correctionOfId) {
        this.correctionOfId = correctionOfId;
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
        final HiringDecisionCriteria that = (HiringDecisionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(decision, that.decision) &&
            Objects.equals(decisionSource, that.decisionSource) &&
            Objects.equals(decidedBy, that.decidedBy) &&
            Objects.equals(decidedAt, that.decidedAt) &&
            Objects.equals(correctionOfId, that.correctionOfId) &&
            Objects.equals(active, that.active) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, decision, decisionSource, decidedBy, decidedAt, correctionOfId, active, applicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HiringDecisionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDecision().map(f -> "decision=" + f + ", ").orElse("") +
            optionalDecisionSource().map(f -> "decisionSource=" + f + ", ").orElse("") +
            optionalDecidedBy().map(f -> "decidedBy=" + f + ", ").orElse("") +
            optionalDecidedAt().map(f -> "decidedAt=" + f + ", ").orElse("") +
            optionalCorrectionOfId().map(f -> "correctionOfId=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
