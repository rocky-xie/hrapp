package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.ApplicationStageAsserts.*;
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
import top.btmdc.hr.domain.ApplicationStage;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.service.dto.ApplicationStageDTO;
import top.btmdc.hr.service.mapper.ApplicationStageMapper;

/**
 * Integration tests for the {@link ApplicationStageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class ApplicationStageResourceIT {

    private static final ApplicationStageType DEFAULT_STAGE_TYPE = ApplicationStageType.RESUME_SCREENING;
    private static final ApplicationStageType UPDATED_STAGE_TYPE = ApplicationStageType.WRITTEN_TEST;

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final String DEFAULT_STAGE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STAGE_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_SELECTED = false;
    private static final Boolean UPDATED_SELECTED = true;

    private static final String DEFAULT_DECISION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DECISION_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationStageRepository applicationStageRepository;

    @Autowired
    private ApplicationStageMapper applicationStageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationStageMockMvc;

    private ApplicationStage applicationStage;

    private ApplicationStage insertedApplicationStage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationStage createEntity() {
        return new ApplicationStage()
            .stageType(DEFAULT_STAGE_TYPE)
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .stageStatus(DEFAULT_STAGE_STATUS)
            .startedAt(DEFAULT_STARTED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .selected(DEFAULT_SELECTED)
            .decisionReason(DEFAULT_DECISION_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationStage createUpdatedEntity() {
        return new ApplicationStage()
            .stageType(UPDATED_STAGE_TYPE)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .stageStatus(UPDATED_STAGE_STATUS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .selected(UPDATED_SELECTED)
            .decisionReason(UPDATED_DECISION_REASON);
    }

    @BeforeEach
    void initTest() {
        applicationStage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedApplicationStage != null) {
            applicationStageRepository.delete(insertedApplicationStage);
            insertedApplicationStage = null;
        }
    }

    @Test
    @Transactional
    void createApplicationStage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);
        var returnedApplicationStageDTO = om.readValue(
            restApplicationStageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationStageDTO.class
        );

        // Validate the ApplicationStage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApplicationStage = applicationStageMapper.toEntity(returnedApplicationStageDTO);
        assertApplicationStageUpdatableFieldsEquals(returnedApplicationStage, getPersistedApplicationStage(returnedApplicationStage));

        insertedApplicationStage = returnedApplicationStage;
    }

    @Test
    @Transactional
    void createApplicationStageWithExistingId() throws Exception {
        // Create the ApplicationStage with an existing ID
        applicationStage.setId(1L);
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStageTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationStage.setStageType(null);

        // Create the ApplicationStage, which fails.
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        restApplicationStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationStage.setSequenceNumber(null);

        // Create the ApplicationStage, which fails.
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        restApplicationStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStageStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationStage.setStageStatus(null);

        // Create the ApplicationStage, which fails.
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        restApplicationStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationStages() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].stageStatus").value(hasItem(DEFAULT_STAGE_STATUS)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].selected").value(hasItem(DEFAULT_SELECTED)))
            .andExpect(jsonPath("$.[*].decisionReason").value(hasItem(DEFAULT_DECISION_REASON)));
    }

    @Test
    @Transactional
    void getApplicationStage() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get the applicationStage
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationStage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationStage.getId().intValue()))
            .andExpect(jsonPath("$.stageType").value(DEFAULT_STAGE_TYPE.toString()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.stageStatus").value(DEFAULT_STAGE_STATUS))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.selected").value(DEFAULT_SELECTED))
            .andExpect(jsonPath("$.decisionReason").value(DEFAULT_DECISION_REASON));
    }

    @Test
    @Transactional
    void getApplicationStagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        Long id = applicationStage.getId();

        defaultApplicationStageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultApplicationStageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultApplicationStageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageType equals to
        defaultApplicationStageFiltering("stageType.equals=" + DEFAULT_STAGE_TYPE, "stageType.equals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageType in
        defaultApplicationStageFiltering(
            "stageType.in=" + DEFAULT_STAGE_TYPE + "," + UPDATED_STAGE_TYPE,
            "stageType.in=" + UPDATED_STAGE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageType is not null
        defaultApplicationStageFiltering("stageType.specified=true", "stageType.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber equals to
        defaultApplicationStageFiltering(
            "sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER,
            "sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber in
        defaultApplicationStageFiltering(
            "sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER,
            "sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber is not null
        defaultApplicationStageFiltering("sequenceNumber.specified=true", "sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber is greater than or equal to
        defaultApplicationStageFiltering(
            "sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER,
            "sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber is less than or equal to
        defaultApplicationStageFiltering(
            "sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER,
            "sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber is less than
        defaultApplicationStageFiltering(
            "sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER,
            "sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where sequenceNumber is greater than
        defaultApplicationStageFiltering(
            "sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER,
            "sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageStatus equals to
        defaultApplicationStageFiltering("stageStatus.equals=" + DEFAULT_STAGE_STATUS, "stageStatus.equals=" + UPDATED_STAGE_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageStatus in
        defaultApplicationStageFiltering(
            "stageStatus.in=" + DEFAULT_STAGE_STATUS + "," + UPDATED_STAGE_STATUS,
            "stageStatus.in=" + UPDATED_STAGE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageStatus is not null
        defaultApplicationStageFiltering("stageStatus.specified=true", "stageStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageStatus contains
        defaultApplicationStageFiltering("stageStatus.contains=" + DEFAULT_STAGE_STATUS, "stageStatus.contains=" + UPDATED_STAGE_STATUS);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStageStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where stageStatus does not contain
        defaultApplicationStageFiltering(
            "stageStatus.doesNotContain=" + UPDATED_STAGE_STATUS,
            "stageStatus.doesNotContain=" + DEFAULT_STAGE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where startedAt equals to
        defaultApplicationStageFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where startedAt in
        defaultApplicationStageFiltering(
            "startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT,
            "startedAt.in=" + UPDATED_STARTED_AT
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where startedAt is not null
        defaultApplicationStageFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where completedAt equals to
        defaultApplicationStageFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationStagesByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where completedAt in
        defaultApplicationStageFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllApplicationStagesByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where completedAt is not null
        defaultApplicationStageFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySelectedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where selected equals to
        defaultApplicationStageFiltering("selected.equals=" + DEFAULT_SELECTED, "selected.equals=" + UPDATED_SELECTED);
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySelectedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where selected in
        defaultApplicationStageFiltering("selected.in=" + DEFAULT_SELECTED + "," + UPDATED_SELECTED, "selected.in=" + UPDATED_SELECTED);
    }

    @Test
    @Transactional
    void getAllApplicationStagesBySelectedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        // Get all the applicationStageList where selected is not null
        defaultApplicationStageFiltering("selected.specified=true", "selected.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationStagesByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            applicationStageRepository.saveAndFlush(applicationStage);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        applicationStage.setApplication(application);
        applicationStageRepository.saveAndFlush(applicationStage);
        Long applicationId = application.getId();
        // Get all the applicationStageList where application equals to applicationId
        defaultApplicationStageShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the applicationStageList where application equals to (applicationId + 1)
        defaultApplicationStageShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultApplicationStageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultApplicationStageShouldBeFound(shouldBeFound);
        defaultApplicationStageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationStageShouldBeFound(String filter) throws Exception {
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].stageStatus").value(hasItem(DEFAULT_STAGE_STATUS)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].selected").value(hasItem(DEFAULT_SELECTED)))
            .andExpect(jsonPath("$.[*].decisionReason").value(hasItem(DEFAULT_DECISION_REASON)));

        // Check, that the count call also returns 1
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationStageShouldNotBeFound(String filter) throws Exception {
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationStage() throws Exception {
        // Get the applicationStage
        restApplicationStageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationStage() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationStage
        ApplicationStage updatedApplicationStage = applicationStageRepository.findById(applicationStage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationStage are not directly saved in db
        em.detach(updatedApplicationStage);
        updatedApplicationStage
            .stageType(UPDATED_STAGE_TYPE)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .stageStatus(UPDATED_STAGE_STATUS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .selected(UPDATED_SELECTED)
            .decisionReason(UPDATED_DECISION_REASON);
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(updatedApplicationStage);

        restApplicationStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationStageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationStageToMatchAllProperties(updatedApplicationStage);
    }

    @Test
    @Transactional
    void putNonExistingApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationStageWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationStage using partial update
        ApplicationStage partialUpdatedApplicationStage = new ApplicationStage();
        partialUpdatedApplicationStage.setId(applicationStage.getId());

        partialUpdatedApplicationStage.startedAt(UPDATED_STARTED_AT).selected(UPDATED_SELECTED).decisionReason(UPDATED_DECISION_REASON);

        restApplicationStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationStage))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationStage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationStageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationStage, applicationStage),
            getPersistedApplicationStage(applicationStage)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationStageWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationStage using partial update
        ApplicationStage partialUpdatedApplicationStage = new ApplicationStage();
        partialUpdatedApplicationStage.setId(applicationStage.getId());

        partialUpdatedApplicationStage
            .stageType(UPDATED_STAGE_TYPE)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .stageStatus(UPDATED_STAGE_STATUS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .selected(UPDATED_SELECTED)
            .decisionReason(UPDATED_DECISION_REASON);

        restApplicationStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationStage))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationStage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationStageUpdatableFieldsEquals(
            partialUpdatedApplicationStage,
            getPersistedApplicationStage(partialUpdatedApplicationStage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationStageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationStage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationStage.setId(longCount.incrementAndGet());

        // Create the ApplicationStage
        ApplicationStageDTO applicationStageDTO = applicationStageMapper.toDto(applicationStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationStageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(applicationStageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationStage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationStage() throws Exception {
        // Initialize the database
        insertedApplicationStage = applicationStageRepository.saveAndFlush(applicationStage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the applicationStage
        restApplicationStageMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationStage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationStageRepository.count();
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

    protected ApplicationStage getPersistedApplicationStage(ApplicationStage applicationStage) {
        return applicationStageRepository.findById(applicationStage.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationStageToMatchAllProperties(ApplicationStage expectedApplicationStage) {
        assertApplicationStageAllPropertiesEquals(expectedApplicationStage, getPersistedApplicationStage(expectedApplicationStage));
    }

    protected void assertPersistedApplicationStageToMatchUpdatableProperties(ApplicationStage expectedApplicationStage) {
        assertApplicationStageAllUpdatablePropertiesEquals(
            expectedApplicationStage,
            getPersistedApplicationStage(expectedApplicationStage)
        );
    }
}
