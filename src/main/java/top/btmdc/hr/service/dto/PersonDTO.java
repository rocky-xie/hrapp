package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.Gender;

/**
 * A DTO for the {@link top.btmdc.hr.domain.Person} entity.
 */
@Schema(description = "职员。核心主体，关联技能、任职、替代等业务数据。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String employeeCode;

    @NotNull
    @Size(max = 100)
    private String personName;

    @Min(value = 0)
    @Max(value = 120)
    private Integer age;

    private Gender gender;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String currentRole;

    @NotNull
    private EmploymentStatus employmentStatus;

    private LocalDate joinDate;

    @NotNull
    private Boolean mentorFlag;

    @NotNull
    private Boolean coreCandidateFlag;

    @Lob
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getMentorFlag() {
        return mentorFlag;
    }

    public void setMentorFlag(Boolean mentorFlag) {
        this.mentorFlag = mentorFlag;
    }

    public Boolean getCoreCandidateFlag() {
        return coreCandidateFlag;
    }

    public void setCoreCandidateFlag(Boolean coreCandidateFlag) {
        this.coreCandidateFlag = coreCandidateFlag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", personName='" + getPersonName() + "'" +
            ", age=" + getAge() +
            ", gender='" + getGender() + "'" +
            ", department='" + getDepartment() + "'" +
            ", currentRole='" + getCurrentRole() + "'" +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", joinDate='" + getJoinDate() + "'" +
            ", mentorFlag='" + getMentorFlag() + "'" +
            ", coreCandidateFlag='" + getCoreCandidateFlag() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
