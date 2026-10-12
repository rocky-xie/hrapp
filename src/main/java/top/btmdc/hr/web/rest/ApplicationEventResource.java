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
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.service.ApplicationEventQueryService;
import top.btmdc.hr.service.ApplicationEventService;
import top.btmdc.hr.service.criteria.ApplicationEventCriteria;
import top.btmdc.hr.service.dto.ApplicationEventDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.ApplicationEvent}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/application-events")
public class ApplicationEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventResource.class);

    private static final String ENTITY_NAME = "applicationEvent";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final ApplicationEventService applicationEventService;

    private final ApplicationEventRepository applicationEventRepository;

    private final ApplicationEventQueryService applicationEventQueryService;

    public ApplicationEventResource(
        ApplicationEventService applicationEventService,
        ApplicationEventRepository applicationEventRepository,
        ApplicationEventQueryService applicationEventQueryService
    ) {
        this.applicationEventService = applicationEventService;
        this.applicationEventRepository = applicationEventRepository;
        this.applicationEventQueryService = applicationEventQueryService;
    }

    /**
     * {@code POST  /application-events} : Create a new applicationEvent.
     *
     * @param applicationEventDTO the applicationEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationEventDTO, or with status {@code 400 (Bad Request)} if the applicationEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationEventDTO> createApplicationEvent(@Valid @RequestBody ApplicationEventDTO applicationEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ApplicationEvent : {}", applicationEventDTO);
        if (applicationEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationEventDTO = applicationEventService.save(applicationEventDTO);
        return ResponseEntity.created(new URI("/api/application-events/" + applicationEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, applicationEventDTO.getId().toString()))
            .body(applicationEventDTO);
    }

    /**
     * {@code PUT  /application-events/:id} : Updates an existing applicationEvent.
     *
     * @param id the id of the applicationEventDTO to save.
     * @param applicationEventDTO the applicationEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationEventDTO,
     * or with status {@code 400 (Bad Request)} if the applicationEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationEventDTO> updateApplicationEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationEventDTO applicationEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApplicationEvent : {}, {}", id, applicationEventDTO);
        if (applicationEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationEventDTO = applicationEventService.update(applicationEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationEventDTO.getId().toString()))
            .body(applicationEventDTO);
    }

    /**
     * {@code PATCH  /application-events/:id} : Partial updates given fields of an existing applicationEvent, field will ignore if it is null
     *
     * @param id the id of the applicationEventDTO to save.
     * @param applicationEventDTO the applicationEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationEventDTO,
     * or with status {@code 400 (Bad Request)} if the applicationEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationEventDTO> partialUpdateApplicationEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationEventDTO applicationEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApplicationEvent partially : {}, {}", id, applicationEventDTO);
        if (applicationEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationEventDTO> result = applicationEventService.partialUpdate(applicationEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-events} : get all the Application Events.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Application Events in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApplicationEventDTO>> getAllApplicationEvents(
        ApplicationEventCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ApplicationEvents by criteria: {}", criteria);

        Page<ApplicationEventDTO> page = applicationEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /application-events/count} : count all the applicationEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countApplicationEvents(ApplicationEventCriteria criteria) {
        LOG.debug("REST request to count ApplicationEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(applicationEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /application-events/:id} : get the "id" applicationEvent.
     *
     * @param id the id of the applicationEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationEventDTO> getApplicationEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApplicationEvent : {}", id);
        Optional<ApplicationEventDTO> applicationEventDTO = applicationEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationEventDTO);
    }

    /**
     * {@code DELETE  /application-events/:id} : delete the "id" applicationEvent.
     *
     * @param id the id of the applicationEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApplicationEvent : {}", id);
        applicationEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
