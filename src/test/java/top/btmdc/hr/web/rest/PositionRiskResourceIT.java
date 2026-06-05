package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionRiskAsserts.*;
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
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionRisk;
import top.btmdc.hr.domain.enumeration.BackupStatus;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;
import top.btmdc.hr.repository.PositionRiskRepository;
import top.btmdc.hr.service.PositionRiskService;
import top.btmdc.hr.service.dto.PositionRiskDTO;
import top.btmdc.hr.service.mapper.PositionRiskMapper;

/**
 * Integration tests for the {@link PositionRiskResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PositionRiskResourceIT {

    private static final RiskType DEFAULT_RISK_TYPE = RiskType.SINGLE_POINT;
    private static final RiskType UPDATED_RISK_TYPE = RiskType.KNOWLEDGE_CONCENTRATION;

    private static final RiskLevel DEFAULT_RISK_LEVEL = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_LEVEL = RiskLevel.MEDIUM;

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.AVAILABLE;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.PARTIAL;

    private static final BackupStatus DEFAULT_BACKUP_STATUS = BackupStatus.AVAILABLE;
    private static final BackupStatus UPDATED_BACKUP_STATUS = BackupStatus.PARTIAL;

    private static final ImportanceLevel DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY = ImportanceLevel.MEDIUM;

    private static final String DEFAULT_RISK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RISK_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMPROVEMENT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_IMPROVEMENT_ACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_IDENTIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_IDENTIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_IDENTIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TARGET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARGET_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TARGET_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CLOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CLOSED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/position-risks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRiskRepository positionRiskRepository;

    @Mock
    private PositionRiskRepository positionRiskRepositoryMock;

    @Autowired
    private PositionRiskMapper positionRiskMapper;

    @Mock
    private PositionRiskService positionRiskServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionRiskMockMvc;

    private PositionRisk positionRisk;

    private PositionRisk insertedPositionRisk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionRisk createEntity(EntityManager em) {
        PositionRisk positionRisk = new PositionRisk()
            .riskType(DEFAULT_RISK_TYPE)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .backupStatus(DEFAULT_BACKUP_STATUS)
            .customerOrSystemDependency(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .riskDescription(DEFAULT_RISK_DESCRIPTION)
            .improvementAction(DEFAULT_IMPROVEMENT_ACTION)
            .identifiedDate(DEFAULT_IDENTIFIED_DATE)
            .targetDate(DEFAULT_TARGET_DATE)
            .closedDate(DEFAULT_CLOSED_DATE);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        positionRisk.setPosition(position);
        return positionRisk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionRisk createUpdatedEntity(EntityManager em) {
        PositionRisk updatedPositionRisk = new PositionRisk()
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .backupStatus(UPDATED_BACKUP_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedPositionRisk.setPosition(position);
        return updatedPositionRisk;
    }

    @BeforeEach
    void initTest() {
        positionRisk = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPositionRisk != null) {
            positionRiskRepository.delete(insertedPositionRisk);
            insertedPositionRisk = null;
        }
    }

    @Test
    @Transactional
    void createPositionRisk() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);
        var returnedPositionRiskDTO = om.readValue(
            restPositionRiskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionRiskDTO.class
        );

        // Validate the PositionRisk in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPositionRisk = positionRiskMapper.toEntity(returnedPositionRiskDTO);
        assertPositionRiskUpdatableFieldsEquals(returnedPositionRisk, getPersistedPositionRisk(returnedPositionRisk));

        insertedPositionRisk = returnedPositionRisk;
    }

    @Test
    @Transactional
    void createPositionRiskWithExistingId() throws Exception {
        // Create the PositionRisk with an existing ID
        positionRisk.setId(1L);
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRiskTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRisk.setRiskType(null);

        // Create the PositionRisk, which fails.
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        restPositionRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRiskLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRisk.setRiskLevel(null);

        // Create the PositionRisk, which fails.
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        restPositionRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifiedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionRisk.setIdentifiedDate(null);

        // Create the PositionRisk, which fails.
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        restPositionRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionRisks() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionRisk.getId().intValue())))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].backupStatus").value(hasItem(DEFAULT_BACKUP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerOrSystemDependency").value(hasItem(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString())))
            .andExpect(jsonPath("$.[*].riskDescription").value(hasItem(DEFAULT_RISK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].identifiedDate").value(hasItem(DEFAULT_IDENTIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionRisksWithEagerRelationshipsIsEnabled() throws Exception {
        when(positionRiskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(positionRiskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionRisksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(positionRiskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(positionRiskRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPositionRisk() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get the positionRisk
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL_ID, positionRisk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionRisk.getId().intValue()))
            .andExpect(jsonPath("$.riskType").value(DEFAULT_RISK_TYPE.toString()))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.documentStatus").value(DEFAULT_DOCUMENT_STATUS.toString()))
            .andExpect(jsonPath("$.backupStatus").value(DEFAULT_BACKUP_STATUS.toString()))
            .andExpect(jsonPath("$.customerOrSystemDependency").value(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString()))
            .andExpect(jsonPath("$.riskDescription").value(DEFAULT_RISK_DESCRIPTION))
            .andExpect(jsonPath("$.improvementAction").value(DEFAULT_IMPROVEMENT_ACTION))
            .andExpect(jsonPath("$.identifiedDate").value(DEFAULT_IDENTIFIED_DATE.toString()))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPositionRisksByIdFiltering() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        Long id = positionRisk.getId();

        defaultPositionRiskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionRiskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionRiskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskType equals to
        defaultPositionRiskFiltering("riskType.equals=" + DEFAULT_RISK_TYPE, "riskType.equals=" + UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskType in
        defaultPositionRiskFiltering("riskType.in=" + DEFAULT_RISK_TYPE + "," + UPDATED_RISK_TYPE, "riskType.in=" + UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskType is not null
        defaultPositionRiskFiltering("riskType.specified=true", "riskType.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskLevel equals to
        defaultPositionRiskFiltering("riskLevel.equals=" + DEFAULT_RISK_LEVEL, "riskLevel.equals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskLevel in
        defaultPositionRiskFiltering("riskLevel.in=" + DEFAULT_RISK_LEVEL + "," + UPDATED_RISK_LEVEL, "riskLevel.in=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPositionRisksByRiskLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where riskLevel is not null
        defaultPositionRiskFiltering("riskLevel.specified=true", "riskLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByDocumentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where documentStatus equals to
        defaultPositionRiskFiltering(
            "documentStatus.equals=" + DEFAULT_DOCUMENT_STATUS,
            "documentStatus.equals=" + UPDATED_DOCUMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByDocumentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where documentStatus in
        defaultPositionRiskFiltering(
            "documentStatus.in=" + DEFAULT_DOCUMENT_STATUS + "," + UPDATED_DOCUMENT_STATUS,
            "documentStatus.in=" + UPDATED_DOCUMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByDocumentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where documentStatus is not null
        defaultPositionRiskFiltering("documentStatus.specified=true", "documentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByBackupStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where backupStatus equals to
        defaultPositionRiskFiltering("backupStatus.equals=" + DEFAULT_BACKUP_STATUS, "backupStatus.equals=" + UPDATED_BACKUP_STATUS);
    }

    @Test
    @Transactional
    void getAllPositionRisksByBackupStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where backupStatus in
        defaultPositionRiskFiltering(
            "backupStatus.in=" + DEFAULT_BACKUP_STATUS + "," + UPDATED_BACKUP_STATUS,
            "backupStatus.in=" + UPDATED_BACKUP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByBackupStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where backupStatus is not null
        defaultPositionRiskFiltering("backupStatus.specified=true", "backupStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByCustomerOrSystemDependencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where customerOrSystemDependency equals to
        defaultPositionRiskFiltering(
            "customerOrSystemDependency.equals=" + DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY,
            "customerOrSystemDependency.equals=" + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByCustomerOrSystemDependencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where customerOrSystemDependency in
        defaultPositionRiskFiltering(
            "customerOrSystemDependency.in=" + DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY + "," + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY,
            "customerOrSystemDependency.in=" + UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByCustomerOrSystemDependencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where customerOrSystemDependency is not null
        defaultPositionRiskFiltering("customerOrSystemDependency.specified=true", "customerOrSystemDependency.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate equals to
        defaultPositionRiskFiltering(
            "identifiedDate.equals=" + DEFAULT_IDENTIFIED_DATE,
            "identifiedDate.equals=" + UPDATED_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate in
        defaultPositionRiskFiltering(
            "identifiedDate.in=" + DEFAULT_IDENTIFIED_DATE + "," + UPDATED_IDENTIFIED_DATE,
            "identifiedDate.in=" + UPDATED_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate is not null
        defaultPositionRiskFiltering("identifiedDate.specified=true", "identifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate is greater than or equal to
        defaultPositionRiskFiltering(
            "identifiedDate.greaterThanOrEqual=" + DEFAULT_IDENTIFIED_DATE,
            "identifiedDate.greaterThanOrEqual=" + UPDATED_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate is less than or equal to
        defaultPositionRiskFiltering(
            "identifiedDate.lessThanOrEqual=" + DEFAULT_IDENTIFIED_DATE,
            "identifiedDate.lessThanOrEqual=" + SMALLER_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate is less than
        defaultPositionRiskFiltering(
            "identifiedDate.lessThan=" + UPDATED_IDENTIFIED_DATE,
            "identifiedDate.lessThan=" + DEFAULT_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByIdentifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where identifiedDate is greater than
        defaultPositionRiskFiltering(
            "identifiedDate.greaterThan=" + SMALLER_IDENTIFIED_DATE,
            "identifiedDate.greaterThan=" + DEFAULT_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate equals to
        defaultPositionRiskFiltering("targetDate.equals=" + DEFAULT_TARGET_DATE, "targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate in
        defaultPositionRiskFiltering(
            "targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE,
            "targetDate.in=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate is not null
        defaultPositionRiskFiltering("targetDate.specified=true", "targetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate is greater than or equal to
        defaultPositionRiskFiltering(
            "targetDate.greaterThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.greaterThanOrEqual=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate is less than or equal to
        defaultPositionRiskFiltering(
            "targetDate.lessThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.lessThanOrEqual=" + SMALLER_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate is less than
        defaultPositionRiskFiltering("targetDate.lessThan=" + UPDATED_TARGET_DATE, "targetDate.lessThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByTargetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where targetDate is greater than
        defaultPositionRiskFiltering("targetDate.greaterThan=" + SMALLER_TARGET_DATE, "targetDate.greaterThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate equals to
        defaultPositionRiskFiltering("closedDate.equals=" + DEFAULT_CLOSED_DATE, "closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate in
        defaultPositionRiskFiltering(
            "closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE,
            "closedDate.in=" + UPDATED_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate is not null
        defaultPositionRiskFiltering("closedDate.specified=true", "closedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate is greater than or equal to
        defaultPositionRiskFiltering(
            "closedDate.greaterThanOrEqual=" + DEFAULT_CLOSED_DATE,
            "closedDate.greaterThanOrEqual=" + UPDATED_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate is less than or equal to
        defaultPositionRiskFiltering(
            "closedDate.lessThanOrEqual=" + DEFAULT_CLOSED_DATE,
            "closedDate.lessThanOrEqual=" + SMALLER_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate is less than
        defaultPositionRiskFiltering("closedDate.lessThan=" + UPDATED_CLOSED_DATE, "closedDate.lessThan=" + DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByClosedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        // Get all the positionRiskList where closedDate is greater than
        defaultPositionRiskFiltering("closedDate.greaterThan=" + SMALLER_CLOSED_DATE, "closedDate.greaterThan=" + DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPositionRisksByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            positionRiskRepository.saveAndFlush(positionRisk);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        positionRisk.setPosition(position);
        positionRiskRepository.saveAndFlush(positionRisk);
        Long positionId = position.getId();
        // Get all the positionRiskList where position equals to positionId
        defaultPositionRiskShouldBeFound("positionId.equals=" + positionId);

        // Get all the positionRiskList where position equals to (positionId + 1)
        defaultPositionRiskShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllPositionRisksByCategoryIsEqualToSomething() throws Exception {
        KeyResponsibilityCategory category;
        if (TestUtil.findAll(em, KeyResponsibilityCategory.class).isEmpty()) {
            positionRiskRepository.saveAndFlush(positionRisk);
            category = KeyResponsibilityCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, KeyResponsibilityCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        positionRisk.setCategory(category);
        positionRiskRepository.saveAndFlush(positionRisk);
        Long categoryId = category.getId();
        // Get all the positionRiskList where category equals to categoryId
        defaultPositionRiskShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the positionRiskList where category equals to (categoryId + 1)
        defaultPositionRiskShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultPositionRiskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionRiskShouldBeFound(shouldBeFound);
        defaultPositionRiskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionRiskShouldBeFound(String filter) throws Exception {
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionRisk.getId().intValue())))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].backupStatus").value(hasItem(DEFAULT_BACKUP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerOrSystemDependency").value(hasItem(DEFAULT_CUSTOMER_OR_SYSTEM_DEPENDENCY.toString())))
            .andExpect(jsonPath("$.[*].riskDescription").value(hasItem(DEFAULT_RISK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].identifiedDate").value(hasItem(DEFAULT_IDENTIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));

        // Check, that the count call also returns 1
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionRiskShouldNotBeFound(String filter) throws Exception {
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionRiskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPositionRisk() throws Exception {
        // Get the positionRisk
        restPositionRiskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPositionRisk() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRisk
        PositionRisk updatedPositionRisk = positionRiskRepository.findById(positionRisk.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPositionRisk are not directly saved in db
        em.detach(updatedPositionRisk);
        updatedPositionRisk
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .backupStatus(UPDATED_BACKUP_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(updatedPositionRisk);

        restPositionRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionRiskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskDTO))
            )
            .andExpect(status().isOk());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionRiskToMatchAllProperties(updatedPositionRisk);
    }

    @Test
    @Transactional
    void putNonExistingPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionRiskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionRiskWithPatch() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRisk using partial update
        PositionRisk partialUpdatedPositionRisk = new PositionRisk();
        partialUpdatedPositionRisk.setId(positionRisk.getId());

        partialUpdatedPositionRisk.backupStatus(UPDATED_BACKUP_STATUS);

        restPositionRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionRisk))
            )
            .andExpect(status().isOk());

        // Validate the PositionRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionRiskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPositionRisk, positionRisk),
            getPersistedPositionRisk(positionRisk)
        );
    }

    @Test
    @Transactional
    void fullUpdatePositionRiskWithPatch() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionRisk using partial update
        PositionRisk partialUpdatedPositionRisk = new PositionRisk();
        partialUpdatedPositionRisk.setId(positionRisk.getId());

        partialUpdatedPositionRisk
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .backupStatus(UPDATED_BACKUP_STATUS)
            .customerOrSystemDependency(UPDATED_CUSTOMER_OR_SYSTEM_DEPENDENCY)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);

        restPositionRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionRisk))
            )
            .andExpect(status().isOk());

        // Validate the PositionRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionRiskUpdatableFieldsEquals(partialUpdatedPositionRisk, getPersistedPositionRisk(partialUpdatedPositionRisk));
    }

    @Test
    @Transactional
    void patchNonExistingPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionRiskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionRisk.setId(longCount.incrementAndGet());

        // Create the PositionRisk
        PositionRiskDTO positionRiskDTO = positionRiskMapper.toDto(positionRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionRiskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionRiskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionRisk() throws Exception {
        // Initialize the database
        insertedPositionRisk = positionRiskRepository.saveAndFlush(positionRisk);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the positionRisk
        restPositionRiskMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionRisk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRiskRepository.count();
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

    protected PositionRisk getPersistedPositionRisk(PositionRisk positionRisk) {
        return positionRiskRepository.findById(positionRisk.getId()).orElseThrow();
    }

    protected void assertPersistedPositionRiskToMatchAllProperties(PositionRisk expectedPositionRisk) {
        assertPositionRiskAllPropertiesEquals(expectedPositionRisk, getPersistedPositionRisk(expectedPositionRisk));
    }

    protected void assertPersistedPositionRiskToMatchUpdatableProperties(PositionRisk expectedPositionRisk) {
        assertPositionRiskAllUpdatablePropertiesEquals(expectedPositionRisk, getPersistedPositionRisk(expectedPositionRisk));
    }
}
