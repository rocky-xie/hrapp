package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.domain.SkillUpgradeRecordAsserts.*;
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
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.SkillUpgradeRecord;
import top.btmdc.hr.domain.enumeration.SkillChangeType;
import top.btmdc.hr.repository.SkillUpgradeRecordRepository;
import top.btmdc.hr.service.SkillUpgradeRecordService;
import top.btmdc.hr.service.dto.SkillUpgradeRecordDTO;
import top.btmdc.hr.service.mapper.SkillUpgradeRecordMapper;

/**
 * Integration tests for the {@link SkillUpgradeRecordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SkillUpgradeRecordResourceIT {

    private static final SkillChangeType DEFAULT_CHANGE_TYPE = SkillChangeType.NEW_SKILL;
    private static final SkillChangeType UPDATED_CHANGE_TYPE = SkillChangeType.LEVEL_UP;

    private static final LocalDate DEFAULT_CHANGE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHANGE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CHANGE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_BEFORE_LEVEL_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_BEFORE_LEVEL_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_AFTER_LEVEL_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_AFTER_LEVEL_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_EVIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_EVIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/skill-upgrade-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillUpgradeRecordRepository skillUpgradeRecordRepository;

    @Mock
    private SkillUpgradeRecordRepository skillUpgradeRecordRepositoryMock;

    @Autowired
    private SkillUpgradeRecordMapper skillUpgradeRecordMapper;

    @Mock
    private SkillUpgradeRecordService skillUpgradeRecordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillUpgradeRecordMockMvc;

    private SkillUpgradeRecord skillUpgradeRecord;

    private SkillUpgradeRecord insertedSkillUpgradeRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillUpgradeRecord createEntity(EntityManager em) {
        SkillUpgradeRecord skillUpgradeRecord = new SkillUpgradeRecord()
            .changeType(DEFAULT_CHANGE_TYPE)
            .changeDate(DEFAULT_CHANGE_DATE)
            .reason(DEFAULT_REASON)
            .beforeLevelLabel(DEFAULT_BEFORE_LEVEL_LABEL)
            .afterLevelLabel(DEFAULT_AFTER_LEVEL_LABEL)
            .evidence(DEFAULT_EVIDENCE)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        skillUpgradeRecord.setPerson(person);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        skillUpgradeRecord.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        skillUpgradeRecord.setNewLevel(skillLevel);
        return skillUpgradeRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillUpgradeRecord createUpdatedEntity(EntityManager em) {
        SkillUpgradeRecord updatedSkillUpgradeRecord = new SkillUpgradeRecord()
            .changeType(UPDATED_CHANGE_TYPE)
            .changeDate(UPDATED_CHANGE_DATE)
            .reason(UPDATED_REASON)
            .beforeLevelLabel(UPDATED_BEFORE_LEVEL_LABEL)
            .afterLevelLabel(UPDATED_AFTER_LEVEL_LABEL)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity();
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        updatedSkillUpgradeRecord.setPerson(person);
        // Add required entity
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skill = SkillResourceIT.createUpdatedEntity();
            em.persist(skill);
            em.flush();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        updatedSkillUpgradeRecord.setSkill(skill);
        // Add required entity
        SkillLevel skillLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillLevel = SkillLevelResourceIT.createUpdatedEntity();
            em.persist(skillLevel);
            em.flush();
        } else {
            skillLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        updatedSkillUpgradeRecord.setNewLevel(skillLevel);
        return updatedSkillUpgradeRecord;
    }

    @BeforeEach
    void initTest() {
        skillUpgradeRecord = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSkillUpgradeRecord != null) {
            skillUpgradeRecordRepository.delete(insertedSkillUpgradeRecord);
            insertedSkillUpgradeRecord = null;
        }
    }

    @Test
    @Transactional
    void createSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);
        var returnedSkillUpgradeRecordDTO = om.readValue(
            restSkillUpgradeRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillUpgradeRecordDTO.class
        );

        // Validate the SkillUpgradeRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkillUpgradeRecord = skillUpgradeRecordMapper.toEntity(returnedSkillUpgradeRecordDTO);
        assertSkillUpgradeRecordUpdatableFieldsEquals(
            returnedSkillUpgradeRecord,
            getPersistedSkillUpgradeRecord(returnedSkillUpgradeRecord)
        );

        insertedSkillUpgradeRecord = returnedSkillUpgradeRecord;
    }

    @Test
    @Transactional
    void createSkillUpgradeRecordWithExistingId() throws Exception {
        // Create the SkillUpgradeRecord with an existing ID
        skillUpgradeRecord.setId(1L);
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillUpgradeRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChangeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillUpgradeRecord.setChangeType(null);

        // Create the SkillUpgradeRecord, which fails.
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        restSkillUpgradeRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangeDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillUpgradeRecord.setChangeDate(null);

        // Create the SkillUpgradeRecord, which fails.
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        restSkillUpgradeRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillUpgradeRecord.setReason(null);

        // Create the SkillUpgradeRecord, which fails.
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        restSkillUpgradeRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecords() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillUpgradeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeType").value(hasItem(DEFAULT_CHANGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(DEFAULT_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].beforeLevelLabel").value(hasItem(DEFAULT_BEFORE_LEVEL_LABEL)))
            .andExpect(jsonPath("$.[*].afterLevelLabel").value(hasItem(DEFAULT_AFTER_LEVEL_LABEL)))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillUpgradeRecordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(skillUpgradeRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillUpgradeRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(skillUpgradeRecordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillUpgradeRecordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(skillUpgradeRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillUpgradeRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(skillUpgradeRecordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSkillUpgradeRecord() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get the skillUpgradeRecord
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, skillUpgradeRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillUpgradeRecord.getId().intValue()))
            .andExpect(jsonPath("$.changeType").value(DEFAULT_CHANGE_TYPE.toString()))
            .andExpect(jsonPath("$.changeDate").value(DEFAULT_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.beforeLevelLabel").value(DEFAULT_BEFORE_LEVEL_LABEL))
            .andExpect(jsonPath("$.afterLevelLabel").value(DEFAULT_AFTER_LEVEL_LABEL))
            .andExpect(jsonPath("$.evidence").value(DEFAULT_EVIDENCE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getSkillUpgradeRecordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        Long id = skillUpgradeRecord.getId();

        defaultSkillUpgradeRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSkillUpgradeRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSkillUpgradeRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeType equals to
        defaultSkillUpgradeRecordFiltering("changeType.equals=" + DEFAULT_CHANGE_TYPE, "changeType.equals=" + UPDATED_CHANGE_TYPE);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeType in
        defaultSkillUpgradeRecordFiltering(
            "changeType.in=" + DEFAULT_CHANGE_TYPE + "," + UPDATED_CHANGE_TYPE,
            "changeType.in=" + UPDATED_CHANGE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeType is not null
        defaultSkillUpgradeRecordFiltering("changeType.specified=true", "changeType.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate equals to
        defaultSkillUpgradeRecordFiltering("changeDate.equals=" + DEFAULT_CHANGE_DATE, "changeDate.equals=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate in
        defaultSkillUpgradeRecordFiltering(
            "changeDate.in=" + DEFAULT_CHANGE_DATE + "," + UPDATED_CHANGE_DATE,
            "changeDate.in=" + UPDATED_CHANGE_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate is not null
        defaultSkillUpgradeRecordFiltering("changeDate.specified=true", "changeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate is greater than or equal to
        defaultSkillUpgradeRecordFiltering(
            "changeDate.greaterThanOrEqual=" + DEFAULT_CHANGE_DATE,
            "changeDate.greaterThanOrEqual=" + UPDATED_CHANGE_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate is less than or equal to
        defaultSkillUpgradeRecordFiltering(
            "changeDate.lessThanOrEqual=" + DEFAULT_CHANGE_DATE,
            "changeDate.lessThanOrEqual=" + SMALLER_CHANGE_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate is less than
        defaultSkillUpgradeRecordFiltering("changeDate.lessThan=" + UPDATED_CHANGE_DATE, "changeDate.lessThan=" + DEFAULT_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByChangeDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where changeDate is greater than
        defaultSkillUpgradeRecordFiltering(
            "changeDate.greaterThan=" + SMALLER_CHANGE_DATE,
            "changeDate.greaterThan=" + DEFAULT_CHANGE_DATE
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where reason equals to
        defaultSkillUpgradeRecordFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where reason in
        defaultSkillUpgradeRecordFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where reason is not null
        defaultSkillUpgradeRecordFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where reason contains
        defaultSkillUpgradeRecordFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where reason does not contain
        defaultSkillUpgradeRecordFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByBeforeLevelLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where beforeLevelLabel equals to
        defaultSkillUpgradeRecordFiltering(
            "beforeLevelLabel.equals=" + DEFAULT_BEFORE_LEVEL_LABEL,
            "beforeLevelLabel.equals=" + UPDATED_BEFORE_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByBeforeLevelLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where beforeLevelLabel in
        defaultSkillUpgradeRecordFiltering(
            "beforeLevelLabel.in=" + DEFAULT_BEFORE_LEVEL_LABEL + "," + UPDATED_BEFORE_LEVEL_LABEL,
            "beforeLevelLabel.in=" + UPDATED_BEFORE_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByBeforeLevelLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where beforeLevelLabel is not null
        defaultSkillUpgradeRecordFiltering("beforeLevelLabel.specified=true", "beforeLevelLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByBeforeLevelLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where beforeLevelLabel contains
        defaultSkillUpgradeRecordFiltering(
            "beforeLevelLabel.contains=" + DEFAULT_BEFORE_LEVEL_LABEL,
            "beforeLevelLabel.contains=" + UPDATED_BEFORE_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByBeforeLevelLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where beforeLevelLabel does not contain
        defaultSkillUpgradeRecordFiltering(
            "beforeLevelLabel.doesNotContain=" + UPDATED_BEFORE_LEVEL_LABEL,
            "beforeLevelLabel.doesNotContain=" + DEFAULT_BEFORE_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAfterLevelLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where afterLevelLabel equals to
        defaultSkillUpgradeRecordFiltering(
            "afterLevelLabel.equals=" + DEFAULT_AFTER_LEVEL_LABEL,
            "afterLevelLabel.equals=" + UPDATED_AFTER_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAfterLevelLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where afterLevelLabel in
        defaultSkillUpgradeRecordFiltering(
            "afterLevelLabel.in=" + DEFAULT_AFTER_LEVEL_LABEL + "," + UPDATED_AFTER_LEVEL_LABEL,
            "afterLevelLabel.in=" + UPDATED_AFTER_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAfterLevelLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where afterLevelLabel is not null
        defaultSkillUpgradeRecordFiltering("afterLevelLabel.specified=true", "afterLevelLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAfterLevelLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where afterLevelLabel contains
        defaultSkillUpgradeRecordFiltering(
            "afterLevelLabel.contains=" + DEFAULT_AFTER_LEVEL_LABEL,
            "afterLevelLabel.contains=" + UPDATED_AFTER_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAfterLevelLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        // Get all the skillUpgradeRecordList where afterLevelLabel does not contain
        defaultSkillUpgradeRecordFiltering(
            "afterLevelLabel.doesNotContain=" + UPDATED_AFTER_LEVEL_LABEL,
            "afterLevelLabel.doesNotContain=" + DEFAULT_AFTER_LEVEL_LABEL
        );
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
            person = PersonResourceIT.createEntity();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        skillUpgradeRecord.setPerson(person);
        skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
        Long personId = person.getId();
        // Get all the skillUpgradeRecordList where person equals to personId
        defaultSkillUpgradeRecordShouldBeFound("personId.equals=" + personId);

        // Get all the skillUpgradeRecordList where person equals to (personId + 1)
        defaultSkillUpgradeRecordShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsBySkillIsEqualToSomething() throws Exception {
        Skill skill;
        if (TestUtil.findAll(em, Skill.class).isEmpty()) {
            skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
            skill = SkillResourceIT.createEntity();
        } else {
            skill = TestUtil.findAll(em, Skill.class).get(0);
        }
        em.persist(skill);
        em.flush();
        skillUpgradeRecord.setSkill(skill);
        skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
        Long skillId = skill.getId();
        // Get all the skillUpgradeRecordList where skill equals to skillId
        defaultSkillUpgradeRecordShouldBeFound("skillId.equals=" + skillId);

        // Get all the skillUpgradeRecordList where skill equals to (skillId + 1)
        defaultSkillUpgradeRecordShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByOldLevelIsEqualToSomething() throws Exception {
        SkillLevel oldLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
            oldLevel = SkillLevelResourceIT.createEntity();
        } else {
            oldLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(oldLevel);
        em.flush();
        skillUpgradeRecord.setOldLevel(oldLevel);
        skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
        Long oldLevelId = oldLevel.getId();
        // Get all the skillUpgradeRecordList where oldLevel equals to oldLevelId
        defaultSkillUpgradeRecordShouldBeFound("oldLevelId.equals=" + oldLevelId);

        // Get all the skillUpgradeRecordList where oldLevel equals to (oldLevelId + 1)
        defaultSkillUpgradeRecordShouldNotBeFound("oldLevelId.equals=" + (oldLevelId + 1));
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByNewLevelIsEqualToSomething() throws Exception {
        SkillLevel newLevel;
        if (TestUtil.findAll(em, SkillLevel.class).isEmpty()) {
            skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
            newLevel = SkillLevelResourceIT.createEntity();
        } else {
            newLevel = TestUtil.findAll(em, SkillLevel.class).get(0);
        }
        em.persist(newLevel);
        em.flush();
        skillUpgradeRecord.setNewLevel(newLevel);
        skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
        Long newLevelId = newLevel.getId();
        // Get all the skillUpgradeRecordList where newLevel equals to newLevelId
        defaultSkillUpgradeRecordShouldBeFound("newLevelId.equals=" + newLevelId);

        // Get all the skillUpgradeRecordList where newLevel equals to (newLevelId + 1)
        defaultSkillUpgradeRecordShouldNotBeFound("newLevelId.equals=" + (newLevelId + 1));
    }

    @Test
    @Transactional
    void getAllSkillUpgradeRecordsByAssessorIsEqualToSomething() throws Exception {
        Person assessor;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
            assessor = PersonResourceIT.createEntity();
        } else {
            assessor = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(assessor);
        em.flush();
        skillUpgradeRecord.setAssessor(assessor);
        skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);
        Long assessorId = assessor.getId();
        // Get all the skillUpgradeRecordList where assessor equals to assessorId
        defaultSkillUpgradeRecordShouldBeFound("assessorId.equals=" + assessorId);

        // Get all the skillUpgradeRecordList where assessor equals to (assessorId + 1)
        defaultSkillUpgradeRecordShouldNotBeFound("assessorId.equals=" + (assessorId + 1));
    }

    private void defaultSkillUpgradeRecordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSkillUpgradeRecordShouldBeFound(shouldBeFound);
        defaultSkillUpgradeRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkillUpgradeRecordShouldBeFound(String filter) throws Exception {
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillUpgradeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeType").value(hasItem(DEFAULT_CHANGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(DEFAULT_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].beforeLevelLabel").value(hasItem(DEFAULT_BEFORE_LEVEL_LABEL)))
            .andExpect(jsonPath("$.[*].afterLevelLabel").value(hasItem(DEFAULT_AFTER_LEVEL_LABEL)))
            .andExpect(jsonPath("$.[*].evidence").value(hasItem(DEFAULT_EVIDENCE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkillUpgradeRecordShouldNotBeFound(String filter) throws Exception {
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkillUpgradeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSkillUpgradeRecord() throws Exception {
        // Get the skillUpgradeRecord
        restSkillUpgradeRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkillUpgradeRecord() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillUpgradeRecord
        SkillUpgradeRecord updatedSkillUpgradeRecord = skillUpgradeRecordRepository.findById(skillUpgradeRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkillUpgradeRecord are not directly saved in db
        em.detach(updatedSkillUpgradeRecord);
        updatedSkillUpgradeRecord
            .changeType(UPDATED_CHANGE_TYPE)
            .changeDate(UPDATED_CHANGE_DATE)
            .reason(UPDATED_REASON)
            .beforeLevelLabel(UPDATED_BEFORE_LEVEL_LABEL)
            .afterLevelLabel(UPDATED_AFTER_LEVEL_LABEL)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(updatedSkillUpgradeRecord);

        restSkillUpgradeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillUpgradeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillUpgradeRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillUpgradeRecordToMatchAllProperties(updatedSkillUpgradeRecord);
    }

    @Test
    @Transactional
    void putNonExistingSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillUpgradeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillUpgradeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillUpgradeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillUpgradeRecordWithPatch() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillUpgradeRecord using partial update
        SkillUpgradeRecord partialUpdatedSkillUpgradeRecord = new SkillUpgradeRecord();
        partialUpdatedSkillUpgradeRecord.setId(skillUpgradeRecord.getId());

        partialUpdatedSkillUpgradeRecord.changeDate(UPDATED_CHANGE_DATE).reason(UPDATED_REASON).evidence(UPDATED_EVIDENCE);

        restSkillUpgradeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillUpgradeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillUpgradeRecord))
            )
            .andExpect(status().isOk());

        // Validate the SkillUpgradeRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpgradeRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSkillUpgradeRecord, skillUpgradeRecord),
            getPersistedSkillUpgradeRecord(skillUpgradeRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateSkillUpgradeRecordWithPatch() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillUpgradeRecord using partial update
        SkillUpgradeRecord partialUpdatedSkillUpgradeRecord = new SkillUpgradeRecord();
        partialUpdatedSkillUpgradeRecord.setId(skillUpgradeRecord.getId());

        partialUpdatedSkillUpgradeRecord
            .changeType(UPDATED_CHANGE_TYPE)
            .changeDate(UPDATED_CHANGE_DATE)
            .reason(UPDATED_REASON)
            .beforeLevelLabel(UPDATED_BEFORE_LEVEL_LABEL)
            .afterLevelLabel(UPDATED_AFTER_LEVEL_LABEL)
            .evidence(UPDATED_EVIDENCE)
            .comment(UPDATED_COMMENT);

        restSkillUpgradeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillUpgradeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillUpgradeRecord))
            )
            .andExpect(status().isOk());

        // Validate the SkillUpgradeRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpgradeRecordUpdatableFieldsEquals(
            partialUpdatedSkillUpgradeRecord,
            getPersistedSkillUpgradeRecord(partialUpdatedSkillUpgradeRecord)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillUpgradeRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillUpgradeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillUpgradeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillUpgradeRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillUpgradeRecord.setId(longCount.incrementAndGet());

        // Create the SkillUpgradeRecord
        SkillUpgradeRecordDTO skillUpgradeRecordDTO = skillUpgradeRecordMapper.toDto(skillUpgradeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillUpgradeRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillUpgradeRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillUpgradeRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillUpgradeRecord() throws Exception {
        // Initialize the database
        insertedSkillUpgradeRecord = skillUpgradeRecordRepository.saveAndFlush(skillUpgradeRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skillUpgradeRecord
        restSkillUpgradeRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillUpgradeRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillUpgradeRecordRepository.count();
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

    protected SkillUpgradeRecord getPersistedSkillUpgradeRecord(SkillUpgradeRecord skillUpgradeRecord) {
        return skillUpgradeRecordRepository.findById(skillUpgradeRecord.getId()).orElseThrow();
    }

    protected void assertPersistedSkillUpgradeRecordToMatchAllProperties(SkillUpgradeRecord expectedSkillUpgradeRecord) {
        assertSkillUpgradeRecordAllPropertiesEquals(expectedSkillUpgradeRecord, getPersistedSkillUpgradeRecord(expectedSkillUpgradeRecord));
    }

    protected void assertPersistedSkillUpgradeRecordToMatchUpdatableProperties(SkillUpgradeRecord expectedSkillUpgradeRecord) {
        assertSkillUpgradeRecordAllUpdatablePropertiesEquals(
            expectedSkillUpgradeRecord,
            getPersistedSkillUpgradeRecord(expectedSkillUpgradeRecord)
        );
    }
}
