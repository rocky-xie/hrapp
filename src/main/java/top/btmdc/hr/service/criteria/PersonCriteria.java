package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.Gender;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.Person} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering EmploymentStatus
     */
    public static class EmploymentStatusFilter extends Filter<EmploymentStatus> {

        public EmploymentStatusFilter() {}

        public EmploymentStatusFilter(EmploymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public EmploymentStatusFilter copy() {
            return new EmploymentStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeCode;

    private StringFilter personName;

    private IntegerFilter age;

    private GenderFilter gender;

    private StringFilter department;

    private StringFilter currentRole;

    private EmploymentStatusFilter employmentStatus;

    private LocalDateFilter joinDate;

    private BooleanFilter mentorFlag;

    private BooleanFilter coreCandidateFlag;

    private Boolean distinct;

    public PersonCriteria() {}

    public PersonCriteria(PersonCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.employeeCode = other.optionalEmployeeCode().map(StringFilter::copy).orElse(null);
        this.personName = other.optionalPersonName().map(StringFilter::copy).orElse(null);
        this.age = other.optionalAge().map(IntegerFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.currentRole = other.optionalCurrentRole().map(StringFilter::copy).orElse(null);
        this.employmentStatus = other.optionalEmploymentStatus().map(EmploymentStatusFilter::copy).orElse(null);
        this.joinDate = other.optionalJoinDate().map(LocalDateFilter::copy).orElse(null);
        this.mentorFlag = other.optionalMentorFlag().map(BooleanFilter::copy).orElse(null);
        this.coreCandidateFlag = other.optionalCoreCandidateFlag().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
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

    public StringFilter getEmployeeCode() {
        return employeeCode;
    }

    public Optional<StringFilter> optionalEmployeeCode() {
        return Optional.ofNullable(employeeCode);
    }

    public StringFilter employeeCode() {
        if (employeeCode == null) {
            setEmployeeCode(new StringFilter());
        }
        return employeeCode;
    }

    public void setEmployeeCode(StringFilter employeeCode) {
        this.employeeCode = employeeCode;
    }

    public StringFilter getPersonName() {
        return personName;
    }

    public Optional<StringFilter> optionalPersonName() {
        return Optional.ofNullable(personName);
    }

    public StringFilter personName() {
        if (personName == null) {
            setPersonName(new StringFilter());
        }
        return personName;
    }

    public void setPersonName(StringFilter personName) {
        this.personName = personName;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public Optional<IntegerFilter> optionalAge() {
        return Optional.ofNullable(age);
    }

    public IntegerFilter age() {
        if (age == null) {
            setAge(new IntegerFilter());
        }
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getCurrentRole() {
        return currentRole;
    }

    public Optional<StringFilter> optionalCurrentRole() {
        return Optional.ofNullable(currentRole);
    }

    public StringFilter currentRole() {
        if (currentRole == null) {
            setCurrentRole(new StringFilter());
        }
        return currentRole;
    }

    public void setCurrentRole(StringFilter currentRole) {
        this.currentRole = currentRole;
    }

    public EmploymentStatusFilter getEmploymentStatus() {
        return employmentStatus;
    }

    public Optional<EmploymentStatusFilter> optionalEmploymentStatus() {
        return Optional.ofNullable(employmentStatus);
    }

    public EmploymentStatusFilter employmentStatus() {
        if (employmentStatus == null) {
            setEmploymentStatus(new EmploymentStatusFilter());
        }
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatusFilter employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDateFilter getJoinDate() {
        return joinDate;
    }

    public Optional<LocalDateFilter> optionalJoinDate() {
        return Optional.ofNullable(joinDate);
    }

    public LocalDateFilter joinDate() {
        if (joinDate == null) {
            setJoinDate(new LocalDateFilter());
        }
        return joinDate;
    }

    public void setJoinDate(LocalDateFilter joinDate) {
        this.joinDate = joinDate;
    }

    public BooleanFilter getMentorFlag() {
        return mentorFlag;
    }

    public Optional<BooleanFilter> optionalMentorFlag() {
        return Optional.ofNullable(mentorFlag);
    }

    public BooleanFilter mentorFlag() {
        if (mentorFlag == null) {
            setMentorFlag(new BooleanFilter());
        }
        return mentorFlag;
    }

    public void setMentorFlag(BooleanFilter mentorFlag) {
        this.mentorFlag = mentorFlag;
    }

    public BooleanFilter getCoreCandidateFlag() {
        return coreCandidateFlag;
    }

    public Optional<BooleanFilter> optionalCoreCandidateFlag() {
        return Optional.ofNullable(coreCandidateFlag);
    }

    public BooleanFilter coreCandidateFlag() {
        if (coreCandidateFlag == null) {
            setCoreCandidateFlag(new BooleanFilter());
        }
        return coreCandidateFlag;
    }

    public void setCoreCandidateFlag(BooleanFilter coreCandidateFlag) {
        this.coreCandidateFlag = coreCandidateFlag;
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
        final PersonCriteria that = (PersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeCode, that.employeeCode) &&
            Objects.equals(personName, that.personName) &&
            Objects.equals(age, that.age) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(department, that.department) &&
            Objects.equals(currentRole, that.currentRole) &&
            Objects.equals(employmentStatus, that.employmentStatus) &&
            Objects.equals(joinDate, that.joinDate) &&
            Objects.equals(mentorFlag, that.mentorFlag) &&
            Objects.equals(coreCandidateFlag, that.coreCandidateFlag) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeCode,
            personName,
            age,
            gender,
            department,
            currentRole,
            employmentStatus,
            joinDate,
            mentorFlag,
            coreCandidateFlag,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEmployeeCode().map(f -> "employeeCode=" + f + ", ").orElse("") +
            optionalPersonName().map(f -> "personName=" + f + ", ").orElse("") +
            optionalAge().map(f -> "age=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalCurrentRole().map(f -> "currentRole=" + f + ", ").orElse("") +
            optionalEmploymentStatus().map(f -> "employmentStatus=" + f + ", ").orElse("") +
            optionalJoinDate().map(f -> "joinDate=" + f + ", ").orElse("") +
            optionalMentorFlag().map(f -> "mentorFlag=" + f + ", ").orElse("") +
            optionalCoreCandidateFlag().map(f -> "coreCandidateFlag=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
