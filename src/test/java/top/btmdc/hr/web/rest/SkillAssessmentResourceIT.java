package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SkillAssessmentAsserts.*;
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
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillAssessment;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.repository.SkillAssessmentRepository;
import top.btmdc.hr.service.SkillAssessmentService;
import top.btmdc.hr.service.dto.SkillAssessmentDTO;
import top.btmdc.hr.service.mapper.SkillAssessmentMapper;

/**
 * Integration tests for the {@link SkillAssessmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SkillAssessmentResourceIT {

    private static final LocalDate DEFAULT_ASSESSMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ASSESSMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ASSESSMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final AssessmentResult DEFAULT_RESULT = AssessmentResult.PASS;
    private static final AssessmentResult UPDATED_RESULT = AssessmentResult.WARNING;

    private static final String DEFAULT_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_EVIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/skill-assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;

    @Mock
    private SkillAssessmentRepository skillAssessmentRepositoryMock;

    @Autowired
    private SkillAssessmentMapper skillAssessmentMapper;

    @Mock
    private SkillAssessmentService skillAssessmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillAssessmentMockMvc;

    private SkillAssessment skillAssessment;

    private SkillAssessment insertedSkillAssessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillAssessment createEntity() {
        return new SkillAssessment()
            .assessmentDate(DEFAULT_ASSESSMENT_DATE)
            .result(DEFAULT_RESULT)
            .evidence(DEFAULT_EVIDENCE)
            .comment(DEFAULT_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillAssessment createUpdatedEntity() {
        return new SkillAssessment()
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .result(UPDATED_RESULT)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);
    }

    @BeforeEach
    void initTest() {
        skillAssessment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSkillAssessment != null) {
            skillAssessmentRepository.delete(insertedSkillAssessment);
            insertedSkillAssessment = null;
        }
    }

    @Test
    @Transactional
    void createSkillAssessment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);
        var returnedSkillAssessmentDTO = om.readValue(
            restSkillAssessmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillAssessmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillAssessmentDTO.class
        );

        // Validate the SkillAssessment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkillAssessment = skillAssessmentMapper.toEntity(returnedSkillAssessmentDTO);
        assertSkillAssessmentUpdatableFieldsEquals(returnedSkillAssessment, getPersistedSkillAssessment(returnedSkillAssessment));

        insertedSkillAssessment = returnedSkillAssessment;
    }

    @Test
    @Transactional
    void createSkillAssessmentWithExistingId() throws Exception {
        // Create the SkillAssessment with an existing ID
        skillAssessment.setId(1L);
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillAssessmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillAssessmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAssessmentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillAssessment.setAssessmentDate(null);

        // Create the SkillAssessment, which fails.
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        restSkillAssessmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillAssessmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillAssessment.setResult(null);

        // Create the SkillAssessment, which fails.
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        restSkillAssessmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillAssessmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkillAssessments() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentDate").value(hasItem(DEFAULT_ASSESSMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillAssessmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(skillAssessmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillAssessmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(skillAssessmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillAssessmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(skillAssessmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillAssessmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(skillAssessmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSkillAssessment() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get the skillAssessment
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL_ID, skillAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillAssessment.getId().intValue()))
            .andExpect(jsonPath("$.assessmentDate").value(DEFAULT_ASSESSMENT_DATE.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.evidence").value(DEFAULT_EVIDENCE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getSkillAssessmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        Long id = skillAssessment.getId();

        defaultSkillAssessmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSkillAssessmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSkillAssessmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate equals to
        defaultSkillAssessmentFiltering(
            "assessmentDate.equals=" + DEFAULT_ASSESSMENT_DATE,
            "assessmentDate.equals=" + UPDATED_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate in
        defaultSkillAssessmentFiltering(
            "assessmentDate.in=" + DEFAULT_ASSESSMENT_DATE + "," + UPDATED_ASSESSMENT_DATE,
            "assessmentDate.in=" + UPDATED_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate is not null
        defaultSkillAssessmentFiltering("assessmentDate.specified=true", "assessmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate is greater than or equal to
        defaultSkillAssessmentFiltering(
            "assessmentDate.greaterThanOrEqual=" + DEFAULT_ASSESSMENT_DATE,
            "assessmentDate.greaterThanOrEqual=" + UPDATED_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate is less than or equal to
        defaultSkillAssessmentFiltering(
            "assessmentDate.lessThanOrEqual=" + DEFAULT_ASSESSMENT_DATE,
            "assessmentDate.lessThanOrEqual=" + SMALLER_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate is less than
        defaultSkillAssessmentFiltering(
            "assessmentDate.lessThan=" + UPDATED_ASSESSMENT_DATE,
            "assessmentDate.lessThan=" + DEFAULT_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where assessmentDate is greater than
        defaultSkillAssessmentFiltering(
            "assessmentDate.greaterThan=" + SMALLER_ASSESSMENT_DATE,
            "assessmentDate.greaterThan=" + DEFAULT_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where result equals to
        defaultSkillAssessmentFiltering("result.equals=" + DEFAULT_RESULT, "result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where result in
        defaultSkillAssessmentFiltering("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT, "result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        // Get all the skillAssessmentList where result is not null
        defaultSkillAssessmentFiltering("result.specified=true", "result.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            skillAssessmentRepository.saveAndFlush(skillAssessment);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        skillAssessment.setPerson(person);
        skillAssessmentRepository.saveAndFlush(skillAssessment);
        Long personId = person.getId();
        // Get all the skillAssessmentList where person equals to personId
        defaultSkillAssessmentShouldBeFound("personId.equals=" + personId);

        // Get all the skillAssessmentList where person equals to (personId + 1)
        defaultSkillAssessmentShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skillAssessmentRepository.saveAndFlush(skillAssessment);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        skillAssessment.setSkill(skill);
        skillAssessmentRepository.saveAndFlush(skillAssessment);
        Long skillId = skill.getId();
        // Get all the skillAssessmentList where skill equals to skillId
        defaultSkillAssessmentShouldBeFound("skillId.equals=" + skillId);

        // Get all the skillAssessmentList where skill equals to (skillId + 1)
        defaultSkillAssessmentShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByAssessorIsEqualToSomething() throws Exception {
        Person assessor;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            skillAssessmentRepository.saveAndFlush(skillAssessment);
            assessor = PersonResourceIT.createEntity();
        } else {
            assessor = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(assessor);
        em.flush();
        skillAssessment.setAssessor(assessor);
        skillAssessmentRepository.saveAndFlush(skillAssessment);
        Long assessorId = assessor.getId();
        // Get all the skillAssessmentList where assessor equals to assessorId
        defaultSkillAssessmentShouldBeFound("assessorId.equals=" + assessorId);

        // Get all the skillAssessmentList where assessor equals to (assessorId + 1)
        defaultSkillAssessmentShouldNotBeFound("assessorId.equals=" + (assessorId + 1));
    }

    @Test
    @Transactional
    void getAllSkillAssessmentsByNewLevelIsEqualToSomething() throws Exception {
        SkillLevel newLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillAssessmentRepository.saveAndFlush(skillAssessment);
            newLevel = SkillLevelResourceIT.createEntity();
        } else {
            newLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(newLevel);
        em.flush();
        skillAssessment.setNewLevel(newLevel);
        skillAssessmentRepository.saveAndFlush(skillAssessment);
        Long newLevelId = newLevel.getId();
        // Get all the skillAssessmentList where newLevel equals to newLevelId
        defaultSkillAssessmentShouldBeFound("newLevelId.equals=" + newLevelId);

        // Get all the skillAssessmentList where newLevel equals to (newLevelId + 1)
        defaultSkillAssessmentShouldNotBeFound("newLevelId.equals=" + (newLevelId + 1));
    }

    private void defaultSkillAssessmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSkillAssessmentShouldBeFound(shouldBeFound);
        defaultSkillAssessmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkillAssessmentShouldBeFound(String filter) throws Exception {
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentDate").value(hasItem(DEFAULT_ASSESSMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkillAssessmentShouldNotBeFound(String filter) throws Exception {
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkillAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSkillAssessment() throws Exception {
        // Get the skillAssessment
        restSkillAssessmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkillAssessment() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillAssessment
        SkillAssessment updatedSkillAssessment = skillAssessmentRepository.findById(skillAssessment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkillAssessment are not directly saved in db
        em.detach(updatedSkillAssessment);
        updatedSkillAssessment
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .result(UPDATED_RESULT)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(updatedSkillAssessment);

        restSkillAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillAssessmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillAssessmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillAssessmentToMatchAllProperties(updatedSkillAssessment);
    }

    @Test
    @Transactional
    void putNonExistingSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillAssessmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillAssessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillAssessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillAssessmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillAssessmentWithPatch() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillAssessment using partial update
        SkillAssessment partialUpdatedSkillAssessment = new SkillAssessment();
        partialUpdatedSkillAssessment.setId(skillAssessment.getId());

        partialUpdatedSkillAssessment.assessmentDate(UPDATED_ASSESSMENT_DATE).result(UPDATED_RESULT).comment(UPDATED_COMMENT);

        restSkillAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillAssessment))
            )
            .andExpect(status().isOk());

        // Validate the SkillAssessment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillAssessmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSkillAssessment, skillAssessment),
            getPersistedSkillAssessment(skillAssessment)
        );
    }

    @Test
    @Transactional
    void fullUpdateSkillAssessmentWithPatch() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillAssessment using partial update
        SkillAssessment partialUpdatedSkillAssessment = new SkillAssessment();
        partialUpdatedSkillAssessment.setId(skillAssessment.getId());

        partialUpdatedSkillAssessment
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .result(UPDATED_RESULT)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);

        restSkillAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillAssessment))
            )
            .andExpect(status().isOk());

        // Validate the SkillAssessment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillAssessmentUpdatableFieldsEquals(
            partialUpdatedSkillAssessment,
            getPersistedSkillAssessment(partialUpdatedSkillAssessment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillAssessmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillAssessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillAssessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillAssessment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillAssessment.setId(longCount.incrementAndGet());

        // Create the SkillAssessment
        SkillAssessmentDTO skillAssessmentDTO = skillAssessmentMapper.toDto(skillAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillAssessmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillAssessmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillAssessment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillAssessment() throws Exception {
        // Initialize the database
        insertedSkillAssessment = skillAssessmentRepository.saveAndFlush(skillAssessment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skillAssessment
        restSkillAssessmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillAssessment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillAssessmentRepository.count();
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

    protected SkillAssessment getPersistedSkillAssessment(SkillAssessment skillAssessment) {
        return skillAssessmentRepository.findById(skillAssessment.getId()).orElseThrow();
    }

    protected void assertPersistedSkillAssessmentToMatchAllProperties(SkillAssessment expectedSkillAssessment) {
        assertSkillAssessmentAllPropertiesEquals(expectedSkillAssessment, getPersistedSkillAssessment(expectedSkillAssessment));
    }

    protected void assertPersistedSkillAssessmentToMatchUpdatableProperties(SkillAssessment expectedSkillAssessment) {
        assertSkillAssessmentAllUpdatablePropertiesEquals(expectedSkillAssessment, getPersistedSkillAssessment(expectedSkillAssessment));
    }
}
