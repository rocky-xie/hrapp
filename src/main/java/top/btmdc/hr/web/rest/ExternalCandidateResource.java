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
import top.btmdc.hr.repository.ExternalCandidateRepository;
import top.btmdc.hr.service.ExternalCandidateQueryService;
import top.btmdc.hr.service.ExternalCandidateService;
import top.btmdc.hr.service.criteria.ExternalCandidateCriteria;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.ExternalCandidate}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/external-candidates")
public class ExternalCandidateResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalCandidateResource.class);

    private static final String ENTITY_NAME = "externalCandidate";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final ExternalCandidateService externalCandidateService;

    private final ExternalCandidateRepository externalCandidateRepository;

    private final ExternalCandidateQueryService externalCandidateQueryService;

    public ExternalCandidateResource(
        ExternalCandidateService externalCandidateService,
        ExternalCandidateRepository externalCandidateRepository,
        ExternalCandidateQueryService externalCandidateQueryService
    ) {
        this.externalCandidateService = externalCandidateService;
        this.externalCandidateRepository = externalCandidateRepository;
        this.externalCandidateQueryService = externalCandidateQueryService;
    }

    /**
     * {@code POST  /external-candidates} : Create a new externalCandidate.
     *
     * @param externalCandidateDTO the externalCandidateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalCandidateDTO, or with status {@code 400 (Bad Request)} if the externalCandidate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExternalCandidateDTO> createExternalCandidate(@Valid @RequestBody ExternalCandidateDTO externalCandidateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExternalCandidate : {}", externalCandidateDTO);
        if (externalCandidateDTO.getId() != null) {
            throw new BadRequestAlertException("A new externalCandidate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        externalCandidateDTO = externalCandidateService.save(externalCandidateDTO);
        return ResponseEntity.created(new URI("/api/external-candidates/" + externalCandidateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, externalCandidateDTO.getId().toString()))
            .body(externalCandidateDTO);
    }

    /**
     * {@code PUT  /external-candidates/:id} : Updates an existing externalCandidate.
     *
     * @param id the id of the externalCandidateDTO to save.
     * @param externalCandidateDTO the externalCandidateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalCandidateDTO,
     * or with status {@code 400 (Bad Request)} if the externalCandidateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalCandidateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExternalCandidateDTO> updateExternalCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExternalCandidateDTO externalCandidateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExternalCandidate : {}, {}", id, externalCandidateDTO);
        if (externalCandidateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalCandidateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalCandidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        externalCandidateDTO = externalCandidateService.update(externalCandidateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalCandidateDTO.getId().toString()))
            .body(externalCandidateDTO);
    }

    /**
     * {@code PATCH  /external-candidates/:id} : Partial updates given fields of an existing externalCandidate, field will ignore if it is null
     *
     * @param id the id of the externalCandidateDTO to save.
     * @param externalCandidateDTO the externalCandidateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalCandidateDTO,
     * or with status {@code 400 (Bad Request)} if the externalCandidateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the externalCandidateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalCandidateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExternalCandidateDTO> partialUpdateExternalCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExternalCandidateDTO externalCandidateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExternalCandidate partially : {}, {}", id, externalCandidateDTO);
        if (externalCandidateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalCandidateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalCandidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExternalCandidateDTO> result = externalCandidateService.partialUpdate(externalCandidateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalCandidateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /external-candidates} : get all the External Candidates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of External Candidates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExternalCandidateDTO>> getAllExternalCandidates(
        ExternalCandidateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExternalCandidates by criteria: {}", criteria);

        Page<ExternalCandidateDTO> page = externalCandidateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /external-candidates/count} : count all the externalCandidates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExternalCandidates(ExternalCandidateCriteria criteria) {
        LOG.debug("REST request to count ExternalCandidates by criteria: {}", criteria);
        return ResponseEntity.ok().body(externalCandidateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /external-candidates/:id} : get the "id" externalCandidate.
     *
     * @param id the id of the externalCandidateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalCandidateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExternalCandidateDTO> getExternalCandidate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExternalCandidate : {}", id);
        Optional<ExternalCandidateDTO> externalCandidateDTO = externalCandidateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(externalCandidateDTO);
    }

    /**
     * {@code DELETE  /external-candidates/:id} : delete the "id" externalCandidate.
     *
     * @param id the id of the externalCandidateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExternalCandidate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExternalCandidate : {}", id);
        externalCandidateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
