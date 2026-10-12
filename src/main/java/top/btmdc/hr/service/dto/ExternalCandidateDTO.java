package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.ExternalCandidate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalCandidateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String fullName;

    @Size(max = 150)
    private String nameReading;

    @Size(max = 150)
    private String englishName;

    private LocalDate birthDate;

    @Size(max = 30)
    private String gender;

    @Size(max = 50)
    private String phone;

    @Size(max = 254)
    private String email;

    @Lob
    private String address;

    @Lob
    private String educationSummary;

    @Lob
    private String workSummary;

    private Long convertedPersonId;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNameReading() {
        return nameReading;
    }

    public void setNameReading(String nameReading) {
        this.nameReading = nameReading;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducationSummary() {
        return educationSummary;
    }

    public void setEducationSummary(String educationSummary) {
        this.educationSummary = educationSummary;
    }

    public String getWorkSummary() {
        return workSummary;
    }

    public void setWorkSummary(String workSummary) {
        this.workSummary = workSummary;
    }

    public Long getConvertedPersonId() {
        return convertedPersonId;
    }

    public void setConvertedPersonId(Long convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalCandidateDTO)) {
            return false;
        }

        ExternalCandidateDTO externalCandidateDTO = (ExternalCandidateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, externalCandidateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalCandidateDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", nameReading='" + getNameReading() + "'" +
            ", englishName='" + getEnglishName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", educationSummary='" + getEducationSummary() + "'" +
            ", workSummary='" + getWorkSummary() + "'" +
            ", convertedPersonId=" + getConvertedPersonId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
