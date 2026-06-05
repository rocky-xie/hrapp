package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SkillAsserts.*;
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
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.enumeration.EvidenceType;
import top.btmdc.hr.domain.enumeration.SkillType;
import top.btmdc.hr.repository.SkillRepository;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.mapper.SkillMapper;

/**
 * Integration tests for the {@link SkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillResourceIT {

    private static final String DEFAULT_SKILL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SKILL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SKILL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SKILL_NAME = "BBBBBBBBBB";

    private static final SkillType DEFAULT_SKILL_TYPE = SkillType.CERTIFICATE;
    private static final SkillType UPDATED_SKILL_TYPE = SkillType.TECHNICAL;

    private static final Boolean DEFAULT_MEASURABLE_FLAG = false;
    private static final Boolean UPDATED_MEASURABLE_FLAG = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final EvidenceType DEFAULT_EVIDENCE_TYPE = EvidenceType.CERTIFICATE;
    private static final EvidenceType UPDATED_EVIDENCE_TYPE = EvidenceType.PROJECT_EXPERIENCE;

    private static final String ENTITY_API_URL = "/api/skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillMockMvc;

    private Skill skill;

    private Skill insertedSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity() {
        return new Skill()
            .skillCode(DEFAULT_SKILL_CODE)
            .skillName(DEFAULT_SKILL_NAME)
            .skillType(DEFAULT_SKILL_TYPE)
            .measurableFlag(DEFAULT_MEASURABLE_FLAG)
            .description(DEFAULT_DESCRIPTION)
            .evidenceType(DEFAULT_EVIDENCE_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity() {
        return new Skill()
            .skillCode(UPDATED_SKILL_CODE)
            .skillName(UPDATED_SKILL_NAME)
            .skillType(UPDATED_SKILL_TYPE)
            .measurableFlag(UPDATED_MEASURABLE_FLAG)
            .description(UPDATED_DESCRIPTION)
            .evidenceType(UPDATED_EVIDENCE_TYPE);
    }

    @BeforeEach
    void initTest() {
        skill = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSkill != null) {
            skillRepository.delete(insertedSkill);
            insertedSkill = null;
        }
    }

    @Test
    @Transactional
    void createSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        var returnedSkillDTO = om.readValue(
            restSkillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillDTO.class
        );

        // Validate the Skill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkill = skillMapper.toEntity(returnedSkillDTO);
        assertSkillUpdatableFieldsEquals(returnedSkill, getPersistedSkill(returnedSkill));

        insertedSkill = returnedSkill;
    }

    @Test
    @Transactional
    void createSkillWithExistingId() throws Exception {
        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkillCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setSkillCode(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkillNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setSkillName(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkillTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setSkillType(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeasurableFlagIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setMeasurableFlag(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkills() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillCode").value(hasItem(DEFAULT_SKILL_CODE)))
            .andExpect(jsonPath("$.[*].skillName").value(hasItem(DEFAULT_SKILL_NAME)))
            .andExpect(jsonPath("$.[*].skillType").value(hasItem(DEFAULT_SKILL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].measurableFlag").value(hasItem(DEFAULT_MEASURABLE_FLAG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].evidenceType").value(hasItem(DEFAULT_EVIDENCE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get the skill
        restSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, skill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skill.getId().intValue()))
            .andExpect(jsonPath("$.skillCode").value(DEFAULT_SKILL_CODE))
            .andExpect(jsonPath("$.skillName").value(DEFAULT_SKILL_NAME))
            .andExpect(jsonPath("$.skillType").value(DEFAULT_SKILL_TYPE.toString()))
            .andExpect(jsonPath("$.measurableFlag").value(DEFAULT_MEASURABLE_FLAG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.evidenceType").value(DEFAULT_EVIDENCE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getSkillsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        Long id = skill.getId();

        defaultSkillFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSkillFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSkillFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillCode equals to
        defaultSkillFiltering("skillCode.equals=" + DEFAULT_SKILL_CODE, "skillCode.equals=" + UPDATED_SKILL_CODE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillCode in
        defaultSkillFiltering("skillCode.in=" + DEFAULT_SKILL_CODE + "," + UPDATED_SKILL_CODE, "skillCode.in=" + UPDATED_SKILL_CODE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillCode is not null
        defaultSkillFiltering("skillCode.specified=true", "skillCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillsBySkillCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillCode contains
        defaultSkillFiltering("skillCode.contains=" + DEFAULT_SKILL_CODE, "skillCode.contains=" + UPDATED_SKILL_CODE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillCode does not contain
        defaultSkillFiltering("skillCode.doesNotContain=" + UPDATED_SKILL_CODE, "skillCode.doesNotContain=" + DEFAULT_SKILL_CODE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillName equals to
        defaultSkillFiltering("skillName.equals=" + DEFAULT_SKILL_NAME, "skillName.equals=" + UPDATED_SKILL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillName in
        defaultSkillFiltering("skillName.in=" + DEFAULT_SKILL_NAME + "," + UPDATED_SKILL_NAME, "skillName.in=" + UPDATED_SKILL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillName is not null
        defaultSkillFiltering("skillName.specified=true", "skillName.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillsBySkillNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillName contains
        defaultSkillFiltering("skillName.contains=" + DEFAULT_SKILL_NAME, "skillName.contains=" + UPDATED_SKILL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillName does not contain
        defaultSkillFiltering("skillName.doesNotContain=" + UPDATED_SKILL_NAME, "skillName.doesNotContain=" + DEFAULT_SKILL_NAME);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillType equals to
        defaultSkillFiltering("skillType.equals=" + DEFAULT_SKILL_TYPE, "skillType.equals=" + UPDATED_SKILL_TYPE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillType in
        defaultSkillFiltering("skillType.in=" + DEFAULT_SKILL_TYPE + "," + UPDATED_SKILL_TYPE, "skillType.in=" + UPDATED_SKILL_TYPE);
    }

    @Test
    @Transactional
    void getAllSkillsBySkillTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where skillType is not null
        defaultSkillFiltering("skillType.specified=true", "skillType.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillsByMeasurableFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where measurableFlag equals to
        defaultSkillFiltering("measurableFlag.equals=" + DEFAULT_MEASURABLE_FLAG, "measurableFlag.equals=" + UPDATED_MEASURABLE_FLAG);
    }

    @Test
    @Transactional
    void getAllSkillsByMeasurableFlagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where measurableFlag in
        defaultSkillFiltering(
            "measurableFlag.in=" + DEFAULT_MEASURABLE_FLAG + "," + UPDATED_MEASURABLE_FLAG,
            "measurableFlag.in=" + UPDATED_MEASURABLE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllSkillsByMeasurableFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where measurableFlag is not null
        defaultSkillFiltering("measurableFlag.specified=true", "measurableFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillsByEvidenceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where evidenceType equals to
        defaultSkillFiltering("evidenceType.equals=" + DEFAULT_EVIDENCE_TYPE, "evidenceType.equals=" + UPDATED_EVIDENCE_TYPE);
    }

    @Test
    @Transactional
    void getAllSkillsByEvidenceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where evidenceType in
        defaultSkillFiltering(
            "evidenceType.in=" + DEFAULT_EVIDENCE_TYPE + "," + UPDATED_EVIDENCE_TYPE,
            "evidenceType.in=" + UPDATED_EVIDENCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSkillsByEvidenceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        // Get all the skillList where evidenceType is not null
        defaultSkillFiltering("evidenceType.specified=true", "evidenceType.specified=false");
    }

    private void defaultSkillFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSkillShouldBeFound(shouldBeFound);
        defaultSkillShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkillShouldBeFound(String filter) throws Exception {
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillCode").value(hasItem(DEFAULT_SKILL_CODE)))
            .andExpect(jsonPath("$.[*].skillName").value(hasItem(DEFAULT_SKILL_NAME)))
            .andExpect(jsonPath("$.[*].skillType").value(hasItem(DEFAULT_SKILL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].measurableFlag").value(hasItem(DEFAULT_MEASURABLE_FLAG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].evidenceType").value(hasItem(DEFAULT_EVIDENCE_TYPE.toString())));

        // Check, that the count call also returns 1
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkillShouldNotBeFound(String filter) throws Exception {
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSkill() throws Exception {
        // Get the skill
        restSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkill are not directly saved in db
        em.detach(updatedSkill);
        updatedSkill
            .skillCode(UPDATED_SKILL_CODE)
            .skillName(UPDATED_SKILL_NAME)
            .skillType(UPDATED_SKILL_TYPE)
            .measurableFlag(UPDATED_MEASURABLE_FLAG)
            .description(UPDATED_DESCRIPTION)
            .evidenceType(UPDATED_EVIDENCE_TYPE);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillToMatchAllProperties(updatedSkill);
    }

    @Test
    @Transactional
    void putNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.skillName(UPDATED_SKILL_NAME).skillType(UPDATED_SKILL_TYPE);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSkill, skill), getPersistedSkill(skill));
    }

    @Test
    @Transactional
    void fullUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill
            .skillCode(UPDATED_SKILL_CODE)
            .skillName(UPDATED_SKILL_NAME)
            .skillType(UPDATED_SKILL_TYPE)
            .measurableFlag(UPDATED_MEASURABLE_FLAG)
            .description(UPDATED_DESCRIPTION)
            .evidenceType(UPDATED_EVIDENCE_TYPE);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(partialUpdatedSkill, getPersistedSkill(partialUpdatedSkill));
    }

    @Test
    @Transactional
    void patchNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.saveAndFlush(skill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skill
        restSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, skill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillRepository.count();
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

    protected Skill getPersistedSkill(Skill skill) {
        return skillRepository.findById(skill.getId()).orElseThrow();
    }

    protected void assertPersistedSkillToMatchAllProperties(Skill expectedSkill) {
        assertSkillAllPropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }

    protected void assertPersistedSkillToMatchUpdatableProperties(Skill expectedSkill) {
        assertSkillAllUpdatablePropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }
}
