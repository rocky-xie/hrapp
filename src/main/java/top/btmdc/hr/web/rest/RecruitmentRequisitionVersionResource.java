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
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.service.RecruitmentRequisitionVersionQueryService;
import top.btmdc.hr.service.RecruitmentRequisitionVersionService;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionVersionCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.RecruitmentRequisitionVersion}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/recruitment-requisition-versions")
public class RecruitmentRequisitionVersionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionVersionResource.class);

    private static final String ENTITY_NAME = "recruitmentRequisitionVersion";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final RecruitmentRequisitionVersionService recruitmentRequisitionVersionService;

    private final RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository;

    private final RecruitmentRequisitionVersionQueryService recruitmentRequisitionVersionQueryService;

    public RecruitmentRequisitionVersionResource(
        RecruitmentRequisitionVersionService recruitmentRequisitionVersionService,
        RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository,
        RecruitmentRequisitionVersionQueryService recruitmentRequisitionVersionQueryService
    ) {
        this.recruitmentRequisitionVersionService = recruitmentRequisitionVersionService;
        this.recruitmentRequisitionVersionRepository = recruitmentRequisitionVersionRepository;
        this.recruitmentRequisitionVersionQueryService = recruitmentRequisitionVersionQueryService;
    }

    /**
     * {@code POST  /recruitment-requisition-versions} : Create a new recruitmentRequisitionVersion.
     *
     * @param recruitmentRequisitionVersionDTO the recruitmentRequisitionVersionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentRequisitionVersionDTO, or with status {@code 400 (Bad Request)} if the recruitmentRequisitionVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecruitmentRequisitionVersionDTO> createRecruitmentRequisitionVersion(
        @Valid @RequestBody RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecruitmentRequisitionVersion : {}", recruitmentRequisitionVersionDTO);
        if (recruitmentRequisitionVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionService.save(recruitmentRequisitionVersionDTO);
        return ResponseEntity.created(new URI("/api/recruitment-requisition-versions/" + recruitmentRequisitionVersionDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    recruitmentRequisitionVersionDTO.getId().toString()
                )
            )
            .body(recruitmentRequisitionVersionDTO);
    }

    /**
     * {@code PUT  /recruitment-requisition-versions/:id} : Updates an existing recruitmentRequisitionVersion.
     *
     * @param id the id of the recruitmentRequisitionVersionDTO to save.
     * @param recruitmentRequisitionVersionDTO the recruitmentRequisitionVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionVersionDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionVersionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionVersionDTO> updateRecruitmentRequisitionVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecruitmentRequisitionVersion : {}, {}", id, recruitmentRequisitionVersionDTO);
        if (recruitmentRequisitionVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionService.update(recruitmentRequisitionVersionDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionVersionDTO.getId().toString())
            )
            .body(recruitmentRequisitionVersionDTO);
    }

    /**
     * {@code PATCH  /recruitment-requisition-versions/:id} : Partial updates given fields of an existing recruitmentRequisitionVersion, field will ignore if it is null
     *
     * @param id the id of the recruitmentRequisitionVersionDTO to save.
     * @param recruitmentRequisitionVersionDTO the recruitmentRequisitionVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionVersionDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionVersionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recruitmentRequisitionVersionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecruitmentRequisitionVersionDTO> partialUpdateRecruitmentRequisitionVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecruitmentRequisitionVersion partially : {}, {}", id, recruitmentRequisitionVersionDTO);
        if (recruitmentRequisitionVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecruitmentRequisitionVersionDTO> result = recruitmentRequisitionVersionService.partialUpdate(
            recruitmentRequisitionVersionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionVersionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recruitment-requisition-versions} : get all the Recruitment Requisition Versions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recruitment Requisition Versions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitmentRequisitionVersionDTO>> getAllRecruitmentRequisitionVersions(
        RecruitmentRequisitionVersionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RecruitmentRequisitionVersions by criteria: {}", criteria);

        Page<RecruitmentRequisitionVersionDTO> page = recruitmentRequisitionVersionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-requisition-versions/count} : count all the recruitmentRequisitionVersions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecruitmentRequisitionVersions(RecruitmentRequisitionVersionCriteria criteria) {
        LOG.debug("REST request to count RecruitmentRequisitionVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(recruitmentRequisitionVersionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recruitment-requisition-versions/:id} : get the "id" recruitmentRequisitionVersion.
     *
     * @param id the id of the recruitmentRequisitionVersionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentRequisitionVersionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionVersionDTO> getRecruitmentRequisitionVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecruitmentRequisitionVersion : {}", id);
        Optional<RecruitmentRequisitionVersionDTO> recruitmentRequisitionVersionDTO = recruitmentRequisitionVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionVersionDTO);
    }

    /**
     * {@code DELETE  /recruitment-requisition-versions/:id} : delete the "id" recruitmentRequisitionVersion.
     *
     * @param id the id of the recruitmentRequisitionVersionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequisitionVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecruitmentRequisitionVersion : {}", id);
        recruitmentRequisitionVersionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
