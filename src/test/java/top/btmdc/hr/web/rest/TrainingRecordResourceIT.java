package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.TrainingRecordAsserts.*;
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
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.TrainingRecord;
import top.btmdc.hr.domain.enumeration.TrainingType;
import top.btmdc.hr.repository.TrainingRecordRepository;
import top.btmdc.hr.service.TrainingRecordService;
import top.btmdc.hr.service.dto.TrainingRecordDTO;
import top.btmdc.hr.service.mapper.TrainingRecordMapper;

/**
 * Integration tests for the {@link TrainingRecordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrainingRecordResourceIT {

    private static final LocalDate DEFAULT_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRAINING_DATE = LocalDate.ofEpochDay(-1L);

    private static final TrainingType DEFAULT_TRAINING_TYPE = TrainingType.ONBOARDING;
    private static final TrainingType UPDATED_TRAINING_TYPE = TrainingType.SHADOWING;

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_TASK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TASK_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_EVIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/training-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainingRecordRepository trainingRecordRepository;

    @Mock
    private TrainingRecordRepository trainingRecordRepositoryMock;

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    @Mock
    private TrainingRecordService trainingRecordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingRecordMockMvc;

    private TrainingRecord trainingRecord;

    private TrainingRecord insertedTrainingRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingRecord createEntity(EntityManager em) {
        TrainingRecord trainingRecord = new TrainingRecord()
            .trainingDate(DEFAULT_TRAINING_DATE)
            .trainingType(DEFAULT_TRAINING_TYPE)
            .topic(DEFAULT_TOPIC)
            .taskDescription(DEFAULT_TASK_DESCRIPTION)
            .resultDescription(DEFAULT_RESULT_DESCRIPTION)
            .evidence(DEFAULT_EVIDENCE)
            .nextAction(DEFAULT_NEXT_ACTION);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        trainingRecord.setPerson(person);
        return trainingRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingRecord createUpdatedEntity(EntityManager em) {
        TrainingRecord updatedTrainingRecord = new TrainingRecord()
            .trainingDate(UPDATED_TRAINING_DATE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .topic(UPDATED_TOPIC)
            .taskDescription(UPDATED_TASK_DESCRIPTION)
            .resultDescription(UPDATED_RESULT_DESCRIPTION)
            .evidence(UPDATED_EVIDENCE)
            .nextAction(UPDATED_NEXT_ACTION);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedTrainingRecord.setPerson(person);
        return updatedTrainingRecord;
    }

    @BeforeEach
    void initTest() {
        trainingRecord = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTrainingRecord != null) {
            trainingRecordRepository.delete(insertedTrainingRecord);
            insertedTrainingRecord = null;
        }
    }

    @Test
    @Transactional
    void createTrainingRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);
        var returnedTrainingRecordDTO = om.readValue(
            restTrainingRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainingRecordDTO.class
        );

        // Validate the TrainingRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrainingRecord = trainingRecordMapper.toEntity(returnedTrainingRecordDTO);
        assertTrainingRecordUpdatableFieldsEquals(returnedTrainingRecord, getPersistedTrainingRecord(returnedTrainingRecord));

        insertedTrainingRecord = returnedTrainingRecord;
    }

    @Test
    @Transactional
    void createTrainingRecordWithExistingId() throws Exception {
        // Create the TrainingRecord with an existing ID
        trainingRecord.setId(1L);
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainingDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingRecord.setTrainingDate(null);

        // Create the TrainingRecord, which fails.
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        restTrainingRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrainingTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingRecord.setTrainingType(null);

        // Create the TrainingRecord, which fails.
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        restTrainingRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTopicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingRecord.setTopic(null);

        // Create the TrainingRecord, which fails.
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        restTrainingRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainingRecords() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingDate").value(hasItem(DEFAULT_TRAINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].trainingType").value(hasItem(DEFAULT_TRAINING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].taskDescription").value(hasItem(DEFAULT_TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].resultDescription").value(hasItem(DEFAULT_RESULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].nextAction").value(hasItem(DEFAULT_NEXT_ACTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingRecordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(trainingRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trainingRecordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingRecordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trainingRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trainingRecordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrainingRecord() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get the trainingRecord
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingRecord.getId().intValue()))
            .andExpect(jsonPath("$.trainingDate").value(DEFAULT_TRAINING_DATE.toString()))
            .andExpect(jsonPath("$.trainingType").value(DEFAULT_TRAINING_TYPE.toString()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.taskDescription").value(DEFAULT_TASK_DESCRIPTION))
            .andExpect(jsonPath("$.resultDescription").value(DEFAULT_RESULT_DESCRIPTION))
            .andExpect(jsonPath("$.evidence").value(DEFAULT_EVIDENCE))
            .andExpect(jsonPath("$.nextAction").value(DEFAULT_NEXT_ACTION));
    }

    @Test
    @Transactional
    void getTrainingRecordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        Long id = trainingRecord.getId();

        defaultTrainingRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrainingRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrainingRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate equals to
        defaultTrainingRecordFiltering("trainingDate.equals=" + DEFAULT_TRAINING_DATE, "trainingDate.equals=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate in
        defaultTrainingRecordFiltering(
            "trainingDate.in=" + DEFAULT_TRAINING_DATE + "," + UPDATED_TRAINING_DATE,
            "trainingDate.in=" + UPDATED_TRAINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate is not null
        defaultTrainingRecordFiltering("trainingDate.specified=true", "trainingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate is greater than or equal to
        defaultTrainingRecordFiltering(
            "trainingDate.greaterThanOrEqual=" + DEFAULT_TRAINING_DATE,
            "trainingDate.greaterThanOrEqual=" + UPDATED_TRAINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate is less than or equal to
        defaultTrainingRecordFiltering(
            "trainingDate.lessThanOrEqual=" + DEFAULT_TRAINING_DATE,
            "trainingDate.lessThanOrEqual=" + SMALLER_TRAINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate is less than
        defaultTrainingRecordFiltering("trainingDate.lessThan=" + UPDATED_TRAINING_DATE, "trainingDate.lessThan=" + DEFAULT_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingDate is greater than
        defaultTrainingRecordFiltering(
            "trainingDate.greaterThan=" + SMALLER_TRAINING_DATE,
            "trainingDate.greaterThan=" + DEFAULT_TRAINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingType equals to
        defaultTrainingRecordFiltering("trainingType.equals=" + DEFAULT_TRAINING_TYPE, "trainingType.equals=" + UPDATED_TRAINING_TYPE);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingType in
        defaultTrainingRecordFiltering(
            "trainingType.in=" + DEFAULT_TRAINING_TYPE + "," + UPDATED_TRAINING_TYPE,
            "trainingType.in=" + UPDATED_TRAINING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where trainingType is not null
        defaultTrainingRecordFiltering("trainingType.specified=true", "trainingType.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where topic equals to
        defaultTrainingRecordFiltering("topic.equals=" + DEFAULT_TOPIC, "topic.equals=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTopicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where topic in
        defaultTrainingRecordFiltering("topic.in=" + DEFAULT_TOPIC + "," + UPDATED_TOPIC, "topic.in=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTopicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where topic is not null
        defaultTrainingRecordFiltering("topic.specified=true", "topic.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTopicContainsSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where topic contains
        defaultTrainingRecordFiltering("topic.contains=" + DEFAULT_TOPIC, "topic.contains=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTopicNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        // Get all the trainingRecordList where topic does not contain
        defaultTrainingRecordFiltering("topic.doesNotContain=" + UPDATED_TOPIC, "topic.doesNotContain=" + DEFAULT_TOPIC);
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            trainingRecordRepository.saveAndFlush(trainingRecord);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        trainingRecord.setPerson(person);
        trainingRecordRepository.saveAndFlush(trainingRecord);
        Long personId = person.getId();
        // Get all the trainingRecordList where person equals to personId
        defaultTrainingRecordShouldBeFound("personId.equals=" + personId);

        // Get all the trainingRecordList where person equals to (personId + 1)
        defaultTrainingRecordShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByTrainingGoalIsEqualToSomething() throws Exception {
        TrainingGoal trainingGoal;
        if (TestUtil.findAll(em, TrainingGoal.class).isEmpty()) {
            trainingRecordRepository.saveAndFlush(trainingRecord);
            trainingGoal = TrainingGoalResourceIT.createEntity();
        } else {
            trainingGoal = TestUtil.findAll(em, TrainingGoal.class).get(0);
        }
        em.persist(trainingGoal);
        em.flush();
        trainingRecord.setTrainingGoal(trainingGoal);
        trainingRecordRepository.saveAndFlush(trainingRecord);
        Long trainingGoalId = trainingGoal.getId();
        // Get all the trainingRecordList where trainingGoal equals to trainingGoalId
        defaultTrainingRecordShouldBeFound("trainingGoalId.equals=" + trainingGoalId);

        // Get all the trainingRecordList where trainingGoal equals to (trainingGoalId + 1)
        defaultTrainingRecordShouldNotBeFound("trainingGoalId.equals=" + (trainingGoalId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            trainingRecordRepository.saveAndFlush(trainingRecord);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        trainingRecord.setPosition(position);
        trainingRecordRepository.saveAndFlush(trainingRecord);
        Long positionId = position.getId();
        // Get all the trainingRecordList where position equals to positionId
        defaultTrainingRecordShouldBeFound("positionId.equals=" + positionId);

        // Get all the trainingRecordList where position equals to (positionId + 1)
        defaultTrainingRecordShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllTrainingRecordsByMentorIsEqualToSomething() throws Exception {
        Person mentor;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            trainingRecordRepository.saveAndFlush(trainingRecord);
            mentor = PersonResourceIT.createEntity();
        } else {
            mentor = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(mentor);
        em.flush();
        trainingRecord.setMentor(mentor);
        trainingRecordRepository.saveAndFlush(trainingRecord);
        Long mentorId = mentor.getId();
        // Get all the trainingRecordList where mentor equals to mentorId
        defaultTrainingRecordShouldBeFound("mentorId.equals=" + mentorId);

        // Get all the trainingRecordList where mentor equals to (mentorId + 1)
        defaultTrainingRecordShouldNotBeFound("mentorId.equals=" + (mentorId + 1));
    }

    private void defaultTrainingRecordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrainingRecordShouldBeFound(shouldBeFound);
        defaultTrainingRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrainingRecordShouldBeFound(String filter) throws Exception {
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingDate").value(hasItem(DEFAULT_TRAINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].trainingType").value(hasItem(DEFAULT_TRAINING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].taskDescription").value(hasItem(DEFAULT_TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].resultDescription").value(hasItem(DEFAULT_RESULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].nextAction").value(hasItem(DEFAULT_NEXT_ACTION)));

        // Check, that the count call also returns 1
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrainingRecordShouldNotBeFound(String filter) throws Exception {
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrainingRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrainingRecord() throws Exception {
        // Get the trainingRecord
        restTrainingRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainingRecord() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingRecord
        TrainingRecord updatedTrainingRecord = trainingRecordRepository.findById(trainingRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrainingRecord are not directly saved in db
        em.detach(updatedTrainingRecord);
        updatedTrainingRecord
            .trainingDate(UPDATED_TRAINING_DATE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .topic(UPDATED_TOPIC)
            .taskDescription(UPDATED_TASK_DESCRIPTION)
            .resultDescription(UPDATED_RESULT_DESCRIPTION)
            .evidence(UPDATED_EVIDENCE)
            .nextAction(UPDATED_NEXT_ACTION);
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(updatedTrainingRecord);

        restTrainingRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainingRecordToMatchAllProperties(updatedTrainingRecord);
    }

    @Test
    @Transactional
    void putNonExistingTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingRecordWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingRecord using partial update
        TrainingRecord partialUpdatedTrainingRecord = new TrainingRecord();
        partialUpdatedTrainingRecord.setId(trainingRecord.getId());

        partialUpdatedTrainingRecord
            .trainingDate(UPDATED_TRAINING_DATE)
            .topic(UPDATED_TOPIC)
            .taskDescription(UPDATED_TASK_DESCRIPTION)
            .resultDescription(UPDATED_RESULT_DESCRIPTION);

        restTrainingRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingRecord))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrainingRecord, trainingRecord),
            getPersistedTrainingRecord(trainingRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrainingRecordWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingRecord using partial update
        TrainingRecord partialUpdatedTrainingRecord = new TrainingRecord();
        partialUpdatedTrainingRecord.setId(trainingRecord.getId());

        partialUpdatedTrainingRecord
            .trainingDate(UPDATED_TRAINING_DATE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .topic(UPDATED_TOPIC)
            .taskDescription(UPDATED_TASK_DESCRIPTION)
            .resultDescription(UPDATED_RESULT_DESCRIPTION)
            .evidence(UPDATED_EVIDENCE)
            .nextAction(UPDATED_NEXT_ACTION);

        restTrainingRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingRecord))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingRecordUpdatableFieldsEquals(partialUpdatedTrainingRecord, getPersistedTrainingRecord(partialUpdatedTrainingRecord));
    }

    @Test
    @Transactional
    void patchNonExistingTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingRecord.setId(longCount.incrementAndGet());

        // Create the TrainingRecord
        TrainingRecordDTO trainingRecordDTO = trainingRecordMapper.toDto(trainingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainingRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingRecord() throws Exception {
        // Initialize the database
        insertedTrainingRecord = trainingRecordRepository.saveAndFlush(trainingRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trainingRecord
        restTrainingRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainingRecordRepository.count();
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

    protected TrainingRecord getPersistedTrainingRecord(TrainingRecord trainingRecord) {
        return trainingRecordRepository.findById(trainingRecord.getId()).orElseThrow();
    }

    protected void assertPersistedTrainingRecordToMatchAllProperties(TrainingRecord expectedTrainingRecord) {
        assertTrainingRecordAllPropertiesEquals(expectedTrainingRecord, getPersistedTrainingRecord(expectedTrainingRecord));
    }

    protected void assertPersistedTrainingRecordToMatchUpdatableProperties(TrainingRecord expectedTrainingRecord) {
        assertTrainingRecordAllUpdatablePropertiesEquals(expectedTrainingRecord, getPersistedTrainingRecord(expectedTrainingRecord));
    }
}
