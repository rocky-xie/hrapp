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
import top.btmdc.hr.repository.SkillLevelRepository;
import top.btmdc.hr.service.SkillLevelQueryService;
import top.btmdc.hr.service.SkillLevelService;
import top.btmdc.hr.service.criteria.SkillLevelCriteria;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.SkillLevel}.
 */
@RestController
@RequestMapping("/api/skill-levels")
public class SkillLevelResource {

    private static final Logger LOG = LoggerFactory.getLogger(SkillLevelResource.class);

    private static final String ENTITY_NAME = "skillLevel";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final SkillLevelService skillLevelService;

    private final SkillLevelRepository skillLevelRepository;

    private final SkillLevelQueryService skillLevelQueryService;

    public SkillLevelResource(
        SkillLevelService skillLevelService,
        SkillLevelRepository skillLevelRepository,
        SkillLevelQueryService skillLevelQueryService
    ) {
        this.skillLevelService = skillLevelService;
        this.skillLevelRepository = skillLevelRepository;
        this.skillLevelQueryService = skillLevelQueryService;
    }

    /**
     * {@code POST  /skill-levels} : Create a new skillLevel.
     *
     * @param skillLevelDTO the skillLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillLevelDTO, or with status {@code 400 (Bad Request)} if the skillLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SkillLevelDTO> createSkillLevel(@Valid @RequestBody SkillLevelDTO skillLevelDTO) throws URISyntaxException {
        LOG.debug("REST request to save SkillLevel : {}", skillLevelDTO);
        if (skillLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new skillLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillLevelDTO = skillLevelService.save(skillLevelDTO);
        return ResponseEntity.created(new URI("/api/skill-levels/" + skillLevelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, skillLevelDTO.getId().toString()))
            .body(skillLevelDTO);
    }

    /**
     * {@code PUT  /skill-levels/:id} : Updates an existing skillLevel.
     *
     * @param id the id of the skillLevelDTO to save.
     * @param skillLevelDTO the skillLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillLevelDTO,
     * or with status {@code 400 (Bad Request)} if the skillLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillLevelDTO> updateSkillLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SkillLevelDTO skillLevelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SkillLevel : {}, {}", id, skillLevelDTO);
        if (skillLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillLevelDTO = skillLevelService.update(skillLevelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillLevelDTO.getId().toString()))
            .body(skillLevelDTO);
    }

    /**
     * {@code PATCH  /skill-levels/:id} : Partial updates given fields of an existing skillLevel, field will ignore if it is null
     *
     * @param id the id of the skillLevelDTO to save.
     * @param skillLevelDTO the skillLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillLevelDTO,
     * or with status {@code 400 (Bad Request)} if the skillLevelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the skillLevelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SkillLevelDTO> partialUpdateSkillLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SkillLevelDTO skillLevelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SkillLevel partially : {}, {}", id, skillLevelDTO);
        if (skillLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillLevelDTO> result = skillLevelService.partialUpdate(skillLevelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skillLevelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /skill-levels} : get all the Skill Levels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Skill Levels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SkillLevelDTO>> getAllSkillLevels(
        SkillLevelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SkillLevels by criteria: {}", criteria);

        Page<SkillLevelDTO> page = skillLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skill-levels/count} : count all the skillLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSkillLevels(SkillLevelCriteria criteria) {
        LOG.debug("REST request to count SkillLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(skillLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /skill-levels/:id} : get the "id" skillLevel.
     *
     * @param id the id of the skillLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillLevelDTO> getSkillLevel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SkillLevel : {}", id);
        Optional<SkillLevelDTO> skillLevelDTO = skillLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillLevelDTO);
    }

    /**
     * {@code DELETE  /skill-levels/:id} : delete the "id" skillLevel.
     *
     * @param id the id of the skillLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillLevel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SkillLevel : {}", id);
        skillLevelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
