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
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.service.RecruitmentRequisitionQueryService;
import top.btmdc.hr.service.RecruitmentRequisitionService;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.RecruitmentRequisition}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/recruitment-requisitions")
public class RecruitmentRequisitionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionResource.class);

    private static final String ENTITY_NAME = "recruitmentRequisition";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final RecruitmentRequisitionService recruitmentRequisitionService;

    private final RecruitmentRequisitionRepository recruitmentRequisitionRepository;

    private final RecruitmentRequisitionQueryService recruitmentRequisitionQueryService;

    public RecruitmentRequisitionResource(
        RecruitmentRequisitionService recruitmentRequisitionService,
        RecruitmentRequisitionRepository recruitmentRequisitionRepository,
        RecruitmentRequisitionQueryService recruitmentRequisitionQueryService
    ) {
        this.recruitmentRequisitionService = recruitmentRequisitionService;
        this.recruitmentRequisitionRepository = recruitmentRequisitionRepository;
        this.recruitmentRequisitionQueryService = recruitmentRequisitionQueryService;
    }

    /**
     * {@code POST  /recruitment-requisitions} : Create a new recruitmentRequisition.
     *
     * @param recruitmentRequisitionDTO the recruitmentRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentRequisitionDTO, or with status {@code 400 (Bad Request)} if the recruitmentRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecruitmentRequisitionDTO> createRecruitmentRequisition(
        @Valid @RequestBody RecruitmentRequisitionDTO recruitmentRequisitionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecruitmentRequisition : {}", recruitmentRequisitionDTO);
        if (recruitmentRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruitmentRequisitionDTO = recruitmentRequisitionService.save(recruitmentRequisitionDTO);
        return ResponseEntity.created(new URI("/api/recruitment-requisitions/" + recruitmentRequisitionDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionDTO.getId().toString())
            )
            .body(recruitmentRequisitionDTO);
    }

    /**
     * {@code PUT  /recruitment-requisitions/:id} : Updates an existing recruitmentRequisition.
     *
     * @param id the id of the recruitmentRequisitionDTO to save.
     * @param recruitmentRequisitionDTO the recruitmentRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionDTO> updateRecruitmentRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecruitmentRequisitionDTO recruitmentRequisitionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecruitmentRequisition : {}, {}", id, recruitmentRequisitionDTO);
        if (recruitmentRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruitmentRequisitionDTO = recruitmentRequisitionService.update(recruitmentRequisitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionDTO.getId().toString()))
            .body(recruitmentRequisitionDTO);
    }

    /**
     * {@code PATCH  /recruitment-requisitions/:id} : Partial updates given fields of an existing recruitmentRequisition, field will ignore if it is null
     *
     * @param id the id of the recruitmentRequisitionDTO to save.
     * @param recruitmentRequisitionDTO the recruitmentRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recruitmentRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecruitmentRequisitionDTO> partialUpdateRecruitmentRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecruitmentRequisitionDTO recruitmentRequisitionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecruitmentRequisition partially : {}, {}", id, recruitmentRequisitionDTO);
        if (recruitmentRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecruitmentRequisitionDTO> result = recruitmentRequisitionService.partialUpdate(recruitmentRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recruitment-requisitions} : get all the Recruitment Requisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recruitment Requisitions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitmentRequisitionDTO>> getAllRecruitmentRequisitions(
        RecruitmentRequisitionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RecruitmentRequisitions by criteria: {}", criteria);

        Page<RecruitmentRequisitionDTO> page = recruitmentRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-requisitions/count} : count all the recruitmentRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecruitmentRequisitions(RecruitmentRequisitionCriteria criteria) {
        LOG.debug("REST request to count RecruitmentRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(recruitmentRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recruitment-requisitions/:id} : get the "id" recruitmentRequisition.
     *
     * @param id the id of the recruitmentRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionDTO> getRecruitmentRequisition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecruitmentRequisition : {}", id);
        Optional<RecruitmentRequisitionDTO> recruitmentRequisitionDTO = recruitmentRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionDTO);
    }

    /**
     * {@code DELETE  /recruitment-requisitions/:id} : delete the "id" recruitmentRequisition.
     *
     * @param id the id of the recruitmentRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequisition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecruitmentRequisition : {}", id);
        recruitmentRequisitionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
