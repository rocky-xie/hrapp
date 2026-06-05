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
import top.btmdc.hr.repository.ImprovementPlanRepository;
import top.btmdc.hr.service.ImprovementPlanQueryService;
import top.btmdc.hr.service.ImprovementPlanService;
import top.btmdc.hr.service.criteria.ImprovementPlanCriteria;
import top.btmdc.hr.service.dto.ImprovementPlanDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.ImprovementPlan}.
 */
@RestController
@RequestMapping("/api/improvement-plans")
public class ImprovementPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImprovementPlanResource.class);

    private static final String ENTITY_NAME = "improvementPlan";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final ImprovementPlanService improvementPlanService;

    private final ImprovementPlanRepository improvementPlanRepository;

    private final ImprovementPlanQueryService improvementPlanQueryService;

    public ImprovementPlanResource(
        ImprovementPlanService improvementPlanService,
        ImprovementPlanRepository improvementPlanRepository,
        ImprovementPlanQueryService improvementPlanQueryService
    ) {
        this.improvementPlanService = improvementPlanService;
        this.improvementPlanRepository = improvementPlanRepository;
        this.improvementPlanQueryService = improvementPlanQueryService;
    }

    /**
     * {@code POST  /improvement-plans} : Create a new improvementPlan.
     *
     * @param improvementPlanDTO the improvementPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new improvementPlanDTO, or with status {@code 400 (Bad Request)} if the improvementPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImprovementPlanDTO> createImprovementPlan(@Valid @RequestBody ImprovementPlanDTO improvementPlanDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ImprovementPlan : {}", improvementPlanDTO);
        if (improvementPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new improvementPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        improvementPlanDTO = improvementPlanService.save(improvementPlanDTO);
        return ResponseEntity.created(new URI("/api/improvement-plans/" + improvementPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, improvementPlanDTO.getId().toString()))
            .body(improvementPlanDTO);
    }

    /**
     * {@code PUT  /improvement-plans/:id} : Updates an existing improvementPlan.
     *
     * @param id the id of the improvementPlanDTO to save.
     * @param improvementPlanDTO the improvementPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated improvementPlanDTO,
     * or with status {@code 400 (Bad Request)} if the improvementPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the improvementPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImprovementPlanDTO> updateImprovementPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImprovementPlanDTO improvementPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImprovementPlan : {}, {}", id, improvementPlanDTO);
        if (improvementPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, improvementPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!improvementPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        improvementPlanDTO = improvementPlanService.update(improvementPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, improvementPlanDTO.getId().toString()))
            .body(improvementPlanDTO);
    }

    /**
     * {@code PATCH  /improvement-plans/:id} : Partial updates given fields of an existing improvementPlan, field will ignore if it is null
     *
     * @param id the id of the improvementPlanDTO to save.
     * @param improvementPlanDTO the improvementPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated improvementPlanDTO,
     * or with status {@code 400 (Bad Request)} if the improvementPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the improvementPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the improvementPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImprovementPlanDTO> partialUpdateImprovementPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImprovementPlanDTO improvementPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImprovementPlan partially : {}, {}", id, improvementPlanDTO);
        if (improvementPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, improvementPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!improvementPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImprovementPlanDTO> result = improvementPlanService.partialUpdate(improvementPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, improvementPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /improvement-plans} : get all the Improvement Plans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Improvement Plans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImprovementPlanDTO>> getAllImprovementPlans(
        ImprovementPlanCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImprovementPlans by criteria: {}", criteria);

        Page<ImprovementPlanDTO> page = improvementPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /improvement-plans/count} : count all the improvementPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImprovementPlans(ImprovementPlanCriteria criteria) {
        LOG.debug("REST request to count ImprovementPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(improvementPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /improvement-plans/:id} : get the "id" improvementPlan.
     *
     * @param id the id of the improvementPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the improvementPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImprovementPlanDTO> getImprovementPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImprovementPlan : {}", id);
        Optional<ImprovementPlanDTO> improvementPlanDTO = improvementPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(improvementPlanDTO);
    }

    /**
     * {@code DELETE  /improvement-plans/:id} : delete the "id" improvementPlan.
     *
     * @param id the id of the improvementPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImprovementPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImprovementPlan : {}", id);
        improvementPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
