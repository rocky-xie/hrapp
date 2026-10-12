package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionVersionAsserts.*;
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
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionVersionMapper;

/**
 * Integration tests for the {@link RecruitmentRequisitionVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class RecruitmentRequisitionVersionResourceIT {

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;
    private static final Integer SMALLER_VERSION_NUMBER = 1 - 1;

    private static final String DEFAULT_SNAPSHOT_JSON = "AAAAAAAAAA";
    private static final String UPDATED_SNAPSHOT_JSON = "BBBBBBBBBB";

    private static final Instant DEFAULT_LOCKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOCKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCKED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LOCKED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recruitment-requisition-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository;

    @Autowired
    private RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentRequisitionVersionMockMvc;

    private RecruitmentRequisitionVersion recruitmentRequisitionVersion;

    private RecruitmentRequisitionVersion insertedRecruitmentRequisitionVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionVersion createEntity() {
        return new RecruitmentRequisitionVersion()
            .versionNumber(DEFAULT_VERSION_NUMBER)
            .snapshotJson(DEFAULT_SNAPSHOT_JSON)
            .lockedAt(DEFAULT_LOCKED_AT)
            .lockedBy(DEFAULT_LOCKED_BY)
            .changeReason(DEFAULT_CHANGE_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionVersion createUpdatedEntity() {
        return new RecruitmentRequisitionVersion()
            .versionNumber(UPDATED_VERSION_NUMBER)
            .snapshotJson(UPDATED_SNAPSHOT_JSON)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .changeReason(UPDATED_CHANGE_REASON);
    }

    @BeforeEach
    void initTest() {
        recruitmentRequisitionVersion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecruitmentRequisitionVersion != null) {
            recruitmentRequisitionVersionRepository.delete(insertedRecruitmentRequisitionVersion);
            insertedRecruitmentRequisitionVersion = null;
        }
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );
        var returnedRecruitmentRequisitionVersionDTO = om.readValue(
            restRecruitmentRequisitionVersionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecruitmentRequisitionVersionDTO.class
        );

        // Validate the RecruitmentRequisitionVersion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecruitmentRequisitionVersion = recruitmentRequisitionVersionMapper.toEntity(returnedRecruitmentRequisitionVersionDTO);
        assertRecruitmentRequisitionVersionUpdatableFieldsEquals(
            returnedRecruitmentRequisitionVersion,
            getPersistedRecruitmentRequisitionVersion(returnedRecruitmentRequisitionVersion)
        );

        insertedRecruitmentRequisitionVersion = returnedRecruitmentRequisitionVersion;
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionVersionWithExistingId() throws Exception {
        // Create the RecruitmentRequisitionVersion with an existing ID
        recruitmentRequisitionVersion.setId(1L);
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVersionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionVersion.setVersionNumber(null);

        // Create the RecruitmentRequisitionVersion, which fails.
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLockedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionVersion.setLockedAt(null);

        // Create the RecruitmentRequisitionVersion, which fails.
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLockedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionVersion.setLockedBy(null);

        // Create the RecruitmentRequisitionVersion, which fails.
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersions() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].snapshotJson").value(hasItem(DEFAULT_SNAPSHOT_JSON)))
            .andExpect(jsonPath("$.[*].lockedAt").value(hasItem(DEFAULT_LOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].lockedBy").value(hasItem(DEFAULT_LOCKED_BY)))
            .andExpect(jsonPath("$.[*].changeReason").value(hasItem(DEFAULT_CHANGE_REASON)));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionVersion() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get the recruitmentRequisitionVersion
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentRequisitionVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentRequisitionVersion.getId().intValue()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.snapshotJson").value(DEFAULT_SNAPSHOT_JSON))
            .andExpect(jsonPath("$.lockedAt").value(DEFAULT_LOCKED_AT.toString()))
            .andExpect(jsonPath("$.lockedBy").value(DEFAULT_LOCKED_BY))
            .andExpect(jsonPath("$.changeReason").value(DEFAULT_CHANGE_REASON));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionVersionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        Long id = recruitmentRequisitionVersion.getId();

        defaultRecruitmentRequisitionVersionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecruitmentRequisitionVersionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecruitmentRequisitionVersionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber equals to
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.equals=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.equals=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber in
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.in=" + DEFAULT_VERSION_NUMBER + "," + UPDATED_VERSION_NUMBER,
            "versionNumber.in=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber is not null
        defaultRecruitmentRequisitionVersionFiltering("versionNumber.specified=true", "versionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber is greater than or equal to
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.greaterThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.greaterThanOrEqual=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber is less than or equal to
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.lessThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.lessThanOrEqual=" + SMALLER_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber is less than
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.lessThan=" + UPDATED_VERSION_NUMBER,
            "versionNumber.lessThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByVersionNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where versionNumber is greater than
        defaultRecruitmentRequisitionVersionFiltering(
            "versionNumber.greaterThan=" + SMALLER_VERSION_NUMBER,
            "versionNumber.greaterThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedAt equals to
        defaultRecruitmentRequisitionVersionFiltering("lockedAt.equals=" + DEFAULT_LOCKED_AT, "lockedAt.equals=" + UPDATED_LOCKED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedAt in
        defaultRecruitmentRequisitionVersionFiltering(
            "lockedAt.in=" + DEFAULT_LOCKED_AT + "," + UPDATED_LOCKED_AT,
            "lockedAt.in=" + UPDATED_LOCKED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedAt is not null
        defaultRecruitmentRequisitionVersionFiltering("lockedAt.specified=true", "lockedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedBy equals to
        defaultRecruitmentRequisitionVersionFiltering("lockedBy.equals=" + DEFAULT_LOCKED_BY, "lockedBy.equals=" + UPDATED_LOCKED_BY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedBy in
        defaultRecruitmentRequisitionVersionFiltering(
            "lockedBy.in=" + DEFAULT_LOCKED_BY + "," + UPDATED_LOCKED_BY,
            "lockedBy.in=" + UPDATED_LOCKED_BY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedBy is not null
        defaultRecruitmentRequisitionVersionFiltering("lockedBy.specified=true", "lockedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedByContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedBy contains
        defaultRecruitmentRequisitionVersionFiltering("lockedBy.contains=" + DEFAULT_LOCKED_BY, "lockedBy.contains=" + UPDATED_LOCKED_BY);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByLockedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        // Get all the recruitmentRequisitionVersionList where lockedBy does not contain
        defaultRecruitmentRequisitionVersionFiltering(
            "lockedBy.doesNotContain=" + UPDATED_LOCKED_BY,
            "lockedBy.doesNotContain=" + DEFAULT_LOCKED_BY
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionVersionsByRequisitionIsEqualToSomething() throws Exception {
        RecruitmentRequisition requisition;
        if (TestUtil.findAll(em, RecruitmentRequisition.class).isEmpty()) {
            recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);
            requisition = RecruitmentRequisitionResourceIT.createEntity();
        } else {
            requisition = TestUtil.findAll(em, RecruitmentRequisition.class).get(0);
        }
        em.persist(requisition);
        em.flush();
        recruitmentRequisitionVersion.setRequisition(requisition);
        recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);
        Long requisitionId = requisition.getId();
        // Get all the recruitmentRequisitionVersionList where requisition equals to requisitionId
        defaultRecruitmentRequisitionVersionShouldBeFound("requisitionId.equals=" + requisitionId);

        // Get all the recruitmentRequisitionVersionList where requisition equals to (requisitionId + 1)
        defaultRecruitmentRequisitionVersionShouldNotBeFound("requisitionId.equals=" + (requisitionId + 1));
    }

    private void defaultRecruitmentRequisitionVersionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecruitmentRequisitionVersionShouldBeFound(shouldBeFound);
        defaultRecruitmentRequisitionVersionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecruitmentRequisitionVersionShouldBeFound(String filter) throws Exception {
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].snapshotJson").value(hasItem(DEFAULT_SNAPSHOT_JSON)))
            .andExpect(jsonPath("$.[*].lockedAt").value(hasItem(DEFAULT_LOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].lockedBy").value(hasItem(DEFAULT_LOCKED_BY)))
            .andExpect(jsonPath("$.[*].changeReason").value(hasItem(DEFAULT_CHANGE_REASON)));

        // Check, that the count call also returns 1
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecruitmentRequisitionVersionShouldNotBeFound(String filter) throws Exception {
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecruitmentRequisitionVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentRequisitionVersion() throws Exception {
        // Get the recruitmentRequisitionVersion
        restRecruitmentRequisitionVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentRequisitionVersion() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionVersion
        RecruitmentRequisitionVersion updatedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository
            .findById(recruitmentRequisitionVersion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecruitmentRequisitionVersion are not directly saved in db
        em.detach(updatedRecruitmentRequisitionVersion);
        updatedRecruitmentRequisitionVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .snapshotJson(UPDATED_SNAPSHOT_JSON)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .changeReason(UPDATED_CHANGE_REASON);
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            updatedRecruitmentRequisitionVersion
        );

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruitmentRequisitionVersionToMatchAllProperties(updatedRecruitmentRequisitionVersion);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentRequisitionVersionWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionVersion using partial update
        RecruitmentRequisitionVersion partialUpdatedRecruitmentRequisitionVersion = new RecruitmentRequisitionVersion();
        partialUpdatedRecruitmentRequisitionVersion.setId(recruitmentRequisitionVersion.getId());

        partialUpdatedRecruitmentRequisitionVersion.versionNumber(UPDATED_VERSION_NUMBER);

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisitionVersion))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionVersionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruitmentRequisitionVersion, recruitmentRequisitionVersion),
            getPersistedRecruitmentRequisitionVersion(recruitmentRequisitionVersion)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentRequisitionVersionWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionVersion using partial update
        RecruitmentRequisitionVersion partialUpdatedRecruitmentRequisitionVersion = new RecruitmentRequisitionVersion();
        partialUpdatedRecruitmentRequisitionVersion.setId(recruitmentRequisitionVersion.getId());

        partialUpdatedRecruitmentRequisitionVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .snapshotJson(UPDATED_SNAPSHOT_JSON)
            .lockedAt(UPDATED_LOCKED_AT)
            .lockedBy(UPDATED_LOCKED_BY)
            .changeReason(UPDATED_CHANGE_REASON);

        restRecruitmentRequisitionVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisitionVersion))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionVersionUpdatableFieldsEquals(
            partialUpdatedRecruitmentRequisitionVersion,
            getPersistedRecruitmentRequisitionVersion(partialUpdatedRecruitmentRequisitionVersion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentRequisitionVersionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentRequisitionVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionVersion.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionVersion
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionMapper.toDto(
            recruitmentRequisitionVersion
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionVersionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionVersionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentRequisitionVersion() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.saveAndFlush(recruitmentRequisitionVersion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruitmentRequisitionVersion
        restRecruitmentRequisitionVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentRequisitionVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruitmentRequisitionVersionRepository.count();
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

    protected RecruitmentRequisitionVersion getPersistedRecruitmentRequisitionVersion(
        RecruitmentRequisitionVersion recruitmentRequisitionVersion
    ) {
        return recruitmentRequisitionVersionRepository.findById(recruitmentRequisitionVersion.getId()).orElseThrow();
    }

    protected void assertPersistedRecruitmentRequisitionVersionToMatchAllProperties(
        RecruitmentRequisitionVersion expectedRecruitmentRequisitionVersion
    ) {
        assertRecruitmentRequisitionVersionAllPropertiesEquals(
            expectedRecruitmentRequisitionVersion,
            getPersistedRecruitmentRequisitionVersion(expectedRecruitmentRequisitionVersion)
        );
    }

    protected void assertPersistedRecruitmentRequisitionVersionToMatchUpdatableProperties(
        RecruitmentRequisitionVersion expectedRecruitmentRequisitionVersion
    ) {
        assertRecruitmentRequisitionVersionAllUpdatablePropertiesEquals(
            expectedRecruitmentRequisitionVersion,
            getPersistedRecruitmentRequisitionVersion(expectedRecruitmentRequisitionVersion)
        );
    }
}
