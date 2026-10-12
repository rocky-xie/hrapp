package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.ApplicationEventAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.IntegrationTest;
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.service.dto.ApplicationEventDTO;
import top.btmdc.hr.service.mapper.ApplicationEventMapper;

/**
 * Integration tests for the {@link ApplicationEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class ApplicationEventResourceIT {

    private static final ApplicationStageType DEFAULT_STAGE_TYPE = ApplicationStageType.RESUME_SCREENING;
    private static final ApplicationStageType UPDATED_STAGE_TYPE = ApplicationStageType.WRITTEN_TEST;

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTOR = "AAAAAAAAAA";
    private static final String UPDATED_ACTOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_OCCURRED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OCCURRED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA_JSON = "AAAAAAAAAA";
    private static final String UPDATED_METADATA_JSON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationEventRepository applicationEventRepository;

    @Autowired
    private ApplicationEventMapper applicationEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationEventMockMvc;

    private ApplicationEvent applicationEvent;

    private ApplicationEvent insertedApplicationEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationEvent createEntity() {
        return new ApplicationEvent()
            .stageType(DEFAULT_STAGE_TYPE)
            .eventType(DEFAULT_EVENT_TYPE)
            .previousValue(DEFAULT_PREVIOUS_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .actor(DEFAULT_ACTOR)
            .occurredAt(DEFAULT_OCCURRED_AT)
            .reason(DEFAULT_REASON)
            .metadataJson(DEFAULT_METADATA_JSON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationEvent createUpdatedEntity() {
        return new ApplicationEvent()
            .stageType(UPDATED_STAGE_TYPE)
            .eventType(UPDATED_EVENT_TYPE)
            .previousValue(UPDATED_PREVIOUS_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .actor(UPDATED_ACTOR)
            .occurredAt(UPDATED_OCCURRED_AT)
            .reason(UPDATED_REASON)
            .metadataJson(UPDATED_METADATA_JSON);
    }

    @BeforeEach
    void initTest() {
        applicationEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedApplicationEvent != null) {
            applicationEventRepository.delete(insertedApplicationEvent);
            insertedApplicationEvent = null;
        }
    }

    @Test
    @Transactional
    void createApplicationEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);
        var returnedApplicationEventDTO = om.readValue(
            restApplicationEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationEventDTO.class
        );

        // Validate the ApplicationEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApplicationEvent = applicationEventMapper.toEntity(returnedApplicationEventDTO);
        assertApplicationEventUpdatableFieldsEquals(returnedApplicationEvent, getPersistedApplicationEvent(returnedApplicationEvent));

        insertedApplicationEvent = returnedApplicationEvent;
    }

    @Test
    @Transactional
    void createApplicationEventWithExistingId() throws Exception {
        // Create the ApplicationEvent with an existing ID
        applicationEvent.setId(1L);
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationEvent.setEventType(null);

        // Create the ApplicationEvent, which fails.
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        restApplicationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationEvent.setActor(null);

        // Create the ApplicationEvent, which fails.
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        restApplicationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOccurredAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationEvent.setOccurredAt(null);

        // Create the ApplicationEvent, which fails.
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        restApplicationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationEvents() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].previousValue").value(hasItem(DEFAULT_PREVIOUS_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].actor").value(hasItem(DEFAULT_ACTOR)))
            .andExpect(jsonPath("$.[*].occurredAt").value(hasItem(DEFAULT_OCCURRED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].metadataJson").value(hasItem(DEFAULT_METADATA_JSON)));
    }

    @Test
    @Transactional
    void getApplicationEvent() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get the applicationEvent
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationEvent.getId().intValue()))
            .andExpect(jsonPath("$.stageType").value(DEFAULT_STAGE_TYPE.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.previousValue").value(DEFAULT_PREVIOUS_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.actor").value(DEFAULT_ACTOR))
            .andExpect(jsonPath("$.occurredAt").value(DEFAULT_OCCURRED_AT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.metadataJson").value(DEFAULT_METADATA_JSON));
    }

    @Test
    @Transactional
    void getApplicationEventsByIdFiltering() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        Long id = applicationEvent.getId();

        defaultApplicationEventFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultApplicationEventFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultApplicationEventFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByStageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where stageType equals to
        defaultApplicationEventFiltering("stageType.equals=" + DEFAULT_STAGE_TYPE, "stageType.equals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByStageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where stageType in
        defaultApplicationEventFiltering(
            "stageType.in=" + DEFAULT_STAGE_TYPE + "," + UPDATED_STAGE_TYPE,
            "stageType.in=" + UPDATED_STAGE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByStageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where stageType is not null
        defaultApplicationEventFiltering("stageType.specified=true", "stageType.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where eventType equals to
        defaultApplicationEventFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where eventType in
        defaultApplicationEventFiltering(
            "eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE,
            "eventType.in=" + UPDATED_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where eventType is not null
        defaultApplicationEventFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where eventType contains
        defaultApplicationEventFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where eventType does not contain
        defaultApplicationEventFiltering(
            "eventType.doesNotContain=" + UPDATED_EVENT_TYPE,
            "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByPreviousValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where previousValue equals to
        defaultApplicationEventFiltering(
            "previousValue.equals=" + DEFAULT_PREVIOUS_VALUE,
            "previousValue.equals=" + UPDATED_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByPreviousValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where previousValue in
        defaultApplicationEventFiltering(
            "previousValue.in=" + DEFAULT_PREVIOUS_VALUE + "," + UPDATED_PREVIOUS_VALUE,
            "previousValue.in=" + UPDATED_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByPreviousValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where previousValue is not null
        defaultApplicationEventFiltering("previousValue.specified=true", "previousValue.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByPreviousValueContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where previousValue contains
        defaultApplicationEventFiltering(
            "previousValue.contains=" + DEFAULT_PREVIOUS_VALUE,
            "previousValue.contains=" + UPDATED_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByPreviousValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where previousValue does not contain
        defaultApplicationEventFiltering(
            "previousValue.doesNotContain=" + UPDATED_PREVIOUS_VALUE,
            "previousValue.doesNotContain=" + DEFAULT_PREVIOUS_VALUE
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByNewValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where newValue equals to
        defaultApplicationEventFiltering("newValue.equals=" + DEFAULT_NEW_VALUE, "newValue.equals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByNewValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where newValue in
        defaultApplicationEventFiltering("newValue.in=" + DEFAULT_NEW_VALUE + "," + UPDATED_NEW_VALUE, "newValue.in=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByNewValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where newValue is not null
        defaultApplicationEventFiltering("newValue.specified=true", "newValue.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByNewValueContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where newValue contains
        defaultApplicationEventFiltering("newValue.contains=" + DEFAULT_NEW_VALUE, "newValue.contains=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByNewValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where newValue does not contain
        defaultApplicationEventFiltering("newValue.doesNotContain=" + UPDATED_NEW_VALUE, "newValue.doesNotContain=" + DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByActorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where actor equals to
        defaultApplicationEventFiltering("actor.equals=" + DEFAULT_ACTOR, "actor.equals=" + UPDATED_ACTOR);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByActorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where actor in
        defaultApplicationEventFiltering("actor.in=" + DEFAULT_ACTOR + "," + UPDATED_ACTOR, "actor.in=" + UPDATED_ACTOR);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByActorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where actor is not null
        defaultApplicationEventFiltering("actor.specified=true", "actor.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByActorContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where actor contains
        defaultApplicationEventFiltering("actor.contains=" + DEFAULT_ACTOR, "actor.contains=" + UPDATED_ACTOR);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByActorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where actor does not contain
        defaultApplicationEventFiltering("actor.doesNotContain=" + UPDATED_ACTOR, "actor.doesNotContain=" + DEFAULT_ACTOR);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByOccurredAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where occurredAt equals to
        defaultApplicationEventFiltering("occurredAt.equals=" + DEFAULT_OCCURRED_AT, "occurredAt.equals=" + UPDATED_OCCURRED_AT);
    }

    @Test
    @Transactional
    void getAllApplicationEventsByOccurredAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where occurredAt in
        defaultApplicationEventFiltering(
            "occurredAt.in=" + DEFAULT_OCCURRED_AT + "," + UPDATED_OCCURRED_AT,
            "occurredAt.in=" + UPDATED_OCCURRED_AT
        );
    }

    @Test
    @Transactional
    void getAllApplicationEventsByOccurredAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList where occurredAt is not null
        defaultApplicationEventFiltering("occurredAt.specified=true", "occurredAt.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationEventsByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            applicationEventRepository.saveAndFlush(applicationEvent);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        applicationEvent.setApplication(application);
        applicationEventRepository.saveAndFlush(applicationEvent);
        Long applicationId = application.getId();
        // Get all the applicationEventList where application equals to applicationId
        defaultApplicationEventShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the applicationEventList where application equals to (applicationId + 1)
        defaultApplicationEventShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultApplicationEventFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultApplicationEventShouldBeFound(shouldBeFound);
        defaultApplicationEventShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationEventShouldBeFound(String filter) throws Exception {
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].previousValue").value(hasItem(DEFAULT_PREVIOUS_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].actor").value(hasItem(DEFAULT_ACTOR)))
            .andExpect(jsonPath("$.[*].occurredAt").value(hasItem(DEFAULT_OCCURRED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].metadataJson").value(hasItem(DEFAULT_METADATA_JSON)));

        // Check, that the count call also returns 1
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationEventShouldNotBeFound(String filter) throws Exception {
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationEvent() throws Exception {
        // Get the applicationEvent
        restApplicationEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationEvent() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationEvent
        ApplicationEvent updatedApplicationEvent = applicationEventRepository.findById(applicationEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationEvent are not directly saved in db
        em.detach(updatedApplicationEvent);
        updatedApplicationEvent
            .stageType(UPDATED_STAGE_TYPE)
            .eventType(UPDATED_EVENT_TYPE)
            .previousValue(UPDATED_PREVIOUS_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .actor(UPDATED_ACTOR)
            .occurredAt(UPDATED_OCCURRED_AT)
            .reason(UPDATED_REASON)
            .metadataJson(UPDATED_METADATA_JSON);
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(updatedApplicationEvent);

        restApplicationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationEventToMatchAllProperties(updatedApplicationEvent);
    }

    @Test
    @Transactional
    void putNonExistingApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationEventWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationEvent using partial update
        ApplicationEvent partialUpdatedApplicationEvent = new ApplicationEvent();
        partialUpdatedApplicationEvent.setId(applicationEvent.getId());

        partialUpdatedApplicationEvent
            .stageType(UPDATED_STAGE_TYPE)
            .eventType(UPDATED_EVENT_TYPE)
            .newValue(UPDATED_NEW_VALUE)
            .actor(UPDATED_ACTOR)
            .reason(UPDATED_REASON);

        restApplicationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationEvent))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationEvent, applicationEvent),
            getPersistedApplicationEvent(applicationEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationEventWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationEvent using partial update
        ApplicationEvent partialUpdatedApplicationEvent = new ApplicationEvent();
        partialUpdatedApplicationEvent.setId(applicationEvent.getId());

        partialUpdatedApplicationEvent
            .stageType(UPDATED_STAGE_TYPE)
            .eventType(UPDATED_EVENT_TYPE)
            .previousValue(UPDATED_PREVIOUS_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .actor(UPDATED_ACTOR)
            .occurredAt(UPDATED_OCCURRED_AT)
            .reason(UPDATED_REASON)
            .metadataJson(UPDATED_METADATA_JSON);

        restApplicationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationEvent))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationEventUpdatableFieldsEquals(
            partialUpdatedApplicationEvent,
            getPersistedApplicationEvent(partialUpdatedApplicationEvent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationEvent.setId(longCount.incrementAndGet());

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(applicationEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationEvent() throws Exception {
        // Initialize the database
        insertedApplicationEvent = applicationEventRepository.saveAndFlush(applicationEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the applicationEvent
        restApplicationEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationEventRepository.count();
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

    protected ApplicationEvent getPersistedApplicationEvent(ApplicationEvent applicationEvent) {
        return applicationEventRepository.findById(applicationEvent.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationEventToMatchAllProperties(ApplicationEvent expectedApplicationEvent) {
        assertApplicationEventAllPropertiesEquals(expectedApplicationEvent, getPersistedApplicationEvent(expectedApplicationEvent));
    }

    protected void assertPersistedApplicationEventToMatchUpdatableProperties(ApplicationEvent expectedApplicationEvent) {
        assertApplicationEventAllUpdatablePropertiesEquals(
            expectedApplicationEvent,
            getPersistedApplicationEvent(expectedApplicationEvent)
        );
    }
}
