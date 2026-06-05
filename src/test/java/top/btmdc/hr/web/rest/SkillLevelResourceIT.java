package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SkillLevelAsserts.*;
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
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.enumeration.LevelCode;
import top.btmdc.hr.repository.SkillLevelRepository;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.service.mapper.SkillLevelMapper;

/**
 * Integration tests for the {@link SkillLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillLevelResourceIT {

    private static final LevelCode DEFAULT_CODE = LevelCode.L0;
    private static final LevelCode UPDATED_CODE = LevelCode.L1;

    private static final String DEFAULT_LEVEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVABLE_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVABLE_EVIDENCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 0;
    private static final Integer UPDATED_SORT_ORDER = 1;
    private static final Integer SMALLER_SORT_ORDER = 0 - 1;

    private static final String ENTITY_API_URL = "/api/skill-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillLevelRepository skillLevelRepository;

    @Autowired
    private SkillLevelMapper skillLevelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillLevelMockMvc;

    private SkillLevel skillLevel;

    private SkillLevel insertedSkillLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillLevel createEntity() {
        return new SkillLevel()
            .code(DEFAULT_CODE)
            .levelName(DEFAULT_LEVEL_NAME)
            .definition(DEFAULT_DEFINITION)
            .observableEvidence(DEFAULT_OBSERVABLE_EVIDENCE)
            .sortOrder(DEFAULT_SORT_ORDER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillLevel createUpdatedEntity() {
        return new SkillLevel()
            .code(UPDATED_CODE)
            .levelName(UPDATED_LEVEL_NAME)
            .definition(UPDATED_DEFINITION)
            .observableEvidence(UPDATED_OBSERVABLE_EVIDENCE)
            .sortOrder(UPDATED_SORT_ORDER);
    }

    @BeforeEach
    void initTest() {
        skillLevel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSkillLevel != null) {
            skillLevelRepository.delete(insertedSkillLevel);
            insertedSkillLevel = null;
        }
    }

    @Test
    @Transactional
    void createSkillLevel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);
        var returnedSkillLevelDTO = om.readValue(
            restSkillLevelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillLevelDTO.class
        );

        // Validate the SkillLevel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkillLevel = skillLevelMapper.toEntity(returnedSkillLevelDTO);
        assertSkillLevelUpdatableFieldsEquals(returnedSkillLevel, getPersistedSkillLevel(returnedSkillLevel));

        insertedSkillLevel = returnedSkillLevel;
    }

    @Test
    @Transactional
    void createSkillLevelWithExistingId() throws Exception {
        // Create the SkillLevel with an existing ID
        skillLevel.setId(1L);
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillLevel.setCode(null);

        // Create the SkillLevel, which fails.
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        restSkillLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillLevel.setLevelName(null);

        // Create the SkillLevel, which fails.
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        restSkillLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSortOrderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillLevel.setSortOrder(null);

        // Create the SkillLevel, which fails.
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        restSkillLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkillLevels() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].levelName").value(hasItem(DEFAULT_LEVEL_NAME)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].observableEvidence").value(hasItem(DEFAULT_OBSERVABLE_EVIDENCE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    void getSkillLevel() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get the skillLevel
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, skillLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillLevel.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.levelName").value(DEFAULT_LEVEL_NAME))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION))
            .andExpect(jsonPath("$.observableEvidence").value(DEFAULT_OBSERVABLE_EVIDENCE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getSkillLevelsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        Long id = skillLevel.getId();

        defaultSkillLevelFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSkillLevelFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSkillLevelFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where code equals to
        defaultSkillLevelFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where code in
        defaultSkillLevelFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where code is not null
        defaultSkillLevelFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillLevelsByLevelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where levelName equals to
        defaultSkillLevelFiltering("levelName.equals=" + DEFAULT_LEVEL_NAME, "levelName.equals=" + UPDATED_LEVEL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByLevelNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where levelName in
        defaultSkillLevelFiltering("levelName.in=" + DEFAULT_LEVEL_NAME + "," + UPDATED_LEVEL_NAME, "levelName.in=" + UPDATED_LEVEL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByLevelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where levelName is not null
        defaultSkillLevelFiltering("levelName.specified=true", "levelName.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillLevelsByLevelNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where levelName contains
        defaultSkillLevelFiltering("levelName.contains=" + DEFAULT_LEVEL_NAME, "levelName.contains=" + UPDATED_LEVEL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillLevelsByLevelNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where levelName does not contain
        defaultSkillLevelFiltering("levelName.doesNotContain=" + UPDATED_LEVEL_NAME, "levelName.doesNotContain=" + DEFAULT_LEVEL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder equals to
        defaultSkillLevelFiltering("sortOrder.equals=" + DEFAULT_SORT_ORDER, "sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder in
        defaultSkillLevelFiltering("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER, "sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder is not null
        defaultSkillLevelFiltering("sortOrder.specified=true", "sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder is greater than or equal to
        defaultSkillLevelFiltering(
            "sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER,
            "sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder is less than or equal to
        defaultSkillLevelFiltering("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER, "sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder is less than
        defaultSkillLevelFiltering("sortOrder.lessThan=" + UPDATED_SORT_ORDER, "sortOrder.lessThan=" + DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllSkillLevelsBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where sortOrder is greater than
        defaultSkillLevelFiltering("sortOrder.greaterThan=" + SMALLER_SORT_ORDER, "sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);
    }

    private void defaultSkillLevelFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSkillLevelShouldBeFound(shouldBeFound);
        defaultSkillLevelShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkillLevelShouldBeFound(String filter) throws Exception {
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].levelName").value(hasItem(DEFAULT_LEVEL_NAME)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].observableEvidence").value(hasItem(DEFAULT_OBSERVABLE_EVIDENCE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkillLevelShouldNotBeFound(String filter) throws Exception {
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkillLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSkillLevel() throws Exception {
        // Get the skillLevel
        restSkillLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkillLevel() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillLevel
        SkillLevel updatedSkillLevel = skillLevelRepository.findById(skillLevel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkillLevel are not directly saved in db
        em.detach(updatedSkillLevel);
        updatedSkillLevel
            .code(UPDATED_CODE)
            .levelName(UPDATED_LEVEL_NAME)
            .definition(UPDATED_DEFINITION)
            .observableEvidence(UPDATED_OBSERVABLE_EVIDENCE)
            .sortOrder(UPDATED_SORT_ORDER);
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(updatedSkillLevel);

        restSkillLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillLevelToMatchAllProperties(updatedSkillLevel);
    }

    @Test
    @Transactional
    void putNonExistingSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillLevelWithPatch() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillLevel using partial update
        SkillLevel partialUpdatedSkillLevel = new SkillLevel();
        partialUpdatedSkillLevel.setId(skillLevel.getId());

        partialUpdatedSkillLevel.observableEvidence(UPDATED_OBSERVABLE_EVIDENCE).sortOrder(UPDATED_SORT_ORDER);

        restSkillLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillLevel))
            )
            .andExpect(status().isOk());

        // Validate the SkillLevel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillLevelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSkillLevel, skillLevel),
            getPersistedSkillLevel(skillLevel)
        );
    }

    @Test
    @Transactional
    void fullUpdateSkillLevelWithPatch() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillLevel using partial update
        SkillLevel partialUpdatedSkillLevel = new SkillLevel();
        partialUpdatedSkillLevel.setId(skillLevel.getId());

        partialUpdatedSkillLevel
            .code(UPDATED_CODE)
            .levelName(UPDATED_LEVEL_NAME)
            .definition(UPDATED_DEFINITION)
            .observableEvidence(UPDATED_OBSERVABLE_EVIDENCE)
            .sortOrder(UPDATED_SORT_ORDER);

        restSkillLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillLevel))
            )
            .andExpect(status().isOk());

        // Validate the SkillLevel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillLevelUpdatableFieldsEquals(partialUpdatedSkillLevel, getPersistedSkillLevel(partialUpdatedSkillLevel));
    }

    @Test
    @Transactional
    void patchNonExistingSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillLevelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillLevel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillLevel.setId(longCount.incrementAndGet());

        // Create the SkillLevel
        SkillLevelDTO skillLevelDTO = skillLevelMapper.toDto(skillLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillLevelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillLevel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillLevel() throws Exception {
        // Initialize the database
        insertedSkillLevel = skillLevelRepository.saveAndFlush(skillLevel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skillLevel
        restSkillLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillLevelRepository.count();
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

    protected SkillLevel getPersistedSkillLevel(SkillLevel skillLevel) {
        return skillLevelRepository.findById(skillLevel.getId()).orElseThrow();
    }

    protected void assertPersistedSkillLevelToMatchAllProperties(SkillLevel expectedSkillLevel) {
        assertSkillLevelAllPropertiesEquals(expectedSkillLevel, getPersistedSkillLevel(expectedSkillLevel));
    }

    protected void assertPersistedSkillLevelToMatchUpdatableProperties(SkillLevel expectedSkillLevel) {
        assertSkillLevelAllUpdatablePropertiesEquals(expectedSkillLevel, getPersistedSkillLevel(expectedSkillLevel));
    }
}
