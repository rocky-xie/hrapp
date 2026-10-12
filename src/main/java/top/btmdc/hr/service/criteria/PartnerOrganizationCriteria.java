package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PartnerOrganization} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PartnerOrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /partner-organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerOrganizationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter partnerCode;

    private StringFilter name;

    private BooleanFilter dispatchEnabled;

    private IntegerFilter defaultObservationDays;

    private BooleanFilter active;

    private Boolean distinct;

    public PartnerOrganizationCriteria() {}

    public PartnerOrganizationCriteria(PartnerOrganizationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.partnerCode = other.optionalPartnerCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.dispatchEnabled = other.optionalDispatchEnabled().map(BooleanFilter::copy).orElse(null);
        this.defaultObservationDays = other.optionalDefaultObservationDays().map(IntegerFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PartnerOrganizationCriteria copy() {
        return new PartnerOrganizationCriteria(this);
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

    public StringFilter getPartnerCode() {
        return partnerCode;
    }

    public Optional<StringFilter> optionalPartnerCode() {
        return Optional.ofNullable(partnerCode);
    }

    public StringFilter partnerCode() {
        if (partnerCode == null) {
            setPartnerCode(new StringFilter());
        }
        return partnerCode;
    }

    public void setPartnerCode(StringFilter partnerCode) {
        this.partnerCode = partnerCode;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getDispatchEnabled() {
        return dispatchEnabled;
    }

    public Optional<BooleanFilter> optionalDispatchEnabled() {
        return Optional.ofNullable(dispatchEnabled);
    }

    public BooleanFilter dispatchEnabled() {
        if (dispatchEnabled == null) {
            setDispatchEnabled(new BooleanFilter());
        }
        return dispatchEnabled;
    }

    public void setDispatchEnabled(BooleanFilter dispatchEnabled) {
        this.dispatchEnabled = dispatchEnabled;
    }

    public IntegerFilter getDefaultObservationDays() {
        return defaultObservationDays;
    }

    public Optional<IntegerFilter> optionalDefaultObservationDays() {
        return Optional.ofNullable(defaultObservationDays);
    }

    public IntegerFilter defaultObservationDays() {
        if (defaultObservationDays == null) {
            setDefaultObservationDays(new IntegerFilter());
        }
        return defaultObservationDays;
    }

    public void setDefaultObservationDays(IntegerFilter defaultObservationDays) {
        this.defaultObservationDays = defaultObservationDays;
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
        final PartnerOrganizationCriteria that = (PartnerOrganizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(partnerCode, that.partnerCode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dispatchEnabled, that.dispatchEnabled) &&
            Objects.equals(defaultObservationDays, that.defaultObservationDays) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partnerCode, name, dispatchEnabled, defaultObservationDays, active, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerOrganizationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPartnerCode().map(f -> "partnerCode=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDispatchEnabled().map(f -> "dispatchEnabled=" + f + ", ").orElse("") +
            optionalDefaultObservationDays().map(f -> "defaultObservationDays=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
