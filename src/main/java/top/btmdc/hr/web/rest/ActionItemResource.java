package top.btmdc.hr.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
import top.btmdc.hr.repository.ActionItemRepository;
import top.btmdc.hr.service.ActionItemQueryService;
import top.btmdc.hr.service.ActionItemService;
import top.btmdc.hr.service.criteria.ActionItemCriteria;
import top.btmdc.hr.service.dto.ActionItemDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/action-items")
public class ActionItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ActionItemResource.class);
    private static final String ENTITY_NAME = "actionItem";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final ActionItemService actionItemService;
    private final ActionItemRepository actionItemRepository;
    private final ActionItemQueryService actionItemQueryService;

    public ActionItemResource(
        ActionItemService actionItemService,
        ActionItemRepository actionItemRepository,
        ActionItemQueryService actionItemQueryService
    ) {
        this.actionItemService = actionItemService;
        this.actionItemRepository = actionItemRepository;
        this.actionItemQueryService = actionItemQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ActionItemDTO> create(@Valid @RequestBody ActionItemDTO dto) throws URISyntaxException {
        LOG.debug("REST request to save ActionItem : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new actionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionItemDTO result = actionItemService.save(dto);
        return ResponseEntity.created(new URI("/api/action-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionItemDTO> update(@PathVariable Long id, @Valid @RequestBody ActionItemDTO dto) throws URISyntaxException {
        if (dto.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!id.equals(dto.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!actionItemRepository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        ActionItemDTO result = actionItemService.update(dto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<ActionItemDTO>> getAll(
        ActionItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ActionItems by criteria: {}", criteria);
        Page<ActionItemDTO> page = actionItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/open")
    public ResponseEntity<List<ActionItemDTO>> getOpen() {
        LOG.debug("REST request to get open ActionItems");
        return ResponseEntity.ok(actionItemService.findOpenItems());
    }

    @GetMapping("/open/count")
    public ResponseEntity<Long> countOpen() {
        return ResponseEntity.ok(actionItemService.countOpen());
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ActionItemDTO> start(@PathVariable Long id) {
        LOG.debug("REST request to start ActionItem : {}", id);
        return ResponseUtil.wrapOrNotFound(actionItemService.start(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ActionItemDTO> complete(@PathVariable Long id) {
        LOG.debug("REST request to complete ActionItem : {}", id);
        return ResponseUtil.wrapOrNotFound(actionItemService.complete(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ActionItemDTO> cancel(@PathVariable Long id) {
        LOG.debug("REST request to cancel ActionItem : {}", id);
        return ResponseUtil.wrapOrNotFound(actionItemService.cancel(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionItemDTO> getOne(@PathVariable Long id) {
        Optional<ActionItemDTO> dto = actionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        LOG.debug("REST request to delete ActionItem : {}", id);
        actionItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
