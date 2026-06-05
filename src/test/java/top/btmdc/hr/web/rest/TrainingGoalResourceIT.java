package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.TrainingGoalAsserts.*;
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
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.enumeration.PlanStatus;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.TrainingGoalService;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.mapper.TrainingGoalMapper;

/**
 * Integration tests for the {@link TrainingGoalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrainingGoalResourceIT {

    private static final String DEFAULT_GOAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GOAL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_LEVEL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_LEVEL_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TARGET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARGET_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TARGET_DATE = LocalDate.ofEpochDay(-1L);

    private static final PlanStatus DEFAULT_STATUS = PlanStatus.DRAFT;
    private static final PlanStatus UPDATED_STATUS = PlanStatus.ACTIVE;

    private static final String ENTITY_API_URL = "/api/training-goals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainingGoalRepository trainingGoalRepository;

    @Mock
    private TrainingGoalRepository trainingGoalRepositoryMock;

    @Autowired
    private TrainingGoalMapper trainingGoalMapper;

    @Mock
    private TrainingGoalService trainingGoalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingGoalMockMvc;

    private TrainingGoal trainingGoal;

    private TrainingGoal insertedTrainingGoal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingGoal createEntity() {
        return new TrainingGoal()
            .goalName(DEFAULT_GOAL_NAME)
            .goalDescription(DEFAULT_GOAL_DESCRIPTION)
            .targetLevelDescription(DEFAULT_TARGET_LEVEL_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .targetDate(DEFAULT_TARGET_DATE)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingGoal createUpdatedEntity() {
        return new TrainingGoal()
            .goalName(UPDATED_GOAL_NAME)
            .goalDescription(UPDATED_GOAL_DESCRIPTION)
            .targetLevelDescription(UPDATED_TARGET_LEVEL_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        trainingGoal = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrainingGoal != null) {
            trainingGoalRepository.delete(insertedTrainingGoal);
            insertedTrainingGoal = null;
        }
    }

    @Test
    @Transactional
    void createTrainingGoal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);
        var returnedTrainingGoalDTO = om.readValue(
            restTrainingGoalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingGoalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainingGoalDTO.class
        );

        // Validate the TrainingGoal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrainingGoal = trainingGoalMapper.toEntity(returnedTrainingGoalDTO);
        assertTrainingGoalUpdatableFieldsEquals(returnedTrainingGoal, getPersistedTrainingGoal(returnedTrainingGoal));

        insertedTrainingGoal = returnedTrainingGoal;
    }

    @Test
    @Transactional
    void createTrainingGoalWithExistingId() throws Exception {
        // Create the TrainingGoal with an existing ID
        trainingGoal.setId(1L);
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingGoalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGoalNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingGoal.setGoalName(null);

        // Create the TrainingGoal, which fails.
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        restTrainingGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingGoalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingGoal.setStatus(null);

        // Create the TrainingGoal, which fails.
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        restTrainingGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingGoalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainingGoals() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingGoal.getId().intValue())))
            .andExpect(jsonPath("$.[*].goalName").value(hasItem(DEFAULT_GOAL_NAME)))
            .andExpect(jsonPath("$.[*].goalDescription").value(hasItem(DEFAULT_GOAL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].targetLevelDescription").value(hasItem(DEFAULT_TARGET_LEVEL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingGoalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(trainingGoalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingGoalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trainingGoalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingGoalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trainingGoalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingGoalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trainingGoalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrainingGoal() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get the trainingGoal
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingGoal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingGoal.getId().intValue()))
            .andExpect(jsonPath("$.goalName").value(DEFAULT_GOAL_NAME))
            .andExpect(jsonPath("$.goalDescription").value(DEFAULT_GOAL_DESCRIPTION))
            .andExpect(jsonPath("$.targetLevelDescription").value(DEFAULT_TARGET_LEVEL_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getTrainingGoalsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        Long id = trainingGoal.getId();

        defaultTrainingGoalFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrainingGoalFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrainingGoalFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByGoalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where goalName equals to
        defaultTrainingGoalFiltering("goalName.equals=" + DEFAULT_GOAL_NAME, "goalName.equals=" + UPDATED_GOAL_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByGoalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where goalName in
        defaultTrainingGoalFiltering("goalName.in=" + DEFAULT_GOAL_NAME + "," + UPDATED_GOAL_NAME, "goalName.in=" + UPDATED_GOAL_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByGoalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where goalName is not null
        defaultTrainingGoalFiltering("goalName.specified=true", "goalName.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByGoalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where goalName contains
        defaultTrainingGoalFiltering("goalName.contains=" + DEFAULT_GOAL_NAME, "goalName.contains=" + UPDATED_GOAL_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByGoalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where goalName does not contain
        defaultTrainingGoalFiltering("goalName.doesNotContain=" + UPDATED_GOAL_NAME, "goalName.doesNotContain=" + DEFAULT_GOAL_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate equals to
        defaultTrainingGoalFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate in
        defaultTrainingGoalFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate is not null
        defaultTrainingGoalFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate is greater than or equal to
        defaultTrainingGoalFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate is less than or equal to
        defaultTrainingGoalFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate is less than
        defaultTrainingGoalFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where startDate is greater than
        defaultTrainingGoalFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate equals to
        defaultTrainingGoalFiltering("targetDate.equals=" + DEFAULT_TARGET_DATE, "targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate in
        defaultTrainingGoalFiltering(
            "targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE,
            "targetDate.in=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate is not null
        defaultTrainingGoalFiltering("targetDate.specified=true", "targetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate is greater than or equal to
        defaultTrainingGoalFiltering(
            "targetDate.greaterThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.greaterThanOrEqual=" + UPDATED_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate is less than or equal to
        defaultTrainingGoalFiltering(
            "targetDate.lessThanOrEqual=" + DEFAULT_TARGET_DATE,
            "targetDate.lessThanOrEqual=" + SMALLER_TARGET_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate is less than
        defaultTrainingGoalFiltering("targetDate.lessThan=" + UPDATED_TARGET_DATE, "targetDate.lessThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where targetDate is greater than
        defaultTrainingGoalFiltering("targetDate.greaterThan=" + SMALLER_TARGET_DATE, "targetDate.greaterThan=" + DEFAULT_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where status equals to
        defaultTrainingGoalFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where status in
        defaultTrainingGoalFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        // Get all the trainingGoalList where status is not null
        defaultTrainingGoalFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            trainingGoalRepository.saveAndFlush(trainingGoal);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        trainingGoal.setPerson(person);
        trainingGoalRepository.saveAndFlush(trainingGoal);
        Long personId = person.getId();
        // Get all the trainingGoalList where person equals to personId
        defaultTrainingGoalShouldBeFound("personId.equals=" + personId);

        // Get all the trainingGoalList where person equals to (personId + 1)
        defaultTrainingGoalShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            trainingGoalRepository.saveAndFlush(trainingGoal);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        trainingGoal.setPosition(position);
        trainingGoalRepository.saveAndFlush(trainingGoal);
        Long positionId = position.getId();
        // Get all the trainingGoalList where position equals to positionId
        defaultTrainingGoalShouldBeFound("positionId.equals=" + positionId);

        // Get all the trainingGoalList where position equals to (positionId + 1)
        defaultTrainingGoalShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingGoalsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            trainingGoalRepository.saveAndFlush(trainingGoal);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        trainingGoal.setSkill(skill);
        trainingGoalRepository.saveAndFlush(trainingGoal);
        Long skillId = skill.getId();
        // Get all the trainingGoalList where skill equals to skillId
        defaultTrainingGoalShouldBeFound("skillId.equals=" + skillId);

        // Get all the trainingGoalList where skill equals to (skillId + 1)
        defaultTrainingGoalShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingGoalsByTargetLevelIsEqualToSomething() throws Exception {
        SkillLevel targetLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            trainingGoalRepository.saveAndFlush(trainingGoal);
            targetLevel = SkillLevelResourceIT.createEntity();
        } else {
            targetLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(targetLevel);
        em.flush();
        trainingGoal.setTargetLevel(targetLevel);
        trainingGoalRepository.saveAndFlush(trainingGoal);
        Long targetLevelId = targetLevel.getId();
        // Get all the trainingGoalList where targetLevel equals to targetLevelId
        defaultTrainingGoalShouldBeFound("targetLevelId.equals=" + targetLevelId);

        // Get all the trainingGoalList where targetLevel equals to (targetLevelId + 1)
        defaultTrainingGoalShouldNotBeFound("targetLevelId.equals=" + (targetLevelId + 1));
    }

    private void defaultTrainingGoalFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrainingGoalShouldBeFound(shouldBeFound);
        defaultTrainingGoalShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrainingGoalShouldBeFound(String filter) throws Exception {
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingGoal.getId().intValue())))
            .andExpect(jsonPath("$.[*].goalName").value(hasItem(DEFAULT_GOAL_NAME)))
            .andExpect(jsonPath("$.[*].goalDescription").value(hasItem(DEFAULT_GOAL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].targetLevelDescription").value(hasItem(DEFAULT_TARGET_LEVEL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrainingGoalShouldNotBeFound(String filter) throws Exception {
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrainingGoalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrainingGoal() throws Exception {
        // Get the trainingGoal
        restTrainingGoalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainingGoal() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingGoal
        TrainingGoal updatedTrainingGoal = trainingGoalRepository.findById(trainingGoal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrainingGoal are not directly saved in db
        em.detach(updatedTrainingGoal);
        updatedTrainingGoal
            .goalName(UPDATED_GOAL_NAME)
            .goalDescription(UPDATED_GOAL_DESCRIPTION)
            .targetLevelDescription(UPDATED_TARGET_LEVEL_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .status(UPDATED_STATUS);
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(updatedTrainingGoal);

        restTrainingGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingGoalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingGoalDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainingGoalToMatchAllProperties(updatedTrainingGoal);
    }

    @Test
    @Transactional
    void putNonExistingTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingGoalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingGoalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingGoalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingGoalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingGoalWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingGoal using partial update
        TrainingGoal partialUpdatedTrainingGoal = new TrainingGoal();
        partialUpdatedTrainingGoal.setId(trainingGoal.getId());

        partialUpdatedTrainingGoal.goalName(UPDATED_GOAL_NAME).status(UPDATED_STATUS);

        restTrainingGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingGoal))
            )
            .andExpect(status().isOk());

        // Validate the TrainingGoal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingGoalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrainingGoal, trainingGoal),
            getPersistedTrainingGoal(trainingGoal)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrainingGoalWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingGoal using partial update
        TrainingGoal partialUpdatedTrainingGoal = new TrainingGoal();
        partialUpdatedTrainingGoal.setId(trainingGoal.getId());

        partialUpdatedTrainingGoal
            .goalName(UPDATED_GOAL_NAME)
            .goalDescription(UPDATED_GOAL_DESCRIPTION)
            .targetLevelDescription(UPDATED_TARGET_LEVEL_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .targetDate(UPDATED_TARGET_DATE)
            .status(UPDATED_STATUS);

        restTrainingGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingGoal))
            )
            .andExpect(status().isOk());

        // Validate the TrainingGoal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingGoalUpdatableFieldsEquals(partialUpdatedTrainingGoal, getPersistedTrainingGoal(partialUpdatedTrainingGoal));
    }

    @Test
    @Transactional
    void patchNonExistingTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingGoalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingGoalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingGoalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingGoal.setId(longCount.incrementAndGet());

        // Create the TrainingGoal
        TrainingGoalDTO trainingGoalDTO = trainingGoalMapper.toDto(trainingGoal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingGoalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainingGoalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingGoal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingGoal() throws Exception {
        // Initialize the database
        insertedTrainingGoal = trainingGoalRepository.saveAndFlush(trainingGoal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trainingGoal
        restTrainingGoalMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingGoal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainingGoalRepository.count();
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

    protected TrainingGoal getPersistedTrainingGoal(TrainingGoal trainingGoal) {
        return trainingGoalRepository.findById(trainingGoal.getId()).orElseThrow();
    }

    protected void assertPersistedTrainingGoalToMatchAllProperties(TrainingGoal expectedTrainingGoal) {
        assertTrainingGoalAllPropertiesEquals(expectedTrainingGoal, getPersistedTrainingGoal(expectedTrainingGoal));
    }

    protected void assertPersistedTrainingGoalToMatchUpdatableProperties(TrainingGoal expectedTrainingGoal) {
        assertTrainingGoalAllUpdatablePropertiesEquals(expectedTrainingGoal, getPersistedTrainingGoal(expectedTrainingGoal));
    }
}
