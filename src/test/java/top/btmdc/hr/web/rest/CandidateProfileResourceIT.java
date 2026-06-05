package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.CandidateProfileAsserts.*;
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
import top.btmdc.hr.domain.CandidateProfile;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.enumeration.CandidateJudgement;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.repository.CandidateProfileRepository;
import top.btmdc.hr.service.CandidateProfileService;
import top.btmdc.hr.service.dto.CandidateProfileDTO;
import top.btmdc.hr.service.mapper.CandidateProfileMapper;

/**
 * Integration tests for the {@link CandidateProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CandidateProfileResourceIT {

    private static final LocalDate DEFAULT_CANDIDATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANDIDATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANDIDATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CULTIVATE_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_CULTIVATE_DIRECTION = "BBBBBBBBBB";

    private static final ImportanceLevel DEFAULT_STABILITY = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_STABILITY = ImportanceLevel.MEDIUM;

    private static final ImportanceLevel DEFAULT_LEARNING_ABILITY = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_LEARNING_ABILITY = ImportanceLevel.MEDIUM;

    private static final ImportanceLevel DEFAULT_COMMUNICATION_COORDINATION = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_COMMUNICATION_COORDINATION = ImportanceLevel.MEDIUM;

    private static final ImportanceLevel DEFAULT_BUSINESS_UNDERSTANDING = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_BUSINESS_UNDERSTANDING = ImportanceLevel.MEDIUM;

    private static final ImportanceLevel DEFAULT_RESPONSIBILITY = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_RESPONSIBILITY = ImportanceLevel.MEDIUM;

    private static final ImportanceLevel DEFAULT_RISK_AWARENESS = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_RISK_AWARENESS = ImportanceLevel.MEDIUM;

    private static final CandidateJudgement DEFAULT_JUDGEMENT = CandidateJudgement.CORE_CANDIDATE;
    private static final CandidateJudgement UPDATED_JUDGEMENT = CandidateJudgement.OBSERVE;

    private static final String DEFAULT_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_EVIDENCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/candidate-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    @Mock
    private CandidateProfileRepository candidateProfileRepositoryMock;

    @Autowired
    private CandidateProfileMapper candidateProfileMapper;

    @Mock
    private CandidateProfileService candidateProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidateProfileMockMvc;

    private CandidateProfile candidateProfile;

    private CandidateProfile insertedCandidateProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidateProfile createEntity(EntityManager em) {
        CandidateProfile candidateProfile = new CandidateProfile()
            .candidateDate(DEFAULT_CANDIDATE_DATE)
            .cultivateDirection(DEFAULT_CULTIVATE_DIRECTION)
            .stability(DEFAULT_STABILITY)
            .learningAbility(DEFAULT_LEARNING_ABILITY)
            .communicationCoordination(DEFAULT_COMMUNICATION_COORDINATION)
            .businessUnderstanding(DEFAULT_BUSINESS_UNDERSTANDING)
            .responsibility(DEFAULT_RESPONSIBILITY)
            .riskAwareness(DEFAULT_RISK_AWARENESS)
            .judgement(DEFAULT_JUDGEMENT)
            .evidence(DEFAULT_EVIDENCE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        candidateProfile.setPerson(person);
        return candidateProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidateProfile createUpdatedEntity(EntityManager em) {
        CandidateProfile updatedCandidateProfile = new CandidateProfile()
            .candidateDate(UPDATED_CANDIDATE_DATE)
            .cultivateDirection(UPDATED_CULTIVATE_DIRECTION)
            .stability(UPDATED_STABILITY)
            .learningAbility(UPDATED_LEARNING_ABILITY)
            .communicationCoordination(UPDATED_COMMUNICATION_COORDINATION)
            .businessUnderstanding(UPDATED_BUSINESS_UNDERSTANDING)
            .responsibility(UPDATED_RESPONSIBILITY)
            .riskAwareness(UPDATED_RISK_AWARENESS)
            .judgement(UPDATED_JUDGEMENT)
            .evidence(UPDATED_EVIDENCE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedCandidateProfile.setPerson(person);
        return updatedCandidateProfile;
    }

    @BeforeEach
    void initTest() {
        candidateProfile = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCandidateProfile != null) {
            candidateProfileRepository.delete(insertedCandidateProfile);
            insertedCandidateProfile = null;
        }
    }

    @Test
    @Transactional
    void createCandidateProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);
        var returnedCandidateProfileDTO = om.readValue(
            restCandidateProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CandidateProfileDTO.class
        );

        // Validate the CandidateProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCandidateProfile = candidateProfileMapper.toEntity(returnedCandidateProfileDTO);
        assertCandidateProfileUpdatableFieldsEquals(returnedCandidateProfile, getPersistedCandidateProfile(returnedCandidateProfile));

        insertedCandidateProfile = returnedCandidateProfile;
    }

    @Test
    @Transactional
    void createCandidateProfileWithExistingId() throws Exception {
        // Create the CandidateProfile with an existing ID
        candidateProfile.setId(1L);
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCandidateDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateProfile.setCandidateDate(null);

        // Create the CandidateProfile, which fails.
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        restCandidateProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJudgementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateProfile.setJudgement(null);

        // Create the CandidateProfile, which fails.
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        restCandidateProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCandidateProfiles() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateDate").value(hasItem(DEFAULT_CANDIDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].cultivateDirection").value(hasItem(DEFAULT_CULTIVATE_DIRECTION)))
            .andExpect(jsonPath("$.[*].stability").value(hasItem(DEFAULT_STABILITY.toString())))
            .andExpect(jsonPath("$.[*].learningAbility").value(hasItem(DEFAULT_LEARNING_ABILITY.toString())))
            .andExpect(jsonPath("$.[*].communicationCoordination").value(hasItem(DEFAULT_COMMUNICATION_COORDINATION.toString())))
            .andExpect(jsonPath("$.[*].businessUnderstanding").value(hasItem(DEFAULT_BUSINESS_UNDERSTANDING.toString())))
            .andExpect(jsonPath("$.[*].responsibility").value(hasItem(DEFAULT_RESPONSIBILITY.toString())))
            .andExpect(jsonPath("$.[*].riskAwareness").value(hasItem(DEFAULT_RISK_AWARENESS.toString())))
            .andExpect(jsonPath("$.[*].judgement").value(hasItem(DEFAULT_JUDGEMENT.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCandidateProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(candidateProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(candidateProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCandidateProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(candidateProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(candidateProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCandidateProfile() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get the candidateProfile
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, candidateProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidateProfile.getId().intValue()))
            .andExpect(jsonPath("$.candidateDate").value(DEFAULT_CANDIDATE_DATE.toString()))
            .andExpect(jsonPath("$.cultivateDirection").value(DEFAULT_CULTIVATE_DIRECTION))
            .andExpect(jsonPath("$.stability").value(DEFAULT_STABILITY.toString()))
            .andExpect(jsonPath("$.learningAbility").value(DEFAULT_LEARNING_ABILITY.toString()))
            .andExpect(jsonPath("$.communicationCoordination").value(DEFAULT_COMMUNICATION_COORDINATION.toString()))
            .andExpect(jsonPath("$.businessUnderstanding").value(DEFAULT_BUSINESS_UNDERSTANDING.toString()))
            .andExpect(jsonPath("$.responsibility").value(DEFAULT_RESPONSIBILITY.toString()))
            .andExpect(jsonPath("$.riskAwareness").value(DEFAULT_RISK_AWARENESS.toString()))
            .andExpect(jsonPath("$.judgement").value(DEFAULT_JUDGEMENT.toString()))
            .andExpect(jsonPath("$.evidence").value(DEFAULT_EVIDENCE));
    }

    @Test
    @Transactional
    void getCandidateProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        Long id = candidateProfile.getId();

        defaultCandidateProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCandidateProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCandidateProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate equals to
        defaultCandidateProfileFiltering(
            "candidateDate.equals=" + DEFAULT_CANDIDATE_DATE,
            "candidateDate.equals=" + UPDATED_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate in
        defaultCandidateProfileFiltering(
            "candidateDate.in=" + DEFAULT_CANDIDATE_DATE + "," + UPDATED_CANDIDATE_DATE,
            "candidateDate.in=" + UPDATED_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate is not null
        defaultCandidateProfileFiltering("candidateDate.specified=true", "candidateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate is greater than or equal to
        defaultCandidateProfileFiltering(
            "candidateDate.greaterThanOrEqual=" + DEFAULT_CANDIDATE_DATE,
            "candidateDate.greaterThanOrEqual=" + UPDATED_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate is less than or equal to
        defaultCandidateProfileFiltering(
            "candidateDate.lessThanOrEqual=" + DEFAULT_CANDIDATE_DATE,
            "candidateDate.lessThanOrEqual=" + SMALLER_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate is less than
        defaultCandidateProfileFiltering(
            "candidateDate.lessThan=" + UPDATED_CANDIDATE_DATE,
            "candidateDate.lessThan=" + DEFAULT_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCandidateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where candidateDate is greater than
        defaultCandidateProfileFiltering(
            "candidateDate.greaterThan=" + SMALLER_CANDIDATE_DATE,
            "candidateDate.greaterThan=" + DEFAULT_CANDIDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCultivateDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where cultivateDirection equals to
        defaultCandidateProfileFiltering(
            "cultivateDirection.equals=" + DEFAULT_CULTIVATE_DIRECTION,
            "cultivateDirection.equals=" + UPDATED_CULTIVATE_DIRECTION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCultivateDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where cultivateDirection in
        defaultCandidateProfileFiltering(
            "cultivateDirection.in=" + DEFAULT_CULTIVATE_DIRECTION + "," + UPDATED_CULTIVATE_DIRECTION,
            "cultivateDirection.in=" + UPDATED_CULTIVATE_DIRECTION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCultivateDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where cultivateDirection is not null
        defaultCandidateProfileFiltering("cultivateDirection.specified=true", "cultivateDirection.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCultivateDirectionContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where cultivateDirection contains
        defaultCandidateProfileFiltering(
            "cultivateDirection.contains=" + DEFAULT_CULTIVATE_DIRECTION,
            "cultivateDirection.contains=" + UPDATED_CULTIVATE_DIRECTION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCultivateDirectionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where cultivateDirection does not contain
        defaultCandidateProfileFiltering(
            "cultivateDirection.doesNotContain=" + UPDATED_CULTIVATE_DIRECTION,
            "cultivateDirection.doesNotContain=" + DEFAULT_CULTIVATE_DIRECTION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByStabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where stability equals to
        defaultCandidateProfileFiltering("stability.equals=" + DEFAULT_STABILITY, "stability.equals=" + UPDATED_STABILITY);
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByStabilityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where stability in
        defaultCandidateProfileFiltering(
            "stability.in=" + DEFAULT_STABILITY + "," + UPDATED_STABILITY,
            "stability.in=" + UPDATED_STABILITY
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByStabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where stability is not null
        defaultCandidateProfileFiltering("stability.specified=true", "stability.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByLearningAbilityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where learningAbility equals to
        defaultCandidateProfileFiltering(
            "learningAbility.equals=" + DEFAULT_LEARNING_ABILITY,
            "learningAbility.equals=" + UPDATED_LEARNING_ABILITY
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByLearningAbilityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where learningAbility in
        defaultCandidateProfileFiltering(
            "learningAbility.in=" + DEFAULT_LEARNING_ABILITY + "," + UPDATED_LEARNING_ABILITY,
            "learningAbility.in=" + UPDATED_LEARNING_ABILITY
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByLearningAbilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where learningAbility is not null
        defaultCandidateProfileFiltering("learningAbility.specified=true", "learningAbility.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCommunicationCoordinationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where communicationCoordination equals to
        defaultCandidateProfileFiltering(
            "communicationCoordination.equals=" + DEFAULT_COMMUNICATION_COORDINATION,
            "communicationCoordination.equals=" + UPDATED_COMMUNICATION_COORDINATION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCommunicationCoordinationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where communicationCoordination in
        defaultCandidateProfileFiltering(
            "communicationCoordination.in=" + DEFAULT_COMMUNICATION_COORDINATION + "," + UPDATED_COMMUNICATION_COORDINATION,
            "communicationCoordination.in=" + UPDATED_COMMUNICATION_COORDINATION
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByCommunicationCoordinationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where communicationCoordination is not null
        defaultCandidateProfileFiltering("communicationCoordination.specified=true", "communicationCoordination.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByBusinessUnderstandingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where businessUnderstanding equals to
        defaultCandidateProfileFiltering(
            "businessUnderstanding.equals=" + DEFAULT_BUSINESS_UNDERSTANDING,
            "businessUnderstanding.equals=" + UPDATED_BUSINESS_UNDERSTANDING
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByBusinessUnderstandingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where businessUnderstanding in
        defaultCandidateProfileFiltering(
            "businessUnderstanding.in=" + DEFAULT_BUSINESS_UNDERSTANDING + "," + UPDATED_BUSINESS_UNDERSTANDING,
            "businessUnderstanding.in=" + UPDATED_BUSINESS_UNDERSTANDING
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByBusinessUnderstandingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where businessUnderstanding is not null
        defaultCandidateProfileFiltering("businessUnderstanding.specified=true", "businessUnderstanding.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByResponsibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where responsibility equals to
        defaultCandidateProfileFiltering(
            "responsibility.equals=" + DEFAULT_RESPONSIBILITY,
            "responsibility.equals=" + UPDATED_RESPONSIBILITY
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByResponsibilityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where responsibility in
        defaultCandidateProfileFiltering(
            "responsibility.in=" + DEFAULT_RESPONSIBILITY + "," + UPDATED_RESPONSIBILITY,
            "responsibility.in=" + UPDATED_RESPONSIBILITY
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByResponsibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where responsibility is not null
        defaultCandidateProfileFiltering("responsibility.specified=true", "responsibility.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByRiskAwarenessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where riskAwareness equals to
        defaultCandidateProfileFiltering(
            "riskAwareness.equals=" + DEFAULT_RISK_AWARENESS,
            "riskAwareness.equals=" + UPDATED_RISK_AWARENESS
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByRiskAwarenessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where riskAwareness in
        defaultCandidateProfileFiltering(
            "riskAwareness.in=" + DEFAULT_RISK_AWARENESS + "," + UPDATED_RISK_AWARENESS,
            "riskAwareness.in=" + UPDATED_RISK_AWARENESS
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByRiskAwarenessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where riskAwareness is not null
        defaultCandidateProfileFiltering("riskAwareness.specified=true", "riskAwareness.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByJudgementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where judgement equals to
        defaultCandidateProfileFiltering("judgement.equals=" + DEFAULT_JUDGEMENT, "judgement.equals=" + UPDATED_JUDGEMENT);
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByJudgementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where judgement in
        defaultCandidateProfileFiltering(
            "judgement.in=" + DEFAULT_JUDGEMENT + "," + UPDATED_JUDGEMENT,
            "judgement.in=" + UPDATED_JUDGEMENT
        );
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByJudgementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        // Get all the candidateProfileList where judgement is not null
        defaultCandidateProfileFiltering("judgement.specified=true", "judgement.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            candidateProfileRepository.saveAndFlush(candidateProfile);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        candidateProfile.setPerson(person);
        candidateProfileRepository.saveAndFlush(candidateProfile);
        Long personId = person.getId();
        // Get all the candidateProfileList where person equals to personId
        defaultCandidateProfileShouldBeFound("personId.equals=" + personId);

        // Get all the candidateProfileList where person equals to (personId + 1)
        defaultCandidateProfileShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            candidateProfileRepository.saveAndFlush(candidateProfile);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        candidateProfile.setPosition(position);
        candidateProfileRepository.saveAndFlush(candidateProfile);
        Long positionId = position.getId();
        // Get all the candidateProfileList where position equals to positionId
        defaultCandidateProfileShouldBeFound("positionId.equals=" + positionId);

        // Get all the candidateProfileList where position equals to (positionId + 1)
        defaultCandidateProfileShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllCandidateProfilesByObserverIsEqualToSomething() throws Exception {
        Person observer;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            candidateProfileRepository.saveAndFlush(candidateProfile);
            observer = PersonResourceIT.createEntity();
        } else {
            observer = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(observer);
        em.flush();
        candidateProfile.setObserver(observer);
        candidateProfileRepository.saveAndFlush(candidateProfile);
        Long observerId = observer.getId();
        // Get all the candidateProfileList where observer equals to observerId
        defaultCandidateProfileShouldBeFound("observerId.equals=" + observerId);

        // Get all the candidateProfileList where observer equals to (observerId + 1)
        defaultCandidateProfileShouldNotBeFound("observerId.equals=" + (observerId + 1));
    }

    private void defaultCandidateProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCandidateProfileShouldBeFound(shouldBeFound);
        defaultCandidateProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCandidateProfileShouldBeFound(String filter) throws Exception {
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].candidateDate").value(hasItem(DEFAULT_CANDIDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].cultivateDirection").value(hasItem(DEFAULT_CULTIVATE_DIRECTION)))
            .andExpect(jsonPath("$.[*].stability").value(hasItem(DEFAULT_STABILITY.toString())))
            .andExpect(jsonPath("$.[*].learningAbility").value(hasItem(DEFAULT_LEARNING_ABILITY.toString())))
            .andExpect(jsonPath("$.[*].communicationCoordination").value(hasItem(DEFAULT_COMMUNICATION_COORDINATION.toString())))
            .andExpect(jsonPath("$.[*].businessUnderstanding").value(hasItem(DEFAULT_BUSINESS_UNDERSTANDING.toString())))
            .andExpect(jsonPath("$.[*].responsibility").value(hasItem(DEFAULT_RESPONSIBILITY.toString())))
            .andExpect(jsonPath("$.[*].riskAwareness").value(hasItem(DEFAULT_RISK_AWARENESS.toString())))
            .andExpect(jsonPath("$.[*].judgement").value(hasItem(DEFAULT_JUDGEMENT.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)));

        // Check, that the count call also returns 1
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCandidateProfileShouldNotBeFound(String filter) throws Exception {
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCandidateProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCandidateProfile() throws Exception {
        // Get the candidateProfile
        restCandidateProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCandidateProfile() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateProfile
        CandidateProfile updatedCandidateProfile = candidateProfileRepository.findById(candidateProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCandidateProfile are not directly saved in db
        em.detach(updatedCandidateProfile);
        updatedCandidateProfile
            .candidateDate(UPDATED_CANDIDATE_DATE)
            .cultivateDirection(UPDATED_CULTIVATE_DIRECTION)
            .stability(UPDATED_STABILITY)
            .learningAbility(UPDATED_LEARNING_ABILITY)
            .communicationCoordination(UPDATED_COMMUNICATION_COORDINATION)
            .businessUnderstanding(UPDATED_BUSINESS_UNDERSTANDING)
            .responsibility(UPDATED_RESPONSIBILITY)
            .riskAwareness(UPDATED_RISK_AWARENESS)
            .judgement(UPDATED_JUDGEMENT)
            .evidence(UPDATED_EVIDENCE);
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(updatedCandidateProfile);

        restCandidateProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCandidateProfileToMatchAllProperties(updatedCandidateProfile);
    }

    @Test
    @Transactional
    void putNonExistingCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidateProfileWithPatch() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateProfile using partial update
        CandidateProfile partialUpdatedCandidateProfile = new CandidateProfile();
        partialUpdatedCandidateProfile.setId(candidateProfile.getId());

        partialUpdatedCandidateProfile
            .learningAbility(UPDATED_LEARNING_ABILITY)
            .businessUnderstanding(UPDATED_BUSINESS_UNDERSTANDING)
            .riskAwareness(UPDATED_RISK_AWARENESS);

        restCandidateProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidateProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidateProfile))
            )
            .andExpect(status().isOk());

        // Validate the CandidateProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCandidateProfile, candidateProfile),
            getPersistedCandidateProfile(candidateProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateCandidateProfileWithPatch() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateProfile using partial update
        CandidateProfile partialUpdatedCandidateProfile = new CandidateProfile();
        partialUpdatedCandidateProfile.setId(candidateProfile.getId());

        partialUpdatedCandidateProfile
            .candidateDate(UPDATED_CANDIDATE_DATE)
            .cultivateDirection(UPDATED_CULTIVATE_DIRECTION)
            .stability(UPDATED_STABILITY)
            .learningAbility(UPDATED_LEARNING_ABILITY)
            .communicationCoordination(UPDATED_COMMUNICATION_COORDINATION)
            .businessUnderstanding(UPDATED_BUSINESS_UNDERSTANDING)
            .responsibility(UPDATED_RESPONSIBILITY)
            .riskAwareness(UPDATED_RISK_AWARENESS)
            .judgement(UPDATED_JUDGEMENT)
            .evidence(UPDATED_EVIDENCE);

        restCandidateProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidateProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidateProfile))
            )
            .andExpect(status().isOk());

        // Validate the CandidateProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateProfileUpdatableFieldsEquals(
            partialUpdatedCandidateProfile,
            getPersistedCandidateProfile(partialUpdatedCandidateProfile)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidateProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidateProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidateProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidateProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateProfile.setId(longCount.incrementAndGet());

        // Create the CandidateProfile
        CandidateProfileDTO candidateProfileDTO = candidateProfileMapper.toDto(candidateProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(candidateProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidateProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidateProfile() throws Exception {
        // Initialize the database
        insertedCandidateProfile = candidateProfileRepository.saveAndFlush(candidateProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the candidateProfile
        restCandidateProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidateProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return candidateProfileRepository.count();
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

    protected CandidateProfile getPersistedCandidateProfile(CandidateProfile candidateProfile) {
        return candidateProfileRepository.findById(candidateProfile.getId()).orElseThrow();
    }

    protected void assertPersistedCandidateProfileToMatchAllProperties(CandidateProfile expectedCandidateProfile) {
        assertCandidateProfileAllPropertiesEquals(expectedCandidateProfile, getPersistedCandidateProfile(expectedCandidateProfile));
    }

    protected void assertPersistedCandidateProfileToMatchUpdatableProperties(CandidateProfile expectedCandidateProfile) {
        assertCandidateProfileAllUpdatablePropertiesEquals(
            expectedCandidateProfile,
            getPersistedCandidateProfile(expectedCandidateProfile)
        );
    }
}
