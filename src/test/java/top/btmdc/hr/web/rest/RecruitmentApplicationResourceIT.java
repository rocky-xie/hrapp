package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.RecruitmentApplicationAsserts.*;
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
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.mapper.RecruitmentApplicationMapper;

/**
 * Integration tests for the {@link RecruitmentApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class RecruitmentApplicationResourceIT {

    private static final String DEFAULT_APPLICATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_CODE = "BBBBBBBBBB";

    private static final RecruitmentType DEFAULT_RECRUITMENT_TYPE = RecruitmentType.GRADUATE;
    private static final RecruitmentType UPDATED_RECRUITMENT_TYPE = RecruitmentType.MID_CAREER;

    private static final ApplicationStageType DEFAULT_CURRENT_STAGE = ApplicationStageType.RESUME_SCREENING;
    private static final ApplicationStageType UPDATED_CURRENT_STAGE = ApplicationStageType.WRITTEN_TEST;

    private static final ApplicationStatus DEFAULT_STATUS = ApplicationStatus.IN_PROGRESS;
    private static final ApplicationStatus UPDATED_STATUS = ApplicationStatus.ON_HOLD;

    private static final ApplicationDecision DEFAULT_DECISION = ApplicationDecision.HIRED;
    private static final ApplicationDecision UPDATED_DECISION = ApplicationDecision.NOT_HIRED;

    private static final String DEFAULT_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPLIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPLIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_RETENTION_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETENTION_UNTIL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RETENTION_UNTIL = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_CONVERTED_PERSON_ID = 1L;
    private static final Long UPDATED_CONVERTED_PERSON_ID = 2L;
    private static final Long SMALLER_CONVERTED_PERSON_ID = 1L - 1L;

    private static final Instant DEFAULT_CONVERTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONVERTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CLOSE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recruitment-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruitmentApplicationRepository recruitmentApplicationRepository;

    @Autowired
    private RecruitmentApplicationMapper recruitmentApplicationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentApplicationMockMvc;

    private RecruitmentApplication recruitmentApplication;

    private RecruitmentApplication insertedRecruitmentApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentApplication createEntity() {
        return new RecruitmentApplication()
            .applicationCode(DEFAULT_APPLICATION_CODE)
            .recruitmentType(DEFAULT_RECRUITMENT_TYPE)
            .currentStage(DEFAULT_CURRENT_STAGE)
            .status(DEFAULT_STATUS)
            .decision(DEFAULT_DECISION)
            .sourceType(DEFAULT_SOURCE_TYPE)
            .sourceDetail(DEFAULT_SOURCE_DETAIL)
            .batchName(DEFAULT_BATCH_NAME)
            .appliedAt(DEFAULT_APPLIED_AT)
            .closedAt(DEFAULT_CLOSED_AT)
            .retentionUntil(DEFAULT_RETENTION_UNTIL)
            .convertedPersonId(DEFAULT_CONVERTED_PERSON_ID)
            .convertedAt(DEFAULT_CONVERTED_AT)
            .closeReason(DEFAULT_CLOSE_REASON)
            .note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentApplication createUpdatedEntity() {
        return new RecruitmentApplication()
            .applicationCode(UPDATED_APPLICATION_CODE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .currentStage(UPDATED_CURRENT_STAGE)
            .status(UPDATED_STATUS)
            .decision(UPDATED_DECISION)
            .sourceType(UPDATED_SOURCE_TYPE)
            .sourceDetail(UPDATED_SOURCE_DETAIL)
            .batchName(UPDATED_BATCH_NAME)
            .appliedAt(UPDATED_APPLIED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .retentionUntil(UPDATED_RETENTION_UNTIL)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .convertedAt(UPDATED_CONVERTED_AT)
            .closeReason(UPDATED_CLOSE_REASON)
            .note(UPDATED_NOTE);
    }

    @BeforeEach
    void initTest() {
        recruitmentApplication = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecruitmentApplication != null) {
            recruitmentApplicationRepository.delete(insertedRecruitmentApplication);
            insertedRecruitmentApplication = null;
        }
    }

    @Test
    @Transactional
    void createRecruitmentApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);
        var returnedRecruitmentApplicationDTO = om.readValue(
            restRecruitmentApplicationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecruitmentApplicationDTO.class
        );

        // Validate the RecruitmentApplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecruitmentApplication = recruitmentApplicationMapper.toEntity(returnedRecruitmentApplicationDTO);
        assertRecruitmentApplicationUpdatableFieldsEquals(
            returnedRecruitmentApplication,
            getPersistedRecruitmentApplication(returnedRecruitmentApplication)
        );

        insertedRecruitmentApplication = returnedRecruitmentApplication;
    }

    @Test
    @Transactional
    void createRecruitmentApplicationWithExistingId() throws Exception {
        // Create the RecruitmentApplication with an existing ID
        recruitmentApplication.setId(1L);
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentApplication.setApplicationCode(null);

        // Create the RecruitmentApplication, which fails.
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecruitmentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentApplication.setRecruitmentType(null);

        // Create the RecruitmentApplication, which fails.
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentApplication.setStatus(null);

        // Create the RecruitmentApplication, which fails.
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentApplication.setSourceType(null);

        // Create the RecruitmentApplication, which fails.
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAppliedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentApplication.setAppliedAt(null);

        // Create the RecruitmentApplication, which fails.
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplications() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationCode").value(hasItem(DEFAULT_APPLICATION_CODE)))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currentStage").value(hasItem(DEFAULT_CURRENT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].sourceDetail").value(hasItem(DEFAULT_SOURCE_DETAIL)))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].retentionUntil").value(hasItem(DEFAULT_RETENTION_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].convertedPersonId").value(hasItem(DEFAULT_CONVERTED_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].convertedAt").value(hasItem(DEFAULT_CONVERTED_AT.toString())))
            .andExpect(jsonPath("$.[*].closeReason").value(hasItem(DEFAULT_CLOSE_REASON)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getRecruitmentApplication() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get the recruitmentApplication
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentApplication.getId().intValue()))
            .andExpect(jsonPath("$.applicationCode").value(DEFAULT_APPLICATION_CODE))
            .andExpect(jsonPath("$.recruitmentType").value(DEFAULT_RECRUITMENT_TYPE.toString()))
            .andExpect(jsonPath("$.currentStage").value(DEFAULT_CURRENT_STAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION.toString()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE))
            .andExpect(jsonPath("$.sourceDetail").value(DEFAULT_SOURCE_DETAIL))
            .andExpect(jsonPath("$.batchName").value(DEFAULT_BATCH_NAME))
            .andExpect(jsonPath("$.appliedAt").value(DEFAULT_APPLIED_AT.toString()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.retentionUntil").value(DEFAULT_RETENTION_UNTIL.toString()))
            .andExpect(jsonPath("$.convertedPersonId").value(DEFAULT_CONVERTED_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.convertedAt").value(DEFAULT_CONVERTED_AT.toString()))
            .andExpect(jsonPath("$.closeReason").value(DEFAULT_CLOSE_REASON))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getRecruitmentApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        Long id = recruitmentApplication.getId();

        defaultRecruitmentApplicationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecruitmentApplicationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecruitmentApplicationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByApplicationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where applicationCode equals to
        defaultRecruitmentApplicationFiltering(
            "applicationCode.equals=" + DEFAULT_APPLICATION_CODE,
            "applicationCode.equals=" + UPDATED_APPLICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByApplicationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where applicationCode in
        defaultRecruitmentApplicationFiltering(
            "applicationCode.in=" + DEFAULT_APPLICATION_CODE + "," + UPDATED_APPLICATION_CODE,
            "applicationCode.in=" + UPDATED_APPLICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByApplicationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where applicationCode is not null
        defaultRecruitmentApplicationFiltering("applicationCode.specified=true", "applicationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByApplicationCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where applicationCode contains
        defaultRecruitmentApplicationFiltering(
            "applicationCode.contains=" + DEFAULT_APPLICATION_CODE,
            "applicationCode.contains=" + UPDATED_APPLICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByApplicationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where applicationCode does not contain
        defaultRecruitmentApplicationFiltering(
            "applicationCode.doesNotContain=" + UPDATED_APPLICATION_CODE,
            "applicationCode.doesNotContain=" + DEFAULT_APPLICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRecruitmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where recruitmentType equals to
        defaultRecruitmentApplicationFiltering(
            "recruitmentType.equals=" + DEFAULT_RECRUITMENT_TYPE,
            "recruitmentType.equals=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRecruitmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where recruitmentType in
        defaultRecruitmentApplicationFiltering(
            "recruitmentType.in=" + DEFAULT_RECRUITMENT_TYPE + "," + UPDATED_RECRUITMENT_TYPE,
            "recruitmentType.in=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRecruitmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where recruitmentType is not null
        defaultRecruitmentApplicationFiltering("recruitmentType.specified=true", "recruitmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByCurrentStageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where currentStage equals to
        defaultRecruitmentApplicationFiltering(
            "currentStage.equals=" + DEFAULT_CURRENT_STAGE,
            "currentStage.equals=" + UPDATED_CURRENT_STAGE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByCurrentStageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where currentStage in
        defaultRecruitmentApplicationFiltering(
            "currentStage.in=" + DEFAULT_CURRENT_STAGE + "," + UPDATED_CURRENT_STAGE,
            "currentStage.in=" + UPDATED_CURRENT_STAGE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByCurrentStageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where currentStage is not null
        defaultRecruitmentApplicationFiltering("currentStage.specified=true", "currentStage.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where status equals to
        defaultRecruitmentApplicationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where status in
        defaultRecruitmentApplicationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where status is not null
        defaultRecruitmentApplicationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByDecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where decision equals to
        defaultRecruitmentApplicationFiltering("decision.equals=" + DEFAULT_DECISION, "decision.equals=" + UPDATED_DECISION);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByDecisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where decision in
        defaultRecruitmentApplicationFiltering(
            "decision.in=" + DEFAULT_DECISION + "," + UPDATED_DECISION,
            "decision.in=" + UPDATED_DECISION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByDecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where decision is not null
        defaultRecruitmentApplicationFiltering("decision.specified=true", "decision.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceType equals to
        defaultRecruitmentApplicationFiltering("sourceType.equals=" + DEFAULT_SOURCE_TYPE, "sourceType.equals=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceType in
        defaultRecruitmentApplicationFiltering(
            "sourceType.in=" + DEFAULT_SOURCE_TYPE + "," + UPDATED_SOURCE_TYPE,
            "sourceType.in=" + UPDATED_SOURCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceType is not null
        defaultRecruitmentApplicationFiltering("sourceType.specified=true", "sourceType.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceType contains
        defaultRecruitmentApplicationFiltering("sourceType.contains=" + DEFAULT_SOURCE_TYPE, "sourceType.contains=" + UPDATED_SOURCE_TYPE);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceType does not contain
        defaultRecruitmentApplicationFiltering(
            "sourceType.doesNotContain=" + UPDATED_SOURCE_TYPE,
            "sourceType.doesNotContain=" + DEFAULT_SOURCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceDetail equals to
        defaultRecruitmentApplicationFiltering(
            "sourceDetail.equals=" + DEFAULT_SOURCE_DETAIL,
            "sourceDetail.equals=" + UPDATED_SOURCE_DETAIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceDetailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceDetail in
        defaultRecruitmentApplicationFiltering(
            "sourceDetail.in=" + DEFAULT_SOURCE_DETAIL + "," + UPDATED_SOURCE_DETAIL,
            "sourceDetail.in=" + UPDATED_SOURCE_DETAIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceDetail is not null
        defaultRecruitmentApplicationFiltering("sourceDetail.specified=true", "sourceDetail.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceDetailContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceDetail contains
        defaultRecruitmentApplicationFiltering(
            "sourceDetail.contains=" + DEFAULT_SOURCE_DETAIL,
            "sourceDetail.contains=" + UPDATED_SOURCE_DETAIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsBySourceDetailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where sourceDetail does not contain
        defaultRecruitmentApplicationFiltering(
            "sourceDetail.doesNotContain=" + UPDATED_SOURCE_DETAIL,
            "sourceDetail.doesNotContain=" + DEFAULT_SOURCE_DETAIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByBatchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where batchName equals to
        defaultRecruitmentApplicationFiltering("batchName.equals=" + DEFAULT_BATCH_NAME, "batchName.equals=" + UPDATED_BATCH_NAME);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByBatchNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where batchName in
        defaultRecruitmentApplicationFiltering(
            "batchName.in=" + DEFAULT_BATCH_NAME + "," + UPDATED_BATCH_NAME,
            "batchName.in=" + UPDATED_BATCH_NAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByBatchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where batchName is not null
        defaultRecruitmentApplicationFiltering("batchName.specified=true", "batchName.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByBatchNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where batchName contains
        defaultRecruitmentApplicationFiltering("batchName.contains=" + DEFAULT_BATCH_NAME, "batchName.contains=" + UPDATED_BATCH_NAME);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByBatchNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where batchName does not contain
        defaultRecruitmentApplicationFiltering(
            "batchName.doesNotContain=" + UPDATED_BATCH_NAME,
            "batchName.doesNotContain=" + DEFAULT_BATCH_NAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByAppliedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where appliedAt equals to
        defaultRecruitmentApplicationFiltering("appliedAt.equals=" + DEFAULT_APPLIED_AT, "appliedAt.equals=" + UPDATED_APPLIED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByAppliedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where appliedAt in
        defaultRecruitmentApplicationFiltering(
            "appliedAt.in=" + DEFAULT_APPLIED_AT + "," + UPDATED_APPLIED_AT,
            "appliedAt.in=" + UPDATED_APPLIED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByAppliedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where appliedAt is not null
        defaultRecruitmentApplicationFiltering("appliedAt.specified=true", "appliedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByClosedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where closedAt equals to
        defaultRecruitmentApplicationFiltering("closedAt.equals=" + DEFAULT_CLOSED_AT, "closedAt.equals=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByClosedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where closedAt in
        defaultRecruitmentApplicationFiltering(
            "closedAt.in=" + DEFAULT_CLOSED_AT + "," + UPDATED_CLOSED_AT,
            "closedAt.in=" + UPDATED_CLOSED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByClosedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where closedAt is not null
        defaultRecruitmentApplicationFiltering("closedAt.specified=true", "closedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil equals to
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.equals=" + DEFAULT_RETENTION_UNTIL,
            "retentionUntil.equals=" + UPDATED_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil in
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.in=" + DEFAULT_RETENTION_UNTIL + "," + UPDATED_RETENTION_UNTIL,
            "retentionUntil.in=" + UPDATED_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil is not null
        defaultRecruitmentApplicationFiltering("retentionUntil.specified=true", "retentionUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil is greater than or equal to
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.greaterThanOrEqual=" + DEFAULT_RETENTION_UNTIL,
            "retentionUntil.greaterThanOrEqual=" + UPDATED_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil is less than or equal to
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.lessThanOrEqual=" + DEFAULT_RETENTION_UNTIL,
            "retentionUntil.lessThanOrEqual=" + SMALLER_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil is less than
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.lessThan=" + UPDATED_RETENTION_UNTIL,
            "retentionUntil.lessThan=" + DEFAULT_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRetentionUntilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where retentionUntil is greater than
        defaultRecruitmentApplicationFiltering(
            "retentionUntil.greaterThan=" + SMALLER_RETENTION_UNTIL,
            "retentionUntil.greaterThan=" + DEFAULT_RETENTION_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId equals to
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.equals=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.equals=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId in
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.in=" + DEFAULT_CONVERTED_PERSON_ID + "," + UPDATED_CONVERTED_PERSON_ID,
            "convertedPersonId.in=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId is not null
        defaultRecruitmentApplicationFiltering("convertedPersonId.specified=true", "convertedPersonId.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId is greater than or equal to
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.greaterThanOrEqual=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.greaterThanOrEqual=" + UPDATED_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId is less than or equal to
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.lessThanOrEqual=" + DEFAULT_CONVERTED_PERSON_ID,
            "convertedPersonId.lessThanOrEqual=" + SMALLER_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId is less than
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.lessThan=" + UPDATED_CONVERTED_PERSON_ID,
            "convertedPersonId.lessThan=" + DEFAULT_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedPersonIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedPersonId is greater than
        defaultRecruitmentApplicationFiltering(
            "convertedPersonId.greaterThan=" + SMALLER_CONVERTED_PERSON_ID,
            "convertedPersonId.greaterThan=" + DEFAULT_CONVERTED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedAt equals to
        defaultRecruitmentApplicationFiltering("convertedAt.equals=" + DEFAULT_CONVERTED_AT, "convertedAt.equals=" + UPDATED_CONVERTED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedAt in
        defaultRecruitmentApplicationFiltering(
            "convertedAt.in=" + DEFAULT_CONVERTED_AT + "," + UPDATED_CONVERTED_AT,
            "convertedAt.in=" + UPDATED_CONVERTED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByConvertedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        // Get all the recruitmentApplicationList where convertedAt is not null
        defaultRecruitmentApplicationFiltering("convertedAt.specified=true", "convertedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByCandidateIsEqualToSomething() throws Exception {
        ExternalCandidate candidate;
        if (TestUtil.findAll(em, ExternalCandidate.class).isEmpty()) {
            recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
            candidate = ExternalCandidateResourceIT.createEntity();
        } else {
            candidate = TestUtil.findAll(em, ExternalCandidate.class).get(0);
        }
        em.persist(candidate);
        em.flush();
        recruitmentApplication.setCandidate(candidate);
        recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
        Long candidateId = candidate.getId();
        // Get all the recruitmentApplicationList where candidate equals to candidateId
        defaultRecruitmentApplicationShouldBeFound("candidateId.equals=" + candidateId);

        // Get all the recruitmentApplicationList where candidate equals to (candidateId + 1)
        defaultRecruitmentApplicationShouldNotBeFound("candidateId.equals=" + (candidateId + 1));
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRequisitionIsEqualToSomething() throws Exception {
        RecruitmentRequisition requisition;
        if (TestUtil.findAll(em, RecruitmentRequisition.class).isEmpty()) {
            recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
            requisition = RecruitmentRequisitionResourceIT.createEntity();
        } else {
            requisition = TestUtil.findAll(em, RecruitmentRequisition.class).get(0);
        }
        em.persist(requisition);
        em.flush();
        recruitmentApplication.setRequisition(requisition);
        recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
        Long requisitionId = requisition.getId();
        // Get all the recruitmentApplicationList where requisition equals to requisitionId
        defaultRecruitmentApplicationShouldBeFound("requisitionId.equals=" + requisitionId);

        // Get all the recruitmentApplicationList where requisition equals to (requisitionId + 1)
        defaultRecruitmentApplicationShouldNotBeFound("requisitionId.equals=" + (requisitionId + 1));
    }

    @Test
    @Transactional
    void getAllRecruitmentApplicationsByRequisitionVersionIsEqualToSomething() throws Exception {
        RecruitmentRequisitionVersion requisitionVersion;
        if (TestUtil.findAll(em, RecruitmentRequisitionVersion.class).isEmpty()) {
            recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
            requisitionVersion = RecruitmentRequisitionVersionResourceIT.createEntity();
        } else {
            requisitionVersion = TestUtil.findAll(em, RecruitmentRequisitionVersion.class).get(0);
        }
        em.persist(requisitionVersion);
        em.flush();
        recruitmentApplication.setRequisitionVersion(requisitionVersion);
        recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);
        Long requisitionVersionId = requisitionVersion.getId();
        // Get all the recruitmentApplicationList where requisitionVersion equals to requisitionVersionId
        defaultRecruitmentApplicationShouldBeFound("requisitionVersionId.equals=" + requisitionVersionId);

        // Get all the recruitmentApplicationList where requisitionVersion equals to (requisitionVersionId + 1)
        defaultRecruitmentApplicationShouldNotBeFound("requisitionVersionId.equals=" + (requisitionVersionId + 1));
    }

    private void defaultRecruitmentApplicationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecruitmentApplicationShouldBeFound(shouldBeFound);
        defaultRecruitmentApplicationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecruitmentApplicationShouldBeFound(String filter) throws Exception {
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationCode").value(hasItem(DEFAULT_APPLICATION_CODE)))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currentStage").value(hasItem(DEFAULT_CURRENT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].sourceDetail").value(hasItem(DEFAULT_SOURCE_DETAIL)))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].retentionUntil").value(hasItem(DEFAULT_RETENTION_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].convertedPersonId").value(hasItem(DEFAULT_CONVERTED_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].convertedAt").value(hasItem(DEFAULT_CONVERTED_AT.toString())))
            .andExpect(jsonPath("$.[*].closeReason").value(hasItem(DEFAULT_CLOSE_REASON)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecruitmentApplicationShouldNotBeFound(String filter) throws Exception {
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecruitmentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentApplication() throws Exception {
        // Get the recruitmentApplication
        restRecruitmentApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentApplication() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentApplication
        RecruitmentApplication updatedRecruitmentApplication = recruitmentApplicationRepository
            .findById(recruitmentApplication.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecruitmentApplication are not directly saved in db
        em.detach(updatedRecruitmentApplication);
        updatedRecruitmentApplication
            .applicationCode(UPDATED_APPLICATION_CODE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .currentStage(UPDATED_CURRENT_STAGE)
            .status(UPDATED_STATUS)
            .decision(UPDATED_DECISION)
            .sourceType(UPDATED_SOURCE_TYPE)
            .sourceDetail(UPDATED_SOURCE_DETAIL)
            .batchName(UPDATED_BATCH_NAME)
            .appliedAt(UPDATED_APPLIED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .retentionUntil(UPDATED_RETENTION_UNTIL)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .convertedAt(UPDATED_CONVERTED_AT)
            .closeReason(UPDATED_CLOSE_REASON)
            .note(UPDATED_NOTE);
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(updatedRecruitmentApplication);

        restRecruitmentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruitmentApplicationToMatchAllProperties(updatedRecruitmentApplication);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentApplicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentApplication using partial update
        RecruitmentApplication partialUpdatedRecruitmentApplication = new RecruitmentApplication();
        partialUpdatedRecruitmentApplication.setId(recruitmentApplication.getId());

        partialUpdatedRecruitmentApplication
            .sourceDetail(UPDATED_SOURCE_DETAIL)
            .appliedAt(UPDATED_APPLIED_AT)
            .retentionUntil(UPDATED_RETENTION_UNTIL)
            .note(UPDATED_NOTE);

        restRecruitmentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentApplication))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruitmentApplication, recruitmentApplication),
            getPersistedRecruitmentApplication(recruitmentApplication)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentApplication using partial update
        RecruitmentApplication partialUpdatedRecruitmentApplication = new RecruitmentApplication();
        partialUpdatedRecruitmentApplication.setId(recruitmentApplication.getId());

        partialUpdatedRecruitmentApplication
            .applicationCode(UPDATED_APPLICATION_CODE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .currentStage(UPDATED_CURRENT_STAGE)
            .status(UPDATED_STATUS)
            .decision(UPDATED_DECISION)
            .sourceType(UPDATED_SOURCE_TYPE)
            .sourceDetail(UPDATED_SOURCE_DETAIL)
            .batchName(UPDATED_BATCH_NAME)
            .appliedAt(UPDATED_APPLIED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .retentionUntil(UPDATED_RETENTION_UNTIL)
            .convertedPersonId(UPDATED_CONVERTED_PERSON_ID)
            .convertedAt(UPDATED_CONVERTED_AT)
            .closeReason(UPDATED_CLOSE_REASON)
            .note(UPDATED_NOTE);

        restRecruitmentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentApplication))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentApplicationUpdatableFieldsEquals(
            partialUpdatedRecruitmentApplication,
            getPersistedRecruitmentApplication(partialUpdatedRecruitmentApplication)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentApplication.setId(longCount.incrementAndGet());

        // Create the RecruitmentApplication
        RecruitmentApplicationDTO recruitmentApplicationDTO = recruitmentApplicationMapper.toDto(recruitmentApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recruitmentApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentApplication() throws Exception {
        // Initialize the database
        insertedRecruitmentApplication = recruitmentApplicationRepository.saveAndFlush(recruitmentApplication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruitmentApplication
        restRecruitmentApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruitmentApplicationRepository.count();
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

    protected RecruitmentApplication getPersistedRecruitmentApplication(RecruitmentApplication recruitmentApplication) {
        return recruitmentApplicationRepository.findById(recruitmentApplication.getId()).orElseThrow();
    }

    protected void assertPersistedRecruitmentApplicationToMatchAllProperties(RecruitmentApplication expectedRecruitmentApplication) {
        assertRecruitmentApplicationAllPropertiesEquals(
            expectedRecruitmentApplication,
            getPersistedRecruitmentApplication(expectedRecruitmentApplication)
        );
    }

    protected void assertPersistedRecruitmentApplicationToMatchUpdatableProperties(RecruitmentApplication expectedRecruitmentApplication) {
        assertRecruitmentApplicationAllUpdatablePropertiesEquals(
            expectedRecruitmentApplication,
            getPersistedRecruitmentApplication(expectedRecruitmentApplication)
        );
    }
}
