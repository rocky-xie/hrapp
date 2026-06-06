package top.btmdc.hr.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.service.ActionItemService;
import top.btmdc.hr.service.PersonSkillQueryService;
import top.btmdc.hr.service.PersonSkillService;
import top.btmdc.hr.service.criteria.PersonSkillCriteria;
import top.btmdc.hr.service.dto.PersonSkillDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PersonSkill}.
 */
@RestController
@RequestMapping("/api/person-skills")
public class PersonSkillResource {

    private static final Logger LOG = LoggerFactory.getLogger(PersonSkillResource.class);

    private static final String ENTITY_NAME = "personSkill";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PersonSkillService personSkillService;

    private final PersonSkillRepository personSkillRepository;

    private final PersonSkillQueryService personSkillQueryService;

    private final ActionItemService actionItemService;

    public PersonSkillResource(
        PersonSkillService personSkillService,
        PersonSkillRepository personSkillRepository,
        PersonSkillQueryService personSkillQueryService,
        ActionItemService actionItemService
    ) {
        this.personSkillService = personSkillService;
        this.personSkillRepository = personSkillRepository;
        this.personSkillQueryService = personSkillQueryService;
        this.actionItemService = actionItemService;
    }

    /**
     * {@code POST  /person-skills} : Create a new personSkill.
     *
     * @param personSkillDTO the personSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personSkillDTO, or with status {@code 400 (Bad Request)} if the personSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonSkillDTO> createPersonSkill(@Valid @RequestBody PersonSkillDTO personSkillDTO) throws URISyntaxException {
        LOG.debug("REST request to save PersonSkill : {}", personSkillDTO);
        if (personSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new personSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personSkillDTO = personSkillService.save(personSkillDTO);
        String skillName = personSkillDTO.getSkill() != null ? personSkillDTO.getSkill().getSkillName() : "unknown";
        actionItemService.createFromSource(
            ActionSourceType.SKILL_REVIEW,
            personSkillDTO.getId(),
            "PERSON_SKILL",
            "Skill '" + skillName + "' recorded for person; review may be needed",
            null,
            ActionPriority.P2_MEDIUM
        );
        return ResponseEntity.created(new URI("/api/person-skills/" + personSkillDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, personSkillDTO.getId().toString()))
            .body(personSkillDTO);
    }

    /**
     * {@code PUT  /person-skills/:id} : Updates an existing personSkill.
     *
     * @param id the id of the personSkillDTO to save.
     * @param personSkillDTO the personSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personSkillDTO,
     * or with status {@code 400 (Bad Request)} if the personSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonSkillDTO> updatePersonSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonSkillDTO personSkillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PersonSkill : {}, {}", id, personSkillDTO);
        if (personSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personSkillDTO = personSkillService.update(personSkillDTO);
        String skillName = personSkillDTO.getSkill() != null ? personSkillDTO.getSkill().getSkillName() : "unknown";
        actionItemService.createFromSource(
            ActionSourceType.SKILL_REVIEW,
            personSkillDTO.getId(),
            "PERSON_SKILL",
            "Skill '" + skillName + "' updated for person; review may be needed",
            null,
            ActionPriority.P2_MEDIUM
        );
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personSkillDTO.getId().toString()))
            .body(personSkillDTO);
    }

    /**
     * {@code PATCH  /person-skills/:id} : Partial updates given fields of an existing personSkill, field will ignore if it is null
     *
     * @param id the id of the personSkillDTO to save.
     * @param personSkillDTO the personSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personSkillDTO,
     * or with status {@code 400 (Bad Request)} if the personSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonSkillDTO> partialUpdatePersonSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonSkillDTO personSkillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PersonSkill partially : {}, {}", id, personSkillDTO);
        if (personSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonSkillDTO> result = personSkillService.partialUpdate(personSkillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personSkillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /person-skills} : get all the Person Skills.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Person Skills in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PersonSkillDTO>> getAllPersonSkills(
        PersonSkillCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PersonSkills by criteria: {}", criteria);

        Page<PersonSkillDTO> page = personSkillQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-skills/count} : count all the personSkills.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPersonSkills(PersonSkillCriteria criteria) {
        LOG.debug("REST request to count PersonSkills by criteria: {}", criteria);
        return ResponseEntity.ok().body(personSkillQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-skills/:id} : get the "id" personSkill.
     *
     * @param id the id of the personSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonSkillDTO> getPersonSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PersonSkill : {}", id);
        Optional<PersonSkillDTO> personSkillDTO = personSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personSkillDTO);
    }

    /**
     * {@code DELETE  /person-skills/:id} : delete the "id" personSkill.
     *
     * @param id the id of the personSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PersonSkill : {}", id);
        personSkillService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
