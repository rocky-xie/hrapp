package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.HiringDecisionAsserts.*;
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
import top.btmdc.hr.domain.HiringDecision;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.service.dto.HiringDecisionDTO;
import top.btmdc.hr.service.mapper.HiringDecisionMapper;

/**
 * Integration tests for the {@link HiringDecisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class HiringDecisionResourceIT {

    private static final ApplicationDecision DEFAULT_DECISION = ApplicationDecision.HIRED;
    private static final ApplicationDecision UPDATED_DECISION = ApplicationDecision.NOT_HIRED;

    private static final String DEFAULT_DECISION_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_DECISION_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_DECIDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_DECIDED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DECIDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECIDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Long DEFAULT_CORRECTION_OF_ID = 1L;
    private static final Long UPDATED_CORRECTION_OF_ID = 2L;
    private static final Long SMALLER_CORRECTION_OF_ID = 1L - 1L;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/hiring-decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HiringDecisionRepository hiringDecisionRepository;

    @Autowired
    private HiringDecisionMapper hiringDecisionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHiringDecisionMockMvc;

    private HiringDecision hiringDecision;

    private HiringDecision insertedHiringDecision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HiringDecision createEntity() {
        return new HiringDecision()
            .decision(DEFAULT_DECISION)
            .decisionSource(DEFAULT_DECISION_SOURCE)
            .decidedBy(DEFAULT_DECIDED_BY)
            .decidedAt(DEFAULT_DECIDED_AT)
            .reason(DEFAULT_REASON)
            .correctionOfId(DEFAULT_CORRECTION_OF_ID)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HiringDecision createUpdatedEntity() {
        return new HiringDecision()
            .decision(UPDATED_DECISION)
            .decisionSource(UPDATED_DECISION_SOURCE)
            .decidedBy(UPDATED_DECIDED_BY)
            .decidedAt(UPDATED_DECIDED_AT)
            .reason(UPDATED_REASON)
            .correctionOfId(UPDATED_CORRECTION_OF_ID)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        hiringDecision = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHiringDecision != null) {
            hiringDecisionRepository.delete(insertedHiringDecision);
            insertedHiringDecision = null;
        }
    }

    @Test
    @Transactional
    void createHiringDecision() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);
        var returnedHiringDecisionDTO = om.readValue(
            restHiringDecisionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HiringDecisionDTO.class
        );

        // Validate the HiringDecision in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHiringDecision = hiringDecisionMapper.toEntity(returnedHiringDecisionDTO);
        assertHiringDecisionUpdatableFieldsEquals(returnedHiringDecision, getPersistedHiringDecision(returnedHiringDecision));

        insertedHiringDecision = returnedHiringDecision;
    }

    @Test
    @Transactional
    void createHiringDecisionWithExistingId() throws Exception {
        // Create the HiringDecision with an existing ID
        hiringDecision.setId(1L);
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDecisionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiringDecision.setDecision(null);

        // Create the HiringDecision, which fails.
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecisionSourceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiringDecision.setDecisionSource(null);

        // Create the HiringDecision, which fails.
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecidedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiringDecision.setDecidedBy(null);

        // Create the HiringDecision, which fails.
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecidedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiringDecision.setDecidedAt(null);

        // Create the HiringDecision, which fails.
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hiringDecision.setActive(null);

        // Create the HiringDecision, which fails.
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        restHiringDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHiringDecisions() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hiringDecision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].decisionSource").value(hasItem(DEFAULT_DECISION_SOURCE)))
            .andExpect(jsonPath("$.[*].decidedBy").value(hasItem(DEFAULT_DECIDED_BY)))
            .andExpect(jsonPath("$.[*].decidedAt").value(hasItem(DEFAULT_DECIDED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].correctionOfId").value(hasItem(DEFAULT_CORRECTION_OF_ID.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getHiringDecision() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get the hiringDecision
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL_ID, hiringDecision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hiringDecision.getId().intValue()))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION.toString()))
            .andExpect(jsonPath("$.decisionSource").value(DEFAULT_DECISION_SOURCE))
            .andExpect(jsonPath("$.decidedBy").value(DEFAULT_DECIDED_BY))
            .andExpect(jsonPath("$.decidedAt").value(DEFAULT_DECIDED_AT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.correctionOfId").value(DEFAULT_CORRECTION_OF_ID.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getHiringDecisionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        Long id = hiringDecision.getId();

        defaultHiringDecisionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHiringDecisionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHiringDecisionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decision equals to
        defaultHiringDecisionFiltering("decision.equals=" + DEFAULT_DECISION, "decision.equals=" + UPDATED_DECISION);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decision in
        defaultHiringDecisionFiltering("decision.in=" + DEFAULT_DECISION + "," + UPDATED_DECISION, "decision.in=" + UPDATED_DECISION);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decision is not null
        defaultHiringDecisionFiltering("decision.specified=true", "decision.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decisionSource equals to
        defaultHiringDecisionFiltering(
            "decisionSource.equals=" + DEFAULT_DECISION_SOURCE,
            "decisionSource.equals=" + UPDATED_DECISION_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decisionSource in
        defaultHiringDecisionFiltering(
            "decisionSource.in=" + DEFAULT_DECISION_SOURCE + "," + UPDATED_DECISION_SOURCE,
            "decisionSource.in=" + UPDATED_DECISION_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decisionSource is not null
        defaultHiringDecisionFiltering("decisionSource.specified=true", "decisionSource.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decisionSource contains
        defaultHiringDecisionFiltering(
            "decisionSource.contains=" + DEFAULT_DECISION_SOURCE,
            "decisionSource.contains=" + UPDATED_DECISION_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecisionSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decisionSource does not contain
        defaultHiringDecisionFiltering(
            "decisionSource.doesNotContain=" + UPDATED_DECISION_SOURCE,
            "decisionSource.doesNotContain=" + DEFAULT_DECISION_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedBy equals to
        defaultHiringDecisionFiltering("decidedBy.equals=" + DEFAULT_DECIDED_BY, "decidedBy.equals=" + UPDATED_DECIDED_BY);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedBy in
        defaultHiringDecisionFiltering(
            "decidedBy.in=" + DEFAULT_DECIDED_BY + "," + UPDATED_DECIDED_BY,
            "decidedBy.in=" + UPDATED_DECIDED_BY
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedBy is not null
        defaultHiringDecisionFiltering("decidedBy.specified=true", "decidedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedByContainsSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedBy contains
        defaultHiringDecisionFiltering("decidedBy.contains=" + DEFAULT_DECIDED_BY, "decidedBy.contains=" + UPDATED_DECIDED_BY);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedBy does not contain
        defaultHiringDecisionFiltering("decidedBy.doesNotContain=" + UPDATED_DECIDED_BY, "decidedBy.doesNotContain=" + DEFAULT_DECIDED_BY);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedAt equals to
        defaultHiringDecisionFiltering("decidedAt.equals=" + DEFAULT_DECIDED_AT, "decidedAt.equals=" + UPDATED_DECIDED_AT);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedAt in
        defaultHiringDecisionFiltering(
            "decidedAt.in=" + DEFAULT_DECIDED_AT + "," + UPDATED_DECIDED_AT,
            "decidedAt.in=" + UPDATED_DECIDED_AT
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByDecidedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where decidedAt is not null
        defaultHiringDecisionFiltering("decidedAt.specified=true", "decidedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId equals to
        defaultHiringDecisionFiltering(
            "correctionOfId.equals=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.equals=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId in
        defaultHiringDecisionFiltering(
            "correctionOfId.in=" + DEFAULT_CORRECTION_OF_ID + "," + UPDATED_CORRECTION_OF_ID,
            "correctionOfId.in=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId is not null
        defaultHiringDecisionFiltering("correctionOfId.specified=true", "correctionOfId.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId is greater than or equal to
        defaultHiringDecisionFiltering(
            "correctionOfId.greaterThanOrEqual=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.greaterThanOrEqual=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId is less than or equal to
        defaultHiringDecisionFiltering(
            "correctionOfId.lessThanOrEqual=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.lessThanOrEqual=" + SMALLER_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId is less than
        defaultHiringDecisionFiltering(
            "correctionOfId.lessThan=" + UPDATED_CORRECTION_OF_ID,
            "correctionOfId.lessThan=" + DEFAULT_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByCorrectionOfIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where correctionOfId is greater than
        defaultHiringDecisionFiltering(
            "correctionOfId.greaterThan=" + SMALLER_CORRECTION_OF_ID,
            "correctionOfId.greaterThan=" + DEFAULT_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where active equals to
        defaultHiringDecisionFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where active in
        defaultHiringDecisionFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        // Get all the hiringDecisionList where active is not null
        defaultHiringDecisionFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllHiringDecisionsByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            hiringDecisionRepository.saveAndFlush(hiringDecision);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        hiringDecision.setApplication(application);
        hiringDecisionRepository.saveAndFlush(hiringDecision);
        Long applicationId = application.getId();
        // Get all the hiringDecisionList where application equals to applicationId
        defaultHiringDecisionShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the hiringDecisionList where application equals to (applicationId + 1)
        defaultHiringDecisionShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultHiringDecisionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHiringDecisionShouldBeFound(shouldBeFound);
        defaultHiringDecisionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHiringDecisionShouldBeFound(String filter) throws Exception {
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hiringDecision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].decisionSource").value(hasItem(DEFAULT_DECISION_SOURCE)))
            .andExpect(jsonPath("$.[*].decidedBy").value(hasItem(DEFAULT_DECIDED_BY)))
            .andExpect(jsonPath("$.[*].decidedAt").value(hasItem(DEFAULT_DECIDED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].correctionOfId").value(hasItem(DEFAULT_CORRECTION_OF_ID.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHiringDecisionShouldNotBeFound(String filter) throws Exception {
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHiringDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHiringDecision() throws Exception {
        // Get the hiringDecision
        restHiringDecisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHiringDecision() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiringDecision
        HiringDecision updatedHiringDecision = hiringDecisionRepository.findById(hiringDecision.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHiringDecision are not directly saved in db
        em.detach(updatedHiringDecision);
        updatedHiringDecision
            .decision(UPDATED_DECISION)
            .decisionSource(UPDATED_DECISION_SOURCE)
            .decidedBy(UPDATED_DECIDED_BY)
            .decidedAt(UPDATED_DECIDED_AT)
            .reason(UPDATED_REASON)
            .correctionOfId(UPDATED_CORRECTION_OF_ID)
            .active(UPDATED_ACTIVE);
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(updatedHiringDecision);

        restHiringDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiringDecisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiringDecisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHiringDecisionToMatchAllProperties(updatedHiringDecision);
    }

    @Test
    @Transactional
    void putNonExistingHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hiringDecisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiringDecisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hiringDecisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHiringDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiringDecision using partial update
        HiringDecision partialUpdatedHiringDecision = new HiringDecision();
        partialUpdatedHiringDecision.setId(hiringDecision.getId());

        partialUpdatedHiringDecision.decisionSource(UPDATED_DECISION_SOURCE);

        restHiringDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiringDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiringDecision))
            )
            .andExpect(status().isOk());

        // Validate the HiringDecision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiringDecisionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHiringDecision, hiringDecision),
            getPersistedHiringDecision(hiringDecision)
        );
    }

    @Test
    @Transactional
    void fullUpdateHiringDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hiringDecision using partial update
        HiringDecision partialUpdatedHiringDecision = new HiringDecision();
        partialUpdatedHiringDecision.setId(hiringDecision.getId());

        partialUpdatedHiringDecision
            .decision(UPDATED_DECISION)
            .decisionSource(UPDATED_DECISION_SOURCE)
            .decidedBy(UPDATED_DECIDED_BY)
            .decidedAt(UPDATED_DECIDED_AT)
            .reason(UPDATED_REASON)
            .correctionOfId(UPDATED_CORRECTION_OF_ID)
            .active(UPDATED_ACTIVE);

        restHiringDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHiringDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHiringDecision))
            )
            .andExpect(status().isOk());

        // Validate the HiringDecision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHiringDecisionUpdatableFieldsEquals(partialUpdatedHiringDecision, getPersistedHiringDecision(partialUpdatedHiringDecision));
    }

    @Test
    @Transactional
    void patchNonExistingHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hiringDecisionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiringDecisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hiringDecisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHiringDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hiringDecision.setId(longCount.incrementAndGet());

        // Create the HiringDecision
        HiringDecisionDTO hiringDecisionDTO = hiringDecisionMapper.toDto(hiringDecision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHiringDecisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hiringDecisionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HiringDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHiringDecision() throws Exception {
        // Initialize the database
        insertedHiringDecision = hiringDecisionRepository.saveAndFlush(hiringDecision);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hiringDecision
        restHiringDecisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, hiringDecision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hiringDecisionRepository.count();
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

    protected HiringDecision getPersistedHiringDecision(HiringDecision hiringDecision) {
        return hiringDecisionRepository.findById(hiringDecision.getId()).orElseThrow();
    }

    protected void assertPersistedHiringDecisionToMatchAllProperties(HiringDecision expectedHiringDecision) {
        assertHiringDecisionAllPropertiesEquals(expectedHiringDecision, getPersistedHiringDecision(expectedHiringDecision));
    }

    protected void assertPersistedHiringDecisionToMatchUpdatableProperties(HiringDecision expectedHiringDecision) {
        assertHiringDecisionAllUpdatablePropertiesEquals(expectedHiringDecision, getPersistedHiringDecision(expectedHiringDecision));
    }
}
