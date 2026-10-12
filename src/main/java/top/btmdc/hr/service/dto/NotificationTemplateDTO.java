package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.RecruitmentType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.NotificationTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String eventType;

    private RecruitmentType recruitmentType;

    @NotNull
    @Size(max = 10)
    private String language;

    @NotNull
    @Min(value = 1)
    private Integer versionNumber;

    @NotNull
    @Size(max = 255)
    private String subjectTemplate;

    @Lob
    private String bodyTemplate;

    @NotNull
    private Boolean externalSafe;

    @NotNull
    private Boolean published;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public RecruitmentType getRecruitmentType() {
        return recruitmentType;
    }

    public void setRecruitmentType(RecruitmentType recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public Boolean getExternalSafe() {
        return externalSafe;
    }

    public void setExternalSafe(Boolean externalSafe) {
        this.externalSafe = externalSafe;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationTemplateDTO)) {
            return false;
        }

        NotificationTemplateDTO notificationTemplateDTO = (NotificationTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationTemplateDTO{" +
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
