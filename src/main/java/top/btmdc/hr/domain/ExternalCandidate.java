package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A ExternalCandidate.
 */
@Entity
@Table(name = "external_candidate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalCandidate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Size(max = 150)
    @Column(name = "name_reading", length = 150)
    private String nameReading;

    @Size(max = 150)
    @Column(name = "english_name", length = 150)
    private String englishName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 30)
    @Column(name = "gender", length = 30)
    private String gender;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 254)
    @Column(name = "email", length = 254)
    private String email;

    @Lob
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "education_summary")
    private String educationSummary;

    @Lob
    @Column(name = "work_summary")
    private String workSummary;

    @Column(name = "converted_person_id")
    private Long convertedPersonId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidate")
    @JsonIgnoreProperties(value = { "candidate" }, allowSetters = true)
    private Set<CandidateResume> resumeses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidate")
    @JsonIgnoreProperties(
        value = {
            "stageses",
            "eventses",
            "activitieses",
            "evaluationses",
            "decisionses",
            "messageses",
            "followUpses",
            "dispatchAssignmentses",
            "candidate",
            "requisition",
            "requisitionVersion",
        },
        allowSetters = true
    )
    private Set<RecruitmentApplication> applicationses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalCandidate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public ExternalCandidate fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNameReading() {
        return this.nameReading;
    }

    public ExternalCandidate nameReading(String nameReading) {
        this.setNameReading(nameReading);
        return this;
    }

    public void setNameReading(String nameReading) {
        this.nameReading = nameReading;
    }

    public String getEnglishName() {
        return this.englishName;
    }

    public ExternalCandidate englishName(String englishName) {
        this.setEnglishName(englishName);
        return this;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public ExternalCandidate birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return this.gender;
    }

    public ExternalCandidate gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public ExternalCandidate phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public ExternalCandidate email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public ExternalCandidate address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducationSummary() {
        return this.educationSummary;
    }

    public ExternalCandidate educationSummary(String educationSummary) {
        this.setEducationSummary(educationSummary);
        return this;
    }

    public void setEducationSummary(String educationSummary) {
        this.educationSummary = educationSummary;
    }

    public String getWorkSummary() {
        return this.workSummary;
    }

    public ExternalCandidate workSummary(String workSummary) {
        this.setWorkSummary(workSummary);
        return this;
    }

    public void setWorkSummary(String workSummary) {
        this.workSummary = workSummary;
    }

    public Long getConvertedPersonId() {
        return this.convertedPersonId;
    }

    public ExternalCandidate convertedPersonId(Long convertedPersonId) {
        this.setConvertedPersonId(convertedPersonId);
        return this;
    }

    public void setConvertedPersonId(Long convertedPersonId) {
        this.convertedPersonId = convertedPersonId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ExternalCandidate createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ExternalCandidate updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CandidateResume> getResumeses() {
        return this.resumeses;
    }

    public void setResumeses(Set<CandidateResume> candidateResumes) {
        if (this.resumeses != null) {
            this.resumeses.forEach(i -> i.setCandidate(null));
        }
        if (candidateResumes != null) {
            candidateResumes.forEach(i -> i.setCandidate(this));
        }
        this.resumeses = candidateResumes;
    }

    public ExternalCandidate resumeses(Set<CandidateResume> candidateResumes) {
        this.setResumeses(candidateResumes);
        return this;
    }

    public ExternalCandidate addResumes(CandidateResume candidateResume) {
        this.resumeses.add(candidateResume);
        candidateResume.setCandidate(this);
        return this;
    }

    public ExternalCandidate removeResumes(CandidateResume candidateResume) {
        this.resumeses.remove(candidateResume);
        candidateResume.setCandidate(null);
        return this;
    }

    public Set<RecruitmentApplication> getApplicationses() {
        return this.applicationses;
    }

    public void setApplicationses(Set<RecruitmentApplication> recruitmentApplications) {
        if (this.applicationses != null) {
            this.applicationses.forEach(i -> i.setCandidate(null));
        }
        if (recruitmentApplications != null) {
            recruitmentApplications.forEach(i -> i.setCandidate(this));
        }
        this.applicationses = recruitmentApplications;
    }

    public ExternalCandidate applicationses(Set<RecruitmentApplication> recruitmentApplications) {
        this.setApplicationses(recruitmentApplications);
        return this;
    }

    public ExternalCandidate addApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.add(recruitmentApplication);
        recruitmentApplication.setCandidate(this);
        return this;
    }

    public ExternalCandidate removeApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.remove(recruitmentApplication);
        recruitmentApplication.setCandidate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalCandidate)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalCandidate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalCandidate{" +
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
