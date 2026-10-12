package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionAsserts.*;
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
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMapper;

/**
 * Integration tests for the {@link RecruitmentRequisitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class RecruitmentRequisitionResourceIT {

    private static final String DEFAULT_REQUISITION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final Long DEFAULT_POSITION_ID = 1L;
    private static final Long UPDATED_POSITION_ID = 2L;
    private static final Long SMALLER_POSITION_ID = 1L - 1L;

    private static final RecruitmentType DEFAULT_RECRUITMENT_TYPE = RecruitmentType.GRADUATE;
    private static final RecruitmentType UPDATED_RECRUITMENT_TYPE = RecruitmentType.MID_CAREER;

    private static final RequisitionLifecycle DEFAULT_LIFECYCLE = RequisitionLifecycle.PREPARING;
    private static final RequisitionLifecycle UPDATED_LIFECYCLE = RequisitionLifecycle.ACTIVE;

    private static final RequisitionEditStatus DEFAULT_EDIT_STATUS = RequisitionEditStatus.EDITABLE;
    private static final RequisitionEditStatus UPDATED_EDIT_STATUS = RequisitionEditStatus.LOCKED;

    private static final Integer DEFAULT_PLANNED_HEADCOUNT = 1;
    private static final Integer UPDATED_PLANNED_HEADCOUNT = 2;
    private static final Integer SMALLER_PLANNED_HEADCOUNT = 1 - 1;

    private static final Integer DEFAULT_HIRED_COUNT = 0;
    private static final Integer UPDATED_HIRED_COUNT = 1;
    private static final Integer SMALLER_HIRED_COUNT = 0 - 1;

    private static final Integer DEFAULT_CHECKED_IN_COUNT = 0;
    private static final Integer UPDATED_CHECKED_IN_COUNT = 1;
    private static final Integer SMALLER_CHECKED_IN_COUNT = 0 - 1;

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TARGET_JOIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARGET_JOIN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TARGET_JOIN_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DESIRED_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DESIRED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DESIRED_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DESIRED_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DESIRED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DESIRED_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CURRENT_VERSION = 0;
    private static final Integer UPDATED_CURRENT_VERSION = 1;
    private static final Integer SMALLER_CURRENT_VERSION = 0 - 1;

    private static final Instant DEFAULT_LOCKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOCKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCKED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LOCKED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UNLOCKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UNLOCKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UNLOCK_REASON = "AAAAAAAAAA";
    private static final String UPDATED_UNLOCK_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIREMENTS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS_JSON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recruitment-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruitmentRequisitionRepository recruitmentRequisitionRepository;

    @Autowired
    private RecruitmentRequisitionMapper recruitmentRequisitionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentRequisitionMockMvc;

    private RecruitmentRequisition recruitmentRequisition;

    private RecruitmentRequisition insertedRecruitmentRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisition createEntity() {
        return new RecruitmentRequisition()
            .requisitionCode(DEFAULT_REQUISITION_CODE)
            .title(DEFAULT_TITLE)
            .department(DEFAULT_DEPARTMENT)
            .positionId(DEFAULT_POSITION_ID)
            .recruitmentType(DEFAULT_RECRUITMENT_TYPE)
            .lifecycle(DEFAULT_LIFECYCLE)
            .editStatus(DEFAULT_EDIT_STATUS)
            .plannedHeadcount(DEFAULT_PLANNED_HEADCOUNT)
            .hiredCount(DEFAULT_HIRED_COUNT)
            .checkedInCount(DEFAULT_CHECKED_IN_COUNT)
            .priority(DEFAULT_PRIORITY)
            .reason(DEFAULT_REASON)
            .targetJoinDate(DEFAULT_TARGET_JOIN_DATE)
            .desiredStartDate(DEFAULT_DESIRED_START_DATE)
            .desiredEndDate(DEFAULT_DESIRED_END_DATE)
            .currentVersion(DEFAULT_CURRENT_VERSION)
            .lockedAt(DEFAULT_LOCKED_AT)
            .lockedBy(DEFAULT_LOCKED_BY)
            .unlockedAt(DEFAULT_UNLOCKED_AT)
            .unlockReason(DEFAULT_UNLOCK_REASON)
            .requirementsJson(DEFAULT_REQUIREMENTS_JSON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisition createUpdatedEntity() {
        return new RecruitmentRequisition()
            .requisitionCode(UPDATED_REQUISITION_CODE)
            .title(UPDATED_TITLE)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .lifecycle(UPDATED_LIFECYCLE)
            .editStatus(UPDATED_EDIT_STATUS)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .hiredCount(UPDATED_HIRED_COUNT)
            .checkedInCount(UPDATED_CHECKED_IN_COUNT)
            .priority(UPDATED_PRIORITY)
            .reason(UPDATED_REASON)
            .targetJoinDate(UPDATED_TARGET_JOIN_DATE)
            .desiredStartDate(UPDATED_DESIRED_START_DATE)
            .desiredEndDate(UPDATED_DESIRED_END_DATE)
            .currentVersion(UPDATED_CURRENT_VERSION)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .unlockedAt(UPDATED_UNLOCKED_AT)
            .unlockReason(UPDATED_UNLOCK_REASON)
            .requirementsJson(UPDATED_REQUIREMENTS_JSON);
    }

    @BeforeEach
    void initTest() {
        recruitmentRequisition = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecruitmentRequisition != null) {
            recruitmentRequisitionRepository.delete(insertedRecruitmentRequisition);
            insertedRecruitmentRequisition = null;
        }
    }

    @Test
    @Transactional
    void createRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);
        var returnedRecruitmentRequisitionDTO = om.readValue(
            restRecruitmentRequisitionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecruitmentRequisitionDTO.class
        );

        // Validate the RecruitmentRequisition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecruitmentRequisition = recruitmentRequisitionMapper.toEntity(returnedRecruitmentRequisitionDTO);
        assertRecruitmentRequisitionUpdatableFieldsEquals(
            returnedRecruitmentRequisition,
            getPersistedRecruitmentRequisition(returnedRecruitmentRequisition)
        );

        insertedRecruitmentRequisition = returnedRecruitmentRequisition;
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionWithExistingId() throws Exception {
        // Create the RecruitmentRequisition with an existing ID
        recruitmentRequisition.setId(1L);
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequisitionCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setRequisitionCode(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setTitle(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setDepartment(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecruitmentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setRecruitmentType(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLifecycleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setLifecycle(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEditStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setEditStatus(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlannedHeadcountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setPlannedHeadcount(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHiredCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setHiredCount(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCheckedInCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setCheckedInCount(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisition.setCurrentVersion(null);

        // Create the RecruitmentRequisition, which fails.
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitions() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionCode").value(hasItem(DEFAULT_REQUISITION_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].lifecycle").value(hasItem(DEFAULT_LIFECYCLE.toString())))
            .andExpect(jsonPath("$.[*].editStatus").value(hasItem(DEFAULT_EDIT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].plannedHeadcount").value(hasItem(DEFAULT_PLANNED_HEADCOUNT)))
            .andExpect(jsonPath("$.[*].hiredCount").value(hasItem(DEFAULT_HIRED_COUNT)))
            .andExpect(jsonPath("$.[*].checkedInCount").value(hasItem(DEFAULT_CHECKED_IN_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].targetJoinDate").value(hasItem(DEFAULT_TARGET_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].desiredStartDate").value(hasItem(DEFAULT_DESIRED_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].desiredEndDate").value(hasItem(DEFAULT_DESIRED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)))
            .andExpect(jsonPath("$.[*].lockedAt").value(hasItem(DEFAULT_LOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].lockedBy").value(hasItem(DEFAULT_LOCKED_BY)))
            .andExpect(jsonPath("$.[*].unlockedAt").value(hasItem(DEFAULT_UNLOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].unlockReason").value(hasItem(DEFAULT_UNLOCK_REASON)))
            .andExpect(jsonPath("$.[*].requirementsJson").value(hasItem(DEFAULT_REQUIREMENTS_JSON)));
    }

    @Test
    @Transactional
    void getRecruitmentRequisition() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get the recruitmentRequisition
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentRequisition.getId().intValue()))
            .andExpect(jsonPath("$.requisitionCode").value(DEFAULT_REQUISITION_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.positionId").value(DEFAULT_POSITION_ID.intValue()))
            .andExpect(jsonPath("$.recruitmentType").value(DEFAULT_RECRUITMENT_TYPE.toString()))
            .andExpect(jsonPath("$.lifecycle").value(DEFAULT_LIFECYCLE.toString()))
            .andExpect(jsonPath("$.editStatus").value(DEFAULT_EDIT_STATUS.toString()))
            .andExpect(jsonPath("$.plannedHeadcount").value(DEFAULT_PLANNED_HEADCOUNT))
            .andExpect(jsonPath("$.hiredCount").value(DEFAULT_HIRED_COUNT))
            .andExpect(jsonPath("$.checkedInCount").value(DEFAULT_CHECKED_IN_COUNT))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.targetJoinDate").value(DEFAULT_TARGET_JOIN_DATE.toString()))
            .andExpect(jsonPath("$.desiredStartDate").value(DEFAULT_DESIRED_START_DATE.toString()))
            .andExpect(jsonPath("$.desiredEndDate").value(DEFAULT_DESIRED_END_DATE.toString()))
            .andExpect(jsonPath("$.currentVersion").value(DEFAULT_CURRENT_VERSION))
            .andExpect(jsonPath("$.lockedAt").value(DEFAULT_LOCKED_AT.toString()))
            .andExpect(jsonPath("$.lockedBy").value(DEFAULT_LOCKED_BY))
            .andExpect(jsonPath("$.unlockedAt").value(DEFAULT_UNLOCKED_AT.toString()))
            .andExpect(jsonPath("$.unlockReason").value(DEFAULT_UNLOCK_REASON))
            .andExpect(jsonPath("$.requirementsJson").value(DEFAULT_REQUIREMENTS_JSON));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        Long id = recruitmentRequisition.getId();

        defaultRecruitmentRequisitionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecruitmentRequisitionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecruitmentRequisitionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRequisitionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where requisitionCode equals to
        defaultRecruitmentRequisitionFiltering(
            "requisitionCode.equals=" + DEFAULT_REQUISITION_CODE,
            "requisitionCode.equals=" + UPDATED_REQUISITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRequisitionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where requisitionCode in
        defaultRecruitmentRequisitionFiltering(
            "requisitionCode.in=" + DEFAULT_REQUISITION_CODE + "," + UPDATED_REQUISITION_CODE,
            "requisitionCode.in=" + UPDATED_REQUISITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRequisitionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where requisitionCode is not null
        defaultRecruitmentRequisitionFiltering("requisitionCode.specified=true", "requisitionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRequisitionCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where requisitionCode contains
        defaultRecruitmentRequisitionFiltering(
            "requisitionCode.contains=" + DEFAULT_REQUISITION_CODE,
            "requisitionCode.contains=" + UPDATED_REQUISITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRequisitionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where requisitionCode does not contain
        defaultRecruitmentRequisitionFiltering(
            "requisitionCode.doesNotContain=" + UPDATED_REQUISITION_CODE,
            "requisitionCode.doesNotContain=" + DEFAULT_REQUISITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where title equals to
        defaultRecruitmentRequisitionFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where title in
        defaultRecruitmentRequisitionFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where title is not null
        defaultRecruitmentRequisitionFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where title contains
        defaultRecruitmentRequisitionFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where title does not contain
        defaultRecruitmentRequisitionFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where department equals to
        defaultRecruitmentRequisitionFiltering("department.equals=" + DEFAULT_DEPARTMENT, "department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where department in
        defaultRecruitmentRequisitionFiltering(
            "department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT,
            "department.in=" + UPDATED_DEPARTMENT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where department is not null
        defaultRecruitmentRequisitionFiltering("department.specified=true", "department.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where department contains
        defaultRecruitmentRequisitionFiltering("department.contains=" + DEFAULT_DEPARTMENT, "department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where department does not contain
        defaultRecruitmentRequisitionFiltering(
            "department.doesNotContain=" + UPDATED_DEPARTMENT,
            "department.doesNotContain=" + DEFAULT_DEPARTMENT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId equals to
        defaultRecruitmentRequisitionFiltering("positionId.equals=" + DEFAULT_POSITION_ID, "positionId.equals=" + UPDATED_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId in
        defaultRecruitmentRequisitionFiltering(
            "positionId.in=" + DEFAULT_POSITION_ID + "," + UPDATED_POSITION_ID,
            "positionId.in=" + UPDATED_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId is not null
        defaultRecruitmentRequisitionFiltering("positionId.specified=true", "positionId.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "positionId.greaterThanOrEqual=" + DEFAULT_POSITION_ID,
            "positionId.greaterThanOrEqual=" + UPDATED_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "positionId.lessThanOrEqual=" + DEFAULT_POSITION_ID,
            "positionId.lessThanOrEqual=" + SMALLER_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId is less than
        defaultRecruitmentRequisitionFiltering("positionId.lessThan=" + UPDATED_POSITION_ID, "positionId.lessThan=" + DEFAULT_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPositionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where positionId is greater than
        defaultRecruitmentRequisitionFiltering(
            "positionId.greaterThan=" + SMALLER_POSITION_ID,
            "positionId.greaterThan=" + DEFAULT_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRecruitmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where recruitmentType equals to
        defaultRecruitmentRequisitionFiltering(
            "recruitmentType.equals=" + DEFAULT_RECRUITMENT_TYPE,
            "recruitmentType.equals=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRecruitmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where recruitmentType in
        defaultRecruitmentRequisitionFiltering(
            "recruitmentType.in=" + DEFAULT_RECRUITMENT_TYPE + "," + UPDATED_RECRUITMENT_TYPE,
            "recruitmentType.in=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByRecruitmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where recruitmentType is not null
        defaultRecruitmentRequisitionFiltering("recruitmentType.specified=true", "recruitmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLifecycleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lifecycle equals to
        defaultRecruitmentRequisitionFiltering("lifecycle.equals=" + DEFAULT_LIFECYCLE, "lifecycle.equals=" + UPDATED_LIFECYCLE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLifecycleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lifecycle in
        defaultRecruitmentRequisitionFiltering(
            "lifecycle.in=" + DEFAULT_LIFECYCLE + "," + UPDATED_LIFECYCLE,
            "lifecycle.in=" + UPDATED_LIFECYCLE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLifecycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lifecycle is not null
        defaultRecruitmentRequisitionFiltering("lifecycle.specified=true", "lifecycle.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByEditStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where editStatus equals to
        defaultRecruitmentRequisitionFiltering("editStatus.equals=" + DEFAULT_EDIT_STATUS, "editStatus.equals=" + UPDATED_EDIT_STATUS);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByEditStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where editStatus in
        defaultRecruitmentRequisitionFiltering(
            "editStatus.in=" + DEFAULT_EDIT_STATUS + "," + UPDATED_EDIT_STATUS,
            "editStatus.in=" + UPDATED_EDIT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByEditStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where editStatus is not null
        defaultRecruitmentRequisitionFiltering("editStatus.specified=true", "editStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount equals to
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.equals=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.equals=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount in
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.in=" + DEFAULT_PLANNED_HEADCOUNT + "," + UPDATED_PLANNED_HEADCOUNT,
            "plannedHeadcount.in=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount is not null
        defaultRecruitmentRequisitionFiltering("plannedHeadcount.specified=true", "plannedHeadcount.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.greaterThanOrEqual=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.greaterThanOrEqual=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.lessThanOrEqual=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.lessThanOrEqual=" + SMALLER_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount is less than
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.lessThan=" + UPDATED_PLANNED_HEADCOUNT,
            "plannedHeadcount.lessThan=" + DEFAULT_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPlannedHeadcountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where plannedHeadcount is greater than
        defaultRecruitmentRequisitionFiltering(
            "plannedHeadcount.greaterThan=" + SMALLER_PLANNED_HEADCOUNT,
            "plannedHeadcount.greaterThan=" + DEFAULT_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount equals to
        defaultRecruitmentRequisitionFiltering("hiredCount.equals=" + DEFAULT_HIRED_COUNT, "hiredCount.equals=" + UPDATED_HIRED_COUNT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount in
        defaultRecruitmentRequisitionFiltering(
            "hiredCount.in=" + DEFAULT_HIRED_COUNT + "," + UPDATED_HIRED_COUNT,
            "hiredCount.in=" + UPDATED_HIRED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount is not null
        defaultRecruitmentRequisitionFiltering("hiredCount.specified=true", "hiredCount.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "hiredCount.greaterThanOrEqual=" + DEFAULT_HIRED_COUNT,
            "hiredCount.greaterThanOrEqual=" + UPDATED_HIRED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "hiredCount.lessThanOrEqual=" + DEFAULT_HIRED_COUNT,
            "hiredCount.lessThanOrEqual=" + SMALLER_HIRED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount is less than
        defaultRecruitmentRequisitionFiltering("hiredCount.lessThan=" + UPDATED_HIRED_COUNT, "hiredCount.lessThan=" + DEFAULT_HIRED_COUNT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByHiredCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where hiredCount is greater than
        defaultRecruitmentRequisitionFiltering(
            "hiredCount.greaterThan=" + SMALLER_HIRED_COUNT,
            "hiredCount.greaterThan=" + DEFAULT_HIRED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount equals to
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.equals=" + DEFAULT_CHECKED_IN_COUNT,
            "checkedInCount.equals=" + UPDATED_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount in
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.in=" + DEFAULT_CHECKED_IN_COUNT + "," + UPDATED_CHECKED_IN_COUNT,
            "checkedInCount.in=" + UPDATED_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount is not null
        defaultRecruitmentRequisitionFiltering("checkedInCount.specified=true", "checkedInCount.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.greaterThanOrEqual=" + DEFAULT_CHECKED_IN_COUNT,
            "checkedInCount.greaterThanOrEqual=" + UPDATED_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.lessThanOrEqual=" + DEFAULT_CHECKED_IN_COUNT,
            "checkedInCount.lessThanOrEqual=" + SMALLER_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount is less than
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.lessThan=" + UPDATED_CHECKED_IN_COUNT,
            "checkedInCount.lessThan=" + DEFAULT_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCheckedInCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where checkedInCount is greater than
        defaultRecruitmentRequisitionFiltering(
            "checkedInCount.greaterThan=" + SMALLER_CHECKED_IN_COUNT,
            "checkedInCount.greaterThan=" + DEFAULT_CHECKED_IN_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where priority equals to
        defaultRecruitmentRequisitionFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where priority in
        defaultRecruitmentRequisitionFiltering(
            "priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY,
            "priority.in=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where priority is not null
        defaultRecruitmentRequisitionFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPriorityContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where priority contains
        defaultRecruitmentRequisitionFiltering("priority.contains=" + DEFAULT_PRIORITY, "priority.contains=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByPriorityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where priority does not contain
        defaultRecruitmentRequisitionFiltering(
            "priority.doesNotContain=" + UPDATED_PRIORITY,
            "priority.doesNotContain=" + DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate equals to
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.equals=" + DEFAULT_TARGET_JOIN_DATE,
            "targetJoinDate.equals=" + UPDATED_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate in
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.in=" + DEFAULT_TARGET_JOIN_DATE + "," + UPDATED_TARGET_JOIN_DATE,
            "targetJoinDate.in=" + UPDATED_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate is not null
        defaultRecruitmentRequisitionFiltering("targetJoinDate.specified=true", "targetJoinDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.greaterThanOrEqual=" + DEFAULT_TARGET_JOIN_DATE,
            "targetJoinDate.greaterThanOrEqual=" + UPDATED_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.lessThanOrEqual=" + DEFAULT_TARGET_JOIN_DATE,
            "targetJoinDate.lessThanOrEqual=" + SMALLER_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate is less than
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.lessThan=" + UPDATED_TARGET_JOIN_DATE,
            "targetJoinDate.lessThan=" + DEFAULT_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByTargetJoinDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where targetJoinDate is greater than
        defaultRecruitmentRequisitionFiltering(
            "targetJoinDate.greaterThan=" + SMALLER_TARGET_JOIN_DATE,
            "targetJoinDate.greaterThan=" + DEFAULT_TARGET_JOIN_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate equals to
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.equals=" + DEFAULT_DESIRED_START_DATE,
            "desiredStartDate.equals=" + UPDATED_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate in
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.in=" + DEFAULT_DESIRED_START_DATE + "," + UPDATED_DESIRED_START_DATE,
            "desiredStartDate.in=" + UPDATED_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate is not null
        defaultRecruitmentRequisitionFiltering("desiredStartDate.specified=true", "desiredStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.greaterThanOrEqual=" + DEFAULT_DESIRED_START_DATE,
            "desiredStartDate.greaterThanOrEqual=" + UPDATED_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.lessThanOrEqual=" + DEFAULT_DESIRED_START_DATE,
            "desiredStartDate.lessThanOrEqual=" + SMALLER_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate is less than
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.lessThan=" + UPDATED_DESIRED_START_DATE,
            "desiredStartDate.lessThan=" + DEFAULT_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredStartDate is greater than
        defaultRecruitmentRequisitionFiltering(
            "desiredStartDate.greaterThan=" + SMALLER_DESIRED_START_DATE,
            "desiredStartDate.greaterThan=" + DEFAULT_DESIRED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate equals to
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.equals=" + DEFAULT_DESIRED_END_DATE,
            "desiredEndDate.equals=" + UPDATED_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate in
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.in=" + DEFAULT_DESIRED_END_DATE + "," + UPDATED_DESIRED_END_DATE,
            "desiredEndDate.in=" + UPDATED_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate is not null
        defaultRecruitmentRequisitionFiltering("desiredEndDate.specified=true", "desiredEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.greaterThanOrEqual=" + DEFAULT_DESIRED_END_DATE,
            "desiredEndDate.greaterThanOrEqual=" + UPDATED_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.lessThanOrEqual=" + DEFAULT_DESIRED_END_DATE,
            "desiredEndDate.lessThanOrEqual=" + SMALLER_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate is less than
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.lessThan=" + UPDATED_DESIRED_END_DATE,
            "desiredEndDate.lessThan=" + DEFAULT_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByDesiredEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where desiredEndDate is greater than
        defaultRecruitmentRequisitionFiltering(
            "desiredEndDate.greaterThan=" + SMALLER_DESIRED_END_DATE,
            "desiredEndDate.greaterThan=" + DEFAULT_DESIRED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion equals to
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.equals=" + DEFAULT_CURRENT_VERSION,
            "currentVersion.equals=" + UPDATED_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion in
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.in=" + DEFAULT_CURRENT_VERSION + "," + UPDATED_CURRENT_VERSION,
            "currentVersion.in=" + UPDATED_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion is not null
        defaultRecruitmentRequisitionFiltering("currentVersion.specified=true", "currentVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion is greater than or equal to
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.greaterThanOrEqual=" + DEFAULT_CURRENT_VERSION,
            "currentVersion.greaterThanOrEqual=" + UPDATED_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion is less than or equal to
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.lessThanOrEqual=" + DEFAULT_CURRENT_VERSION,
            "currentVersion.lessThanOrEqual=" + SMALLER_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion is less than
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.lessThan=" + UPDATED_CURRENT_VERSION,
            "currentVersion.lessThan=" + DEFAULT_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByCurrentVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where currentVersion is greater than
        defaultRecruitmentRequisitionFiltering(
            "currentVersion.greaterThan=" + SMALLER_CURRENT_VERSION,
            "currentVersion.greaterThan=" + DEFAULT_CURRENT_VERSION
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedAt equals to
        defaultRecruitmentRequisitionFiltering("lockedAt.equals=" + DEFAULT_LOCKED_AT, "lockedAt.equals=" + UPDATED_LOCKED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedAt in
        defaultRecruitmentRequisitionFiltering(
            "lockedAt.in=" + DEFAULT_LOCKED_AT + "," + UPDATED_LOCKED_AT,
            "lockedAt.in=" + UPDATED_LOCKED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedAt is not null
        defaultRecruitmentRequisitionFiltering("lockedAt.specified=true", "lockedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedBy equals to
        defaultRecruitmentRequisitionFiltering("lockedBy.equals=" + DEFAULT_LOCKED_BY, "lockedBy.equals=" + UPDATED_LOCKED_BY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedBy in
        defaultRecruitmentRequisitionFiltering(
            "lockedBy.in=" + DEFAULT_LOCKED_BY + "," + UPDATED_LOCKED_BY,
            "lockedBy.in=" + UPDATED_LOCKED_BY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedBy is not null
        defaultRecruitmentRequisitionFiltering("lockedBy.specified=true", "lockedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedByContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedBy contains
        defaultRecruitmentRequisitionFiltering("lockedBy.contains=" + DEFAULT_LOCKED_BY, "lockedBy.contains=" + UPDATED_LOCKED_BY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByLockedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where lockedBy does not contain
        defaultRecruitmentRequisitionFiltering(
            "lockedBy.doesNotContain=" + UPDATED_LOCKED_BY,
            "lockedBy.doesNotContain=" + DEFAULT_LOCKED_BY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByUnlockedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where unlockedAt equals to
        defaultRecruitmentRequisitionFiltering("unlockedAt.equals=" + DEFAULT_UNLOCKED_AT, "unlockedAt.equals=" + UPDATED_UNLOCKED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByUnlockedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where unlockedAt in
        defaultRecruitmentRequisitionFiltering(
            "unlockedAt.in=" + DEFAULT_UNLOCKED_AT + "," + UPDATED_UNLOCKED_AT,
            "unlockedAt.in=" + UPDATED_UNLOCKED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionsByUnlockedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        // Get all the recruitmentRequisitionList where unlockedAt is not null
        defaultRecruitmentRequisitionFiltering("unlockedAt.specified=true", "unlockedAt.specified=false");
    }

    private void defaultRecruitmentRequisitionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecruitmentRequisitionShouldBeFound(shouldBeFound);
        defaultRecruitmentRequisitionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecruitmentRequisitionShouldBeFound(String filter) throws Exception {
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionCode").value(hasItem(DEFAULT_REQUISITION_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].lifecycle").value(hasItem(DEFAULT_LIFECYCLE.toString())))
            .andExpect(jsonPath("$.[*].editStatus").value(hasItem(DEFAULT_EDIT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].plannedHeadcount").value(hasItem(DEFAULT_PLANNED_HEADCOUNT)))
            .andExpect(jsonPath("$.[*].hiredCount").value(hasItem(DEFAULT_HIRED_COUNT)))
            .andExpect(jsonPath("$.[*].checkedInCount").value(hasItem(DEFAULT_CHECKED_IN_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].targetJoinDate").value(hasItem(DEFAULT_TARGET_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].desiredStartDate").value(hasItem(DEFAULT_DESIRED_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].desiredEndDate").value(hasItem(DEFAULT_DESIRED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)))
            .andExpect(jsonPath("$.[*].lockedAt").value(hasItem(DEFAULT_LOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].lockedBy").value(hasItem(DEFAULT_LOCKED_BY)))
            .andExpect(jsonPath("$.[*].unlockedAt").value(hasItem(DEFAULT_UNLOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].unlockReason").value(hasItem(DEFAULT_UNLOCK_REASON)))
            .andExpect(jsonPath("$.[*].requirementsJson").value(hasItem(DEFAULT_REQUIREMENTS_JSON)));

        // Check, that the count call also returns 1
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecruitmentRequisitionShouldNotBeFound(String filter) throws Exception {
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecruitmentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentRequisition() throws Exception {
        // Get the recruitmentRequisition
        restRecruitmentRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentRequisition() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisition
        RecruitmentRequisition updatedRecruitmentRequisition = recruitmentRequisitionRepository
            .findById(recruitmentRequisition.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecruitmentRequisition are not directly saved in db
        em.detach(updatedRecruitmentRequisition);
        updatedRecruitmentRequisition
            .requisitionCode(UPDATED_REQUISITION_CODE)
            .title(UPDATED_TITLE)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .lifecycle(UPDATED_LIFECYCLE)
            .editStatus(UPDATED_EDIT_STATUS)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .hiredCount(UPDATED_HIRED_COUNT)
            .checkedInCount(UPDATED_CHECKED_IN_COUNT)
            .priority(UPDATED_PRIORITY)
            .reason(UPDATED_REASON)
            .targetJoinDate(UPDATED_TARGET_JOIN_DATE)
            .desiredStartDate(UPDATED_DESIRED_START_DATE)
            .desiredEndDate(UPDATED_DESIRED_END_DATE)
            .currentVersion(UPDATED_CURRENT_VERSION)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .unlockedAt(UPDATED_UNLOCKED_AT)
            .unlockReason(UPDATED_UNLOCK_REASON)
            .requirementsJson(UPDATED_REQUIREMENTS_JSON);
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(updatedRecruitmentRequisition);

        restRecruitmentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruitmentRequisitionToMatchAllProperties(updatedRecruitmentRequisition);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentRequisitionWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisition using partial update
        RecruitmentRequisition partialUpdatedRecruitmentRequisition = new RecruitmentRequisition();
        partialUpdatedRecruitmentRequisition.setId(recruitmentRequisition.getId());

        partialUpdatedRecruitmentRequisition
            .department(UPDATED_DEPARTMENT)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .hiredCount(UPDATED_HIRED_COUNT)
            .priority(UPDATED_PRIORITY)
            .desiredStartDate(UPDATED_DESIRED_START_DATE)
            .lockedBy(UPDATED_LOCKED_BY)
            .unlockedAt(UPDATED_UNLOCKED_AT)
            .unlockReason(UPDATED_UNLOCK_REASON);

        restRecruitmentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisition))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruitmentRequisition, recruitmentRequisition),
            getPersistedRecruitmentRequisition(recruitmentRequisition)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentRequisitionWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisition using partial update
        RecruitmentRequisition partialUpdatedRecruitmentRequisition = new RecruitmentRequisition();
        partialUpdatedRecruitmentRequisition.setId(recruitmentRequisition.getId());

        partialUpdatedRecruitmentRequisition
            .requisitionCode(UPDATED_REQUISITION_CODE)
            .title(UPDATED_TITLE)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .lifecycle(UPDATED_LIFECYCLE)
            .editStatus(UPDATED_EDIT_STATUS)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .hiredCount(UPDATED_HIRED_COUNT)
            .checkedInCount(UPDATED_CHECKED_IN_COUNT)
            .priority(UPDATED_PRIORITY)
            .reason(UPDATED_REASON)
            .targetJoinDate(UPDATED_TARGET_JOIN_DATE)
            .desiredStartDate(UPDATED_DESIRED_START_DATE)
            .desiredEndDate(UPDATED_DESIRED_END_DATE)
            .currentVersion(UPDATED_CURRENT_VERSION)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .unlockedAt(UPDATED_UNLOCKED_AT)
            .unlockReason(UPDATED_UNLOCK_REASON)
            .requirementsJson(UPDATED_REQUIREMENTS_JSON);

        restRecruitmentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisition))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionUpdatableFieldsEquals(
            partialUpdatedRecruitmentRequisition,
            getPersistedRecruitmentRequisition(partialUpdatedRecruitmentRequisition)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentRequisition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisition.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisition
        RecruitmentRequisitionDTO recruitmentRequisitionDTO = recruitmentRequisitionMapper.toDto(recruitmentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recruitmentRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentRequisition() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisition = recruitmentRequisitionRepository.saveAndFlush(recruitmentRequisition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruitmentRequisition
        restRecruitmentRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruitmentRequisitionRepository.count();
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

    protected RecruitmentRequisition getPersistedRecruitmentRequisition(RecruitmentRequisition recruitmentRequisition) {
        return recruitmentRequisitionRepository.findById(recruitmentRequisition.getId()).orElseThrow();
    }

    protected void assertPersistedRecruitmentRequisitionToMatchAllProperties(RecruitmentRequisition expectedRecruitmentRequisition) {
        assertRecruitmentRequisitionAllPropertiesEquals(
            expectedRecruitmentRequisition,
            getPersistedRecruitmentRequisition(expectedRecruitmentRequisition)
        );
    }

    protected void assertPersistedRecruitmentRequisitionToMatchUpdatableProperties(RecruitmentRequisition expectedRecruitmentRequisition) {
        assertRecruitmentRequisitionAllUpdatablePropertiesEquals(
            expectedRecruitmentRequisition,
            getPersistedRecruitmentRequisition(expectedRecruitmentRequisition)
        );
    }
}
