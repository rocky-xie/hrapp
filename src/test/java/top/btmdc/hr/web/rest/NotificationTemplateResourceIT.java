package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.NotificationTemplateAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import top.btmdc.hr.domain.NotificationTemplate;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.NotificationTemplateRepository;
import top.btmdc.hr.service.dto.NotificationTemplateDTO;
import top.btmdc.hr.service.mapper.NotificationTemplateMapper;

/**
 * Integration tests for the {@link NotificationTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class NotificationTemplateResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final RecruitmentType DEFAULT_RECRUITMENT_TYPE = RecruitmentType.GRADUATE;
    private static final RecruitmentType UPDATED_RECRUITMENT_TYPE = RecruitmentType.MID_CAREER;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;
    private static final Integer SMALLER_VERSION_NUMBER = 1 - 1;

    private static final String DEFAULT_SUBJECT_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_TEMPLATE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_BODY_TEMPLATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EXTERNAL_SAFE = false;
    private static final Boolean UPDATED_EXTERNAL_SAFE = true;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final String ENTITY_API_URL = "/api/notification-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationTemplateMapper notificationTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationTemplateMockMvc;

    private NotificationTemplate notificationTemplate;

    private NotificationTemplate insertedNotificationTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTemplate createEntity() {
        return new NotificationTemplate()
            .eventType(DEFAULT_EVENT_TYPE)
            .recruitmentType(DEFAULT_RECRUITMENT_TYPE)
            .language(DEFAULT_LANGUAGE)
            .versionNumber(DEFAULT_VERSION_NUMBER)
            .subjectTemplate(DEFAULT_SUBJECT_TEMPLATE)
            .bodyTemplate(DEFAULT_BODY_TEMPLATE)
            .externalSafe(DEFAULT_EXTERNAL_SAFE)
            .published(DEFAULT_PUBLISHED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTemplate createUpdatedEntity() {
        return new NotificationTemplate()
            .eventType(UPDATED_EVENT_TYPE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .language(UPDATED_LANGUAGE)
            .versionNumber(UPDATED_VERSION_NUMBER)
            .subjectTemplate(UPDATED_SUBJECT_TEMPLATE)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .externalSafe(UPDATED_EXTERNAL_SAFE)
            .published(UPDATED_PUBLISHED);
    }

    @BeforeEach
    void initTest() {
        notificationTemplate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationTemplate != null) {
            notificationTemplateRepository.delete(insertedNotificationTemplate);
            insertedNotificationTemplate = null;
        }
    }

    @Test
    @Transactional
    void createNotificationTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);
        var returnedNotificationTemplateDTO = om.readValue(
            restNotificationTemplateMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationTemplateDTO.class
        );

        // Validate the NotificationTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationTemplate = notificationTemplateMapper.toEntity(returnedNotificationTemplateDTO);
        assertNotificationTemplateUpdatableFieldsEquals(
            returnedNotificationTemplate,
            getPersistedNotificationTemplate(returnedNotificationTemplate)
        );

        insertedNotificationTemplate = returnedNotificationTemplate;
    }

    @Test
    @Transactional
    void createNotificationTemplateWithExistingId() throws Exception {
        // Create the NotificationTemplate with an existing ID
        notificationTemplate.setId(1L);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setEventType(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setLanguage(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setVersionNumber(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setSubjectTemplate(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalSafeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setExternalSafe(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublishedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setPublished(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationTemplates() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].subjectTemplate").value(hasItem(DEFAULT_SUBJECT_TEMPLATE)))
            .andExpect(jsonPath("$.[*].bodyTemplate").value(hasItem(DEFAULT_BODY_TEMPLATE)))
            .andExpect(jsonPath("$.[*].externalSafe").value(hasItem(DEFAULT_EXTERNAL_SAFE)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED)));
    }

    @Test
    @Transactional
    void getNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get the notificationTemplate
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationTemplate.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.recruitmentType").value(DEFAULT_RECRUITMENT_TYPE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.subjectTemplate").value(DEFAULT_SUBJECT_TEMPLATE))
            .andExpect(jsonPath("$.bodyTemplate").value(DEFAULT_BODY_TEMPLATE))
            .andExpect(jsonPath("$.externalSafe").value(DEFAULT_EXTERNAL_SAFE))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED));
    }

    @Test
    @Transactional
    void getNotificationTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        Long id = notificationTemplate.getId();

        defaultNotificationTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where eventType equals to
        defaultNotificationTemplateFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where eventType in
        defaultNotificationTemplateFiltering(
            "eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE,
            "eventType.in=" + UPDATED_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where eventType is not null
        defaultNotificationTemplateFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where eventType contains
        defaultNotificationTemplateFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where eventType does not contain
        defaultNotificationTemplateFiltering(
            "eventType.doesNotContain=" + UPDATED_EVENT_TYPE,
            "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByRecruitmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where recruitmentType equals to
        defaultNotificationTemplateFiltering(
            "recruitmentType.equals=" + DEFAULT_RECRUITMENT_TYPE,
            "recruitmentType.equals=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByRecruitmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where recruitmentType in
        defaultNotificationTemplateFiltering(
            "recruitmentType.in=" + DEFAULT_RECRUITMENT_TYPE + "," + UPDATED_RECRUITMENT_TYPE,
            "recruitmentType.in=" + UPDATED_RECRUITMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByRecruitmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where recruitmentType is not null
        defaultNotificationTemplateFiltering("recruitmentType.specified=true", "recruitmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where language equals to
        defaultNotificationTemplateFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where language in
        defaultNotificationTemplateFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where language is not null
        defaultNotificationTemplateFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where language contains
        defaultNotificationTemplateFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where language does not contain
        defaultNotificationTemplateFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber equals to
        defaultNotificationTemplateFiltering(
            "versionNumber.equals=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.equals=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber in
        defaultNotificationTemplateFiltering(
            "versionNumber.in=" + DEFAULT_VERSION_NUMBER + "," + UPDATED_VERSION_NUMBER,
            "versionNumber.in=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber is not null
        defaultNotificationTemplateFiltering("versionNumber.specified=true", "versionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber is greater than or equal to
        defaultNotificationTemplateFiltering(
            "versionNumber.greaterThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.greaterThanOrEqual=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber is less than or equal to
        defaultNotificationTemplateFiltering(
            "versionNumber.lessThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.lessThanOrEqual=" + SMALLER_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber is less than
        defaultNotificationTemplateFiltering(
            "versionNumber.lessThan=" + UPDATED_VERSION_NUMBER,
            "versionNumber.lessThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByVersionNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where versionNumber is greater than
        defaultNotificationTemplateFiltering(
            "versionNumber.greaterThan=" + SMALLER_VERSION_NUMBER,
            "versionNumber.greaterThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesBySubjectTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where subjectTemplate equals to
        defaultNotificationTemplateFiltering(
            "subjectTemplate.equals=" + DEFAULT_SUBJECT_TEMPLATE,
            "subjectTemplate.equals=" + UPDATED_SUBJECT_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesBySubjectTemplateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where subjectTemplate in
        defaultNotificationTemplateFiltering(
            "subjectTemplate.in=" + DEFAULT_SUBJECT_TEMPLATE + "," + UPDATED_SUBJECT_TEMPLATE,
            "subjectTemplate.in=" + UPDATED_SUBJECT_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesBySubjectTemplateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where subjectTemplate is not null
        defaultNotificationTemplateFiltering("subjectTemplate.specified=true", "subjectTemplate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesBySubjectTemplateContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where subjectTemplate contains
        defaultNotificationTemplateFiltering(
            "subjectTemplate.contains=" + DEFAULT_SUBJECT_TEMPLATE,
            "subjectTemplate.contains=" + UPDATED_SUBJECT_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesBySubjectTemplateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where subjectTemplate does not contain
        defaultNotificationTemplateFiltering(
            "subjectTemplate.doesNotContain=" + UPDATED_SUBJECT_TEMPLATE,
            "subjectTemplate.doesNotContain=" + DEFAULT_SUBJECT_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByExternalSafeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where externalSafe equals to
        defaultNotificationTemplateFiltering(
            "externalSafe.equals=" + DEFAULT_EXTERNAL_SAFE,
            "externalSafe.equals=" + UPDATED_EXTERNAL_SAFE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByExternalSafeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where externalSafe in
        defaultNotificationTemplateFiltering(
            "externalSafe.in=" + DEFAULT_EXTERNAL_SAFE + "," + UPDATED_EXTERNAL_SAFE,
            "externalSafe.in=" + UPDATED_EXTERNAL_SAFE
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByExternalSafeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where externalSafe is not null
        defaultNotificationTemplateFiltering("externalSafe.specified=true", "externalSafe.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where published equals to
        defaultNotificationTemplateFiltering("published.equals=" + DEFAULT_PUBLISHED, "published.equals=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where published in
        defaultNotificationTemplateFiltering(
            "published.in=" + DEFAULT_PUBLISHED + "," + UPDATED_PUBLISHED,
            "published.in=" + UPDATED_PUBLISHED
        );
    }

    @Test
    @Transactional
    void getAllNotificationTemplatesByPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList where published is not null
        defaultNotificationTemplateFiltering("published.specified=true", "published.specified=false");
    }

    private void defaultNotificationTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationTemplateShouldBeFound(shouldBeFound);
        defaultNotificationTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationTemplateShouldBeFound(String filter) throws Exception {
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].recruitmentType").value(hasItem(DEFAULT_RECRUITMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].subjectTemplate").value(hasItem(DEFAULT_SUBJECT_TEMPLATE)))
            .andExpect(jsonPath("$.[*].bodyTemplate").value(hasItem(DEFAULT_BODY_TEMPLATE)))
            .andExpect(jsonPath("$.[*].externalSafe").value(hasItem(DEFAULT_EXTERNAL_SAFE)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED)));

        // Check, that the count call also returns 1
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationTemplateShouldNotBeFound(String filter) throws Exception {
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationTemplate() throws Exception {
        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate
        NotificationTemplate updatedNotificationTemplate = notificationTemplateRepository
            .findById(notificationTemplate.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationTemplate are not directly saved in db
        em.detach(updatedNotificationTemplate);
        updatedNotificationTemplate
            .eventType(UPDATED_EVENT_TYPE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .language(UPDATED_LANGUAGE)
            .versionNumber(UPDATED_VERSION_NUMBER)
            .subjectTemplate(UPDATED_SUBJECT_TEMPLATE)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .externalSafe(UPDATED_EXTERNAL_SAFE)
            .published(UPDATED_PUBLISHED);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(updatedNotificationTemplate);

        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationTemplateToMatchAllProperties(updatedNotificationTemplate);
    }

    @Test
    @Transactional
    void putNonExistingNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate using partial update
        NotificationTemplate partialUpdatedNotificationTemplate = new NotificationTemplate();
        partialUpdatedNotificationTemplate.setId(notificationTemplate.getId());

        partialUpdatedNotificationTemplate.eventType(UPDATED_EVENT_TYPE).published(UPDATED_PUBLISHED);

        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTemplate))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationTemplate, notificationTemplate),
            getPersistedNotificationTemplate(notificationTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate using partial update
        NotificationTemplate partialUpdatedNotificationTemplate = new NotificationTemplate();
        partialUpdatedNotificationTemplate.setId(notificationTemplate.getId());

        partialUpdatedNotificationTemplate
            .eventType(UPDATED_EVENT_TYPE)
            .recruitmentType(UPDATED_RECRUITMENT_TYPE)
            .language(UPDATED_LANGUAGE)
            .versionNumber(UPDATED_VERSION_NUMBER)
            .subjectTemplate(UPDATED_SUBJECT_TEMPLATE)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .externalSafe(UPDATED_EXTERNAL_SAFE)
            .published(UPDATED_PUBLISHED);

        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTemplate))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTemplateUpdatableFieldsEquals(
            partialUpdatedNotificationTemplate,
            getPersistedNotificationTemplate(partialUpdatedNotificationTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationTemplate
        restNotificationTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationTemplateRepository.count();
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

    protected NotificationTemplate getPersistedNotificationTemplate(NotificationTemplate notificationTemplate) {
        return notificationTemplateRepository.findById(notificationTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationTemplateToMatchAllProperties(NotificationTemplate expectedNotificationTemplate) {
        assertNotificationTemplateAllPropertiesEquals(
            expectedNotificationTemplate,
            getPersistedNotificationTemplate(expectedNotificationTemplate)
        );
    }

    protected void assertPersistedNotificationTemplateToMatchUpdatableProperties(NotificationTemplate expectedNotificationTemplate) {
        assertNotificationTemplateAllUpdatablePropertiesEquals(
            expectedNotificationTemplate,
            getPersistedNotificationTemplate(expectedNotificationTemplate)
        );
    }
}
