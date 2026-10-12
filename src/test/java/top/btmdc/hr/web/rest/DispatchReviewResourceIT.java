package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.DispatchReviewAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.DispatchReview;
import top.btmdc.hr.repository.DispatchReviewRepository;
import top.btmdc.hr.service.dto.DispatchReviewDTO;
import top.btmdc.hr.service.mapper.DispatchReviewMapper;

/**
 * Integration tests for the {@link DispatchReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_HR_RECRUITER")
class DispatchReviewResourceIT {

    private static final Instant DEFAULT_REVIEWED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEWED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REVIEWER_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_RECOMMENDATION = "AAAAAAAAAA";
    private static final String UPDATED_RECOMMENDATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXTENSION_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXTENSION_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXTENSION_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FINAL_DECISION = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_DECISION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dispatch-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DispatchReviewRepository dispatchReviewRepository;

    @Autowired
    private DispatchReviewMapper dispatchReviewMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispatchReviewMockMvc;

    private DispatchReview dispatchReview;

    private DispatchReview insertedDispatchReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DispatchReview createEntity() {
        return new DispatchReview()
            .reviewedAt(DEFAULT_REVIEWED_AT)
            .reviewerUsername(DEFAULT_REVIEWER_USERNAME)
            .result(DEFAULT_RESULT)
            .recommendation(DEFAULT_RECOMMENDATION)
            .extensionEndDate(DEFAULT_EXTENSION_END_DATE)
            .finalDecision(DEFAULT_FINAL_DECISION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DispatchReview createUpdatedEntity() {
        return new DispatchReview()
            .reviewedAt(UPDATED_REVIEWED_AT)
            .reviewerUsername(UPDATED_REVIEWER_USERNAME)
            .result(UPDATED_RESULT)
            .recommendation(UPDATED_RECOMMENDATION)
            .extensionEndDate(UPDATED_EXTENSION_END_DATE)
            .finalDecision(UPDATED_FINAL_DECISION);
    }

    @BeforeEach
    void initTest() {
        dispatchReview = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDispatchReview != null) {
            dispatchReviewRepository.delete(insertedDispatchReview);
            insertedDispatchReview = null;
        }
    }

    @Test
    @Transactional
    void createDispatchReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);
        var returnedDispatchReviewDTO = om.readValue(
            restDispatchReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchReviewDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DispatchReviewDTO.class
        );

        // Validate the DispatchReview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDispatchReview = dispatchReviewMapper.toEntity(returnedDispatchReviewDTO);
        assertDispatchReviewUpdatableFieldsEquals(returnedDispatchReview, getPersistedDispatchReview(returnedDispatchReview));

        insertedDispatchReview = returnedDispatchReview;
    }

    @Test
    @Transactional
    void createDispatchReviewWithExistingId() throws Exception {
        // Create the DispatchReview with an existing ID
        dispatchReview.setId(1L);
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispatchReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchReviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReviewedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchReview.setReviewedAt(null);

        // Create the DispatchReview, which fails.
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        restDispatchReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchReviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReviewerUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispatchReview.setReviewerUsername(null);

        // Create the DispatchReview, which fails.
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        restDispatchReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchReviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispatchReviews() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatchReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(DEFAULT_REVIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].reviewerUsername").value(hasItem(DEFAULT_REVIEWER_USERNAME)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION)))
            .andExpect(jsonPath("$.[*].extensionEndDate").value(hasItem(DEFAULT_EXTENSION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].finalDecision").value(hasItem(DEFAULT_FINAL_DECISION)));
    }

    @Test
    @Transactional
    void getDispatchReview() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get the dispatchReview
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, dispatchReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispatchReview.getId().intValue()))
            .andExpect(jsonPath("$.reviewedAt").value(DEFAULT_REVIEWED_AT.toString()))
            .andExpect(jsonPath("$.reviewerUsername").value(DEFAULT_REVIEWER_USERNAME))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.recommendation").value(DEFAULT_RECOMMENDATION))
            .andExpect(jsonPath("$.extensionEndDate").value(DEFAULT_EXTENSION_END_DATE.toString()))
            .andExpect(jsonPath("$.finalDecision").value(DEFAULT_FINAL_DECISION));
    }

    @Test
    @Transactional
    void getDispatchReviewsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        Long id = dispatchReview.getId();

        defaultDispatchReviewFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDispatchReviewFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDispatchReviewFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewedAt equals to
        defaultDispatchReviewFiltering("reviewedAt.equals=" + DEFAULT_REVIEWED_AT, "reviewedAt.equals=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewedAt in
        defaultDispatchReviewFiltering(
            "reviewedAt.in=" + DEFAULT_REVIEWED_AT + "," + UPDATED_REVIEWED_AT,
            "reviewedAt.in=" + UPDATED_REVIEWED_AT
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewedAt is not null
        defaultDispatchReviewFiltering("reviewedAt.specified=true", "reviewedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewerUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewerUsername equals to
        defaultDispatchReviewFiltering(
            "reviewerUsername.equals=" + DEFAULT_REVIEWER_USERNAME,
            "reviewerUsername.equals=" + UPDATED_REVIEWER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewerUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewerUsername in
        defaultDispatchReviewFiltering(
            "reviewerUsername.in=" + DEFAULT_REVIEWER_USERNAME + "," + UPDATED_REVIEWER_USERNAME,
            "reviewerUsername.in=" + UPDATED_REVIEWER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewerUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewerUsername is not null
        defaultDispatchReviewFiltering("reviewerUsername.specified=true", "reviewerUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewerUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewerUsername contains
        defaultDispatchReviewFiltering(
            "reviewerUsername.contains=" + DEFAULT_REVIEWER_USERNAME,
            "reviewerUsername.contains=" + UPDATED_REVIEWER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByReviewerUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where reviewerUsername does not contain
        defaultDispatchReviewFiltering(
            "reviewerUsername.doesNotContain=" + UPDATED_REVIEWER_USERNAME,
            "reviewerUsername.doesNotContain=" + DEFAULT_REVIEWER_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByRecommendationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where recommendation equals to
        defaultDispatchReviewFiltering(
            "recommendation.equals=" + DEFAULT_RECOMMENDATION,
            "recommendation.equals=" + UPDATED_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByRecommendationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where recommendation in
        defaultDispatchReviewFiltering(
            "recommendation.in=" + DEFAULT_RECOMMENDATION + "," + UPDATED_RECOMMENDATION,
            "recommendation.in=" + UPDATED_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByRecommendationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where recommendation is not null
        defaultDispatchReviewFiltering("recommendation.specified=true", "recommendation.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByRecommendationContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where recommendation contains
        defaultDispatchReviewFiltering(
            "recommendation.contains=" + DEFAULT_RECOMMENDATION,
            "recommendation.contains=" + UPDATED_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByRecommendationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where recommendation does not contain
        defaultDispatchReviewFiltering(
            "recommendation.doesNotContain=" + UPDATED_RECOMMENDATION,
            "recommendation.doesNotContain=" + DEFAULT_RECOMMENDATION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate equals to
        defaultDispatchReviewFiltering(
            "extensionEndDate.equals=" + DEFAULT_EXTENSION_END_DATE,
            "extensionEndDate.equals=" + UPDATED_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate in
        defaultDispatchReviewFiltering(
            "extensionEndDate.in=" + DEFAULT_EXTENSION_END_DATE + "," + UPDATED_EXTENSION_END_DATE,
            "extensionEndDate.in=" + UPDATED_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate is not null
        defaultDispatchReviewFiltering("extensionEndDate.specified=true", "extensionEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate is greater than or equal to
        defaultDispatchReviewFiltering(
            "extensionEndDate.greaterThanOrEqual=" + DEFAULT_EXTENSION_END_DATE,
            "extensionEndDate.greaterThanOrEqual=" + UPDATED_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate is less than or equal to
        defaultDispatchReviewFiltering(
            "extensionEndDate.lessThanOrEqual=" + DEFAULT_EXTENSION_END_DATE,
            "extensionEndDate.lessThanOrEqual=" + SMALLER_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate is less than
        defaultDispatchReviewFiltering(
            "extensionEndDate.lessThan=" + UPDATED_EXTENSION_END_DATE,
            "extensionEndDate.lessThan=" + DEFAULT_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByExtensionEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where extensionEndDate is greater than
        defaultDispatchReviewFiltering(
            "extensionEndDate.greaterThan=" + SMALLER_EXTENSION_END_DATE,
            "extensionEndDate.greaterThan=" + DEFAULT_EXTENSION_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByFinalDecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where finalDecision equals to
        defaultDispatchReviewFiltering("finalDecision.equals=" + DEFAULT_FINAL_DECISION, "finalDecision.equals=" + UPDATED_FINAL_DECISION);
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByFinalDecisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where finalDecision in
        defaultDispatchReviewFiltering(
            "finalDecision.in=" + DEFAULT_FINAL_DECISION + "," + UPDATED_FINAL_DECISION,
            "finalDecision.in=" + UPDATED_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByFinalDecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where finalDecision is not null
        defaultDispatchReviewFiltering("finalDecision.specified=true", "finalDecision.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByFinalDecisionContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where finalDecision contains
        defaultDispatchReviewFiltering(
            "finalDecision.contains=" + DEFAULT_FINAL_DECISION,
            "finalDecision.contains=" + UPDATED_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByFinalDecisionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        // Get all the dispatchReviewList where finalDecision does not contain
        defaultDispatchReviewFiltering(
            "finalDecision.doesNotContain=" + UPDATED_FINAL_DECISION,
            "finalDecision.doesNotContain=" + DEFAULT_FINAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllDispatchReviewsByDispatchAssignmentIsEqualToSomething() throws Exception {
        DispatchAssignment dispatchAssignment;
        if (TestUtil.findAll(em, DispatchAssignment.class).isEmpty()) {
            dispatchReviewRepository.saveAndFlush(dispatchReview);
            dispatchAssignment = DispatchAssignmentResourceIT.createEntity();
        } else {
            dispatchAssignment = TestUtil.findAll(em, DispatchAssignment.class).get(0);
        }
        em.persist(dispatchAssignment);
        em.flush();
        dispatchReview.setDispatchAssignment(dispatchAssignment);
        dispatchReviewRepository.saveAndFlush(dispatchReview);
        Long dispatchAssignmentId = dispatchAssignment.getId();
        // Get all the dispatchReviewList where dispatchAssignment equals to dispatchAssignmentId
        defaultDispatchReviewShouldBeFound("dispatchAssignmentId.equals=" + dispatchAssignmentId);

        // Get all the dispatchReviewList where dispatchAssignment equals to (dispatchAssignmentId + 1)
        defaultDispatchReviewShouldNotBeFound("dispatchAssignmentId.equals=" + (dispatchAssignmentId + 1));
    }

    private void defaultDispatchReviewFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDispatchReviewShouldBeFound(shouldBeFound);
        defaultDispatchReviewShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDispatchReviewShouldBeFound(String filter) throws Exception {
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatchReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(DEFAULT_REVIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].reviewerUsername").value(hasItem(DEFAULT_REVIEWER_USERNAME)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION)))
            .andExpect(jsonPath("$.[*].extensionEndDate").value(hasItem(DEFAULT_EXTENSION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].finalDecision").value(hasItem(DEFAULT_FINAL_DECISION)));

        // Check, that the count call also returns 1
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDispatchReviewShouldNotBeFound(String filter) throws Exception {
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDispatchReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDispatchReview() throws Exception {
        // Get the dispatchReview
        restDispatchReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDispatchReview() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchReview
        DispatchReview updatedDispatchReview = dispatchReviewRepository.findById(dispatchReview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDispatchReview are not directly saved in db
        em.detach(updatedDispatchReview);
        updatedDispatchReview
            .reviewedAt(UPDATED_REVIEWED_AT)
            .reviewerUsername(UPDATED_REVIEWER_USERNAME)
            .result(UPDATED_RESULT)
            .recommendation(UPDATED_RECOMMENDATION)
            .extensionEndDate(UPDATED_EXTENSION_END_DATE)
            .finalDecision(UPDATED_FINAL_DECISION);
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(updatedDispatchReview);

        restDispatchReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatchReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchReviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDispatchReviewToMatchAllProperties(updatedDispatchReview);
    }

    @Test
    @Transactional
    void putNonExistingDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatchReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispatchReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispatchReviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispatchReviewWithPatch() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchReview using partial update
        DispatchReview partialUpdatedDispatchReview = new DispatchReview();
        partialUpdatedDispatchReview.setId(dispatchReview.getId());

        partialUpdatedDispatchReview
            .reviewedAt(UPDATED_REVIEWED_AT)
            .result(UPDATED_RESULT)
            .extensionEndDate(UPDATED_EXTENSION_END_DATE)
            .finalDecision(UPDATED_FINAL_DECISION);

        restDispatchReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispatchReview))
            )
            .andExpect(status().isOk());

        // Validate the DispatchReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispatchReviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDispatchReview, dispatchReview),
            getPersistedDispatchReview(dispatchReview)
        );
    }

    @Test
    @Transactional
    void fullUpdateDispatchReviewWithPatch() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispatchReview using partial update
        DispatchReview partialUpdatedDispatchReview = new DispatchReview();
        partialUpdatedDispatchReview.setId(dispatchReview.getId());

        partialUpdatedDispatchReview
            .reviewedAt(UPDATED_REVIEWED_AT)
            .reviewerUsername(UPDATED_REVIEWER_USERNAME)
            .result(UPDATED_RESULT)
            .recommendation(UPDATED_RECOMMENDATION)
            .extensionEndDate(UPDATED_EXTENSION_END_DATE)
            .finalDecision(UPDATED_FINAL_DECISION);

        restDispatchReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatchReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispatchReview))
            )
            .andExpect(status().isOk());

        // Validate the DispatchReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispatchReviewUpdatableFieldsEquals(partialUpdatedDispatchReview, getPersistedDispatchReview(partialUpdatedDispatchReview));
    }

    @Test
    @Transactional
    void patchNonExistingDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispatchReviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispatchReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispatchReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispatchReview.setId(longCount.incrementAndGet());

        // Create the DispatchReview
        DispatchReviewDTO dispatchReviewDTO = dispatchReviewMapper.toDto(dispatchReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dispatchReviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DispatchReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispatchReview() throws Exception {
        // Initialize the database
        insertedDispatchReview = dispatchReviewRepository.saveAndFlush(dispatchReview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dispatchReview
        restDispatchReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispatchReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dispatchReviewRepository.count();
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

    protected DispatchReview getPersistedDispatchReview(DispatchReview dispatchReview) {
        return dispatchReviewRepository.findById(dispatchReview.getId()).orElseThrow();
    }

    protected void assertPersistedDispatchReviewToMatchAllProperties(DispatchReview expectedDispatchReview) {
        assertDispatchReviewAllPropertiesEquals(expectedDispatchReview, getPersistedDispatchReview(expectedDispatchReview));
    }

    protected void assertPersistedDispatchReviewToMatchUpdatableProperties(DispatchReview expectedDispatchReview) {
        assertDispatchReviewAllUpdatablePropertiesEquals(expectedDispatchReview, getPersistedDispatchReview(expectedDispatchReview));
    }
}
