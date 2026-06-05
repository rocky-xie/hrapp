package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionRiskEvaluationAsserts.*;
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
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.service.PositionRiskEvaluationService;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.service.mapper.PositionRiskEvaluationMapper;

/**
 * Integration tests for the {@link PositionRiskEvaluationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PositionRiskEvaluationResourceIT {

    private static final LocalDate DEFAULT_EVALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EVALUATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_OWNER_COUNT = 0;
    private static final Integer UPDATED_OWNER_COUNT = 1;
    private static final Integer SMALLER_OWNER_COUNT = 0 - 1;

    private static final Integer DEFAULT_SUBSTITUTABLE_OWNER_COUNT = 0;
    private static final Integer UPDATED_SUBSTITUTABLE_OWNER_COUNT = 1;
    private static final Integer SMALLER_SUBSTITUTABLE_OWNER_COUNT = 0 - 1;

    private static final Boolean DEFAULT_HAS_SUBSTITUTE = false;
    private static final Boolean UPDATED_HAS_SUBSTITUTE = true;

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.AVAILABLE;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.PARTIAL;

    private static final ImportanceLevel DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY = ImportanceLevel.MEDIUM;

    private static final ReadinessLevel DEFAULT_SUCCESSION_READINESS = ReadinessLevel.IMMEDIATE;
    private static final ReadinessLevel UPDATED_SUCCESSION_READINESS = ReadinessLevel.THREE_MONTHS;

    private static final RiskLevel DEFAULT_RISK_LEVEL = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_LEVEL = RiskLevel.MEDIUM;

    private static final String DEFAULT_RISK_REASON = "AAAAAAAAAA";
    private static final String UPDATED_RISK_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_RECOMMENDED_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_RECOMMENDED_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/position-risk-evaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    @Mock
    private PositionRiskEvaluationRepository positionRiskEvaluationRepositoryMock;

    @Autowired
    private PositionRiskEvaluationMapper positionRiskEvaluationMapper;

    @Mock
    private PositionRiskEvaluationService positionRiskEvaluationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionRiskEvaluationMockMvc;

    private PositionRiskEvaluation positionRiskEvaluation;

    private PositionRiskEvaluation insertedPositionRiskEvaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionRiskEvaluation createEntity(EntityManager em) {
        PositionRiskEvaluation positionRiskEvaluation = new PositionRiskEvaluation()
            .evaluationDate(DEFAULT_EVALUATION_DATE)
            .ownerCount(DEFAULT_OWNER_COUNT)
            .substitutableOwnerCount(DEFAULT_SUBSTITUTABLE_OWNER_COUNT)
            .hasSubstitute(DEFAULT_HAS_SUBSTITUTE)
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .customerOrSystemDependency(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .successionReadiness(DEFAULT_SUCCESSION_READINESS)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .riskReason(DEFAULT_RISK_REASON)
            .recommendedAction(DEFAULT_RECOMMENDED_ACTION);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        positionRiskEvaluation.setPosition(position);
        return positionRiskEvaluation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionRiskEvaluation createUpdatedEntity(EntityManager em) {
        PositionRiskEvaluation updatedPositionRiskEvaluation = new PositionRiskEvaluation()
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .ownerCount(UPDATED_OWNER_COUNT)
            .substitutableOwnerCount(UPDATED_SUBSTITUTABLE_OWNER_COUNT)
            .hasSubstitute(UPDATED_HAS_SUBSTITUTE)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskReason(UPDATED_RISK_REASON)
            .recommendedAction(UPDATED_RECOMMENDED_ACTION);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedPositionRiskEvaluation.setPosition(position);
        return updatedPositionRiskEvaluation;
    }

    @BeforeEach
    void initTest() {
        positionRiskEvaluation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPositionRiskEvaluation != null) {
            positionRiskEvaluationRepository.delete(insertedPositionRiskEvaluation);
            insertedPositionRiskEvaluation = null;
        }
    }

    @Test
    @Transactional
    void createPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);
        var returnedPositionRiskEvaluationDTO = om.readValue(
            restPositionRiskEvaluationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionRiskEvaluationDTO.class
        );

        // Validate the PositionRiskEvaluation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPositionRiskEvaluation = positionRiskEvaluationMapper.toEntity(returnedPositionRiskEvaluationDTO);
        assertPositionRiskEvaluationUpdatableFieldsEquals(
            returnedPositionRiskEvaluation,
            getPersistedPositionRiskEvaluation(returnedPositionRiskEvaluation)
        );

        insertedPositionRiskEvaluation = returnedPositionRiskEvaluation;
    }

    @Test
    @Transactional
    void createPositionRiskEvaluationWithExistingId() throws Exception {
        // Create the PositionRiskEvaluation with an existing ID
        positionRiskEvaluation.setId(1L);
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionRiskEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEvaluationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRiskEvaluation.setEvaluationDate(null);

        // Create the PositionRiskEvaluation, which fails.
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        restPositionRiskEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasSubstituteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRiskEvaluation.setHasSubstitute(null);

        // Create the PositionRiskEvaluation, which fails.
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        restPositionRiskEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRiskLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRiskEvaluation.setRiskLevel(null);

        // Create the PositionRiskEvaluation, which fails.
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        restPositionRiskEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluations() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionRiskEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].ownerCount").value(hasItem(DEFAULT_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].substitutableOwnerCount").value(hasItem(DEFAULT_SUBSTITUTABLE_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].hasSubstitute").value(hasItem(DEFAULT_HAS_SUBSTITUTE)))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerOrSystemDependency").value(hasItem(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString())))
            .andExpect(jsonPath("$.[*].successionReadiness").value(hasItem(DEFAULT_SUCCESSION_READINESS.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].riskReason").value(hasItem(DEFAULT_RISK_REASON)))
            .andExpect(jsonPath("$.[*].recommendedAction").value(hasItem(DEFAULT_RECOMMENDED_ACTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionRiskEvaluationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(positionRiskEvaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionRiskEvaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(positionRiskEvaluationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionRiskEvaluationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(positionRiskEvaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionRiskEvaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(positionRiskEvaluationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPositionRiskEvaluation() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get the positionRiskEvaluation
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, positionRiskEvaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionRiskEvaluation.getId().intValue()))
            .andExpect(jsonPath("$.evaluationDate").value(DEFAULT_EVALUATION_DATE.toString()))
            .andExpect(jsonPath("$.ownerCount").value(DEFAULT_OWNER_COUNT))
            .andExpect(jsonPath("$.substitutableOwnerCount").value(DEFAULT_SUBSTITUTABLE_OWNER_COUNT))
            .andExpect(jsonPath("$.hasSubstitute").value(DEFAULT_HAS_SUBSTITUTE))
            .andExpect(jsonPath("$.documentStatus").value(DEFAULT_DOCUMENT_STATUS.toString()))
            .andExpect(jsonPath("$.customerOrSystemDependency").value(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString()))
            .andExpect(jsonPath("$.successionReadiness").value(DEFAULT_SUCCESSION_READINESS.toString()))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.riskReason").value(DEFAULT_RISK_REASON))
            .andExpect(jsonPath("$.recommendedAction").value(DEFAULT_RECOMMENDED_ACTION));
    }

    @Test
    @Transactional
    void getPositionRiskEvaluationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        Long id = positionRiskEvaluation.getId();

        defaultPositionRiskEvaluationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionRiskEvaluationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionRiskEvaluationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate equals to
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.equals=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.equals=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate in
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.in=" + DEFAULT_EVALUATION_DATE + "," + UPDATED_EVALUATION_DATE,
            "evaluationDate.in=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate is not null
        defaultPositionRiskEvaluationFiltering("evaluationDate.specified=true", "evaluationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate is greater than or equal to
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.greaterThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.greaterThanOrEqual=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate is less than or equal to
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.lessThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.lessThanOrEqual=" + SMALLER_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate is less than
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.lessThan=" + UPDATED_EVALUATION_DATE,
            "evaluationDate.lessThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByEvaluationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where evaluationDate is greater than
        defaultPositionRiskEvaluationFiltering(
            "evaluationDate.greaterThan=" + SMALLER_EVALUATION_DATE,
            "evaluationDate.greaterThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount equals to
        defaultPositionRiskEvaluationFiltering("ownerCount.equals=" + DEFAULT_OWNER_COUNT, "ownerCount.equals=" + UPDATED_OWNER_COUNT);
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount in
        defaultPositionRiskEvaluationFiltering(
            "ownerCount.in=" + DEFAULT_OWNER_COUNT + "," + UPDATED_OWNER_COUNT,
            "ownerCount.in=" + UPDATED_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount is not null
        defaultPositionRiskEvaluationFiltering("ownerCount.specified=true", "ownerCount.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount is greater than or equal to
        defaultPositionRiskEvaluationFiltering(
            "ownerCount.greaterThanOrEqual=" + DEFAULT_OWNER_COUNT,
            "ownerCount.greaterThanOrEqual=" + UPDATED_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount is less than or equal to
        defaultPositionRiskEvaluationFiltering(
            "ownerCount.lessThanOrEqual=" + DEFAULT_OWNER_COUNT,
            "ownerCount.lessThanOrEqual=" + SMALLER_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount is less than
        defaultPositionRiskEvaluationFiltering("ownerCount.lessThan=" + UPDATED_OWNER_COUNT, "ownerCount.lessThan=" + DEFAULT_OWNER_COUNT);
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByOwnerCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where ownerCount is greater than
        defaultPositionRiskEvaluationFiltering(
            "ownerCount.greaterThan=" + SMALLER_OWNER_COUNT,
            "ownerCount.greaterThan=" + DEFAULT_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount equals to
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.equals=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.equals=" + UPDATED_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount in
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.in=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT + "," + UPDATED_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.in=" + UPDATED_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount is not null
        defaultPositionRiskEvaluationFiltering("substitutableOwnerCount.specified=true", "substitutableOwnerCount.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount is greater than or equal to
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.greaterThanOrEqual=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.greaterThanOrEqual=" + UPDATED_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount is less than or equal to
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.lessThanOrEqual=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.lessThanOrEqual=" + SMALLER_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount is less than
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.lessThan=" + UPDATED_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.lessThan=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySubstitutableOwnerCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where substitutableOwnerCount is greater than
        defaultPositionRiskEvaluationFiltering(
            "substitutableOwnerCount.greaterThan=" + SMALLER_SUBSTITUTABLE_OWNER_COUNT,
            "substitutableOwnerCount.greaterThan=" + DEFAULT_SUBSTITUTABLE_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByHasSubstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where hasSubstitute equals to
        defaultPositionRiskEvaluationFiltering(
            "hasSubstitute.equals=" + DEFAULT_HAS_SUBSTITUTE,
            "hasSubstitute.equals=" + UPDATED_HAS_SUBSTITUTE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByHasSubstituteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where hasSubstitute in
        defaultPositionRiskEvaluationFiltering(
            "hasSubstitute.in=" + DEFAULT_HAS_SUBSTITUTE + "," + UPDATED_HAS_SUBSTITUTE,
            "hasSubstitute.in=" + UPDATED_HAS_SUBSTITUTE
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByHasSubstituteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where hasSubstitute is not null
        defaultPositionRiskEvaluationFiltering("hasSubstitute.specified=true", "hasSubstitute.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByDocumentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where documentStatus equals to
        defaultPositionRiskEvaluationFiltering(
            "documentStatus.equals=" + DEFAULT_DOCUMENT_STATUS,
            "documentStatus.equals=" + UPDATED_DOCUMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByDocumentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where documentStatus in
        defaultPositionRiskEvaluationFiltering(
            "documentStatus.in=" + DEFAULT_DOCUMENT_STATUS + "," + UPDATED_DOCUMENT_STATUS,
            "documentStatus.in=" + UPDATED_DOCUMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByDocumentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where documentStatus is not null
        defaultPositionRiskEvaluationFiltering("documentStatus.specified=true", "documentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByCustomerOrSystemDependencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where customerOrSystemDependency equals to
        defaultPositionRiskEvaluationFiltering(
            "customerOrSystemDependency.equals=" + DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY,
            "customerOrSystemDependency.equals=" + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByCustomerOrSystemDependencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where customerOrSystemDependency in
        defaultPositionRiskEvaluationFiltering(
            "customerOrSystemDependency.in=" + DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY + "," + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY,
            "customerOrSystemDependency.in=" + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByCustomerOrSystemDependencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where customerOrSystemDependency is not null
        defaultPositionRiskEvaluationFiltering("customerOrSystemDependency.specified=true", "customerOrSystemDependency.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySuccessionReadinessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where successionReadiness equals to
        defaultPositionRiskEvaluationFiltering(
            "successionReadiness.equals=" + DEFAULT_SUCCESSION_READINESS,
            "successionReadiness.equals=" + UPDATED_SUCCESSION_READINESS
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySuccessionReadinessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where successionReadiness in
        defaultPositionRiskEvaluationFiltering(
            "successionReadiness.in=" + DEFAULT_SUCCESSION_READINESS + "," + UPDATED_SUCCESSION_READINESS,
            "successionReadiness.in=" + UPDATED_SUCCESSION_READINESS
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsBySuccessionReadinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where successionReadiness is not null
        defaultPositionRiskEvaluationFiltering("successionReadiness.specified=true", "successionReadiness.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByRiskLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where riskLevel equals to
        defaultPositionRiskEvaluationFiltering("riskLevel.equals=" + DEFAULT_RISK_LEVEL, "riskLevel.equals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByRiskLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where riskLevel in
        defaultPositionRiskEvaluationFiltering(
            "riskLevel.in=" + DEFAULT_RISK_LEVEL + "," + UPDATED_RISK_LEVEL,
            "riskLevel.in=" + UPDATED_RISK_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByRiskLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        // Get all the positionRiskEvaluationList where riskLevel is not null
        defaultPositionRiskEvaluationFiltering("riskLevel.specified=true", "riskLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRiskEvaluationsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        positionRiskEvaluation.setPosition(position);
        positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);
        Long positionId = position.getId();
        // Get all the positionRiskEvaluationList where position equals to positionId
        defaultPositionRiskEvaluationShouldBeFound("positionId.equals=" + positionId);

        // Get all the positionRiskEvaluationList where position equals to (positionId + 1)
        defaultPositionRiskEvaluationShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    private void defaultPositionRiskEvaluationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionRiskEvaluationShouldBeFound(shouldBeFound);
        defaultPositionRiskEvaluationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionRiskEvaluationShouldBeFound(String filter) throws Exception {
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionRiskEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].ownerCount").value(hasItem(DEFAULT_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].substitutableOwnerCount").value(hasItem(DEFAULT_SUBSTITUTABLE_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].hasSubstitute").value(hasItem(DEFAULT_HAS_SUBSTITUTE)))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerOrSystemDependency").value(hasItem(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString())))
            .andExpect(jsonPath("$.[*].successionReadiness").value(hasItem(DEFAULT_SUCCESSION_READINESS.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].riskReason").value(hasItem(DEFAULT_RISK_REASON)))
            .andExpect(jsonPath("$.[*].recommendedAction").value(hasItem(DEFAULT_RECOMMENDED_ACTION)));

        // Check, that the count call also returns 1
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionRiskEvaluationShouldNotBeFound(String filter) throws Exception {
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionRiskEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPositionRiskEvaluation() throws Exception {
        // Get the positionRiskEvaluation
        restPositionRiskEvaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPositionRiskEvaluation() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRiskEvaluation
        PositionRiskEvaluation updatedPositionRiskEvaluation = positionRiskEvaluationRepository
            .findById(positionRiskEvaluation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPositionRiskEvaluation are not directly saved in db
        em.detach(updatedPositionRiskEvaluation);
        updatedPositionRiskEvaluation
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .ownerCount(UPDATED_OWNER_COUNT)
            .substitutableOwnerCount(UPDATED_SUBSTITUTABLE_OWNER_COUNT)
            .hasSubstitute(UPDATED_HAS_SUBSTITUTE)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskReason(UPDATED_RISK_REASON)
            .recommendedAction(UPDATED_RECOMMENDED_ACTION);
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(updatedPositionRiskEvaluation);

        restPositionRiskEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionRiskEvaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionRiskEvaluationToMatchAllProperties(updatedPositionRiskEvaluation);
    }

    @Test
    @Transactional
    void putNonExistingPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionRiskEvaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskEvaluationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionRiskEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRiskEvaluation using partial update
        PositionRiskEvaluation partialUpdatedPositionRiskEvaluation = new PositionRiskEvaluation();
        partialUpdatedPositionRiskEvaluation.setId(positionRiskEvaluation.getId());

        partialUpdatedPositionRiskEvaluation
            .ownerCount(UPDATED_OWNER_COUNT)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .successionReadiness(UPDATED_SUCCESSION_READINESS);

        restPositionRiskEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionRiskEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionRiskEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the PositionRiskEvaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionRiskEvaluationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPositionRiskEvaluation, positionRiskEvaluation),
            getPersistedPositionRiskEvaluation(positionRiskEvaluation)
        );
    }

    @Test
    @Transactional
    void fullUpdatePositionRiskEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRiskEvaluation using partial update
        PositionRiskEvaluation partialUpdatedPositionRiskEvaluation = new PositionRiskEvaluation();
        partialUpdatedPositionRiskEvaluation.setId(positionRiskEvaluation.getId());

        partialUpdatedPositionRiskEvaluation
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .ownerCount(UPDATED_OWNER_COUNT)
            .substitutableOwnerCount(UPDATED_SUBSTITUTABLE_OWNER_COUNT)
            .hasSubstitute(UPDATED_HAS_SUBSTITUTE)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .successionReadiness(UPDATED_SUCCESSION_READINESS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskReason(UPDATED_RISK_REASON)
            .recommendedAction(UPDATED_RECOMMENDED_ACTION);

        restPositionRiskEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionRiskEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionRiskEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the PositionRiskEvaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionRiskEvaluationUpdatableFieldsEquals(
            partialUpdatedPositionRiskEvaluation,
            getPersistedPositionRiskEvaluation(partialUpdatedPositionRiskEvaluation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionRiskEvaluationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionRiskEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRiskEvaluation.setId(longCount.incrementAndGet());

        // Create the PositionRiskEvaluation
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationMapper.toDto(positionRiskEvaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionRiskEvaluationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionRiskEvaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionRiskEvaluation() throws Exception {
        // Initialize the database
        insertedPositionRiskEvaluation = positionRiskEvaluationRepository.saveAndFlush(positionRiskEvaluation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the positionRiskEvaluation
        restPositionRiskEvaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionRiskEvaluation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRiskEvaluationRepository.count();
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

    protected PositionRiskEvaluation getPersistedPositionRiskEvaluation(PositionRiskEvaluation positionRiskEvaluation) {
        return positionRiskEvaluationRepository.findById(positionRiskEvaluation.getId()).orElseThrow();
    }

    protected void assertPersistedPositionRiskEvaluationToMatchAllProperties(PositionRiskEvaluation expectedPositionRiskEvaluation) {
        assertPositionRiskEvaluationAllPropertiesEquals(
            expectedPositionRiskEvaluation,
            getPersistedPositionRiskEvaluation(expectedPositionRiskEvaluation)
        );
    }

    protected void assertPersistedPositionRiskEvaluationToMatchUpdatableProperties(PositionRiskEvaluation expectedPositionRiskEvaluation) {
        assertPositionRiskEvaluationAllUpdatablePropertiesEquals(
            expectedPositionRiskEvaluation,
            getPersistedPositionRiskEvaluation(expectedPositionRiskEvaluation)
        );
    }
}
