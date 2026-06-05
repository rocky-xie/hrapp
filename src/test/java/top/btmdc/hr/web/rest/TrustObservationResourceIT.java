package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.TrustObservationAsserts.*;
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
import top.btmdc.hr.domain.TrustObservation;
import top.btmdc.hr.domain.enumeration.TrustStage;
import top.btmdc.hr.repository.TrustObservationRepository;
import top.btmdc.hr.service.TrustObservationService;
import top.btmdc.hr.service.dto.TrustObservationDTO;
import top.btmdc.hr.service.mapper.TrustObservationMapper;

/**
 * Integration tests for the {@link TrustObservationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrustObservationResourceIT {

    private static final LocalDate DEFAULT_OBSERVATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OBSERVATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OBSERVATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final TrustStage DEFAULT_TRUST_STAGE = TrustStage.S0_UNOBSERVED;
    private static final TrustStage UPDATED_TRUST_STAGE = TrustStage.S1_BASIC_TRUST;

    private static final String DEFAULT_OBSERVED_BEHAVIOR = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVED_BEHAVIOR = "BBBBBBBBBB";

    private static final String DEFAULT_POSITIVE_SIGNAL = "AAAAAAAAAA";
    private static final String UPDATED_POSITIVE_SIGNAL = "BBBBBBBBBB";

    private static final String DEFAULT_RISK_SIGNAL = "AAAAAAAAAA";
    private static final String UPDATED_RISK_SIGNAL = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_OBSERVATION_POINT = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_OBSERVATION_POINT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trust-observations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrustObservationRepository trustObservationRepository;

    @Mock
    private TrustObservationRepository trustObservationRepositoryMock;

    @Autowired
    private TrustObservationMapper trustObservationMapper;

    @Mock
    private TrustObservationService trustObservationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrustObservationMockMvc;

    private TrustObservation trustObservation;

    private TrustObservation insertedTrustObservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrustObservation createEntity(EntityManager em) {
        TrustObservation trustObservation = new TrustObservation()
            .observationDate(DEFAULT_OBSERVATION_DATE)
            .trustStage(DEFAULT_TRUST_STAGE)
            .observedBehavior(DEFAULT_OBSERVED_BEHAVIOR)
            .positiveSignal(DEFAULT_POSITIVE_SIGNAL)
            .riskSignal(DEFAULT_RISK_SIGNAL)
            .nextObservationPoint(DEFAULT_NEXT_OBSERVATION_POINT);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        trustObservation.setPerson(person);
        return trustObservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrustObservation createUpdatedEntity(EntityManager em) {
        TrustObservation updatedTrustObservation = new TrustObservation()
            .observationDate(UPDATED_OBSERVATION_DATE)
            .trustStage(UPDATED_TRUST_STAGE)
            .observedBehavior(UPDATED_OBSERVED_BEHAVIOR)
            .positiveSignal(UPDATED_POSITIVE_SIGNAL)
            .riskSignal(UPDATED_RISK_SIGNAL)
            .nextObservationPoint(UPDATED_NEXT_OBSERVATION_POINT);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedTrustObservation.setPerson(person);
        return updatedTrustObservation;
    }

    @BeforeEach
    void initTest() {
        trustObservation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTrustObservation != null) {
            trustObservationRepository.delete(insertedTrustObservation);
            insertedTrustObservation = null;
        }
    }

    @Test
    @Transactional
    void createTrustObservation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);
        var returnedTrustObservationDTO = om.readValue(
            restTrustObservationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trustObservationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrustObservationDTO.class
        );

        // Validate the TrustObservation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrustObservation = trustObservationMapper.toEntity(returnedTrustObservationDTO);
        assertTrustObservationUpdatableFieldsEquals(returnedTrustObservation, getPersistedTrustObservation(returnedTrustObservation));

        insertedTrustObservation = returnedTrustObservation;
    }

    @Test
    @Transactional
    void createTrustObservationWithExistingId() throws Exception {
        // Create the TrustObservation with an existing ID
        trustObservation.setId(1L);
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrustObservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trustObservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObservationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trustObservation.setObservationDate(null);

        // Create the TrustObservation, which fails.
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        restTrustObservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trustObservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrustStageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trustObservation.setTrustStage(null);

        // Create the TrustObservation, which fails.
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        restTrustObservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trustObservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrustObservations() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trustObservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].observationDate").value(hasItem(DEFAULT_OBSERVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].trustStage").value(hasItem(DEFAULT_TRUST_STAGE.toString())))
            .andExpect(jsonPath("$.[*].observedBehavior").value(hasItem(DEFAULT_OBSERVED_BEHAVIOR)))
            .andExpect(jsonPath("$.[*].positiveSignal").value(hasItem(DEFAULT_POSITIVE_SIGNAL)))
            .andExpect(jsonPath("$.[*].riskSignal").value(hasItem(DEFAULT_RISK_SIGNAL)))
            .andExpect(jsonPath("$.[*].nextObservationPoint").value(hasItem(DEFAULT_NEXT_OBSERVATION_POINT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrustObservationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(trustObservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrustObservationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trustObservationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrustObservationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trustObservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrustObservationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trustObservationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrustObservation() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get the trustObservation
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL_ID, trustObservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trustObservation.getId().intValue()))
            .andExpect(jsonPath("$.observationDate").value(DEFAULT_OBSERVATION_DATE.toString()))
            .andExpect(jsonPath("$.trustStage").value(DEFAULT_TRUST_STAGE.toString()))
            .andExpect(jsonPath("$.observedBehavior").value(DEFAULT_OBSERVED_BEHAVIOR))
            .andExpect(jsonPath("$.positiveSignal").value(DEFAULT_POSITIVE_SIGNAL))
            .andExpect(jsonPath("$.riskSignal").value(DEFAULT_RISK_SIGNAL))
            .andExpect(jsonPath("$.nextObservationPoint").value(DEFAULT_NEXT_OBSERVATION_POINT));
    }

    @Test
    @Transactional
    void getTrustObservationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        Long id = trustObservation.getId();

        defaultTrustObservationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrustObservationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrustObservationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate equals to
        defaultTrustObservationFiltering(
            "observationDate.equals=" + DEFAULT_OBSERVATION_DATE,
            "observationDate.equals=" + UPDATED_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate in
        defaultTrustObservationFiltering(
            "observationDate.in=" + DEFAULT_OBSERVATION_DATE + "," + UPDATED_OBSERVATION_DATE,
            "observationDate.in=" + UPDATED_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate is not null
        defaultTrustObservationFiltering("observationDate.specified=true", "observationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate is greater than or equal to
        defaultTrustObservationFiltering(
            "observationDate.greaterThanOrEqual=" + DEFAULT_OBSERVATION_DATE,
            "observationDate.greaterThanOrEqual=" + UPDATED_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate is less than or equal to
        defaultTrustObservationFiltering(
            "observationDate.lessThanOrEqual=" + DEFAULT_OBSERVATION_DATE,
            "observationDate.lessThanOrEqual=" + SMALLER_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate is less than
        defaultTrustObservationFiltering(
            "observationDate.lessThan=" + UPDATED_OBSERVATION_DATE,
            "observationDate.lessThan=" + DEFAULT_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObservationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where observationDate is greater than
        defaultTrustObservationFiltering(
            "observationDate.greaterThan=" + SMALLER_OBSERVATION_DATE,
            "observationDate.greaterThan=" + DEFAULT_OBSERVATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByTrustStageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where trustStage equals to
        defaultTrustObservationFiltering("trustStage.equals=" + DEFAULT_TRUST_STAGE, "trustStage.equals=" + UPDATED_TRUST_STAGE);
    }

    @Test
    @Transactional
    void getAllTrustObservationsByTrustStageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where trustStage in
        defaultTrustObservationFiltering(
            "trustStage.in=" + DEFAULT_TRUST_STAGE + "," + UPDATED_TRUST_STAGE,
            "trustStage.in=" + UPDATED_TRUST_STAGE
        );
    }

    @Test
    @Transactional
    void getAllTrustObservationsByTrustStageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        // Get all the trustObservationList where trustStage is not null
        defaultTrustObservationFiltering("trustStage.specified=true", "trustStage.specified=false");
    }

    @Test
    @Transactional
    void getAllTrustObservationsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            trustObservationRepository.saveAndFlush(trustObservation);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        trustObservation.setPerson(person);
        trustObservationRepository.saveAndFlush(trustObservation);
        Long personId = person.getId();
        // Get all the trustObservationList where person equals to personId
        defaultTrustObservationShouldBeFound("personId.equals=" + personId);

        // Get all the trustObservationList where person equals to (personId + 1)
        defaultTrustObservationShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllTrustObservationsByObserverIsEqualToSomething() throws Exception {
        Person observer;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            trustObservationRepository.saveAndFlush(trustObservation);
            observer = PersonResourceIT.createEntity();
        } else {
            observer = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(observer);
        em.flush();
        trustObservation.setObserver(observer);
        trustObservationRepository.saveAndFlush(trustObservation);
        Long observerId = observer.getId();
        // Get all the trustObservationList where observer equals to observerId
        defaultTrustObservationShouldBeFound("observerId.equals=" + observerId);

        // Get all the trustObservationList where observer equals to (observerId + 1)
        defaultTrustObservationShouldNotBeFound("observerId.equals=" + (observerId + 1));
    }

    private void defaultTrustObservationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrustObservationShouldBeFound(shouldBeFound);
        defaultTrustObservationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrustObservationShouldBeFound(String filter) throws Exception {
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trustObservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].observationDate").value(hasItem(DEFAULT_OBSERVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].trustStage").value(hasItem(DEFAULT_TRUST_STAGE.toString())))
            .andExpect(jsonPath("$.[*].observedBehavior").value(hasItem(DEFAULT_OBSERVED_BEHAVIOR)))
            .andExpect(jsonPath("$.[*].positiveSignal").value(hasItem(DEFAULT_POSITIVE_SIGNAL)))
            .andExpect(jsonPath("$.[*].riskSignal").value(hasItem(DEFAULT_RISK_SIGNAL)))
            .andExpect(jsonPath("$.[*].nextObservationPoint").value(hasItem(DEFAULT_NEXT_OBSERVATION_POINT)));

        // Check, that the count call also returns 1
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrustObservationShouldNotBeFound(String filter) throws Exception {
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrustObservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrustObservation() throws Exception {
        // Get the trustObservation
        restTrustObservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrustObservation() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trustObservation
        TrustObservation updatedTrustObservation = trustObservationRepository.findById(trustObservation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrustObservation are not directly saved in db
        em.detach(updatedTrustObservation);
        updatedTrustObservation
            .observationDate(UPDATED_OBSERVATION_DATE)
            .trustStage(UPDATED_TRUST_STAGE)
            .observedBehavior(UPDATED_OBSERVED_BEHAVIOR)
            .positiveSignal(UPDATED_POSITIVE_SIGNAL)
            .riskSignal(UPDATED_RISK_SIGNAL)
            .nextObservationPoint(UPDATED_NEXT_OBSERVATION_POINT);
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(updatedTrustObservation);

        restTrustObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trustObservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trustObservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrustObservationToMatchAllProperties(updatedTrustObservation);
    }

    @Test
    @Transactional
    void putNonExistingTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trustObservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trustObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trustObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trustObservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrustObservationWithPatch() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trustObservation using partial update
        TrustObservation partialUpdatedTrustObservation = new TrustObservation();
        partialUpdatedTrustObservation.setId(trustObservation.getId());

        partialUpdatedTrustObservation
            .observedBehavior(UPDATED_OBSERVED_BEHAVIOR)
            .riskSignal(UPDATED_RISK_SIGNAL)
            .nextObservationPoint(UPDATED_NEXT_OBSERVATION_POINT);

        restTrustObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrustObservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrustObservation))
            )
            .andExpect(status().isOk());

        // Validate the TrustObservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrustObservationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrustObservation, trustObservation),
            getPersistedTrustObservation(trustObservation)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrustObservationWithPatch() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trustObservation using partial update
        TrustObservation partialUpdatedTrustObservation = new TrustObservation();
        partialUpdatedTrustObservation.setId(trustObservation.getId());

        partialUpdatedTrustObservation
            .observationDate(UPDATED_OBSERVATION_DATE)
            .trustStage(UPDATED_TRUST_STAGE)
            .observedBehavior(UPDATED_OBSERVED_BEHAVIOR)
            .positiveSignal(UPDATED_POSITIVE_SIGNAL)
            .riskSignal(UPDATED_RISK_SIGNAL)
            .nextObservationPoint(UPDATED_NEXT_OBSERVATION_POINT);

        restTrustObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrustObservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrustObservation))
            )
            .andExpect(status().isOk());

        // Validate the TrustObservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrustObservationUpdatableFieldsEquals(
            partialUpdatedTrustObservation,
            getPersistedTrustObservation(partialUpdatedTrustObservation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trustObservationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trustObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trustObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrustObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trustObservation.setId(longCount.incrementAndGet());

        // Create the TrustObservation
        TrustObservationDTO trustObservationDTO = trustObservationMapper.toDto(trustObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrustObservationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trustObservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrustObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrustObservation() throws Exception {
        // Initialize the database
        insertedTrustObservation = trustObservationRepository.saveAndFlush(trustObservation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trustObservation
        restTrustObservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, trustObservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trustObservationRepository.count();
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

    protected TrustObservation getPersistedTrustObservation(TrustObservation trustObservation) {
        return trustObservationRepository.findById(trustObservation.getId()).orElseThrow();
    }

    protected void assertPersistedTrustObservationToMatchAllProperties(TrustObservation expectedTrustObservation) {
        assertTrustObservationAllPropertiesEquals(expectedTrustObservation, getPersistedTrustObservation(expectedTrustObservation));
    }

    protected void assertPersistedTrustObservationToMatchUpdatableProperties(TrustObservation expectedTrustObservation) {
        assertTrustObservationAllUpdatablePropertiesEquals(
            expectedTrustObservation,
            getPersistedTrustObservation(expectedTrustObservation)
        );
    }
}
