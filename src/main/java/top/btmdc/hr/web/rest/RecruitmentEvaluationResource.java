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
import top.btmdc.hr.repository.RecruitmentEvaluationRepository;
import top.btmdc.hr.service.RecruitmentEvaluationQueryService;
import top.btmdc.hr.service.RecruitmentEvaluationService;
import top.btmdc.hr.service.criteria.RecruitmentEvaluationCriteria;
import top.btmdc.hr.service.dto.RecruitmentEvaluationDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.RecruitmentEvaluation}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/recruitment-evaluations")
public class RecruitmentEvaluationResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentEvaluationResource.class);

    private static final String ENTITY_NAME = "recruitmentEvaluation";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final RecruitmentEvaluationService recruitmentEvaluationService;

    private final RecruitmentEvaluationRepository recruitmentEvaluationRepository;

    private final RecruitmentEvaluationQueryService recruitmentEvaluationQueryService;

    public RecruitmentEvaluationResource(
        RecruitmentEvaluationService recruitmentEvaluationService,
        RecruitmentEvaluationRepository recruitmentEvaluationRepository,
        RecruitmentEvaluationQueryService recruitmentEvaluationQueryService
    ) {
        this.recruitmentEvaluationService = recruitmentEvaluationService;
        this.recruitmentEvaluationRepository = recruitmentEvaluationRepository;
        this.recruitmentEvaluationQueryService = recruitmentEvaluationQueryService;
    }

    /**
     * {@code POST  /recruitment-evaluations} : Create a new recruitmentEvaluation.
     *
     * @param recruitmentEvaluationDTO the recruitmentEvaluationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentEvaluationDTO, or with status {@code 400 (Bad Request)} if the recruitmentEvaluation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecruitmentEvaluationDTO> createRecruitmentEvaluation(
        @Valid @RequestBody RecruitmentEvaluationDTO recruitmentEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecruitmentEvaluation : {}", recruitmentEvaluationDTO);
        if (recruitmentEvaluationDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentEvaluation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruitmentEvaluationDTO = recruitmentEvaluationService.save(recruitmentEvaluationDTO);
        return ResponseEntity.created(new URI("/api/recruitment-evaluations/" + recruitmentEvaluationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, recruitmentEvaluationDTO.getId().toString()))
            .body(recruitmentEvaluationDTO);
    }

    /**
     * {@code PUT  /recruitment-evaluations/:id} : Updates an existing recruitmentEvaluation.
     *
     * @param id the id of the recruitmentEvaluationDTO to save.
     * @param recruitmentEvaluationDTO the recruitmentEvaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentEvaluationDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentEvaluationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentEvaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentEvaluationDTO> updateRecruitmentEvaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecruitmentEvaluationDTO recruitmentEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecruitmentEvaluation : {}, {}", id, recruitmentEvaluationDTO);
        if (recruitmentEvaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentEvaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentEvaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruitmentEvaluationDTO = recruitmentEvaluationService.update(recruitmentEvaluationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentEvaluationDTO.getId().toString()))
            .body(recruitmentEvaluationDTO);
    }

    /**
     * {@code PATCH  /recruitment-evaluations/:id} : Partial updates given fields of an existing recruitmentEvaluation, field will ignore if it is null
     *
     * @param id the id of the recruitmentEvaluationDTO to save.
     * @param recruitmentEvaluationDTO the recruitmentEvaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentEvaluationDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentEvaluationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recruitmentEvaluationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentEvaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecruitmentEvaluationDTO> partialUpdateRecruitmentEvaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecruitmentEvaluationDTO recruitmentEvaluationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecruitmentEvaluation partially : {}, {}", id, recruitmentEvaluationDTO);
        if (recruitmentEvaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentEvaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentEvaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecruitmentEvaluationDTO> result = recruitmentEvaluationService.partialUpdate(recruitmentEvaluationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentEvaluationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recruitment-evaluations} : get all the Recruitment Evaluations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recruitment Evaluations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitmentEvaluationDTO>> getAllRecruitmentEvaluations(
        RecruitmentEvaluationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RecruitmentEvaluations by criteria: {}", criteria);

        Page<RecruitmentEvaluationDTO> page = recruitmentEvaluationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-evaluations/count} : count all the recruitmentEvaluations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecruitmentEvaluations(RecruitmentEvaluationCriteria criteria) {
        LOG.debug("REST request to count RecruitmentEvaluations by criteria: {}", criteria);
        return ResponseEntity.ok().body(recruitmentEvaluationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recruitment-evaluations/:id} : get the "id" recruitmentEvaluation.
     *
     * @param id the id of the recruitmentEvaluationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentEvaluationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentEvaluationDTO> getRecruitmentEvaluation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecruitmentEvaluation : {}", id);
        Optional<RecruitmentEvaluationDTO> recruitmentEvaluationDTO = recruitmentEvaluationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentEvaluationDTO);
    }

    /**
     * {@code DELETE  /recruitment-evaluations/:id} : delete the "id" recruitmentEvaluation.
     *
     * @param id the id of the recruitmentEvaluationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentEvaluation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecruitmentEvaluation : {}", id);
        recruitmentEvaluationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
