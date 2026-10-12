package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * A NotificationTemplate.
 */
@Entity
@Table(name = "notification_template")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationTemplate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "event_type", length = 60, nullable = false)
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_type")
    private RecruitmentType recruitmentType;

    @NotNull
    @Size(max = 10)
    @Column(name = "language", length = 10, nullable = false)
    private String language;

    @NotNull
    @Min(value = 1)
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "subject_template", length = 255, nullable = false)
    private String subjectTemplate;

    @Lob
    @Column(name = "body_template", nullable = false)
    private String bodyTemplate;

    @NotNull
    @Column(name = "external_safe", nullable = false)
    private Boolean externalSafe;

    @NotNull
    @Column(name = "published", nullable = false)
    private Boolean published;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public NotificationTemplate eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public RecruitmentType getRecruitmentType() {
        return this.recruitmentType;
    }

    public NotificationTemplate recruitmentType(RecruitmentType recruitmentType) {
        this.setRecruitmentType(recruitmentType);
        return this;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public String getLanguage() {
        return this.language;
    }

    public NotificationTemplate language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getVersionNumber() {
        return this.versionNumber;
    }

    public NotificationTemplate versionNumber(Integer versionNumber) {
        this.setVersionNumber(versionNumber);
        return this;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getSubjectTemplate() {
        return this.subjectTemplate;
    }

    public NotificationTemplate subjectTemplate(String subjectTemplate) {
        this.setSubjectTemplate(subjectTemplate);
        return this;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public String getBodyTemplate() {
        return this.bodyTemplate;
    }

    public NotificationTemplate bodyTemplate(String bodyTemplate) {
        this.setBodyTemplate(bodyTemplate);
        return this;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public Boolean getExternalSafe() {
        return this.externalSafe;
    }

    public NotificationTemplate externalSafe(Boolean externalSafe) {
        this.setExternalSafe(externalSafe);
        return this;
    }

    public void setExternalSafe(Boolean externalSafe) {
        this.externalSafe = externalSafe;
    }

    public Boolean getPublished() {
        return this.published;
    }

    public NotificationTemplate published(Boolean published) {
        this.setPublished(published);
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationTemplate{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", recruitmentType='" + getRecruitmentType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", versionNumber=" + getVersionNumber() +
            ", subjectTemplate='" + getSubjectTemplate() + "'" +
            ", bodyTemplate='" + getBodyTemplate() + "'" +
            ", externalSafe='" + getExternalSafe() + "'" +
            ", published='" + getPublished() + "'" +
            "}";
    }
}
