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
import top.btmdc.hr.repository.PositionMatchRepository;
import top.btmdc.hr.service.PositionMatchQueryService;
import top.btmdc.hr.service.PositionMatchService;
import top.btmdc.hr.service.criteria.PositionMatchCriteria;
import top.btmdc.hr.service.dto.PositionMatchDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PositionMatch}.
 */
@RestController
@RequestMapping("/api/position-matches")
public class PositionMatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionMatchResource.class);

    private static final String ENTITY_NAME = "positionMatch";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PositionMatchService positionMatchService;

    private final PositionMatchRepository positionMatchRepository;

    private final PositionMatchQueryService positionMatchQueryService;

    public PositionMatchResource(
        PositionMatchService positionMatchService,
        PositionMatchRepository positionMatchRepository,
        PositionMatchQueryService positionMatchQueryService
    ) {
        this.positionMatchService = positionMatchService;
        this.positionMatchRepository = positionMatchRepository;
        this.positionMatchQueryService = positionMatchQueryService;
    }

    /**
     * {@code POST  /position-matches} : Create a new positionMatch.
     *
     * @param positionMatchDTO the positionMatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionMatchDTO, or with status {@code 400 (Bad Request)} if the positionMatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PositionMatchDTO> createPositionMatch(@Valid @RequestBody PositionMatchDTO positionMatchDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PositionMatch : {}", positionMatchDTO);
        if (positionMatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new positionMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        positionMatchDTO = positionMatchService.save(positionMatchDTO);
        return ResponseEntity.created(new URI("/api/position-matches/" + positionMatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, positionMatchDTO.getId().toString()))
            .body(positionMatchDTO);
    }

    /**
     * {@code PUT  /position-matches/:id} : Updates an existing positionMatch.
     *
     * @param id the id of the positionMatchDTO to save.
     * @param positionMatchDTO the positionMatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionMatchDTO,
     * or with status {@code 400 (Bad Request)} if the positionMatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionMatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionMatchDTO> updatePositionMatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionMatchDTO positionMatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PositionMatch : {}, {}", id, positionMatchDTO);
        if (positionMatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionMatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionMatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        positionMatchDTO = positionMatchService.update(positionMatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionMatchDTO.getId().toString()))
            .body(positionMatchDTO);
    }

    /**
     * {@code PATCH  /position-matches/:id} : Partial updates given fields of an existing positionMatch, field will ignore if it is null
     *
     * @param id the id of the positionMatchDTO to save.
     * @param positionMatchDTO the positionMatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionMatchDTO,
     * or with status {@code 400 (Bad Request)} if the positionMatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionMatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionMatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionMatchDTO> partialUpdatePositionMatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionMatchDTO positionMatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PositionMatch partially : {}, {}", id, positionMatchDTO);
        if (positionMatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionMatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionMatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionMatchDTO> result = positionMatchService.partialUpdate(positionMatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionMatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /position-matches} : get all the Position Matches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Position Matches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PositionMatchDTO>> getAllPositionMatches(
        PositionMatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PositionMatches by criteria: {}", criteria);

        Page<PositionMatchDTO> page = positionMatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /position-matches/count} : count all the positionMatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPositionMatches(PositionMatchCriteria criteria) {
        LOG.debug("REST request to count PositionMatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionMatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /position-matches/:id} : get the "id" positionMatch.
     *
     * @param id the id of the positionMatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionMatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionMatchDTO> getPositionMatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PositionMatch : {}", id);
        Optional<PositionMatchDTO> positionMatchDTO = positionMatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionMatchDTO);
    }

    /**
     * {@code DELETE  /position-matches/:id} : delete the "id" positionMatch.
     *
     * @param id the id of the positionMatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionMatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PositionMatch : {}", id);
        positionMatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
