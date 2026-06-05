package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PersonRiskAsserts.*;
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
import top.btmdc.hr.domain.PersonRisk;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.domain.enumeration.RiskType;
import top.btmdc.hr.repository.PersonRiskRepository;
import top.btmdc.hr.service.PersonRiskService;
import top.btmdc.hr.service.dto.PersonRiskDTO;
import top.btmdc.hr.service.mapper.PersonRiskMapper;

/**
 * Integration tests for the {@link PersonRiskResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonRiskResourceIT {

    private static final RiskType DEFAULT_RISK_TYPE = RiskType.SINGLE_POINT;
    private static final RiskType UPDATED_RISK_TYPE = RiskType.KNOWLEDGE_CONCENTRATION;

    private static final RiskLevel DEFAULT_RISK_LEVEL = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_LEVEL = RiskLevel.MEDIUM;

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

    private static final String ENTITY_API_URL = "/api/person-risks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonRiskRepository personRiskRepository;

    @Mock
    private PersonRiskRepository personRiskRepositoryMock;

    @Autowired
    private PersonRiskMapper personRiskMapper;

    @Mock
    private PersonRiskService personRiskServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonRiskMockMvc;

    private PersonRisk personRisk;

    private PersonRisk insertedPersonRisk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonRisk createEntity(EntityManager em) {
        PersonRisk personRisk = new PersonRisk()
            .riskType(DEFAULT_RISK_TYPE)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .riskDescription(DEFAULT_RISK_DESCRIPTION)
            .improvementAction(DEFAULT_IMPROVEMENT_ACTION)
            .identifiedDate(DEFAULT_IDENTIFIED_DATE)
            .targetDate(DEFAULT_TARGET_DATE)
            .closedDate(DEFAULT_CLOSED_DATE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        personRisk.setPerson(person);
        return personRisk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonRisk createUpdatedEntity(EntityManager em) {
        PersonRisk updatedPersonRisk = new PersonRisk()
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedPersonRisk.setPerson(person);
        return updatedPersonRisk;
    }

    @BeforeEach
    void initTest() {
        personRisk = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPersonRisk != null) {
            personRiskRepository.delete(insertedPersonRisk);
            insertedPersonRisk = null;
        }
    }

    @Test
    @Transactional
    void createPersonRisk() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);
        var returnedPersonRiskDTO = om.readValue(
            restPersonRiskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonRiskDTO.class
        );

        // Validate the PersonRisk in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPersonRisk = personRiskMapper.toEntity(returnedPersonRiskDTO);
        assertPersonRiskUpdatableFieldsEquals(returnedPersonRisk, getPersistedPersonRisk(returnedPersonRisk));

        insertedPersonRisk = returnedPersonRisk;
    }

    @Test
    @Transactional
    void createPersonRiskWithExistingId() throws Exception {
        // Create the PersonRisk with an existing ID
        personRisk.setId(1L);
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRiskTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personRisk.setRiskType(null);

        // Create the PersonRisk, which fails.
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        restPersonRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRiskLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personRisk.setRiskLevel(null);

        // Create the PersonRisk, which fails.
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        restPersonRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifiedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personRisk.setIdentifiedDate(null);

        // Create the PersonRisk, which fails.
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        restPersonRiskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonRisks() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personRisk.getId().intValue())))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].riskDescription").value(hasItem(DEFAULT_RISK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].identifiedDate").value(hasItem(DEFAULT_IDENTIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonRisksWithEagerRelationshipsIsEnabled() throws Exception {
        when(personRiskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personRiskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonRisksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personRiskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonRiskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(personRiskRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPersonRisk() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get the personRisk
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL_ID, personRisk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personRisk.getId().intValue()))
            .andExpect(jsonPath("$.riskType").value(DEFAULT_RISK_TYPE.toString()))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.riskDescription").value(DEFAULT_RISK_DESCRIPTION))
            .andExpect(jsonPath("$.improvementAction").value(DEFAULT_IMPROVEMENT_ACTION))
            .andExpect(jsonPath("$.identifiedDate").value(DEFAULT_IDENTIFIED_DATE.toString()))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPersonRisksByIdFiltering() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        Long id = personRisk.getId();

        defaultPersonRiskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPersonRiskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPersonRiskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskType equals to
        defaultPersonRiskFiltering("riskType.equals=" + DEFAULT_RISK_TYPE, "riskType.equals=" + UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskType in
        defaultPersonRiskFiltering("riskType.in=" + DEFAULT_RISK_TYPE + "," + UPDATED_RISK_TYPE, "riskType.in=" + UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskType is not null
        defaultPersonRiskFiltering("riskType.specified=true", "riskType.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskLevel equals to
        defaultPersonRiskFiltering("riskLevel.equals=" + DEFAULT_RISK_LEVEL, "riskLevel.equals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskLevel in
        defaultPersonRiskFiltering("riskLevel.in=" + DEFAULT_RISK_LEVEL + "," + UPDATED_RISK_LEVEL, "riskLevel.in=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    void getAllPersonRisksByRiskLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where riskLevel is not null
        defaultPersonRiskFiltering("riskLevel.specified=true", "riskLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate equals to
        defaultPersonRiskFiltering("identifiedDate.equals=" + DEFAULT_IDENTIFIED_DATE, "identifiedDate.equals=" + UPDATED_IDENTIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate in
        defaultPersonRiskFiltering(
            "identifiedDate.in=" + DEFAULT_IDENTIFIED_DATE + "," + UPDATED_IDENTIFIED_DATE,
            "identifiedDate.in=" + UPDATED_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate is not null
        defaultPersonRiskFiltering("identifiedDate.specified=true", "identifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate is greater than or equal to
        defaultPersonRiskFiltering(
            "identifiedDate.greaterThanOrEqual=" + DEFAULT_IDENTIFIED_DATE,
            "identifiedDate.greaterThanOrEqual=" + UPDATED_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate is less than or equal to
        defaultPersonRiskFiltering(
            "identifiedDate.lessThanOrEqual=" + DEFAULT_IDENTIFIED_DATE,
            "identifiedDate.lessThanOrEqual=" + SMALLER_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate is less than
        defaultPersonRiskFiltering(
            "identifiedDate.lessThan=" + UPDATED_IDENTIFIED_DATE,
            "identifiedDate.lessThan=" + DEFAULT_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByIdentifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where identifiedDate is greater than
        defaultPersonRiskFiltering(
            "identifiedDate.greaterThan=" + SMALLER_IDENTIFIED_DATE,
            "identifiedDate.greaterThan=" + DEFAULT_IDENTIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate equals to
        defaultPersonRiskFiltering("targetDate.equals=" + DEFAULT_TARGET_DATE, "targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate in
        defaultPersonRiskFiltering(
            "targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE,
            "targetDate.in=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate is not null
        defaultPersonRiskFiltering("targetDate.specified=true", "targetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate is greater than or equal to
        defaultPersonRiskFiltering(
            "targetDate.greaterThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.greaterThanOrEqual=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate is less than or equal to
        defaultPersonRiskFiltering(
            "targetDate.lessThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.lessThanOrEqual=" + SMALLER_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate is less than
        defaultPersonRiskFiltering("targetDate.lessThan=" + UPDATED_TARGET_DATE, "targetDate.lessThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByTargetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where targetDate is greater than
        defaultPersonRiskFiltering("targetDate.greaterThan=" + SMALLER_TARGET_DATE, "targetDate.greaterThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate equals to
        defaultPersonRiskFiltering("closedDate.equals=" + DEFAULT_CLOSED_DATE, "closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate in
        defaultPersonRiskFiltering(
            "closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE,
            "closedDate.in=" + UPDATED_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate is not null
        defaultPersonRiskFiltering("closedDate.specified=true", "closedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate is greater than or equal to
        defaultPersonRiskFiltering(
            "closedDate.greaterThanOrEqual=" + DEFAULT_CLOSED_DATE,
            "closedDate.greaterThanOrEqual=" + UPDATED_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate is less than or equal to
        defaultPersonRiskFiltering(
            "closedDate.lessThanOrEqual=" + DEFAULT_CLOSED_DATE,
            "closedDate.lessThanOrEqual=" + SMALLER_CLOSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate is less than
        defaultPersonRiskFiltering("closedDate.lessThan=" + UPDATED_CLOSED_DATE, "closedDate.lessThan=" + DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByClosedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        // Get all the personRiskList where closedDate is greater than
        defaultPersonRiskFiltering("closedDate.greaterThan=" + SMALLER_CLOSED_DATE, "closedDate.greaterThan=" + DEFAULT_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllPersonRisksByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personRiskRepository.saveAndFlush(personRisk);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        personRisk.setPerson(person);
        personRiskRepository.saveAndFlush(personRisk);
        Long personId = person.getId();
        // Get all the personRiskList where person equals to personId
        defaultPersonRiskShouldBeFound("personId.equals=" + personId);

        // Get all the personRiskList where person equals to (personId + 1)
        defaultPersonRiskShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllPersonRisksByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            personRiskRepository.saveAndFlush(personRisk);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        personRisk.setPosition(position);
        personRiskRepository.saveAndFlush(personRisk);
        Long positionId = position.getId();
        // Get all the personRiskList where position equals to positionId
        defaultPersonRiskShouldBeFound("positionId.equals=" + positionId);

        // Get all the personRiskList where position equals to (positionId + 1)
        defaultPersonRiskShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    private void defaultPersonRiskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPersonRiskShouldBeFound(shouldBeFound);
        defaultPersonRiskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonRiskShouldBeFound(String filter) throws Exception {
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personRisk.getId().intValue())))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].riskDescription").value(hasItem(DEFAULT_RISK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].improvementAction").value(hasItem(DEFAULT_IMPROVEMENT_ACTION)))
            .andExpect(jsonPath("$.[*].identifiedDate").value(hasItem(DEFAULT_IDENTIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())));

        // Check, that the count call also returns 1
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonRiskShouldNotBeFound(String filter) throws Exception {
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonRiskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonRisk() throws Exception {
        // Get the personRisk
        restPersonRiskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonRisk() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personRisk
        PersonRisk updatedPersonRisk = personRiskRepository.findById(personRisk.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonRisk are not directly saved in db
        em.detach(updatedPersonRisk);
        updatedPersonRisk
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(updatedPersonRisk);

        restPersonRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personRiskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personRiskDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonRiskToMatchAllProperties(updatedPersonRisk);
    }

    @Test
    @Transactional
    void putNonExistingPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personRiskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonRiskWithPatch() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personRisk using partial update
        PersonRisk partialUpdatedPersonRisk = new PersonRisk();
        partialUpdatedPersonRisk.setId(personRisk.getId());

        partialUpdatedPersonRisk.riskLevel(UPDATED_RISK_LEVEL).targetDate(UPDATED_TARGET_DATE).closedDate(UPDATED_CLOSED_DATE);

        restPersonRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonRisk))
            )
            .andExpect(status().isOk());

        // Validate the PersonRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonRiskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonRisk, personRisk),
            getPersistedPersonRisk(personRisk)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonRiskWithPatch() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personRisk using partial update
        PersonRisk partialUpdatedPersonRisk = new PersonRisk();
        partialUpdatedPersonRisk.setId(personRisk.getId());

        partialUpdatedPersonRisk
            .riskType(UPDATED_RISK_TYPE)
            .riskLevel(UPDATED_RISK_LEVEL)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .improvementAction(UPDATED_IMPROVEMENT_ACTION)
            .identifiedDate(UPDATED_IDENTIFIED_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .closedDate(UPDATED_CLOSED_DATE);

        restPersonRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonRisk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonRisk))
            )
            .andExpect(status().isOk());

        // Validate the PersonRisk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonRiskUpdatableFieldsEquals(partialUpdatedPersonRisk, getPersistedPersonRisk(partialUpdatedPersonRisk));
    }

    @Test
    @Transactional
    void patchNonExistingPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personRiskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personRiskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonRisk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personRisk.setId(longCount.incrementAndGet());

        // Create the PersonRisk
        PersonRiskDTO personRiskDTO = personRiskMapper.toDto(personRisk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonRiskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personRiskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonRisk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonRisk() throws Exception {
        // Initialize the database
        insertedPersonRisk = personRiskRepository.saveAndFlush(personRisk);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personRisk
        restPersonRiskMockMvc
            .perform(delete(ENTITY_API_URL_ID, personRisk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personRiskRepository.count();
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

    protected PersonRisk getPersistedPersonRisk(PersonRisk personRisk) {
        return personRiskRepository.findById(personRisk.getId()).orElseThrow();
    }

    protected void assertPersistedPersonRiskToMatchAllProperties(PersonRisk expectedPersonRisk) {
        assertPersonRiskAllPropertiesEquals(expectedPersonRisk, getPersistedPersonRisk(expectedPersonRisk));
    }

    protected void assertPersistedPersonRiskToMatchUpdatableProperties(PersonRisk expectedPersonRisk) {
        assertPersonRiskAllUpdatablePropertiesEquals(expectedPersonRisk, getPersistedPersonRisk(expectedPersonRisk));
    }
}
