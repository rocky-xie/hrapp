package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.RecruitmentRequisitionMember} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.RecruitmentRequisitionMemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recruitment-requisition-members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionMemberCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private StringFilter memberType;

    private BooleanFilter active;

    private InstantFilter createdAt;

    private InstantFilter removedAt;

    private LongFilter requisitionId;

    private Boolean distinct;

    public RecruitmentRequisitionMemberCriteria() {}

    public RecruitmentRequisitionMemberCriteria(RecruitmentRequisitionMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.memberType = other.optionalMemberType().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.removedAt = other.optionalRemovedAt().map(InstantFilter::copy).orElse(null);
        this.requisitionId = other.optionalRequisitionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecruitmentRequisitionMemberCriteria copy() {
        return new RecruitmentRequisitionMemberCriteria(this);
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

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getMemberType() {
        return memberType;
    }

    public Optional<StringFilter> optionalMemberType() {
        return Optional.ofNullable(memberType);
    }

    public StringFilter memberType() {
        if (memberType == null) {
            setMemberType(new StringFilter());
        }
        return memberType;
    }

    public void setMemberType(StringFilter memberType) {
        this.memberType = memberType;
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

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getRemovedAt() {
        return removedAt;
    }

    public Optional<InstantFilter> optionalRemovedAt() {
        return Optional.ofNullable(removedAt);
    }

    public InstantFilter removedAt() {
        if (removedAt == null) {
            setRemovedAt(new InstantFilter());
        }
        return removedAt;
    }

    public void setRemovedAt(InstantFilter removedAt) {
        this.removedAt = removedAt;
    }

    public LongFilter getRequisitionId() {
        return requisitionId;
    }

    public Optional<LongFilter> optionalRequisitionId() {
        return Optional.ofNullable(requisitionId);
    }

    public LongFilter requisitionId() {
        if (requisitionId == null) {
            setRequisitionId(new LongFilter());
        }
        return requisitionId;
    }

    public void setRequisitionId(LongFilter requisitionId) {
        this.requisitionId = requisitionId;
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
        final RecruitmentRequisitionMemberCriteria that = (RecruitmentRequisitionMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(memberType, that.memberType) &&
            Objects.equals(active, that.active) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(requisitionId, that.requisitionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, memberType, active, createdAt, removedAt, requisitionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionMemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalMemberType().map(f -> "memberType=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalRemovedAt().map(f -> "removedAt=" + f + ", ").orElse("") +
            optionalRequisitionId().map(f -> "requisitionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
