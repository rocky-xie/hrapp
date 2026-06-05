package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.PersonSkillAsserts.*;
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
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.enumeration.ConfidenceLevel;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.service.PersonSkillService;
import top.btmdc.hr.service.dto.PersonSkillDTO;
import top.btmdc.hr.service.mapper.PersonSkillMapper;

/**
 * Integration tests for the {@link PersonSkillResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonSkillResourceIT {

    private static final LocalDate DEFAULT_ASSESSMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ASSESSMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ASSESSMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_NEXT_REVIEW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_REVIEW_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NEXT_REVIEW_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_EVIDENCE = "BBBBBBBBBB";

    private static final ConfidenceLevel DEFAULT_CONFIDENCE = ConfidenceLevel.HIGH;
    private static final ConfidenceLevel UPDATED_CONFIDENCE = ConfidenceLevel.MEDIUM;

    private static final String DEFAULT_GROWTH_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_GROWTH_DIRECTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/person-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonSkillRepository personSkillRepository;

    @Mock
    private PersonSkillRepository personSkillRepositoryMock;

    @Autowired
    private PersonSkillMapper personSkillMapper;

    @Mock
    private PersonSkillService personSkillServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonSkillMockMvc;

    private PersonSkill personSkill;

    private PersonSkill insertedPersonSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonSkill createEntity(EntityManager em) {
        PersonSkill personSkill = new PersonSkill()
            .assessmentDate(DEFAULT_ASSESSMENT_DATE)
            .nextReviewDate(DEFAULT_NEXT_REVIEW_DATE)
            .evidence(DEFAULT_EVIDENCE)
            .confidence(DEFAULT_CONFIDENCE)
            .growthDirection(DEFAULT_GROWTH_DIRECTION);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        personSkill.setPerson(person);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        personSkill.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        personSkill.setCurrentLevel(skillLevel);
        return personSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonSkill createUpdatedEntity(EntityManager em) {
        PersonSkill updatedPersonSkill = new PersonSkill()
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .nextReviewDate(UPDATED_NEXT_REVIEW_DATE)
            .evidence(UPDATED_EVIDENCE)
            .confidence(UPDATED_CONFIDENCE)
            .growthDirection(UPDATED_GROWTH_DIRECTION);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedPersonSkill.setPerson(person);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createUpdatedEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        updatedPersonSkill.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createUpdatedEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        updatedPersonSkill.setCurrentLevel(skillLevel);
        return updatedPersonSkill;
    }

    @BeforeEach
    void initTest() {
        personSkill = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPersonSkill != null) {
            personSkillRepository.delete(insertedPersonSkill);
            insertedPersonSkill = null;
        }
    }

    @Test
    @Transactional
    void createPersonSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);
        var returnedPersonSkillDTO = om.readValue(
            restPersonSkillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personSkillDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonSkillDTO.class
        );

        // Validate the PersonSkill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPersonSkill = personSkillMapper.toEntity(returnedPersonSkillDTO);
        assertPersonSkillUpdatableFieldsEquals(returnedPersonSkill, getPersistedPersonSkill(returnedPersonSkill));

        insertedPersonSkill = returnedPersonSkill;
    }

    @Test
    @Transactional
    void createPersonSkillWithExistingId() throws Exception {
        // Create the PersonSkill with an existing ID
        personSkill.setId(1L);
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personSkillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAssessmentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personSkill.setAssessmentDate(null);

        // Create the PersonSkill, which fails.
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        restPersonSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personSkillDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonSkills() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentDate").value(hasItem(DEFAULT_ASSESSMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextReviewDate").value(hasItem(DEFAULT_NEXT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE.toString())))
            .andExpect(jsonPath("$.[*].growthDirection").value(hasItem(DEFAULT_GROWTH_DIRECTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonSkillsWithEagerRelationshipsIsEnabled() throws Exception {
        when(personSkillServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonSkillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personSkillServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonSkillsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personSkillServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonSkillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(personSkillRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPersonSkill() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get the personSkill
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, personSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personSkill.getId().intValue()))
            .andExpect(jsonPath("$.assessmentDate").value(DEFAULT_ASSESSMENT_DATE.toString()))
            .andExpect(jsonPath("$.nextReviewDate").value(DEFAULT_NEXT_REVIEW_DATE.toString()))
            .andExpect(jsonPath("$.evidence").value(DEFAULT_EVIDENCE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE.toString()))
            .andExpect(jsonPath("$.growthDirection").value(DEFAULT_GROWTH_DIRECTION));
    }

    @Test
    @Transactional
    void getPersonSkillsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        Long id = personSkill.getId();

        defaultPersonSkillFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPersonSkillFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPersonSkillFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate equals to
        defaultPersonSkillFiltering("assessmentDate.equals=" + DEFAULT_ASSESSMENT_DATE, "assessmentDate.equals=" + UPDATED_ASSESSMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate in
        defaultPersonSkillFiltering(
            "assessmentDate.in=" + DEFAULT_ASSESSMENT_DATE + "," + UPDATED_ASSESSMENT_DATE,
            "assessmentDate.in=" + UPDATED_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate is not null
        defaultPersonSkillFiltering("assessmentDate.specified=true", "assessmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate is greater than or equal to
        defaultPersonSkillFiltering(
            "assessmentDate.greaterThanOrEqual=" + DEFAULT_ASSESSMENT_DATE,
            "assessmentDate.greaterThanOrEqual=" + UPDATED_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate is less than or equal to
        defaultPersonSkillFiltering(
            "assessmentDate.lessThanOrEqual=" + DEFAULT_ASSESSMENT_DATE,
            "assessmentDate.lessThanOrEqual=" + SMALLER_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate is less than
        defaultPersonSkillFiltering(
            "assessmentDate.lessThan=" + UPDATED_ASSESSMENT_DATE,
            "assessmentDate.lessThan=" + DEFAULT_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByAssessmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where assessmentDate is greater than
        defaultPersonSkillFiltering(
            "assessmentDate.greaterThan=" + SMALLER_ASSESSMENT_DATE,
            "assessmentDate.greaterThan=" + DEFAULT_ASSESSMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate equals to
        defaultPersonSkillFiltering(
            "nextReviewDate.equals=" + DEFAULT_NEXT_REVIEW_DATE,
            "nextReviewDate.equals=" + UPDATED_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate in
        defaultPersonSkillFiltering(
            "nextReviewDate.in=" + DEFAULT_NEXT_REVIEW_DATE + "," + UPDATED_NEXT_REVIEW_DATE,
            "nextReviewDate.in=" + UPDATED_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate is not null
        defaultPersonSkillFiltering("nextReviewDate.specified=true", "nextReviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate is greater than or equal to
        defaultPersonSkillFiltering(
            "nextReviewDate.greaterThanOrEqual=" + DEFAULT_NEXT_REVIEW_DATE,
            "nextReviewDate.greaterThanOrEqual=" + UPDATED_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate is less than or equal to
        defaultPersonSkillFiltering(
            "nextReviewDate.lessThanOrEqual=" + DEFAULT_NEXT_REVIEW_DATE,
            "nextReviewDate.lessThanOrEqual=" + SMALLER_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate is less than
        defaultPersonSkillFiltering(
            "nextReviewDate.lessThan=" + UPDATED_NEXT_REVIEW_DATE,
            "nextReviewDate.lessThan=" + DEFAULT_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByNextReviewDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where nextReviewDate is greater than
        defaultPersonSkillFiltering(
            "nextReviewDate.greaterThan=" + SMALLER_NEXT_REVIEW_DATE,
            "nextReviewDate.greaterThan=" + DEFAULT_NEXT_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where confidence equals to
        defaultPersonSkillFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllPersonSkillsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where confidence in
        defaultPersonSkillFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllPersonSkillsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        // Get all the personSkillList where confidence is not null
        defaultPersonSkillFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonSkillsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personSkillRepository.saveAndFlush(personSkill);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        personSkill.setPerson(person);
        personSkillRepository.saveAndFlush(personSkill);
        Long personId = person.getId();
        // Get all the personSkillList where person equals to personId
        defaultPersonSkillShouldBeFound("personId.equals=" + personId);

        // Get all the personSkillList where person equals to (personId + 1)
        defaultPersonSkillShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllPersonSkillsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            personSkillRepository.saveAndFlush(personSkill);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        personSkill.setSkill(skill);
        personSkillRepository.saveAndFlush(personSkill);
        Long skillId = skill.getId();
        // Get all the personSkillList where skill equals to skillId
        defaultPersonSkillShouldBeFound("skillId.equals=" + skillId);

        // Get all the personSkillList where skill equals to (skillId + 1)
        defaultPersonSkillShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllPersonSkillsByCurrentLevelIsEqualToSomething() throws Exception {
        SkillLevel currentLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            personSkillRepository.saveAndFlush(personSkill);
            currentLevel = SkillLevelResourceIT.createEntity();
        } else {
            currentLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(currentLevel);
        em.flush();
        personSkill.setCurrentLevel(currentLevel);
        personSkillRepository.saveAndFlush(personSkill);
        Long currentLevelId = currentLevel.getId();
        // Get all the personSkillList where currentLevel equals to currentLevelId
        defaultPersonSkillShouldBeFound("currentLevelId.equals=" + currentLevelId);

        // Get all the personSkillList where currentLevel equals to (currentLevelId + 1)
        defaultPersonSkillShouldNotBeFound("currentLevelId.equals=" + (currentLevelId + 1));
    }

    @Test
    @Transactional
    void getAllPersonSkillsByPreviousLevelIsEqualToSomething() throws Exception {
        SkillLevel previousLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            personSkillRepository.saveAndFlush(personSkill);
            previousLevel = SkillLevelResourceIT.createEntity();
        } else {
            previousLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(previousLevel);
        em.flush();
        personSkill.setPreviousLevel(previousLevel);
        personSkillRepository.saveAndFlush(personSkill);
        Long previousLevelId = previousLevel.getId();
        // Get all the personSkillList where previousLevel equals to previousLevelId
        defaultPersonSkillShouldBeFound("previousLevelId.equals=" + previousLevelId);

        // Get all the personSkillList where previousLevel equals to (previousLevelId + 1)
        defaultPersonSkillShouldNotBeFound("previousLevelId.equals=" + (previousLevelId + 1));
    }

    private void defaultPersonSkillFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPersonSkillShouldBeFound(shouldBeFound);
        defaultPersonSkillShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonSkillShouldBeFound(String filter) throws Exception {
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].assessmentDate").value(hasItem(DEFAULT_ASSESSMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextReviewDate").value(hasItem(DEFAULT_NEXT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE.toString())))
            .andExpect(jsonPath("$.[*].growthDirection").value(hasItem(DEFAULT_GROWTH_DIRECTION)));

        // Check, that the count call also returns 1
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonSkillShouldNotBeFound(String filter) throws Exception {
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonSkillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonSkill() throws Exception {
        // Get the personSkill
        restPersonSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonSkill() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personSkill
        PersonSkill updatedPersonSkill = personSkillRepository.findById(personSkill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonSkill are not directly saved in db
        em.detach(updatedPersonSkill);
        updatedPersonSkill
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .nextReviewDate(UPDATED_NEXT_REVIEW_DATE)
            .evidence(UPDATED_EVIDENCE)
            .confidence(UPDATED_CONFIDENCE)
            .growthDirection(UPDATED_GROWTH_DIRECTION);
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(updatedPersonSkill);

        restPersonSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personSkillDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonSkillToMatchAllProperties(updatedPersonSkill);
    }

    @Test
    @Transactional
    void putNonExistingPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonSkillWithPatch() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personSkill using partial update
        PersonSkill partialUpdatedPersonSkill = new PersonSkill();
        partialUpdatedPersonSkill.setId(personSkill.getId());

        partialUpdatedPersonSkill
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .nextReviewDate(UPDATED_NEXT_REVIEW_DATE)
            .evidence(UPDATED_EVIDENCE);

        restPersonSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonSkill))
            )
            .andExpect(status().isOk());

        // Validate the PersonSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonSkillUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonSkill, personSkill),
            getPersistedPersonSkill(personSkill)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonSkillWithPatch() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personSkill using partial update
        PersonSkill partialUpdatedPersonSkill = new PersonSkill();
        partialUpdatedPersonSkill.setId(personSkill.getId());

        partialUpdatedPersonSkill
            .assessmentDate(UPDATED_ASSESSMENT_DATE)
            .nextReviewDate(UPDATED_NEXT_REVIEW_DATE)
            .evidence(UPDATED_EVIDENCE)
            .confidence(UPDATED_CONFIDENCE)
            .growthDirection(UPDATED_GROWTH_DIRECTION);

        restPersonSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonSkill))
            )
            .andExpect(status().isOk());

        // Validate the PersonSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonSkillUpdatableFieldsEquals(partialUpdatedPersonSkill, getPersistedPersonSkill(partialUpdatedPersonSkill));
    }

    @Test
    @Transactional
    void patchNonExistingPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personSkillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personSkill.setId(longCount.incrementAndGet());

        // Create the PersonSkill
        PersonSkillDTO personSkillDTO = personSkillMapper.toDto(personSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonSkill() throws Exception {
        // Initialize the database
        insertedPersonSkill = personSkillRepository.saveAndFlush(personSkill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personSkill
        restPersonSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, personSkill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personSkillRepository.count();
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

    protected PersonSkill getPersistedPersonSkill(PersonSkill personSkill) {
        return personSkillRepository.findById(personSkill.getId()).orElseThrow();
    }

    protected void assertPersistedPersonSkillToMatchAllProperties(PersonSkill expectedPersonSkill) {
        assertPersonSkillAllPropertiesEquals(expectedPersonSkill, getPersistedPersonSkill(expectedPersonSkill));
    }

    protected void assertPersistedPersonSkillToMatchUpdatableProperties(PersonSkill expectedPersonSkill) {
        assertPersonSkillAllUpdatablePropertiesEquals(expectedPersonSkill, getPersistedPersonSkill(expectedPersonSkill));
    }
}
