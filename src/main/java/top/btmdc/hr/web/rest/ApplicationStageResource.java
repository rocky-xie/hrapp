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
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.service.ApplicationStageQueryService;
import top.btmdc.hr.service.ApplicationStageService;
import top.btmdc.hr.service.criteria.ApplicationStageCriteria;
import top.btmdc.hr.service.dto.ApplicationStageDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.ApplicationStage}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/application-stages")
public class ApplicationStageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStageResource.class);

    private static final String ENTITY_NAME = "applicationStage";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final ApplicationStageService applicationStageService;

    private final ApplicationStageRepository applicationStageRepository;

    private final ApplicationStageQueryService applicationStageQueryService;

    public ApplicationStageResource(
        ApplicationStageService applicationStageService,
        ApplicationStageRepository applicationStageRepository,
        ApplicationStageQueryService applicationStageQueryService
    ) {
        this.applicationStageService = applicationStageService;
        this.applicationStageRepository = applicationStageRepository;
        this.applicationStageQueryService = applicationStageQueryService;
    }

    /**
     * {@code POST  /application-stages} : Create a new applicationStage.
     *
     * @param applicationStageDTO the applicationStageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationStageDTO, or with status {@code 400 (Bad Request)} if the applicationStage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationStageDTO> createApplicationStage(@Valid @RequestBody ApplicationStageDTO applicationStageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ApplicationStage : {}", applicationStageDTO);
        if (applicationStageDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationStage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationStageDTO = applicationStageService.save(applicationStageDTO);
        return ResponseEntity.created(new URI("/api/application-stages/" + applicationStageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, applicationStageDTO.getId().toString()))
            .body(applicationStageDTO);
    }

    /**
     * {@code PUT  /application-stages/:id} : Updates an existing applicationStage.
     *
     * @param id the id of the applicationStageDTO to save.
     * @param applicationStageDTO the applicationStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationStageDTO,
     * or with status {@code 400 (Bad Request)} if the applicationStageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationStageDTO> updateApplicationStage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationStageDTO applicationStageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApplicationStage : {}, {}", id, applicationStageDTO);
        if (applicationStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationStageDTO = applicationStageService.update(applicationStageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationStageDTO.getId().toString()))
            .body(applicationStageDTO);
    }

    /**
     * {@code PATCH  /application-stages/:id} : Partial updates given fields of an existing applicationStage, field will ignore if it is null
     *
     * @param id the id of the applicationStageDTO to save.
     * @param applicationStageDTO the applicationStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationStageDTO,
     * or with status {@code 400 (Bad Request)} if the applicationStageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationStageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationStageDTO> partialUpdateApplicationStage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationStageDTO applicationStageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApplicationStage partially : {}, {}", id, applicationStageDTO);
        if (applicationStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationStageDTO> result = applicationStageService.partialUpdate(applicationStageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationStageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-stages} : get all the Application Stages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Application Stages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApplicationStageDTO>> getAllApplicationStages(
        ApplicationStageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ApplicationStages by criteria: {}", criteria);

        Page<ApplicationStageDTO> page = applicationStageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /application-stages/count} : count all the applicationStages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countApplicationStages(ApplicationStageCriteria criteria) {
        LOG.debug("REST request to count ApplicationStages by criteria: {}", criteria);
        return ResponseEntity.ok().body(applicationStageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /application-stages/:id} : get the "id" applicationStage.
     *
     * @param id the id of the applicationStageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationStageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationStageDTO> getApplicationStage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApplicationStage : {}", id);
        Optional<ApplicationStageDTO> applicationStageDTO = applicationStageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationStageDTO);
    }

    /**
     * {@code DELETE  /application-stages/:id} : delete the "id" applicationStage.
     *
     * @param id the id of the applicationStageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationStage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApplicationStage : {}", id);
        applicationStageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
