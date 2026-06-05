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
import top.btmdc.hr.repository.PositionRiskRepository;
import top.btmdc.hr.service.PositionRiskQueryService;
import top.btmdc.hr.service.PositionRiskService;
import top.btmdc.hr.service.criteria.PositionRiskCriteria;
import top.btmdc.hr.service.dto.PositionRiskDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PositionRisk}.
 */
@RestController
@RequestMapping("/api/position-risks")
public class PositionRiskResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskResource.class);

    private static final String ENTITY_NAME = "positionRisk";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PositionRiskService positionRiskService;

    private final PositionRiskRepository positionRiskRepository;

    private final PositionRiskQueryService positionRiskQueryService;

    public PositionRiskResource(
        PositionRiskService positionRiskService,
        PositionRiskRepository positionRiskRepository,
        PositionRiskQueryService positionRiskQueryService
    ) {
        this.positionRiskService = positionRiskService;
        this.positionRiskRepository = positionRiskRepository;
        this.positionRiskQueryService = positionRiskQueryService;
    }

    /**
     * {@code POST  /position-risks} : Create a new positionRisk.
     *
     * @param positionRiskDTO the positionRiskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionRiskDTO, or with status {@code 400 (Bad Request)} if the positionRisk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PositionRiskDTO> createPositionRisk(@Valid @RequestBody PositionRiskDTO positionRiskDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PositionRisk : {}", positionRiskDTO);
        if (positionRiskDTO.getId() != null) {
            throw new BadRequestAlertException("A new positionRisk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        positionRiskDTO = positionRiskService.save(positionRiskDTO);
        return ResponseEntity.created(new URI("/api/position-risks/" + positionRiskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionRiskDTO.getId().toString()))
            .body(positionRiskDTO);
    }

    /**
     * {@code PUT  /position-risks/:id} : Updates an existing positionRisk.
     *
     * @param id the id of the positionRiskDTO to save.
     * @param positionRiskDTO the positionRiskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionRiskDTO,
     * or with status {@code 400 (Bad Request)} if the positionRiskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionRiskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionRiskDTO> updatePositionRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionRiskDTO positionRiskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PositionRisk : {}, {}", id, positionRiskDTO);
        if (positionRiskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionRiskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        positionRiskDTO = positionRiskService.update(positionRiskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionRiskDTO.getId().toString()))
            .body(positionRiskDTO);
    }

    /**
     * {@code PATCH  /position-risks/:id} : Partial updates given fields of an existing positionRisk, field will ignore if it is null
     *
     * @param id the id of the positionRiskDTO to save.
     * @param positionRiskDTO the positionRiskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionRiskDTO,
     * or with status {@code 400 (Bad Request)} if the positionRiskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionRiskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionRiskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionRiskDTO> partialUpdatePositionRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionRiskDTO positionRiskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PositionRisk partially : {}, {}", id, positionRiskDTO);
        if (positionRiskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionRiskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionRiskDTO> result = positionRiskService.partialUpdate(positionRiskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionRiskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /position-risks} : get all the Position Risks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Position Risks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PositionRiskDTO>> getAllPositionRisks(
        PositionRiskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PositionRisks by criteria: {}", criteria);

        Page<PositionRiskDTO> page = positionRiskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /position-risks/count} : count all the positionRisks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPositionRisks(PositionRiskCriteria criteria) {
        LOG.debug("REST request to count PositionRisks by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionRiskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /position-risks/:id} : get the "id" positionRisk.
     *
     * @param id the id of the positionRiskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionRiskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionRiskDTO> getPositionRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PositionRisk : {}", id);
        Optional<PositionRiskDTO> positionRiskDTO = positionRiskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionRiskDTO);
    }

    /**
     * {@code DELETE  /position-risks/:id} : delete the "id" positionRisk.
     *
     * @param id the id of the positionRiskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PositionRisk : {}", id);
        positionRiskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
