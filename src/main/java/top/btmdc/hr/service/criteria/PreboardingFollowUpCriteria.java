package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PreboardingFollowUp} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PreboardingFollowUpResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /preboarding-follow-ups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreboardingFollowUpCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter candidateId;

    private LongFilter personId;

    private InstantFilter plannedAt;

    private InstantFilter contactedAt;

    private StringFilter ownerUsername;

    private StringFilter contactMethod;

    private StringFilter candidateIntent;

    private StringFilter riskLevel;

    private InstantFilter nextContactAt;

    private LongFilter applicationId;

    private Boolean distinct;

    public PreboardingFollowUpCriteria() {}

    public PreboardingFollowUpCriteria(PreboardingFollowUpCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.candidateId = other.optionalCandidateId().map(LongFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.plannedAt = other.optionalPlannedAt().map(InstantFilter::copy).orElse(null);
        this.contactedAt = other.optionalContactedAt().map(InstantFilter::copy).orElse(null);
        this.ownerUsername = other.optionalOwnerUsername().map(StringFilter::copy).orElse(null);
        this.contactMethod = other.optionalContactMethod().map(StringFilter::copy).orElse(null);
        this.candidateIntent = other.optionalCandidateIntent().map(StringFilter::copy).orElse(null);
        this.riskLevel = other.optionalRiskLevel().map(StringFilter::copy).orElse(null);
        this.nextContactAt = other.optionalNextContactAt().map(InstantFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PreboardingFollowUpCriteria copy() {
        return new PreboardingFollowUpCriteria(this);
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

    public LongFilter getCandidateId() {
        return candidateId;
    }

    public Optional<LongFilter> optionalCandidateId() {
        return Optional.ofNullable(candidateId);
    }

    public LongFilter candidateId() {
        if (candidateId == null) {
            setCandidateId(new LongFilter());
        }
        return candidateId;
    }

    public void setCandidateId(LongFilter candidateId) {
        this.candidateId = candidateId;
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

    public InstantFilter getPlannedAt() {
        return plannedAt;
    }

    public Optional<InstantFilter> optionalPlannedAt() {
        return Optional.ofNullable(plannedAt);
    }

    public InstantFilter plannedAt() {
        if (plannedAt == null) {
            setPlannedAt(new InstantFilter());
        }
        return plannedAt;
    }

    public void setPlannedAt(InstantFilter plannedAt) {
        this.plannedAt = plannedAt;
    }

    public InstantFilter getContactedAt() {
        return contactedAt;
    }

    public Optional<InstantFilter> optionalContactedAt() {
        return Optional.ofNullable(contactedAt);
    }

    public InstantFilter contactedAt() {
        if (contactedAt == null) {
            setContactedAt(new InstantFilter());
        }
        return contactedAt;
    }

    public void setContactedAt(InstantFilter contactedAt) {
        this.contactedAt = contactedAt;
    }

    public StringFilter getOwnerUsername() {
        return ownerUsername;
    }

    public Optional<StringFilter> optionalOwnerUsername() {
        return Optional.ofNullable(ownerUsername);
    }

    public StringFilter ownerUsername() {
        if (ownerUsername == null) {
            setOwnerUsername(new StringFilter());
        }
        return ownerUsername;
    }

    public void setOwnerUsername(StringFilter ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public StringFilter getContactMethod() {
        return contactMethod;
    }

    public Optional<StringFilter> optionalContactMethod() {
        return Optional.ofNullable(contactMethod);
    }

    public StringFilter contactMethod() {
        if (contactMethod == null) {
            setContactMethod(new StringFilter());
        }
        return contactMethod;
    }

    public void setContactMethod(StringFilter contactMethod) {
        this.contactMethod = contactMethod;
    }

    public StringFilter getCandidateIntent() {
        return candidateIntent;
    }

    public Optional<StringFilter> optionalCandidateIntent() {
        return Optional.ofNullable(candidateIntent);
    }

    public StringFilter candidateIntent() {
        if (candidateIntent == null) {
            setCandidateIntent(new StringFilter());
        }
        return candidateIntent;
    }

    public void setCandidateIntent(StringFilter candidateIntent) {
        this.candidateIntent = candidateIntent;
    }

    public StringFilter getRiskLevel() {
        return riskLevel;
    }

    public Optional<StringFilter> optionalRiskLevel() {
        return Optional.ofNullable(riskLevel);
    }

    public StringFilter riskLevel() {
        if (riskLevel == null) {
            setRiskLevel(new StringFilter());
        }
        return riskLevel;
    }

    public void setRiskLevel(StringFilter riskLevel) {
        this.riskLevel = riskLevel;
    }

    public InstantFilter getNextContactAt() {
        return nextContactAt;
    }

    public Optional<InstantFilter> optionalNextContactAt() {
        return Optional.ofNullable(nextContactAt);
    }

    public InstantFilter nextContactAt() {
        if (nextContactAt == null) {
            setNextContactAt(new InstantFilter());
        }
        return nextContactAt;
    }

    public void setNextContactAt(InstantFilter nextContactAt) {
        this.nextContactAt = nextContactAt;
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
        final PreboardingFollowUpCriteria that = (PreboardingFollowUpCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(plannedAt, that.plannedAt) &&
            Objects.equals(contactedAt, that.contactedAt) &&
            Objects.equals(ownerUsername, that.ownerUsername) &&
            Objects.equals(contactMethod, that.contactMethod) &&
            Objects.equals(candidateIntent, that.candidateIntent) &&
            Objects.equals(riskLevel, that.riskLevel) &&
            Objects.equals(nextContactAt, that.nextContactAt) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            candidateId,
            personId,
            plannedAt,
            contactedAt,
            ownerUsername,
            contactMethod,
            candidateIntent,
            riskLevel,
            nextContactAt,
            applicationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreboardingFollowUpCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCandidateId().map(f -> "candidateId=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalPlannedAt().map(f -> "plannedAt=" + f + ", ").orElse("") +
            optionalContactedAt().map(f -> "contactedAt=" + f + ", ").orElse("") +
            optionalOwnerUsername().map(f -> "ownerUsername=" + f + ", ").orElse("") +
            optionalContactMethod().map(f -> "contactMethod=" + f + ", ").orElse("") +
            optionalCandidateIntent().map(f -> "candidateIntent=" + f + ", ").orElse("") +
            optionalRiskLevel().map(f -> "riskLevel=" + f + ", ").orElse("") +
            optionalNextContactAt().map(f -> "nextContactAt=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
