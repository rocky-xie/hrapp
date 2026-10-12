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
import top.btmdc.hr.repository.PreboardingFollowUpRepository;
import top.btmdc.hr.service.PreboardingFollowUpQueryService;
import top.btmdc.hr.service.PreboardingFollowUpService;
import top.btmdc.hr.service.criteria.PreboardingFollowUpCriteria;
import top.btmdc.hr.service.dto.PreboardingFollowUpDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PreboardingFollowUp}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/preboarding-follow-ups")
public class PreboardingFollowUpResource {

    private static final Logger LOG = LoggerFactory.getLogger(PreboardingFollowUpResource.class);

    private static final String ENTITY_NAME = "preboardingFollowUp";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PreboardingFollowUpService preboardingFollowUpService;

    private final PreboardingFollowUpRepository preboardingFollowUpRepository;

    private final PreboardingFollowUpQueryService preboardingFollowUpQueryService;

    public PreboardingFollowUpResource(
        PreboardingFollowUpService preboardingFollowUpService,
        PreboardingFollowUpRepository preboardingFollowUpRepository,
        PreboardingFollowUpQueryService preboardingFollowUpQueryService
    ) {
        this.preboardingFollowUpService = preboardingFollowUpService;
        this.preboardingFollowUpRepository = preboardingFollowUpRepository;
        this.preboardingFollowUpQueryService = preboardingFollowUpQueryService;
    }

    /**
     * {@code POST  /preboarding-follow-ups} : Create a new preboardingFollowUp.
     *
     * @param preboardingFollowUpDTO the preboardingFollowUpDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preboardingFollowUpDTO, or with status {@code 400 (Bad Request)} if the preboardingFollowUp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PreboardingFollowUpDTO> createPreboardingFollowUp(
        @Valid @RequestBody PreboardingFollowUpDTO preboardingFollowUpDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PreboardingFollowUp : {}", preboardingFollowUpDTO);
        if (preboardingFollowUpDTO.getId() != null) {
            throw new BadRequestAlertException("A new preboardingFollowUp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        preboardingFollowUpDTO = preboardingFollowUpService.save(preboardingFollowUpDTO);
        return ResponseEntity.created(new URI("/api/preboarding-follow-ups/" + preboardingFollowUpDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, preboardingFollowUpDTO.getId().toString()))
            .body(preboardingFollowUpDTO);
    }

    /**
     * {@code PUT  /preboarding-follow-ups/:id} : Updates an existing preboardingFollowUp.
     *
     * @param id the id of the preboardingFollowUpDTO to save.
     * @param preboardingFollowUpDTO the preboardingFollowUpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preboardingFollowUpDTO,
     * or with status {@code 400 (Bad Request)} if the preboardingFollowUpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preboardingFollowUpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PreboardingFollowUpDTO> updatePreboardingFollowUp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PreboardingFollowUpDTO preboardingFollowUpDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PreboardingFollowUp : {}, {}", id, preboardingFollowUpDTO);
        if (preboardingFollowUpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preboardingFollowUpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preboardingFollowUpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        preboardingFollowUpDTO = preboardingFollowUpService.update(preboardingFollowUpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, preboardingFollowUpDTO.getId().toString()))
            .body(preboardingFollowUpDTO);
    }

    /**
     * {@code PATCH  /preboarding-follow-ups/:id} : Partial updates given fields of an existing preboardingFollowUp, field will ignore if it is null
     *
     * @param id the id of the preboardingFollowUpDTO to save.
     * @param preboardingFollowUpDTO the preboardingFollowUpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preboardingFollowUpDTO,
     * or with status {@code 400 (Bad Request)} if the preboardingFollowUpDTO is not valid,
     * or with status {@code 404 (Not Found)} if the preboardingFollowUpDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the preboardingFollowUpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PreboardingFollowUpDTO> partialUpdatePreboardingFollowUp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PreboardingFollowUpDTO preboardingFollowUpDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PreboardingFollowUp partially : {}, {}", id, preboardingFollowUpDTO);
        if (preboardingFollowUpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preboardingFollowUpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preboardingFollowUpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PreboardingFollowUpDTO> result = preboardingFollowUpService.partialUpdate(preboardingFollowUpDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, preboardingFollowUpDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /preboarding-follow-ups} : get all the Preboarding Follow Ups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Preboarding Follow Ups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PreboardingFollowUpDTO>> getAllPreboardingFollowUps(
        PreboardingFollowUpCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PreboardingFollowUps by criteria: {}", criteria);

        Page<PreboardingFollowUpDTO> page = preboardingFollowUpQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /preboarding-follow-ups/count} : count all the preboardingFollowUps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPreboardingFollowUps(PreboardingFollowUpCriteria criteria) {
        LOG.debug("REST request to count PreboardingFollowUps by criteria: {}", criteria);
        return ResponseEntity.ok().body(preboardingFollowUpQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /preboarding-follow-ups/:id} : get the "id" preboardingFollowUp.
     *
     * @param id the id of the preboardingFollowUpDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preboardingFollowUpDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreboardingFollowUpDTO> getPreboardingFollowUp(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PreboardingFollowUp : {}", id);
        Optional<PreboardingFollowUpDTO> preboardingFollowUpDTO = preboardingFollowUpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preboardingFollowUpDTO);
    }

    /**
     * {@code DELETE  /preboarding-follow-ups/:id} : delete the "id" preboardingFollowUp.
     *
     * @param id the id of the preboardingFollowUpDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreboardingFollowUp(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PreboardingFollowUp : {}", id);
        preboardingFollowUpService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
