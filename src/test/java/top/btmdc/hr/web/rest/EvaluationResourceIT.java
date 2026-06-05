package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.EvaluationAsserts.*;
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
import top.btmdc.hr.domain.Evaluation;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.domain.enumeration.ProgressStatus;
import top.btmdc.hr.repository.EvaluationRepository;
import top.btmdc.hr.service.EvaluationService;
import top.btmdc.hr.service.dto.EvaluationDTO;
import top.btmdc.hr.service.mapper.EvaluationMapper;

/**
 * Integration tests for the {@link EvaluationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvaluationResourceIT {

    private static final String DEFAULT_EVALUATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVALUATION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EVALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EVALUATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PERIOD_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_LABEL = "BBBBBBBBBB";

    private static final ProgressStatus DEFAULT_PROGRESS_STATUS = ProgressStatus.NORMAL;
    private static final ProgressStatus UPDATED_PROGRESS_STATUS = ProgressStatus.SLOW;

    private static final AssessmentResult DEFAULT_RESULT = AssessmentResult.PASS;
    private static final AssessmentResult UPDATED_RESULT = AssessmentResult.WARNING;

    private static final String DEFAULT_STRENGTHS = "AAAAAAAAAA";
    private static final String UPDATED_STRENGTHS = "BBBBBBBBBB";

    private static final String DEFAULT_WEAKNESSES = "AAAAAAAAAA";
    private static final String UPDATED_WEAKNESSES = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_NEEDED = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_NEEDED = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_TRAINING_FOCUS = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_TRAINING_FOCUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_POSITION_ADJUSTMENT_NEEDED = false;
    private static final Boolean UPDATED_POSITION_ADJUSTMENT_NEEDED = true;

    private static final String ENTITY_API_URL = "/api/evaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Mock
    private EvaluationRepository evaluationRepositoryMock;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Mock
    private EvaluationService evaluationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    private Evaluation insertedEvaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation()
            .evaluationName(DEFAULT_EVALUATION_NAME)
            .evaluationDate(DEFAULT_EVALUATION_DATE)
            .periodLabel(DEFAULT_PERIOD_LABEL)
            .progressStatus(DEFAULT_PROGRESS_STATUS)
            .result(DEFAULT_RESULT)
            .strengths(DEFAULT_STRENGTHS)
            .weaknesses(DEFAULT_WEAKNESSES)
            .supportNeeded(DEFAULT_SUPPORT_NEEDED)
            .nextTrainingFocus(DEFAULT_NEXT_TRAINING_FOCUS)
            .positionAdjustmentNeeded(DEFAULT_POSITION_ADJUSTMENT_NEEDED);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        evaluation.setPerson(person);
        return evaluation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createUpdatedEntity(EntityManager em) {
        Evaluation updatedEvaluation = new Evaluation()
            .evaluationName(UPDATED_EVALUATION_NAME)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .periodLabel(UPDATED_PERIOD_LABEL)
            .progressStatus(UPDATED_PROGRESS_STATUS)
            .result(UPDATED_RESULT)
            .strengths(UPDATED_STRENGTHS)
            .weaknesses(UPDATED_WEAKNESSES)
            .supportNeeded(UPDATED_SUPPORT_NEEDED)
            .nextTrainingFocus(UPDATED_NEXT_TRAINING_FOCUS)
            .positionAdjustmentNeeded(UPDATED_POSITION_ADJUSTMENT_NEEDED);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedEvaluation.setPerson(person);
        return updatedEvaluation;
    }

    @BeforeEach
    void initTest() {
        evaluation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEvaluation != null) {
            evaluationRepository.delete(insertedEvaluation);
            insertedEvaluation = null;
        }
    }

    @Test
    @Transactional
    void createEvaluation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);
        var returnedEvaluationDTO = om.readValue(
            restEvaluationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvaluationDTO.class
        );

        // Validate the Evaluation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvaluation = evaluationMapper.toEntity(returnedEvaluationDTO);
        assertEvaluationUpdatableFieldsEquals(returnedEvaluation, getPersistedEvaluation(returnedEvaluation));

        insertedEvaluation = returnedEvaluation;
    }

    @Test
    @Transactional
    void createEvaluationWithExistingId() throws Exception {
        // Create the Evaluation with an existing ID
        evaluation.setId(1L);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEvaluationNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setEvaluationName(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEvaluationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setEvaluationDate(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluations() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluationName").value(hasItem(DEFAULT_EVALUATION_NAME)))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodLabel").value(hasItem(DEFAULT_PERIOD_LABEL)))
            .andExpect(jsonPath("$.[*].progressStatus").value(hasItem(DEFAULT_PROGRESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].strengths").value(hasItem(DEFAULT_STRENGTHS)))
            .andExpect(jsonPath("$.[*].weaknesses").value(hasItem(DEFAULT_WEAKNESSES)))
            .andExpect(jsonPath("$.[*].supportNeeded").value(hasItem(DEFAULT_SUPPORT_NEEDED)))
            .andExpect(jsonPath("$.[*].nextTrainingFocus").value(hasItem(DEFAULT_NEXT_TRAINING_FOCUS)))
            .andExpect(jsonPath("$.[*].positionAdjustmentNeeded").value(hasItem(DEFAULT_POSITION_ADJUSTMENT_NEEDED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(evaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evaluationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evaluationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.evaluationName").value(DEFAULT_EVALUATION_NAME))
            .andExpect(jsonPath("$.evaluationDate").value(DEFAULT_EVALUATION_DATE.toString()))
            .andExpect(jsonPath("$.periodLabel").value(DEFAULT_PERIOD_LABEL))
            .andExpect(jsonPath("$.progressStatus").value(DEFAULT_PROGRESS_STATUS.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.strengths").value(DEFAULT_STRENGTHS))
            .andExpect(jsonPath("$.weaknesses").value(DEFAULT_WEAKNESSES))
            .andExpect(jsonPath("$.supportNeeded").value(DEFAULT_SUPPORT_NEEDED))
            .andExpect(jsonPath("$.nextTrainingFocus").value(DEFAULT_NEXT_TRAINING_FOCUS))
            .andExpect(jsonPath("$.positionAdjustmentNeeded").value(DEFAULT_POSITION_ADJUSTMENT_NEEDED));
    }

    @Test
    @Transactional
    void getEvaluationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        Long id = evaluation.getId();

        defaultEvaluationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEvaluationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEvaluationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationName equals to
        defaultEvaluationFiltering("evaluationName.equals=" + DEFAULT_EVALUATION_NAME, "evaluationName.equals=" + UPDATED_EVALUATION_NAME);
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationName in
        defaultEvaluationFiltering(
            "evaluationName.in=" + DEFAULT_EVALUATION_NAME + "," + UPDATED_EVALUATION_NAME,
            "evaluationName.in=" + UPDATED_EVALUATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationName is not null
        defaultEvaluationFiltering("evaluationName.specified=true", "evaluationName.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationName contains
        defaultEvaluationFiltering(
            "evaluationName.contains=" + DEFAULT_EVALUATION_NAME,
            "evaluationName.contains=" + UPDATED_EVALUATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationName does not contain
        defaultEvaluationFiltering(
            "evaluationName.doesNotContain=" + UPDATED_EVALUATION_NAME,
            "evaluationName.doesNotContain=" + DEFAULT_EVALUATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate equals to
        defaultEvaluationFiltering("evaluationDate.equals=" + DEFAULT_EVALUATION_DATE, "evaluationDate.equals=" + UPDATED_EVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate in
        defaultEvaluationFiltering(
            "evaluationDate.in=" + DEFAULT_EVALUATION_DATE + "," + UPDATED_EVALUATION_DATE,
            "evaluationDate.in=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate is not null
        defaultEvaluationFiltering("evaluationDate.specified=true", "evaluationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate is greater than or equal to
        defaultEvaluationFiltering(
            "evaluationDate.greaterThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.greaterThanOrEqual=" + UPDATED_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate is less than or equal to
        defaultEvaluationFiltering(
            "evaluationDate.lessThanOrEqual=" + DEFAULT_EVALUATION_DATE,
            "evaluationDate.lessThanOrEqual=" + SMALLER_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate is less than
        defaultEvaluationFiltering(
            "evaluationDate.lessThan=" + UPDATED_EVALUATION_DATE,
            "evaluationDate.lessThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where evaluationDate is greater than
        defaultEvaluationFiltering(
            "evaluationDate.greaterThan=" + SMALLER_EVALUATION_DATE,
            "evaluationDate.greaterThan=" + DEFAULT_EVALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByPeriodLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where periodLabel equals to
        defaultEvaluationFiltering("periodLabel.equals=" + DEFAULT_PERIOD_LABEL, "periodLabel.equals=" + UPDATED_PERIOD_LABEL);
    }

    @Test
    @Transactional
    void getAllEvaluationsByPeriodLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where periodLabel in
        defaultEvaluationFiltering(
            "periodLabel.in=" + DEFAULT_PERIOD_LABEL + "," + UPDATED_PERIOD_LABEL,
            "periodLabel.in=" + UPDATED_PERIOD_LABEL
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByPeriodLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where periodLabel is not null
        defaultEvaluationFiltering("periodLabel.specified=true", "periodLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByPeriodLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where periodLabel contains
        defaultEvaluationFiltering("periodLabel.contains=" + DEFAULT_PERIOD_LABEL, "periodLabel.contains=" + UPDATED_PERIOD_LABEL);
    }

    @Test
    @Transactional
    void getAllEvaluationsByPeriodLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where periodLabel does not contain
        defaultEvaluationFiltering(
            "periodLabel.doesNotContain=" + UPDATED_PERIOD_LABEL,
            "periodLabel.doesNotContain=" + DEFAULT_PERIOD_LABEL
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByProgressStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where progressStatus equals to
        defaultEvaluationFiltering("progressStatus.equals=" + DEFAULT_PROGRESS_STATUS, "progressStatus.equals=" + UPDATED_PROGRESS_STATUS);
    }

    @Test
    @Transactional
    void getAllEvaluationsByProgressStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where progressStatus in
        defaultEvaluationFiltering(
            "progressStatus.in=" + DEFAULT_PROGRESS_STATUS + "," + UPDATED_PROGRESS_STATUS,
            "progressStatus.in=" + UPDATED_PROGRESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByProgressStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where progressStatus is not null
        defaultEvaluationFiltering("progressStatus.specified=true", "progressStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where result equals to
        defaultEvaluationFiltering("result.equals=" + DEFAULT_RESULT, "result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllEvaluationsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where result in
        defaultEvaluationFiltering("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT, "result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllEvaluationsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where result is not null
        defaultEvaluationFiltering("result.specified=true", "result.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByPositionAdjustmentNeededIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where positionAdjustmentNeeded equals to
        defaultEvaluationFiltering(
            "positionAdjustmentNeeded.equals=" + DEFAULT_POSITION_ADJUSTMENT_NEEDED,
            "positionAdjustmentNeeded.equals=" + UPDATED_POSITION_ADJUSTMENT_NEEDED
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByPositionAdjustmentNeededIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where positionAdjustmentNeeded in
        defaultEvaluationFiltering(
            "positionAdjustmentNeeded.in=" + DEFAULT_POSITION_ADJUSTMENT_NEEDED + "," + UPDATED_POSITION_ADJUSTMENT_NEEDED,
            "positionAdjustmentNeeded.in=" + UPDATED_POSITION_ADJUSTMENT_NEEDED
        );
    }

    @Test
    @Transactional
    void getAllEvaluationsByPositionAdjustmentNeededIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList where positionAdjustmentNeeded is not null
        defaultEvaluationFiltering("positionAdjustmentNeeded.specified=true", "positionAdjustmentNeeded.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            evaluationRepository.saveAndFlush(evaluation);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        evaluation.setPerson(person);
        evaluationRepository.saveAndFlush(evaluation);
        Long personId = person.getId();
        // Get all the evaluationList where person equals to personId
        defaultEvaluationShouldBeFound("personId.equals=" + personId);

        // Get all the evaluationList where person equals to (personId + 1)
        defaultEvaluationShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            evaluationRepository.saveAndFlush(evaluation);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        evaluation.setPosition(position);
        evaluationRepository.saveAndFlush(evaluation);
        Long positionId = position.getId();
        // Get all the evaluationList where position equals to positionId
        defaultEvaluationShouldBeFound("positionId.equals=" + positionId);

        // Get all the evaluationList where position equals to (positionId + 1)
        defaultEvaluationShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationsByTrainingGoalIsEqualToSomething() throws Exception {
        TrainingGoal trainingGoal;
        if (TestUtil.findAll(em, TrainingGoal.class).isEmpty()) {
            evaluationRepository.saveAndFlush(evaluation);
            trainingGoal = TrainingGoalResourceIT.createEntity();
        } else {
            trainingGoal = TestUtil.findAll(em, TrainingGoal.class).get(0);
        }
        em.persist(trainingGoal);
        em.flush();
        evaluation.setTrainingGoal(trainingGoal);
        evaluationRepository.saveAndFlush(evaluation);
        Long trainingGoalId = trainingGoal.getId();
        // Get all the evaluationList where trainingGoal equals to trainingGoalId
        defaultEvaluationShouldBeFound("trainingGoalId.equals=" + trainingGoalId);

        // Get all the evaluationList where trainingGoal equals to (trainingGoalId + 1)
        defaultEvaluationShouldNotBeFound("trainingGoalId.equals=" + (trainingGoalId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationsByEvaluatorIsEqualToSomething() throws Exception {
        Person evaluator;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            evaluationRepository.saveAndFlush(evaluation);
            evaluator = PersonResourceIT.createEntity();
        } else {
            evaluator = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(evaluator);
        em.flush();
        evaluation.setEvaluator(evaluator);
        evaluationRepository.saveAndFlush(evaluation);
        Long evaluatorId = evaluator.getId();
        // Get all the evaluationList where evaluator equals to evaluatorId
        defaultEvaluationShouldBeFound("evaluatorId.equals=" + evaluatorId);

        // Get all the evaluationList where evaluator equals to (evaluatorId + 1)
        defaultEvaluationShouldNotBeFound("evaluatorId.equals=" + (evaluatorId + 1));
    }

    private void defaultEvaluationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEvaluationShouldBeFound(shouldBeFound);
        defaultEvaluationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluationShouldBeFound(String filter) throws Exception {
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluationName").value(hasItem(DEFAULT_EVALUATION_NAME)))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodLabel").value(hasItem(DEFAULT_PERIOD_LABEL)))
            .andExpect(jsonPath("$.[*].progressStatus").value(hasItem(DEFAULT_PROGRESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].strengths").value(hasItem(DEFAULT_STRENGTHS)))
            .andExpect(jsonPath("$.[*].weaknesses").value(hasItem(DEFAULT_WEAKNESSES)))
            .andExpect(jsonPath("$.[*].supportNeeded").value(hasItem(DEFAULT_SUPPORT_NEEDED)))
            .andExpect(jsonPath("$.[*].nextTrainingFocus").value(hasItem(DEFAULT_NEXT_TRAINING_FOCUS)))
            .andExpect(jsonPath("$.[*].positionAdjustmentNeeded").value(hasItem(DEFAULT_POSITION_ADJUSTMENT_NEEDED)));

        // Check, that the count call also returns 1
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvaluationShouldNotBeFound(String filter) throws Exception {
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findById(evaluation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvaluation are not directly saved in db
        em.detach(updatedEvaluation);
        updatedEvaluation
            .evaluationName(UPDATED_EVALUATION_NAME)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .periodLabel(UPDATED_PERIOD_LABEL)
            .progressStatus(UPDATED_PROGRESS_STATUS)
            .result(UPDATED_RESULT)
            .strengths(UPDATED_STRENGTHS)
            .weaknesses(UPDATED_WEAKNESSES)
            .supportNeeded(UPDATED_SUPPORT_NEEDED)
            .nextTrainingFocus(UPDATED_NEXT_TRAINING_FOCUS)
            .positionAdjustmentNeeded(UPDATED_POSITION_ADJUSTMENT_NEEDED);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(updatedEvaluation);

        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvaluationToMatchAllProperties(updatedEvaluation);
    }

    @Test
    @Transactional
    void putNonExistingEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        partialUpdatedEvaluation
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .weaknesses(UPDATED_WEAKNESSES)
            .nextTrainingFocus(UPDATED_NEXT_TRAINING_FOCUS)
            .positionAdjustmentNeeded(UPDATED_POSITION_ADJUSTMENT_NEEDED);

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvaluation, evaluation),
            getPersistedEvaluation(evaluation)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        partialUpdatedEvaluation
            .evaluationName(UPDATED_EVALUATION_NAME)
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .periodLabel(UPDATED_PERIOD_LABEL)
            .progressStatus(UPDATED_PROGRESS_STATUS)
            .result(UPDATED_RESULT)
            .strengths(UPDATED_STRENGTHS)
            .weaknesses(UPDATED_WEAKNESSES)
            .supportNeeded(UPDATED_SUPPORT_NEEDED)
            .nextTrainingFocus(UPDATED_NEXT_TRAINING_FOCUS)
            .positionAdjustmentNeeded(UPDATED_POSITION_ADJUSTMENT_NEEDED);

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationUpdatableFieldsEquals(partialUpdatedEvaluation, getPersistedEvaluation(partialUpdatedEvaluation));
    }

    @Test
    @Transactional
    void patchNonExistingEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evaluation
        restEvaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evaluationRepository.count();
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

    protected Evaluation getPersistedEvaluation(Evaluation evaluation) {
        return evaluationRepository.findById(evaluation.getId()).orElseThrow();
    }

    protected void assertPersistedEvaluationToMatchAllProperties(Evaluation expectedEvaluation) {
        assertEvaluationAllPropertiesEquals(expectedEvaluation, getPersistedEvaluation(expectedEvaluation));
    }

    protected void assertPersistedEvaluationToMatchUpdatableProperties(Evaluation expectedEvaluation) {
        assertEvaluationAllUpdatablePropertiesEquals(expectedEvaluation, getPersistedEvaluation(expectedEvaluation));
    }
}
