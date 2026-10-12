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
import top.btmdc.hr.repository.SelectionActivityRepository;
import top.btmdc.hr.service.SelectionActivityQueryService;
import top.btmdc.hr.service.SelectionActivityService;
import top.btmdc.hr.service.criteria.SelectionActivityCriteria;
import top.btmdc.hr.service.dto.SelectionActivityDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.SelectionActivity}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/selection-activities")
public class SelectionActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(SelectionActivityResource.class);

    private static final String ENTITY_NAME = "selectionActivity";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final SelectionActivityService selectionActivityService;

    private final SelectionActivityRepository selectionActivityRepository;

    private final SelectionActivityQueryService selectionActivityQueryService;

    public SelectionActivityResource(
        SelectionActivityService selectionActivityService,
        SelectionActivityRepository selectionActivityRepository,
        SelectionActivityQueryService selectionActivityQueryService
    ) {
        this.selectionActivityService = selectionActivityService;
        this.selectionActivityRepository = selectionActivityRepository;
        this.selectionActivityQueryService = selectionActivityQueryService;
    }

    /**
     * {@code POST  /selection-activities} : Create a new selectionActivity.
     *
     * @param selectionActivityDTO the selectionActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new selectionActivityDTO, or with status {@code 400 (Bad Request)} if the selectionActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SelectionActivityDTO> createSelectionActivity(@Valid @RequestBody SelectionActivityDTO selectionActivityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SelectionActivity : {}", selectionActivityDTO);
        if (selectionActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new selectionActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        selectionActivityDTO = selectionActivityService.save(selectionActivityDTO);
        return ResponseEntity.created(new URI("/api/selection-activities/" + selectionActivityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, selectionActivityDTO.getId().toString()))
            .body(selectionActivityDTO);
    }

    /**
     * {@code PUT  /selection-activities/:id} : Updates an existing selectionActivity.
     *
     * @param id the id of the selectionActivityDTO to save.
     * @param selectionActivityDTO the selectionActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectionActivityDTO,
     * or with status {@code 400 (Bad Request)} if the selectionActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the selectionActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SelectionActivityDTO> updateSelectionActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SelectionActivityDTO selectionActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SelectionActivity : {}, {}", id, selectionActivityDTO);
        if (selectionActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectionActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectionActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        selectionActivityDTO = selectionActivityService.update(selectionActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectionActivityDTO.getId().toString()))
            .body(selectionActivityDTO);
    }

    /**
     * {@code PATCH  /selection-activities/:id} : Partial updates given fields of an existing selectionActivity, field will ignore if it is null
     *
     * @param id the id of the selectionActivityDTO to save.
     * @param selectionActivityDTO the selectionActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectionActivityDTO,
     * or with status {@code 400 (Bad Request)} if the selectionActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the selectionActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the selectionActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SelectionActivityDTO> partialUpdateSelectionActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SelectionActivityDTO selectionActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SelectionActivity partially : {}, {}", id, selectionActivityDTO);
        if (selectionActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectionActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectionActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SelectionActivityDTO> result = selectionActivityService.partialUpdate(selectionActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectionActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /selection-activities} : get all the Selection Activities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Selection Activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SelectionActivityDTO>> getAllSelectionActivities(
        SelectionActivityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SelectionActivities by criteria: {}", criteria);

        Page<SelectionActivityDTO> page = selectionActivityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /selection-activities/count} : count all the selectionActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSelectionActivities(SelectionActivityCriteria criteria) {
        LOG.debug("REST request to count SelectionActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(selectionActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /selection-activities/:id} : get the "id" selectionActivity.
     *
     * @param id the id of the selectionActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the selectionActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SelectionActivityDTO> getSelectionActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SelectionActivity : {}", id);
        Optional<SelectionActivityDTO> selectionActivityDTO = selectionActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(selectionActivityDTO);
    }

    /**
     * {@code DELETE  /selection-activities/:id} : delete the "id" selectionActivity.
     *
     * @param id the id of the selectionActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSelectionActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SelectionActivity : {}", id);
        selectionActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
