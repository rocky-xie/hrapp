package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.Gender;

/**
 * 职员。核心主体，关联技能、任职、替代等业务数据。
 */
@Entity
@Table(name = "person")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "employee_code", length = 50, unique = true)
    private String employeeCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "person_name", length = 100, nullable = false)
    private String personName;

    @Min(value = 0)
    @Max(value = 120)
    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;

    @Size(max = 100)
    @Column(name = "current_role", length = 100)
    private String currentRole;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @NotNull
    @Column(name = "mentor_flag", nullable = false)
    private Boolean mentorFlag;

    @NotNull
    @Column(name = "core_candidate_flag", nullable = false)
    private Boolean coreCandidateFlag;

    @Lob
    @Column(name = "note")
    private String note;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return this.employeeCode;
    }

    public Person employeeCode(String employeeCode) {
        this.setEmployeeCode(employeeCode);
        return this;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPersonName() {
        return this.personName;
    }

    public Person personName(String personName) {
        this.setPersonName(personName);
        return this;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getAge() {
        return this.age;
    }

    public Person age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Person gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return this.department;
    }

    public Person department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCurrentRole() {
        return this.currentRole;
    }

    public Person currentRole(String currentRole) {
        this.setCurrentRole(currentRole);
        return this;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public EmploymentStatus getEmploymentStatus() {
        return this.employmentStatus;
    }

    public Person employmentStatus(EmploymentStatus employmentStatus) {
        this.setEmploymentStatus(employmentStatus);
        return this;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDate getJoinDate() {
        return this.joinDate;
    }

    public Person joinDate(LocalDate joinDate) {
        this.setJoinDate(joinDate);
        return this;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getMentorFlag() {
        return this.mentorFlag;
    }

    public Person mentorFlag(Boolean mentorFlag) {
        this.setMentorFlag(mentorFlag);
        return this;
    }

    public void setMentorFlag(Boolean mentorFlag) {
        this.mentorFlag = mentorFlag;
    }

    public Boolean getCoreCandidateFlag() {
        return this.coreCandidateFlag;
    }

    public Person coreCandidateFlag(Boolean coreCandidateFlag) {
        this.setCoreCandidateFlag(coreCandidateFlag);
        return this;
    }

    public void setCoreCandidateFlag(Boolean coreCandidateFlag) {
        this.coreCandidateFlag = coreCandidateFlag;
    }

    public String getNote() {
        return this.note;
    }

    public Person note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return getId() != null && getId().equals(((Person) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
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
