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
import top.btmdc.hr.repository.TrustObservationRepository;
import top.btmdc.hr.service.TrustObservationQueryService;
import top.btmdc.hr.service.TrustObservationService;
import top.btmdc.hr.service.criteria.TrustObservationCriteria;
import top.btmdc.hr.service.dto.TrustObservationDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.TrustObservation}.
 */
@RestController
@RequestMapping("/api/trust-observations")
public class TrustObservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrustObservationResource.class);

    private static final String ENTITY_NAME = "trustObservation";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final TrustObservationService trustObservationService;

    private final TrustObservationRepository trustObservationRepository;

    private final TrustObservationQueryService trustObservationQueryService;

    public TrustObservationResource(
        TrustObservationService trustObservationService,
        TrustObservationRepository trustObservationRepository,
        TrustObservationQueryService trustObservationQueryService
    ) {
        this.trustObservationService = trustObservationService;
        this.trustObservationRepository = trustObservationRepository;
        this.trustObservationQueryService = trustObservationQueryService;
    }

    /**
     * {@code POST  /trust-observations} : Create a new trustObservation.
     *
     * @param trustObservationDTO the trustObservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trustObservationDTO, or with status {@code 400 (Bad Request)} if the trustObservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrustObservationDTO> createTrustObservation(@Valid @RequestBody TrustObservationDTO trustObservationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrustObservation : {}", trustObservationDTO);
        if (trustObservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new trustObservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trustObservationDTO = trustObservationService.save(trustObservationDTO);
        return ResponseEntity.created(new URI("/api/trust-observations/" + trustObservationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trustObservationDTO.getId().toString()))
            .body(trustObservationDTO);
    }

    /**
     * {@code PUT  /trust-observations/:id} : Updates an existing trustObservation.
     *
     * @param id the id of the trustObservationDTO to save.
     * @param trustObservationDTO the trustObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trustObservationDTO,
     * or with status {@code 400 (Bad Request)} if the trustObservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trustObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrustObservationDTO> updateTrustObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrustObservationDTO trustObservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrustObservation : {}, {}", id, trustObservationDTO);
        if (trustObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trustObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trustObservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trustObservationDTO = trustObservationService.update(trustObservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trustObservationDTO.getId().toString()))
            .body(trustObservationDTO);
    }

    /**
     * {@code PATCH  /trust-observations/:id} : Partial updates given fields of an existing trustObservation, field will ignore if it is null
     *
     * @param id the id of the trustObservationDTO to save.
     * @param trustObservationDTO the trustObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trustObservationDTO,
     * or with status {@code 400 (Bad Request)} if the trustObservationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trustObservationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trustObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrustObservationDTO> partialUpdateTrustObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrustObservationDTO trustObservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrustObservation partially : {}, {}", id, trustObservationDTO);
        if (trustObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trustObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trustObservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrustObservationDTO> result = trustObservationService.partialUpdate(trustObservationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trustObservationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trust-observations} : get all the Trust Observations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Trust Observations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrustObservationDTO>> getAllTrustObservations(
        TrustObservationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TrustObservations by criteria: {}", criteria);

        Page<TrustObservationDTO> page = trustObservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trust-observations/count} : count all the trustObservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrustObservations(TrustObservationCriteria criteria) {
        LOG.debug("REST request to count TrustObservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(trustObservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trust-observations/:id} : get the "id" trustObservation.
     *
     * @param id the id of the trustObservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trustObservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrustObservationDTO> getTrustObservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrustObservation : {}", id);
        Optional<TrustObservationDTO> trustObservationDTO = trustObservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trustObservationDTO);
    }

    /**
     * {@code DELETE  /trust-observations/:id} : delete the "id" trustObservation.
     *
     * @param id the id of the trustObservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrustObservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrustObservation : {}", id);
        trustObservationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
