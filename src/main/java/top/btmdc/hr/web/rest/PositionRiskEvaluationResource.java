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
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.service.ActionItemService;
import top.btmdc.hr.service.PositionRiskEvaluationQueryService;
import top.btmdc.hr.service.PositionRiskEvaluationService;
import top.btmdc.hr.service.criteria.PositionRiskEvaluationCriteria;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PositionRiskEvaluation}.
 */
@RestController
@RequestMapping("/api/position-risk-evaluations")
public class PositionRiskEvaluationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskEvaluationResource.class);

    private static final String ENTITY_NAME = "positionRiskEvaluation";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PositionRiskEvaluationService positionRiskEvaluationService;

    private final PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    private final PositionRiskEvaluationQueryService positionRiskEvaluationQueryService;

    private final ActionItemService actionItemService;

    public PositionRiskEvaluationResource(
        PositionRiskEvaluationService positionRiskEvaluationService,
        PositionRiskEvaluationRepository positionRiskEvaluationRepository,
        PositionRiskEvaluationQueryService positionRiskEvaluationQueryService,
        ActionItemService actionItemService
    ) {
        this.positionRiskEvaluationService = positionRiskEvaluationService;
        this.positionRiskEvaluationRepository = positionRiskEvaluationRepository;
        this.positionRiskEvaluationQueryService = positionRiskEvaluationQueryService;
        this.actionItemService = actionItemService;
    }

    /**
     * {@code POST  /position-risk-evaluations} : Create a new positionRiskEvaluation.
     *
     * @param positionRiskEvaluationDTO the positionRiskEvaluationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionRiskEvaluationDTO, or with status {@code 400 (Bad Request)} if the positionRiskEvaluation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PositionRiskEvaluationDTO> createPositionRiskEvaluation(
        @Valid @RequestBody PositionRiskEvaluationDTO positionRiskEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PositionRiskEvaluation : {}", positionRiskEvaluationDTO);
        if (positionRiskEvaluationDTO.getId() != null) {
            throw new BadRequestAlertException("A new positionRiskEvaluation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        positionRiskEvaluationDTO = positionRiskEvaluationService.save(positionRiskEvaluationDTO);
        return ResponseEntity.created(new URI("/api/position-risk-evaluations/" + positionRiskEvaluationDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionRiskEvaluationDTO.getId().toString())
            )
            .body(positionRiskEvaluationDTO);
    }

    /**
     * {@code POST  /position-risk-evaluations/evaluate/:positionId} : evaluate and persist a position risk snapshot.
     *
     * @param positionId the target position id.
     * @param documentStatus current document status.
     * @param customerOrSystemDependency customer or system dependency level.
     * @param successionReadiness successor readiness.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the calculated PositionRiskEvaluationDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaluate/{positionId}")
    public ResponseEntity<PositionRiskEvaluationDTO> evaluatePositionRisk(
        @PathVariable("positionId") Long positionId,
        @RequestParam(required = false) DocumentStatus documentStatus,
        @RequestParam(required = false) ImportanceLevel customerOrSystemDependency,
        @RequestParam(required = false) ReadinessLevel successionReadiness,
        @RequestParam(required = false, defaultValue = "false") boolean preview
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to evaluate PositionRiskEvaluation : positionId={}, documentStatus={}, customerOrSystemDependency={}, successionReadiness={}, preview={}",
            positionId,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness,
            preview
        );
        PositionRiskEvaluationDTO positionRiskEvaluationDTO = positionRiskEvaluationService.evaluate(
            positionId,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness,
            preview
        );
        if (!preview && positionRiskEvaluationDTO.getRiskLevel() == top.btmdc.hr.domain.enumeration.RiskLevel.HIGH) {
            String posName =
                positionRiskEvaluationDTO.getPosition() != null ? positionRiskEvaluationDTO.getPosition().getPositionName() : "unknown";
            actionItemService.createFromSource(
                ActionSourceType.HIGH_RISK_POSITION,
                positionRiskEvaluationDTO.getId(),
                "POSITION_RISK_EVALUATION",
                "High risk position evaluated: " + posName,
                null,
                ActionPriority.P1_HIGH
            );
        }
        if (preview) {
            return ResponseEntity.ok().body(positionRiskEvaluationDTO);
        }
        return ResponseEntity.created(new URI("/api/position-risk-evaluations/" + positionRiskEvaluationDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionRiskEvaluationDTO.getId().toString())
            )
            .body(positionRiskEvaluationDTO);
    }

    /**
     * {@code PUT  /position-risk-evaluations/:id} : Updates an existing positionRiskEvaluation.
     *
     * @param id the id of the positionRiskEvaluationDTO to save.
     * @param positionRiskEvaluationDTO the positionRiskEvaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionRiskEvaluationDTO,
     * or with status {@code 400 (Bad Request)} if the positionRiskEvaluationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionRiskEvaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionRiskEvaluationDTO> updatePositionRiskEvaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionRiskEvaluationDTO positionRiskEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PositionRiskEvaluation : {}, {}", id, positionRiskEvaluationDTO);
        if (positionRiskEvaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionRiskEvaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRiskEvaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        positionRiskEvaluationDTO = positionRiskEvaluationService.update(positionRiskEvaluationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionRiskEvaluationDTO.getId().toString()))
            .body(positionRiskEvaluationDTO);
    }

    /**
     * {@code PATCH  /position-risk-evaluations/:id} : Partial updates given fields of an existing positionRiskEvaluation, field will ignore if it is null
     *
     * @param id the id of the positionRiskEvaluationDTO to save.
     * @param positionRiskEvaluationDTO the positionRiskEvaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionRiskEvaluationDTO,
     * or with status {@code 400 (Bad Request)} if the positionRiskEvaluationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionRiskEvaluationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionRiskEvaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionRiskEvaluationDTO> partialUpdatePositionRiskEvaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionRiskEvaluationDTO positionRiskEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PositionRiskEvaluation partially : {}, {}", id, positionRiskEvaluationDTO);
        if (positionRiskEvaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionRiskEvaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRiskEvaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionRiskEvaluationDTO> result = positionRiskEvaluationService.partialUpdate(positionRiskEvaluationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionRiskEvaluationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /position-risk-evaluations} : get all the Position Risk Evaluations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Position Risk Evaluations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PositionRiskEvaluationDTO>> getAllPositionRiskEvaluations(
        PositionRiskEvaluationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PositionRiskEvaluations by criteria: {}", criteria);

        Page<PositionRiskEvaluationDTO> page = positionRiskEvaluationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /position-risk-evaluations/count} : count all the positionRiskEvaluations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPositionRiskEvaluations(PositionRiskEvaluationCriteria criteria) {
        LOG.debug("REST request to count PositionRiskEvaluations by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionRiskEvaluationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /position-risk-evaluations/:id} : get the "id" positionRiskEvaluation.
     *
     * @param id the id of the positionRiskEvaluationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionRiskEvaluationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionRiskEvaluationDTO> getPositionRiskEvaluation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PositionRiskEvaluation : {}", id);
        Optional<PositionRiskEvaluationDTO> positionRiskEvaluationDTO = positionRiskEvaluationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionRiskEvaluationDTO);
    }

    /**
     * {@code DELETE  /position-risk-evaluations/:id} : delete the "id" positionRiskEvaluation.
     *
     * @param id the id of the positionRiskEvaluationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionRiskEvaluation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PositionRiskEvaluation : {}", id);
        positionRiskEvaluationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
