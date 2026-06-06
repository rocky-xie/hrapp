package top.btmdc.hr.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.ActionItemService;
import top.btmdc.hr.service.StaffSubstitutionQueryService;
import top.btmdc.hr.service.StaffSubstitutionService;
import top.btmdc.hr.service.criteria.StaffSubstitutionCriteria;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.StaffSubstitution}.
 */
@RestController
@RequestMapping("/api/staff-substitutions")
public class StaffSubstitutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(StaffSubstitutionResource.class);

    private static final String ENTITY_NAME = "staffSubstitution";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final StaffSubstitutionService staffSubstitutionService;

    private final StaffSubstitutionRepository staffSubstitutionRepository;

    private final StaffSubstitutionQueryService staffSubstitutionQueryService;

    private final ActionItemService actionItemService;

    public StaffSubstitutionResource(
        StaffSubstitutionService staffSubstitutionService,
        StaffSubstitutionRepository staffSubstitutionRepository,
        StaffSubstitutionQueryService staffSubstitutionQueryService,
        ActionItemService actionItemService
    ) {
        this.staffSubstitutionService = staffSubstitutionService;
        this.staffSubstitutionRepository = staffSubstitutionRepository;
        this.staffSubstitutionQueryService = staffSubstitutionQueryService;
        this.actionItemService = actionItemService;
    }

    /**
     * {@code POST  /staff-substitutions} : Create a new staffSubstitution.
     *
     * @param staffSubstitutionDTO the staffSubstitutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staffSubstitutionDTO, or with status {@code 400 (Bad Request)} if the staffSubstitution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StaffSubstitutionDTO> createStaffSubstitution(@Valid @RequestBody StaffSubstitutionDTO staffSubstitutionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StaffSubstitution : {}", staffSubstitutionDTO);
        if (staffSubstitutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new staffSubstitution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (
            staffSubstitutionDTO.getPosition() != null &&
            staffSubstitutionDTO.getPosition().getId() != null &&
            staffSubstitutionDTO.getCandidatePerson() != null &&
            staffSubstitutionDTO.getCandidatePerson().getId() != null &&
            staffSubstitutionRepository
                .findOneByPositionIdAndCandidatePersonId(
                    staffSubstitutionDTO.getPosition().getId(),
                    staffSubstitutionDTO.getCandidatePerson().getId()
                )
                .isPresent()
        ) {
            throw new BadRequestAlertException(
                "A StaffSubstitution record already exists for this position and candidate person",
                ENTITY_NAME,
                "duplicatePositionCandidate"
            );
        }
        staffSubstitutionDTO = staffSubstitutionService.save(staffSubstitutionDTO);
        return ResponseEntity.created(new URI("/api/staff-substitutions/" + staffSubstitutionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, staffSubstitutionDTO.getId().toString()))
            .body(staffSubstitutionDTO);
    }

    /**
     * {@code POST  /staff-substitutions/calculate} : calculate and persist a position-oriented substitution evaluation.
     *
     * @param positionId the target position id.
     * @param candidatePersonId the candidate person id.
     * @param thresholdRate optional threshold rate, defaults to 80.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the calculated StaffSubstitutionDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calculate")
    public ResponseEntity<StaffSubstitutionDTO> calculateStaffSubstitution(
        @RequestParam Long positionId,
        @RequestParam Long candidatePersonId,
        @RequestParam(required = false) BigDecimal thresholdRate
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to calculate StaffSubstitution : positionId={}, candidatePersonId={}, thresholdRate={}",
            positionId,
            candidatePersonId,
            thresholdRate
        );
        StaffSubstitutionDTO staffSubstitutionDTO = staffSubstitutionService.calculate(positionId, candidatePersonId, thresholdRate);
        if (Boolean.FALSE.equals(staffSubstitutionDTO.getSubstitutable())) {
            String posName = staffSubstitutionDTO.getPosition() != null ? staffSubstitutionDTO.getPosition().getPositionName() : "unknown";
            actionItemService.createFromSource(
                ActionSourceType.SUBSTITUTION_GAP,
                staffSubstitutionDTO.getId(),
                "STAFF_SUBSTITUTION",
                "Insufficient substitution coverage for position '" + posName + "' by candidate",
                null,
                ActionPriority.P1_HIGH
            );
        }
        return ResponseEntity.created(new URI("/api/staff-substitutions/" + staffSubstitutionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, staffSubstitutionDTO.getId().toString()))
            .body(staffSubstitutionDTO);
    }

    /**
     * {@code PUT  /staff-substitutions/:id} : Updates an existing staffSubstitution.
     *
     * @param id the id of the staffSubstitutionDTO to save.
     * @param staffSubstitutionDTO the staffSubstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffSubstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the staffSubstitutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staffSubstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaffSubstitutionDTO> updateStaffSubstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaffSubstitutionDTO staffSubstitutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StaffSubstitution : {}, {}", id, staffSubstitutionDTO);
        if (staffSubstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffSubstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffSubstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        staffSubstitutionDTO = staffSubstitutionService.update(staffSubstitutionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staffSubstitutionDTO.getId().toString()))
            .body(staffSubstitutionDTO);
    }

    /**
     * {@code PATCH  /staff-substitutions/:id} : Partial updates given fields of an existing staffSubstitution, field will ignore if it is null
     *
     * @param id the id of the staffSubstitutionDTO to save.
     * @param staffSubstitutionDTO the staffSubstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffSubstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the staffSubstitutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the staffSubstitutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the staffSubstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaffSubstitutionDTO> partialUpdateStaffSubstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaffSubstitutionDTO staffSubstitutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StaffSubstitution partially : {}, {}", id, staffSubstitutionDTO);
        if (staffSubstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffSubstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffSubstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaffSubstitutionDTO> result = staffSubstitutionService.partialUpdate(staffSubstitutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staffSubstitutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /staff-substitutions} : get all the Staff Substitutions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Staff Substitutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StaffSubstitutionDTO>> getAllStaffSubstitutions(
        StaffSubstitutionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StaffSubstitutions by criteria: {}", criteria);

        Page<StaffSubstitutionDTO> page = staffSubstitutionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /staff-substitutions/count} : count all the staffSubstitutions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStaffSubstitutions(StaffSubstitutionCriteria criteria) {
        LOG.debug("REST request to count StaffSubstitutions by criteria: {}", criteria);
        return ResponseEntity.ok().body(staffSubstitutionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /staff-substitutions/:id} : get the "id" staffSubstitution.
     *
     * @param id the id of the staffSubstitutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staffSubstitutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffSubstitutionDTO> getStaffSubstitution(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StaffSubstitution : {}", id);
        Optional<StaffSubstitutionDTO> staffSubstitutionDTO = staffSubstitutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staffSubstitutionDTO);
    }

    /**
     * {@code DELETE  /staff-substitutions/:id} : delete the "id" staffSubstitution.
     *
     * @param id the id of the staffSubstitutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaffSubstitution(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StaffSubstitution : {}", id);
        staffSubstitutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
