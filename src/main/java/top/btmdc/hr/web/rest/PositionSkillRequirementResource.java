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
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.service.PositionSkillRequirementQueryService;
import top.btmdc.hr.service.PositionSkillRequirementService;
import top.btmdc.hr.service.criteria.PositionSkillRequirementCriteria;
import top.btmdc.hr.service.dto.PositionSkillRequirementDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PositionSkillRequirement}.
 */
@RestController
@RequestMapping("/api/position-skill-requirements")
public class PositionSkillRequirementResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionSkillRequirementResource.class);

    private static final String ENTITY_NAME = "positionSkillRequirement";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PositionSkillRequirementService positionSkillRequirementService;

    private final PositionSkillRequirementRepository positionSkillRequirementRepository;

    private final PositionSkillRequirementQueryService positionSkillRequirementQueryService;

    public PositionSkillRequirementResource(
        PositionSkillRequirementService positionSkillRequirementService,
        PositionSkillRequirementRepository positionSkillRequirementRepository,
        PositionSkillRequirementQueryService positionSkillRequirementQueryService
    ) {
        this.positionSkillRequirementService = positionSkillRequirementService;
        this.positionSkillRequirementRepository = positionSkillRequirementRepository;
        this.positionSkillRequirementQueryService = positionSkillRequirementQueryService;
    }

    /**
     * {@code POST  /position-skill-requirements} : Create a new positionSkillRequirement.
     *
     * @param positionSkillRequirementDTO the positionSkillRequirementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionSkillRequirementDTO, or with status {@code 400 (Bad Request)} if the positionSkillRequirement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PositionSkillRequirementDTO> createPositionSkillRequirement(
        @Valid @RequestBody PositionSkillRequirementDTO positionSkillRequirementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PositionSkillRequirement : {}", positionSkillRequirementDTO);
        if (positionSkillRequirementDTO.getId() != null) {
            throw new BadRequestAlertException("A new positionSkillRequirement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        positionSkillRequirementDTO = positionSkillRequirementService.save(positionSkillRequirementDTO);
        return ResponseEntity.created(new URI("/api/position-skill-requirements/" + positionSkillRequirementDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionSkillRequirementDTO.getId().toString())
            )
            .body(positionSkillRequirementDTO);
    }

    /**
     * {@code PUT  /position-skill-requirements/:id} : Updates an existing positionSkillRequirement.
     *
     * @param id the id of the positionSkillRequirementDTO to save.
     * @param positionSkillRequirementDTO the positionSkillRequirementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionSkillRequirementDTO,
     * or with status {@code 400 (Bad Request)} if the positionSkillRequirementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionSkillRequirementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionSkillRequirementDTO> updatePositionSkillRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionSkillRequirementDTO positionSkillRequirementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PositionSkillRequirement : {}, {}", id, positionSkillRequirementDTO);
        if (positionSkillRequirementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionSkillRequirementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionSkillRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        positionSkillRequirementDTO = positionSkillRequirementService.update(positionSkillRequirementDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionSkillRequirementDTO.getId().toString())
            )
            .body(positionSkillRequirementDTO);
    }

    /**
     * {@code PATCH  /position-skill-requirements/:id} : Partial updates given fields of an existing positionSkillRequirement, field will ignore if it is null
     *
     * @param id the id of the positionSkillRequirementDTO to save.
     * @param positionSkillRequirementDTO the positionSkillRequirementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionSkillRequirementDTO,
     * or with status {@code 400 (Bad Request)} if the positionSkillRequirementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionSkillRequirementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionSkillRequirementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionSkillRequirementDTO> partialUpdatePositionSkillRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionSkillRequirementDTO positionSkillRequirementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PositionSkillRequirement partially : {}, {}", id, positionSkillRequirementDTO);
        if (positionSkillRequirementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionSkillRequirementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionSkillRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionSkillRequirementDTO> result = positionSkillRequirementService.partialUpdate(positionSkillRequirementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionSkillRequirementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /position-skill-requirements} : get all the Position Skill Requirements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Position Skill Requirements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PositionSkillRequirementDTO>> getAllPositionSkillRequirements(
        PositionSkillRequirementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PositionSkillRequirements by criteria: {}", criteria);

        Page<PositionSkillRequirementDTO> page = positionSkillRequirementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /position-skill-requirements/count} : count all the positionSkillRequirements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPositionSkillRequirements(PositionSkillRequirementCriteria criteria) {
        LOG.debug("REST request to count PositionSkillRequirements by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionSkillRequirementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /position-skill-requirements/:id} : get the "id" positionSkillRequirement.
     *
     * @param id the id of the positionSkillRequirementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionSkillRequirementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionSkillRequirementDTO> getPositionSkillRequirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PositionSkillRequirement : {}", id);
        Optional<PositionSkillRequirementDTO> positionSkillRequirementDTO = positionSkillRequirementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionSkillRequirementDTO);
    }

    /**
     * {@code DELETE  /position-skill-requirements/:id} : delete the "id" positionSkillRequirement.
     *
     * @param id the id of the positionSkillRequirementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionSkillRequirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PositionSkillRequirement : {}", id);
        positionSkillRequirementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
