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
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.service.PositionAssignmentQueryService;
import top.btmdc.hr.service.PositionAssignmentService;
import top.btmdc.hr.service.criteria.PositionAssignmentCriteria;
import top.btmdc.hr.service.dto.PositionAssignmentDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PositionAssignment}.
 */
@RestController
@RequestMapping("/api/position-assignments")
public class PositionAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionAssignmentResource.class);

    private static final String ENTITY_NAME = "positionAssignment";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PositionAssignmentService positionAssignmentService;

    private final PositionAssignmentRepository positionAssignmentRepository;

    private final PositionAssignmentQueryService positionAssignmentQueryService;

    public PositionAssignmentResource(
        PositionAssignmentService positionAssignmentService,
        PositionAssignmentRepository positionAssignmentRepository,
        PositionAssignmentQueryService positionAssignmentQueryService
    ) {
        this.positionAssignmentService = positionAssignmentService;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.positionAssignmentQueryService = positionAssignmentQueryService;
    }

    /**
     * {@code POST  /position-assignments} : Create a new positionAssignment.
     *
     * @param positionAssignmentDTO the positionAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionAssignmentDTO, or with status {@code 400 (Bad Request)} if the positionAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PositionAssignmentDTO> createPositionAssignment(@Valid @RequestBody PositionAssignmentDTO positionAssignmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PositionAssignment : {}", positionAssignmentDTO);
        if (positionAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new positionAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        positionAssignmentDTO = positionAssignmentService.save(positionAssignmentDTO);
        return ResponseEntity.created(new URI("/api/position-assignments/" + positionAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionAssignmentDTO.getId().toString()))
            .body(positionAssignmentDTO);
    }

    /**
     * {@code PUT  /position-assignments/:id} : Updates an existing positionAssignment.
     *
     * @param id the id of the positionAssignmentDTO to save.
     * @param positionAssignmentDTO the positionAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the positionAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionAssignmentDTO> updatePositionAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionAssignmentDTO positionAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PositionAssignment : {}, {}", id, positionAssignmentDTO);
        if (positionAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        positionAssignmentDTO = positionAssignmentService.update(positionAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionAssignmentDTO.getId().toString()))
            .body(positionAssignmentDTO);
    }

    /**
     * {@code PATCH  /position-assignments/:id} : Partial updates given fields of an existing positionAssignment, field will ignore if it is null
     *
     * @param id the id of the positionAssignmentDTO to save.
     * @param positionAssignmentDTO the positionAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the positionAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionAssignmentDTO> partialUpdatePositionAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionAssignmentDTO positionAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PositionAssignment partially : {}, {}", id, positionAssignmentDTO);
        if (positionAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionAssignmentDTO> result = positionAssignmentService.partialUpdate(positionAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /position-assignments} : get all the Position Assignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Position Assignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PositionAssignmentDTO>> getAllPositionAssignments(
        PositionAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PositionAssignments by criteria: {}", criteria);

        Page<PositionAssignmentDTO> page = positionAssignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /position-assignments/count} : count all the positionAssignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPositionAssignments(PositionAssignmentCriteria criteria) {
        LOG.debug("REST request to count PositionAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionAssignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /position-assignments/:id} : get the "id" positionAssignment.
     *
     * @param id the id of the positionAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionAssignmentDTO> getPositionAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PositionAssignment : {}", id);
        Optional<PositionAssignmentDTO> positionAssignmentDTO = positionAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionAssignmentDTO);
    }

    /**
     * {@code DELETE  /position-assignments/:id} : delete the "id" positionAssignment.
     *
     * @param id the id of the positionAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PositionAssignment : {}", id);
        positionAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
