package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PositionSkillRequirementAsserts.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.enumeration.RequirementImportance;
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.service.PositionSkillRequirementService;
import top.btmdc.hr.service.dto.PositionSkillRequirementDTO;
import top.btmdc.hr.service.mapper.PositionSkillRequirementMapper;

/**
 * Integration tests for the {@link PositionSkillRequirementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PositionSkillRequirementResourceIT {

    private static final RequirementImportance DEFAULT_IMPORTANCE = RequirementImportance.REQUIRED;
    private static final RequirementImportance UPDATED_IMPORTANCE = RequirementImportance.IMPORTANT;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/position-skill-requirements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionSkillRequirementRepository positionSkillRequirementRepository;

    @Mock
    private PositionSkillRequirementRepository positionSkillRequirementRepositoryMock;

    @Autowired
    private PositionSkillRequirementMapper positionSkillRequirementMapper;

    @Mock
    private PositionSkillRequirementService positionSkillRequirementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionSkillRequirementMockMvc;

    private PositionSkillRequirement positionSkillRequirement;

    private PositionSkillRequirement insertedPositionSkillRequirement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionSkillRequirement createEntity(EntityManager em) {
        PositionSkillRequirement positionSkillRequirement = new PositionSkillRequirement()
            .importance(DEFAULT_IMPORTANCE)
            .remark(DEFAULT_REMARK);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        positionSkillRequirement.setPosition(position);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        positionSkillRequirement.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        positionSkillRequirement.setRequiredLevel(skillLevel);
        return positionSkillRequirement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionSkillRequirement createUpdatedEntity(EntityManager em) {
        PositionSkillRequirement updatedPositionSkillRequirement = new PositionSkillRequirement()
            .importance(UPDATED_IMPORTANCE)
            .remark(UPDATED_REMARK);
        // Add required entity
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceIT.createUpdatedEntity();
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        updatedPositionSkillRequirement.setPosition(position);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createUpdatedEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        updatedPositionSkillRequirement.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createUpdatedEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        updatedPositionSkillRequirement.setRequiredLevel(skillLevel);
        return updatedPositionSkillRequirement;
    }

    @BeforeEach
    void initTest() {
        positionSkillRequirement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPositionSkillRequirement != null) {
            positionSkillRequirementRepository.delete(insertedPositionSkillRequirement);
            insertedPositionSkillRequirement = null;
        }
    }

    @Test
    @Transactional
    void createPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);
        var returnedPositionSkillRequirementDTO = om.readValue(
            restPositionSkillRequirementMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionSkillRequirementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionSkillRequirementDTO.class
        );

        // Validate the PositionSkillRequirement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPositionSkillRequirement = positionSkillRequirementMapper.toEntity(returnedPositionSkillRequirementDTO);
        assertPositionSkillRequirementUpdatableFieldsEquals(
            returnedPositionSkillRequirement,
            getPersistedPositionSkillRequirement(returnedPositionSkillRequirement)
        );

        insertedPositionSkillRequirement = returnedPositionSkillRequirement;
    }

    @Test
    @Transactional
    void createPositionSkillRequirementWithExistingId() throws Exception {
        // Create the PositionSkillRequirement with an existing ID
        positionSkillRequirement.setId(1L);
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionSkillRequirementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImportanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        positionSkillRequirement.setImportance(null);

        // Create the PositionSkillRequirement, which fails.
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        restPositionSkillRequirementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirements() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        // Get all the positionSkillRequirementList
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionSkillRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionSkillRequirementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(positionSkillRequirementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionSkillRequirementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(positionSkillRequirementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPositionSkillRequirementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(positionSkillRequirementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPositionSkillRequirementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(positionSkillRequirementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPositionSkillRequirement() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        // Get the positionSkillRequirement
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL_ID, positionSkillRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionSkillRequirement.getId().intValue()))
            .andExpect(jsonPath("$.importance").value(DEFAULT_IMPORTANCE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    @Transactional
    void getPositionSkillRequirementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        Long id = positionSkillRequirement.getId();

        defaultPositionSkillRequirementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionSkillRequirementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionSkillRequirementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByImportanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        // Get all the positionSkillRequirementList where importance equals to
        defaultPositionSkillRequirementFiltering("importance.equals=" + DEFAULT_IMPORTANCE, "importance.equals=" + UPDATED_IMPORTANCE);
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByImportanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        // Get all the positionSkillRequirementList where importance in
        defaultPositionSkillRequirementFiltering(
            "importance.in=" + DEFAULT_IMPORTANCE + "," + UPDATED_IMPORTANCE,
            "importance.in=" + UPDATED_IMPORTANCE
        );
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByImportanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        // Get all the positionSkillRequirementList where importance is not null
        defaultPositionSkillRequirementFiltering("importance.specified=true", "importance.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
            position = PositionResourceIT.createEntity();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        positionSkillRequirement.setPosition(position);
        positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
        Long positionId = position.getId();
        // Get all the positionSkillRequirementList where position equals to positionId
        defaultPositionSkillRequirementShouldBeFound("positionId.equals=" + positionId);

        // Get all the positionSkillRequirementList where position equals to (positionId + 1)
        defaultPositionSkillRequirementShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        positionSkillRequirement.setSkill(skill);
        positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
        Long skillId = skill.getId();
        // Get all the positionSkillRequirementList where skill equals to skillId
        defaultPositionSkillRequirementShouldBeFound("skillId.equals=" + skillId);

        // Get all the positionSkillRequirementList where skill equals to (skillId + 1)
        defaultPositionSkillRequirementShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByRequiredLevelIsEqualToSomething() throws Exception {
        SkillLevel requiredLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
            requiredLevel = SkillLevelResourceIT.createEntity();
        } else {
            requiredLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(requiredLevel);
        em.flush();
        positionSkillRequirement.setRequiredLevel(requiredLevel);
        positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
        Long requiredLevelId = requiredLevel.getId();
        // Get all the positionSkillRequirementList where requiredLevel equals to requiredLevelId
        defaultPositionSkillRequirementShouldBeFound("requiredLevelId.equals=" + requiredLevelId);

        // Get all the positionSkillRequirementList where requiredLevel equals to (requiredLevelId + 1)
        defaultPositionSkillRequirementShouldNotBeFound("requiredLevelId.equals=" + (requiredLevelId + 1));
    }

    @Test
    @Transactional
    void getAllPositionSkillRequirementsByPreferredLevelIsEqualToSomething() throws Exception {
        SkillLevel preferredLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
            preferredLevel = SkillLevelResourceIT.createEntity();
        } else {
            preferredLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(preferredLevel);
        em.flush();
        positionSkillRequirement.setPreferredLevel(preferredLevel);
        positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);
        Long preferredLevelId = preferredLevel.getId();
        // Get all the positionSkillRequirementList where preferredLevel equals to preferredLevelId
        defaultPositionSkillRequirementShouldBeFound("preferredLevelId.equals=" + preferredLevelId);

        // Get all the positionSkillRequirementList where preferredLevel equals to (preferredLevelId + 1)
        defaultPositionSkillRequirementShouldNotBeFound("preferredLevelId.equals=" + (preferredLevelId + 1));
    }

    private void defaultPositionSkillRequirementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionSkillRequirementShouldBeFound(shouldBeFound);
        defaultPositionSkillRequirementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionSkillRequirementShouldBeFound(String filter) throws Exception {
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionSkillRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));

        // Check, that the count call also returns 1
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionSkillRequirementShouldNotBeFound(String filter) throws Exception {
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionSkillRequirementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPositionSkillRequirement() throws Exception {
        // Get the positionSkillRequirement
        restPositionSkillRequirementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPositionSkillRequirement() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionSkillRequirement
        PositionSkillRequirement updatedPositionSkillRequirement = positionSkillRequirementRepository
            .findById(positionSkillRequirement.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPositionSkillRequirement are not directly saved in db
        em.detach(updatedPositionSkillRequirement);
        updatedPositionSkillRequirement.importance(UPDATED_IMPORTANCE).remark(UPDATED_REMARK);
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(updatedPositionSkillRequirement);

        restPositionSkillRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionSkillRequirementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isOk());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionSkillRequirementToMatchAllProperties(updatedPositionSkillRequirement);
    }

    @Test
    @Transactional
    void putNonExistingPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionSkillRequirementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionSkillRequirementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionSkillRequirementWithPatch() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionSkillRequirement using partial update
        PositionSkillRequirement partialUpdatedPositionSkillRequirement = new PositionSkillRequirement();
        partialUpdatedPositionSkillRequirement.setId(positionSkillRequirement.getId());

        partialUpdatedPositionSkillRequirement.importance(UPDATED_IMPORTANCE).remark(UPDATED_REMARK);

        restPositionSkillRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionSkillRequirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionSkillRequirement))
            )
            .andExpect(status().isOk());

        // Validate the PositionSkillRequirement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionSkillRequirementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPositionSkillRequirement, positionSkillRequirement),
            getPersistedPositionSkillRequirement(positionSkillRequirement)
        );
    }

    @Test
    @Transactional
    void fullUpdatePositionSkillRequirementWithPatch() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the positionSkillRequirement using partial update
        PositionSkillRequirement partialUpdatedPositionSkillRequirement = new PositionSkillRequirement();
        partialUpdatedPositionSkillRequirement.setId(positionSkillRequirement.getId());

        partialUpdatedPositionSkillRequirement.importance(UPDATED_IMPORTANCE).remark(UPDATED_REMARK);

        restPositionSkillRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionSkillRequirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPositionSkillRequirement))
            )
            .andExpect(status().isOk());

        // Validate the PositionSkillRequirement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionSkillRequirementUpdatableFieldsEquals(
            partialUpdatedPositionSkillRequirement,
            getPersistedPositionSkillRequirement(partialUpdatedPositionSkillRequirement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionSkillRequirementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionSkillRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        positionSkillRequirement.setId(longCount.incrementAndGet());

        // Create the PositionSkillRequirement
        PositionSkillRequirementDTO positionSkillRequirementDTO = positionSkillRequirementMapper.toDto(positionSkillRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionSkillRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionSkillRequirementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionSkillRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionSkillRequirement() throws Exception {
        // Initialize the database
        insertedPositionSkillRequirement = positionSkillRequirementRepository.saveAndFlush(positionSkillRequirement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the positionSkillRequirement
        restPositionSkillRequirementMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionSkillRequirement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionSkillRequirementRepository.count();
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

    protected PositionSkillRequirement getPersistedPositionSkillRequirement(PositionSkillRequirement positionSkillRequirement) {
        return positionSkillRequirementRepository.findById(positionSkillRequirement.getId()).orElseThrow();
    }

    protected void assertPersistedPositionSkillRequirementToMatchAllProperties(PositionSkillRequirement expectedPositionSkillRequirement) {
        assertPositionSkillRequirementAllPropertiesEquals(
            expectedPositionSkillRequirement,
            getPersistedPositionSkillRequirement(expectedPositionSkillRequirement)
        );
    }

    protected void assertPersistedPositionSkillRequirementToMatchUpdatableProperties(
        PositionSkillRequirement expectedPositionSkillRequirement
    ) {
        assertPositionSkillRequirementAllUpdatablePropertiesEquals(
            expectedPositionSkillRequirement,
            getPersistedPositionSkillRequirement(expectedPositionSkillRequirement)
        );
    }
}
