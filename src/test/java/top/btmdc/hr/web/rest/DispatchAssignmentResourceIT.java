package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.DispatchAssignmentAsserts.*;
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
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.repository.DispatchAssignmentRepository;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.service.mapper.DispatchAssignmentMapper;

/**
 * Integration tests for the {@link DispatchAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class DispatchAssignmentResourceIT {

    private static final Long DEFAULT_CANDIDATE_ID = 1L;
    private static final Long UPDATED_CANDIDATE_ID = 2L;
    private static final Long SMALLER_CANDIDATE_ID = 1L - 1L;

    private static final Long DEFAULT_PERSON_ID = 1L;
    private static final Long UPDATED_PERSON_ID = 2L;
    private static final Long SMALLER_PERSON_ID = 1L - 1L;

    private static final Long DEFAULT_PARTNER_ORGANIZATION_ID = 1L;
    private static final Long UPDATED_PARTNER_ORGANIZATION_ID = 2L;
    private static final Long SMALLER_PARTNER_ORGANIZATION_ID = 1L - 1L;

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final Long DEFAULT_POSITION_ID = 1L;
    private static final Long UPDATED_POSITION_ID = 2L;
    private static final Long SMALLER_POSITION_ID = 1L - 1L;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPECTED_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPECTED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPECTED_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_OBSERVATION_DAYS = 0;
    private static final Integer UPDATED_OBSERVATION_DAYS = 1;
    private static final Integer SMALLER_OBSERVATION_DAYS = 0 - 1;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FINAL_DECISION = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_DECISION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dispatch-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DispatchAssignmentRepository dispatchAssignmentRepository;

    @Autowired
    private DispatchAssignmentMapper dispatchAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispatchAssignmentMockMvc;

    private DispatchAssignment dispatchAssignment;

    private DispatchAssignment insertedDispatchAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DispatchAssignment createEntity() {
        return new DispatchAssignment()
            .candidateId(DEFAULT_CANDIDATE_ID)
            .personId(DEFAULT_PERSON_ID)
            .partnerOrganizationId(DEFAULT_PARTNER_ORGANIZATION_ID)
            .department(DEFAULT_DEPARTMENT)
            .positionId(DEFAULT_POSITION_ID)
            .startDate(DEFAULT_START_DATE)
            .expectedEndDate(DEFAULT_EXPECTED_END_DATE)
            .observationDays(DEFAULT_OBSERVATION_DAYS)
            .status(DEFAULT_STATUS)
            .finalDecision(DEFAULT_FINAL_DECISION)
            .note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DispatchAssignment createUpdatedEntity() {
        return new DispatchAssignment()
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .partnerOrganizationId(UPDATED_PARTNER_ORGANIZATION_ID)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .startDate(UPDATED_START_DATE)
            .expectedEndDate(UPDATED_EXPECTED_END_DATE)
            .observationDays(UPDATED_OBSERVATION_DAYS)
            .status(UPDATED_STATUS)
            .finalDecision(UPDATED_FINAL_DECISION)
            .note(UPDATED_NOTE);
    }

    @BeforeEach
    void initTest() {
        dispatchAssignment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDispatchAssignment != null) {
            dispatchAssignmentRepository.delete(insertedDispatchAssignment);
            insertedDispatchAssignment = null;
        }
    }

    @Test
    @Transactional
    void createDispatchAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);
        var returnedDispatchAssignmentDTO = om.readValue(
            restDispatchAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DispatchAssignmentDTO.class
        );

        // Validate the DispatchAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDispatchAssignment = dispatchAssignmentMapper.toEntity(returnedDispatchAssignmentDTO);
        assertDispatchAssignmentUpdatableFieldsEquals(
            returnedDispatchAssignment,
            getPersistedDispatchAssignment(returnedDispatchAssignment)
        );

        insertedDispatchAssignment = returnedDispatchAssignment;
    }

    @Test
    @Transactional
    void createDispatchAssignmentWithExistingId() throws Exception {
        // Create the DispatchAssignment with an existing ID
        dispatchAssignment.setId(1L);
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispatchAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCandidateIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchAssignment.setCandidateId(null);

        // Create the DispatchAssignment, which fails.
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        restDispatchAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPartnerOrganizationIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchAssignment.setPartnerOrganizationId(null);

        // Create the DispatchAssignment, which fails.
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        restDispatchAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchAssignment.setDepartment(null);

        // Create the DispatchAssignment, which fails.
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        restDispatchAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchAssignment.setStatus(null);

        // Create the DispatchAssignment, which fails.
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        restDispatchAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispatchAssignments() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatchAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateId").value(hasItem(DEFAULT_CANDIDATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].partnerOrganizationId").value(hasItem(DEFAULT_PARTNER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedEndDate").value(hasItem(DEFAULT_EXPECTED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].observationDays").value(hasItem(DEFAULT_OBSERVATION_DAYS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].finalDecision").value(hasItem(DEFAULT_FINAL_DECISION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getDispatchAssignment() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get the dispatchAssignment
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, dispatchAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispatchAssignment.getId().intValue()))
            .andExpect(jsonPath("$.candidateId").value(DEFAULT_CANDIDATE_ID.intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.partnerOrganizationId").value(DEFAULT_PARTNER_ORGANIZATION_ID.intValue()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.positionId").value(DEFAULT_POSITION_ID.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.expectedEndDate").value(DEFAULT_EXPECTED_END_DATE.toString()))
            .andExpect(jsonPath("$.observationDays").value(DEFAULT_OBSERVATION_DAYS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.finalDecision").value(DEFAULT_FINAL_DECISION))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getDispatchAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        Long id = dispatchAssignment.getId();

        defaultDispatchAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDispatchAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDispatchAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId equals to
        defaultDispatchAssignmentFiltering("candidateId.equals=" + DEFAULT_CANDIDATE_ID, "candidateId.equals=" + UPDATED_CANDIDATE_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId in
        defaultDispatchAssignmentFiltering(
            "candidateId.in=" + DEFAULT_CANDIDATE_ID + "," + UPDATED_CANDIDATE_ID,
            "candidateId.in=" + UPDATED_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId is not null
        defaultDispatchAssignmentFiltering("candidateId.specified=true", "candidateId.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "candidateId.greaterThanOrEqual=" + DEFAULT_CANDIDATE_ID,
            "candidateId.greaterThanOrEqual=" + UPDATED_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId is less than or equal to
        defaultDispatchAssignmentFiltering(
            "candidateId.lessThanOrEqual=" + DEFAULT_CANDIDATE_ID,
            "candidateId.lessThanOrEqual=" + SMALLER_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId is less than
        defaultDispatchAssignmentFiltering("candidateId.lessThan=" + UPDATED_CANDIDATE_ID, "candidateId.lessThan=" + DEFAULT_CANDIDATE_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByCandidateIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where candidateId is greater than
        defaultDispatchAssignmentFiltering(
            "candidateId.greaterThan=" + SMALLER_CANDIDATE_ID,
            "candidateId.greaterThan=" + DEFAULT_CANDIDATE_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId equals to
        defaultDispatchAssignmentFiltering("personId.equals=" + DEFAULT_PERSON_ID, "personId.equals=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId in
        defaultDispatchAssignmentFiltering(
            "personId.in=" + DEFAULT_PERSON_ID + "," + UPDATED_PERSON_ID,
            "personId.in=" + UPDATED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId is not null
        defaultDispatchAssignmentFiltering("personId.specified=true", "personId.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "personId.greaterThanOrEqual=" + DEFAULT_PERSON_ID,
            "personId.greaterThanOrEqual=" + UPDATED_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId is less than or equal to
        defaultDispatchAssignmentFiltering(
            "personId.lessThanOrEqual=" + DEFAULT_PERSON_ID,
            "personId.lessThanOrEqual=" + SMALLER_PERSON_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId is less than
        defaultDispatchAssignmentFiltering("personId.lessThan=" + UPDATED_PERSON_ID, "personId.lessThan=" + DEFAULT_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPersonIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where personId is greater than
        defaultDispatchAssignmentFiltering("personId.greaterThan=" + SMALLER_PERSON_ID, "personId.greaterThan=" + DEFAULT_PERSON_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId equals to
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.equals=" + DEFAULT_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.equals=" + UPDATED_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId in
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.in=" + DEFAULT_PARTNER_ORGANIZATION_ID + "," + UPDATED_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.in=" + UPDATED_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId is not null
        defaultDispatchAssignmentFiltering("partnerOrganizationId.specified=true", "partnerOrganizationId.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.greaterThanOrEqual=" + DEFAULT_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.greaterThanOrEqual=" + UPDATED_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId is less than or equal to
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.lessThanOrEqual=" + DEFAULT_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.lessThanOrEqual=" + SMALLER_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId is less than
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.lessThan=" + UPDATED_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.lessThan=" + DEFAULT_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPartnerOrganizationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where partnerOrganizationId is greater than
        defaultDispatchAssignmentFiltering(
            "partnerOrganizationId.greaterThan=" + SMALLER_PARTNER_ORGANIZATION_ID,
            "partnerOrganizationId.greaterThan=" + DEFAULT_PARTNER_ORGANIZATION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where department equals to
        defaultDispatchAssignmentFiltering("department.equals=" + DEFAULT_DEPARTMENT, "department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where department in
        defaultDispatchAssignmentFiltering(
            "department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT,
            "department.in=" + UPDATED_DEPARTMENT
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where department is not null
        defaultDispatchAssignmentFiltering("department.specified=true", "department.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where department contains
        defaultDispatchAssignmentFiltering("department.contains=" + DEFAULT_DEPARTMENT, "department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where department does not contain
        defaultDispatchAssignmentFiltering(
            "department.doesNotContain=" + UPDATED_DEPARTMENT,
            "department.doesNotContain=" + DEFAULT_DEPARTMENT
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId equals to
        defaultDispatchAssignmentFiltering("positionId.equals=" + DEFAULT_POSITION_ID, "positionId.equals=" + UPDATED_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId in
        defaultDispatchAssignmentFiltering(
            "positionId.in=" + DEFAULT_POSITION_ID + "," + UPDATED_POSITION_ID,
            "positionId.in=" + UPDATED_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId is not null
        defaultDispatchAssignmentFiltering("positionId.specified=true", "positionId.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "positionId.greaterThanOrEqual=" + DEFAULT_POSITION_ID,
            "positionId.greaterThanOrEqual=" + UPDATED_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId is less than or equal to
        defaultDispatchAssignmentFiltering(
            "positionId.lessThanOrEqual=" + DEFAULT_POSITION_ID,
            "positionId.lessThanOrEqual=" + SMALLER_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId is less than
        defaultDispatchAssignmentFiltering("positionId.lessThan=" + UPDATED_POSITION_ID, "positionId.lessThan=" + DEFAULT_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByPositionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where positionId is greater than
        defaultDispatchAssignmentFiltering(
            "positionId.greaterThan=" + SMALLER_POSITION_ID,
            "positionId.greaterThan=" + DEFAULT_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate equals to
        defaultDispatchAssignmentFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate in
        defaultDispatchAssignmentFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate is not null
        defaultDispatchAssignmentFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate is less than or equal to
        defaultDispatchAssignmentFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate is less than
        defaultDispatchAssignmentFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where startDate is greater than
        defaultDispatchAssignmentFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate equals to
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.equals=" + DEFAULT_EXPECTED_END_DATE,
            "expectedEndDate.equals=" + UPDATED_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate in
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.in=" + DEFAULT_EXPECTED_END_DATE + "," + UPDATED_EXPECTED_END_DATE,
            "expectedEndDate.in=" + UPDATED_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate is not null
        defaultDispatchAssignmentFiltering("expectedEndDate.specified=true", "expectedEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.greaterThanOrEqual=" + DEFAULT_EXPECTED_END_DATE,
            "expectedEndDate.greaterThanOrEqual=" + UPDATED_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate is less than or equal to
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.lessThanOrEqual=" + DEFAULT_EXPECTED_END_DATE,
            "expectedEndDate.lessThanOrEqual=" + SMALLER_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate is less than
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.lessThan=" + UPDATED_EXPECTED_END_DATE,
            "expectedEndDate.lessThan=" + DEFAULT_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByExpectedEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where expectedEndDate is greater than
        defaultDispatchAssignmentFiltering(
            "expectedEndDate.greaterThan=" + SMALLER_EXPECTED_END_DATE,
            "expectedEndDate.greaterThan=" + DEFAULT_EXPECTED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays equals to
        defaultDispatchAssignmentFiltering(
            "observationDays.equals=" + DEFAULT_OBSERVATION_DAYS,
            "observationDays.equals=" + UPDATED_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays in
        defaultDispatchAssignmentFiltering(
            "observationDays.in=" + DEFAULT_OBSERVATION_DAYS + "," + UPDATED_OBSERVATION_DAYS,
            "observationDays.in=" + UPDATED_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays is not null
        defaultDispatchAssignmentFiltering("observationDays.specified=true", "observationDays.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays is greater than or equal to
        defaultDispatchAssignmentFiltering(
            "observationDays.greaterThanOrEqual=" + DEFAULT_OBSERVATION_DAYS,
            "observationDays.greaterThanOrEqual=" + UPDATED_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays is less than or equal to
        defaultDispatchAssignmentFiltering(
            "observationDays.lessThanOrEqual=" + DEFAULT_OBSERVATION_DAYS,
            "observationDays.lessThanOrEqual=" + SMALLER_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays is less than
        defaultDispatchAssignmentFiltering(
            "observationDays.lessThan=" + UPDATED_OBSERVATION_DAYS,
            "observationDays.lessThan=" + DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByObservationDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where observationDays is greater than
        defaultDispatchAssignmentFiltering(
            "observationDays.greaterThan=" + SMALLER_OBSERVATION_DAYS,
            "observationDays.greaterThan=" + DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where status equals to
        defaultDispatchAssignmentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where status in
        defaultDispatchAssignmentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where status is not null
        defaultDispatchAssignmentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where status contains
        defaultDispatchAssignmentFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where status does not contain
        defaultDispatchAssignmentFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByFinalDecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where finalDecision equals to
        defaultDispatchAssignmentFiltering(
            "finalDecision.equals=" + DEFAULT_FINAL_DECISION,
            "finalDecision.equals=" + UPDATED_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByFinalDecisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where finalDecision in
        defaultDispatchAssignmentFiltering(
            "finalDecision.in=" + DEFAULT_FINAL_DECISION + "," + UPDATED_FINAL_DECISION,
            "finalDecision.in=" + UPDATED_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByFinalDecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where finalDecision is not null
        defaultDispatchAssignmentFiltering("finalDecision.specified=true", "finalDecision.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByFinalDecisionContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where finalDecision contains
        defaultDispatchAssignmentFiltering(
            "finalDecision.contains=" + DEFAULT_FINAL_DECISION,
            "finalDecision.contains=" + UPDATED_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByFinalDecisionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        // Get all the dispatchAssignmentList where finalDecision does not contain
        defaultDispatchAssignmentFiltering(
            "finalDecision.doesNotContain=" + UPDATED_FINAL_DECISION,
            "finalDecision.doesNotContain=" + DEFAULT_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchAssignmentsByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        dispatchAssignment.setApplication(application);
        dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);
        Long applicationId = application.getId();
        // Get all the dispatchAssignmentList where application equals to applicationId
        defaultDispatchAssignmentShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the dispatchAssignmentList where application equals to (applicationId + 1)
        defaultDispatchAssignmentShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultDispatchAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDispatchAssignmentShouldBeFound(shouldBeFound);
        defaultDispatchAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDispatchAssignmentShouldBeFound(String filter) throws Exception {
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatchAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateId").value(hasItem(DEFAULT_CANDIDATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].partnerOrganizationId").value(hasItem(DEFAULT_PARTNER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedEndDate").value(hasItem(DEFAULT_EXPECTED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].observationDays").value(hasItem(DEFAULT_OBSERVATION_DAYS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].finalDecision").value(hasItem(DEFAULT_FINAL_DECISION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDispatchAssignmentShouldNotBeFound(String filter) throws Exception {
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDispatchAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDispatchAssignment() throws Exception {
        // Get the dispatchAssignment
        restDispatchAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDispatchAssignment() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchAssignment
        DispatchAssignment updatedDispatchAssignment = dispatchAssignmentRepository.findById(dispatchAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDispatchAssignment are not directly saved in db
        em.detach(updatedDispatchAssignment);
        updatedDispatchAssignment
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .partnerOrganizationId(UPDATED_PARTNER_ORGANIZATION_ID)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .startDate(UPDATED_START_DATE)
            .expectedEndDate(UPDATED_EXPECTED_END_DATE)
            .observationDays(UPDATED_OBSERVATION_DAYS)
            .status(UPDATED_STATUS)
            .finalDecision(UPDATED_FINAL_DECISION)
            .note(UPDATED_NOTE);
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(updatedDispatchAssignment);

        restDispatchAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatchAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDispatchAssignmentToMatchAllProperties(updatedDispatchAssignment);
    }

    @Test
    @Transactional
    void putNonExistingDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatchAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispatchAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchAssignment using partial update
        DispatchAssignment partialUpdatedDispatchAssignment = new DispatchAssignment();
        partialUpdatedDispatchAssignment.setId(dispatchAssignment.getId());

        partialUpdatedDispatchAssignment
            .personId(UPDATED_PERSON_ID)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .finalDecision(UPDATED_FINAL_DECISION);

        restDispatchAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispatchAssignment))
            )
            .andExpect(status().isOk());

        // Validate the DispatchAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispatchAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDispatchAssignment, dispatchAssignment),
            getPersistedDispatchAssignment(dispatchAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateDispatchAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchAssignment using partial update
        DispatchAssignment partialUpdatedDispatchAssignment = new DispatchAssignment();
        partialUpdatedDispatchAssignment.setId(dispatchAssignment.getId());

        partialUpdatedDispatchAssignment
            .candidateId(UPDATED_CANDIDATE_ID)
            .personId(UPDATED_PERSON_ID)
            .partnerOrganizationId(UPDATED_PARTNER_ORGANIZATION_ID)
            .department(UPDATED_DEPARTMENT)
            .positionId(UPDATED_POSITION_ID)
            .startDate(UPDATED_START_DATE)
            .expectedEndDate(UPDATED_EXPECTED_END_DATE)
            .observationDays(UPDATED_OBSERVATION_DAYS)
            .status(UPDATED_STATUS)
            .finalDecision(UPDATED_FINAL_DECISION)
            .note(UPDATED_NOTE);

        restDispatchAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispatchAssignment))
            )
            .andExpect(status().isOk());

        // Validate the DispatchAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispatchAssignmentUpdatableFieldsEquals(
            partialUpdatedDispatchAssignment,
            getPersistedDispatchAssignment(partialUpdatedDispatchAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispatchAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispatchAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispatchAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchAssignment.setId(longCount.incrementAndGet());

        // Create the DispatchAssignment
        DispatchAssignmentDTO dispatchAssignmentDTO = dispatchAssignmentMapper.toDto(dispatchAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dispatchAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DispatchAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispatchAssignment() throws Exception {
        // Initialize the database
        insertedDispatchAssignment = dispatchAssignmentRepository.saveAndFlush(dispatchAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dispatchAssignment
        restDispatchAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispatchAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dispatchAssignmentRepository.count();
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

    protected DispatchAssignment getPersistedDispatchAssignment(DispatchAssignment dispatchAssignment) {
        return dispatchAssignmentRepository.findById(dispatchAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedDispatchAssignmentToMatchAllProperties(DispatchAssignment expectedDispatchAssignment) {
        assertDispatchAssignmentAllPropertiesEquals(expectedDispatchAssignment, getPersistedDispatchAssignment(expectedDispatchAssignment));
    }

    protected void assertPersistedDispatchAssignmentToMatchUpdatableProperties(DispatchAssignment expectedDispatchAssignment) {
        assertDispatchAssignmentAllUpdatablePropertiesEquals(
            expectedDispatchAssignment,
            getPersistedDispatchAssignment(expectedDispatchAssignment)
        );
    }
}
