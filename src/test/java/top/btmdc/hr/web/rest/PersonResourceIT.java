package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PersonAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.IntegrationTest;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.Gender;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.mapper.PersonMapper;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonResourceIT {

    private static final String DEFAULT_EMPLOYEE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;
    private static final Integer SMALLER_AGE = 0 - 1;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_ROLE = "BBBBBBBBBB";

    private static final EmploymentStatus DEFAULT_EMPLOYMENT_STATUS = EmploymentStatus.NEWCOMER;
    private static final EmploymentStatus UPDATED_EMPLOYMENT_STATUS = EmploymentStatus.INDEPENDENT_STAFF;

    private static final LocalDate DEFAULT_JOIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOIN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_JOIN_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_MENTOR_FLAG = false;
    private static final Boolean UPDATED_MENTOR_FLAG = true;

    private static final Boolean DEFAULT_CORE_CANDIDATE_FLAG = false;
    private static final Boolean UPDATED_CORE_CANDIDATE_FLAG = true;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    private Person insertedPerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity() {
        return new Person()
            .employeeCode(DEFAULT_EMPLOYEE_CODE)
            .personName(DEFAULT_PERSON_NAME)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .department(DEFAULT_DEPARTMENT)
            .currentRole(DEFAULT_CURRENT_ROLE)
            .employmentStatus(DEFAULT_EMPLOYMENT_STATUS)
            .joinDate(DEFAULT_JOIN_DATE)
            .mentorFlag(DEFAULT_MENTOR_FLAG)
            .coreCandidateFlag(DEFAULT_CORE_CANDIDATE_FLAG)
            .note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity() {
        return new Person()
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .personName(UPDATED_PERSON_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .department(UPDATED_DEPARTMENT)
            .currentRole(UPDATED_CURRENT_ROLE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .joinDate(UPDATED_JOIN_DATE)
            .mentorFlag(UPDATED_MENTOR_FLAG)
            .coreCandidateFlag(UPDATED_CORE_CANDIDATE_FLAG)
            .note(UPDATED_NOTE);
    }

    @BeforeEach
    void initTest() {
        person = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPerson != null) {
            personRepository.delete(insertedPerson);
            insertedPerson = null;
        }
    }

    @Test
    @Transactional
    void createPerson() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);
        var returnedPersonDTO = om.readValue(
            restPersonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonDTO.class
        );

        // Validate the Person in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPerson = personMapper.toEntity(returnedPersonDTO);
        assertPersonUpdatableFieldsEquals(returnedPerson, getPersistedPerson(returnedPerson));

        insertedPerson = returnedPerson;
    }

    @Test
    @Transactional
    void createPersonWithExistingId() throws Exception {
        // Create the Person with an existing ID
        person.setId(1L);
        PersonDTO personDTO = personMapper.toDto(person);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPersonNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        person.setPersonName(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmploymentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        person.setEmploymentStatus(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMentorFlagIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        person.setMentorFlag(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoreCandidateFlagIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        person.setCoreCandidateFlag(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCode").value(hasItem(DEFAULT_EMPLOYEE_CODE)))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].currentRole").value(hasItem(DEFAULT_CURRENT_ROLE)))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].joinDate").value(hasItem(DEFAULT_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].mentorFlag").value(hasItem(DEFAULT_MENTOR_FLAG)))
            .andExpect(jsonPath("$.[*].coreCandidateFlag").value(hasItem(DEFAULT_CORE_CANDIDATE_FLAG)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getPerson() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.employeeCode").value(DEFAULT_EMPLOYEE_CODE))
            .andExpect(jsonPath("$.personName").value(DEFAULT_PERSON_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.currentRole").value(DEFAULT_CURRENT_ROLE))
            .andExpect(jsonPath("$.employmentStatus").value(DEFAULT_EMPLOYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.joinDate").value(DEFAULT_JOIN_DATE.toString()))
            .andExpect(jsonPath("$.mentorFlag").value(DEFAULT_MENTOR_FLAG))
            .andExpect(jsonPath("$.coreCandidateFlag").value(DEFAULT_CORE_CANDIDATE_FLAG))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPersonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPersonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeopleByEmployeeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employeeCode equals to
        defaultPersonFiltering("employeeCode.equals=" + DEFAULT_EMPLOYEE_CODE, "employeeCode.equals=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByEmployeeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employeeCode in
        defaultPersonFiltering(
            "employeeCode.in=" + DEFAULT_EMPLOYEE_CODE + "," + UPDATED_EMPLOYEE_CODE,
            "employeeCode.in=" + UPDATED_EMPLOYEE_CODE
        );
    }

    @Test
    @Transactional
    void getAllPeopleByEmployeeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employeeCode is not null
        defaultPersonFiltering("employeeCode.specified=true", "employeeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByEmployeeCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employeeCode contains
        defaultPersonFiltering("employeeCode.contains=" + DEFAULT_EMPLOYEE_CODE, "employeeCode.contains=" + UPDATED_EMPLOYEE_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByEmployeeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employeeCode does not contain
        defaultPersonFiltering(
            "employeeCode.doesNotContain=" + UPDATED_EMPLOYEE_CODE,
            "employeeCode.doesNotContain=" + DEFAULT_EMPLOYEE_CODE
        );
    }

    @Test
    @Transactional
    void getAllPeopleByPersonNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where personName equals to
        defaultPersonFiltering("personName.equals=" + DEFAULT_PERSON_NAME, "personName.equals=" + UPDATED_PERSON_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPersonNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where personName in
        defaultPersonFiltering("personName.in=" + DEFAULT_PERSON_NAME + "," + UPDATED_PERSON_NAME, "personName.in=" + UPDATED_PERSON_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPersonNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where personName is not null
        defaultPersonFiltering("personName.specified=true", "personName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByPersonNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where personName contains
        defaultPersonFiltering("personName.contains=" + DEFAULT_PERSON_NAME, "personName.contains=" + UPDATED_PERSON_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPersonNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where personName does not contain
        defaultPersonFiltering("personName.doesNotContain=" + UPDATED_PERSON_NAME, "personName.doesNotContain=" + DEFAULT_PERSON_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age equals to
        defaultPersonFiltering("age.equals=" + DEFAULT_AGE, "age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age in
        defaultPersonFiltering("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE, "age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age is not null
        defaultPersonFiltering("age.specified=true", "age.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age is greater than or equal to
        defaultPersonFiltering("age.greaterThanOrEqual=" + DEFAULT_AGE, "age.greaterThanOrEqual=" + (DEFAULT_AGE + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age is less than or equal to
        defaultPersonFiltering("age.lessThanOrEqual=" + DEFAULT_AGE, "age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age is less than
        defaultPersonFiltering("age.lessThan=" + (DEFAULT_AGE + 1), "age.lessThan=" + DEFAULT_AGE);
    }

    @Test
    @Transactional
    void getAllPeopleByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where age is greater than
        defaultPersonFiltering("age.greaterThan=" + SMALLER_AGE, "age.greaterThan=" + DEFAULT_AGE);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where gender equals to
        defaultPersonFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where gender in
        defaultPersonFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where gender is not null
        defaultPersonFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where department equals to
        defaultPersonFiltering("department.equals=" + DEFAULT_DEPARTMENT, "department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllPeopleByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where department in
        defaultPersonFiltering("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT, "department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllPeopleByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where department is not null
        defaultPersonFiltering("department.specified=true", "department.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where department contains
        defaultPersonFiltering("department.contains=" + DEFAULT_DEPARTMENT, "department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllPeopleByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where department does not contain
        defaultPersonFiltering("department.doesNotContain=" + UPDATED_DEPARTMENT, "department.doesNotContain=" + DEFAULT_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where currentRole equals to
        defaultPersonFiltering("currentRole.equals=" + DEFAULT_CURRENT_ROLE, "currentRole.equals=" + UPDATED_CURRENT_ROLE);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentRoleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where currentRole in
        defaultPersonFiltering(
            "currentRole.in=" + DEFAULT_CURRENT_ROLE + "," + UPDATED_CURRENT_ROLE,
            "currentRole.in=" + UPDATED_CURRENT_ROLE
        );
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where currentRole is not null
        defaultPersonFiltering("currentRole.specified=true", "currentRole.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentRoleContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where currentRole contains
        defaultPersonFiltering("currentRole.contains=" + DEFAULT_CURRENT_ROLE, "currentRole.contains=" + UPDATED_CURRENT_ROLE);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentRoleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where currentRole does not contain
        defaultPersonFiltering("currentRole.doesNotContain=" + UPDATED_CURRENT_ROLE, "currentRole.doesNotContain=" + DEFAULT_CURRENT_ROLE);
    }

    @Test
    @Transactional
    void getAllPeopleByEmploymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employmentStatus equals to
        defaultPersonFiltering(
            "employmentStatus.equals=" + DEFAULT_EMPLOYMENT_STATUS,
            "employmentStatus.equals=" + UPDATED_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPeopleByEmploymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employmentStatus in
        defaultPersonFiltering(
            "employmentStatus.in=" + DEFAULT_EMPLOYMENT_STATUS + "," + UPDATED_EMPLOYMENT_STATUS,
            "employmentStatus.in=" + UPDATED_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPeopleByEmploymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where employmentStatus is not null
        defaultPersonFiltering("employmentStatus.specified=true", "employmentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate equals to
        defaultPersonFiltering("joinDate.equals=" + DEFAULT_JOIN_DATE, "joinDate.equals=" + UPDATED_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate in
        defaultPersonFiltering("joinDate.in=" + DEFAULT_JOIN_DATE + "," + UPDATED_JOIN_DATE, "joinDate.in=" + UPDATED_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate is not null
        defaultPersonFiltering("joinDate.specified=true", "joinDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate is greater than or equal to
        defaultPersonFiltering("joinDate.greaterThanOrEqual=" + DEFAULT_JOIN_DATE, "joinDate.greaterThanOrEqual=" + UPDATED_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate is less than or equal to
        defaultPersonFiltering("joinDate.lessThanOrEqual=" + DEFAULT_JOIN_DATE, "joinDate.lessThanOrEqual=" + SMALLER_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate is less than
        defaultPersonFiltering("joinDate.lessThan=" + UPDATED_JOIN_DATE, "joinDate.lessThan=" + DEFAULT_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByJoinDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where joinDate is greater than
        defaultPersonFiltering("joinDate.greaterThan=" + SMALLER_JOIN_DATE, "joinDate.greaterThan=" + DEFAULT_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMentorFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where mentorFlag equals to
        defaultPersonFiltering("mentorFlag.equals=" + DEFAULT_MENTOR_FLAG, "mentorFlag.equals=" + UPDATED_MENTOR_FLAG);
    }

    @Test
    @Transactional
    void getAllPeopleByMentorFlagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where mentorFlag in
        defaultPersonFiltering("mentorFlag.in=" + DEFAULT_MENTOR_FLAG + "," + UPDATED_MENTOR_FLAG, "mentorFlag.in=" + UPDATED_MENTOR_FLAG);
    }

    @Test
    @Transactional
    void getAllPeopleByMentorFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where mentorFlag is not null
        defaultPersonFiltering("mentorFlag.specified=true", "mentorFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByCoreCandidateFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where coreCandidateFlag equals to
        defaultPersonFiltering(
            "coreCandidateFlag.equals=" + DEFAULT_CORE_CANDIDATE_FLAG,
            "coreCandidateFlag.equals=" + UPDATED_CORE_CANDIDATE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllPeopleByCoreCandidateFlagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where coreCandidateFlag in
        defaultPersonFiltering(
            "coreCandidateFlag.in=" + DEFAULT_CORE_CANDIDATE_FLAG + "," + UPDATED_CORE_CANDIDATE_FLAG,
            "coreCandidateFlag.in=" + UPDATED_CORE_CANDIDATE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllPeopleByCoreCandidateFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        // Get all the personList where coreCandidateFlag is not null
        defaultPersonFiltering("coreCandidateFlag.specified=true", "coreCandidateFlag.specified=false");
    }

    private void defaultPersonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPersonShouldBeFound(shouldBeFound);
        defaultPersonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCode").value(hasItem(DEFAULT_EMPLOYEE_CODE)))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].currentRole").value(hasItem(DEFAULT_CURRENT_ROLE)))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].joinDate").value(hasItem(DEFAULT_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].mentorFlag").value(hasItem(DEFAULT_MENTOR_FLAG)))
            .andExpect(jsonPath("$.[*].coreCandidateFlag").value(hasItem(DEFAULT_CORE_CANDIDATE_FLAG)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerson() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .personName(UPDATED_PERSON_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .department(UPDATED_DEPARTMENT)
            .currentRole(UPDATED_CURRENT_ROLE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .joinDate(UPDATED_JOIN_DATE)
            .mentorFlag(UPDATED_MENTOR_FLAG)
            .coreCandidateFlag(UPDATED_CORE_CANDIDATE_FLAG)
            .note(UPDATED_NOTE);
        PersonDTO personDTO = personMapper.toDto(updatedPerson);

        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonToMatchAllProperties(updatedPerson);
    }

    @Test
    @Transactional
    void putNonExistingPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .personName(UPDATED_PERSON_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .mentorFlag(UPDATED_MENTOR_FLAG);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPerson, person), getPersistedPerson(person));
    }

    @Test
    @Transactional
    void fullUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .employeeCode(UPDATED_EMPLOYEE_CODE)
            .personName(UPDATED_PERSON_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .department(UPDATED_DEPARTMENT)
            .currentRole(UPDATED_CURRENT_ROLE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .joinDate(UPDATED_JOIN_DATE)
            .mentorFlag(UPDATED_MENTOR_FLAG)
            .coreCandidateFlag(UPDATED_CORE_CANDIDATE_FLAG)
            .note(UPDATED_NOTE);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonUpdatableFieldsEquals(partialUpdatedPerson, getPersistedPerson(partialUpdatedPerson));
    }

    @Test
    @Transactional
    void patchNonExistingPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        person.setId(longCount.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerson() throws Exception {
        // Initialize the database
        insertedPerson = personRepository.saveAndFlush(person);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the person
        restPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, person.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Person getPersistedPerson(Person person) {
        return personRepository.findById(person.getId()).orElseThrow();
    }

    protected void assertPersistedPersonToMatchAllProperties(Person expectedPerson) {
        assertPersonAllPropertiesEquals(expectedPerson, getPersistedPerson(expectedPerson));
    }

    protected void assertPersistedPersonToMatchUpdatableProperties(Person expectedPerson) {
        assertPersonAllUpdatablePropertiesEquals(expectedPerson, getPersistedPerson(expectedPerson));
    }
}
