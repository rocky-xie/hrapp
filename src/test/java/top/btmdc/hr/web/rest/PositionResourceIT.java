package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionAsserts.*;
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
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.PositionType;
import top.btmdc.hr.domain.enumeration.ReviewCycle;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.mapper.PositionMapper;

/**
 * Integration tests for the {@link PositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PositionResourceIT {

    private static final String DEFAULT_POSITION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_NAME = "BBBBBBBBBB";

    private static final PositionType DEFAULT_POSITION_TYPE = PositionType.TECHNICAL;
    private static final PositionType UPDATED_POSITION_TYPE = PositionType.BUSINESS_SUPPORT;

    private static final ImportanceLevel DEFAULT_BUSINESS_IMPORTANCE = ImportanceLevel.HIGH;
    private static final ImportanceLevel UPDATED_BUSINESS_IMPORTANCE = ImportanceLevel.MEDIUM;

    private static final Boolean DEFAULT_KEY_POSITION = false;
    private static final Boolean UPDATED_KEY_POSITION = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLANNED_HEADCOUNT = 0;
    private static final Integer UPDATED_PLANNED_HEADCOUNT = 1;
    private static final Integer SMALLER_PLANNED_HEADCOUNT = 0 - 1;

    private static final Integer DEFAULT_MINIMUM_OWNER_COUNT = 0;
    private static final Integer UPDATED_MINIMUM_OWNER_COUNT = 1;
    private static final Integer SMALLER_MINIMUM_OWNER_COUNT = 0 - 1;

    private static final ReviewCycle DEFAULT_REVIEW_CYCLE = ReviewCycle.MONTHLY;
    private static final ReviewCycle UPDATED_REVIEW_CYCLE = ReviewCycle.QUARTERLY;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    private Position insertedPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity() {
        return new Position()
            .positionCode(DEFAULT_POSITION_CODE)
            .positionName(DEFAULT_POSITION_NAME)
            .positionType(DEFAULT_POSITION_TYPE)
            .businessImportance(DEFAULT_BUSINESS_IMPORTANCE)
            .keyPosition(DEFAULT_KEY_POSITION)
            .description(DEFAULT_DESCRIPTION)
            .plannedHeadcount(DEFAULT_PLANNED_HEADCOUNT)
            .minimumOwnerCount(DEFAULT_MINIMUM_OWNER_COUNT)
            .reviewCycle(DEFAULT_REVIEW_CYCLE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity() {
        return new Position()
            .positionCode(UPDATED_POSITION_CODE)
            .positionName(UPDATED_POSITION_NAME)
            .positionType(UPDATED_POSITION_TYPE)
            .businessImportance(UPDATED_BUSINESS_IMPORTANCE)
            .keyPosition(UPDATED_KEY_POSITION)
            .description(UPDATED_DESCRIPTION)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .minimumOwnerCount(UPDATED_MINIMUM_OWNER_COUNT)
            .reviewCycle(UPDATED_REVIEW_CYCLE)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        position = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPosition != null) {
            positionRepository.delete(insertedPosition);
            insertedPosition = null;
        }
    }

    @Test
    @Transactional
    void createPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        var returnedPositionDTO = om.readValue(
            restPositionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionDTO.class
        );

        // Validate the Position in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPosition = positionMapper.toEntity(returnedPositionDTO);
        assertPositionUpdatableFieldsEquals(returnedPosition, getPersistedPosition(returnedPosition));

        insertedPosition = returnedPosition;
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPositionCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setPositionCode(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setPositionName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setPositionType(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBusinessImportanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setBusinessImportance(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeyPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setKeyPosition(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setActive(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositions() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].positionCode").value(hasItem(DEFAULT_POSITION_CODE)))
            .andExpect(jsonPath("$.[*].positionName").value(hasItem(DEFAULT_POSITION_NAME)))
            .andExpect(jsonPath("$.[*].positionType").value(hasItem(DEFAULT_POSITION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessImportance").value(hasItem(DEFAULT_BUSINESS_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].keyPosition").value(hasItem(DEFAULT_KEY_POSITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].plannedHeadcount").value(hasItem(DEFAULT_PLANNED_HEADCOUNT)))
            .andExpect(jsonPath("$.[*].minimumOwnerCount").value(hasItem(DEFAULT_MINIMUM_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].reviewCycle").value(hasItem(DEFAULT_REVIEW_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.positionCode").value(DEFAULT_POSITION_CODE))
            .andExpect(jsonPath("$.positionName").value(DEFAULT_POSITION_NAME))
            .andExpect(jsonPath("$.positionType").value(DEFAULT_POSITION_TYPE.toString()))
            .andExpect(jsonPath("$.businessImportance").value(DEFAULT_BUSINESS_IMPORTANCE.toString()))
            .andExpect(jsonPath("$.keyPosition").value(DEFAULT_KEY_POSITION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.plannedHeadcount").value(DEFAULT_PLANNED_HEADCOUNT))
            .andExpect(jsonPath("$.minimumOwnerCount").value(DEFAULT_MINIMUM_OWNER_COUNT))
            .andExpect(jsonPath("$.reviewCycle").value(DEFAULT_REVIEW_CYCLE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getPositionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        Long id = position.getId();

        defaultPositionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionCode equals to
        defaultPositionFiltering("positionCode.equals=" + DEFAULT_POSITION_CODE, "positionCode.equals=" + UPDATED_POSITION_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionCode in
        defaultPositionFiltering(
            "positionCode.in=" + DEFAULT_POSITION_CODE + "," + UPDATED_POSITION_CODE,
            "positionCode.in=" + UPDATED_POSITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPositionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionCode is not null
        defaultPositionFiltering("positionCode.specified=true", "positionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByPositionCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionCode contains
        defaultPositionFiltering("positionCode.contains=" + DEFAULT_POSITION_CODE, "positionCode.contains=" + UPDATED_POSITION_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionCode does not contain
        defaultPositionFiltering(
            "positionCode.doesNotContain=" + UPDATED_POSITION_CODE,
            "positionCode.doesNotContain=" + DEFAULT_POSITION_CODE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPositionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionName equals to
        defaultPositionFiltering("positionName.equals=" + DEFAULT_POSITION_NAME, "positionName.equals=" + UPDATED_POSITION_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionName in
        defaultPositionFiltering(
            "positionName.in=" + DEFAULT_POSITION_NAME + "," + UPDATED_POSITION_NAME,
            "positionName.in=" + UPDATED_POSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPositionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionName is not null
        defaultPositionFiltering("positionName.specified=true", "positionName.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByPositionNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionName contains
        defaultPositionFiltering("positionName.contains=" + DEFAULT_POSITION_NAME, "positionName.contains=" + UPDATED_POSITION_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionName does not contain
        defaultPositionFiltering(
            "positionName.doesNotContain=" + UPDATED_POSITION_NAME,
            "positionName.doesNotContain=" + DEFAULT_POSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPositionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionType equals to
        defaultPositionFiltering("positionType.equals=" + DEFAULT_POSITION_TYPE, "positionType.equals=" + UPDATED_POSITION_TYPE);
    }

    @Test
    @Transactional
    void getAllPositionsByPositionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionType in
        defaultPositionFiltering(
            "positionType.in=" + DEFAULT_POSITION_TYPE + "," + UPDATED_POSITION_TYPE,
            "positionType.in=" + UPDATED_POSITION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPositionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where positionType is not null
        defaultPositionFiltering("positionType.specified=true", "positionType.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByBusinessImportanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where businessImportance equals to
        defaultPositionFiltering(
            "businessImportance.equals=" + DEFAULT_BUSINESS_IMPORTANCE,
            "businessImportance.equals=" + UPDATED_BUSINESS_IMPORTANCE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByBusinessImportanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where businessImportance in
        defaultPositionFiltering(
            "businessImportance.in=" + DEFAULT_BUSINESS_IMPORTANCE + "," + UPDATED_BUSINESS_IMPORTANCE,
            "businessImportance.in=" + UPDATED_BUSINESS_IMPORTANCE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByBusinessImportanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where businessImportance is not null
        defaultPositionFiltering("businessImportance.specified=true", "businessImportance.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByKeyPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where keyPosition equals to
        defaultPositionFiltering("keyPosition.equals=" + DEFAULT_KEY_POSITION, "keyPosition.equals=" + UPDATED_KEY_POSITION);
    }

    @Test
    @Transactional
    void getAllPositionsByKeyPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where keyPosition in
        defaultPositionFiltering(
            "keyPosition.in=" + DEFAULT_KEY_POSITION + "," + UPDATED_KEY_POSITION,
            "keyPosition.in=" + UPDATED_KEY_POSITION
        );
    }

    @Test
    @Transactional
    void getAllPositionsByKeyPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where keyPosition is not null
        defaultPositionFiltering("keyPosition.specified=true", "keyPosition.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount equals to
        defaultPositionFiltering(
            "plannedHeadcount.equals=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.equals=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount in
        defaultPositionFiltering(
            "plannedHeadcount.in=" + DEFAULT_PLANNED_HEADCOUNT + "," + UPDATED_PLANNED_HEADCOUNT,
            "plannedHeadcount.in=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount is not null
        defaultPositionFiltering("plannedHeadcount.specified=true", "plannedHeadcount.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount is greater than or equal to
        defaultPositionFiltering(
            "plannedHeadcount.greaterThanOrEqual=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.greaterThanOrEqual=" + UPDATED_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount is less than or equal to
        defaultPositionFiltering(
            "plannedHeadcount.lessThanOrEqual=" + DEFAULT_PLANNED_HEADCOUNT,
            "plannedHeadcount.lessThanOrEqual=" + SMALLER_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount is less than
        defaultPositionFiltering(
            "plannedHeadcount.lessThan=" + UPDATED_PLANNED_HEADCOUNT,
            "plannedHeadcount.lessThan=" + DEFAULT_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByPlannedHeadcountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where plannedHeadcount is greater than
        defaultPositionFiltering(
            "plannedHeadcount.greaterThan=" + SMALLER_PLANNED_HEADCOUNT,
            "plannedHeadcount.greaterThan=" + DEFAULT_PLANNED_HEADCOUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount equals to
        defaultPositionFiltering(
            "minimumOwnerCount.equals=" + DEFAULT_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.equals=" + UPDATED_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount in
        defaultPositionFiltering(
            "minimumOwnerCount.in=" + DEFAULT_MINIMUM_OWNER_COUNT + "," + UPDATED_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.in=" + UPDATED_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount is not null
        defaultPositionFiltering("minimumOwnerCount.specified=true", "minimumOwnerCount.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount is greater than or equal to
        defaultPositionFiltering(
            "minimumOwnerCount.greaterThanOrEqual=" + DEFAULT_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.greaterThanOrEqual=" + UPDATED_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount is less than or equal to
        defaultPositionFiltering(
            "minimumOwnerCount.lessThanOrEqual=" + DEFAULT_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.lessThanOrEqual=" + SMALLER_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount is less than
        defaultPositionFiltering(
            "minimumOwnerCount.lessThan=" + UPDATED_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.lessThan=" + DEFAULT_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByMinimumOwnerCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where minimumOwnerCount is greater than
        defaultPositionFiltering(
            "minimumOwnerCount.greaterThan=" + SMALLER_MINIMUM_OWNER_COUNT,
            "minimumOwnerCount.greaterThan=" + DEFAULT_MINIMUM_OWNER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPositionsByReviewCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where reviewCycle equals to
        defaultPositionFiltering("reviewCycle.equals=" + DEFAULT_REVIEW_CYCLE, "reviewCycle.equals=" + UPDATED_REVIEW_CYCLE);
    }

    @Test
    @Transactional
    void getAllPositionsByReviewCycleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where reviewCycle in
        defaultPositionFiltering(
            "reviewCycle.in=" + DEFAULT_REVIEW_CYCLE + "," + UPDATED_REVIEW_CYCLE,
            "reviewCycle.in=" + UPDATED_REVIEW_CYCLE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByReviewCycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where reviewCycle is not null
        defaultPositionFiltering("reviewCycle.specified=true", "reviewCycle.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active equals to
        defaultPositionFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active in
        defaultPositionFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active is not null
        defaultPositionFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultPositionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionShouldBeFound(shouldBeFound);
        defaultPositionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionShouldBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].positionCode").value(hasItem(DEFAULT_POSITION_CODE)))
            .andExpect(jsonPath("$.[*].positionName").value(hasItem(DEFAULT_POSITION_NAME)))
            .andExpect(jsonPath("$.[*].positionType").value(hasItem(DEFAULT_POSITION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessImportance").value(hasItem(DEFAULT_BUSINESS_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].keyPosition").value(hasItem(DEFAULT_KEY_POSITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].plannedHeadcount").value(hasItem(DEFAULT_PLANNED_HEADCOUNT)))
            .andExpect(jsonPath("$.[*].minimumOwnerCount").value(hasItem(DEFAULT_MINIMUM_OWNER_COUNT)))
            .andExpect(jsonPath("$.[*].reviewCycle").value(hasItem(DEFAULT_REVIEW_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionShouldNotBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPosition are not directly saved in db
        em.detach(updatedPosition);
        updatedPosition
            .positionCode(UPDATED_POSITION_CODE)
            .positionName(UPDATED_POSITION_NAME)
            .positionType(UPDATED_POSITION_TYPE)
            .businessImportance(UPDATED_BUSINESS_IMPORTANCE)
            .keyPosition(UPDATED_KEY_POSITION)
            .description(UPDATED_DESCRIPTION)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .minimumOwnerCount(UPDATED_MINIMUM_OWNER_COUNT)
            .reviewCycle(UPDATED_REVIEW_CYCLE)
            .active(UPDATED_ACTIVE);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionToMatchAllProperties(updatedPosition);
    }

    @Test
    @Transactional
    void putNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition
            .positionCode(UPDATED_POSITION_CODE)
            .positionName(UPDATED_POSITION_NAME)
            .positionType(UPDATED_POSITION_TYPE)
            .businessImportance(UPDATED_BUSINESS_IMPORTANCE)
            .keyPosition(UPDATED_KEY_POSITION)
            .reviewCycle(UPDATED_REVIEW_CYCLE);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosition, position), getPersistedPosition(position));
    }

    @Test
    @Transactional
    void fullUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition
            .positionCode(UPDATED_POSITION_CODE)
            .positionName(UPDATED_POSITION_NAME)
            .positionType(UPDATED_POSITION_TYPE)
            .businessImportance(UPDATED_BUSINESS_IMPORTANCE)
            .keyPosition(UPDATED_KEY_POSITION)
            .description(UPDATED_DESCRIPTION)
            .plannedHeadcount(UPDATED_PLANNED_HEADCOUNT)
            .minimumOwnerCount(UPDATED_MINIMUM_OWNER_COUNT)
            .reviewCycle(UPDATED_REVIEW_CYCLE)
            .active(UPDATED_ACTIVE);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(partialUpdatedPosition, getPersistedPosition(partialUpdatedPosition));
    }

    @Test
    @Transactional
    void patchNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the position
        restPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, position.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRepository.count();
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

    protected Position getPersistedPosition(Position position) {
        return positionRepository.findById(position.getId()).orElseThrow();
    }

    protected void assertPersistedPositionToMatchAllProperties(Position expectedPosition) {
        assertPositionAllPropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }

    protected void assertPersistedPositionToMatchUpdatableProperties(Position expectedPosition) {
        assertPositionAllUpdatablePropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }
}
