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
import top.btmdc.hr.repository.KeyResponsibilityCategoryRepository;
import top.btmdc.hr.service.KeyResponsibilityCategoryQueryService;
import top.btmdc.hr.service.KeyResponsibilityCategoryService;
import top.btmdc.hr.service.criteria.KeyResponsibilityCategoryCriteria;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.KeyResponsibilityCategory}.
 */
@RestController
@RequestMapping("/api/key-responsibility-categories")
public class KeyResponsibilityCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(KeyResponsibilityCategoryResource.class);

    private static final String ENTITY_NAME = "keyResponsibilityCategory";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final KeyResponsibilityCategoryService keyResponsibilityCategoryService;

    private final KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository;

    private final KeyResponsibilityCategoryQueryService keyResponsibilityCategoryQueryService;

    public KeyResponsibilityCategoryResource(
        KeyResponsibilityCategoryService keyResponsibilityCategoryService,
        KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository,
        KeyResponsibilityCategoryQueryService keyResponsibilityCategoryQueryService
    ) {
        this.keyResponsibilityCategoryService = keyResponsibilityCategoryService;
        this.keyResponsibilityCategoryRepository = keyResponsibilityCategoryRepository;
        this.keyResponsibilityCategoryQueryService = keyResponsibilityCategoryQueryService;
    }

    /**
     * {@code POST  /key-responsibility-categories} : Create a new keyResponsibilityCategory.
     *
     * @param keyResponsibilityCategoryDTO the keyResponsibilityCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keyResponsibilityCategoryDTO, or with status {@code 400 (Bad Request)} if the keyResponsibilityCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KeyResponsibilityCategoryDTO> createKeyResponsibilityCategory(
        @Valid @RequestBody KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save KeyResponsibilityCategory : {}", keyResponsibilityCategoryDTO);
        if (keyResponsibilityCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new keyResponsibilityCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        keyResponsibilityCategoryDTO = keyResponsibilityCategoryService.save(keyResponsibilityCategoryDTO);
        return ResponseEntity.created(new URI("/api/key-responsibility-categories/" + keyResponsibilityCategoryDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, keyResponsibilityCategoryDTO.getId().toString())
            )
            .body(keyResponsibilityCategoryDTO);
    }

    /**
     * {@code PUT  /key-responsibility-categories/:id} : Updates an existing keyResponsibilityCategory.
     *
     * @param id the id of the keyResponsibilityCategoryDTO to save.
     * @param keyResponsibilityCategoryDTO the keyResponsibilityCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyResponsibilityCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the keyResponsibilityCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keyResponsibilityCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KeyResponsibilityCategoryDTO> updateKeyResponsibilityCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update KeyResponsibilityCategory : {}, {}", id, keyResponsibilityCategoryDTO);
        if (keyResponsibilityCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keyResponsibilityCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keyResponsibilityCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        keyResponsibilityCategoryDTO = keyResponsibilityCategoryService.update(keyResponsibilityCategoryDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, keyResponsibilityCategoryDTO.getId().toString())
            )
            .body(keyResponsibilityCategoryDTO);
    }

    /**
     * {@code PATCH  /key-responsibility-categories/:id} : Partial updates given fields of an existing keyResponsibilityCategory, field will ignore if it is null
     *
     * @param id the id of the keyResponsibilityCategoryDTO to save.
     * @param keyResponsibilityCategoryDTO the keyResponsibilityCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyResponsibilityCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the keyResponsibilityCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the keyResponsibilityCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the keyResponsibilityCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KeyResponsibilityCategoryDTO> partialUpdateKeyResponsibilityCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KeyResponsibilityCategory partially : {}, {}", id, keyResponsibilityCategoryDTO);
        if (keyResponsibilityCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keyResponsibilityCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keyResponsibilityCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KeyResponsibilityCategoryDTO> result = keyResponsibilityCategoryService.partialUpdate(keyResponsibilityCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, keyResponsibilityCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /key-responsibility-categories} : get all the Key Responsibility Categories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Key Responsibility Categories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KeyResponsibilityCategoryDTO>> getAllKeyResponsibilityCategories(
        KeyResponsibilityCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get KeyResponsibilityCategories by criteria: {}", criteria);

        Page<KeyResponsibilityCategoryDTO> page = keyResponsibilityCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /key-responsibility-categories/count} : count all the keyResponsibilityCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countKeyResponsibilityCategories(KeyResponsibilityCategoryCriteria criteria) {
        LOG.debug("REST request to count KeyResponsibilityCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(keyResponsibilityCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /key-responsibility-categories/:id} : get the "id" keyResponsibilityCategory.
     *
     * @param id the id of the keyResponsibilityCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keyResponsibilityCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KeyResponsibilityCategoryDTO> getKeyResponsibilityCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KeyResponsibilityCategory : {}", id);
        Optional<KeyResponsibilityCategoryDTO> keyResponsibilityCategoryDTO = keyResponsibilityCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keyResponsibilityCategoryDTO);
    }

    /**
     * {@code DELETE  /key-responsibility-categories/:id} : delete the "id" keyResponsibilityCategory.
     *
     * @param id the id of the keyResponsibilityCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyResponsibilityCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KeyResponsibilityCategory : {}", id);
        keyResponsibilityCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
