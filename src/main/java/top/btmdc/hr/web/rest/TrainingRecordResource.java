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
import top.btmdc.hr.repository.TrainingRecordRepository;
import top.btmdc.hr.service.TrainingRecordQueryService;
import top.btmdc.hr.service.TrainingRecordService;
import top.btmdc.hr.service.criteria.TrainingRecordCriteria;
import top.btmdc.hr.service.dto.TrainingRecordDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.TrainingRecord}.
 */
@RestController
@RequestMapping("/api/training-records")
public class TrainingRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRecordResource.class);

    private static final String ENTITY_NAME = "trainingRecord";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final TrainingRecordService trainingRecordService;

    private final TrainingRecordRepository trainingRecordRepository;

    private final TrainingRecordQueryService trainingRecordQueryService;

    public TrainingRecordResource(
        TrainingRecordService trainingRecordService,
        TrainingRecordRepository trainingRecordRepository,
        TrainingRecordQueryService trainingRecordQueryService
    ) {
        this.trainingRecordService = trainingRecordService;
        this.trainingRecordRepository = trainingRecordRepository;
        this.trainingRecordQueryService = trainingRecordQueryService;
    }

    /**
     * {@code POST  /training-records} : Create a new trainingRecord.
     *
     * @param trainingRecordDTO the trainingRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingRecordDTO, or with status {@code 400 (Bad Request)} if the trainingRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainingRecordDTO> createTrainingRecord(@Valid @RequestBody TrainingRecordDTO trainingRecordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrainingRecord : {}", trainingRecordDTO);
        if (trainingRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trainingRecordDTO = trainingRecordService.save(trainingRecordDTO);
        return ResponseEntity.created(new URI("/api/training-records/" + trainingRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trainingRecordDTO.getId().toString()))
            .body(trainingRecordDTO);
    }

    /**
     * {@code PUT  /training-records/:id} : Updates an existing trainingRecord.
     *
     * @param id the id of the trainingRecordDTO to save.
     * @param trainingRecordDTO the trainingRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingRecordDTO,
     * or with status {@code 400 (Bad Request)} if the trainingRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingRecordDTO> updateTrainingRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainingRecordDTO trainingRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrainingRecord : {}, {}", id, trainingRecordDTO);
        if (trainingRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trainingRecordDTO = trainingRecordService.update(trainingRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingRecordDTO.getId().toString()))
            .body(trainingRecordDTO);
    }

    /**
     * {@code PATCH  /training-records/:id} : Partial updates given fields of an existing trainingRecord, field will ignore if it is null
     *
     * @param id the id of the trainingRecordDTO to save.
     * @param trainingRecordDTO the trainingRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingRecordDTO,
     * or with status {@code 400 (Bad Request)} if the trainingRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainingRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingRecordDTO> partialUpdateTrainingRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainingRecordDTO trainingRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrainingRecord partially : {}, {}", id, trainingRecordDTO);
        if (trainingRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingRecordDTO> result = trainingRecordService.partialUpdate(trainingRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /training-records} : get all the Training Records.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Training Records in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrainingRecordDTO>> getAllTrainingRecords(
        TrainingRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TrainingRecords by criteria: {}", criteria);

        Page<TrainingRecordDTO> page = trainingRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /training-records/count} : count all the trainingRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrainingRecords(TrainingRecordCriteria criteria) {
        LOG.debug("REST request to count TrainingRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(trainingRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /training-records/:id} : get the "id" trainingRecord.
     *
     * @param id the id of the trainingRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingRecordDTO> getTrainingRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrainingRecord : {}", id);
        Optional<TrainingRecordDTO> trainingRecordDTO = trainingRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainingRecordDTO);
    }

    /**
     * {@code DELETE  /training-records/:id} : delete the "id" trainingRecord.
     *
     * @param id the id of the trainingRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrainingRecord : {}", id);
        trainingRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
