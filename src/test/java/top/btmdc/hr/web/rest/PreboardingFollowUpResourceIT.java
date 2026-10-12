package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PreboardingFollowUpAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
import top.btmdc.hr.domain.PreboardingFollowUp;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.repository.PreboardingFollowUpRepository;
import top.btmdc.hr.service.dto.PreboardingFollowUpDTO;
import top.btmdc.hr.service.mapper.PreboardingFollowUpMapper;

/**
 * Integration tests for the {@link PreboardingFollowUpResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class PreboardingFollowUpResourceIT {

    private static final Long DEFAULT_CANDIDATE_ID = 1L;
    private static final Long UPDATED_CANDIDATE_ID = 2L;
    private static final Long SMALLER_CANDIDATE_ID = 1L - 1L;

    private static final Long DEFAULT_PERSON_ID = 1L;
    private static final Long UPDATED_PERSON_ID = 2L;
    private static final Long SMALLER_PERSON_ID = 1L - 1L;

    private static final Instant DEFAULT_PLANNED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLANNED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CONTACTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONTACTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_CANDIDATE_INTENT = "AAAAAAAAAA";
    private static final String UPDATED_CANDIDATE_INTENT = "BBBBBBBBBB";

    private static final String DEFAULT_RISK_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_RISK_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_NEXT_CONTACT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_CONTACT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/preboarding-follow-ups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PreboardingFollowUpRepository preboardingFollowUpRepository;

    @Autowired
    private PreboardingFollowUpMapper preboardingFollowUpMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreboardingFollowUpMockMvc;

    private PreboardingFollowUp preboardingFollowUp;

    private PreboardingFollowUp insertedPreboardingFollowUp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PreboardingFollowUp createEntity() {
        return new PreboardingFollowUp()
            .candidateId(DEFAULT_CANDIDATE_ID)
            .personId(DEFAULT_PERSON_ID)
            .plannedAt(DEFAULT_PLANNED_AT)
            .contactedAt(DEFAULT_CONTACTED_AT)
            .ownerUsername(DEFAULT_OWNER_USERNAME)
            .contactMethod(DEFAULT_CONTACT_METHOD)
            .result(DEFAULT_RESULT)
            .candidateIntent(DEFAULT_CANDIDATE_INTENT)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .nextAction(DEFAULT_NEXT_ACTION)
            .nextContactAt(DEFAULT_NEXT_CONTACT_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PreboardingFollowUp createUpdatedEntity() {
        return new PreboardingFollowUp()
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .plannedAt(UPDATED_PLANNED_AT)
            .contactedAt(UPDATED_CONTACTED_AT)
            .ownerUsername(UPDATED_OWNER_USERNAME)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .result(UPDATED_RESULT)
            .candidateIntent(UPDATED_CANDIDATE_INTENT)
            .riskLevel(UPDATED_RISK_LEVEL)
            .nextAction(UPDATED_NEXT_ACTION)
            .nextContactAt(UPDATED_NEXT_CONTACT_AT);
    }

    @BeforeEach
    void initTest() {
        preboardingFollowUp = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPreboardingFollowUp != null) {
            preboardingFollowUpRepository.delete(insertedPreboardingFollowUp);
            insertedPreboardingFollowUp = null;
        }
    }

    @Test
    @Transactional
    void createPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);
        var returnedPreboardingFollowUpDTO = om.readValue(
            restPreboardingFollowUpMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preboardingFollowUpDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PreboardingFollowUpDTO.class
        );

        // Validate the PreboardingFollowUp in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPreboardingFollowUp = preboardingFollowUpMapper.toEntity(returnedPreboardingFollowUpDTO);
        assertPreboardingFollowUpUpdatableFieldsEquals(
            returnedPreboardingFollowUp,
            getPersistedPreboardingFollowUp(returnedPreboardingFollowUp)
        );

        insertedPreboardingFollowUp = returnedPreboardingFollowUp;
    }

    @Test
    @Transactional
    void createPreboardingFollowUpWithExistingId() throws Exception {
        // Create the PreboardingFollowUp with an existing ID
        preboardingFollowUp.setId(1L);
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreboardingFollowUpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preboardingFollowUpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCandidateIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        preboardingFollowUp.setCandidateId(null);

        // Create the PreboardingFollowUp, which fails.
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        restPreboardingFollowUpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preboardingFollowUpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        preboardingFollowUp.setOwnerUsername(null);

        // Create the PreboardingFollowUp, which fails.
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        restPreboardingFollowUpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preboardingFollowUpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUps() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preboardingFollowUp.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateId").value(hasItem(DEFAULT_CANDIDATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].plannedAt").value(hasItem(DEFAULT_PLANNED_AT.toString())))
            .andExpect(jsonPath("$.[*].contactedAt").value(hasItem(DEFAULT_CONTACTED_AT.toString())))
            .andExpect(jsonPath("$.[*].ownerUsername").value(hasItem(DEFAULT_OWNER_USERNAME)))
            .andExpect(jsonPath("$.[*].contactMethod").value(hasItem(DEFAULT_CONTACT_METHOD)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].candidateIntent").value(hasItem(DEFAULT_CANDIDATE_INTENT)))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL)))
            .andExpect(jsonPath("$.[*].nextAction").value(hasItem(DEFAULT_NEXT_ACTION)))
            .andExpect(jsonPath("$.[*].nextContactAt").value(hasItem(DEFAULT_NEXT_CONTACT_AT.toString())));
    }

    @Test
    @Transactional
    void getPreboardingFollowUp() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get the preboardingFollowUp
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL_ID, preboardingFollowUp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(preboardingFollowUp.getId().intValue()))
            .andExpect(jsonPath("$.candidateId").value(DEFAULT_CANDIDATE_ID.intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.plannedAt").value(DEFAULT_PLANNED_AT.toString()))
            .andExpect(jsonPath("$.contactedAt").value(DEFAULT_CONTACTED_AT.toString()))
            .andExpect(jsonPath("$.ownerUsername").value(DEFAULT_OWNER_USERNAME))
            .andExpect(jsonPath("$.contactMethod").value(DEFAULT_CONTACT_METHOD))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.candidateIntent").value(DEFAULT_CANDIDATE_INTENT))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL))
            .andExpect(jsonPath("$.nextAction").value(DEFAULT_NEXT_ACTION))
            .andExpect(jsonPath("$.nextContactAt").value(DEFAULT_NEXT_CONTACT_AT.toString()));
    }

    @Test
    @Transactional
    void getPreboardingFollowUpsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        Long id = preboardingFollowUp.getId();

        defaultPreboardingFollowUpFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPreboardingFollowUpFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPreboardingFollowUpFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId equals to
        defaultPreboardingFollowUpFiltering("candidateId.equals=" + DEFAULT_CANDIDATE_ID, "candidateId.equals=" + UPDATED_CANDIDATE_ID);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId in
        defaultPreboardingFollowUpFiltering(
            "candidateId.in=" + DEFAULT_CANDIDATE_ID + "," + UPDATED_CANDIDATE_ID,
            "candidateId.in=" + UPDATED_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId is not null
        defaultPreboardingFollowUpFiltering("candidateId.specified=true", "candidateId.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId is greater than or equal to
        defaultPreboardingFollowUpFiltering(
            "candidateId.greaterThanOrEqual=" + DEFAULT_CANDIDATE_ID,
            "candidateId.greaterThanOrEqual=" + UPDATED_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId is less than or equal to
        defaultPreboardingFollowUpFiltering(
            "candidateId.lessThanOrEqual=" + DEFAULT_CANDIDATE_ID,
            "candidateId.lessThanOrEqual=" + SMALLER_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId is less than
        defaultPreboardingFollowUpFiltering("candidateId.lessThan=" + UPDATED_CANDIDATE_ID, "candidateId.lessThan=" + DEFAULT_CANDIDATE_ID);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateId is greater than
        defaultPreboardingFollowUpFiltering(
            "candidateId.greaterThan=" + SMALLER_CANDIDATE_ID,
            "candidateId.greaterThan=" + DEFAULT_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId equals to
        defaultPreboardingFollowUpFiltering("personId.equals=" + DEFAULT_PERSON_ID, "personId.equals=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId in
        defaultPreboardingFollowUpFiltering(
            "personId.in=" + DEFAULT_PERSON_ID + "," + UPDATED_PERSON_ID,
            "personId.in=" + UPDATED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId is not null
        defaultPreboardingFollowUpFiltering("personId.specified=true", "personId.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId is greater than or equal to
        defaultPreboardingFollowUpFiltering(
            "personId.greaterThanOrEqual=" + DEFAULT_PERSON_ID,
            "personId.greaterThanOrEqual=" + UPDATED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId is less than or equal to
        defaultPreboardingFollowUpFiltering(
            "personId.lessThanOrEqual=" + DEFAULT_PERSON_ID,
            "personId.lessThanOrEqual=" + SMALLER_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId is less than
        defaultPreboardingFollowUpFiltering("personId.lessThan=" + UPDATED_PERSON_ID, "personId.lessThan=" + DEFAULT_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPersonIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where personId is greater than
        defaultPreboardingFollowUpFiltering("personId.greaterThan=" + SMALLER_PERSON_ID, "personId.greaterThan=" + DEFAULT_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPlannedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where plannedAt equals to
        defaultPreboardingFollowUpFiltering("plannedAt.equals=" + DEFAULT_PLANNED_AT, "plannedAt.equals=" + UPDATED_PLANNED_AT);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPlannedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where plannedAt in
        defaultPreboardingFollowUpFiltering(
            "plannedAt.in=" + DEFAULT_PLANNED_AT + "," + UPDATED_PLANNED_AT,
            "plannedAt.in=" + UPDATED_PLANNED_AT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByPlannedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where plannedAt is not null
        defaultPreboardingFollowUpFiltering("plannedAt.specified=true", "plannedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactedAt equals to
        defaultPreboardingFollowUpFiltering("contactedAt.equals=" + DEFAULT_CONTACTED_AT, "contactedAt.equals=" + UPDATED_CONTACTED_AT);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactedAt in
        defaultPreboardingFollowUpFiltering(
            "contactedAt.in=" + DEFAULT_CONTACTED_AT + "," + UPDATED_CONTACTED_AT,
            "contactedAt.in=" + UPDATED_CONTACTED_AT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactedAt is not null
        defaultPreboardingFollowUpFiltering("contactedAt.specified=true", "contactedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByOwnerUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where ownerUsername equals to
        defaultPreboardingFollowUpFiltering(
            "ownerUsername.equals=" + DEFAULT_OWNER_USERNAME,
            "ownerUsername.equals=" + UPDATED_OWNER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByOwnerUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where ownerUsername in
        defaultPreboardingFollowUpFiltering(
            "ownerUsername.in=" + DEFAULT_OWNER_USERNAME + "," + UPDATED_OWNER_USERNAME,
            "ownerUsername.in=" + UPDATED_OWNER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByOwnerUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where ownerUsername is not null
        defaultPreboardingFollowUpFiltering("ownerUsername.specified=true", "ownerUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByOwnerUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where ownerUsername contains
        defaultPreboardingFollowUpFiltering(
            "ownerUsername.contains=" + DEFAULT_OWNER_USERNAME,
            "ownerUsername.contains=" + UPDATED_OWNER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByOwnerUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where ownerUsername does not contain
        defaultPreboardingFollowUpFiltering(
            "ownerUsername.doesNotContain=" + UPDATED_OWNER_USERNAME,
            "ownerUsername.doesNotContain=" + DEFAULT_OWNER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactMethod equals to
        defaultPreboardingFollowUpFiltering(
            "contactMethod.equals=" + DEFAULT_CONTACT_METHOD,
            "contactMethod.equals=" + UPDATED_CONTACT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactMethod in
        defaultPreboardingFollowUpFiltering(
            "contactMethod.in=" + DEFAULT_CONTACT_METHOD + "," + UPDATED_CONTACT_METHOD,
            "contactMethod.in=" + UPDATED_CONTACT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactMethod is not null
        defaultPreboardingFollowUpFiltering("contactMethod.specified=true", "contactMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactMethod contains
        defaultPreboardingFollowUpFiltering(
            "contactMethod.contains=" + DEFAULT_CONTACT_METHOD,
            "contactMethod.contains=" + UPDATED_CONTACT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByContactMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where contactMethod does not contain
        defaultPreboardingFollowUpFiltering(
            "contactMethod.doesNotContain=" + UPDATED_CONTACT_METHOD,
            "contactMethod.doesNotContain=" + DEFAULT_CONTACT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIntentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateIntent equals to
        defaultPreboardingFollowUpFiltering(
            "candidateIntent.equals=" + DEFAULT_CANDIDATE_INTENT,
            "candidateIntent.equals=" + UPDATED_CANDIDATE_INTENT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIntentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateIntent in
        defaultPreboardingFollowUpFiltering(
            "candidateIntent.in=" + DEFAULT_CANDIDATE_INTENT + "," + UPDATED_CANDIDATE_INTENT,
            "candidateIntent.in=" + UPDATED_CANDIDATE_INTENT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIntentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateIntent is not null
        defaultPreboardingFollowUpFiltering("candidateIntent.specified=true", "candidateIntent.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIntentContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateIntent contains
        defaultPreboardingFollowUpFiltering(
            "candidateIntent.contains=" + DEFAULT_CANDIDATE_INTENT,
            "candidateIntent.contains=" + UPDATED_CANDIDATE_INTENT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByCandidateIntentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where candidateIntent does not contain
        defaultPreboardingFollowUpFiltering(
            "candidateIntent.doesNotContain=" + UPDATED_CANDIDATE_INTENT,
            "candidateIntent.doesNotContain=" + DEFAULT_CANDIDATE_INTENT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByRiskLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where riskLevel equals to
        defaultPreboardingFollowUpFiltering("riskLevel.equals=" + DEFAULT_RISK_LEVEL, "riskLevel.equals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByRiskLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where riskLevel in
        defaultPreboardingFollowUpFiltering(
            "riskLevel.in=" + DEFAULT_RISK_LEVEL + "," + UPDATED_RISK_LEVEL,
            "riskLevel.in=" + UPDATED_RISK_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByRiskLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where riskLevel is not null
        defaultPreboardingFollowUpFiltering("riskLevel.specified=true", "riskLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByRiskLevelContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where riskLevel contains
        defaultPreboardingFollowUpFiltering("riskLevel.contains=" + DEFAULT_RISK_LEVEL, "riskLevel.contains=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByRiskLevelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where riskLevel does not contain
        defaultPreboardingFollowUpFiltering(
            "riskLevel.doesNotContain=" + UPDATED_RISK_LEVEL,
            "riskLevel.doesNotContain=" + DEFAULT_RISK_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByNextContactAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where nextContactAt equals to
        defaultPreboardingFollowUpFiltering(
            "nextContactAt.equals=" + DEFAULT_NEXT_CONTACT_AT,
            "nextContactAt.equals=" + UPDATED_NEXT_CONTACT_AT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByNextContactAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where nextContactAt in
        defaultPreboardingFollowUpFiltering(
            "nextContactAt.in=" + DEFAULT_NEXT_CONTACT_AT + "," + UPDATED_NEXT_CONTACT_AT,
            "nextContactAt.in=" + UPDATED_NEXT_CONTACT_AT
        );
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByNextContactAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        // Get all the preboardingFollowUpList where nextContactAt is not null
        defaultPreboardingFollowUpFiltering("nextContactAt.specified=true", "nextContactAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPreboardingFollowUpsByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        preboardingFollowUp.setApplication(application);
        preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);
        Long applicationId = application.getId();
        // Get all the preboardingFollowUpList where application equals to applicationId
        defaultPreboardingFollowUpShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the preboardingFollowUpList where application equals to (applicationId + 1)
        defaultPreboardingFollowUpShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultPreboardingFollowUpFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPreboardingFollowUpShouldBeFound(shouldBeFound);
        defaultPreboardingFollowUpShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPreboardingFollowUpShouldBeFound(String filter) throws Exception {
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preboardingFollowUp.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateId").value(hasItem(DEFAULT_CANDIDATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].plannedAt").value(hasItem(DEFAULT_PLANNED_AT.toString())))
            .andExpect(jsonPath("$.[*].contactedAt").value(hasItem(DEFAULT_CONTACTED_AT.toString())))
            .andExpect(jsonPath("$.[*].ownerUsername").value(hasItem(DEFAULT_OWNER_USERNAME)))
            .andExpect(jsonPath("$.[*].contactMethod").value(hasItem(DEFAULT_CONTACT_METHOD)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].candidateIntent").value(hasItem(DEFAULT_CANDIDATE_INTENT)))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL)))
            .andExpect(jsonPath("$.[*].nextAction").value(hasItem(DEFAULT_NEXT_ACTION)))
            .andExpect(jsonPath("$.[*].nextContactAt").value(hasItem(DEFAULT_NEXT_CONTACT_AT.toString())));

        // Check, that the count call also returns 1
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPreboardingFollowUpShouldNotBeFound(String filter) throws Exception {
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPreboardingFollowUpMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPreboardingFollowUp() throws Exception {
        // Get the preboardingFollowUp
        restPreboardingFollowUpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPreboardingFollowUp() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the preboardingFollowUp
        PreboardingFollowUp updatedPreboardingFollowUp = preboardingFollowUpRepository.findById(preboardingFollowUp.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPreboardingFollowUp are not directly saved in db
        em.detach(updatedPreboardingFollowUp);
        updatedPreboardingFollowUp
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .plannedAt(UPDATED_PLANNED_AT)
            .contactedAt(UPDATED_CONTACTED_AT)
            .ownerUsername(UPDATED_OWNER_USERNAME)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .result(UPDATED_RESULT)
            .candidateIntent(UPDATED_CANDIDATE_INTENT)
            .riskLevel(UPDATED_RISK_LEVEL)
            .nextAction(UPDATED_NEXT_ACTION)
            .nextContactAt(UPDATED_NEXT_CONTACT_AT);
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(updatedPreboardingFollowUp);

        restPreboardingFollowUpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, preboardingFollowUpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isOk());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPreboardingFollowUpToMatchAllProperties(updatedPreboardingFollowUp);
    }

    @Test
    @Transactional
    void putNonExistingPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, preboardingFollowUpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preboardingFollowUpDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePreboardingFollowUpWithPatch() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the preboardingFollowUp using partial update
        PreboardingFollowUp partialUpdatedPreboardingFollowUp = new PreboardingFollowUp();
        partialUpdatedPreboardingFollowUp.setId(preboardingFollowUp.getId());

        partialUpdatedPreboardingFollowUp
            .personId(UPDATED_PERSON_ID)
            .plannedAt(UPDATED_PLANNED_AT)
            .contactedAt(UPDATED_CONTACTED_AT)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .result(UPDATED_RESULT);

        restPreboardingFollowUpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreboardingFollowUp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPreboardingFollowUp))
            )
            .andExpect(status().isOk());

        // Validate the PreboardingFollowUp in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPreboardingFollowUpUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPreboardingFollowUp, preboardingFollowUp),
            getPersistedPreboardingFollowUp(preboardingFollowUp)
        );
    }

    @Test
    @Transactional
    void fullUpdatePreboardingFollowUpWithPatch() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the preboardingFollowUp using partial update
        PreboardingFollowUp partialUpdatedPreboardingFollowUp = new PreboardingFollowUp();
        partialUpdatedPreboardingFollowUp.setId(preboardingFollowUp.getId());

        partialUpdatedPreboardingFollowUp
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .plannedAt(UPDATED_PLANNED_AT)
            .contactedAt(UPDATED_CONTACTED_AT)
            .ownerUsername(UPDATED_OWNER_USERNAME)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .result(UPDATED_RESULT)
            .candidateIntent(UPDATED_CANDIDATE_INTENT)
            .riskLevel(UPDATED_RISK_LEVEL)
            .nextAction(UPDATED_NEXT_ACTION)
            .nextContactAt(UPDATED_NEXT_CONTACT_AT);

        restPreboardingFollowUpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreboardingFollowUp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPreboardingFollowUp))
            )
            .andExpect(status().isOk());

        // Validate the PreboardingFollowUp in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPreboardingFollowUpUpdatableFieldsEquals(
            partialUpdatedPreboardingFollowUp,
            getPersistedPreboardingFollowUp(partialUpdatedPreboardingFollowUp)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, preboardingFollowUpDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPreboardingFollowUp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        preboardingFollowUp.setId(longCount.incrementAndGet());

        // Create the PreboardingFollowUp
        PreboardingFollowUpDTO preboardingFollowUpDTO = preboardingFollowUpMapper.toDto(preboardingFollowUp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreboardingFollowUpMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(preboardingFollowUpDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PreboardingFollowUp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePreboardingFollowUp() throws Exception {
        // Initialize the database
        insertedPreboardingFollowUp = preboardingFollowUpRepository.saveAndFlush(preboardingFollowUp);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the preboardingFollowUp
        restPreboardingFollowUpMockMvc
            .perform(delete(ENTITY_API_URL_ID, preboardingFollowUp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return preboardingFollowUpRepository.count();
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

    protected PreboardingFollowUp getPersistedPreboardingFollowUp(PreboardingFollowUp preboardingFollowUp) {
        return preboardingFollowUpRepository.findById(preboardingFollowUp.getId()).orElseThrow();
    }

    protected void assertPersistedPreboardingFollowUpToMatchAllProperties(PreboardingFollowUp expectedPreboardingFollowUp) {
        assertPreboardingFollowUpAllPropertiesEquals(
            expectedPreboardingFollowUp,
            getPersistedPreboardingFollowUp(expectedPreboardingFollowUp)
        );
    }

    protected void assertPersistedPreboardingFollowUpToMatchUpdatableProperties(PreboardingFollowUp expectedPreboardingFollowUp) {
        assertPreboardingFollowUpAllUpdatablePropertiesEquals(
            expectedPreboardingFollowUp,
            getPersistedPreboardingFollowUp(expectedPreboardingFollowUp)
        );
    }
}
