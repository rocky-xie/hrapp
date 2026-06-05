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
import top.btmdc.hr.repository.SkillAssessmentRepository;
import top.btmdc.hr.service.SkillAssessmentQueryService;
import top.btmdc.hr.service.SkillAssessmentService;
import top.btmdc.hr.service.criteria.SkillAssessmentCriteria;
import top.btmdc.hr.service.dto.SkillAssessmentDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.SkillAssessment}.
 */
@RestController
@RequestMapping("/api/skill-assessments")
public class SkillAssessmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(SkillAssessmentResource.class);

    private static final String ENTITY_NAME = "skillAssessment";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final SkillAssessmentService skillAssessmentService;

    private final SkillAssessmentRepository skillAssessmentRepository;

    private final SkillAssessmentQueryService skillAssessmentQueryService;

    public SkillAssessmentResource(
        SkillAssessmentService skillAssessmentService,
        SkillAssessmentRepository skillAssessmentRepository,
        SkillAssessmentQueryService skillAssessmentQueryService
    ) {
        this.skillAssessmentService = skillAssessmentService;
        this.skillAssessmentRepository = skillAssessmentRepository;
        this.skillAssessmentQueryService = skillAssessmentQueryService;
    }

    /**
     * {@code POST  /skill-assessments} : Create a new skillAssessment.
     *
     * @param skillAssessmentDTO the skillAssessmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillAssessmentDTO, or with status {@code 400 (Bad Request)} if the skillAssessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SkillAssessmentDTO> createSkillAssessment(@Valid @RequestBody SkillAssessmentDTO skillAssessmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SkillAssessment : {}", skillAssessmentDTO);
        if (skillAssessmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new skillAssessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillAssessmentDTO = skillAssessmentService.save(skillAssessmentDTO);
        return ResponseEntity.created(new URI("/api/skill-assessments/" + skillAssessmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, skillAssessmentDTO.getId().toString()))
            .body(skillAssessmentDTO);
    }

    /**
     * {@code PUT  /skill-assessments/:id} : Updates an existing skillAssessment.
     *
     * @param id the id of the skillAssessmentDTO to save.
     * @param skillAssessmentDTO the skillAssessmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillAssessmentDTO,
     * or with status {@code 400 (Bad Request)} if the skillAssessmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillAssessmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillAssessmentDTO> updateSkillAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SkillAssessmentDTO skillAssessmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SkillAssessment : {}, {}", id, skillAssessmentDTO);
        if (skillAssessmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillAssessmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillAssessmentDTO = skillAssessmentService.update(skillAssessmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillAssessmentDTO.getId().toString()))
            .body(skillAssessmentDTO);
    }

    /**
     * {@code PATCH  /skill-assessments/:id} : Partial updates given fields of an existing skillAssessment, field will ignore if it is null
     *
     * @param id the id of the skillAssessmentDTO to save.
     * @param skillAssessmentDTO the skillAssessmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillAssessmentDTO,
     * or with status {@code 400 (Bad Request)} if the skillAssessmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the skillAssessmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillAssessmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SkillAssessmentDTO> partialUpdateSkillAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SkillAssessmentDTO skillAssessmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SkillAssessment partially : {}, {}", id, skillAssessmentDTO);
        if (skillAssessmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillAssessmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillAssessmentDTO> result = skillAssessmentService.partialUpdate(skillAssessmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillAssessmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /skill-assessments} : get all the Skill Assessments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Skill Assessments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SkillAssessmentDTO>> getAllSkillAssessments(
        SkillAssessmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SkillAssessments by criteria: {}", criteria);

        Page<SkillAssessmentDTO> page = skillAssessmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skill-assessments/count} : count all the skillAssessments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSkillAssessments(SkillAssessmentCriteria criteria) {
        LOG.debug("REST request to count SkillAssessments by criteria: {}", criteria);
        return ResponseEntity.ok().body(skillAssessmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /skill-assessments/:id} : get the "id" skillAssessment.
     *
     * @param id the id of the skillAssessmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillAssessmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillAssessmentDTO> getSkillAssessment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SkillAssessment : {}", id);
        Optional<SkillAssessmentDTO> skillAssessmentDTO = skillAssessmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillAssessmentDTO);
    }

    /**
     * {@code DELETE  /skill-assessments/:id} : delete the "id" skillAssessment.
     *
     * @param id the id of the skillAssessmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillAssessment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SkillAssessment : {}", id);
        skillAssessmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
