package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionMemberAsserts.*;
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
import top.btmdc.hr.domain.RecruitmentRequisitionMember;
import top.btmdc.hr.repository.RecruitmentRequisitionMemberRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionMemberDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMemberMapper;

/**
 * Integration tests for the {@link RecruitmentRequisitionMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class RecruitmentRequisitionMemberResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMBER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REMOVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REMOVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/recruitment-requisition-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository;

    @Autowired
    private RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentRequisitionMemberMockMvc;

    private RecruitmentRequisitionMember recruitmentRequisitionMember;

    private RecruitmentRequisitionMember insertedRecruitmentRequisitionMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionMember createEntity() {
        return new RecruitmentRequisitionMember()
            .username(DEFAULT_USERNAME)
            .memberType(DEFAULT_MEMBER_TYPE)
            .active(DEFAULT_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .removedAt(DEFAULT_REMOVED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionMember createUpdatedEntity() {
        return new RecruitmentRequisitionMember()
            .username(UPDATED_USERNAME)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .removedAt(UPDATED_REMOVED_AT);
    }

    @BeforeEach
    void initTest() {
        recruitmentRequisitionMember = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecruitmentRequisitionMember != null) {
            recruitmentRequisitionMemberRepository.delete(insertedRecruitmentRequisitionMember);
            insertedRecruitmentRequisitionMember = null;
        }
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );
        var returnedRecruitmentRequisitionMemberDTO = om.readValue(
            restRecruitmentRequisitionMemberMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecruitmentRequisitionMemberDTO.class
        );

        // Validate the RecruitmentRequisitionMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecruitmentRequisitionMember = recruitmentRequisitionMemberMapper.toEntity(returnedRecruitmentRequisitionMemberDTO);
        assertRecruitmentRequisitionMemberUpdatableFieldsEquals(
            returnedRecruitmentRequisitionMember,
            getPersistedRecruitmentRequisitionMember(returnedRecruitmentRequisitionMember)
        );

        insertedRecruitmentRequisitionMember = returnedRecruitmentRequisitionMember;
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionMemberWithExistingId() throws Exception {
        // Create the RecruitmentRequisitionMember with an existing ID
        recruitmentRequisitionMember.setId(1L);
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionMember.setUsername(null);

        // Create the RecruitmentRequisitionMember, which fails.
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionMember.setMemberType(null);

        // Create the RecruitmentRequisitionMember, which fails.
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionMember.setActive(null);

        // Create the RecruitmentRequisitionMember, which fails.
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruitmentRequisitionMember.setCreatedAt(null);

        // Create the RecruitmentRequisitionMember, which fails.
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembers() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionMember() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get the recruitmentRequisitionMember
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentRequisitionMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentRequisitionMember.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.memberType").value(DEFAULT_MEMBER_TYPE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        Long id = recruitmentRequisitionMember.getId();

        defaultRecruitmentRequisitionMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecruitmentRequisitionMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecruitmentRequisitionMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where username equals to
        defaultRecruitmentRequisitionMemberFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where username in
        defaultRecruitmentRequisitionMemberFiltering(
            "username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME,
            "username.in=" + UPDATED_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where username is not null
        defaultRecruitmentRequisitionMemberFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where username contains
        defaultRecruitmentRequisitionMemberFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where username does not contain
        defaultRecruitmentRequisitionMemberFiltering(
            "username.doesNotContain=" + UPDATED_USERNAME,
            "username.doesNotContain=" + DEFAULT_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByMemberTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where memberType equals to
        defaultRecruitmentRequisitionMemberFiltering(
            "memberType.equals=" + DEFAULT_MEMBER_TYPE,
            "memberType.equals=" + UPDATED_MEMBER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByMemberTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where memberType in
        defaultRecruitmentRequisitionMemberFiltering(
            "memberType.in=" + DEFAULT_MEMBER_TYPE + "," + UPDATED_MEMBER_TYPE,
            "memberType.in=" + UPDATED_MEMBER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByMemberTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where memberType is not null
        defaultRecruitmentRequisitionMemberFiltering("memberType.specified=true", "memberType.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByMemberTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where memberType contains
        defaultRecruitmentRequisitionMemberFiltering(
            "memberType.contains=" + DEFAULT_MEMBER_TYPE,
            "memberType.contains=" + UPDATED_MEMBER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByMemberTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where memberType does not contain
        defaultRecruitmentRequisitionMemberFiltering(
            "memberType.doesNotContain=" + UPDATED_MEMBER_TYPE,
            "memberType.doesNotContain=" + DEFAULT_MEMBER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where active equals to
        defaultRecruitmentRequisitionMemberFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where active in
        defaultRecruitmentRequisitionMemberFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where active is not null
        defaultRecruitmentRequisitionMemberFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where createdAt equals to
        defaultRecruitmentRequisitionMemberFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where createdAt in
        defaultRecruitmentRequisitionMemberFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where createdAt is not null
        defaultRecruitmentRequisitionMemberFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where removedAt equals to
        defaultRecruitmentRequisitionMemberFiltering("removedAt.equals=" + DEFAULT_REMOVED_AT, "removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where removedAt in
        defaultRecruitmentRequisitionMemberFiltering(
            "removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT,
            "removedAt.in=" + UPDATED_REMOVED_AT
        );
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        // Get all the recruitmentRequisitionMemberList where removedAt is not null
        defaultRecruitmentRequisitionMemberFiltering("removedAt.specified=true", "removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionMembersByRequisitionIsEqualToSomething() throws Exception {
        RecruitmentRequisition requisition;
        if (TestUtil.findAll(em, RecruitmentRequisition.class).isEmpty()) {
            recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);
            requisition = RecruitmentRequisitionResourceIT.createEntity();
        } else {
            requisition = TestUtil.findAll(em, RecruitmentRequisition.class).get(0);
        }
        em.persist(requisition);
        em.flush();
        recruitmentRequisitionMember.setRequisition(requisition);
        recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);
        Long requisitionId = requisition.getId();
        // Get all the recruitmentRequisitionMemberList where requisition equals to requisitionId
        defaultRecruitmentRequisitionMemberShouldBeFound("requisitionId.equals=" + requisitionId);

        // Get all the recruitmentRequisitionMemberList where requisition equals to (requisitionId + 1)
        defaultRecruitmentRequisitionMemberShouldNotBeFound("requisitionId.equals=" + (requisitionId + 1));
    }

    private void defaultRecruitmentRequisitionMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecruitmentRequisitionMemberShouldBeFound(shouldBeFound);
        defaultRecruitmentRequisitionMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecruitmentRequisitionMemberShouldBeFound(String filter) throws Exception {
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecruitmentRequisitionMemberShouldNotBeFound(String filter) throws Exception {
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecruitmentRequisitionMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentRequisitionMember() throws Exception {
        // Get the recruitmentRequisitionMember
        restRecruitmentRequisitionMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentRequisitionMember() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionMember
        RecruitmentRequisitionMember updatedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository
            .findById(recruitmentRequisitionMember.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecruitmentRequisitionMember are not directly saved in db
        em.detach(updatedRecruitmentRequisitionMember);
        updatedRecruitmentRequisitionMember
            .username(UPDATED_USERNAME)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .removedAt(UPDATED_REMOVED_AT);
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            updatedRecruitmentRequisitionMember
        );

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruitmentRequisitionMemberToMatchAllProperties(updatedRecruitmentRequisitionMember);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentRequisitionMemberWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionMember using partial update
        RecruitmentRequisitionMember partialUpdatedRecruitmentRequisitionMember = new RecruitmentRequisitionMember();
        partialUpdatedRecruitmentRequisitionMember.setId(recruitmentRequisitionMember.getId());

        partialUpdatedRecruitmentRequisitionMember.createdAt(UPDATED_CREATED_AT);

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisitionMember))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruitmentRequisitionMember, recruitmentRequisitionMember),
            getPersistedRecruitmentRequisitionMember(recruitmentRequisitionMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentRequisitionMemberWithPatch() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruitmentRequisitionMember using partial update
        RecruitmentRequisitionMember partialUpdatedRecruitmentRequisitionMember = new RecruitmentRequisitionMember();
        partialUpdatedRecruitmentRequisitionMember.setId(recruitmentRequisitionMember.getId());

        partialUpdatedRecruitmentRequisitionMember
            .username(UPDATED_USERNAME)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .removedAt(UPDATED_REMOVED_AT);

        restRecruitmentRequisitionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruitmentRequisitionMember))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruitmentRequisitionMemberUpdatableFieldsEquals(
            partialUpdatedRecruitmentRequisitionMember,
            getPersistedRecruitmentRequisitionMember(partialUpdatedRecruitmentRequisitionMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentRequisitionMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentRequisitionMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruitmentRequisitionMember.setId(longCount.incrementAndGet());

        // Create the RecruitmentRequisitionMember
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberMapper.toDto(
            recruitmentRequisitionMember
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruitmentRequisitionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentRequisitionMember() throws Exception {
        // Initialize the database
        insertedRecruitmentRequisitionMember = recruitmentRequisitionMemberRepository.saveAndFlush(recruitmentRequisitionMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruitmentRequisitionMember
        restRecruitmentRequisitionMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentRequisitionMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruitmentRequisitionMemberRepository.count();
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

    protected RecruitmentRequisitionMember getPersistedRecruitmentRequisitionMember(
        RecruitmentRequisitionMember recruitmentRequisitionMember
    ) {
        return recruitmentRequisitionMemberRepository.findById(recruitmentRequisitionMember.getId()).orElseThrow();
    }

    protected void assertPersistedRecruitmentRequisitionMemberToMatchAllProperties(
        RecruitmentRequisitionMember expectedRecruitmentRequisitionMember
    ) {
        assertRecruitmentRequisitionMemberAllPropertiesEquals(
            expectedRecruitmentRequisitionMember,
            getPersistedRecruitmentRequisitionMember(expectedRecruitmentRequisitionMember)
        );
    }

    protected void assertPersistedRecruitmentRequisitionMemberToMatchUpdatableProperties(
        RecruitmentRequisitionMember expectedRecruitmentRequisitionMember
    ) {
        assertRecruitmentRequisitionMemberAllUpdatablePropertiesEquals(
            expectedRecruitmentRequisitionMember,
            getPersistedRecruitmentRequisitionMember(expectedRecruitmentRequisitionMember)
        );
    }
}
