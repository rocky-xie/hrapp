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
import top.btmdc.hr.repository.PartnerOrganizationRepository;
import top.btmdc.hr.service.PartnerOrganizationQueryService;
import top.btmdc.hr.service.PartnerOrganizationService;
import top.btmdc.hr.service.criteria.PartnerOrganizationCriteria;
import top.btmdc.hr.service.dto.PartnerOrganizationDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PartnerOrganization}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/partner-organizations")
public class PartnerOrganizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerOrganizationResource.class);

    private static final String ENTITY_NAME = "partnerOrganization";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PartnerOrganizationService partnerOrganizationService;

    private final PartnerOrganizationRepository partnerOrganizationRepository;

    private final PartnerOrganizationQueryService partnerOrganizationQueryService;

    public PartnerOrganizationResource(
        PartnerOrganizationService partnerOrganizationService,
        PartnerOrganizationRepository partnerOrganizationRepository,
        PartnerOrganizationQueryService partnerOrganizationQueryService
    ) {
        this.partnerOrganizationService = partnerOrganizationService;
        this.partnerOrganizationRepository = partnerOrganizationRepository;
        this.partnerOrganizationQueryService = partnerOrganizationQueryService;
    }

    /**
     * {@code POST  /partner-organizations} : Create a new partnerOrganization.
     *
     * @param partnerOrganizationDTO the partnerOrganizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerOrganizationDTO, or with status {@code 400 (Bad Request)} if the partnerOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartnerOrganizationDTO> createPartnerOrganization(
        @Valid @RequestBody PartnerOrganizationDTO partnerOrganizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PartnerOrganization : {}", partnerOrganizationDTO);
        if (partnerOrganizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new partnerOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partnerOrganizationDTO = partnerOrganizationService.save(partnerOrganizationDTO);
        return ResponseEntity.created(new URI("/api/partner-organizations/" + partnerOrganizationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, partnerOrganizationDTO.getId().toString()))
            .body(partnerOrganizationDTO);
    }

    /**
     * {@code PUT  /partner-organizations/:id} : Updates an existing partnerOrganization.
     *
     * @param id the id of the partnerOrganizationDTO to save.
     * @param partnerOrganizationDTO the partnerOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the partnerOrganizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartnerOrganizationDTO> updatePartnerOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartnerOrganizationDTO partnerOrganizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartnerOrganization : {}, {}", id, partnerOrganizationDTO);
        if (partnerOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partnerOrganizationDTO = partnerOrganizationService.update(partnerOrganizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partnerOrganizationDTO.getId().toString()))
            .body(partnerOrganizationDTO);
    }

    /**
     * {@code PATCH  /partner-organizations/:id} : Partial updates given fields of an existing partnerOrganization, field will ignore if it is null
     *
     * @param id the id of the partnerOrganizationDTO to save.
     * @param partnerOrganizationDTO the partnerOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the partnerOrganizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partnerOrganizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partnerOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartnerOrganizationDTO> partialUpdatePartnerOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartnerOrganizationDTO partnerOrganizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PartnerOrganization partially : {}, {}", id, partnerOrganizationDTO);
        if (partnerOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartnerOrganizationDTO> result = partnerOrganizationService.partialUpdate(partnerOrganizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partnerOrganizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /partner-organizations} : get all the Partner Organizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Partner Organizations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartnerOrganizationDTO>> getAllPartnerOrganizations(
        PartnerOrganizationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartnerOrganizations by criteria: {}", criteria);

        Page<PartnerOrganizationDTO> page = partnerOrganizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partner-organizations/count} : count all the partnerOrganizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartnerOrganizations(PartnerOrganizationCriteria criteria) {
        LOG.debug("REST request to count PartnerOrganizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(partnerOrganizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /partner-organizations/:id} : get the "id" partnerOrganization.
     *
     * @param id the id of the partnerOrganizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerOrganizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartnerOrganizationDTO> getPartnerOrganization(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartnerOrganization : {}", id);
        Optional<PartnerOrganizationDTO> partnerOrganizationDTO = partnerOrganizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partnerOrganizationDTO);
    }

    /**
     * {@code DELETE  /partner-organizations/:id} : delete the "id" partnerOrganization.
     *
     * @param id the id of the partnerOrganizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartnerOrganization(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartnerOrganization : {}", id);
        partnerOrganizationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
