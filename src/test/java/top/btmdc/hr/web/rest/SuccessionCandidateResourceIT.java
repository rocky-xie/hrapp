package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SuccessionCandidateAsserts.*;
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
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.service.SuccessionCandidateService;
import top.btmdc.hr.service.dto.SuccessionCandidateDTO;
import top.btmdc.hr.service.mapper.SuccessionCandidateMapper;

/**
 * Integration tests for the {@link SuccessionCandidateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SuccessionCandidateResourceIT {

    private static final ReadinessLevel DEFAULT_SUCCESSION_READINESS = ReadinessLevel.IMMEDIATE;
    private static final ReadinessLevel UPDATED_SUCCESSION_READINESS = ReadinessLevel.THREE_MONTHS;

    private static final String DEFAULT_REQUIRED_TRAINING = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED_TRAINING = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATED_TIME_TO_READY = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATED_TIME_TO_READY = "BBBBBBBBBB";

    private static final RiskLevel DEFAULT_RISK_AFTER_TRAINING = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_AFTER_TRAINING = RiskLevel.MEDIUM;

    private static final LocalDate DEFAULT_REVIEW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVIEW_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REVIEW_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/succession-candidates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SuccessionCandidateRepository successionCandidateRepository;

    @Mock
    private SuccessionCandidateRepository successionCandidateRepositoryMock;

    @Autowired
    private SuccessionCandidateMapper successionCandidateMapper;

    @Mock
    private SuccessionCandidateService successionCandidateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuccessionCandidateMockMvc;

    private SuccessionCandidate successionCandidate;

    private SuccessionCandidate insertedSuccessionCandidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuccessionCandidate createEntity(EntityManager em) {
        SuccessionCandidate successionCandidate = new SuccessionCandidate()
            .successionReadiness(DEFAULT_SUCCESSION_READINESS)
            .requiredTraining(DEFAULT_REQUIRED_TRAINING)
            .estimatedTimeToReady(DEFAULT_ESTIMATED_TIME_TO_READY)
            .riskAfterTraining(DEFAULT_RISK_AFTER_TRAINING)
            .reviewDate(DEFAULT_REVIEW_DATE)
            .priority(DEFAULT_PRIORITY);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        successionCandidate.setPosition(position);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        successionCandidate.setCandidate(person);
        return successionCandidate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuccessionCandidate createUpdatedEntity(EntityManager em) {
        SuccessionCandidate updatedSuccessionCandidate = new SuccessionCandidate()
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .requiredTraining(UPDATED_REQUIRED_TRAINING)
            .estimatedTimeToReady(UPDATED_ESTIMATED_TIME_TO_READY)
            .riskAfterTraining(UPDATED_RISK_AFTER_TRAINING)
            .reviewDate(UPDATED_REVIEW_DATE)
            .priority(UPDATED_PRIORITY);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedSuccessionCandidate.setPosition(position);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedSuccessionCandidate.setCandidate(person);
        return updatedSuccessionCandidate;
    }

    @BeforeEach
    void initTest() {
        successionCandidate = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSuccessionCandidate != null) {
            successionCandidateRepository.delete(insertedSuccessionCandidate);
            insertedSuccessionCandidate = null;
        }
    }

    @Test
    @Transactional
    void createSuccessionCandidate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);
        var returnedSuccessionCandidateDTO = om.readValue(
            restSuccessionCandidateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(successionCandidateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SuccessionCandidateDTO.class
        );

        // Validate the SuccessionCandidate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSuccessionCandidate = successionCandidateMapper.toEntity(returnedSuccessionCandidateDTO);
        assertSuccessionCandidateUpdatableFieldsEquals(
            returnedSuccessionCandidate,
            getPersistedSuccessionCandidate(returnedSuccessionCandidate)
        );

        insertedSuccessionCandidate = returnedSuccessionCandidate;
    }

    @Test
    @Transactional
    void createSuccessionCandidateWithExistingId() throws Exception {
        // Create the SuccessionCandidate with an existing ID
        successionCandidate.setId(1L);
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuccessionCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(successionCandidateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSuccessionReadinessIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        successionCandidate.setSuccessionReadiness(null);

        // Create the SuccessionCandidate, which fails.
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        restSuccessionCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(successionCandidateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidates() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(successionCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].successionReadiness").value(hasItem(DEFAULT_SUCCESSION_READINESS.toString())))
            .andExpect(jsonPath("$.[*].requiredTraining").value(hasItem(DEFAULT_REQUIRED_TRAINING)))
            .andExpect(jsonPath("$.[*].estimatedTimeToReady").value(hasItem(DEFAULT_ESTIMATED_TIME_TO_READY)))
            .andExpect(jsonPath("$.[*].riskAfterTraining").value(hasItem(DEFAULT_RISK_AFTER_TRAINING.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuccessionCandidatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(successionCandidateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSuccessionCandidateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(successionCandidateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuccessionCandidatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(successionCandidateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSuccessionCandidateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(successionCandidateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSuccessionCandidate() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get the successionCandidate
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL_ID, successionCandidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(successionCandidate.getId().intValue()))
            .andExpect(jsonPath("$.successionReadiness").value(DEFAULT_SUCCESSION_READINESS.toString()))
            .andExpect(jsonPath("$.requiredTraining").value(DEFAULT_REQUIRED_TRAINING))
            .andExpect(jsonPath("$.estimatedTimeToReady").value(DEFAULT_ESTIMATED_TIME_TO_READY))
            .andExpect(jsonPath("$.riskAfterTraining").value(DEFAULT_RISK_AFTER_TRAINING.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getSuccessionCandidatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        Long id = successionCandidate.getId();

        defaultSuccessionCandidateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSuccessionCandidateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSuccessionCandidateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesBySuccessionReadinessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where successionReadiness equals to
        defaultSuccessionCandidateFiltering(
            "successionReadiness.equals=" + DEFAULT_SUCCESSION_READINESS,
            "successionReadiness.equals=" + UPDATED_SUCCESSION_READINESS
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesBySuccessionReadinessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where successionReadiness in
        defaultSuccessionCandidateFiltering(
            "successionReadiness.in=" + DEFAULT_SUCCESSION_READINESS + "," + UPDATED_SUCCESSION_READINESS,
            "successionReadiness.in=" + UPDATED_SUCCESSION_READINESS
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesBySuccessionReadinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where successionReadiness is not null
        defaultSuccessionCandidateFiltering("successionReadiness.specified=true", "successionReadiness.specified=false");
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByEstimatedTimeToReadyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where estimatedTimeToReady equals to
        defaultSuccessionCandidateFiltering(
            "estimatedTimeToReady.equals=" + DEFAULT_ESTIMATED_TIME_TO_READY,
            "estimatedTimeToReady.equals=" + UPDATED_ESTIMATED_TIME_TO_READY
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByEstimatedTimeToReadyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where estimatedTimeToReady in
        defaultSuccessionCandidateFiltering(
            "estimatedTimeToReady.in=" + DEFAULT_ESTIMATED_TIME_TO_READY + "," + UPDATED_ESTIMATED_TIME_TO_READY,
            "estimatedTimeToReady.in=" + UPDATED_ESTIMATED_TIME_TO_READY
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByEstimatedTimeToReadyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where estimatedTimeToReady is not null
        defaultSuccessionCandidateFiltering("estimatedTimeToReady.specified=true", "estimatedTimeToReady.specified=false");
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByEstimatedTimeToReadyContainsSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where estimatedTimeToReady contains
        defaultSuccessionCandidateFiltering(
            "estimatedTimeToReady.contains=" + DEFAULT_ESTIMATED_TIME_TO_READY,
            "estimatedTimeToReady.contains=" + UPDATED_ESTIMATED_TIME_TO_READY
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByEstimatedTimeToReadyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where estimatedTimeToReady does not contain
        defaultSuccessionCandidateFiltering(
            "estimatedTimeToReady.doesNotContain=" + UPDATED_ESTIMATED_TIME_TO_READY,
            "estimatedTimeToReady.doesNotContain=" + DEFAULT_ESTIMATED_TIME_TO_READY
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByRiskAfterTrainingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where riskAfterTraining equals to
        defaultSuccessionCandidateFiltering(
            "riskAfterTraining.equals=" + DEFAULT_RISK_AFTER_TRAINING,
            "riskAfterTraining.equals=" + UPDATED_RISK_AFTER_TRAINING
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByRiskAfterTrainingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where riskAfterTraining in
        defaultSuccessionCandidateFiltering(
            "riskAfterTraining.in=" + DEFAULT_RISK_AFTER_TRAINING + "," + UPDATED_RISK_AFTER_TRAINING,
            "riskAfterTraining.in=" + UPDATED_RISK_AFTER_TRAINING
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByRiskAfterTrainingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where riskAfterTraining is not null
        defaultSuccessionCandidateFiltering("riskAfterTraining.specified=true", "riskAfterTraining.specified=false");
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate equals to
        defaultSuccessionCandidateFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate in
        defaultSuccessionCandidateFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate is not null
        defaultSuccessionCandidateFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate is greater than or equal to
        defaultSuccessionCandidateFiltering(
            "reviewDate.greaterThanOrEqual=" + DEFAULT_REVIEW_DATE,
            "reviewDate.greaterThanOrEqual=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate is less than or equal to
        defaultSuccessionCandidateFiltering(
            "reviewDate.lessThanOrEqual=" + DEFAULT_REVIEW_DATE,
            "reviewDate.lessThanOrEqual=" + SMALLER_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate is less than
        defaultSuccessionCandidateFiltering("reviewDate.lessThan=" + UPDATED_REVIEW_DATE, "reviewDate.lessThan=" + DEFAULT_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByReviewDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where reviewDate is greater than
        defaultSuccessionCandidateFiltering(
            "reviewDate.greaterThan=" + SMALLER_REVIEW_DATE,
            "reviewDate.greaterThan=" + DEFAULT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority equals to
        defaultSuccessionCandidateFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority in
        defaultSuccessionCandidateFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority is not null
        defaultSuccessionCandidateFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority is greater than or equal to
        defaultSuccessionCandidateFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority is less than or equal to
        defaultSuccessionCandidateFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority is less than
        defaultSuccessionCandidateFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        // Get all the successionCandidateList where priority is greater than
        defaultSuccessionCandidateFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            successionCandidateRepository.saveAndFlush(successionCandidate);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        successionCandidate.setPosition(position);
        successionCandidateRepository.saveAndFlush(successionCandidate);
        Long positionId = position.getId();
        // Get all the successionCandidateList where position equals to positionId
        defaultSuccessionCandidateShouldBeFound("positionId.equals=" + positionId);

        // Get all the successionCandidateList where position equals to (positionId + 1)
        defaultSuccessionCandidateShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByCurrentOwnerIsEqualToSomething() throws Exception {
        Person currentOwner;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            successionCandidateRepository.saveAndFlush(successionCandidate);
            currentOwner = PersonResourceIT.createEntity();
        } else {
            currentOwner = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(currentOwner);
        em.flush();
        successionCandidate.setCurrentOwner(currentOwner);
        successionCandidateRepository.saveAndFlush(successionCandidate);
        Long currentOwnerId = currentOwner.getId();
        // Get all the successionCandidateList where currentOwner equals to currentOwnerId
        defaultSuccessionCandidateShouldBeFound("currentOwnerId.equals=" + currentOwnerId);

        // Get all the successionCandidateList where currentOwner equals to (currentOwnerId + 1)
        defaultSuccessionCandidateShouldNotBeFound("currentOwnerId.equals=" + (currentOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllSuccessionCandidatesByCandidateIsEqualToSomething() throws Exception {
        Person candidate;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            successionCandidateRepository.saveAndFlush(successionCandidate);
            candidate = PersonResourceIT.createEntity();
        } else {
            candidate = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(candidate);
        em.flush();
        successionCandidate.setCandidate(candidate);
        successionCandidateRepository.saveAndFlush(successionCandidate);
        Long candidateId = candidate.getId();
        // Get all the successionCandidateList where candidate equals to candidateId
        defaultSuccessionCandidateShouldBeFound("candidateId.equals=" + candidateId);

        // Get all the successionCandidateList where candidate equals to (candidateId + 1)
        defaultSuccessionCandidateShouldNotBeFound("candidateId.equals=" + (candidateId + 1));
    }

    private void defaultSuccessionCandidateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSuccessionCandidateShouldBeFound(shouldBeFound);
        defaultSuccessionCandidateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuccessionCandidateShouldBeFound(String filter) throws Exception {
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(successionCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].successionReadiness").value(hasItem(DEFAULT_SUCCESSION_READINESS.toString())))
            .andExpect(jsonPath("$.[*].requiredTraining").value(hasItem(DEFAULT_REQUIRED_TRAINING)))
            .andExpect(jsonPath("$.[*].estimatedTimeToReady").value(hasItem(DEFAULT_ESTIMATED_TIME_TO_READY)))
            .andExpect(jsonPath("$.[*].riskAfterTraining").value(hasItem(DEFAULT_RISK_AFTER_TRAINING.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));

        // Check, that the count call also returns 1
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuccessionCandidateShouldNotBeFound(String filter) throws Exception {
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuccessionCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuccessionCandidate() throws Exception {
        // Get the successionCandidate
        restSuccessionCandidateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSuccessionCandidate() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the successionCandidate
        SuccessionCandidate updatedSuccessionCandidate = successionCandidateRepository.findById(successionCandidate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSuccessionCandidate are not directly saved in db
        em.detach(updatedSuccessionCandidate);
        updatedSuccessionCandidate
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .requiredTraining(UPDATED_REQUIRED_TRAINING)
            .estimatedTimeToReady(UPDATED_ESTIMATED_TIME_TO_READY)
            .riskAfterTraining(UPDATED_RISK_AFTER_TRAINING)
            .reviewDate(UPDATED_REVIEW_DATE)
            .priority(UPDATED_PRIORITY);
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(updatedSuccessionCandidate);

        restSuccessionCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, successionCandidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isOk());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSuccessionCandidateToMatchAllProperties(updatedSuccessionCandidate);
    }

    @Test
    @Transactional
    void putNonExistingSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, successionCandidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(successionCandidateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuccessionCandidateWithPatch() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the successionCandidate using partial update
        SuccessionCandidate partialUpdatedSuccessionCandidate = new SuccessionCandidate();
        partialUpdatedSuccessionCandidate.setId(successionCandidate.getId());

        partialUpdatedSuccessionCandidate
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .requiredTraining(UPDATED_REQUIRED_TRAINING)
            .riskAfterTraining(UPDATED_RISK_AFTER_TRAINING);

        restSuccessionCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuccessionCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuccessionCandidate))
            )
            .andExpect(status().isOk());

        // Validate the SuccessionCandidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuccessionCandidateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSuccessionCandidate, successionCandidate),
            getPersistedSuccessionCandidate(successionCandidate)
        );
    }

    @Test
    @Transactional
    void fullUpdateSuccessionCandidateWithPatch() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the successionCandidate using partial update
        SuccessionCandidate partialUpdatedSuccessionCandidate = new SuccessionCandidate();
        partialUpdatedSuccessionCandidate.setId(successionCandidate.getId());

        partialUpdatedSuccessionCandidate
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .requiredTraining(UPDATED_REQUIRED_TRAINING)
            .estimatedTimeToReady(UPDATED_ESTIMATED_TIME_TO_READY)
            .riskAfterTraining(UPDATED_RISK_AFTER_TRAINING)
            .reviewDate(UPDATED_REVIEW_DATE)
            .priority(UPDATED_PRIORITY);

        restSuccessionCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuccessionCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuccessionCandidate))
            )
            .andExpect(status().isOk());

        // Validate the SuccessionCandidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuccessionCandidateUpdatableFieldsEquals(
            partialUpdatedSuccessionCandidate,
            getPersistedSuccessionCandidate(partialUpdatedSuccessionCandidate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, successionCandidateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuccessionCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        successionCandidate.setId(longCount.incrementAndGet());

        // Create the SuccessionCandidate
        SuccessionCandidateDTO successionCandidateDTO = successionCandidateMapper.toDto(successionCandidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuccessionCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(successionCandidateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuccessionCandidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuccessionCandidate() throws Exception {
        // Initialize the database
        insertedSuccessionCandidate = successionCandidateRepository.saveAndFlush(successionCandidate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the successionCandidate
        restSuccessionCandidateMockMvc
            .perform(delete(ENTITY_API_URL_ID, successionCandidate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return successionCandidateRepository.count();
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

    protected SuccessionCandidate getPersistedSuccessionCandidate(SuccessionCandidate successionCandidate) {
        return successionCandidateRepository.findById(successionCandidate.getId()).orElseThrow();
    }

    protected void assertPersistedSuccessionCandidateToMatchAllProperties(SuccessionCandidate expectedSuccessionCandidate) {
        assertSuccessionCandidateAllPropertiesEquals(
            expectedSuccessionCandidate,
            getPersistedSuccessionCandidate(expectedSuccessionCandidate)
        );
    }

    protected void assertPersistedSuccessionCandidateToMatchUpdatableProperties(SuccessionCandidate expectedSuccessionCandidate) {
        assertSuccessionCandidateAllUpdatablePropertiesEquals(
            expectedSuccessionCandidate,
            getPersistedSuccessionCandidate(expectedSuccessionCandidate)
        );
    }
}
