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
import top.btmdc.hr.repository.CandidateProfileRepository;
import top.btmdc.hr.service.CandidateProfileQueryService;
import top.btmdc.hr.service.CandidateProfileService;
import top.btmdc.hr.service.criteria.CandidateProfileCriteria;
import top.btmdc.hr.service.dto.CandidateProfileDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.CandidateProfile}.
 */
@RestController
@RequestMapping("/api/candidate-profiles")
public class CandidateProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateProfileResource.class);

    private static final String ENTITY_NAME = "candidateProfile";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final CandidateProfileService candidateProfileService;

    private final CandidateProfileRepository candidateProfileRepository;

    private final CandidateProfileQueryService candidateProfileQueryService;

    public CandidateProfileResource(
        CandidateProfileService candidateProfileService,
        CandidateProfileRepository candidateProfileRepository,
        CandidateProfileQueryService candidateProfileQueryService
    ) {
        this.candidateProfileService = candidateProfileService;
        this.candidateProfileRepository = candidateProfileRepository;
        this.candidateProfileQueryService = candidateProfileQueryService;
    }

    /**
     * {@code POST  /candidate-profiles} : Create a new candidateProfile.
     *
     * @param candidateProfileDTO the candidateProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidateProfileDTO, or with status {@code 400 (Bad Request)} if the candidateProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CandidateProfileDTO> createCandidateProfile(@Valid @RequestBody CandidateProfileDTO candidateProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CandidateProfile : {}", candidateProfileDTO);
        if (candidateProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new candidateProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        candidateProfileDTO = candidateProfileService.save(candidateProfileDTO);
        return ResponseEntity.created(new URI("/api/candidate-profiles/" + candidateProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, candidateProfileDTO.getId().toString()))
            .body(candidateProfileDTO);
    }

    /**
     * {@code PUT  /candidate-profiles/:id} : Updates an existing candidateProfile.
     *
     * @param id the id of the candidateProfileDTO to save.
     * @param candidateProfileDTO the candidateProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidateProfileDTO,
     * or with status {@code 400 (Bad Request)} if the candidateProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidateProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CandidateProfileDTO> updateCandidateProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CandidateProfileDTO candidateProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CandidateProfile : {}, {}", id, candidateProfileDTO);
        if (candidateProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidateProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        candidateProfileDTO = candidateProfileService.update(candidateProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidateProfileDTO.getId().toString()))
            .body(candidateProfileDTO);
    }

    /**
     * {@code PATCH  /candidate-profiles/:id} : Partial updates given fields of an existing candidateProfile, field will ignore if it is null
     *
     * @param id the id of the candidateProfileDTO to save.
     * @param candidateProfileDTO the candidateProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidateProfileDTO,
     * or with status {@code 400 (Bad Request)} if the candidateProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the candidateProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidateProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CandidateProfileDTO> partialUpdateCandidateProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CandidateProfileDTO candidateProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CandidateProfile partially : {}, {}", id, candidateProfileDTO);
        if (candidateProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidateProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CandidateProfileDTO> result = candidateProfileService.partialUpdate(candidateProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidateProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /candidate-profiles} : get all the Candidate Profiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Candidate Profiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CandidateProfileDTO>> getAllCandidateProfiles(
        CandidateProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CandidateProfiles by criteria: {}", criteria);

        Page<CandidateProfileDTO> page = candidateProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidate-profiles/count} : count all the candidateProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCandidateProfiles(CandidateProfileCriteria criteria) {
        LOG.debug("REST request to count CandidateProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(candidateProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /candidate-profiles/:id} : get the "id" candidateProfile.
     *
     * @param id the id of the candidateProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidateProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CandidateProfileDTO> getCandidateProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CandidateProfile : {}", id);
        Optional<CandidateProfileDTO> candidateProfileDTO = candidateProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidateProfileDTO);
    }

    /**
     * {@code DELETE  /candidate-profiles/:id} : delete the "id" candidateProfile.
     *
     * @param id the id of the candidateProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CandidateProfile : {}", id);
        candidateProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
