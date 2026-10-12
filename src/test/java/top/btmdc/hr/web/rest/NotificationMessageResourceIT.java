package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.NotificationMessageAsserts.*;
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
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.mapper.NotificationMessageMapper;

/**
 * Integration tests for the {@link NotificationMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class NotificationMessageResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_TEMPLATE_ID = 1L;
    private static final Long UPDATED_TEMPLATE_ID = 2L;
    private static final Long SMALLER_TEMPLATE_ID = 1L - 1L;

    private static final NotificationMessageStatus DEFAULT_STATUS = NotificationMessageStatus.WAITING_CONFIRMATION;
    private static final NotificationMessageStatus UPDATED_STATUS = NotificationMessageStatus.PENDING;

    private static final String DEFAULT_SEND_MODE = "AAAAAAAAAA";
    private static final String UPDATED_SEND_MODE = "BBBBBBBBBB";

    private static final String DEFAULT_FINAL_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_FINAL_BODY = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENTS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENTS_JSON = "BBBBBBBBBB";

    private static final Integer DEFAULT_RETRY_COUNT = 0;
    private static final Integer UPDATED_RETRY_COUNT = 1;
    private static final Integer SMALLER_RETRY_COUNT = 0 - 1;

    private static final Integer DEFAULT_MAX_RETRIES = 0;
    private static final Integer UPDATED_MAX_RETRIES = 1;
    private static final Integer SMALLER_MAX_RETRIES = 0 - 1;

    private static final String DEFAULT_LAST_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_LAST_ERROR = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIRMED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CONFIRMED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONFIRMED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMessageMockMvc;

    private NotificationMessage notificationMessage;

    private NotificationMessage insertedNotificationMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationMessage createEntity() {
        return new NotificationMessage()
            .eventType(DEFAULT_EVENT_TYPE)
            .templateId(DEFAULT_TEMPLATE_ID)
            .status(DEFAULT_STATUS)
            .sendMode(DEFAULT_SEND_MODE)
            .finalSubject(DEFAULT_FINAL_SUBJECT)
            .finalBody(DEFAULT_FINAL_BODY)
            .recipientsJson(DEFAULT_RECIPIENTS_JSON)
            .retryCount(DEFAULT_RETRY_COUNT)
            .maxRetries(DEFAULT_MAX_RETRIES)
            .lastError(DEFAULT_LAST_ERROR)
            .createdBy(DEFAULT_CREATED_BY)
            .confirmedBy(DEFAULT_CONFIRMED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .confirmedAt(DEFAULT_CONFIRMED_AT)
            .sentAt(DEFAULT_SENT_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationMessage createUpdatedEntity() {
        return new NotificationMessage()
            .eventType(UPDATED_EVENT_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .status(UPDATED_STATUS)
            .sendMode(UPDATED_SEND_MODE)
            .finalSubject(UPDATED_FINAL_SUBJECT)
            .finalBody(UPDATED_FINAL_BODY)
            .recipientsJson(UPDATED_RECIPIENTS_JSON)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .lastError(UPDATED_LAST_ERROR)
            .createdBy(UPDATED_CREATED_BY)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .confirmedAt(UPDATED_CONFIRMED_AT)
            .sentAt(UPDATED_SENT_AT);
    }

    @BeforeEach
    void initTest() {
        notificationMessage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationMessage != null) {
            notificationMessageRepository.delete(insertedNotificationMessage);
            insertedNotificationMessage = null;
        }
    }

    @Test
    @Transactional
    void createNotificationMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);
        var returnedNotificationMessageDTO = om.readValue(
            restNotificationMessageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationMessageDTO.class
        );

        // Validate the NotificationMessage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationMessage = notificationMessageMapper.toEntity(returnedNotificationMessageDTO);
        assertNotificationMessageUpdatableFieldsEquals(
            returnedNotificationMessage,
            getPersistedNotificationMessage(returnedNotificationMessage)
        );

        insertedNotificationMessage = returnedNotificationMessage;
    }

    @Test
    @Transactional
    void createNotificationMessageWithExistingId() throws Exception {
        // Create the NotificationMessage with an existing ID
        notificationMessage.setId(1L);
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setEventType(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setStatus(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSendModeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setSendMode(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinalSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setFinalSubject(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRetryCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setRetryCount(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxRetriesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setMaxRetries(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setCreatedBy(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationMessage.setCreatedAt(null);

        // Create the NotificationMessage, which fails.
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        restNotificationMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationMessages() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendMode").value(hasItem(DEFAULT_SEND_MODE)))
            .andExpect(jsonPath("$.[*].finalSubject").value(hasItem(DEFAULT_FINAL_SUBJECT)))
            .andExpect(jsonPath("$.[*].finalBody").value(hasItem(DEFAULT_FINAL_BODY)))
            .andExpect(jsonPath("$.[*].recipientsJson").value(hasItem(DEFAULT_RECIPIENTS_JSON)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].maxRetries").value(hasItem(DEFAULT_MAX_RETRIES)))
            .andExpect(jsonPath("$.[*].lastError").value(hasItem(DEFAULT_LAST_ERROR)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].confirmedBy").value(hasItem(DEFAULT_CONFIRMED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].confirmedAt").value(hasItem(DEFAULT_CONFIRMED_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())));
    }

    @Test
    @Transactional
    void getNotificationMessage() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get the notificationMessage
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationMessage.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sendMode").value(DEFAULT_SEND_MODE))
            .andExpect(jsonPath("$.finalSubject").value(DEFAULT_FINAL_SUBJECT))
            .andExpect(jsonPath("$.finalBody").value(DEFAULT_FINAL_BODY))
            .andExpect(jsonPath("$.recipientsJson").value(DEFAULT_RECIPIENTS_JSON))
            .andExpect(jsonPath("$.retryCount").value(DEFAULT_RETRY_COUNT))
            .andExpect(jsonPath("$.maxRetries").value(DEFAULT_MAX_RETRIES))
            .andExpect(jsonPath("$.lastError").value(DEFAULT_LAST_ERROR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.confirmedBy").value(DEFAULT_CONFIRMED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.confirmedAt").value(DEFAULT_CONFIRMED_AT.toString()))
            .andExpect(jsonPath("$.sentAt").value(DEFAULT_SENT_AT.toString()));
    }

    @Test
    @Transactional
    void getNotificationMessagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        Long id = notificationMessage.getId();

        defaultNotificationMessageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationMessageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationMessageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where eventType equals to
        defaultNotificationMessageFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where eventType in
        defaultNotificationMessageFiltering(
            "eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE,
            "eventType.in=" + UPDATED_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where eventType is not null
        defaultNotificationMessageFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where eventType contains
        defaultNotificationMessageFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where eventType does not contain
        defaultNotificationMessageFiltering(
            "eventType.doesNotContain=" + UPDATED_EVENT_TYPE,
            "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId equals to
        defaultNotificationMessageFiltering("templateId.equals=" + DEFAULT_TEMPLATE_ID, "templateId.equals=" + UPDATED_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId in
        defaultNotificationMessageFiltering(
            "templateId.in=" + DEFAULT_TEMPLATE_ID + "," + UPDATED_TEMPLATE_ID,
            "templateId.in=" + UPDATED_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId is not null
        defaultNotificationMessageFiltering("templateId.specified=true", "templateId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId is greater than or equal to
        defaultNotificationMessageFiltering(
            "templateId.greaterThanOrEqual=" + DEFAULT_TEMPLATE_ID,
            "templateId.greaterThanOrEqual=" + UPDATED_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId is less than or equal to
        defaultNotificationMessageFiltering(
            "templateId.lessThanOrEqual=" + DEFAULT_TEMPLATE_ID,
            "templateId.lessThanOrEqual=" + SMALLER_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId is less than
        defaultNotificationMessageFiltering("templateId.lessThan=" + UPDATED_TEMPLATE_ID, "templateId.lessThan=" + DEFAULT_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByTemplateIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where templateId is greater than
        defaultNotificationMessageFiltering(
            "templateId.greaterThan=" + SMALLER_TEMPLATE_ID,
            "templateId.greaterThan=" + DEFAULT_TEMPLATE_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where status equals to
        defaultNotificationMessageFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where status in
        defaultNotificationMessageFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where status is not null
        defaultNotificationMessageFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySendModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sendMode equals to
        defaultNotificationMessageFiltering("sendMode.equals=" + DEFAULT_SEND_MODE, "sendMode.equals=" + UPDATED_SEND_MODE);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySendModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sendMode in
        defaultNotificationMessageFiltering(
            "sendMode.in=" + DEFAULT_SEND_MODE + "," + UPDATED_SEND_MODE,
            "sendMode.in=" + UPDATED_SEND_MODE
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySendModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sendMode is not null
        defaultNotificationMessageFiltering("sendMode.specified=true", "sendMode.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySendModeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sendMode contains
        defaultNotificationMessageFiltering("sendMode.contains=" + DEFAULT_SEND_MODE, "sendMode.contains=" + UPDATED_SEND_MODE);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySendModeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sendMode does not contain
        defaultNotificationMessageFiltering("sendMode.doesNotContain=" + UPDATED_SEND_MODE, "sendMode.doesNotContain=" + DEFAULT_SEND_MODE);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByFinalSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where finalSubject equals to
        defaultNotificationMessageFiltering("finalSubject.equals=" + DEFAULT_FINAL_SUBJECT, "finalSubject.equals=" + UPDATED_FINAL_SUBJECT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByFinalSubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where finalSubject in
        defaultNotificationMessageFiltering(
            "finalSubject.in=" + DEFAULT_FINAL_SUBJECT + "," + UPDATED_FINAL_SUBJECT,
            "finalSubject.in=" + UPDATED_FINAL_SUBJECT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByFinalSubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where finalSubject is not null
        defaultNotificationMessageFiltering("finalSubject.specified=true", "finalSubject.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByFinalSubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where finalSubject contains
        defaultNotificationMessageFiltering(
            "finalSubject.contains=" + DEFAULT_FINAL_SUBJECT,
            "finalSubject.contains=" + UPDATED_FINAL_SUBJECT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByFinalSubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where finalSubject does not contain
        defaultNotificationMessageFiltering(
            "finalSubject.doesNotContain=" + UPDATED_FINAL_SUBJECT,
            "finalSubject.doesNotContain=" + DEFAULT_FINAL_SUBJECT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount equals to
        defaultNotificationMessageFiltering("retryCount.equals=" + DEFAULT_RETRY_COUNT, "retryCount.equals=" + UPDATED_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount in
        defaultNotificationMessageFiltering(
            "retryCount.in=" + DEFAULT_RETRY_COUNT + "," + UPDATED_RETRY_COUNT,
            "retryCount.in=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount is not null
        defaultNotificationMessageFiltering("retryCount.specified=true", "retryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount is greater than or equal to
        defaultNotificationMessageFiltering(
            "retryCount.greaterThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.greaterThanOrEqual=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount is less than or equal to
        defaultNotificationMessageFiltering(
            "retryCount.lessThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.lessThanOrEqual=" + SMALLER_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount is less than
        defaultNotificationMessageFiltering("retryCount.lessThan=" + UPDATED_RETRY_COUNT, "retryCount.lessThan=" + DEFAULT_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByRetryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where retryCount is greater than
        defaultNotificationMessageFiltering(
            "retryCount.greaterThan=" + SMALLER_RETRY_COUNT,
            "retryCount.greaterThan=" + DEFAULT_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries equals to
        defaultNotificationMessageFiltering("maxRetries.equals=" + DEFAULT_MAX_RETRIES, "maxRetries.equals=" + UPDATED_MAX_RETRIES);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries in
        defaultNotificationMessageFiltering(
            "maxRetries.in=" + DEFAULT_MAX_RETRIES + "," + UPDATED_MAX_RETRIES,
            "maxRetries.in=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries is not null
        defaultNotificationMessageFiltering("maxRetries.specified=true", "maxRetries.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries is greater than or equal to
        defaultNotificationMessageFiltering(
            "maxRetries.greaterThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.greaterThanOrEqual=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries is less than or equal to
        defaultNotificationMessageFiltering(
            "maxRetries.lessThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.lessThanOrEqual=" + SMALLER_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries is less than
        defaultNotificationMessageFiltering("maxRetries.lessThan=" + UPDATED_MAX_RETRIES, "maxRetries.lessThan=" + DEFAULT_MAX_RETRIES);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByMaxRetriesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where maxRetries is greater than
        defaultNotificationMessageFiltering(
            "maxRetries.greaterThan=" + SMALLER_MAX_RETRIES,
            "maxRetries.greaterThan=" + DEFAULT_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdBy equals to
        defaultNotificationMessageFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdBy in
        defaultNotificationMessageFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdBy is not null
        defaultNotificationMessageFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdBy contains
        defaultNotificationMessageFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdBy does not contain
        defaultNotificationMessageFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedBy equals to
        defaultNotificationMessageFiltering("confirmedBy.equals=" + DEFAULT_CONFIRMED_BY, "confirmedBy.equals=" + UPDATED_CONFIRMED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedBy in
        defaultNotificationMessageFiltering(
            "confirmedBy.in=" + DEFAULT_CONFIRMED_BY + "," + UPDATED_CONFIRMED_BY,
            "confirmedBy.in=" + UPDATED_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedBy is not null
        defaultNotificationMessageFiltering("confirmedBy.specified=true", "confirmedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedByContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedBy contains
        defaultNotificationMessageFiltering("confirmedBy.contains=" + DEFAULT_CONFIRMED_BY, "confirmedBy.contains=" + UPDATED_CONFIRMED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedBy does not contain
        defaultNotificationMessageFiltering(
            "confirmedBy.doesNotContain=" + UPDATED_CONFIRMED_BY,
            "confirmedBy.doesNotContain=" + DEFAULT_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdAt equals to
        defaultNotificationMessageFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdAt in
        defaultNotificationMessageFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where createdAt is not null
        defaultNotificationMessageFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedAt equals to
        defaultNotificationMessageFiltering("confirmedAt.equals=" + DEFAULT_CONFIRMED_AT, "confirmedAt.equals=" + UPDATED_CONFIRMED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedAt in
        defaultNotificationMessageFiltering(
            "confirmedAt.in=" + DEFAULT_CONFIRMED_AT + "," + UPDATED_CONFIRMED_AT,
            "confirmedAt.in=" + UPDATED_CONFIRMED_AT
        );
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByConfirmedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where confirmedAt is not null
        defaultNotificationMessageFiltering("confirmedAt.specified=true", "confirmedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySentAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sentAt equals to
        defaultNotificationMessageFiltering("sentAt.equals=" + DEFAULT_SENT_AT, "sentAt.equals=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySentAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sentAt in
        defaultNotificationMessageFiltering("sentAt.in=" + DEFAULT_SENT_AT + "," + UPDATED_SENT_AT, "sentAt.in=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    void getAllNotificationMessagesBySentAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        // Get all the notificationMessageList where sentAt is not null
        defaultNotificationMessageFiltering("sentAt.specified=true", "sentAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationMessagesByApplicationIsEqualToSomething() throws Exception {
        RecruitmentApplication application;
        if (TestUtil.findAll(em, RecruitmentApplication.class).isEmpty()) {
            notificationMessageRepository.saveAndFlush(notificationMessage);
            application = RecruitmentApplicationResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, RecruitmentApplication.class).get(0);
        }
        em.persist(application);
        em.flush();
        notificationMessage.setApplication(application);
        notificationMessageRepository.saveAndFlush(notificationMessage);
        Long applicationId = application.getId();
        // Get all the notificationMessageList where application equals to applicationId
        defaultNotificationMessageShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the notificationMessageList where application equals to (applicationId + 1)
        defaultNotificationMessageShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    private void defaultNotificationMessageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationMessageShouldBeFound(shouldBeFound);
        defaultNotificationMessageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationMessageShouldBeFound(String filter) throws Exception {
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendMode").value(hasItem(DEFAULT_SEND_MODE)))
            .andExpect(jsonPath("$.[*].finalSubject").value(hasItem(DEFAULT_FINAL_SUBJECT)))
            .andExpect(jsonPath("$.[*].finalBody").value(hasItem(DEFAULT_FINAL_BODY)))
            .andExpect(jsonPath("$.[*].recipientsJson").value(hasItem(DEFAULT_RECIPIENTS_JSON)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].maxRetries").value(hasItem(DEFAULT_MAX_RETRIES)))
            .andExpect(jsonPath("$.[*].lastError").value(hasItem(DEFAULT_LAST_ERROR)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].confirmedBy").value(hasItem(DEFAULT_CONFIRMED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].confirmedAt").value(hasItem(DEFAULT_CONFIRMED_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())));

        // Check, that the count call also returns 1
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationMessageShouldNotBeFound(String filter) throws Exception {
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationMessage() throws Exception {
        // Get the notificationMessage
        restNotificationMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationMessage() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationMessage
        NotificationMessage updatedNotificationMessage = notificationMessageRepository.findById(notificationMessage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationMessage are not directly saved in db
        em.detach(updatedNotificationMessage);
        updatedNotificationMessage
            .eventType(UPDATED_EVENT_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .status(UPDATED_STATUS)
            .sendMode(UPDATED_SEND_MODE)
            .finalSubject(UPDATED_FINAL_SUBJECT)
            .finalBody(UPDATED_FINAL_BODY)
            .recipientsJson(UPDATED_RECIPIENTS_JSON)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .lastError(UPDATED_LAST_ERROR)
            .createdBy(UPDATED_CREATED_BY)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .confirmedAt(UPDATED_CONFIRMED_AT)
            .sentAt(UPDATED_SENT_AT);
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(updatedNotificationMessage);

        restNotificationMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationMessageToMatchAllProperties(updatedNotificationMessage);
    }

    @Test
    @Transactional
    void putNonExistingNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationMessageWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationMessage using partial update
        NotificationMessage partialUpdatedNotificationMessage = new NotificationMessage();
        partialUpdatedNotificationMessage.setId(notificationMessage.getId());

        partialUpdatedNotificationMessage
            .eventType(UPDATED_EVENT_TYPE)
            .status(UPDATED_STATUS)
            .finalSubject(UPDATED_FINAL_SUBJECT)
            .finalBody(UPDATED_FINAL_BODY)
            .recipientsJson(UPDATED_RECIPIENTS_JSON)
            .lastError(UPDATED_LAST_ERROR)
            .createdAt(UPDATED_CREATED_AT)
            .confirmedAt(UPDATED_CONFIRMED_AT);

        restNotificationMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationMessage))
            )
            .andExpect(status().isOk());

        // Validate the NotificationMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationMessageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationMessage, notificationMessage),
            getPersistedNotificationMessage(notificationMessage)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationMessageWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationMessage using partial update
        NotificationMessage partialUpdatedNotificationMessage = new NotificationMessage();
        partialUpdatedNotificationMessage.setId(notificationMessage.getId());

        partialUpdatedNotificationMessage
            .eventType(UPDATED_EVENT_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .status(UPDATED_STATUS)
            .sendMode(UPDATED_SEND_MODE)
            .finalSubject(UPDATED_FINAL_SUBJECT)
            .finalBody(UPDATED_FINAL_BODY)
            .recipientsJson(UPDATED_RECIPIENTS_JSON)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .lastError(UPDATED_LAST_ERROR)
            .createdBy(UPDATED_CREATED_BY)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .confirmedAt(UPDATED_CONFIRMED_AT)
            .sentAt(UPDATED_SENT_AT);

        restNotificationMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationMessage))
            )
            .andExpect(status().isOk());

        // Validate the NotificationMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationMessageUpdatableFieldsEquals(
            partialUpdatedNotificationMessage,
            getPersistedNotificationMessage(partialUpdatedNotificationMessage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationMessage.setId(longCount.incrementAndGet());

        // Create the NotificationMessage
        NotificationMessageDTO notificationMessageDTO = notificationMessageMapper.toDto(notificationMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMessageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationMessage() throws Exception {
        // Initialize the database
        insertedNotificationMessage = notificationMessageRepository.saveAndFlush(notificationMessage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationMessage
        restNotificationMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationMessageRepository.count();
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

    protected NotificationMessage getPersistedNotificationMessage(NotificationMessage notificationMessage) {
        return notificationMessageRepository.findById(notificationMessage.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationMessageToMatchAllProperties(NotificationMessage expectedNotificationMessage) {
        assertNotificationMessageAllPropertiesEquals(
            expectedNotificationMessage,
            getPersistedNotificationMessage(expectedNotificationMessage)
        );
    }

    protected void assertPersistedNotificationMessageToMatchUpdatableProperties(NotificationMessage expectedNotificationMessage) {
        assertNotificationMessageAllUpdatablePropertiesEquals(
            expectedNotificationMessage,
            getPersistedNotificationMessage(expectedNotificationMessage)
        );
    }
}
