package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.ExternalCandidateAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.ExternalCandidateRepository;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;
import top.btmdc.hr.service.mapper.ExternalCandidateMapper;

/**
 * Integration tests for the {@link ExternalCandidateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class ExternalCandidateResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_READING = "AAAAAAAAAA";
    private static final String UPDATED_NAME_READING = "BBBBBBBBBB";

    private static final String DEFAULT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENGLISH_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATION_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_WORK_SUMMARY = "BBBBBBBBBB";

    private static final Long DEFAULT_CONVERTED_PERSON_ID = 1L;
    private static final Long UPDATED_CONVERTED_PERSON_ID = 2L;
    private static final Long SMALLER_CONVERTED_PERSON_ID = 1L - 1L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/external-candidates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExternalCandidateRepository externalCandidateRepository;

    @Autowired
    private ExternalCandidateMapper externalCandidateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalCandidateMockMvc;

    private ExternalCandidate externalCandidate;

    private ExternalCandidate insertedExternalCandidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalCandidate createEntity() {
        return new ExternalCandidate()
            .fullName(DEFAULT_FULL_NAME)
            .nameReading(DEFAULT_NAME_READING)
            .englishName(DEFAULT_ENGLISH_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .educationSummary(DEFAULT_EDUCATION_SUMMARY)
            .workSummary(DEFAULT_WORK_SUMMARY)
            .convertedPersonId(DEFAULT_CONVERTED_PERSON_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalCandidate createUpdatedEntity() {
        return new ExternalCandidate()
            .fullName(UPDATED_FULL_NAME)
            .nameReading(UPDATED_NAME_READING)
            .englishName(UPDATED_ENGLISH_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .educationSummary(UPDATED_EDUCATION_SUMMARY)
            .workSummary(UPDATED_WORK_SUMMARY)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        externalCandidate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExternalCandidate != null) {
            externalCandidateRepository.delete(insertedExternalCandidate);
            insertedExternalCandidate = null;
        }
    }

    @Test
    @Transactional
    void createExternalCandidate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);
        var returnedExternalCandidateDTO = om.readValue(
            restExternalCandidateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalCandidateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExternalCandidateDTO.class
        );

        // Validate the ExternalCandidate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExternalCandidate = externalCandidateMapper.toEntity(returnedExternalCandidateDTO);
        assertExternalCandidateUpdatableFieldsEquals(returnedExternalCandidate, getPersistedExternalCandidate(returnedExternalCandidate));

        insertedExternalCandidate = returnedExternalCandidate;
    }

    @Test
    @Transactional
    void createExternalCandidateWithExistingId() throws Exception {
        // Create the ExternalCandidate with an existing ID
        externalCandidate.setId(1L);
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalCandidateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalCandidate.setFullName(null);

        // Create the ExternalCandidate, which fails.
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        restExternalCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalCandidateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalCandidate.setCreatedAt(null);

        // Create the ExternalCandidate, which fails.
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        restExternalCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalCandidateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExternalCandidates() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].nameReading").value(hasItem(DEFAULT_NAME_READING)))
            .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].educationSummary").value(hasItem(DEFAULT_EDUCATION_SUMMARY)))
            .andExpect(jsonPath("$.[*].workSummary").value(hasItem(DEFAULT_WORK_SUMMARY)))
            .andExpect(jsonPath("$.[*].convertedPersonId").value(hasItem(DEFAULT_CONVERTED_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getExternalCandidate() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get the externalCandidate
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL_ID, externalCandidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalCandidate.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.nameReading").value(DEFAULT_NAME_READING))
            .andExpect(jsonPath("$.englishName").value(DEFAULT_ENGLISH_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.educationSummary").value(DEFAULT_EDUCATION_SUMMARY))
            .andExpect(jsonPath("$.workSummary").value(DEFAULT_WORK_SUMMARY))
            .andExpect(jsonPath("$.convertedPersonId").value(DEFAULT_CONVERTED_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getExternalCandidatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        Long id = externalCandidate.getId();

        defaultExternalCandidateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExternalCandidateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExternalCandidateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where fullName equals to
        defaultExternalCandidateFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where fullName in
        defaultExternalCandidateFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where fullName is not null
        defaultExternalCandidateFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where fullName contains
        defaultExternalCandidateFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where fullName does not contain
        defaultExternalCandidateFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByNameReadingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where nameReading equals to
        defaultExternalCandidateFiltering("nameReading.equals=" + DEFAULT_NAME_READING, "nameReading.equals=" + UPDATED_NAME_READING);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByNameReadingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where nameReading in
        defaultExternalCandidateFiltering(
            "nameReading.in=" + DEFAULT_NAME_READING + "," + UPDATED_NAME_READING,
            "nameReading.in=" + UPDATED_NAME_READING
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByNameReadingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where nameReading is not null
        defaultExternalCandidateFiltering("nameReading.specified=true", "nameReading.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByNameReadingContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where nameReading contains
        defaultExternalCandidateFiltering("nameReading.contains=" + DEFAULT_NAME_READING, "nameReading.contains=" + UPDATED_NAME_READING);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByNameReadingNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where nameReading does not contain
        defaultExternalCandidateFiltering(
            "nameReading.doesNotContain=" + UPDATED_NAME_READING,
            "nameReading.doesNotContain=" + DEFAULT_NAME_READING
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where englishName equals to
        defaultExternalCandidateFiltering("englishName.equals=" + DEFAULT_ENGLISH_NAME, "englishName.equals=" + UPDATED_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where englishName in
        defaultExternalCandidateFiltering(
            "englishName.in=" + DEFAULT_ENGLISH_NAME + "," + UPDATED_ENGLISH_NAME,
            "englishName.in=" + UPDATED_ENGLISH_NAME
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where englishName is not null
        defaultExternalCandidateFiltering("englishName.specified=true", "englishName.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where englishName contains
        defaultExternalCandidateFiltering("englishName.contains=" + DEFAULT_ENGLISH_NAME, "englishName.contains=" + UPDATED_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where englishName does not contain
        defaultExternalCandidateFiltering(
            "englishName.doesNotContain=" + UPDATED_ENGLISH_NAME,
            "englishName.doesNotContain=" + DEFAULT_ENGLISH_NAME
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate equals to
        defaultExternalCandidateFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate in
        defaultExternalCandidateFiltering(
            "birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE,
            "birthDate.in=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate is not null
        defaultExternalCandidateFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate is greater than or equal to
        defaultExternalCandidateFiltering(
            "birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate is less than or equal to
        defaultExternalCandidateFiltering(
            "birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate is less than
        defaultExternalCandidateFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where birthDate is greater than
        defaultExternalCandidateFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where gender equals to
        defaultExternalCandidateFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where gender in
        defaultExternalCandidateFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where gender is not null
        defaultExternalCandidateFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByGenderContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where gender contains
        defaultExternalCandidateFiltering("gender.contains=" + DEFAULT_GENDER, "gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where gender does not contain
        defaultExternalCandidateFiltering("gender.doesNotContain=" + UPDATED_GENDER, "gender.doesNotContain=" + DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where phone equals to
        defaultExternalCandidateFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where phone in
        defaultExternalCandidateFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where phone is not null
        defaultExternalCandidateFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where phone contains
        defaultExternalCandidateFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where phone does not contain
        defaultExternalCandidateFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where email equals to
        defaultExternalCandidateFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where email in
        defaultExternalCandidateFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where email is not null
        defaultExternalCandidateFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where email contains
        defaultExternalCandidateFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where email does not contain
        defaultExternalCandidateFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId equals to
        defaultExternalCandidateFiltering(
            "convertedPersonId.equals=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.equals=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId in
        defaultExternalCandidateFiltering(
            "convertedPersonId.in=" + DEFAULT_CONVERTED_PERSON_ID + "," + UPDATED_CONVERTED_PERSON_ID,
            "convertedPersonId.in=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId is not null
        defaultExternalCandidateFiltering("convertedPersonId.specified=true", "convertedPersonId.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId is greater than or equal to
        defaultExternalCandidateFiltering(
            "convertedPersonId.greaterThanOrEqual=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.greaterThanOrEqual=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId is less than or equal to
        defaultExternalCandidateFiltering(
            "convertedPersonId.lessThanOrEqual=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.lessThanOrEqual=" + SMALLER_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId is less than
        defaultExternalCandidateFiltering(
            "convertedPersonId.lessThan=" + UPDATED_CONVERTED_PERSON_ID,
            "convertedPersonId.lessThan=" + DEFAULT_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByConvertedPersonIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where convertedPersonId is greater than
        defaultExternalCandidateFiltering(
            "convertedPersonId.greaterThan=" + SMALLER_CONVERTED_PERSON_ID,
            "convertedPersonId.greaterThan=" + DEFAULT_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where createdAt equals to
        defaultExternalCandidateFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where createdAt in
        defaultExternalCandidateFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where createdAt is not null
        defaultExternalCandidateFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where updatedAt equals to
        defaultExternalCandidateFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where updatedAt in
        defaultExternalCandidateFiltering(
            "updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT,
            "updatedAt.in=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllExternalCandidatesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        // Get all the externalCandidateList where updatedAt is not null
        defaultExternalCandidateFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    private void defaultExternalCandidateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExternalCandidateShouldBeFound(shouldBeFound);
        defaultExternalCandidateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExternalCandidateShouldBeFound(String filter) throws Exception {
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].nameReading").value(hasItem(DEFAULT_NAME_READING)))
            .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].educationSummary").value(hasItem(DEFAULT_EDUCATION_SUMMARY)))
            .andExpect(jsonPath("$.[*].workSummary").value(hasItem(DEFAULT_WORK_SUMMARY)))
            .andExpect(jsonPath("$.[*].convertedPersonId").value(hasItem(DEFAULT_CONVERTED_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExternalCandidateShouldNotBeFound(String filter) throws Exception {
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExternalCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExternalCandidate() throws Exception {
        // Get the externalCandidate
        restExternalCandidateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExternalCandidate() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalCandidate
        ExternalCandidate updatedExternalCandidate = externalCandidateRepository.findById(externalCandidate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExternalCandidate are not directly saved in db
        em.detach(updatedExternalCandidate);
        updatedExternalCandidate
            .fullName(UPDATED_FULL_NAME)
            .nameReading(UPDATED_NAME_READING)
            .englishName(UPDATED_ENGLISH_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .educationSummary(UPDATED_EDUCATION_SUMMARY)
            .workSummary(UPDATED_WORK_SUMMARY)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(updatedExternalCandidate);

        restExternalCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalCandidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalCandidateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExternalCandidateToMatchAllProperties(updatedExternalCandidate);
    }

    @Test
    @Transactional
    void putNonExistingExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalCandidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalCandidateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalCandidateWithPatch() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalCandidate using partial update
        ExternalCandidate partialUpdatedExternalCandidate = new ExternalCandidate();
        partialUpdatedExternalCandidate.setId(externalCandidate.getId());

        partialUpdatedExternalCandidate
            .birthDate(UPDATED_BIRTH_DATE)
            .phone(UPDATED_PHONE)
            .educationSummary(UPDATED_EDUCATION_SUMMARY)
            .workSummary(UPDATED_WORK_SUMMARY);

        restExternalCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalCandidate))
            )
            .andExpect(status().isOk());

        // Validate the ExternalCandidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalCandidateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExternalCandidate, externalCandidate),
            getPersistedExternalCandidate(externalCandidate)
        );
    }

    @Test
    @Transactional
    void fullUpdateExternalCandidateWithPatch() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalCandidate using partial update
        ExternalCandidate partialUpdatedExternalCandidate = new ExternalCandidate();
        partialUpdatedExternalCandidate.setId(externalCandidate.getId());

        partialUpdatedExternalCandidate
            .fullName(UPDATED_FULL_NAME)
            .nameReading(UPDATED_NAME_READING)
            .englishName(UPDATED_ENGLISH_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .educationSummary(UPDATED_EDUCATION_SUMMARY)
            .workSummary(UPDATED_WORK_SUMMARY)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restExternalCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalCandidate))
            )
            .andExpect(status().isOk());

        // Validate the ExternalCandidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalCandidateUpdatableFieldsEquals(
            partialUpdatedExternalCandidate,
            getPersistedExternalCandidate(partialUpdatedExternalCandidate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalCandidateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalCandidate.setId(longCount.incrementAndGet());

        // Create the ExternalCandidate
        ExternalCandidateDTO externalCandidateDTO = externalCandidateMapper.toDto(externalCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalCandidateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(externalCandidateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExternalCandidate() throws Exception {
        // Initialize the database
        insertedExternalCandidate = externalCandidateRepository.saveAndFlush(externalCandidate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the externalCandidate
        restExternalCandidateMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalCandidate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return externalCandidateRepository.count();
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

    protected ExternalCandidate getPersistedExternalCandidate(ExternalCandidate externalCandidate) {
        return externalCandidateRepository.findById(externalCandidate.getId()).orElseThrow();
    }

    protected void assertPersistedExternalCandidateToMatchAllProperties(ExternalCandidate expectedExternalCandidate) {
        assertExternalCandidateAllPropertiesEquals(expectedExternalCandidate, getPersistedExternalCandidate(expectedExternalCandidate));
    }

    protected void assertPersistedExternalCandidateToMatchUpdatableProperties(ExternalCandidate expectedExternalCandidate) {
        assertExternalCandidateAllUpdatablePropertiesEquals(
            expectedExternalCandidate,
            getPersistedExternalCandidate(expectedExternalCandidate)
        );
    }
}
