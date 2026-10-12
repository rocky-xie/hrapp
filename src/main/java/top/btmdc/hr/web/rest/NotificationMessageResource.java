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
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.service.NotificationMessageQueryService;
import top.btmdc.hr.service.NotificationMessageService;
import top.btmdc.hr.service.criteria.NotificationMessageCriteria;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link top.btmdc.hr.domain.NotificationMessage}.
 */
@RestController
@PreAuthorize("hasAnyAuthority(\"ROLE_HR_RECRUITER\", \"ROLE_ADMIN\")")
@RequestMapping("/api/notification-messages")
public class NotificationMessageResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationMessageResource.class);

    private static final String ENTITY_NAME = "notificationMessage";

    @Value("${jhipster.clientApp.name:hrapp}")
    private String applicationName;

    private final NotificationMessageService notificationMessageService;

    private final NotificationMessageRepository notificationMessageRepository;

    private final NotificationMessageQueryService notificationMessageQueryService;

    public NotificationMessageResource(
        NotificationMessageService notificationMessageService,
        NotificationMessageRepository notificationMessageRepository,
        NotificationMessageQueryService notificationMessageQueryService
    ) {
        this.notificationMessageService = notificationMessageService;
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageQueryService = notificationMessageQueryService;
    }

    /**
     * {@code POST  /notification-messages} : Create a new notificationMessage.
     *
     * @param notificationMessageDTO the notificationMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationMessageDTO, or with status {@code 400 (Bad Request)} if the notificationMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationMessageDTO> createNotificationMessage(
        @Valid @RequestBody NotificationMessageDTO notificationMessageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save NotificationMessage : {}", notificationMessageDTO);
        if (notificationMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationMessageDTO = notificationMessageService.save(notificationMessageDTO);
        return ResponseEntity.created(new URI("/api/notification-messages/" + notificationMessageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, notificationMessageDTO.getId().toString()))
            .body(notificationMessageDTO);
    }

    /**
     * {@code PUT  /notification-messages/:id} : Updates an existing notificationMessage.
     *
     * @param id the id of the notificationMessageDTO to save.
     * @param notificationMessageDTO the notificationMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationMessageDTO,
     * or with status {@code 400 (Bad Request)} if the notificationMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationMessageDTO> updateNotificationMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationMessageDTO notificationMessageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationMessage : {}, {}", id, notificationMessageDTO);
        if (notificationMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationMessageDTO = notificationMessageService.update(notificationMessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificationMessageDTO.getId().toString()))
            .body(notificationMessageDTO);
    }

    /**
     * {@code PATCH  /notification-messages/:id} : Partial updates given fields of an existing notificationMessage, field will ignore if it is null
     *
     * @param id the id of the notificationMessageDTO to save.
     * @param notificationMessageDTO the notificationMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationMessageDTO,
     * or with status {@code 400 (Bad Request)} if the notificationMessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationMessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationMessageDTO> partialUpdateNotificationMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationMessageDTO notificationMessageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NotificationMessage partially : {}, {}", id, notificationMessageDTO);
        if (notificationMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationMessageDTO> result = notificationMessageService.partialUpdate(notificationMessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificationMessageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-messages} : get all the Notification Messages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Notification Messages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificationMessageDTO>> getAllNotificationMessages(
        NotificationMessageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NotificationMessages by criteria: {}", criteria);

        Page<NotificationMessageDTO> page = notificationMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-messages/count} : count all the notificationMessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotificationMessages(NotificationMessageCriteria criteria) {
        LOG.debug("REST request to count NotificationMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-messages/:id} : get the "id" notificationMessage.
     *
     * @param id the id of the notificationMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationMessageDTO> getNotificationMessage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NotificationMessage : {}", id);
        Optional<NotificationMessageDTO> notificationMessageDTO = notificationMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationMessageDTO);
    }

    /**
     * {@code DELETE  /notification-messages/:id} : delete the "id" notificationMessage.
     *
     * @param id the id of the notificationMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationMessage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NotificationMessage : {}", id);
        notificationMessageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
