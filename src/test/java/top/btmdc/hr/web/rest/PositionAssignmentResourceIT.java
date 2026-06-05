package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionAssignmentAsserts.*;
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
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.service.PositionAssignmentService;
import top.btmdc.hr.service.dto.PositionAssignmentDTO;
import top.btmdc.hr.service.mapper.PositionAssignmentMapper;

/**
 * Integration tests for the {@link PositionAssignmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PositionAssignmentResourceIT {

    private static final Boolean DEFAULT_PRIMARY_OWNER = false;
    private static final Boolean UPDATED_PRIMARY_OWNER = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RESPONSIBILITY_SCOPE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSIBILITY_SCOPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/position-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionAssignmentRepository positionAssignmentRepository;

    @Mock
    private PositionAssignmentRepository positionAssignmentRepositoryMock;

    @Autowired
    private PositionAssignmentMapper positionAssignmentMapper;

    @Mock
    private PositionAssignmentService positionAssignmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionAssignmentMockMvc;

    private PositionAssignment positionAssignment;

    private PositionAssignment insertedPositionAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionAssignment createEntity(EntityManager em) {
        PositionAssignment positionAssignment = new PositionAssignment()
            .primaryOwner(DEFAULT_PRIMARY_OWNER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .responsibilityScope(DEFAULT_RESPONSIBILITY_SCOPE)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        positionAssignment.setPerson(person);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        positionAssignment.setPosition(position);
        return positionAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionAssignment createUpdatedEntity(EntityManager em) {
        PositionAssignment updatedPositionAssignment = new PositionAssignment()
            .primaryOwner(UPDATED_PRIMARY_OWNER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .responsibilityScope(UPDATED_RESPONSIBILITY_SCOPE)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedPositionAssignment.setPerson(person);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedPositionAssignment.setPosition(position);
        return updatedPositionAssignment;
    }

    @BeforeEach
    void initTest() {
        positionAssignment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPositionAssignment != null) {
            positionAssignmentRepository.delete(insertedPositionAssignment);
            insertedPositionAssignment = null;
        }
    }

    @Test
    @Transactional
    void createPositionAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);
        var returnedPositionAssignmentDTO = om.readValue(
            restPositionAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionAssignmentDTO.class
        );

        // Validate the PositionAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPositionAssignment = positionAssignmentMapper.toEntity(returnedPositionAssignmentDTO);
        assertPositionAssignmentUpdatableFieldsEquals(
            returnedPositionAssignment,
            getPersistedPositionAssignment(returnedPositionAssignment)
        );

        insertedPositionAssignment = returnedPositionAssignment;
    }

    @Test
    @Transactional
    void createPositionAssignmentWithExistingId() throws Exception {
        // Create the PositionAssignment with an existing ID
        positionAssignment.setId(1L);
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrimaryOwnerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionAssignment.setPrimaryOwner(null);

        // Create the PositionAssignment, which fails.
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        restPositionAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionAssignment.setActive(null);

        // Create the PositionAssignment, which fails.
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        restPositionAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionAssignments() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].primaryOwner").value(hasItem(DEFAULT_PRIMARY_OWNER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].responsibilityScope").value(hasItem(DEFAULT_RESPONSIBILITY_SCOPE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionAssignmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(positionAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(positionAssignmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionAssignmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(positionAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(positionAssignmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPositionAssignment() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get the positionAssignment
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, positionAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionAssignment.getId().intValue()))
            .andExpect(jsonPath("$.primaryOwner").value(DEFAULT_PRIMARY_OWNER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.responsibilityScope").value(DEFAULT_RESPONSIBILITY_SCOPE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getPositionAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        Long id = positionAssignment.getId();

        defaultPositionAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByPrimaryOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where primaryOwner equals to
        defaultPositionAssignmentFiltering("primaryOwner.equals=" + DEFAULT_PRIMARY_OWNER, "primaryOwner.equals=" + UPDATED_PRIMARY_OWNER);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByPrimaryOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where primaryOwner in
        defaultPositionAssignmentFiltering(
            "primaryOwner.in=" + DEFAULT_PRIMARY_OWNER + "," + UPDATED_PRIMARY_OWNER,
            "primaryOwner.in=" + UPDATED_PRIMARY_OWNER
        );
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByPrimaryOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where primaryOwner is not null
        defaultPositionAssignmentFiltering("primaryOwner.specified=true", "primaryOwner.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate equals to
        defaultPositionAssignmentFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate in
        defaultPositionAssignmentFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate is not null
        defaultPositionAssignmentFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate is greater than or equal to
        defaultPositionAssignmentFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate is less than or equal to
        defaultPositionAssignmentFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate is less than
        defaultPositionAssignmentFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where startDate is greater than
        defaultPositionAssignmentFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate equals to
        defaultPositionAssignmentFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate in
        defaultPositionAssignmentFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate is not null
        defaultPositionAssignmentFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate is greater than or equal to
        defaultPositionAssignmentFiltering(
            "endDate.greaterThanOrEqual=" + DEFAULT_END_DATE,
            "endDate.greaterThanOrEqual=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate is less than or equal to
        defaultPositionAssignmentFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate is less than
        defaultPositionAssignmentFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where endDate is greater than
        defaultPositionAssignmentFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where active equals to
        defaultPositionAssignmentFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where active in
        defaultPositionAssignmentFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        // Get all the positionAssignmentList where active is not null
        defaultPositionAssignmentFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            positionAssignmentRepository.saveAndFlush(positionAssignment);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        positionAssignment.setPerson(person);
        positionAssignmentRepository.saveAndFlush(positionAssignment);
        Long personId = person.getId();
        // Get all the positionAssignmentList where person equals to personId
        defaultPositionAssignmentShouldBeFound("personId.equals=" + personId);

        // Get all the positionAssignmentList where person equals to (personId + 1)
        defaultPositionAssignmentShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllPositionAssignmentsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            positionAssignmentRepository.saveAndFlush(positionAssignment);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        positionAssignment.setPosition(position);
        positionAssignmentRepository.saveAndFlush(positionAssignment);
        Long positionId = position.getId();
        // Get all the positionAssignmentList where position equals to positionId
        defaultPositionAssignmentShouldBeFound("positionId.equals=" + positionId);

        // Get all the positionAssignmentList where position equals to (positionId + 1)
        defaultPositionAssignmentShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    private void defaultPositionAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionAssignmentShouldBeFound(shouldBeFound);
        defaultPositionAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionAssignmentShouldBeFound(String filter) throws Exception {
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].primaryOwner").value(hasItem(DEFAULT_PRIMARY_OWNER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].responsibilityScope").value(hasItem(DEFAULT_RESPONSIBILITY_SCOPE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionAssignmentShouldNotBeFound(String filter) throws Exception {
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPositionAssignment() throws Exception {
        // Get the positionAssignment
        restPositionAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPositionAssignment() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionAssignment
        PositionAssignment updatedPositionAssignment = positionAssignmentRepository.findById(positionAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPositionAssignment are not directly saved in db
        em.detach(updatedPositionAssignment);
        updatedPositionAssignment
            .primaryOwner(UPDATED_PRIMARY_OWNER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .responsibilityScope(UPDATED_RESPONSIBILITY_SCOPE)
            .active(UPDATED_ACTIVE);
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(updatedPositionAssignment);

        restPositionAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionAssignmentToMatchAllProperties(updatedPositionAssignment);
    }

    @Test
    @Transactional
    void putNonExistingPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionAssignment using partial update
        PositionAssignment partialUpdatedPositionAssignment = new PositionAssignment();
        partialUpdatedPositionAssignment.setId(positionAssignment.getId());

        partialUpdatedPositionAssignment.responsibilityScope(UPDATED_RESPONSIBILITY_SCOPE).active(UPDATED_ACTIVE);

        restPositionAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionAssignment))
            )
            .andExpect(status().isOk());

        // Validate the PositionAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPositionAssignment, positionAssignment),
            getPersistedPositionAssignment(positionAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdatePositionAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionAssignment using partial update
        PositionAssignment partialUpdatedPositionAssignment = new PositionAssignment();
        partialUpdatedPositionAssignment.setId(positionAssignment.getId());

        partialUpdatedPositionAssignment
            .primaryOwner(UPDATED_PRIMARY_OWNER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .responsibilityScope(UPDATED_RESPONSIBILITY_SCOPE)
            .active(UPDATED_ACTIVE);

        restPositionAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionAssignment))
            )
            .andExpect(status().isOk());

        // Validate the PositionAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionAssignmentUpdatableFieldsEquals(
            partialUpdatedPositionAssignment,
            getPersistedPositionAssignment(partialUpdatedPositionAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionAssignment.setId(longCount.incrementAndGet());

        // Create the PositionAssignment
        PositionAssignmentDTO positionAssignmentDTO = positionAssignmentMapper.toDto(positionAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionAssignment() throws Exception {
        // Initialize the database
        insertedPositionAssignment = positionAssignmentRepository.saveAndFlush(positionAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the positionAssignment
        restPositionAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionAssignmentRepository.count();
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

    protected PositionAssignment getPersistedPositionAssignment(PositionAssignment positionAssignment) {
        return positionAssignmentRepository.findById(positionAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedPositionAssignmentToMatchAllProperties(PositionAssignment expectedPositionAssignment) {
        assertPositionAssignmentAllPropertiesEquals(expectedPositionAssignment, getPersistedPositionAssignment(expectedPositionAssignment));
    }

    protected void assertPersistedPositionAssignmentToMatchUpdatableProperties(PositionAssignment expectedPositionAssignment) {
        assertPositionAssignmentAllUpdatablePropertiesEquals(
            expectedPositionAssignment,
            getPersistedPositionAssignment(expectedPositionAssignment)
        );
    }
}
