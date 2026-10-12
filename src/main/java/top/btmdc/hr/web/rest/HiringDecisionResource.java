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
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.service.HiringDecisionQueryService;
import top.btmdc.hr.service.HiringDecisionService;
import top.btmdc.hr.service.criteria.HiringDecisionCriteria;
import top.btmdc.hr.service.dto.HiringDecisionDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.HiringDecision}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/hiring-decisions")
public class HiringDecisionResource {

    private static final Logger LOG = LoggerFactory.getLogger(HiringDecisionResource.class);

    private static final String ENTITY_NAME = "hiringDecision";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final HiringDecisionService hiringDecisionService;

    private final HiringDecisionRepository hiringDecisionRepository;

    private final HiringDecisionQueryService hiringDecisionQueryService;

    public HiringDecisionResource(
        HiringDecisionService hiringDecisionService,
        HiringDecisionRepository hiringDecisionRepository,
        HiringDecisionQueryService hiringDecisionQueryService
    ) {
        this.hiringDecisionService = hiringDecisionService;
        this.hiringDecisionRepository = hiringDecisionRepository;
        this.hiringDecisionQueryService = hiringDecisionQueryService;
    }

    /**
     * {@code POST  /hiring-decisions} : Create a new hiringDecision.
     *
     * @param hiringDecisionDTO the hiringDecisionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hiringDecisionDTO, or with status {@code 400 (Bad Request)} if the hiringDecision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HiringDecisionDTO> createHiringDecision(@Valid @RequestBody HiringDecisionDTO hiringDecisionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HiringDecision : {}", hiringDecisionDTO);
        if (hiringDecisionDTO.getId() != null) {
            throw new BadRequestAlertException("A new hiringDecision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hiringDecisionDTO = hiringDecisionService.save(hiringDecisionDTO);
        return ResponseEntity.created(new URI("/api/hiring-decisions/" + hiringDecisionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, hiringDecisionDTO.getId().toString()))
            .body(hiringDecisionDTO);
    }

    /**
     * {@code PUT  /hiring-decisions/:id} : Updates an existing hiringDecision.
     *
     * @param id the id of the hiringDecisionDTO to save.
     * @param hiringDecisionDTO the hiringDecisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiringDecisionDTO,
     * or with status {@code 400 (Bad Request)} if the hiringDecisionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hiringDecisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HiringDecisionDTO> updateHiringDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HiringDecisionDTO hiringDecisionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HiringDecision : {}, {}", id, hiringDecisionDTO);
        if (hiringDecisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hiringDecisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hiringDecisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hiringDecisionDTO = hiringDecisionService.update(hiringDecisionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hiringDecisionDTO.getId().toString()))
            .body(hiringDecisionDTO);
    }

    /**
     * {@code PATCH  /hiring-decisions/:id} : Partial updates given fields of an existing hiringDecision, field will ignore if it is null
     *
     * @param id the id of the hiringDecisionDTO to save.
     * @param hiringDecisionDTO the hiringDecisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiringDecisionDTO,
     * or with status {@code 400 (Bad Request)} if the hiringDecisionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hiringDecisionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hiringDecisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HiringDecisionDTO> partialUpdateHiringDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HiringDecisionDTO hiringDecisionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HiringDecision partially : {}, {}", id, hiringDecisionDTO);
        if (hiringDecisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hiringDecisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hiringDecisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HiringDecisionDTO> result = hiringDecisionService.partialUpdate(hiringDecisionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hiringDecisionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hiring-decisions} : get all the Hiring Decisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Hiring Decisions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HiringDecisionDTO>> getAllHiringDecisions(
        HiringDecisionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HiringDecisions by criteria: {}", criteria);

        Page<HiringDecisionDTO> page = hiringDecisionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hiring-decisions/count} : count all the hiringDecisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHiringDecisions(HiringDecisionCriteria criteria) {
        LOG.debug("REST request to count HiringDecisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(hiringDecisionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hiring-decisions/:id} : get the "id" hiringDecision.
     *
     * @param id the id of the hiringDecisionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hiringDecisionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HiringDecisionDTO> getHiringDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HiringDecision : {}", id);
        Optional<HiringDecisionDTO> hiringDecisionDTO = hiringDecisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hiringDecisionDTO);
    }

    /**
     * {@code DELETE  /hiring-decisions/:id} : delete the "id" hiringDecision.
     *
     * @param id the id of the hiringDecisionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHiringDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HiringDecision : {}", id);
        hiringDecisionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
