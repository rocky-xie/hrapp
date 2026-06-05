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
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.service.SuccessionCandidateQueryService;
import top.btmdc.hr.service.SuccessionCandidateService;
import top.btmdc.hr.service.criteria.SuccessionCandidateCriteria;
import top.btmdc.hr.service.dto.SuccessionCandidateDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.SuccessionCandidate}.
 */
@RestController
@RequestMapping("/api/succession-candidates")
public class SuccessionCandidateResource {

    private static final Logger LOG = LoggerFactory.getLogger(SuccessionCandidateResource.class);

    private static final String ENTITY_NAME = "successionCandidate";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final SuccessionCandidateService successionCandidateService;

    private final SuccessionCandidateRepository successionCandidateRepository;

    private final SuccessionCandidateQueryService successionCandidateQueryService;

    public SuccessionCandidateResource(
        SuccessionCandidateService successionCandidateService,
        SuccessionCandidateRepository successionCandidateRepository,
        SuccessionCandidateQueryService successionCandidateQueryService
    ) {
        this.successionCandidateService = successionCandidateService;
        this.successionCandidateRepository = successionCandidateRepository;
        this.successionCandidateQueryService = successionCandidateQueryService;
    }

    /**
     * {@code POST  /succession-candidates} : Create a new successionCandidate.
     *
     * @param successionCandidateDTO the successionCandidateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new successionCandidateDTO, or with status {@code 400 (Bad Request)} if the successionCandidate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SuccessionCandidateDTO> createSuccessionCandidate(
        @Valid @RequestBody SuccessionCandidateDTO successionCandidateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SuccessionCandidate : {}", successionCandidateDTO);
        if (successionCandidateDTO.getId() != null) {
            throw new BadRequestAlertException("A new successionCandidate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        successionCandidateDTO = successionCandidateService.save(successionCandidateDTO);
        return ResponseEntity.created(new URI("/api/succession-candidates/" + successionCandidateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, successionCandidateDTO.getId().toString()))
            .body(successionCandidateDTO);
    }

    /**
     * {@code PUT  /succession-candidates/:id} : Updates an existing successionCandidate.
     *
     * @param id the id of the successionCandidateDTO to save.
     * @param successionCandidateDTO the successionCandidateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated successionCandidateDTO,
     * or with status {@code 400 (Bad Request)} if the successionCandidateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the successionCandidateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuccessionCandidateDTO> updateSuccessionCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuccessionCandidateDTO successionCandidateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SuccessionCandidate : {}, {}", id, successionCandidateDTO);
        if (successionCandidateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, successionCandidateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!successionCandidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        successionCandidateDTO = successionCandidateService.update(successionCandidateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, successionCandidateDTO.getId().toString()))
            .body(successionCandidateDTO);
    }

    /**
     * {@code PATCH  /succession-candidates/:id} : Partial updates given fields of an existing successionCandidate, field will ignore if it is null
     *
     * @param id the id of the successionCandidateDTO to save.
     * @param successionCandidateDTO the successionCandidateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated successionCandidateDTO,
     * or with status {@code 400 (Bad Request)} if the successionCandidateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the successionCandidateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the successionCandidateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuccessionCandidateDTO> partialUpdateSuccessionCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuccessionCandidateDTO successionCandidateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SuccessionCandidate partially : {}, {}", id, successionCandidateDTO);
        if (successionCandidateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, successionCandidateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!successionCandidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuccessionCandidateDTO> result = successionCandidateService.partialUpdate(successionCandidateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, successionCandidateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /succession-candidates} : get all the Succession Candidates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Succession Candidates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SuccessionCandidateDTO>> getAllSuccessionCandidates(
        SuccessionCandidateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SuccessionCandidates by criteria: {}", criteria);

        Page<SuccessionCandidateDTO> page = successionCandidateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /succession-candidates/count} : count all the successionCandidates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSuccessionCandidates(SuccessionCandidateCriteria criteria) {
        LOG.debug("REST request to count SuccessionCandidates by criteria: {}", criteria);
        return ResponseEntity.ok().body(successionCandidateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /succession-candidates/:id} : get the "id" successionCandidate.
     *
     * @param id the id of the successionCandidateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the successionCandidateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessionCandidateDTO> getSuccessionCandidate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SuccessionCandidate : {}", id);
        Optional<SuccessionCandidateDTO> successionCandidateDTO = successionCandidateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(successionCandidateDTO);
    }

    /**
     * {@code DELETE  /succession-candidates/:id} : delete the "id" successionCandidate.
     *
     * @param id the id of the successionCandidateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuccessionCandidate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SuccessionCandidate : {}", id);
        successionCandidateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
