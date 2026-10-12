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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import top.btmdc.hr.repository.DispatchAssignmentRepository;
import top.btmdc.hr.service.DispatchAssignmentQueryService;
import top.btmdc.hr.service.DispatchAssignmentService;
import top.btmdc.hr.service.criteria.DispatchAssignmentCriteria;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.DispatchAssignment}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/dispatch-assignments")
public class DispatchAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchAssignmentResource.class);

    private static final String ENTITY_NAME = "dispatchAssignment";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final DispatchAssignmentService dispatchAssignmentService;

    private final DispatchAssignmentRepository dispatchAssignmentRepository;

    private final DispatchAssignmentQueryService dispatchAssignmentQueryService;

    public DispatchAssignmentResource(
        DispatchAssignmentService dispatchAssignmentService,
        DispatchAssignmentRepository dispatchAssignmentRepository,
        DispatchAssignmentQueryService dispatchAssignmentQueryService
    ) {
        this.dispatchAssignmentService = dispatchAssignmentService;
        this.dispatchAssignmentRepository = dispatchAssignmentRepository;
        this.dispatchAssignmentQueryService = dispatchAssignmentQueryService;
    }

    /**
     * {@code POST  /dispatch-assignments} : Create a new dispatchAssignment.
     *
     * @param dispatchAssignmentDTO the dispatchAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispatchAssignmentDTO, or with status {@code 400 (Bad Request)} if the dispatchAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DispatchAssignmentDTO> createDispatchAssignment(@Valid @RequestBody DispatchAssignmentDTO dispatchAssignmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DispatchAssignment : {}", dispatchAssignmentDTO);
        if (dispatchAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new dispatchAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dispatchAssignmentDTO = dispatchAssignmentService.save(dispatchAssignmentDTO);
        return ResponseEntity.created(new URI("/api/dispatch-assignments/" + dispatchAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dispatchAssignmentDTO.getId().toString()))
            .body(dispatchAssignmentDTO);
    }

    /**
     * {@code PUT  /dispatch-assignments/:id} : Updates an existing dispatchAssignment.
     *
     * @param id the id of the dispatchAssignmentDTO to save.
     * @param dispatchAssignmentDTO the dispatchAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the dispatchAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispatchAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DispatchAssignmentDTO> updateDispatchAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DispatchAssignmentDTO dispatchAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DispatchAssignment : {}, {}", id, dispatchAssignmentDTO);
        if (dispatchAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dispatchAssignmentDTO = dispatchAssignmentService.update(dispatchAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatchAssignmentDTO.getId().toString()))
            .body(dispatchAssignmentDTO);
    }

    /**
     * {@code PATCH  /dispatch-assignments/:id} : Partial updates given fields of an existing dispatchAssignment, field will ignore if it is null
     *
     * @param id the id of the dispatchAssignmentDTO to save.
     * @param dispatchAssignmentDTO the dispatchAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the dispatchAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dispatchAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispatchAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DispatchAssignmentDTO> partialUpdateDispatchAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DispatchAssignmentDTO dispatchAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DispatchAssignment partially : {}, {}", id, dispatchAssignmentDTO);
        if (dispatchAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DispatchAssignmentDTO> result = dispatchAssignmentService.partialUpdate(dispatchAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatchAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dispatch-assignments} : get all the Dispatch Assignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Dispatch Assignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DispatchAssignmentDTO>> getAllDispatchAssignments(
        DispatchAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DispatchAssignments by criteria: {}", criteria);

        Page<DispatchAssignmentDTO> page = dispatchAssignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispatch-assignments/count} : count all the dispatchAssignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDispatchAssignments(DispatchAssignmentCriteria criteria) {
        LOG.debug("REST request to count DispatchAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(dispatchAssignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dispatch-assignments/:id} : get the "id" dispatchAssignment.
     *
     * @param id the id of the dispatchAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispatchAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DispatchAssignmentDTO> getDispatchAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DispatchAssignment : {}", id);
        Optional<DispatchAssignmentDTO> dispatchAssignmentDTO = dispatchAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispatchAssignmentDTO);
    }

    /**
     * {@code DELETE  /dispatch-assignments/:id} : delete the "id" dispatchAssignment.
     *
     * @param id the id of the dispatchAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatchAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DispatchAssignment : {}", id);
        dispatchAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
