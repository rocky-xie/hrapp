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
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.TrainingGoalQueryService;
import top.btmdc.hr.service.TrainingGoalService;
import top.btmdc.hr.service.criteria.TrainingGoalCriteria;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.TrainingGoal}.
 */
@RestController
@RequestMapping("/api/training-goals")
public class TrainingGoalResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingGoalResource.class);

    private static final String ENTITY_NAME = "trainingGoal";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final TrainingGoalService trainingGoalService;

    private final TrainingGoalRepository trainingGoalRepository;

    private final TrainingGoalQueryService trainingGoalQueryService;

    public TrainingGoalResource(
        TrainingGoalService trainingGoalService,
        TrainingGoalRepository trainingGoalRepository,
        TrainingGoalQueryService trainingGoalQueryService
    ) {
        this.trainingGoalService = trainingGoalService;
        this.trainingGoalRepository = trainingGoalRepository;
        this.trainingGoalQueryService = trainingGoalQueryService;
    }

    /**
     * {@code POST  /training-goals} : Create a new trainingGoal.
     *
     * @param trainingGoalDTO the trainingGoalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingGoalDTO, or with status {@code 400 (Bad Request)} if the trainingGoal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainingGoalDTO> createTrainingGoal(@Valid @RequestBody TrainingGoalDTO trainingGoalDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrainingGoal : {}", trainingGoalDTO);
        if (trainingGoalDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingGoal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trainingGoalDTO = trainingGoalService.save(trainingGoalDTO);
        return ResponseEntity.created(new URI("/api/training-goals/" + trainingGoalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trainingGoalDTO.getId().toString()))
            .body(trainingGoalDTO);
    }

    /**
     * {@code PUT  /training-goals/:id} : Updates an existing trainingGoal.
     *
     * @param id the id of the trainingGoalDTO to save.
     * @param trainingGoalDTO the trainingGoalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingGoalDTO,
     * or with status {@code 400 (Bad Request)} if the trainingGoalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingGoalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingGoalDTO> updateTrainingGoal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainingGoalDTO trainingGoalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrainingGoal : {}, {}", id, trainingGoalDTO);
        if (trainingGoalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingGoalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingGoalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trainingGoalDTO = trainingGoalService.update(trainingGoalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingGoalDTO.getId().toString()))
            .body(trainingGoalDTO);
    }

    /**
     * {@code PATCH  /training-goals/:id} : Partial updates given fields of an existing trainingGoal, field will ignore if it is null
     *
     * @param id the id of the trainingGoalDTO to save.
     * @param trainingGoalDTO the trainingGoalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingGoalDTO,
     * or with status {@code 400 (Bad Request)} if the trainingGoalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainingGoalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingGoalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingGoalDTO> partialUpdateTrainingGoal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainingGoalDTO trainingGoalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrainingGoal partially : {}, {}", id, trainingGoalDTO);
        if (trainingGoalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingGoalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingGoalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingGoalDTO> result = trainingGoalService.partialUpdate(trainingGoalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingGoalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /training-goals} : get all the Training Goals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Training Goals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrainingGoalDTO>> getAllTrainingGoals(
        TrainingGoalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TrainingGoals by criteria: {}", criteria);

        Page<TrainingGoalDTO> page = trainingGoalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /training-goals/count} : count all the trainingGoals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrainingGoals(TrainingGoalCriteria criteria) {
        LOG.debug("REST request to count TrainingGoals by criteria: {}", criteria);
        return ResponseEntity.ok().body(trainingGoalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /training-goals/:id} : get the "id" trainingGoal.
     *
     * @param id the id of the trainingGoalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingGoalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingGoalDTO> getTrainingGoal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrainingGoal : {}", id);
        Optional<TrainingGoalDTO> trainingGoalDTO = trainingGoalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainingGoalDTO);
    }

    /**
     * {@code POST  /training-goals/{id}/complete} : complete a training goal, creating a SkillAssessment.
     *
     * @param id the id of the trainingGoal to complete.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingGoalDTO.
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<TrainingGoalDTO> completeTrainingGoal(@PathVariable("id") Long id) {
        LOG.debug("REST request to complete TrainingGoal : {}", id);
        TrainingGoalDTO result = trainingGoalService.completeGoal(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /training-goals/:id} : delete the "id" trainingGoal.
     *
     * @param id the id of the trainingGoalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingGoal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrainingGoal : {}", id);
        trainingGoalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
