package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.KeyResponsibilityCategoryAsserts.*;
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
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.repository.KeyResponsibilityCategoryRepository;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;
import top.btmdc.hr.service.mapper.KeyResponsibilityCategoryMapper;

/**
 * Integration tests for the {@link KeyResponsibilityCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KeyResponsibilityCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMPLES = "AAAAAAAAAA";
    private static final String UPDATED_EXAMPLES = "BBBBBBBBBB";

    private static final String DEFAULT_RISK_FOCUS = "AAAAAAAAAA";
    private static final String UPDATED_RISK_FOCUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/key-responsibility-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository;

    @Autowired
    private KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeyResponsibilityCategoryMockMvc;

    private KeyResponsibilityCategory keyResponsibilityCategory;

    private KeyResponsibilityCategory insertedKeyResponsibilityCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyResponsibilityCategory createEntity() {
        return new KeyResponsibilityCategory().categoryName(DEFAULT_CATEGORY_NAME).examples(DEFAULT_EXAMPLES).riskFocus(DEFAULT_RISK_FOCUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeyResponsibilityCategory createUpdatedEntity() {
        return new KeyResponsibilityCategory().categoryName(UPDATED_CATEGORY_NAME).examples(UPDATED_EXAMPLES).riskFocus(UPDATED_RISK_FOCUS);
    }

    @BeforeEach
    void initTest() {
        keyResponsibilityCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKeyResponsibilityCategory != null) {
            keyResponsibilityCategoryRepository.delete(insertedKeyResponsibilityCategory);
            insertedKeyResponsibilityCategory = null;
        }
    }

    @Test
    @Transactional
    void createKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);
        var returnedKeyResponsibilityCategoryDTO = om.readValue(
            restKeyResponsibilityCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KeyResponsibilityCategoryDTO.class
        );

        // Validate the KeyResponsibilityCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKeyResponsibilityCategory = keyResponsibilityCategoryMapper.toEntity(returnedKeyResponsibilityCategoryDTO);
        assertKeyResponsibilityCategoryUpdatableFieldsEquals(
            returnedKeyResponsibilityCategory,
            getPersistedKeyResponsibilityCategory(returnedKeyResponsibilityCategory)
        );

        insertedKeyResponsibilityCategory = returnedKeyResponsibilityCategory;
    }

    @Test
    @Transactional
    void createKeyResponsibilityCategoryWithExistingId() throws Exception {
        // Create the KeyResponsibilityCategory with an existing ID
        keyResponsibilityCategory.setId(1L);
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyResponsibilityCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keyResponsibilityCategory.setCategoryName(null);

        // Create the KeyResponsibilityCategory, which fails.
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        restKeyResponsibilityCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategories() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyResponsibilityCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].examples").value(hasItem(DEFAULT_EXAMPLES)))
            .andExpect(jsonPath("$.[*].riskFocus").value(hasItem(DEFAULT_RISK_FOCUS)));
    }

    @Test
    @Transactional
    void getKeyResponsibilityCategory() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get the keyResponsibilityCategory
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, keyResponsibilityCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keyResponsibilityCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.examples").value(DEFAULT_EXAMPLES))
            .andExpect(jsonPath("$.riskFocus").value(DEFAULT_RISK_FOCUS));
    }

    @Test
    @Transactional
    void getKeyResponsibilityCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        Long id = keyResponsibilityCategory.getId();

        defaultKeyResponsibilityCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultKeyResponsibilityCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultKeyResponsibilityCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList where categoryName equals to
        defaultKeyResponsibilityCategoryFiltering(
            "categoryName.equals=" + DEFAULT_CATEGORY_NAME,
            "categoryName.equals=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList where categoryName in
        defaultKeyResponsibilityCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList where categoryName is not null
        defaultKeyResponsibilityCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList where categoryName contains
        defaultKeyResponsibilityCategoryFiltering(
            "categoryName.contains=" + DEFAULT_CATEGORY_NAME,
            "categoryName.contains=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllKeyResponsibilityCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        // Get all the keyResponsibilityCategoryList where categoryName does not contain
        defaultKeyResponsibilityCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    private void defaultKeyResponsibilityCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultKeyResponsibilityCategoryShouldBeFound(shouldBeFound);
        defaultKeyResponsibilityCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKeyResponsibilityCategoryShouldBeFound(String filter) throws Exception {
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyResponsibilityCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].examples").value(hasItem(DEFAULT_EXAMPLES)))
            .andExpect(jsonPath("$.[*].riskFocus").value(hasItem(DEFAULT_RISK_FOCUS)));

        // Check, that the count call also returns 1
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKeyResponsibilityCategoryShouldNotBeFound(String filter) throws Exception {
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeyResponsibilityCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKeyResponsibilityCategory() throws Exception {
        // Get the keyResponsibilityCategory
        restKeyResponsibilityCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKeyResponsibilityCategory() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keyResponsibilityCategory
        KeyResponsibilityCategory updatedKeyResponsibilityCategory = keyResponsibilityCategoryRepository
            .findById(keyResponsibilityCategory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedKeyResponsibilityCategory are not directly saved in db
        em.detach(updatedKeyResponsibilityCategory);
        updatedKeyResponsibilityCategory.categoryName(UPDATED_CATEGORY_NAME).examples(UPDATED_EXAMPLES).riskFocus(UPDATED_RISK_FOCUS);
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(updatedKeyResponsibilityCategory);

        restKeyResponsibilityCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keyResponsibilityCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKeyResponsibilityCategoryToMatchAllProperties(updatedKeyResponsibilityCategory);
    }

    @Test
    @Transactional
    void putNonExistingKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keyResponsibilityCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKeyResponsibilityCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keyResponsibilityCategory using partial update
        KeyResponsibilityCategory partialUpdatedKeyResponsibilityCategory = new KeyResponsibilityCategory();
        partialUpdatedKeyResponsibilityCategory.setId(keyResponsibilityCategory.getId());

        partialUpdatedKeyResponsibilityCategory.examples(UPDATED_EXAMPLES).riskFocus(UPDATED_RISK_FOCUS);

        restKeyResponsibilityCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeyResponsibilityCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeyResponsibilityCategory))
            )
            .andExpect(status().isOk());

        // Validate the KeyResponsibilityCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeyResponsibilityCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKeyResponsibilityCategory, keyResponsibilityCategory),
            getPersistedKeyResponsibilityCategory(keyResponsibilityCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateKeyResponsibilityCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keyResponsibilityCategory using partial update
        KeyResponsibilityCategory partialUpdatedKeyResponsibilityCategory = new KeyResponsibilityCategory();
        partialUpdatedKeyResponsibilityCategory.setId(keyResponsibilityCategory.getId());

        partialUpdatedKeyResponsibilityCategory
            .categoryName(UPDATED_CATEGORY_NAME)
            .examples(UPDATED_EXAMPLES)
            .riskFocus(UPDATED_RISK_FOCUS);

        restKeyResponsibilityCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeyResponsibilityCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeyResponsibilityCategory))
            )
            .andExpect(status().isOk());

        // Validate the KeyResponsibilityCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeyResponsibilityCategoryUpdatableFieldsEquals(
            partialUpdatedKeyResponsibilityCategory,
            getPersistedKeyResponsibilityCategory(partialUpdatedKeyResponsibilityCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, keyResponsibilityCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKeyResponsibilityCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keyResponsibilityCategory.setId(longCount.incrementAndGet());

        // Create the KeyResponsibilityCategory
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeyResponsibilityCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keyResponsibilityCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeyResponsibilityCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKeyResponsibilityCategory() throws Exception {
        // Initialize the database
        insertedKeyResponsibilityCategory = keyResponsibilityCategoryRepository.saveAndFlush(keyResponsibilityCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the keyResponsibilityCategory
        restKeyResponsibilityCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, keyResponsibilityCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return keyResponsibilityCategoryRepository.count();
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

    protected KeyResponsibilityCategory getPersistedKeyResponsibilityCategory(KeyResponsibilityCategory keyResponsibilityCategory) {
        return keyResponsibilityCategoryRepository.findById(keyResponsibilityCategory.getId()).orElseThrow();
    }

    protected void assertPersistedKeyResponsibilityCategoryToMatchAllProperties(
        KeyResponsibilityCategory expectedKeyResponsibilityCategory
    ) {
        assertKeyResponsibilityCategoryAllPropertiesEquals(
            expectedKeyResponsibilityCategory,
            getPersistedKeyResponsibilityCategory(expectedKeyResponsibilityCategory)
        );
    }

    protected void assertPersistedKeyResponsibilityCategoryToMatchUpdatableProperties(
        KeyResponsibilityCategory expectedKeyResponsibilityCategory
    ) {
        assertKeyResponsibilityCategoryAllUpdatablePropertiesEquals(
            expectedKeyResponsibilityCategory,
            getPersistedKeyResponsibilityCategory(expectedKeyResponsibilityCategory)
        );
    }
}
