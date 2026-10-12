package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.RecruitmentEvaluationAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static top.btmdc.hr.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentEvaluation;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.repository.RecruitmentEvaluationRepository;
import top.btmdc.hr.service.dto.RecruitmentEvaluationDTO;
import top.btmdc.hr.service.mapper.RecruitmentEvaluationMapper;

/**
 * Integration tests for the {@link RecruitmentEvaluationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class RecruitmentEvaluationResourceIT {

    private static final ApplicationStageType DEFAULT_STAGE_TYPE = ApplicationStageType.RESUME_SCREENING;
    private static final ApplicationStageType UPDATED_STAGE_TYPE = ApplicationStageType.WRITTEN_TEST;

    private static final Long DEFAULT_ACTIVITY_ID = 1L;
    private static final Long UPDATED_ACTIVITY_ID = 2L;
    private static final Long SMALLER_ACTIVITY_ID = 1L - 1L;

    private static final Long DEFAULT_TEMPLATE_VERSION_ID = 1L;
    private static final Long UPDATED_TEMPLATE_VERSION_ID = 2L;
    private static final Long SMALLER_TEMPLATE_VERSION_ID = 1L - 1L;

    private static final String DEFAULT_EVALUATOR_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_EVALUATOR_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECORDED_BY_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_RECORDED_BY_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_OVERALL_RECOMMENDATION = "AAAAAAAAAA";
    private static final String UPDATED_OVERALL_RECOMMENDATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_SCORE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_SCORE = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_SCORE = new BigDecimal(0 - 1);

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWERS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_ANSWERS_JSON = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVALUATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVALUATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SUBMITTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PUBLISHED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_CORRECTION_OF_ID = 1L;
    private static final Long UPDATED_CORRECTION_OF_ID = 2L;
    private static final Long SMALLER_CORRECTION_OF_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/recruitment-evaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruitmentEvaluationRepository recruitmentEvaluationRepository;

    @Autowired
    private RecruitmentEvaluationMapper recruitmentEvaluationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentEvaluationMockMvc;

    private RecruitmentEvaluation recruitmentEvaluation;

    private RecruitmentEvaluation insertedRecruitmentEvaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentEvaluation createEntity() {
        return new RecruitmentEvaluation()
            .stageType(DEFAULT_STAGE_TYPE)
            .activityId(DEFAULT_ACTIVITY_ID)
            .templateVersionId(DEFAULT_TEMPLATE_VERSION_ID)
            .evaluatorUsername(DEFAULT_EVALUATOR_USERNAME)
            .recordedByUsername(DEFAULT_RECORDED_BY_USERNAME)
            .status(DEFAULT_STATUS)
            .overallRecommendation(DEFAULT_OVERALL_RECOMMENDATION)
            .totalScore(DEFAULT_TOTAL_SCORE)
            .summary(DEFAULT_SUMMARY)
            .answersJson(DEFAULT_ANSWERS_JSON)
            .evaluatedAt(DEFAULT_EVALUATED_AT)
            .submittedAt(DEFAULT_SUBMITTED_AT)
            .publishedAt(DEFAULT_PUBLISHED_AT)
            .correctionOfId(DEFAULT_CORRECTION_OF_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentEvaluation createUpdatedEntity() {
        return new RecruitmentEvaluation()
            .stageType(UPDATED_STAGE_TYPE)
            .activityId(UPDATED_ACTIVITY_ID)
            .templateVersionId(UPDATED_TEMPLATE_VERSION_ID)
            .evaluatorUsername(UPDATED_EVALUATOR_USERNAME)
            .recordedByUsername(UPDATED_RECORDED_BY_USERNAME)
            .status(UPDATED_STATUS)
            .overallRecommendation(UPDATED_OVERALL_RECOMMENDATION)
            .totalScore(UPDATED_TOTAL_SCORE)
            .summary(UPDATED_SUMMARY)
            .answersJson(UPDATED_ANSWERS_JSON)
            .evaluatedAt(UPDATED_EVALUATED_AT)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .correctionOfId(UPDATED_CORRECTION_OF_ID);
    }

    @BeforeEach
    void initTest() {
        recruitmentEvaluation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecruitmentEvaluation != null) {
            recruitmentEvaluationRepository.delete(insertedRecruitmentEvaluation);
            insertedRecruitmentEvaluation = null;
        }
    }

    @Test
    @Transactional
    void createRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);
        var returnedRecruitmentEvaluationDTO = om.readValue(
            restRecruitmentEvaluationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecruitmentEvaluationDTO.class
        );

        // Validate the RecruitmentEvaluation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecruitmentEvaluation = recruitmentEvaluationMapper.toEntity(returnedRecruitmentEvaluationDTO);
        assertRecruitmentEvaluationUpdatableFieldsEquals(
            returnedRecruitmentEvaluation,
            getPersistedRecruitmentEvaluation(returnedRecruitmentEvaluation)
        );

        insertedRecruitmentEvaluation = returnedRecruitmentEvaluation;
    }

    @Test
    @Transactional
    void createRecruitmentEvaluationWithExistingId() throws Exception {
        // Create the RecruitmentEvaluation with an existing ID
        recruitmentEvaluation.setId(1L);
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStageTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentEvaluation.setStageType(null);

        // Create the RecruitmentEvaluation, which fails.
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        restRecruitmentEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEvaluatorUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentEvaluation.setEvaluatorUsername(null);

        // Create the RecruitmentEvaluation, which fails.
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        restRecruitmentEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordedByUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentEvaluation.setRecordedByUsername(null);

        // Create the RecruitmentEvaluation, which fails.
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        restRecruitmentEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentEvaluation.setStatus(null);

        // Create the RecruitmentEvaluation, which fails.
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        restRecruitmentEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluations() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityId").value(hasItem(DEFAULT_ACTIVITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].templateVersionId").value(hasItem(DEFAULT_TEMPLATE_VERSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].evaluatorUsername").value(hasItem(DEFAULT_EVALUATOR_USERNAME)))
            .andExpect(jsonPath("$.[*].recordedByUsername").value(hasItem(DEFAULT_RECORDED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].overallRecommendation").value(hasItem(DEFAULT_OVERALL_RECOMMENDATION)))
            .andExpect(jsonPath("$.[*].totalScore").value(hasItem(sameNumber(DEFAULT_TOTAL_SCORE))))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].answersJson").value(hasItem(DEFAULT_ANSWERS_JSON)))
            .andExpect(jsonPath("$.[*].evaluatedAt").value(hasItem(DEFAULT_EVALUATED_AT.toString())))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].publishedAt").value(hasItem(DEFAULT_PUBLISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].correctionOfId").value(hasItem(DEFAULT_CORRECTION_OF_ID.intValue())));
    }

    @Test
    @Transactional
    void getRecruitmentEvaluation() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get the recruitmentEvaluation
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentEvaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentEvaluation.getId().intValue()))
            .andExpect(jsonPath("$.stageType").value(DEFAULT_STAGE_TYPE.toString()))
            .andExpect(jsonPath("$.activityId").value(DEFAULT_ACTIVITY_ID.intValue()))
            .andExpect(jsonPath("$.templateVersionId").value(DEFAULT_TEMPLATE_VERSION_ID.intValue()))
            .andExpect(jsonPath("$.evaluatorUsername").value(DEFAULT_EVALUATOR_USERNAME))
            .andExpect(jsonPath("$.recordedByUsername").value(DEFAULT_RECORDED_BY_USERNAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.overallRecommendation").value(DEFAULT_OVERALL_RECOMMENDATION))
            .andExpect(jsonPath("$.totalScore").value(sameNumber(DEFAULT_TOTAL_SCORE)))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.answersJson").value(DEFAULT_ANSWERS_JSON))
            .andExpect(jsonPath("$.evaluatedAt").value(DEFAULT_EVALUATED_AT.toString()))
            .andExpect(jsonPath("$.submittedAt").value(DEFAULT_SUBMITTED_AT.toString()))
            .andExpect(jsonPath("$.publishedAt").value(DEFAULT_PUBLISHED_AT.toString()))
            .andExpect(jsonPath("$.correctionOfId").value(DEFAULT_CORRECTION_OF_ID.intValue()));
    }

    @Test
    @Transactional
    void getRecruitmentEvaluationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        Long id = recruitmentEvaluation.getId();

        defaultRecruitmentEvaluationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecruitmentEvaluationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecruitmentEvaluationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where stageType equals to
        defaultRecruitmentEvaluationFiltering("stageType.equals=" + DEFAULT_STAGE_TYPE, "stageType.equals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where stageType in
        defaultRecruitmentEvaluationFiltering(
            "stageType.in=" + DEFAULT_STAGE_TYPE + "," + UPDATED_STAGE_TYPE,
            "stageType.in=" + UPDATED_STAGE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where stageType is not null
        defaultRecruitmentEvaluationFiltering("stageType.specified=true", "stageType.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId equals to
        defaultRecruitmentEvaluationFiltering("activityId.equals=" + DEFAULT_ACTIVITY_ID, "activityId.equals=" + UPDATED_ACTIVITY_ID);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId in
        defaultRecruitmentEvaluationFiltering(
            "activityId.in=" + DEFAULT_ACTIVITY_ID + "," + UPDATED_ACTIVITY_ID,
            "activityId.in=" + UPDATED_ACTIVITY_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId is not null
        defaultRecruitmentEvaluationFiltering("activityId.specified=true", "activityId.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId is greater than or equal to
        defaultRecruitmentEvaluationFiltering(
            "activityId.greaterThanOrEqual=" + DEFAULT_ACTIVITY_ID,
            "activityId.greaterThanOrEqual=" + UPDATED_ACTIVITY_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId is less than or equal to
        defaultRecruitmentEvaluationFiltering(
            "activityId.lessThanOrEqual=" + DEFAULT_ACTIVITY_ID,
            "activityId.lessThanOrEqual=" + SMALLER_ACTIVITY_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId is less than
        defaultRecruitmentEvaluationFiltering("activityId.lessThan=" + UPDATED_ACTIVITY_ID, "activityId.lessThan=" + DEFAULT_ACTIVITY_ID);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByActivityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where activityId is greater than
        defaultRecruitmentEvaluationFiltering(
            "activityId.greaterThan=" + SMALLER_ACTIVITY_ID,
            "activityId.greaterThan=" + DEFAULT_ACTIVITY_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId equals to
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.equals=" + DEFAULT_TEMPLATE_VERSION_ID,
            "templateVersionId.equals=" + UPDATED_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId in
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.in=" + DEFAULT_TEMPLATE_VERSION_ID + "," + UPDATED_TEMPLATE_VERSION_ID,
            "templateVersionId.in=" + UPDATED_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId is not null
        defaultRecruitmentEvaluationFiltering("templateVersionId.specified=true", "templateVersionId.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId is greater than or equal to
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.greaterThanOrEqual=" + DEFAULT_TEMPLATE_VERSION_ID,
            "templateVersionId.greaterThanOrEqual=" + UPDATED_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId is less than or equal to
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.lessThanOrEqual=" + DEFAULT_TEMPLATE_VERSION_ID,
            "templateVersionId.lessThanOrEqual=" + SMALLER_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId is less than
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.lessThan=" + UPDATED_TEMPLATE_VERSION_ID,
            "templateVersionId.lessThan=" + DEFAULT_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTemplateVersionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where templateVersionId is greater than
        defaultRecruitmentEvaluationFiltering(
            "templateVersionId.greaterThan=" + SMALLER_TEMPLATE_VERSION_ID,
            "templateVersionId.greaterThan=" + DEFAULT_TEMPLATE_VERSION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatorUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatorUsername equals to
        defaultRecruitmentEvaluationFiltering(
            "evaluatorUsername.equals=" + DEFAULT_EVALUATOR_USERNAME,
            "evaluatorUsername.equals=" + UPDATED_EVALUATOR_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatorUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatorUsername in
        defaultRecruitmentEvaluationFiltering(
            "evaluatorUsername.in=" + DEFAULT_EVALUATOR_USERNAME + "," + UPDATED_EVALUATOR_USERNAME,
            "evaluatorUsername.in=" + UPDATED_EVALUATOR_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatorUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatorUsername is not null
        defaultRecruitmentEvaluationFiltering("evaluatorUsername.specified=true", "evaluatorUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatorUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatorUsername contains
        defaultRecruitmentEvaluationFiltering(
            "evaluatorUsername.contains=" + DEFAULT_EVALUATOR_USERNAME,
            "evaluatorUsername.contains=" + UPDATED_EVALUATOR_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatorUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatorUsername does not contain
        defaultRecruitmentEvaluationFiltering(
            "evaluatorUsername.doesNotContain=" + UPDATED_EVALUATOR_USERNAME,
            "evaluatorUsername.doesNotContain=" + DEFAULT_EVALUATOR_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByRecordedByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where recordedByUsername equals to
        defaultRecruitmentEvaluationFiltering(
            "recordedByUsername.equals=" + DEFAULT_RECORDED_BY_USERNAME,
            "recordedByUsername.equals=" + UPDATED_RECORDED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByRecordedByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where recordedByUsername in
        defaultRecruitmentEvaluationFiltering(
            "recordedByUsername.in=" + DEFAULT_RECORDED_BY_USERNAME + "," + UPDATED_RECORDED_BY_USERNAME,
            "recordedByUsername.in=" + UPDATED_RECORDED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByRecordedByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where recordedByUsername is not null
        defaultRecruitmentEvaluationFiltering("recordedByUsername.specified=true", "recordedByUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByRecordedByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where recordedByUsername contains
        defaultRecruitmentEvaluationFiltering(
            "recordedByUsername.contains=" + DEFAULT_RECORDED_BY_USERNAME,
            "recordedByUsername.contains=" + UPDATED_RECORDED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByRecordedByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where recordedByUsername does not contain
        defaultRecruitmentEvaluationFiltering(
            "recordedByUsername.doesNotContain=" + UPDATED_RECORDED_BY_USERNAME,
            "recordedByUsername.doesNotContain=" + DEFAULT_RECORDED_BY_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where status equals to
        defaultRecruitmentEvaluationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where status in
        defaultRecruitmentEvaluationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where status is not null
        defaultRecruitmentEvaluationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where status contains
        defaultRecruitmentEvaluationFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where status does not contain
        defaultRecruitmentEvaluationFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByOverallRecommendationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where overallRecommendation equals to
        defaultRecruitmentEvaluationFiltering(
            "overallRecommendation.equals=" + DEFAULT_OVERALL_RECOMMENDATION,
            "overallRecommendation.equals=" + UPDATED_OVERALL_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByOverallRecommendationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where overallRecommendation in
        defaultRecruitmentEvaluationFiltering(
            "overallRecommendation.in=" + DEFAULT_OVERALL_RECOMMENDATION + "," + UPDATED_OVERALL_RECOMMENDATION,
            "overallRecommendation.in=" + UPDATED_OVERALL_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByOverallRecommendationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where overallRecommendation is not null
        defaultRecruitmentEvaluationFiltering("overallRecommendation.specified=true", "overallRecommendation.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByOverallRecommendationContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where overallRecommendation contains
        defaultRecruitmentEvaluationFiltering(
            "overallRecommendation.contains=" + DEFAULT_OVERALL_RECOMMENDATION,
            "overallRecommendation.contains=" + UPDATED_OVERALL_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByOverallRecommendationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where overallRecommendation does not contain
        defaultRecruitmentEvaluationFiltering(
            "overallRecommendation.doesNotContain=" + UPDATED_OVERALL_RECOMMENDATION,
            "overallRecommendation.doesNotContain=" + DEFAULT_OVERALL_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore equals to
        defaultRecruitmentEvaluationFiltering("totalScore.equals=" + DEFAULT_TOTAL_SCORE, "totalScore.equals=" + UPDATED_TOTAL_SCORE);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore in
        defaultRecruitmentEvaluationFiltering(
            "totalScore.in=" + DEFAULT_TOTAL_SCORE + "," + UPDATED_TOTAL_SCORE,
            "totalScore.in=" + UPDATED_TOTAL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore is not null
        defaultRecruitmentEvaluationFiltering("totalScore.specified=true", "totalScore.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore is greater than or equal to
        defaultRecruitmentEvaluationFiltering(
            "totalScore.greaterThanOrEqual=" + DEFAULT_TOTAL_SCORE,
            "totalScore.greaterThanOrEqual=" + UPDATED_TOTAL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore is less than or equal to
        defaultRecruitmentEvaluationFiltering(
            "totalScore.lessThanOrEqual=" + DEFAULT_TOTAL_SCORE,
            "totalScore.lessThanOrEqual=" + SMALLER_TOTAL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore is less than
        defaultRecruitmentEvaluationFiltering("totalScore.lessThan=" + UPDATED_TOTAL_SCORE, "totalScore.lessThan=" + DEFAULT_TOTAL_SCORE);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByTotalScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where totalScore is greater than
        defaultRecruitmentEvaluationFiltering(
            "totalScore.greaterThan=" + SMALLER_TOTAL_SCORE,
            "totalScore.greaterThan=" + DEFAULT_TOTAL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatedAt equals to
        defaultRecruitmentEvaluationFiltering("evaluatedAt.equals=" + DEFAULT_EVALUATED_AT, "evaluatedAt.equals=" + UPDATED_EVALUATED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatedAt in
        defaultRecruitmentEvaluationFiltering(
            "evaluatedAt.in=" + DEFAULT_EVALUATED_AT + "," + UPDATED_EVALUATED_AT,
            "evaluatedAt.in=" + UPDATED_EVALUATED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByEvaluatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where evaluatedAt is not null
        defaultRecruitmentEvaluationFiltering("evaluatedAt.specified=true", "evaluatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsBySubmittedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where submittedAt equals to
        defaultRecruitmentEvaluationFiltering("submittedAt.equals=" + DEFAULT_SUBMITTED_AT, "submittedAt.equals=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsBySubmittedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where submittedAt in
        defaultRecruitmentEvaluationFiltering(
            "submittedAt.in=" + DEFAULT_SUBMITTED_AT + "," + UPDATED_SUBMITTED_AT,
            "submittedAt.in=" + UPDATED_SUBMITTED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsBySubmittedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where submittedAt is not null
        defaultRecruitmentEvaluationFiltering("submittedAt.specified=true", "submittedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByPublishedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where publishedAt equals to
        defaultRecruitmentEvaluationFiltering("publishedAt.equals=" + DEFAULT_PUBLISHED_AT, "publishedAt.equals=" + UPDATED_PUBLISHED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByPublishedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where publishedAt in
        defaultRecruitmentEvaluationFiltering(
            "publishedAt.in=" + DEFAULT_PUBLISHED_AT + "," + UPDATED_PUBLISHED_AT,
            "publishedAt.in=" + UPDATED_PUBLISHED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByPublishedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where publishedAt is not null
        defaultRecruitmentEvaluationFiltering("publishedAt.specified=true", "publishedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId equals to
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.equals=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.equals=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId in
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.in=" + DEFAULT_CORRECTION_OF_ID + "," + UPDATED_CORRECTION_OF_ID,
            "correctionOfId.in=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId is not null
        defaultRecruitmentEvaluationFiltering("correctionOfId.specified=true", "correctionOfId.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId is greater than or equal to
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.greaterThanOrEqual=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.greaterThanOrEqual=" + UPDATED_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId is less than or equal to
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.lessThanOrEqual=" + DEFAULT_CORRECTION_OF_ID,
            "correctionOfId.lessThanOrEqual=" + SMALLER_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId is less than
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.lessThan=" + UPDATED_CORRECTION_OF_ID,
            "correctionOfId.lessThan=" + DEFAULT_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByCorrectionOfIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        // Get all the recruitmentEvaluationList where correctionOfId is greater than
        defaultRecruitmentEvaluationFiltering(
            "correctionOfId.greaterThan=" + SMALLER_CORRECTION_OF_ID,
            "correctionOfId.greaterThan=" + DEFAULT_CORRECTION_OF_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentEvaluationsByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        recruitmentEvaluation.setApplication(application);
        recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);
        Long applicationId = application.getId();
        // Get all the recruitmentEvaluationList where application equals to applicationId
        defaultRecruitmentEvaluationShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the recruitmentEvaluationList where application equals to (applicationId + 1)
        defaultRecruitmentEvaluationShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultRecruitmentEvaluationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecruitmentEvaluationShouldBeFound(shouldBeFound);
        defaultRecruitmentEvaluationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecruitmentEvaluationShouldBeFound(String filter) throws Exception {
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityId").value(hasItem(DEFAULT_ACTIVITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].templateVersionId").value(hasItem(DEFAULT_TEMPLATE_VERSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].evaluatorUsername").value(hasItem(DEFAULT_EVALUATOR_USERNAME)))
            .andExpect(jsonPath("$.[*].recordedByUsername").value(hasItem(DEFAULT_RECORDED_BY_USERNAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].overallRecommendation").value(hasItem(DEFAULT_OVERALL_RECOMMENDATION)))
            .andExpect(jsonPath("$.[*].totalScore").value(hasItem(sameNumber(DEFAULT_TOTAL_SCORE))))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].answersJson").value(hasItem(DEFAULT_ANSWERS_JSON)))
            .andExpect(jsonPath("$.[*].evaluatedAt").value(hasItem(DEFAULT_EVALUATED_AT.toString())))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].publishedAt").value(hasItem(DEFAULT_PUBLISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].correctionOfId").value(hasItem(DEFAULT_CORRECTION_OF_ID.intValue())));

        // Check, that the count call also returns 1
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecruitmentEvaluationShouldNotBeFound(String filter) throws Exception {
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecruitmentEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentEvaluation() throws Exception {
        // Get the recruitmentEvaluation
        restRecruitmentEvaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentEvaluation() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentEvaluation
        RecruitmentEvaluation updatedRecruitmentEvaluation = recruitmentEvaluationRepository
            .findById(recruitmentEvaluation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecruitmentEvaluation are not directly saved in db
        em.detach(updatedRecruitmentEvaluation);
        updatedRecruitmentEvaluation
            .stageType(UPDATED_STAGE_TYPE)
            .activityId(UPDATED_ACTIVITY_ID)
            .templateVersionId(UPDATED_TEMPLATE_VERSION_ID)
            .evaluatorUsername(UPDATED_EVALUATOR_USERNAME)
            .recordedByUsername(UPDATED_RECORDED_BY_USERNAME)
            .status(UPDATED_STATUS)
            .overallRecommendation(UPDATED_OVERALL_RECOMMENDATION)
            .totalScore(UPDATED_TOTAL_SCORE)
            .summary(UPDATED_SUMMARY)
            .answersJson(UPDATED_ANSWERS_JSON)
            .evaluatedAt(UPDATED_EVALUATED_AT)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .correctionOfId(UPDATED_CORRECTION_OF_ID);
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(updatedRecruitmentEvaluation);

        restRecruitmentEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentEvaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruitmentEvaluationToMatchAllProperties(updatedRecruitmentEvaluation);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentEvaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentEvaluationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentEvaluation using partial update
        RecruitmentEvaluation partialUpdatedRecruitmentEvaluation = new RecruitmentEvaluation();
        partialUpdatedRecruitmentEvaluation.setId(recruitmentEvaluation.getId());

        partialUpdatedRecruitmentEvaluation
            .activityId(UPDATED_ACTIVITY_ID)
            .templateVersionId(UPDATED_TEMPLATE_VERSION_ID)
            .recordedByUsername(UPDATED_RECORDED_BY_USERNAME)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .correctionOfId(UPDATED_CORRECTION_OF_ID);

        restRecruitmentEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentEvaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentEvaluationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruitmentEvaluation, recruitmentEvaluation),
            getPersistedRecruitmentEvaluation(recruitmentEvaluation)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentEvaluation using partial update
        RecruitmentEvaluation partialUpdatedRecruitmentEvaluation = new RecruitmentEvaluation();
        partialUpdatedRecruitmentEvaluation.setId(recruitmentEvaluation.getId());

        partialUpdatedRecruitmentEvaluation
            .stageType(UPDATED_STAGE_TYPE)
            .activityId(UPDATED_ACTIVITY_ID)
            .templateVersionId(UPDATED_TEMPLATE_VERSION_ID)
            .evaluatorUsername(UPDATED_EVALUATOR_USERNAME)
            .recordedByUsername(UPDATED_RECORDED_BY_USERNAME)
            .status(UPDATED_STATUS)
            .overallRecommendation(UPDATED_OVERALL_RECOMMENDATION)
            .totalScore(UPDATED_TOTAL_SCORE)
            .summary(UPDATED_SUMMARY)
            .answersJson(UPDATED_ANSWERS_JSON)
            .evaluatedAt(UPDATED_EVALUATED_AT)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .correctionOfId(UPDATED_CORRECTION_OF_ID);

        restRecruitmentEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentEvaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentEvaluationUpdatableFieldsEquals(
            partialUpdatedRecruitmentEvaluation,
            getPersistedRecruitmentEvaluation(partialUpdatedRecruitmentEvaluation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentEvaluationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentEvaluation.setId(longCount.incrementAndGet());

        // Create the RecruitmentEvaluation
        RecruitmentEvaluationDTO recruitmentEvaluationDTO = recruitmentEvaluationMapper.toDto(recruitmentEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recruitmentEvaluationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentEvaluation() throws Exception {
        // Initialize the database
        insertedRecruitmentEvaluation = recruitmentEvaluationRepository.saveAndFlush(recruitmentEvaluation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruitmentEvaluation
        restRecruitmentEvaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentEvaluation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruitmentEvaluationRepository.count();
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

    protected RecruitmentEvaluation getPersistedRecruitmentEvaluation(RecruitmentEvaluation recruitmentEvaluation) {
        return recruitmentEvaluationRepository.findById(recruitmentEvaluation.getId()).orElseThrow();
    }

    protected void assertPersistedRecruitmentEvaluationToMatchAllProperties(RecruitmentEvaluation expectedRecruitmentEvaluation) {
        assertRecruitmentEvaluationAllPropertiesEquals(
            expectedRecruitmentEvaluation,
            getPersistedRecruitmentEvaluation(expectedRecruitmentEvaluation)
        );
    }

    protected void assertPersistedRecruitmentEvaluationToMatchUpdatableProperties(RecruitmentEvaluation expectedRecruitmentEvaluation) {
        assertRecruitmentEvaluationAllUpdatablePropertiesEquals(
            expectedRecruitmentEvaluation,
            getPersistedRecruitmentEvaluation(expectedRecruitmentEvaluation)
        );
    }
}
