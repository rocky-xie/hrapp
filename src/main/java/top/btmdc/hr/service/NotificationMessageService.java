package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.mapper.NotificationMessageMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.NotificationMessage}.
 */
@Service
@Transactional
public class NotificationMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationMessageService.class);

    private final NotificationMessageRepository notificationMessageRepository;

    private final NotificationMessageMapper notificationMessageMapper;

    public NotificationMessageService(
        NotificationMessageRepository notificationMessageRepository,
        NotificationMessageMapper notificationMessageMapper
    ) {
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageMapper = notificationMessageMapper;
    }

    /**
     * Save a notificationMessage.
     *
     * @param notificationMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationMessageDTO save(NotificationMessageDTO notificationMessageDTO) {
        LOG.debug("Request to save NotificationMessage : {}", notificationMessageDTO);
        NotificationMessage notificationMessage = notificationMessageMapper.toEntity(notificationMessageDTO);
        notificationMessage = notificationMessageRepository.save(notificationMessage);
        return notificationMessageMapper.toDto(notificationMessage);
    }

    /**
     * Update a notificationMessage.
     *
     * @param notificationMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationMessageDTO update(NotificationMessageDTO notificationMessageDTO) {
        LOG.debug("Request to update NotificationMessage : {}", notificationMessageDTO);
        NotificationMessage notificationMessage = notificationMessageMapper.toEntity(notificationMessageDTO);
        notificationMessage = notificationMessageRepository.save(notificationMessage);
        return notificationMessageMapper.toDto(notificationMessage);
    }

    /**
     * Partially update a notificationMessage.
     *
     * @param notificationMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationMessageDTO> partialUpdate(NotificationMessageDTO notificationMessageDTO) {
        LOG.debug("Request to partially update NotificationMessage : {}", notificationMessageDTO);

        return notificationMessageRepository
            .findById(notificationMessageDTO.getId())
            .map(existingNotificationMessage -> {
                notificationMessageMapper.partialUpdate(existingNotificationMessage, notificationMessageDTO);

                return existingNotificationMessage;
            })
            .map(notificationMessageRepository::save)
            .map(notificationMessageMapper::toDto);
    }

    /**
     * Get one notificationMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationMessageDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationMessage : {}", id);
        return notificationMessageRepository.findById(id).map(notificationMessageMapper::toDto);
    }

    /**
     * Delete the notificationMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationMessage : {}", id);
        notificationMessageRepository.deleteById(id);
    }
}
