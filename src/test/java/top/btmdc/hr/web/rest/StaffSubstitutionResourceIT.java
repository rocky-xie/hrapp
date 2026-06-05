package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.StaffSubstitutionAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static top.btmdc.hr.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.StaffSubstitutionService;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;
import top.btmdc.hr.service.mapper.StaffSubstitutionMapper;

/**
 * Integration tests for the {@link StaffSubstitutionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StaffSubstitutionResourceIT {

    private static final BigDecimal DEFAULT_COVERAGE_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_COVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal SMALLER_COVERAGE_RATE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_THRESHOLD_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_THRESHOLD_RATE = new BigDecimal(1);
    private static final BigDecimal SMALLER_THRESHOLD_RATE = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_TOTAL_SKILL_COUNT = 0;
    private static final Integer UPDATED_TOTAL_SKILL_COUNT = 1;
    private static final Integer SMALLER_TOTAL_SKILL_COUNT = 0 - 1;

    private static final Integer DEFAULT_COVERED_SKILL_COUNT = 0;
    private static final Integer UPDATED_COVERED_SKILL_COUNT = 1;
    private static final Integer SMALLER_COVERED_SKILL_COUNT = 0 - 1;

    private static final String DEFAULT_MISSING_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_MISSING_SKILLS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUBSTITUTABLE = false;
    private static final Boolean UPDATED_SUBSTITUTABLE = true;

    private static final LocalDate DEFAULT_EVALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EVALUATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/staff-substitutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StaffSubstitutionRepository staffSubstitutionRepository;

    @Mock
    private StaffSubstitutionRepository staffSubstitutionRepositoryMock;

    @Autowired
    private StaffSubstitutionMapper staffSubstitutionMapper;

    @Mock
    private StaffSubstitutionService staffSubstitutionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffSubstitutionMockMvc;

    private StaffSubstitution staffSubstitution;

    private StaffSubstitution insertedStaffSubstitution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffSubstitution createEntity(EntityManager em) {
        StaffSubstitution staffSubstitution = new StaffSubstitution()
            .coverageRate(DEFAULT_COVERAGE_RATE)
            .thresholdRate(DEFAULT_THRESHOLD_RATE)
            .totalSkillCount(DEFAULT_TOTAL_SKILL_COUNT)
            .coveredSkillCount(DEFAULT_COVERED_SKILL_COUNT)
            .missingSkills(DEFAULT_MISSING_SKILLS)
            .substitutable(DEFAULT_SUBSTITUTABLE)
            .evaluationDate(DEFAULT_EVALUATION_DATE)
            .reason(DEFAULT_REASON);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        staffSubstitution.setPosition(position);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        staffSubstitution.setCandidatePerson(person);
        return staffSubstitution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffSubstitution createUpdatedEntity(EntityManager em) {
        StaffSubstitution updatedStaffSubstitution = new StaffSubstitution()
            .coverageRate(UPDATED_COVERAGE_RATE)
            .thresholdRate(UPDATED_THRESHOLD_RATE)
            .totalSkillCount(UPDATED_TOTAL_SKILL_COUNT)
            .coveredSkillCount(UPDATED_COVERED_SKILL_COUNT)
            .missingSkills(UPDATED_MISSING_SKILLS)
            .substitutable(UPDATED_SUBSTITUTABLE)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .reason(UPDATED_REASON);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedStaffSubstitution.setPosition(position);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedStaffSubstitution.setCandidatePerson(person);
        return updatedStaffSubstitution;
    }

    @BeforeEach
    void initTest() {
        staffSubstitution = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedStaffSubstitution != null) {
            staffSubstitutionRepository.delete(insertedStaffSubstitution);
            insertedStaffSubstitution = null;
        }
    }

    @Test
    @Transactional
    void createStaffSubstitution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);
        var returnedStaffSubstitutionDTO = om.readValue(
            restStaffSubstitutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StaffSubstitutionDTO.class
        );

        // Validate the StaffSubstitution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStaffSubstitution = staffSubstitutionMapper.toEntity(returnedStaffSubstitutionDTO);
        assertStaffSubstitutionUpdatableFieldsEquals(returnedStaffSubstitution, getPersistedStaffSubstitution(returnedStaffSubstitution));

        insertedStaffSubstitution = returnedStaffSubstitution;
    }

    @Test
    @Transactional
    void createStaffSubstitutionWithExistingId() throws Exception {
        // Create the StaffSubstitution with an existing ID
        staffSubstitution.setId(1L);
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffSubstitutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoverageRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staffSubstitution.setCoverageRate(null);

        // Create the StaffSubstitution, which fails.
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        restStaffSubstitutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkThresholdRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staffSubstitution.setThresholdRate(null);

        // Create the StaffSubstitution, which fails.
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        restStaffSubstitutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubstitutableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staffSubstitution.setSubstitutable(null);

        // Create the StaffSubstitution, which fails.
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        restStaffSubstitutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEvaluationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staffSubstitution.setEvaluationDate(null);

        // Create the StaffSubstitution, which fails.
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        restStaffSubstitutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaffSubstitutions() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffSubstitution.getId().intValue())))
            .andExpect(jsonPath("$.[*].coverageRate").value(hasItem(sameNumber(DEFAULT_COVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].thresholdRate").value(hasItem(sameNumber(DEFAULT_THRESHOLD_RATE))))
            .andExpect(jsonPath("$.[*].totalSkillCount").value(hasItem(DEFAULT_TOTAL_SKILL_COUNT)))
            .andExpect(jsonPath("$.[*].coveredSkillCount").value(hasItem(DEFAULT_COVERED_SKILL_COUNT)))
            .andExpect(jsonPath("$.[*].missingSkills").value(hasItem(DEFAULT_MISSING_SKILLS)))
            .andExpect(jsonPath("$.[*].substitutable").value(hasItem(DEFAULT_SUBSTITUTABLE)))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffSubstitutionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(staffSubstitutionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffSubstitutionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staffSubstitutionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffSubstitutionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(staffSubstitutionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffSubstitutionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(staffSubstitutionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStaffSubstitution() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get the staffSubstitution
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL_ID, staffSubstitution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffSubstitution.getId().intValue()))
            .andExpect(jsonPath("$.coverageRate").value(sameNumber(DEFAULT_COVERAGE_RATE)))
            .andExpect(jsonPath("$.thresholdRate").value(sameNumber(DEFAULT_THRESHOLD_RATE)))
            .andExpect(jsonPath("$.totalSkillCount").value(DEFAULT_TOTAL_SKILL_COUNT))
            .andExpect(jsonPath("$.coveredSkillCount").value(DEFAULT_COVERED_SKILL_COUNT))
            .andExpect(jsonPath("$.missingSkills").value(DEFAULT_MISSING_SKILLS))
            .andExpect(jsonPath("$.substitutable").value(DEFAULT_SUBSTITUTABLE))
            .andExpect(jsonPath("$.evaluationDate").value(DEFAULT_EVALUATION_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getStaffSubstitutionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        Long id = staffSubstitution.getId();

        defaultStaffSubstitutionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStaffSubstitutionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStaffSubstitutionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate equals to
        defaultStaffSubstitutionFiltering("coverageRate.equals=" + DEFAULT_COVERAGE_RATE, "coverageRate.equals=" + UPDATED_COVERAGE_RATE);
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate in
        defaultStaffSubstitutionFiltering(
            "coverageRate.in=" + DEFAULT_COVERAGE_RATE + "," + UPDATED_COVERAGE_RATE,
            "coverageRate.in=" + UPDATED_COVERAGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate is not null
        defaultStaffSubstitutionFiltering("coverageRate.specified=true", "coverageRate.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate is greater than or equal to
        defaultStaffSubstitutionFiltering(
            "coverageRate.greaterThanOrEqual=" + DEFAULT_COVERAGE_RATE,
            "coverageRate.greaterThanOrEqual=" + (DEFAULT_COVERAGE_RATE.add(BigDecimal.ONE))
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate is less than or equal to
        defaultStaffSubstitutionFiltering(
            "coverageRate.lessThanOrEqual=" + DEFAULT_COVERAGE_RATE,
            "coverageRate.lessThanOrEqual=" + SMALLER_COVERAGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate is less than
        defaultStaffSubstitutionFiltering(
            "coverageRate.lessThan=" + (DEFAULT_COVERAGE_RATE.add(BigDecimal.ONE)),
            "coverageRate.lessThan=" + DEFAULT_COVERAGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoverageRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coverageRate is greater than
        defaultStaffSubstitutionFiltering(
            "coverageRate.greaterThan=" + SMALLER_COVERAGE_RATE,
            "coverageRate.greaterThan=" + DEFAULT_COVERAGE_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate equals to
        defaultStaffSubstitutionFiltering(
            "thresholdRate.equals=" + DEFAULT_THRESHOLD_RATE,
            "thresholdRate.equals=" + UPDATED_THRESHOLD_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate in
        defaultStaffSubstitutionFiltering(
            "thresholdRate.in=" + DEFAULT_THRESHOLD_RATE + "," + UPDATED_THRESHOLD_RATE,
            "thresholdRate.in=" + UPDATED_THRESHOLD_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate is not null
        defaultStaffSubstitutionFiltering("thresholdRate.specified=true", "thresholdRate.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate is greater than or equal to
        defaultStaffSubstitutionFiltering(
            "thresholdRate.greaterThanOrEqual=" + DEFAULT_THRESHOLD_RATE,
            "thresholdRate.greaterThanOrEqual=" + (DEFAULT_THRESHOLD_RATE.add(BigDecimal.ONE))
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate is less than or equal to
        defaultStaffSubstitutionFiltering(
            "thresholdRate.lessThanOrEqual=" + DEFAULT_THRESHOLD_RATE,
            "thresholdRate.lessThanOrEqual=" + SMALLER_THRESHOLD_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate is less than
        defaultStaffSubstitutionFiltering(
            "thresholdRate.lessThan=" + (DEFAULT_THRESHOLD_RATE.add(BigDecimal.ONE)),
            "thresholdRate.lessThan=" + DEFAULT_THRESHOLD_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByThresholdRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where thresholdRate is greater than
        defaultStaffSubstitutionFiltering(
            "thresholdRate.greaterThan=" + SMALLER_THRESHOLD_RATE,
            "thresholdRate.greaterThan=" + DEFAULT_THRESHOLD_RATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount equals to
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.equals=" + DEFAULT_TOTAL_SKILL_COUNT,
            "totalSkillCount.equals=" + UPDATED_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount in
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.in=" + DEFAULT_TOTAL_SKILL_COUNT + "," + UPDATED_TOTAL_SKILL_COUNT,
            "totalSkillCount.in=" + UPDATED_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount is not null
        defaultStaffSubstitutionFiltering("totalSkillCount.specified=true", "totalSkillCount.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount is greater than or equal to
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.greaterThanOrEqual=" + DEFAULT_TOTAL_SKILL_COUNT,
            "totalSkillCount.greaterThanOrEqual=" + UPDATED_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount is less than or equal to
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.lessThanOrEqual=" + DEFAULT_TOTAL_SKILL_COUNT,
            "totalSkillCount.lessThanOrEqual=" + SMALLER_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount is less than
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.lessThan=" + UPDATED_TOTAL_SKILL_COUNT,
            "totalSkillCount.lessThan=" + DEFAULT_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByTotalSkillCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where totalSkillCount is greater than
        defaultStaffSubstitutionFiltering(
            "totalSkillCount.greaterThan=" + SMALLER_TOTAL_SKILL_COUNT,
            "totalSkillCount.greaterThan=" + DEFAULT_TOTAL_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount equals to
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.equals=" + DEFAULT_COVERED_SKILL_COUNT,
            "coveredSkillCount.equals=" + UPDATED_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount in
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.in=" + DEFAULT_COVERED_SKILL_COUNT + "," + UPDATED_COVERED_SKILL_COUNT,
            "coveredSkillCount.in=" + UPDATED_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount is not null
        defaultStaffSubstitutionFiltering("coveredSkillCount.specified=true", "coveredSkillCount.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount is greater than or equal to
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.greaterThanOrEqual=" + DEFAULT_COVERED_SKILL_COUNT,
            "coveredSkillCount.greaterThanOrEqual=" + UPDATED_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount is less than or equal to
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.lessThanOrEqual=" + DEFAULT_COVERED_SKILL_COUNT,
            "coveredSkillCount.lessThanOrEqual=" + SMALLER_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount is less than
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.lessThan=" + UPDATED_COVERED_SKILL_COUNT,
            "coveredSkillCount.lessThan=" + DEFAULT_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCoveredSkillCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where coveredSkillCount is greater than
        defaultStaffSubstitutionFiltering(
            "coveredSkillCount.greaterThan=" + SMALLER_COVERED_SKILL_COUNT,
            "coveredSkillCount.greaterThan=" + DEFAULT_COVERED_SKILL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsBySubstitutableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where substitutable equals to
        defaultStaffSubstitutionFiltering("substitutable.equals=" + DEFAULT_SUBSTITUTABLE, "substitutable.equals=" + UPDATED_SUBSTITUTABLE);
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsBySubstitutableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where substitutable in
        defaultStaffSubstitutionFiltering(
            "substitutable.in=" + DEFAULT_SUBSTITUTABLE + "," + UPDATED_SUBSTITUTABLE,
            "substitutable.in=" + UPDATED_SUBSTITUTABLE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsBySubstitutableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where substitutable is not null
        defaultStaffSubstitutionFiltering("substitutable.specified=true", "substitutable.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate equals to
        defaultStaffSubstitutionFiltering(
            "evaluationDate.equals=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.equals=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate in
        defaultStaffSubstitutionFiltering(
            "evaluationDate.in=" + DEFAULT_EVALUATION_DATE + "," + UPDATED_EVALUATION_DATE,
            "evaluationDate.in=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate is not null
        defaultStaffSubstitutionFiltering("evaluationDate.specified=true", "evaluationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate is greater than or equal to
        defaultStaffSubstitutionFiltering(
            "evaluationDate.greaterThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.greaterThanOrEqual=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate is less than or equal to
        defaultStaffSubstitutionFiltering(
            "evaluationDate.lessThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.lessThanOrEqual=" + SMALLER_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate is less than
        defaultStaffSubstitutionFiltering(
            "evaluationDate.lessThan=" + UPDATED_EVALUATION_DATE,
            "evaluationDate.lessThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByEvaluationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        // Get all the staffSubstitutionList where evaluationDate is greater than
        defaultStaffSubstitutionFiltering(
            "evaluationDate.greaterThan=" + SMALLER_EVALUATION_DATE,
            "evaluationDate.greaterThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            staffSubstitutionRepository.saveAndFlush(staffSubstitution);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        staffSubstitution.setPosition(position);
        staffSubstitutionRepository.saveAndFlush(staffSubstitution);
        Long positionId = position.getId();
        // Get all the staffSubstitutionList where position equals to positionId
        defaultStaffSubstitutionShouldBeFound("positionId.equals=" + positionId);

        // Get all the staffSubstitutionList where position equals to (positionId + 1)
        defaultStaffSubstitutionShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllStaffSubstitutionsByCandidatePersonIsEqualToSomething() throws Exception {
        Person candidatePerson;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            staffSubstitutionRepository.saveAndFlush(staffSubstitution);
            candidatePerson = PersonResourceIT.createEntity();
        } else {
            candidatePerson = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(candidatePerson);
        em.flush();
        staffSubstitution.setCandidatePerson(candidatePerson);
        staffSubstitutionRepository.saveAndFlush(staffSubstitution);
        Long candidatePersonId = candidatePerson.getId();
        // Get all the staffSubstitutionList where candidatePerson equals to candidatePersonId
        defaultStaffSubstitutionShouldBeFound("candidatePersonId.equals=" + candidatePersonId);

        // Get all the staffSubstitutionList where candidatePerson equals to (candidatePersonId + 1)
        defaultStaffSubstitutionShouldNotBeFound("candidatePersonId.equals=" + (candidatePersonId + 1));
    }

    private void defaultStaffSubstitutionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStaffSubstitutionShouldBeFound(shouldBeFound);
        defaultStaffSubstitutionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaffSubstitutionShouldBeFound(String filter) throws Exception {
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffSubstitution.getId().intValue())))
            .andExpect(jsonPath("$.[*].coverageRate").value(hasItem(sameNumber(DEFAULT_COVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].thresholdRate").value(hasItem(sameNumber(DEFAULT_THRESHOLD_RATE))))
            .andExpect(jsonPath("$.[*].totalSkillCount").value(hasItem(DEFAULT_TOTAL_SKILL_COUNT)))
            .andExpect(jsonPath("$.[*].coveredSkillCount").value(hasItem(DEFAULT_COVERED_SKILL_COUNT)))
            .andExpect(jsonPath("$.[*].missingSkills").value(hasItem(DEFAULT_MISSING_SKILLS)))
            .andExpect(jsonPath("$.[*].substitutable").value(hasItem(DEFAULT_SUBSTITUTABLE)))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));

        // Check, that the count call also returns 1
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaffSubstitutionShouldNotBeFound(String filter) throws Exception {
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaffSubstitutionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStaffSubstitution() throws Exception {
        // Get the staffSubstitution
        restStaffSubstitutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStaffSubstitution() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staffSubstitution
        StaffSubstitution updatedStaffSubstitution = staffSubstitutionRepository.findById(staffSubstitution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStaffSubstitution are not directly saved in db
        em.detach(updatedStaffSubstitution);
        updatedStaffSubstitution
            .coverageRate(UPDATED_COVERAGE_RATE)
            .thresholdRate(UPDATED_THRESHOLD_RATE)
            .totalSkillCount(UPDATED_TOTAL_SKILL_COUNT)
            .coveredSkillCount(UPDATED_COVERED_SKILL_COUNT)
            .missingSkills(UPDATED_MISSING_SKILLS)
            .substitutable(UPDATED_SUBSTITUTABLE)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .reason(UPDATED_REASON);
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(updatedStaffSubstitution);

        restStaffSubstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffSubstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staffSubstitutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStaffSubstitutionToMatchAllProperties(updatedStaffSubstitution);
    }

    @Test
    @Transactional
    void putNonExistingStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffSubstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staffSubstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staffSubstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaffSubstitutionWithPatch() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staffSubstitution using partial update
        StaffSubstitution partialUpdatedStaffSubstitution = new StaffSubstitution();
        partialUpdatedStaffSubstitution.setId(staffSubstitution.getId());

        partialUpdatedStaffSubstitution
            .coverageRate(UPDATED_COVERAGE_RATE)
            .thresholdRate(UPDATED_THRESHOLD_RATE)
            .coveredSkillCount(UPDATED_COVERED_SKILL_COUNT)
            .evaluationDate(UPDATED_EVALUATION_DATE);

        restStaffSubstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffSubstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaffSubstitution))
            )
            .andExpect(status().isOk());

        // Validate the StaffSubstitution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaffSubstitutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStaffSubstitution, staffSubstitution),
            getPersistedStaffSubstitution(staffSubstitution)
        );
    }

    @Test
    @Transactional
    void fullUpdateStaffSubstitutionWithPatch() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staffSubstitution using partial update
        StaffSubstitution partialUpdatedStaffSubstitution = new StaffSubstitution();
        partialUpdatedStaffSubstitution.setId(staffSubstitution.getId());

        partialUpdatedStaffSubstitution
            .coverageRate(UPDATED_COVERAGE_RATE)
            .thresholdRate(UPDATED_THRESHOLD_RATE)
            .totalSkillCount(UPDATED_TOTAL_SKILL_COUNT)
            .coveredSkillCount(UPDATED_COVERED_SKILL_COUNT)
            .missingSkills(UPDATED_MISSING_SKILLS)
            .substitutable(UPDATED_SUBSTITUTABLE)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .reason(UPDATED_REASON);

        restStaffSubstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffSubstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaffSubstitution))
            )
            .andExpect(status().isOk());

        // Validate the StaffSubstitution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaffSubstitutionUpdatableFieldsEquals(
            partialUpdatedStaffSubstitution,
            getPersistedStaffSubstitution(partialUpdatedStaffSubstitution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffSubstitutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staffSubstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staffSubstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaffSubstitution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staffSubstitution.setId(longCount.incrementAndGet());

        // Create the StaffSubstitution
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionMapper.toDto(staffSubstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffSubstitutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(staffSubstitutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffSubstitution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaffSubstitution() throws Exception {
        // Initialize the database
        insertedStaffSubstitution = staffSubstitutionRepository.saveAndFlush(staffSubstitution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the staffSubstitution
        restStaffSubstitutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, staffSubstitution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return staffSubstitutionRepository.count();
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

    protected StaffSubstitution getPersistedStaffSubstitution(StaffSubstitution staffSubstitution) {
        return staffSubstitutionRepository.findById(staffSubstitution.getId()).orElseThrow();
    }

    protected void assertPersistedStaffSubstitutionToMatchAllProperties(StaffSubstitution expectedStaffSubstitution) {
        assertStaffSubstitutionAllPropertiesEquals(expectedStaffSubstitution, getPersistedStaffSubstitution(expectedStaffSubstitution));
    }

    protected void assertPersistedStaffSubstitutionToMatchUpdatableProperties(StaffSubstitution expectedStaffSubstitution) {
        assertStaffSubstitutionAllUpdatablePropertiesEquals(
            expectedStaffSubstitution,
            getPersistedStaffSubstitution(expectedStaffSubstitution)
        );
    }
}
