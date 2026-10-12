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
import top.btmdc.hr.repository.DispatchReviewRepository;
import top.btmdc.hr.service.DispatchReviewQueryService;
import top.btmdc.hr.service.DispatchReviewService;
import top.btmdc.hr.service.criteria.DispatchReviewCriteria;
import top.btmdc.hr.service.dto.DispatchReviewDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.DispatchReview}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/dispatch-reviews")
public class DispatchReviewResource {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchReviewResource.class);

    private static final String ENTITY_NAME = "dispatchReview";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final DispatchReviewService dispatchReviewService;

    private final DispatchReviewRepository dispatchReviewRepository;

    private final DispatchReviewQueryService dispatchReviewQueryService;

    public DispatchReviewResource(
        DispatchReviewService dispatchReviewService,
        DispatchReviewRepository dispatchReviewRepository,
        DispatchReviewQueryService dispatchReviewQueryService
    ) {
        this.dispatchReviewService = dispatchReviewService;
        this.dispatchReviewRepository = dispatchReviewRepository;
        this.dispatchReviewQueryService = dispatchReviewQueryService;
    }

    /**
     * {@code POST  /dispatch-reviews} : Create a new dispatchReview.
     *
     * @param dispatchReviewDTO the dispatchReviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispatchReviewDTO, or with status {@code 400 (Bad Request)} if the dispatchReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DispatchReviewDTO> createDispatchReview(@Valid @RequestBody DispatchReviewDTO dispatchReviewDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DispatchReview : {}", dispatchReviewDTO);
        if (dispatchReviewDTO.getId() != null) {
            throw new BadRequestAlertException("A new dispatchReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dispatchReviewDTO = dispatchReviewService.save(dispatchReviewDTO);
        return ResponseEntity.created(new URI("/api/dispatch-reviews/" + dispatchReviewDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dispatchReviewDTO.getId().toString()))
            .body(dispatchReviewDTO);
    }

    /**
     * {@code PUT  /dispatch-reviews/:id} : Updates an existing dispatchReview.
     *
     * @param id the id of the dispatchReviewDTO to save.
     * @param dispatchReviewDTO the dispatchReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchReviewDTO,
     * or with status {@code 400 (Bad Request)} if the dispatchReviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispatchReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DispatchReviewDTO> updateDispatchReview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DispatchReviewDTO dispatchReviewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DispatchReview : {}, {}", id, dispatchReviewDTO);
        if (dispatchReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dispatchReviewDTO = dispatchReviewService.update(dispatchReviewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatchReviewDTO.getId().toString()))
            .body(dispatchReviewDTO);
    }

    /**
     * {@code PATCH  /dispatch-reviews/:id} : Partial updates given fields of an existing dispatchReview, field will ignore if it is null
     *
     * @param id the id of the dispatchReviewDTO to save.
     * @param dispatchReviewDTO the dispatchReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatchReviewDTO,
     * or with status {@code 400 (Bad Request)} if the dispatchReviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dispatchReviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispatchReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DispatchReviewDTO> partialUpdateDispatchReview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DispatchReviewDTO dispatchReviewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DispatchReview partially : {}, {}", id, dispatchReviewDTO);
        if (dispatchReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatchReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DispatchReviewDTO> result = dispatchReviewService.partialUpdate(dispatchReviewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatchReviewDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dispatch-reviews} : get all the Dispatch Reviews.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Dispatch Reviews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DispatchReviewDTO>> getAllDispatchReviews(
        DispatchReviewCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DispatchReviews by criteria: {}", criteria);

        Page<DispatchReviewDTO> page = dispatchReviewQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispatch-reviews/count} : count all the dispatchReviews.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDispatchReviews(DispatchReviewCriteria criteria) {
        LOG.debug("REST request to count DispatchReviews by criteria: {}", criteria);
        return ResponseEntity.ok().body(dispatchReviewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dispatch-reviews/:id} : get the "id" dispatchReview.
     *
     * @param id the id of the dispatchReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispatchReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DispatchReviewDTO> getDispatchReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DispatchReview : {}", id);
        Optional<DispatchReviewDTO> dispatchReviewDTO = dispatchReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispatchReviewDTO);
    }

    /**
     * {@code DELETE  /dispatch-reviews/:id} : delete the "id" dispatchReview.
     *
     * @param id the id of the dispatchReviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatchReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DispatchReview : {}", id);
        dispatchReviewService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
