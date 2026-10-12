package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.service.dto.ApplicationEventDTO;
import top.btmdc.hr.service.mapper.ApplicationEventMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.ApplicationEvent}.
 */
@Service
@Transactional
public class ApplicationEventService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventService.class);

    private final ApplicationEventRepository applicationEventRepository;

    private final ApplicationEventMapper applicationEventMapper;

    public ApplicationEventService(ApplicationEventRepository applicationEventRepository, ApplicationEventMapper applicationEventMapper) {
        this.applicationEventRepository = applicationEventRepository;
        this.applicationEventMapper = applicationEventMapper;
    }

    /**
     * Save a applicationEvent.
     *
     * @param applicationEventDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationEventDTO save(ApplicationEventDTO applicationEventDTO) {
        LOG.debug("Request to save ApplicationEvent : {}", applicationEventDTO);
        ApplicationEvent applicationEvent = applicationEventMapper.toEntity(applicationEventDTO);
        applicationEvent = applicationEventRepository.save(applicationEvent);
        return applicationEventMapper.toDto(applicationEvent);
    }

    /**
     * Update a applicationEvent.
     *
     * @param applicationEventDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationEventDTO update(ApplicationEventDTO applicationEventDTO) {
        LOG.debug("Request to update ApplicationEvent : {}", applicationEventDTO);
        ApplicationEvent applicationEvent = applicationEventMapper.toEntity(applicationEventDTO);
        applicationEvent = applicationEventRepository.save(applicationEvent);
        return applicationEventMapper.toDto(applicationEvent);
    }

    /**
     * Partially update a applicationEvent.
     *
     * @param applicationEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationEventDTO> partialUpdate(ApplicationEventDTO applicationEventDTO) {
        LOG.debug("Request to partially update ApplicationEvent : {}", applicationEventDTO);

        return applicationEventRepository
            .findById(applicationEventDTO.getId())
            .map(existingApplicationEvent -> {
                applicationEventMapper.partialUpdate(existingApplicationEvent, applicationEventDTO);

                return existingApplicationEvent;
            })
            .map(applicationEventRepository::save)
            .map(applicationEventMapper::toDto);
    }

    /**
     * Get one applicationEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationEventDTO> findOne(Long id) {
        LOG.debug("Request to get ApplicationEvent : {}", id);
        return applicationEventRepository.findById(id).map(applicationEventMapper::toDto);
    }

    /**
     * Delete the applicationEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ApplicationEvent : {}", id);
        applicationEventRepository.deleteById(id);
    }
}
