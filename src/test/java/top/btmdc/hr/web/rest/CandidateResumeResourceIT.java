package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.CandidateResumeAsserts.*;
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
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.service.mapper.CandidateResumeMapper;

/**
 * Integration tests for the {@link CandidateResumeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class CandidateResumeResourceIT {

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;
    private static final Integer SMALLER_VERSION_NUMBER = 1 - 1;

    private static final Instant DEFAULT_RECEIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 0L;
    private static final Long UPDATED_FILE_SIZE = 1L;
    private static final Long SMALLER_FILE_SIZE = 0L - 1L;

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_STRUCTURED_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_SUMMARY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_VERSION = false;
    private static final Boolean UPDATED_ACTIVE_VERSION = true;

    private static final String DEFAULT_UPLOADED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPLOADED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/candidate-resumes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CandidateResumeRepository candidateResumeRepository;

    @Autowired
    private CandidateResumeMapper candidateResumeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidateResumeMockMvc;

    private CandidateResume candidateResume;

    private CandidateResume insertedCandidateResume;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidateResume createEntity() {
        return new CandidateResume()
            .versionNumber(DEFAULT_VERSION_NUMBER)
            .receivedAt(DEFAULT_RECEIVED_AT)
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .mediaType(DEFAULT_MEDIA_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .sha256(DEFAULT_SHA_256)
            .storageKey(DEFAULT_STORAGE_KEY)
            .structuredSummary(DEFAULT_STRUCTURED_SUMMARY)
            .activeVersion(DEFAULT_ACTIVE_VERSION)
            .uploadedBy(DEFAULT_UPLOADED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidateResume createUpdatedEntity() {
        return new CandidateResume()
            .versionNumber(UPDATED_VERSION_NUMBER)
            .receivedAt(UPDATED_RECEIVED_AT)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .mediaType(UPDATED_MEDIA_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .sha256(UPDATED_SHA_256)
            .storageKey(UPDATED_STORAGE_KEY)
            .structuredSummary(UPDATED_STRUCTURED_SUMMARY)
            .activeVersion(UPDATED_ACTIVE_VERSION)
            .uploadedBy(UPDATED_UPLOADED_BY);
    }

    @BeforeEach
    void initTest() {
        candidateResume = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCandidateResume != null) {
            candidateResumeRepository.delete(insertedCandidateResume);
            insertedCandidateResume = null;
        }
    }

    @Test
    @Transactional
    void createCandidateResume() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);
        var returnedCandidateResumeDTO = om.readValue(
            restCandidateResumeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CandidateResumeDTO.class
        );

        // Validate the CandidateResume in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCandidateResume = candidateResumeMapper.toEntity(returnedCandidateResumeDTO);
        assertCandidateResumeUpdatableFieldsEquals(returnedCandidateResume, getPersistedCandidateResume(returnedCandidateResume));

        insertedCandidateResume = returnedCandidateResume;
    }

    @Test
    @Transactional
    void createCandidateResumeWithExistingId() throws Exception {
        // Create the CandidateResume with an existing ID
        candidateResume.setId(1L);
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVersionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setVersionNumber(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceivedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setReceivedAt(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginalFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setOriginalFileName(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setFileSize(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setSha256(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStorageKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setStorageKey(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setActiveVersion(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidateResume.setUploadedBy(null);

        // Create the CandidateResume, which fails.
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        restCandidateResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCandidateResumes() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateResume.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].storageKey").value(hasItem(DEFAULT_STORAGE_KEY)))
            .andExpect(jsonPath("$.[*].structuredSummary").value(hasItem(DEFAULT_STRUCTURED_SUMMARY)))
            .andExpect(jsonPath("$.[*].activeVersion").value(hasItem(DEFAULT_ACTIVE_VERSION)))
            .andExpect(jsonPath("$.[*].uploadedBy").value(hasItem(DEFAULT_UPLOADED_BY)));
    }

    @Test
    @Transactional
    void getCandidateResume() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get the candidateResume
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL_ID, candidateResume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidateResume.getId().intValue()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.receivedAt").value(DEFAULT_RECEIVED_AT.toString()))
            .andExpect(jsonPath("$.originalFileName").value(DEFAULT_ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$.mediaType").value(DEFAULT_MEDIA_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.storageKey").value(DEFAULT_STORAGE_KEY))
            .andExpect(jsonPath("$.structuredSummary").value(DEFAULT_STRUCTURED_SUMMARY))
            .andExpect(jsonPath("$.activeVersion").value(DEFAULT_ACTIVE_VERSION))
            .andExpect(jsonPath("$.uploadedBy").value(DEFAULT_UPLOADED_BY));
    }

    @Test
    @Transactional
    void getCandidateResumesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        Long id = candidateResume.getId();

        defaultCandidateResumeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCandidateResumeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCandidateResumeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber equals to
        defaultCandidateResumeFiltering("versionNumber.equals=" + DEFAULT_VERSION_NUMBER, "versionNumber.equals=" + UPDATED_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber in
        defaultCandidateResumeFiltering(
            "versionNumber.in=" + DEFAULT_VERSION_NUMBER + "," + UPDATED_VERSION_NUMBER,
            "versionNumber.in=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber is not null
        defaultCandidateResumeFiltering("versionNumber.specified=true", "versionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber is greater than or equal to
        defaultCandidateResumeFiltering(
            "versionNumber.greaterThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.greaterThanOrEqual=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber is less than or equal to
        defaultCandidateResumeFiltering(
            "versionNumber.lessThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.lessThanOrEqual=" + SMALLER_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber is less than
        defaultCandidateResumeFiltering(
            "versionNumber.lessThan=" + UPDATED_VERSION_NUMBER,
            "versionNumber.lessThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByVersionNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where versionNumber is greater than
        defaultCandidateResumeFiltering(
            "versionNumber.greaterThan=" + SMALLER_VERSION_NUMBER,
            "versionNumber.greaterThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByReceivedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where receivedAt equals to
        defaultCandidateResumeFiltering("receivedAt.equals=" + DEFAULT_RECEIVED_AT, "receivedAt.equals=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByReceivedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where receivedAt in
        defaultCandidateResumeFiltering(
            "receivedAt.in=" + DEFAULT_RECEIVED_AT + "," + UPDATED_RECEIVED_AT,
            "receivedAt.in=" + UPDATED_RECEIVED_AT
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByReceivedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where receivedAt is not null
        defaultCandidateResumeFiltering("receivedAt.specified=true", "receivedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByOriginalFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where originalFileName equals to
        defaultCandidateResumeFiltering(
            "originalFileName.equals=" + DEFAULT_ORIGINAL_FILE_NAME,
            "originalFileName.equals=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByOriginalFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where originalFileName in
        defaultCandidateResumeFiltering(
            "originalFileName.in=" + DEFAULT_ORIGINAL_FILE_NAME + "," + UPDATED_ORIGINAL_FILE_NAME,
            "originalFileName.in=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByOriginalFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where originalFileName is not null
        defaultCandidateResumeFiltering("originalFileName.specified=true", "originalFileName.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByOriginalFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where originalFileName contains
        defaultCandidateResumeFiltering(
            "originalFileName.contains=" + DEFAULT_ORIGINAL_FILE_NAME,
            "originalFileName.contains=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByOriginalFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where originalFileName does not contain
        defaultCandidateResumeFiltering(
            "originalFileName.doesNotContain=" + UPDATED_ORIGINAL_FILE_NAME,
            "originalFileName.doesNotContain=" + DEFAULT_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByMediaTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where mediaType equals to
        defaultCandidateResumeFiltering("mediaType.equals=" + DEFAULT_MEDIA_TYPE, "mediaType.equals=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByMediaTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where mediaType in
        defaultCandidateResumeFiltering(
            "mediaType.in=" + DEFAULT_MEDIA_TYPE + "," + UPDATED_MEDIA_TYPE,
            "mediaType.in=" + UPDATED_MEDIA_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByMediaTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where mediaType is not null
        defaultCandidateResumeFiltering("mediaType.specified=true", "mediaType.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByMediaTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where mediaType contains
        defaultCandidateResumeFiltering("mediaType.contains=" + DEFAULT_MEDIA_TYPE, "mediaType.contains=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByMediaTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where mediaType does not contain
        defaultCandidateResumeFiltering("mediaType.doesNotContain=" + UPDATED_MEDIA_TYPE, "mediaType.doesNotContain=" + DEFAULT_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize equals to
        defaultCandidateResumeFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize in
        defaultCandidateResumeFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize is not null
        defaultCandidateResumeFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize is greater than or equal to
        defaultCandidateResumeFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize is less than or equal to
        defaultCandidateResumeFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize is less than
        defaultCandidateResumeFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where fileSize is greater than
        defaultCandidateResumeFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCandidateResumesBySha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where sha256 equals to
        defaultCandidateResumeFiltering("sha256.equals=" + DEFAULT_SHA_256, "sha256.equals=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllCandidateResumesBySha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where sha256 in
        defaultCandidateResumeFiltering("sha256.in=" + DEFAULT_SHA_256 + "," + UPDATED_SHA_256, "sha256.in=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllCandidateResumesBySha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where sha256 is not null
        defaultCandidateResumeFiltering("sha256.specified=true", "sha256.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesBySha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where sha256 contains
        defaultCandidateResumeFiltering("sha256.contains=" + DEFAULT_SHA_256, "sha256.contains=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllCandidateResumesBySha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where sha256 does not contain
        defaultCandidateResumeFiltering("sha256.doesNotContain=" + UPDATED_SHA_256, "sha256.doesNotContain=" + DEFAULT_SHA_256);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByStorageKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where storageKey equals to
        defaultCandidateResumeFiltering("storageKey.equals=" + DEFAULT_STORAGE_KEY, "storageKey.equals=" + UPDATED_STORAGE_KEY);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByStorageKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where storageKey in
        defaultCandidateResumeFiltering(
            "storageKey.in=" + DEFAULT_STORAGE_KEY + "," + UPDATED_STORAGE_KEY,
            "storageKey.in=" + UPDATED_STORAGE_KEY
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByStorageKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where storageKey is not null
        defaultCandidateResumeFiltering("storageKey.specified=true", "storageKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByStorageKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where storageKey contains
        defaultCandidateResumeFiltering("storageKey.contains=" + DEFAULT_STORAGE_KEY, "storageKey.contains=" + UPDATED_STORAGE_KEY);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByStorageKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where storageKey does not contain
        defaultCandidateResumeFiltering(
            "storageKey.doesNotContain=" + UPDATED_STORAGE_KEY,
            "storageKey.doesNotContain=" + DEFAULT_STORAGE_KEY
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByActiveVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where activeVersion equals to
        defaultCandidateResumeFiltering("activeVersion.equals=" + DEFAULT_ACTIVE_VERSION, "activeVersion.equals=" + UPDATED_ACTIVE_VERSION);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByActiveVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where activeVersion in
        defaultCandidateResumeFiltering(
            "activeVersion.in=" + DEFAULT_ACTIVE_VERSION + "," + UPDATED_ACTIVE_VERSION,
            "activeVersion.in=" + UPDATED_ACTIVE_VERSION
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByActiveVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where activeVersion is not null
        defaultCandidateResumeFiltering("activeVersion.specified=true", "activeVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByUploadedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where uploadedBy equals to
        defaultCandidateResumeFiltering("uploadedBy.equals=" + DEFAULT_UPLOADED_BY, "uploadedBy.equals=" + UPDATED_UPLOADED_BY);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByUploadedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where uploadedBy in
        defaultCandidateResumeFiltering(
            "uploadedBy.in=" + DEFAULT_UPLOADED_BY + "," + UPDATED_UPLOADED_BY,
            "uploadedBy.in=" + UPDATED_UPLOADED_BY
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByUploadedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where uploadedBy is not null
        defaultCandidateResumeFiltering("uploadedBy.specified=true", "uploadedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidateResumesByUploadedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where uploadedBy contains
        defaultCandidateResumeFiltering("uploadedBy.contains=" + DEFAULT_UPLOADED_BY, "uploadedBy.contains=" + UPDATED_UPLOADED_BY);
    }

    @Test
    @Transactional
    void getAllCandidateResumesByUploadedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        // Get all the candidateResumeList where uploadedBy does not contain
        defaultCandidateResumeFiltering(
            "uploadedBy.doesNotContain=" + UPDATED_UPLOADED_BY,
            "uploadedBy.doesNotContain=" + DEFAULT_UPLOADED_BY
        );
    }

    @Test
    @Transactional
    void getAllCandidateResumesByCandidateIsEqualToSomething() throws Exception {
        ExternalCandidate candidate;
        if (TestUtil.findAll(em, ExternalCandidate.class).isEmpty()) {
            candidateResumeRepository.saveAndFlush(candidateResume);
            candidate = ExternalCandidateResourceIT.createEntity();
        } else {
            candidate = TestUtil.findAll(em, ExternalCandidate.class).get(0);
        }
        em.persist(candidate);
        em.flush();
        candidateResume.setCandidate(candidate);
        candidateResumeRepository.saveAndFlush(candidateResume);
        Long candidateId = candidate.getId();
        // Get all the candidateResumeList where candidate equals to candidateId
        defaultCandidateResumeShouldBeFound("candidateId.equals=" + candidateId);

        // Get all the candidateResumeList where candidate equals to (candidateId + 1)
        defaultCandidateResumeShouldNotBeFound("candidateId.equals=" + (candidateId + 1));
    }

    private void defaultCandidateResumeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCandidateResumeShouldBeFound(shouldBeFound);
        defaultCandidateResumeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCandidateResumeShouldBeFound(String filter) throws Exception {
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateResume.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].storageKey").value(hasItem(DEFAULT_STORAGE_KEY)))
            .andExpect(jsonPath("$.[*].structuredSummary").value(hasItem(DEFAULT_STRUCTURED_SUMMARY)))
            .andExpect(jsonPath("$.[*].activeVersion").value(hasItem(DEFAULT_ACTIVE_VERSION)))
            .andExpect(jsonPath("$.[*].uploadedBy").value(hasItem(DEFAULT_UPLOADED_BY)));

        // Check, that the count call also returns 1
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCandidateResumeShouldNotBeFound(String filter) throws Exception {
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCandidateResumeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCandidateResume() throws Exception {
        // Get the candidateResume
        restCandidateResumeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCandidateResume() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateResume
        CandidateResume updatedCandidateResume = candidateResumeRepository.findById(candidateResume.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCandidateResume are not directly saved in db
        em.detach(updatedCandidateResume);
        updatedCandidateResume
            .versionNumber(UPDATED_VERSION_NUMBER)
            .receivedAt(UPDATED_RECEIVED_AT)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .mediaType(UPDATED_MEDIA_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .sha256(UPDATED_SHA_256)
            .storageKey(UPDATED_STORAGE_KEY)
            .structuredSummary(UPDATED_STRUCTURED_SUMMARY)
            .activeVersion(UPDATED_ACTIVE_VERSION)
            .uploadedBy(UPDATED_UPLOADED_BY);
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(updatedCandidateResume);

        restCandidateResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateResumeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateResumeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCandidateResumeToMatchAllProperties(updatedCandidateResume);
    }

    @Test
    @Transactional
    void putNonExistingCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateResumeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateResumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidateResumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidateResumeWithPatch() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateResume using partial update
        CandidateResume partialUpdatedCandidateResume = new CandidateResume();
        partialUpdatedCandidateResume.setId(candidateResume.getId());

        partialUpdatedCandidateResume
            .versionNumber(UPDATED_VERSION_NUMBER)
            .mediaType(UPDATED_MEDIA_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .storageKey(UPDATED_STORAGE_KEY)
            .activeVersion(UPDATED_ACTIVE_VERSION);

        restCandidateResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidateResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidateResume))
            )
            .andExpect(status().isOk());

        // Validate the CandidateResume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateResumeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCandidateResume, candidateResume),
            getPersistedCandidateResume(candidateResume)
        );
    }

    @Test
    @Transactional
    void fullUpdateCandidateResumeWithPatch() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidateResume using partial update
        CandidateResume partialUpdatedCandidateResume = new CandidateResume();
        partialUpdatedCandidateResume.setId(candidateResume.getId());

        partialUpdatedCandidateResume
            .versionNumber(UPDATED_VERSION_NUMBER)
            .receivedAt(UPDATED_RECEIVED_AT)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .mediaType(UPDATED_MEDIA_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .sha256(UPDATED_SHA_256)
            .storageKey(UPDATED_STORAGE_KEY)
            .structuredSummary(UPDATED_STRUCTURED_SUMMARY)
            .activeVersion(UPDATED_ACTIVE_VERSION)
            .uploadedBy(UPDATED_UPLOADED_BY);

        restCandidateResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidateResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidateResume))
            )
            .andExpect(status().isOk());

        // Validate the CandidateResume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateResumeUpdatableFieldsEquals(
            partialUpdatedCandidateResume,
            getPersistedCandidateResume(partialUpdatedCandidateResume)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidateResumeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidateResumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidateResumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidateResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidateResume.setId(longCount.incrementAndGet());

        // Create the CandidateResume
        CandidateResumeDTO candidateResumeDTO = candidateResumeMapper.toDto(candidateResume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateResumeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(candidateResumeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidateResume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidateResume() throws Exception {
        // Initialize the database
        insertedCandidateResume = candidateResumeRepository.saveAndFlush(candidateResume);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the candidateResume
        restCandidateResumeMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidateResume.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return candidateResumeRepository.count();
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

    protected CandidateResume getPersistedCandidateResume(CandidateResume candidateResume) {
        return candidateResumeRepository.findById(candidateResume.getId()).orElseThrow();
    }

    protected void assertPersistedCandidateResumeToMatchAllProperties(CandidateResume expectedCandidateResume) {
        assertCandidateResumeAllPropertiesEquals(expectedCandidateResume, getPersistedCandidateResume(expectedCandidateResume));
    }

    protected void assertPersistedCandidateResumeToMatchUpdatableProperties(CandidateResume expectedCandidateResume) {
        assertCandidateResumeAllUpdatablePropertiesEquals(expectedCandidateResume, getPersistedCandidateResume(expectedCandidateResume));
    }
}
