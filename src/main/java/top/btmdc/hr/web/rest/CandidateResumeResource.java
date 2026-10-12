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
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.service.CandidateResumeQueryService;
import top.btmdc.hr.service.CandidateResumeService;
import top.btmdc.hr.service.criteria.CandidateResumeCriteria;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.CandidateResume}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/candidate-resumes")
public class CandidateResumeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateResumeResource.class);

    private static final String ENTITY_NAME = "candidateResume";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final CandidateResumeService candidateResumeService;

    private final CandidateResumeRepository candidateResumeRepository;

    private final CandidateResumeQueryService candidateResumeQueryService;

    public CandidateResumeResource(
        CandidateResumeService candidateResumeService,
        CandidateResumeRepository candidateResumeRepository,
        CandidateResumeQueryService candidateResumeQueryService
    ) {
        this.candidateResumeService = candidateResumeService;
        this.candidateResumeRepository = candidateResumeRepository;
        this.candidateResumeQueryService = candidateResumeQueryService;
    }

    /**
     * {@code POST  /candidate-resumes} : Create a new candidateResume.
     *
     * @param candidateResumeDTO the candidateResumeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidateResumeDTO, or with status {@code 400 (Bad Request)} if the candidateResume has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CandidateResumeDTO> createCandidateResume(@Valid @RequestBody CandidateResumeDTO candidateResumeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CandidateResume : {}", candidateResumeDTO);
        if (candidateResumeDTO.getId() != null) {
            throw new BadRequestAlertException("A new candidateResume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        candidateResumeDTO = candidateResumeService.save(candidateResumeDTO);
        return ResponseEntity.created(new URI("/api/candidate-resumes/" + candidateResumeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, candidateResumeDTO.getId().toString()))
            .body(candidateResumeDTO);
    }

    /**
     * {@code PUT  /candidate-resumes/:id} : Updates an existing candidateResume.
     *
     * @param id the id of the candidateResumeDTO to save.
     * @param candidateResumeDTO the candidateResumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidateResumeDTO,
     * or with status {@code 400 (Bad Request)} if the candidateResumeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidateResumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CandidateResumeDTO> updateCandidateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CandidateResumeDTO candidateResumeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CandidateResume : {}, {}", id, candidateResumeDTO);
        if (candidateResumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidateResumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateResumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        candidateResumeDTO = candidateResumeService.update(candidateResumeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidateResumeDTO.getId().toString()))
            .body(candidateResumeDTO);
    }

    /**
     * {@code PATCH  /candidate-resumes/:id} : Partial updates given fields of an existing candidateResume, field will ignore if it is null
     *
     * @param id the id of the candidateResumeDTO to save.
     * @param candidateResumeDTO the candidateResumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidateResumeDTO,
     * or with status {@code 400 (Bad Request)} if the candidateResumeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the candidateResumeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidateResumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidateResumeDTO> partialUpdateCandidateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CandidateResumeDTO candidateResumeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CandidateResume partially : {}, {}", id, candidateResumeDTO);
        if (candidateResumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidateResumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateResumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidateResumeDTO> result = candidateResumeService.partialUpdate(candidateResumeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidateResumeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /candidate-resumes} : get all the Candidate Resumes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Candidate Resumes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CandidateResumeDTO>> getAllCandidateResumes(
        CandidateResumeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CandidateResumes by criteria: {}", criteria);

        Page<CandidateResumeDTO> page = candidateResumeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidate-resumes/count} : count all the candidateResumes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCandidateResumes(CandidateResumeCriteria criteria) {
        LOG.debug("REST request to count CandidateResumes by criteria: {}", criteria);
        return ResponseEntity.ok().body(candidateResumeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /candidate-resumes/:id} : get the "id" candidateResume.
     *
     * @param id the id of the candidateResumeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidateResumeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CandidateResumeDTO> getCandidateResume(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CandidateResume : {}", id);
        Optional<CandidateResumeDTO> candidateResumeDTO = candidateResumeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidateResumeDTO);
    }

    /**
     * {@code DELETE  /candidate-resumes/:id} : delete the "id" candidateResume.
     *
     * @param id the id of the candidateResumeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateResume(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CandidateResume : {}", id);
        candidateResumeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
