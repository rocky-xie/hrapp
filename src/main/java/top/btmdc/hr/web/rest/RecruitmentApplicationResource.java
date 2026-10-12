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
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.RecruitmentApplicationQueryService;
import top.btmdc.hr.service.RecruitmentApplicationService;
import top.btmdc.hr.service.criteria.RecruitmentApplicationCriteria;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.RecruitmentApplication}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/recruitment-applications")
public class RecruitmentApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentApplicationResource.class);

    private static final String ENTITY_NAME = "recruitmentApplication";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final RecruitmentApplicationService recruitmentApplicationService;

    private final RecruitmentApplicationRepository recruitmentApplicationRepository;

    private final RecruitmentApplicationQueryService recruitmentApplicationQueryService;

    public RecruitmentApplicationResource(
        RecruitmentApplicationService recruitmentApplicationService,
        RecruitmentApplicationRepository recruitmentApplicationRepository,
        RecruitmentApplicationQueryService recruitmentApplicationQueryService
    ) {
        this.recruitmentApplicationService = recruitmentApplicationService;
        this.recruitmentApplicationRepository = recruitmentApplicationRepository;
        this.recruitmentApplicationQueryService = recruitmentApplicationQueryService;
    }

    /**
     * {@code POST  /recruitment-applications} : Create a new recruitmentApplication.
     *
     * @param recruitmentApplicationDTO the recruitmentApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentApplicationDTO, or with status {@code 400 (Bad Request)} if the recruitmentApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecruitmentApplicationDTO> createRecruitmentApplication(
        @Valid @RequestBody RecruitmentApplicationDTO recruitmentApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecruitmentApplication : {}", recruitmentApplicationDTO);
        if (recruitmentApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruitmentApplicationDTO = recruitmentApplicationService.save(recruitmentApplicationDTO);
        return ResponseEntity.created(new URI("/api/recruitment-applications/" + recruitmentApplicationDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, recruitmentApplicationDTO.getId().toString())
            )
            .body(recruitmentApplicationDTO);
    }

    /**
     * {@code PUT  /recruitment-applications/:id} : Updates an existing recruitmentApplication.
     *
     * @param id the id of the recruitmentApplicationDTO to save.
     * @param recruitmentApplicationDTO the recruitmentApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentApplicationDTO> updateRecruitmentApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecruitmentApplicationDTO recruitmentApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecruitmentApplication : {}, {}", id, recruitmentApplicationDTO);
        if (recruitmentApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruitmentApplicationDTO = recruitmentApplicationService.update(recruitmentApplicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentApplicationDTO.getId().toString()))
            .body(recruitmentApplicationDTO);
    }

    /**
     * {@code PATCH  /recruitment-applications/:id} : Partial updates given fields of an existing recruitmentApplication, field will ignore if it is null
     *
     * @param id the id of the recruitmentApplicationDTO to save.
     * @param recruitmentApplicationDTO the recruitmentApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recruitmentApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecruitmentApplicationDTO> partialUpdateRecruitmentApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecruitmentApplicationDTO recruitmentApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecruitmentApplication partially : {}, {}", id, recruitmentApplicationDTO);
        if (recruitmentApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecruitmentApplicationDTO> result = recruitmentApplicationService.partialUpdate(recruitmentApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentApplicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recruitment-applications} : get all the Recruitment Applications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recruitment Applications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getAllRecruitmentApplications(
        RecruitmentApplicationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RecruitmentApplications by criteria: {}", criteria);

        Page<RecruitmentApplicationDTO> page = recruitmentApplicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-applications/count} : count all the recruitmentApplications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecruitmentApplications(RecruitmentApplicationCriteria criteria) {
        LOG.debug("REST request to count RecruitmentApplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(recruitmentApplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recruitment-applications/:id} : get the "id" recruitmentApplication.
     *
     * @param id the id of the recruitmentApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentApplicationDTO> getRecruitmentApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecruitmentApplication : {}", id);
        Optional<RecruitmentApplicationDTO> recruitmentApplicationDTO = recruitmentApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentApplicationDTO);
    }

    /**
     * {@code DELETE  /recruitment-applications/:id} : delete the "id" recruitmentApplication.
     *
     * @param id the id of the recruitmentApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecruitmentApplication : {}", id);
        recruitmentApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
