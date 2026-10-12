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
import top.btmdc.hr.repository.RecruitmentRequisitionMemberRepository;
import top.btmdc.hr.service.RecruitmentRequisitionMemberQueryService;
import top.btmdc.hr.service.RecruitmentRequisitionMemberService;
import top.btmdc.hr.service.criteria.RecruitmentRequisitionMemberCriteria;
import top.btmdc.hr.service.dto.RecruitmentRequisitionMemberDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.RecruitmentRequisitionMember}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/recruitment-requisition-members")
public class RecruitmentRequisitionMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionMemberResource.class);

    private static final String ENTITY_NAME = "recruitmentRequisitionMember";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final RecruitmentRequisitionMemberService recruitmentRequisitionMemberService;

    private final RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository;

    private final RecruitmentRequisitionMemberQueryService recruitmentRequisitionMemberQueryService;

    public RecruitmentRequisitionMemberResource(
        RecruitmentRequisitionMemberService recruitmentRequisitionMemberService,
        RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository,
        RecruitmentRequisitionMemberQueryService recruitmentRequisitionMemberQueryService
    ) {
        this.recruitmentRequisitionMemberService = recruitmentRequisitionMemberService;
        this.recruitmentRequisitionMemberRepository = recruitmentRequisitionMemberRepository;
        this.recruitmentRequisitionMemberQueryService = recruitmentRequisitionMemberQueryService;
    }

    /**
     * {@code POST  /recruitment-requisition-members} : Create a new recruitmentRequisitionMember.
     *
     * @param recruitmentRequisitionMemberDTO the recruitmentRequisitionMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentRequisitionMemberDTO, or with status {@code 400 (Bad Request)} if the recruitmentRequisitionMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecruitmentRequisitionMemberDTO> createRecruitmentRequisitionMember(
        @Valid @RequestBody RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save RecruitmentRequisitionMember : {}", recruitmentRequisitionMemberDTO);
        if (recruitmentRequisitionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberService.save(recruitmentRequisitionMemberDTO);
        return ResponseEntity.created(new URI("/api/recruitment-requisition-members/" + recruitmentRequisitionMemberDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    recruitmentRequisitionMemberDTO.getId().toString()
                )
            )
            .body(recruitmentRequisitionMemberDTO);
    }

    /**
     * {@code PUT  /recruitment-requisition-members/:id} : Updates an existing recruitmentRequisitionMember.
     *
     * @param id the id of the recruitmentRequisitionMemberDTO to save.
     * @param recruitmentRequisitionMemberDTO the recruitmentRequisitionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionMemberDTO> updateRecruitmentRequisitionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecruitmentRequisitionMember : {}, {}", id, recruitmentRequisitionMemberDTO);
        if (recruitmentRequisitionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberService.update(recruitmentRequisitionMemberDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionMemberDTO.getId().toString())
            )
            .body(recruitmentRequisitionMemberDTO);
    }

    /**
     * {@code PATCH  /recruitment-requisition-members/:id} : Partial updates given fields of an existing recruitmentRequisitionMember, field will ignore if it is null
     *
     * @param id the id of the recruitmentRequisitionMemberDTO to save.
     * @param recruitmentRequisitionMemberDTO the recruitmentRequisitionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentRequisitionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the recruitmentRequisitionMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recruitmentRequisitionMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentRequisitionMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecruitmentRequisitionMemberDTO> partialUpdateRecruitmentRequisitionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecruitmentRequisitionMember partially : {}, {}", id, recruitmentRequisitionMemberDTO);
        if (recruitmentRequisitionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruitmentRequisitionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruitmentRequisitionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecruitmentRequisitionMemberDTO> result = recruitmentRequisitionMemberService.partialUpdate(
            recruitmentRequisitionMemberDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recruitmentRequisitionMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recruitment-requisition-members} : get all the Recruitment Requisition Members.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recruitment Requisition Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecruitmentRequisitionMemberDTO>> getAllRecruitmentRequisitionMembers(
        RecruitmentRequisitionMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RecruitmentRequisitionMembers by criteria: {}", criteria);

        Page<RecruitmentRequisitionMemberDTO> page = recruitmentRequisitionMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-requisition-members/count} : count all the recruitmentRequisitionMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecruitmentRequisitionMembers(RecruitmentRequisitionMemberCriteria criteria) {
        LOG.debug("REST request to count RecruitmentRequisitionMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(recruitmentRequisitionMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recruitment-requisition-members/:id} : get the "id" recruitmentRequisitionMember.
     *
     * @param id the id of the recruitmentRequisitionMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentRequisitionMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionMemberDTO> getRecruitmentRequisitionMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecruitmentRequisitionMember : {}", id);
        Optional<RecruitmentRequisitionMemberDTO> recruitmentRequisitionMemberDTO = recruitmentRequisitionMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionMemberDTO);
    }

    /**
     * {@code DELETE  /recruitment-requisition-members/:id} : delete the "id" recruitmentRequisitionMember.
     *
     * @param id the id of the recruitmentRequisitionMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequisitionMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecruitmentRequisitionMember : {}", id);
        recruitmentRequisitionMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
