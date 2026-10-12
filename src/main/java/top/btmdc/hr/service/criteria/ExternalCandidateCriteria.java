package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.ExternalCandidate} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.ExternalCandidateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /external-candidates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalCandidateCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter nameReading;

    private StringFilter englishName;

    private LocalDateFilter birthDate;

    private StringFilter gender;

    private StringFilter phone;

    private StringFilter email;

    private LongFilter convertedPersonId;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter resumesId;

    private LongFilter applicationsId;

    private Boolean distinct;

    public ExternalCandidateCriteria() {}

    public ExternalCandidateCriteria(ExternalCandidateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.nameReading = other.optionalNameReading().map(StringFilter::copy).orElse(null);
        this.englishName = other.optionalEnglishName().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.convertedPersonId = other.optionalConvertedPersonId().map(LongFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.resumesId = other.optionalResumesId().map(LongFilter::copy).orElse(null);
        this.applicationsId = other.optionalApplicationsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExternalCandidateCriteria copy() {
        return new ExternalCandidateCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getNameReading() {
        return nameReading;
    }

    public Optional<StringFilter> optionalNameReading() {
        return Optional.ofNullable(nameReading);
    }

    public StringFilter nameReading() {
        if (nameReading == null) {
            setNameReading(new StringFilter());
        }
        return nameReading;
    }

    public void setNameReading(StringFilter nameReading) {
        this.nameReading = nameReading;
    }

    public StringFilter getEnglishName() {
        return englishName;
    }

    public Optional<StringFilter> optionalEnglishName() {
        return Optional.ofNullable(englishName);
    }

    public StringFilter englishName() {
        if (englishName == null) {
            setEnglishName(new StringFilter());
        }
        return englishName;
    }

    public void setEnglishName(StringFilter englishName) {
        this.englishName = englishName;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getGender() {
        return gender;
    }

    public Optional<StringFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public StringFilter gender() {
        if (gender == null) {
            setGender(new StringFilter());
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getConvertedPersonId() {
        return convertedPersonId;
    }

    public Optional<LongFilter> optionalConvertedPersonId() {
        return Optional.ofNullable(convertedPersonId);
    }

    public LongFilter convertedPersonId() {
        if (convertedPersonId == null) {
            setConvertedPersonId(new LongFilter());
        }
        return convertedPersonId;
    }

    public void setConvertedPersonId(LongFilter convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
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

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getResumesId() {
        return resumesId;
    }

    public Optional<LongFilter> optionalResumesId() {
        return Optional.ofNullable(resumesId);
    }

    public LongFilter resumesId() {
        if (resumesId == null) {
            setResumesId(new LongFilter());
        }
        return resumesId;
    }

    public void setResumesId(LongFilter resumesId) {
        this.resumesId = resumesId;
    }

    public LongFilter getApplicationsId() {
        return applicationsId;
    }

    public Optional<LongFilter> optionalApplicationsId() {
        return Optional.ofNullable(applicationsId);
    }

    public LongFilter applicationsId() {
        if (applicationsId == null) {
            setApplicationsId(new LongFilter());
        }
        return applicationsId;
    }

    public void setApplicationsId(LongFilter applicationsId) {
        this.applicationsId = applicationsId;
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
        final ExternalCandidateCriteria that = (ExternalCandidateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(nameReading, that.nameReading) &&
            Objects.equals(englishName, that.englishName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(convertedPersonId, that.convertedPersonId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(resumesId, that.resumesId) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fullName,
            nameReading,
            englishName,
            birthDate,
            gender,
            phone,
            email,
            convertedPersonId,
            createdAt,
            updatedAt,
            resumesId,
            applicationsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalCandidateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalNameReading().map(f -> "nameReading=" + f + ", ").orElse("") +
            optionalEnglishName().map(f -> "englishName=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalConvertedPersonId().map(f -> "convertedPersonId=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalResumesId().map(f -> "resumesId=" + f + ", ").orElse("") +
            optionalApplicationsId().map(f -> "applicationsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
