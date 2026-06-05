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
import top.btmdc.hr.repository.SkillUpgradeRecordRepository;
import top.btmdc.hr.service.SkillUpgradeRecordQueryService;
import top.btmdc.hr.service.SkillUpgradeRecordService;
import top.btmdc.hr.service.criteria.SkillUpgradeRecordCriteria;
import top.btmdc.hr.service.dto.SkillUpgradeRecordDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.SkillUpgradeRecord}.
 */
@RestController
@RequestMapping("/api/skill-upgrade-records")
public class SkillUpgradeRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(SkillUpgradeRecordResource.class);

    private static final String ENTITY_NAME = "skillUpgradeRecord";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final SkillUpgradeRecordService skillUpgradeRecordService;

    private final SkillUpgradeRecordRepository skillUpgradeRecordRepository;

    private final SkillUpgradeRecordQueryService skillUpgradeRecordQueryService;

    public SkillUpgradeRecordResource(
        SkillUpgradeRecordService skillUpgradeRecordService,
        SkillUpgradeRecordRepository skillUpgradeRecordRepository,
        SkillUpgradeRecordQueryService skillUpgradeRecordQueryService
    ) {
        this.skillUpgradeRecordService = skillUpgradeRecordService;
        this.skillUpgradeRecordRepository = skillUpgradeRecordRepository;
        this.skillUpgradeRecordQueryService = skillUpgradeRecordQueryService;
    }

    /**
     * {@code POST  /skill-upgrade-records} : Create a new skillUpgradeRecord.
     *
     * @param skillUpgradeRecordDTO the skillUpgradeRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillUpgradeRecordDTO, or with status {@code 400 (Bad Request)} if the skillUpgradeRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SkillUpgradeRecordDTO> createSkillUpgradeRecord(@Valid @RequestBody SkillUpgradeRecordDTO skillUpgradeRecordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SkillUpgradeRecord : {}", skillUpgradeRecordDTO);
        if (skillUpgradeRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new skillUpgradeRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillUpgradeRecordDTO = skillUpgradeRecordService.save(skillUpgradeRecordDTO);
        return ResponseEntity.created(new URI("/api/skill-upgrade-records/" + skillUpgradeRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, skillUpgradeRecordDTO.getId().toString()))
            .body(skillUpgradeRecordDTO);
    }

    /**
     * {@code PUT  /skill-upgrade-records/:id} : Updates an existing skillUpgradeRecord.
     *
     * @param id the id of the skillUpgradeRecordDTO to save.
     * @param skillUpgradeRecordDTO the skillUpgradeRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillUpgradeRecordDTO,
     * or with status {@code 400 (Bad Request)} if the skillUpgradeRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillUpgradeRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillUpgradeRecordDTO> updateSkillUpgradeRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SkillUpgradeRecordDTO skillUpgradeRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SkillUpgradeRecord : {}, {}", id, skillUpgradeRecordDTO);
        if (skillUpgradeRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillUpgradeRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillUpgradeRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillUpgradeRecordDTO = skillUpgradeRecordService.update(skillUpgradeRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillUpgradeRecordDTO.getId().toString()))
            .body(skillUpgradeRecordDTO);
    }

    /**
     * {@code PATCH  /skill-upgrade-records/:id} : Partial updates given fields of an existing skillUpgradeRecord, field will ignore if it is null
     *
     * @param id the id of the skillUpgradeRecordDTO to save.
     * @param skillUpgradeRecordDTO the skillUpgradeRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillUpgradeRecordDTO,
     * or with status {@code 400 (Bad Request)} if the skillUpgradeRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the skillUpgradeRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillUpgradeRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SkillUpgradeRecordDTO> partialUpdateSkillUpgradeRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SkillUpgradeRecordDTO skillUpgradeRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SkillUpgradeRecord partially : {}, {}", id, skillUpgradeRecordDTO);
        if (skillUpgradeRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillUpgradeRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillUpgradeRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillUpgradeRecordDTO> result = skillUpgradeRecordService.partialUpdate(skillUpgradeRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillUpgradeRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /skill-upgrade-records} : get all the Skill Upgrade Records.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Skill Upgrade Records in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SkillUpgradeRecordDTO>> getAllSkillUpgradeRecords(
        SkillUpgradeRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SkillUpgradeRecords by criteria: {}", criteria);

        Page<SkillUpgradeRecordDTO> page = skillUpgradeRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skill-upgrade-records/count} : count all the skillUpgradeRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSkillUpgradeRecords(SkillUpgradeRecordCriteria criteria) {
        LOG.debug("REST request to count SkillUpgradeRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(skillUpgradeRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /skill-upgrade-records/:id} : get the "id" skillUpgradeRecord.
     *
     * @param id the id of the skillUpgradeRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillUpgradeRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillUpgradeRecordDTO> getSkillUpgradeRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SkillUpgradeRecord : {}", id);
        Optional<SkillUpgradeRecordDTO> skillUpgradeRecordDTO = skillUpgradeRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillUpgradeRecordDTO);
    }

    /**
     * {@code DELETE  /skill-upgrade-records/:id} : delete the "id" skillUpgradeRecord.
     *
     * @param id the id of the skillUpgradeRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillUpgradeRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SkillUpgradeRecord : {}", id);
        skillUpgradeRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
