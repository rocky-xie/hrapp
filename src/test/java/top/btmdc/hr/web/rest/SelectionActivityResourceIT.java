package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SelectionActivityAsserts.*;
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
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.SelectionActivity;
import top.btmdc.hr.domain.enumeration.ApplicationStageType;
import top.btmdc.hr.domain.enumeration.SelectionActivityType;
import top.btmdc.hr.repository.SelectionActivityRepository;
import top.btmdc.hr.service.dto.SelectionActivityDTO;
import top.btmdc.hr.service.mapper.SelectionActivityMapper;

/**
 * Integration tests for the {@link SelectionActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class SelectionActivityResourceIT {

    private static final ApplicationStageType DEFAULT_STAGE_TYPE = ApplicationStageType.RESUME_SCREENING;
    private static final ApplicationStageType UPDATED_STAGE_TYPE = ApplicationStageType.WRITTEN_TEST;

    private static final SelectionActivityType DEFAULT_ACTIVITY_TYPE = SelectionActivityType.WRITTEN_TEST;
    private static final SelectionActivityType UPDATED_ACTIVITY_TYPE = SelectionActivityType.FIRST_INTERVIEW;

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MEETING_ROOM_ID = 1L;
    private static final Long UPDATED_MEETING_ROOM_ID = 2L;
    private static final Long SMALLER_MEETING_ROOM_ID = 1L - 1L;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCHEDULE_VERSION = 1;
    private static final Integer UPDATED_SCHEDULE_VERSION = 2;
    private static final Integer SMALLER_SCHEDULE_VERSION = 1 - 1;

    private static final String DEFAULT_CHANGE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_CALENDAR_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_CALENDAR_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_EVENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/selection-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SelectionActivityRepository selectionActivityRepository;

    @Autowired
    private SelectionActivityMapper selectionActivityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSelectionActivityMockMvc;

    private SelectionActivity selectionActivity;

    private SelectionActivity insertedSelectionActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectionActivity createEntity() {
        return new SelectionActivity()
            .stageType(DEFAULT_STAGE_TYPE)
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .meetingRoomId(DEFAULT_MEETING_ROOM_ID)
            .location(DEFAULT_LOCATION)
            .status(DEFAULT_STATUS)
            .scheduleVersion(DEFAULT_SCHEDULE_VERSION)
            .changeReason(DEFAULT_CHANGE_REASON)
            .externalCalendarProvider(DEFAULT_EXTERNAL_CALENDAR_PROVIDER)
            .externalEventId(DEFAULT_EXTERNAL_EVENT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectionActivity createUpdatedEntity() {
        return new SelectionActivity()
            .stageType(UPDATED_STAGE_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .meetingRoomId(UPDATED_MEETING_ROOM_ID)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS)
            .scheduleVersion(UPDATED_SCHEDULE_VERSION)
            .changeReason(UPDATED_CHANGE_REASON)
            .externalCalendarProvider(UPDATED_EXTERNAL_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID);
    }

    @BeforeEach
    void initTest() {
        selectionActivity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSelectionActivity != null) {
            selectionActivityRepository.delete(insertedSelectionActivity);
            insertedSelectionActivity = null;
        }
    }

    @Test
    @Transactional
    void createSelectionActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);
        var returnedSelectionActivityDTO = om.readValue(
            restSelectionActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SelectionActivityDTO.class
        );

        // Validate the SelectionActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSelectionActivity = selectionActivityMapper.toEntity(returnedSelectionActivityDTO);
        assertSelectionActivityUpdatableFieldsEquals(returnedSelectionActivity, getPersistedSelectionActivity(returnedSelectionActivity));

        insertedSelectionActivity = returnedSelectionActivity;
    }

    @Test
    @Transactional
    void createSelectionActivityWithExistingId() throws Exception {
        // Create the SelectionActivity with an existing ID
        selectionActivity.setId(1L);
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStageTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setStageType(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setActivityType(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setStartAt(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setEndAt(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setStatus(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        selectionActivity.setScheduleVersion(null);

        // Create the SelectionActivity, which fails.
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        restSelectionActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSelectionActivities() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectionActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].meetingRoomId").value(hasItem(DEFAULT_MEETING_ROOM_ID.intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].scheduleVersion").value(hasItem(DEFAULT_SCHEDULE_VERSION)))
            .andExpect(jsonPath("$.[*].changeReason").value(hasItem(DEFAULT_CHANGE_REASON)))
            .andExpect(jsonPath("$.[*].externalCalendarProvider").value(hasItem(DEFAULT_EXTERNAL_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)));
    }

    @Test
    @Transactional
    void getSelectionActivity() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get the selectionActivity
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, selectionActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(selectionActivity.getId().intValue()))
            .andExpect(jsonPath("$.stageType").value(DEFAULT_STAGE_TYPE.toString()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.meetingRoomId").value(DEFAULT_MEETING_ROOM_ID.intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.scheduleVersion").value(DEFAULT_SCHEDULE_VERSION))
            .andExpect(jsonPath("$.changeReason").value(DEFAULT_CHANGE_REASON))
            .andExpect(jsonPath("$.externalCalendarProvider").value(DEFAULT_EXTERNAL_CALENDAR_PROVIDER))
            .andExpect(jsonPath("$.externalEventId").value(DEFAULT_EXTERNAL_EVENT_ID));
    }

    @Test
    @Transactional
    void getSelectionActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        Long id = selectionActivity.getId();

        defaultSelectionActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSelectionActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSelectionActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where stageType equals to
        defaultSelectionActivityFiltering("stageType.equals=" + DEFAULT_STAGE_TYPE, "stageType.equals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where stageType in
        defaultSelectionActivityFiltering(
            "stageType.in=" + DEFAULT_STAGE_TYPE + "," + UPDATED_STAGE_TYPE,
            "stageType.in=" + UPDATED_STAGE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where stageType is not null
        defaultSelectionActivityFiltering("stageType.specified=true", "stageType.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByActivityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where activityType equals to
        defaultSelectionActivityFiltering("activityType.equals=" + DEFAULT_ACTIVITY_TYPE, "activityType.equals=" + UPDATED_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByActivityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where activityType in
        defaultSelectionActivityFiltering(
            "activityType.in=" + DEFAULT_ACTIVITY_TYPE + "," + UPDATED_ACTIVITY_TYPE,
            "activityType.in=" + UPDATED_ACTIVITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByActivityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where activityType is not null
        defaultSelectionActivityFiltering("activityType.specified=true", "activityType.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where startAt equals to
        defaultSelectionActivityFiltering("startAt.equals=" + DEFAULT_START_AT, "startAt.equals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where startAt in
        defaultSelectionActivityFiltering("startAt.in=" + DEFAULT_START_AT + "," + UPDATED_START_AT, "startAt.in=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where startAt is not null
        defaultSelectionActivityFiltering("startAt.specified=true", "startAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByEndAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where endAt equals to
        defaultSelectionActivityFiltering("endAt.equals=" + DEFAULT_END_AT, "endAt.equals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByEndAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where endAt in
        defaultSelectionActivityFiltering("endAt.in=" + DEFAULT_END_AT + "," + UPDATED_END_AT, "endAt.in=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByEndAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where endAt is not null
        defaultSelectionActivityFiltering("endAt.specified=true", "endAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId equals to
        defaultSelectionActivityFiltering(
            "meetingRoomId.equals=" + DEFAULT_MEETING_ROOM_ID,
            "meetingRoomId.equals=" + UPDATED_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId in
        defaultSelectionActivityFiltering(
            "meetingRoomId.in=" + DEFAULT_MEETING_ROOM_ID + "," + UPDATED_MEETING_ROOM_ID,
            "meetingRoomId.in=" + UPDATED_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId is not null
        defaultSelectionActivityFiltering("meetingRoomId.specified=true", "meetingRoomId.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId is greater than or equal to
        defaultSelectionActivityFiltering(
            "meetingRoomId.greaterThanOrEqual=" + DEFAULT_MEETING_ROOM_ID,
            "meetingRoomId.greaterThanOrEqual=" + UPDATED_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId is less than or equal to
        defaultSelectionActivityFiltering(
            "meetingRoomId.lessThanOrEqual=" + DEFAULT_MEETING_ROOM_ID,
            "meetingRoomId.lessThanOrEqual=" + SMALLER_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId is less than
        defaultSelectionActivityFiltering(
            "meetingRoomId.lessThan=" + UPDATED_MEETING_ROOM_ID,
            "meetingRoomId.lessThan=" + DEFAULT_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByMeetingRoomIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where meetingRoomId is greater than
        defaultSelectionActivityFiltering(
            "meetingRoomId.greaterThan=" + SMALLER_MEETING_ROOM_ID,
            "meetingRoomId.greaterThan=" + DEFAULT_MEETING_ROOM_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where location equals to
        defaultSelectionActivityFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where location in
        defaultSelectionActivityFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where location is not null
        defaultSelectionActivityFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where location contains
        defaultSelectionActivityFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where location does not contain
        defaultSelectionActivityFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where status equals to
        defaultSelectionActivityFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where status in
        defaultSelectionActivityFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where status is not null
        defaultSelectionActivityFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where status contains
        defaultSelectionActivityFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where status does not contain
        defaultSelectionActivityFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion equals to
        defaultSelectionActivityFiltering(
            "scheduleVersion.equals=" + DEFAULT_SCHEDULE_VERSION,
            "scheduleVersion.equals=" + UPDATED_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion in
        defaultSelectionActivityFiltering(
            "scheduleVersion.in=" + DEFAULT_SCHEDULE_VERSION + "," + UPDATED_SCHEDULE_VERSION,
            "scheduleVersion.in=" + UPDATED_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion is not null
        defaultSelectionActivityFiltering("scheduleVersion.specified=true", "scheduleVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion is greater than or equal to
        defaultSelectionActivityFiltering(
            "scheduleVersion.greaterThanOrEqual=" + DEFAULT_SCHEDULE_VERSION,
            "scheduleVersion.greaterThanOrEqual=" + UPDATED_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion is less than or equal to
        defaultSelectionActivityFiltering(
            "scheduleVersion.lessThanOrEqual=" + DEFAULT_SCHEDULE_VERSION,
            "scheduleVersion.lessThanOrEqual=" + SMALLER_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion is less than
        defaultSelectionActivityFiltering(
            "scheduleVersion.lessThan=" + UPDATED_SCHEDULE_VERSION,
            "scheduleVersion.lessThan=" + DEFAULT_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByScheduleVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where scheduleVersion is greater than
        defaultSelectionActivityFiltering(
            "scheduleVersion.greaterThan=" + SMALLER_SCHEDULE_VERSION,
            "scheduleVersion.greaterThan=" + DEFAULT_SCHEDULE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalCalendarProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalCalendarProvider equals to
        defaultSelectionActivityFiltering(
            "externalCalendarProvider.equals=" + DEFAULT_EXTERNAL_CALENDAR_PROVIDER,
            "externalCalendarProvider.equals=" + UPDATED_EXTERNAL_CALENDAR_PROVIDER
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalCalendarProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalCalendarProvider in
        defaultSelectionActivityFiltering(
            "externalCalendarProvider.in=" + DEFAULT_EXTERNAL_CALENDAR_PROVIDER + "," + UPDATED_EXTERNAL_CALENDAR_PROVIDER,
            "externalCalendarProvider.in=" + UPDATED_EXTERNAL_CALENDAR_PROVIDER
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalCalendarProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalCalendarProvider is not null
        defaultSelectionActivityFiltering("externalCalendarProvider.specified=true", "externalCalendarProvider.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalCalendarProviderContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalCalendarProvider contains
        defaultSelectionActivityFiltering(
            "externalCalendarProvider.contains=" + DEFAULT_EXTERNAL_CALENDAR_PROVIDER,
            "externalCalendarProvider.contains=" + UPDATED_EXTERNAL_CALENDAR_PROVIDER
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalCalendarProviderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalCalendarProvider does not contain
        defaultSelectionActivityFiltering(
            "externalCalendarProvider.doesNotContain=" + UPDATED_EXTERNAL_CALENDAR_PROVIDER,
            "externalCalendarProvider.doesNotContain=" + DEFAULT_EXTERNAL_CALENDAR_PROVIDER
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalEventId equals to
        defaultSelectionActivityFiltering(
            "externalEventId.equals=" + DEFAULT_EXTERNAL_EVENT_ID,
            "externalEventId.equals=" + UPDATED_EXTERNAL_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalEventId in
        defaultSelectionActivityFiltering(
            "externalEventId.in=" + DEFAULT_EXTERNAL_EVENT_ID + "," + UPDATED_EXTERNAL_EVENT_ID,
            "externalEventId.in=" + UPDATED_EXTERNAL_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalEventId is not null
        defaultSelectionActivityFiltering("externalEventId.specified=true", "externalEventId.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalEventIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalEventId contains
        defaultSelectionActivityFiltering(
            "externalEventId.contains=" + DEFAULT_EXTERNAL_EVENT_ID,
            "externalEventId.contains=" + UPDATED_EXTERNAL_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByExternalEventIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        // Get all the selectionActivityList where externalEventId does not contain
        defaultSelectionActivityFiltering(
            "externalEventId.doesNotContain=" + UPDATED_EXTERNAL_EVENT_ID,
            "externalEventId.doesNotContain=" + DEFAULT_EXTERNAL_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSelectionActivitiesByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            selectionActivityRepository.saveAndFlush(selectionActivity);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        selectionActivity.setApplication(application);
        selectionActivityRepository.saveAndFlush(selectionActivity);
        Long applicationId = application.getId();
        // Get all the selectionActivityList where application equals to applicationId
        defaultSelectionActivityShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the selectionActivityList where application equals to (applicationId + 1)
        defaultSelectionActivityShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultSelectionActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSelectionActivityShouldBeFound(shouldBeFound);
        defaultSelectionActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSelectionActivityShouldBeFound(String filter) throws Exception {
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectionActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].meetingRoomId").value(hasItem(DEFAULT_MEETING_ROOM_ID.intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].scheduleVersion").value(hasItem(DEFAULT_SCHEDULE_VERSION)))
            .andExpect(jsonPath("$.[*].changeReason").value(hasItem(DEFAULT_CHANGE_REASON)))
            .andExpect(jsonPath("$.[*].externalCalendarProvider").value(hasItem(DEFAULT_EXTERNAL_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)));

        // Check, that the count call also returns 1
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSelectionActivityShouldNotBeFound(String filter) throws Exception {
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSelectionActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSelectionActivity() throws Exception {
        // Get the selectionActivity
        restSelectionActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSelectionActivity() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the selectionActivity
        SelectionActivity updatedSelectionActivity = selectionActivityRepository.findById(selectionActivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSelectionActivity are not directly saved in db
        em.detach(updatedSelectionActivity);
        updatedSelectionActivity
            .stageType(UPDATED_STAGE_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .meetingRoomId(UPDATED_MEETING_ROOM_ID)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS)
            .scheduleVersion(UPDATED_SCHEDULE_VERSION)
            .changeReason(UPDATED_CHANGE_REASON)
            .externalCalendarProvider(UPDATED_EXTERNAL_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID);
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(updatedSelectionActivity);

        restSelectionActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, selectionActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(selectionActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSelectionActivityToMatchAllProperties(updatedSelectionActivity);
    }

    @Test
    @Transactional
    void putNonExistingSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, selectionActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(selectionActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(selectionActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSelectionActivityWithPatch() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the selectionActivity using partial update
        SelectionActivity partialUpdatedSelectionActivity = new SelectionActivity();
        partialUpdatedSelectionActivity.setId(selectionActivity.getId());

        partialUpdatedSelectionActivity
            .activityType(UPDATED_ACTIVITY_TYPE)
            .meetingRoomId(UPDATED_MEETING_ROOM_ID)
            .scheduleVersion(UPDATED_SCHEDULE_VERSION)
            .changeReason(UPDATED_CHANGE_REASON);

        restSelectionActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectionActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSelectionActivity))
            )
            .andExpect(status().isOk());

        // Validate the SelectionActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSelectionActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSelectionActivity, selectionActivity),
            getPersistedSelectionActivity(selectionActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateSelectionActivityWithPatch() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the selectionActivity using partial update
        SelectionActivity partialUpdatedSelectionActivity = new SelectionActivity();
        partialUpdatedSelectionActivity.setId(selectionActivity.getId());

        partialUpdatedSelectionActivity
            .stageType(UPDATED_STAGE_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .meetingRoomId(UPDATED_MEETING_ROOM_ID)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS)
            .scheduleVersion(UPDATED_SCHEDULE_VERSION)
            .changeReason(UPDATED_CHANGE_REASON)
            .externalCalendarProvider(UPDATED_EXTERNAL_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID);

        restSelectionActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectionActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSelectionActivity))
            )
            .andExpect(status().isOk());

        // Validate the SelectionActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSelectionActivityUpdatableFieldsEquals(
            partialUpdatedSelectionActivity,
            getPersistedSelectionActivity(partialUpdatedSelectionActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, selectionActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(selectionActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(selectionActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSelectionActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        selectionActivity.setId(longCount.incrementAndGet());

        // Create the SelectionActivity
        SelectionActivityDTO selectionActivityDTO = selectionActivityMapper.toDto(selectionActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(selectionActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectionActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSelectionActivity() throws Exception {
        // Initialize the database
        insertedSelectionActivity = selectionActivityRepository.saveAndFlush(selectionActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the selectionActivity
        restSelectionActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, selectionActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return selectionActivityRepository.count();
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

    protected SelectionActivity getPersistedSelectionActivity(SelectionActivity selectionActivity) {
        return selectionActivityRepository.findById(selectionActivity.getId()).orElseThrow();
    }

    protected void assertPersistedSelectionActivityToMatchAllProperties(SelectionActivity expectedSelectionActivity) {
        assertSelectionActivityAllPropertiesEquals(expectedSelectionActivity, getPersistedSelectionActivity(expectedSelectionActivity));
    }

    protected void assertPersistedSelectionActivityToMatchUpdatableProperties(SelectionActivity expectedSelectionActivity) {
        assertSelectionActivityAllUpdatablePropertiesEquals(
            expectedSelectionActivity,
            getPersistedSelectionActivity(expectedSelectionActivity)
        );
    }
}
