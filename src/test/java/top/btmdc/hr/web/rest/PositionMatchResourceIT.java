package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionMatchAsserts.*;
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
import top.btmdc.hr.domain.PositionMatch;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.Recommendation;
import top.btmdc.hr.repository.PositionMatchRepository;
import top.btmdc.hr.service.PositionMatchService;
import top.btmdc.hr.service.dto.PositionMatchDTO;
import top.btmdc.hr.service.mapper.PositionMatchMapper;

/**
 * Integration tests for the {@link PositionMatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PositionMatchResourceIT {

    private static final Integer DEFAULT_MATCH_SCORE = 0;
    private static final Integer UPDATED_MATCH_SCORE = 1;
    private static final Integer SMALLER_MATCH_SCORE = 0 - 1;

    private static final String DEFAULT_MATCHED_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_MATCHED_SKILLS = "BBBBBBBBBB";

    private static final String DEFAULT_GAP_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_GAP_SKILLS = "BBBBBBBBBB";

    private static final ReadinessLevel DEFAULT_READINESS = ReadinessLevel.IMMEDIATE;
    private static final ReadinessLevel UPDATED_READINESS = ReadinessLevel.THREE_MONTHS;

    private static final Recommendation DEFAULT_RECOMMENDATION = Recommendation.FIT;
    private static final Recommendation UPDATED_RECOMMENDATION = Recommendation.TRAINABLE;

    private static final LocalDate DEFAULT_ANALYSIS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANALYSIS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ANALYSIS_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/position-matches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionMatchRepository positionMatchRepository;

    @Mock
    private PositionMatchRepository positionMatchRepositoryMock;

    @Autowired
    private PositionMatchMapper positionMatchMapper;

    @Mock
    private PositionMatchService positionMatchServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionMatchMockMvc;

    private PositionMatch positionMatch;

    private PositionMatch insertedPositionMatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionMatch createEntity(EntityManager em) {
        PositionMatch positionMatch = new PositionMatch()
            .matchScore(DEFAULT_MATCH_SCORE)
            .matchedSkills(DEFAULT_MATCHED_SKILLS)
            .gapSkills(DEFAULT_GAP_SKILLS)
            .readiness(DEFAULT_READINESS)
            .recommendation(DEFAULT_RECOMMENDATION)
            .analysisDate(DEFAULT_ANALYSIS_DATE)
            .remark(DEFAULT_REMARK);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        positionMatch.setPerson(person);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        positionMatch.setPosition(position);
        return positionMatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionMatch createUpdatedEntity(EntityManager em) {
        PositionMatch updatedPositionMatch = new PositionMatch()
            .matchScore(UPDATED_MATCH_SCORE)
            .matchedSkills(UPDATED_MATCHED_SKILLS)
            .gapSkills(UPDATED_GAP_SKILLS)
            .readiness(UPDATED_READINESS)
            .recommendation(UPDATED_RECOMMENDATION)
            .analysisDate(UPDATED_ANALYSIS_DATE)
            .remark(UPDATED_REMARK);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedPositionMatch.setPerson(person);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedPositionMatch.setPosition(position);
        return updatedPositionMatch;
    }

    @BeforeEach
    void initTest() {
        positionMatch = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPositionMatch != null) {
            positionMatchRepository.delete(insertedPositionMatch);
            insertedPositionMatch = null;
        }
    }

    @Test
    @Transactional
    void createPositionMatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);
        var returnedPositionMatchDTO = om.readValue(
            restPositionMatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionMatchDTO.class
        );

        // Validate the PositionMatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPositionMatch = positionMatchMapper.toEntity(returnedPositionMatchDTO);
        assertPositionMatchUpdatableFieldsEquals(returnedPositionMatch, getPersistedPositionMatch(returnedPositionMatch));

        insertedPositionMatch = returnedPositionMatch;
    }

    @Test
    @Transactional
    void createPositionMatchWithExistingId() throws Exception {
        // Create the PositionMatch with an existing ID
        positionMatch.setId(1L);
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReadinessIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionMatch.setReadiness(null);

        // Create the PositionMatch, which fails.
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        restPositionMatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecommendationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionMatch.setRecommendation(null);

        // Create the PositionMatch, which fails.
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        restPositionMatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnalysisDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionMatch.setAnalysisDate(null);

        // Create the PositionMatch, which fails.
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        restPositionMatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionMatches() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionMatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].matchScore").value(hasItem(DEFAULT_MATCH_SCORE)))
            .andExpect(jsonPath("$.[*].matchedSkills").value(hasItem(DEFAULT_MATCHED_SKILLS)))
            .andExpect(jsonPath("$.[*].gapSkills").value(hasItem(DEFAULT_GAP_SKILLS)))
            .andExpect(jsonPath("$.[*].readiness").value(hasItem(DEFAULT_READINESS.toString())))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].analysisDate").value(hasItem(DEFAULT_ANALYSIS_DATE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionMatchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(positionMatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionMatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(positionMatchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionMatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(positionMatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionMatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(positionMatchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPositionMatch() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get the positionMatch
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL_ID, positionMatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionMatch.getId().intValue()))
            .andExpect(jsonPath("$.matchScore").value(DEFAULT_MATCH_SCORE))
            .andExpect(jsonPath("$.matchedSkills").value(DEFAULT_MATCHED_SKILLS))
            .andExpect(jsonPath("$.gapSkills").value(DEFAULT_GAP_SKILLS))
            .andExpect(jsonPath("$.readiness").value(DEFAULT_READINESS.toString()))
            .andExpect(jsonPath("$.recommendation").value(DEFAULT_RECOMMENDATION.toString()))
            .andExpect(jsonPath("$.analysisDate").value(DEFAULT_ANALYSIS_DATE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    @Transactional
    void getPositionMatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        Long id = positionMatch.getId();

        defaultPositionMatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionMatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionMatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore equals to
        defaultPositionMatchFiltering("matchScore.equals=" + DEFAULT_MATCH_SCORE, "matchScore.equals=" + UPDATED_MATCH_SCORE);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore in
        defaultPositionMatchFiltering(
            "matchScore.in=" + DEFAULT_MATCH_SCORE + "," + UPDATED_MATCH_SCORE,
            "matchScore.in=" + UPDATED_MATCH_SCORE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore is not null
        defaultPositionMatchFiltering("matchScore.specified=true", "matchScore.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore is greater than or equal to
        defaultPositionMatchFiltering(
            "matchScore.greaterThanOrEqual=" + DEFAULT_MATCH_SCORE,
            "matchScore.greaterThanOrEqual=" + (DEFAULT_MATCH_SCORE + 1)
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore is less than or equal to
        defaultPositionMatchFiltering(
            "matchScore.lessThanOrEqual=" + DEFAULT_MATCH_SCORE,
            "matchScore.lessThanOrEqual=" + SMALLER_MATCH_SCORE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore is less than
        defaultPositionMatchFiltering("matchScore.lessThan=" + (DEFAULT_MATCH_SCORE + 1), "matchScore.lessThan=" + DEFAULT_MATCH_SCORE);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByMatchScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where matchScore is greater than
        defaultPositionMatchFiltering("matchScore.greaterThan=" + SMALLER_MATCH_SCORE, "matchScore.greaterThan=" + DEFAULT_MATCH_SCORE);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByReadinessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where readiness equals to
        defaultPositionMatchFiltering("readiness.equals=" + DEFAULT_READINESS, "readiness.equals=" + UPDATED_READINESS);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByReadinessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where readiness in
        defaultPositionMatchFiltering("readiness.in=" + DEFAULT_READINESS + "," + UPDATED_READINESS, "readiness.in=" + UPDATED_READINESS);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByReadinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where readiness is not null
        defaultPositionMatchFiltering("readiness.specified=true", "readiness.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionMatchesByRecommendationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where recommendation equals to
        defaultPositionMatchFiltering("recommendation.equals=" + DEFAULT_RECOMMENDATION, "recommendation.equals=" + UPDATED_RECOMMENDATION);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByRecommendationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where recommendation in
        defaultPositionMatchFiltering(
            "recommendation.in=" + DEFAULT_RECOMMENDATION + "," + UPDATED_RECOMMENDATION,
            "recommendation.in=" + UPDATED_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByRecommendationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where recommendation is not null
        defaultPositionMatchFiltering("recommendation.specified=true", "recommendation.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate equals to
        defaultPositionMatchFiltering("analysisDate.equals=" + DEFAULT_ANALYSIS_DATE, "analysisDate.equals=" + UPDATED_ANALYSIS_DATE);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate in
        defaultPositionMatchFiltering(
            "analysisDate.in=" + DEFAULT_ANALYSIS_DATE + "," + UPDATED_ANALYSIS_DATE,
            "analysisDate.in=" + UPDATED_ANALYSIS_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate is not null
        defaultPositionMatchFiltering("analysisDate.specified=true", "analysisDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate is greater than or equal to
        defaultPositionMatchFiltering(
            "analysisDate.greaterThanOrEqual=" + DEFAULT_ANALYSIS_DATE,
            "analysisDate.greaterThanOrEqual=" + UPDATED_ANALYSIS_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate is less than or equal to
        defaultPositionMatchFiltering(
            "analysisDate.lessThanOrEqual=" + DEFAULT_ANALYSIS_DATE,
            "analysisDate.lessThanOrEqual=" + SMALLER_ANALYSIS_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate is less than
        defaultPositionMatchFiltering("analysisDate.lessThan=" + UPDATED_ANALYSIS_DATE, "analysisDate.lessThan=" + DEFAULT_ANALYSIS_DATE);
    }

    @Test
    @Transactional
    void getAllPositionMatchesByAnalysisDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        // Get all the positionMatchList where analysisDate is greater than
        defaultPositionMatchFiltering(
            "analysisDate.greaterThan=" + SMALLER_ANALYSIS_DATE,
            "analysisDate.greaterThan=" + DEFAULT_ANALYSIS_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionMatchesByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            positionMatchRepository.saveAndFlush(positionMatch);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        positionMatch.setPerson(person);
        positionMatchRepository.saveAndFlush(positionMatch);
        Long personId = person.getId();
        // Get all the positionMatchList where person equals to personId
        defaultPositionMatchShouldBeFound("personId.equals=" + personId);

        // Get all the positionMatchList where person equals to (personId + 1)
        defaultPositionMatchShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllPositionMatchesByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            positionMatchRepository.saveAndFlush(positionMatch);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        positionMatch.setPosition(position);
        positionMatchRepository.saveAndFlush(positionMatch);
        Long positionId = position.getId();
        // Get all the positionMatchList where position equals to positionId
        defaultPositionMatchShouldBeFound("positionId.equals=" + positionId);

        // Get all the positionMatchList where position equals to (positionId + 1)
        defaultPositionMatchShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    private void defaultPositionMatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionMatchShouldBeFound(shouldBeFound);
        defaultPositionMatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionMatchShouldBeFound(String filter) throws Exception {
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionMatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].matchScore").value(hasItem(DEFAULT_MATCH_SCORE)))
            .andExpect(jsonPath("$.[*].matchedSkills").value(hasItem(DEFAULT_MATCHED_SKILLS)))
            .andExpect(jsonPath("$.[*].gapSkills").value(hasItem(DEFAULT_GAP_SKILLS)))
            .andExpect(jsonPath("$.[*].readiness").value(hasItem(DEFAULT_READINESS.toString())))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].analysisDate").value(hasItem(DEFAULT_ANALYSIS_DATE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));

        // Check, that the count call also returns 1
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionMatchShouldNotBeFound(String filter) throws Exception {
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPositionMatch() throws Exception {
        // Get the positionMatch
        restPositionMatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPositionMatch() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionMatch
        PositionMatch updatedPositionMatch = positionMatchRepository.findById(positionMatch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPositionMatch are not directly saved in db
        em.detach(updatedPositionMatch);
        updatedPositionMatch
            .matchScore(UPDATED_MATCH_SCORE)
            .matchedSkills(UPDATED_MATCHED_SKILLS)
            .gapSkills(UPDATED_GAP_SKILLS)
            .readiness(UPDATED_READINESS)
            .recommendation(UPDATED_RECOMMENDATION)
            .analysisDate(UPDATED_ANALYSIS_DATE)
            .remark(UPDATED_REMARK);
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(updatedPositionMatch);

        restPositionMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionMatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionMatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionMatchToMatchAllProperties(updatedPositionMatch);
    }

    @Test
    @Transactional
    void putNonExistingPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionMatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionMatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionMatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionMatchWithPatch() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionMatch using partial update
        PositionMatch partialUpdatedPositionMatch = new PositionMatch();
        partialUpdatedPositionMatch.setId(positionMatch.getId());

        partialUpdatedPositionMatch
            .matchScore(UPDATED_MATCH_SCORE)
            .matchedSkills(UPDATED_MATCHED_SKILLS)
            .gapSkills(UPDATED_GAP_SKILLS)
            .readiness(UPDATED_READINESS)
            .recommendation(UPDATED_RECOMMENDATION);

        restPositionMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionMatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionMatch))
            )
            .andExpect(status().isOk());

        // Validate the PositionMatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionMatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPositionMatch, positionMatch),
            getPersistedPositionMatch(positionMatch)
        );
    }

    @Test
    @Transactional
    void fullUpdatePositionMatchWithPatch() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionMatch using partial update
        PositionMatch partialUpdatedPositionMatch = new PositionMatch();
        partialUpdatedPositionMatch.setId(positionMatch.getId());

        partialUpdatedPositionMatch
            .matchScore(UPDATED_MATCH_SCORE)
            .matchedSkills(UPDATED_MATCHED_SKILLS)
            .gapSkills(UPDATED_GAP_SKILLS)
            .readiness(UPDATED_READINESS)
            .recommendation(UPDATED_RECOMMENDATION)
            .analysisDate(UPDATED_ANALYSIS_DATE)
            .remark(UPDATED_REMARK);

        restPositionMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionMatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionMatch))
            )
            .andExpect(status().isOk());

        // Validate the PositionMatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionMatchUpdatableFieldsEquals(partialUpdatedPositionMatch, getPersistedPositionMatch(partialUpdatedPositionMatch));
    }

    @Test
    @Transactional
    void patchNonExistingPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionMatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionMatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionMatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionMatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionMatch.setId(longCount.incrementAndGet());

        // Create the PositionMatch
        PositionMatchDTO positionMatchDTO = positionMatchMapper.toDto(positionMatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionMatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionMatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionMatch() throws Exception {
        // Initialize the database
        insertedPositionMatch = positionMatchRepository.saveAndFlush(positionMatch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the positionMatch
        restPositionMatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionMatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionMatchRepository.count();
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

    protected PositionMatch getPersistedPositionMatch(PositionMatch positionMatch) {
        return positionMatchRepository.findById(positionMatch.getId()).orElseThrow();
    }

    protected void assertPersistedPositionMatchToMatchAllProperties(PositionMatch expectedPositionMatch) {
        assertPositionMatchAllPropertiesEquals(expectedPositionMatch, getPersistedPositionMatch(expectedPositionMatch));
    }

    protected void assertPersistedPositionMatchToMatchUpdatableProperties(PositionMatch expectedPositionMatch) {
        assertPositionMatchAllUpdatablePropertiesEquals(expectedPositionMatch, getPersistedPositionMatch(expectedPositionMatch));
    }
}
