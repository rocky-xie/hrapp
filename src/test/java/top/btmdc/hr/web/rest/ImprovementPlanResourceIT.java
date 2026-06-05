package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.ImprovementPlanAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.IntegrationTest;
import top.btmdc.hr.domain.ImprovementPlan;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.enumeration.PlanStatus;
import top.btmdc.hr.repository.ImprovementPlanRepository;
import top.btmdc.hr.service.ImprovementPlanService;
import top.btmdc.hr.service.dto.ImprovementPlanDTO;
import top.btmdc.hr.service.mapper.ImprovementPlanMapper;

/**
 * Integration tests for the {@link ImprovementPlanResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImprovementPlanResourceIT {

    private static final String DEFAULT_PLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_NAME = "BBBBBBBBBB";

    private static final PlanStatus DEFAULT_PLAN_STATUS = PlanStatus.DRAFT;
    private static final PlanStatus UPDATED_PLAN_STATUS = PlanStatus.ACTIVE;

    private static final String DEFAULT_PROBLEM_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_PROBLEM_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_IMPROVEMENT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_IMPROVEMENT_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TARGET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARGET_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TARGET_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMPLETION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REVIEW_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_RESULT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/improvement-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImprovementPlanRepository improvementPlanRepository;

    @Mock
    private ImprovementPlanRepository improvementPlanRepositoryMock;

    @Autowired
    private ImprovementPlanMapper improvementPlanMapper;

    @Mock
    private ImprovementPlanService improvementPlanServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImprovementPlanMockMvc;

    private ImprovementPlan improvementPlan;

    private ImprovementPlan insertedImprovementPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImprovementPlan createEntity() {
        return new ImprovementPlan()
            .planName(DEFAULT_PLAN_NAME)
            .planStatus(DEFAULT_PLAN_STATUS)
            .problemSummary(DEFAULT_PROBLEM_SUMMARY)
            .improvementAction(DEFAULT_IMPROVEMENT_ACTION)
            .ownerName(DEFAULT_OWNER_NAME)
            .startDate(DEFAULT_START_DATE)
            .targetDate(DEFAULT_TARGET_DATE)
            .completionDate(DEFAULT_COMPLETION_DATE)
            .reviewResult(DEFAULT_REVIEW_RESULT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImprovementPlan createUpdatedEntity() {
        return new ImprovementPlan()
            .planName(UPDATED_PLAN_NAME)
            .planStatus(UPDATED_PLAN_STATUS)
            .problemSummary(UPDATED_PROBLEM_SUMMARY)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .ownerName(UPDATED_OWNER_NAME)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .reviewResult(UPDATED_REVIEW_RESULT);
    }

    @BeforeEach
    void initTest() {
        improvementPlan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImprovementPlan != null) {
            improvementPlanRepository.delete(insertedImprovementPlan);
            insertedImprovementPlan = null;
        }
    }

    @Test
    @Transactional
    void createImprovementPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);
        var returnedImprovementPlanDTO = om.readValue(
            restImprovementPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(improvementPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImprovementPlanDTO.class
        );

        // Validate the ImprovementPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImprovementPlan = improvementPlanMapper.toEntity(returnedImprovementPlanDTO);
        assertImprovementPlanUpdatableFieldsEquals(returnedImprovementPlan, getPersistedImprovementPlan(returnedImprovementPlan));

        insertedImprovementPlan = returnedImprovementPlan;
    }

    @Test
    @Transactional
    void createImprovementPlanWithExistingId() throws Exception {
        // Create the ImprovementPlan with an existing ID
        improvementPlan.setId(1L);
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImprovementPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(improvementPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlanNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        improvementPlan.setPlanName(null);

        // Create the ImprovementPlan, which fails.
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        restImprovementPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(improvementPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlanStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        improvementPlan.setPlanStatus(null);

        // Create the ImprovementPlan, which fails.
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        restImprovementPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(improvementPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImprovementPlans() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(improvementPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].planStatus").value(hasItem(DEFAULT_PLAN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].problemSummary").value(hasItem(DEFAULT_PROBLEM_SUMMARY)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewResult").value(hasItem(DEFAULT_REVIEW_RESULT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImprovementPlansWithEagerRelationshipsIsEnabled() throws Exception {
        when(improvementPlanServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImprovementPlanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(improvementPlanServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImprovementPlansWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(improvementPlanServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImprovementPlanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(improvementPlanRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getImprovementPlan() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get the improvementPlan
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, improvementPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(improvementPlan.getId().intValue()))
            .andExpect(jsonPath("$.planName").value(DEFAULT_PLAN_NAME))
            .andExpect(jsonPath("$.planStatus").value(DEFAULT_PLAN_STATUS.toString()))
            .andExpect(jsonPath("$.problemSummary").value(DEFAULT_PROBLEM_SUMMARY))
            .andExpect(jsonPath("$.improvementAction").value(DEFAULT_IMPROVEMENT_ACTION))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.completionDate").value(DEFAULT_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.reviewResult").value(DEFAULT_REVIEW_RESULT));
    }

    @Test
    @Transactional
    void getImprovementPlansByIdFiltering() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        Long id = improvementPlan.getId();

        defaultImprovementPlanFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImprovementPlanFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImprovementPlanFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planName equals to
        defaultImprovementPlanFiltering("planName.equals=" + DEFAULT_PLAN_NAME, "planName.equals=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planName in
        defaultImprovementPlanFiltering("planName.in=" + DEFAULT_PLAN_NAME + "," + UPDATED_PLAN_NAME, "planName.in=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planName is not null
        defaultImprovementPlanFiltering("planName.specified=true", "planName.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planName contains
        defaultImprovementPlanFiltering("planName.contains=" + DEFAULT_PLAN_NAME, "planName.contains=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planName does not contain
        defaultImprovementPlanFiltering("planName.doesNotContain=" + UPDATED_PLAN_NAME, "planName.doesNotContain=" + DEFAULT_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planStatus equals to
        defaultImprovementPlanFiltering("planStatus.equals=" + DEFAULT_PLAN_STATUS, "planStatus.equals=" + UPDATED_PLAN_STATUS);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planStatus in
        defaultImprovementPlanFiltering(
            "planStatus.in=" + DEFAULT_PLAN_STATUS + "," + UPDATED_PLAN_STATUS,
            "planStatus.in=" + UPDATED_PLAN_STATUS
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPlanStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where planStatus is not null
        defaultImprovementPlanFiltering("planStatus.specified=true", "planStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByOwnerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where ownerName equals to
        defaultImprovementPlanFiltering("ownerName.equals=" + DEFAULT_OWNER_NAME, "ownerName.equals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByOwnerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where ownerName in
        defaultImprovementPlanFiltering(
            "ownerName.in=" + DEFAULT_OWNER_NAME + "," + UPDATED_OWNER_NAME,
            "ownerName.in=" + UPDATED_OWNER_NAME
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByOwnerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where ownerName is not null
        defaultImprovementPlanFiltering("ownerName.specified=true", "ownerName.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByOwnerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where ownerName contains
        defaultImprovementPlanFiltering("ownerName.contains=" + DEFAULT_OWNER_NAME, "ownerName.contains=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByOwnerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where ownerName does not contain
        defaultImprovementPlanFiltering("ownerName.doesNotContain=" + UPDATED_OWNER_NAME, "ownerName.doesNotContain=" + DEFAULT_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate equals to
        defaultImprovementPlanFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate in
        defaultImprovementPlanFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate is not null
        defaultImprovementPlanFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate is greater than or equal to
        defaultImprovementPlanFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate is less than or equal to
        defaultImprovementPlanFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate is less than
        defaultImprovementPlanFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where startDate is greater than
        defaultImprovementPlanFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate equals to
        defaultImprovementPlanFiltering("targetDate.equals=" + DEFAULT_TARGET_DATE, "targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate in
        defaultImprovementPlanFiltering(
            "targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE,
            "targetDate.in=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate is not null
        defaultImprovementPlanFiltering("targetDate.specified=true", "targetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate is greater than or equal to
        defaultImprovementPlanFiltering(
            "targetDate.greaterThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.greaterThanOrEqual=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate is less than or equal to
        defaultImprovementPlanFiltering(
            "targetDate.lessThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.lessThanOrEqual=" + SMALLER_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate is less than
        defaultImprovementPlanFiltering("targetDate.lessThan=" + UPDATED_TARGET_DATE, "targetDate.lessThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByTargetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where targetDate is greater than
        defaultImprovementPlanFiltering("targetDate.greaterThan=" + SMALLER_TARGET_DATE, "targetDate.greaterThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate equals to
        defaultImprovementPlanFiltering(
            "completionDate.equals=" + DEFAULT_COMPLETION_DATE,
            "completionDate.equals=" + UPDATED_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate in
        defaultImprovementPlanFiltering(
            "completionDate.in=" + DEFAULT_COMPLETION_DATE + "," + UPDATED_COMPLETION_DATE,
            "completionDate.in=" + UPDATED_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate is not null
        defaultImprovementPlanFiltering("completionDate.specified=true", "completionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate is greater than or equal to
        defaultImprovementPlanFiltering(
            "completionDate.greaterThanOrEqual=" + DEFAULT_COMPLETION_DATE,
            "completionDate.greaterThanOrEqual=" + UPDATED_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate is less than or equal to
        defaultImprovementPlanFiltering(
            "completionDate.lessThanOrEqual=" + DEFAULT_COMPLETION_DATE,
            "completionDate.lessThanOrEqual=" + SMALLER_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate is less than
        defaultImprovementPlanFiltering(
            "completionDate.lessThan=" + UPDATED_COMPLETION_DATE,
            "completionDate.lessThan=" + DEFAULT_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByCompletionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        // Get all the improvementPlanList where completionDate is greater than
        defaultImprovementPlanFiltering(
            "completionDate.greaterThan=" + SMALLER_COMPLETION_DATE,
            "completionDate.greaterThan=" + DEFAULT_COMPLETION_DATE
        );
    }

    @Test
    @Transactional
    void getAllImprovementPlansByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            improvementPlanRepository.saveAndFlush(improvementPlan);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        improvementPlan.setPosition(position);
        improvementPlanRepository.saveAndFlush(improvementPlan);
        Long positionId = position.getId();
        // Get all the improvementPlanList where position equals to positionId
        defaultImprovementPlanShouldBeFound("positionId.equals=" + positionId);

        // Get all the improvementPlanList where position equals to (positionId + 1)
        defaultImprovementPlanShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllImprovementPlansBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            improvementPlanRepository.saveAndFlush(improvementPlan);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        improvementPlan.setSkill(skill);
        improvementPlanRepository.saveAndFlush(improvementPlan);
        Long skillId = skill.getId();
        // Get all the improvementPlanList where skill equals to skillId
        defaultImprovementPlanShouldBeFound("skillId.equals=" + skillId);

        // Get all the improvementPlanList where skill equals to (skillId + 1)
        defaultImprovementPlanShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    private void defaultImprovementPlanFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImprovementPlanShouldBeFound(shouldBeFound);
        defaultImprovementPlanShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImprovementPlanShouldBeFound(String filter) throws Exception {
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(improvementPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].planStatus").value(hasItem(DEFAULT_PLAN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].problemSummary").value(hasItem(DEFAULT_PROBLEM_SUMMARY)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewResult").value(hasItem(DEFAULT_REVIEW_RESULT)));

        // Check, that the count call also returns 1
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImprovementPlanShouldNotBeFound(String filter) throws Exception {
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImprovementPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImprovementPlan() throws Exception {
        // Get the improvementPlan
        restImprovementPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImprovementPlan() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the improvementPlan
        ImprovementPlan updatedImprovementPlan = improvementPlanRepository.findById(improvementPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImprovementPlan are not directly saved in db
        em.detach(updatedImprovementPlan);
        updatedImprovementPlan
            .planName(UPDATED_PLAN_NAME)
            .planStatus(UPDATED_PLAN_STATUS)
            .problemSummary(UPDATED_PROBLEM_SUMMARY)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .ownerName(UPDATED_OWNER_NAME)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .reviewResult(UPDATED_REVIEW_RESULT);
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(updatedImprovementPlan);

        restImprovementPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, improvementPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(improvementPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImprovementPlanToMatchAllProperties(updatedImprovementPlan);
    }

    @Test
    @Transactional
    void putNonExistingImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, improvementPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(improvementPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(improvementPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(improvementPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImprovementPlanWithPatch() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the improvementPlan using partial update
        ImprovementPlan partialUpdatedImprovementPlan = new ImprovementPlan();
        partialUpdatedImprovementPlan.setId(improvementPlan.getId());

        partialUpdatedImprovementPlan
            .planName(UPDATED_PLAN_NAME)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .startDate(UPDATED_START_DATE)
            .completionDate(UPDATED_COMPLETION_DATE);

        restImprovementPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImprovementPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImprovementPlan))
            )
            .andExpect(status().isOk());

        // Validate the ImprovementPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImprovementPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImprovementPlan, improvementPlan),
            getPersistedImprovementPlan(improvementPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateImprovementPlanWithPatch() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the improvementPlan using partial update
        ImprovementPlan partialUpdatedImprovementPlan = new ImprovementPlan();
        partialUpdatedImprovementPlan.setId(improvementPlan.getId());

        partialUpdatedImprovementPlan
            .planName(UPDATED_PLAN_NAME)
            .planStatus(UPDATED_PLAN_STATUS)
            .problemSummary(UPDATED_PROBLEM_SUMMARY)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .ownerName(UPDATED_OWNER_NAME)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .reviewResult(UPDATED_REVIEW_RESULT);

        restImprovementPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImprovementPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImprovementPlan))
            )
            .andExpect(status().isOk());

        // Validate the ImprovementPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImprovementPlanUpdatableFieldsEquals(
            partialUpdatedImprovementPlan,
            getPersistedImprovementPlan(partialUpdatedImprovementPlan)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, improvementPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(improvementPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(improvementPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImprovementPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        improvementPlan.setId(longCount.incrementAndGet());

        // Create the ImprovementPlan
        ImprovementPlanDTO improvementPlanDTO = improvementPlanMapper.toDto(improvementPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImprovementPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(improvementPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImprovementPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImprovementPlan() throws Exception {
        // Initialize the database
        insertedImprovementPlan = improvementPlanRepository.saveAndFlush(improvementPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the improvementPlan
        restImprovementPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, improvementPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return improvementPlanRepository.count();
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

    protected ImprovementPlan getPersistedImprovementPlan(ImprovementPlan improvementPlan) {
        return improvementPlanRepository.findById(improvementPlan.getId()).orElseThrow();
    }

    protected void assertPersistedImprovementPlanToMatchAllProperties(ImprovementPlan expectedImprovementPlan) {
        assertImprovementPlanAllPropertiesEquals(expectedImprovementPlan, getPersistedImprovementPlan(expectedImprovementPlan));
    }

    protected void assertPersistedImprovementPlanToMatchUpdatableProperties(ImprovementPlan expectedImprovementPlan) {
        assertImprovementPlanAllUpdatablePropertiesEquals(expectedImprovementPlan, getPersistedImprovementPlan(expectedImprovementPlan));
    }
}
