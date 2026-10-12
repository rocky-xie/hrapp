package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.NotificationTemplate} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.NotificationTemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationTemplateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RecruitmentType
     */
    public static class RecruitmentTypeFilter extends Filter<RecruitmentType> {

        public RecruitmentTypeFilter() {}

        public RecruitmentTypeFilter(RecruitmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public RecruitmentTypeFilter copy() {
            return new RecruitmentTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventType;

    private RecruitmentTypeFilter recruitmentType;

    private StringFilter language;

    private IntegerFilter versionNumber;

    private StringFilter subjectTemplate;

    private BooleanFilter externalSafe;

    private BooleanFilter published;

    private Boolean distinct;

    public NotificationTemplateCriteria() {}

    public NotificationTemplateCriteria(NotificationTemplateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(StringFilter::copy).orElse(null);
        this.recruitmentType = other.optionalRecruitmentType().map(RecruitmentTypeFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.versionNumber = other.optionalVersionNumber().map(IntegerFilter::copy).orElse(null);
        this.subjectTemplate = other.optionalSubjectTemplate().map(StringFilter::copy).orElse(null);
        this.externalSafe = other.optionalExternalSafe().map(BooleanFilter::copy).orElse(null);
        this.published = other.optionalPublished().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationTemplateCriteria copy() {
        return new NotificationTemplateCriteria(this);
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

    public StringFilter getEventType() {
        return eventType;
    }

    public Optional<StringFilter> optionalEventType() {
        return Optional.ofNullable(eventType);
    }

    public StringFilter eventType() {
        if (eventType == null) {
            setEventType(new StringFilter());
        }
        return eventType;
    }

    public void setEventType(StringFilter eventType) {
        this.eventType = eventType;
    }

    public RecruitmentTypeFilter getRecruitmentType() {
        return recruitmentType;
    }

    public Optional<RecruitmentTypeFilter> optionalRecruitmentType() {
        return Optional.ofNullable(recruitmentType);
    }

    public RecruitmentTypeFilter recruitmentType() {
        if (recruitmentType == null) {
            setRecruitmentType(new RecruitmentTypeFilter());
        }
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentTypeFilter recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public Optional<StringFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public StringFilter language() {
        if (language == null) {
            setLanguage(new StringFilter());
        }
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public IntegerFilter getVersionNumber() {
        return versionNumber;
    }

    public Optional<IntegerFilter> optionalVersionNumber() {
        return Optional.ofNullable(versionNumber);
    }

    public IntegerFilter versionNumber() {
        if (versionNumber == null) {
            setVersionNumber(new IntegerFilter());
        }
        return versionNumber;
    }

    public void setVersionNumber(IntegerFilter versionNumber) {
        this.versionNumber = versionNumber;
    }

    public StringFilter getSubjectTemplate() {
        return subjectTemplate;
    }

    public Optional<StringFilter> optionalSubjectTemplate() {
        return Optional.ofNullable(subjectTemplate);
    }

    public StringFilter subjectTemplate() {
        if (subjectTemplate == null) {
            setSubjectTemplate(new StringFilter());
        }
        return subjectTemplate;
    }

    public void setSubjectTemplate(StringFilter subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public BooleanFilter getExternalSafe() {
        return externalSafe;
    }

    public Optional<BooleanFilter> optionalExternalSafe() {
        return Optional.ofNullable(externalSafe);
    }

    public BooleanFilter externalSafe() {
        if (externalSafe == null) {
            setExternalSafe(new BooleanFilter());
        }
        return externalSafe;
    }

    public void setExternalSafe(BooleanFilter externalSafe) {
        this.externalSafe = externalSafe;
    }

    public BooleanFilter getPublished() {
        return published;
    }

    public Optional<BooleanFilter> optionalPublished() {
        return Optional.ofNullable(published);
    }

    public BooleanFilter published() {
        if (published == null) {
            setPublished(new BooleanFilter());
        }
        return published;
    }

    public void setPublished(BooleanFilter published) {
        this.published = published;
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
        final NotificationTemplateCriteria that = (NotificationTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(recruitmentType, that.recruitmentType) &&
            Objects.equals(language, that.language) &&
            Objects.equals(versionNumber, that.versionNumber) &&
            Objects.equals(subjectTemplate, that.subjectTemplate) &&
            Objects.equals(externalSafe, that.externalSafe) &&
            Objects.equals(published, that.published) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, recruitmentType, language, versionNumber, subjectTemplate, externalSafe, published, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationTemplateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalRecruitmentType().map(f -> "recruitmentType=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalVersionNumber().map(f -> "versionNumber=" + f + ", ").orElse("") +
            optionalSubjectTemplate().map(f -> "subjectTemplate=" + f + ", ").orElse("") +
            optionalExternalSafe().map(f -> "externalSafe=" + f + ", ").orElse("") +
            optionalPublished().map(f -> "published=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
