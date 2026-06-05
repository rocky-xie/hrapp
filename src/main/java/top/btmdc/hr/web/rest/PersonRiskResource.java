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
import top.btmdc.hr.repository.PersonRiskRepository;
import top.btmdc.hr.service.PersonRiskQueryService;
import top.btmdc.hr.service.PersonRiskService;
import top.btmdc.hr.service.criteria.PersonRiskCriteria;
import top.btmdc.hr.service.dto.PersonRiskDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.PersonRisk}.
 */
@RestController
@RequestMapping("/api/person-risks")
public class PersonRiskResource {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRiskResource.class);

    private static final String ENTITY_NAME = "personRisk";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final PersonRiskService personRiskService;

    private final PersonRiskRepository personRiskRepository;

    private final PersonRiskQueryService personRiskQueryService;

    public PersonRiskResource(
        PersonRiskService personRiskService,
        PersonRiskRepository personRiskRepository,
        PersonRiskQueryService personRiskQueryService
    ) {
        this.personRiskService = personRiskService;
        this.personRiskRepository = personRiskRepository;
        this.personRiskQueryService = personRiskQueryService;
    }

    /**
     * {@code POST  /person-risks} : Create a new personRisk.
     *
     * @param personRiskDTO the personRiskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personRiskDTO, or with status {@code 400 (Bad Request)} if the personRisk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonRiskDTO> createPersonRisk(@Valid @RequestBody PersonRiskDTO personRiskDTO) throws URISyntaxException {
        LOG.debug("REST request to save PersonRisk : {}", personRiskDTO);
        if (personRiskDTO.getId() != null) {
            throw new BadRequestAlertException("A new personRisk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personRiskDTO = personRiskService.save(personRiskDTO);
        return ResponseEntity.created(new URI("/api/person-risks/" + personRiskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, personRiskDTO.getId().toString()))
            .body(personRiskDTO);
    }

    /**
     * {@code PUT  /person-risks/:id} : Updates an existing personRisk.
     *
     * @param id the id of the personRiskDTO to save.
     * @param personRiskDTO the personRiskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personRiskDTO,
     * or with status {@code 400 (Bad Request)} if the personRiskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personRiskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonRiskDTO> updatePersonRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonRiskDTO personRiskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PersonRisk : {}, {}", id, personRiskDTO);
        if (personRiskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personRiskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personRiskDTO = personRiskService.update(personRiskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personRiskDTO.getId().toString()))
            .body(personRiskDTO);
    }

    /**
     * {@code PATCH  /person-risks/:id} : Partial updates given fields of an existing personRisk, field will ignore if it is null
     *
     * @param id the id of the personRiskDTO to save.
     * @param personRiskDTO the personRiskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personRiskDTO,
     * or with status {@code 400 (Bad Request)} if the personRiskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personRiskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personRiskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonRiskDTO> partialUpdatePersonRisk(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonRiskDTO personRiskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PersonRisk partially : {}, {}", id, personRiskDTO);
        if (personRiskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personRiskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRiskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonRiskDTO> result = personRiskService.partialUpdate(personRiskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personRiskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /person-risks} : get all the Person Risks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Person Risks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PersonRiskDTO>> getAllPersonRisks(
        PersonRiskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PersonRisks by criteria: {}", criteria);

        Page<PersonRiskDTO> page = personRiskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-risks/count} : count all the personRisks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPersonRisks(PersonRiskCriteria criteria) {
        LOG.debug("REST request to count PersonRisks by criteria: {}", criteria);
        return ResponseEntity.ok().body(personRiskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-risks/:id} : get the "id" personRisk.
     *
     * @param id the id of the personRiskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personRiskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonRiskDTO> getPersonRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PersonRisk : {}", id);
        Optional<PersonRiskDTO> personRiskDTO = personRiskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personRiskDTO);
    }

    /**
     * {@code DELETE  /person-risks/:id} : delete the "id" personRisk.
     *
     * @param id the id of the personRiskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonRisk(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PersonRisk : {}", id);
        personRiskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
