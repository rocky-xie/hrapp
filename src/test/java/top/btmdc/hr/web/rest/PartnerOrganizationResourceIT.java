package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PartnerOrganizationAsserts.*;
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
import top.btmdc.hr.domain.PartnerOrganization;
import top.btmdc.hr.repository.PartnerOrganizationRepository;
import top.btmdc.hr.service.dto.PartnerOrganizationDTO;
import top.btmdc.hr.service.mapper.PartnerOrganizationMapper;

/**
 * Integration tests for the {@link PartnerOrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class PartnerOrganizationResourceIT {

    private static final String DEFAULT_PARTNER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARTNER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLES_JSON = "AAAAAAAAAA";
    private static final String UPDATED_ROLES_JSON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISPATCH_ENABLED = false;
    private static final Boolean UPDATED_DISPATCH_ENABLED = true;

    private static final Integer DEFAULT_DEFAULT_OBSERVATION_DAYS = 0;
    private static final Integer UPDATED_DEFAULT_OBSERVATION_DAYS = 1;
    private static final Integer SMALLER_DEFAULT_OBSERVATION_DAYS = 0 - 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/partner-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartnerOrganizationRepository partnerOrganizationRepository;

    @Autowired
    private PartnerOrganizationMapper partnerOrganizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerOrganizationMockMvc;

    private PartnerOrganization partnerOrganization;

    private PartnerOrganization insertedPartnerOrganization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerOrganization createEntity() {
        return new PartnerOrganization()
            .partnerCode(DEFAULT_PARTNER_CODE)
            .name(DEFAULT_NAME)
            .rolesJson(DEFAULT_ROLES_JSON)
            .dispatchEnabled(DEFAULT_DISPATCH_ENABLED)
            .defaultObservationDays(DEFAULT_DEFAULT_OBSERVATION_DAYS)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerOrganization createUpdatedEntity() {
        return new PartnerOrganization()
            .partnerCode(UPDATED_PARTNER_CODE)
            .name(UPDATED_NAME)
            .rolesJson(UPDATED_ROLES_JSON)
            .dispatchEnabled(UPDATED_DISPATCH_ENABLED)
            .defaultObservationDays(UPDATED_DEFAULT_OBSERVATION_DAYS)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        partnerOrganization = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartnerOrganization != null) {
            partnerOrganizationRepository.delete(insertedPartnerOrganization);
            insertedPartnerOrganization = null;
        }
    }

    @Test
    @Transactional
    void createPartnerOrganization() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);
        var returnedPartnerOrganizationDTO = om.readValue(
            restPartnerOrganizationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartnerOrganizationDTO.class
        );

        // Validate the PartnerOrganization in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPartnerOrganization = partnerOrganizationMapper.toEntity(returnedPartnerOrganizationDTO);
        assertPartnerOrganizationUpdatableFieldsEquals(
            returnedPartnerOrganization,
            getPersistedPartnerOrganization(returnedPartnerOrganization)
        );

        insertedPartnerOrganization = returnedPartnerOrganization;
    }

    @Test
    @Transactional
    void createPartnerOrganizationWithExistingId() throws Exception {
        // Create the PartnerOrganization with an existing ID
        partnerOrganization.setId(1L);
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPartnerCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partnerOrganization.setPartnerCode(null);

        // Create the PartnerOrganization, which fails.
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        restPartnerOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partnerOrganization.setName(null);

        // Create the PartnerOrganization, which fails.
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        restPartnerOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDispatchEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partnerOrganization.setDispatchEnabled(null);

        // Create the PartnerOrganization, which fails.
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        restPartnerOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partnerOrganization.setActive(null);

        // Create the PartnerOrganization, which fails.
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        restPartnerOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizations() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].partnerCode").value(hasItem(DEFAULT_PARTNER_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rolesJson").value(hasItem(DEFAULT_ROLES_JSON)))
            .andExpect(jsonPath("$.[*].dispatchEnabled").value(hasItem(DEFAULT_DISPATCH_ENABLED)))
            .andExpect(jsonPath("$.[*].defaultObservationDays").value(hasItem(DEFAULT_DEFAULT_OBSERVATION_DAYS)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getPartnerOrganization() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get the partnerOrganization
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, partnerOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerOrganization.getId().intValue()))
            .andExpect(jsonPath("$.partnerCode").value(DEFAULT_PARTNER_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rolesJson").value(DEFAULT_ROLES_JSON))
            .andExpect(jsonPath("$.dispatchEnabled").value(DEFAULT_DISPATCH_ENABLED))
            .andExpect(jsonPath("$.defaultObservationDays").value(DEFAULT_DEFAULT_OBSERVATION_DAYS))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getPartnerOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        Long id = partnerOrganization.getId();

        defaultPartnerOrganizationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartnerOrganizationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartnerOrganizationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByPartnerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where partnerCode equals to
        defaultPartnerOrganizationFiltering("partnerCode.equals=" + DEFAULT_PARTNER_CODE, "partnerCode.equals=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByPartnerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where partnerCode in
        defaultPartnerOrganizationFiltering(
            "partnerCode.in=" + DEFAULT_PARTNER_CODE + "," + UPDATED_PARTNER_CODE,
            "partnerCode.in=" + UPDATED_PARTNER_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByPartnerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where partnerCode is not null
        defaultPartnerOrganizationFiltering("partnerCode.specified=true", "partnerCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByPartnerCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where partnerCode contains
        defaultPartnerOrganizationFiltering("partnerCode.contains=" + DEFAULT_PARTNER_CODE, "partnerCode.contains=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByPartnerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where partnerCode does not contain
        defaultPartnerOrganizationFiltering(
            "partnerCode.doesNotContain=" + UPDATED_PARTNER_CODE,
            "partnerCode.doesNotContain=" + DEFAULT_PARTNER_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where name equals to
        defaultPartnerOrganizationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where name in
        defaultPartnerOrganizationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where name is not null
        defaultPartnerOrganizationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where name contains
        defaultPartnerOrganizationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where name does not contain
        defaultPartnerOrganizationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDispatchEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where dispatchEnabled equals to
        defaultPartnerOrganizationFiltering(
            "dispatchEnabled.equals=" + DEFAULT_DISPATCH_ENABLED,
            "dispatchEnabled.equals=" + UPDATED_DISPATCH_ENABLED
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDispatchEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where dispatchEnabled in
        defaultPartnerOrganizationFiltering(
            "dispatchEnabled.in=" + DEFAULT_DISPATCH_ENABLED + "," + UPDATED_DISPATCH_ENABLED,
            "dispatchEnabled.in=" + UPDATED_DISPATCH_ENABLED
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDispatchEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where dispatchEnabled is not null
        defaultPartnerOrganizationFiltering("dispatchEnabled.specified=true", "dispatchEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays equals to
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.equals=" + DEFAULT_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.equals=" + UPDATED_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays in
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.in=" + DEFAULT_DEFAULT_OBSERVATION_DAYS + "," + UPDATED_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.in=" + UPDATED_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays is not null
        defaultPartnerOrganizationFiltering("defaultObservationDays.specified=true", "defaultObservationDays.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays is greater than or equal to
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.greaterThanOrEqual=" + DEFAULT_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.greaterThanOrEqual=" + UPDATED_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays is less than or equal to
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.lessThanOrEqual=" + DEFAULT_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.lessThanOrEqual=" + SMALLER_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays is less than
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.lessThan=" + UPDATED_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.lessThan=" + DEFAULT_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByDefaultObservationDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where defaultObservationDays is greater than
        defaultPartnerOrganizationFiltering(
            "defaultObservationDays.greaterThan=" + SMALLER_DEFAULT_OBSERVATION_DAYS,
            "defaultObservationDays.greaterThan=" + DEFAULT_DEFAULT_OBSERVATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where active equals to
        defaultPartnerOrganizationFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where active in
        defaultPartnerOrganizationFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPartnerOrganizationsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        // Get all the partnerOrganizationList where active is not null
        defaultPartnerOrganizationFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultPartnerOrganizationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartnerOrganizationShouldBeFound(shouldBeFound);
        defaultPartnerOrganizationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartnerOrganizationShouldBeFound(String filter) throws Exception {
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].partnerCode").value(hasItem(DEFAULT_PARTNER_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rolesJson").value(hasItem(DEFAULT_ROLES_JSON)))
            .andExpect(jsonPath("$.[*].dispatchEnabled").value(hasItem(DEFAULT_DISPATCH_ENABLED)))
            .andExpect(jsonPath("$.[*].defaultObservationDays").value(hasItem(DEFAULT_DEFAULT_OBSERVATION_DAYS)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartnerOrganizationShouldNotBeFound(String filter) throws Exception {
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartnerOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartnerOrganization() throws Exception {
        // Get the partnerOrganization
        restPartnerOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartnerOrganization() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerOrganization
        PartnerOrganization updatedPartnerOrganization = partnerOrganizationRepository.findById(partnerOrganization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartnerOrganization are not directly saved in db
        em.detach(updatedPartnerOrganization);
        updatedPartnerOrganization
            .partnerCode(UPDATED_PARTNER_CODE)
            .name(UPDATED_NAME)
            .rolesJson(UPDATED_ROLES_JSON)
            .dispatchEnabled(UPDATED_DISPATCH_ENABLED)
            .defaultObservationDays(UPDATED_DEFAULT_OBSERVATION_DAYS)
            .active(UPDATED_ACTIVE);
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(updatedPartnerOrganization);

        restPartnerOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartnerOrganizationToMatchAllProperties(updatedPartnerOrganization);
    }

    @Test
    @Transactional
    void putNonExistingPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerOrganizationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartnerOrganizationWithPatch() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerOrganization using partial update
        PartnerOrganization partialUpdatedPartnerOrganization = new PartnerOrganization();
        partialUpdatedPartnerOrganization.setId(partnerOrganization.getId());

        partialUpdatedPartnerOrganization.name(UPDATED_NAME).active(UPDATED_ACTIVE);

        restPartnerOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartnerOrganization))
            )
            .andExpect(status().isOk());

        // Validate the PartnerOrganization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerOrganizationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartnerOrganization, partnerOrganization),
            getPersistedPartnerOrganization(partnerOrganization)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartnerOrganizationWithPatch() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerOrganization using partial update
        PartnerOrganization partialUpdatedPartnerOrganization = new PartnerOrganization();
        partialUpdatedPartnerOrganization.setId(partnerOrganization.getId());

        partialUpdatedPartnerOrganization
            .partnerCode(UPDATED_PARTNER_CODE)
            .name(UPDATED_NAME)
            .rolesJson(UPDATED_ROLES_JSON)
            .dispatchEnabled(UPDATED_DISPATCH_ENABLED)
            .defaultObservationDays(UPDATED_DEFAULT_OBSERVATION_DAYS)
            .active(UPDATED_ACTIVE);

        restPartnerOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartnerOrganization))
            )
            .andExpect(status().isOk());

        // Validate the PartnerOrganization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerOrganizationUpdatableFieldsEquals(
            partialUpdatedPartnerOrganization,
            getPersistedPartnerOrganization(partialUpdatedPartnerOrganization)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partnerOrganizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartnerOrganization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerOrganization.setId(longCount.incrementAndGet());

        // Create the PartnerOrganization
        PartnerOrganizationDTO partnerOrganizationDTO = partnerOrganizationMapper.toDto(partnerOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partnerOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerOrganization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartnerOrganization() throws Exception {
        // Initialize the database
        insertedPartnerOrganization = partnerOrganizationRepository.saveAndFlush(partnerOrganization);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partnerOrganization
        restPartnerOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, partnerOrganization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partnerOrganizationRepository.count();
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

    protected PartnerOrganization getPersistedPartnerOrganization(PartnerOrganization partnerOrganization) {
        return partnerOrganizationRepository.findById(partnerOrganization.getId()).orElseThrow();
    }

    protected void assertPersistedPartnerOrganizationToMatchAllProperties(PartnerOrganization expectedPartnerOrganization) {
        assertPartnerOrganizationAllPropertiesEquals(
            expectedPartnerOrganization,
            getPersistedPartnerOrganization(expectedPartnerOrganization)
        );
    }

    protected void assertPersistedPartnerOrganizationToMatchUpdatableProperties(PartnerOrganization expectedPartnerOrganization) {
        assertPartnerOrganizationAllUpdatablePropertiesEquals(
            expectedPartnerOrganization,
            getPersistedPartnerOrganization(expectedPartnerOrganization)
        );
    }
}
