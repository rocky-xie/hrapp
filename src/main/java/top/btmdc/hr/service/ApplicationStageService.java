package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ApplicationStage;
import top.btmdc.hr.repository.ApplicationStageRepository;
import top.btmdc.hr.service.dto.ApplicationStageDTO;
import top.btmdc.hr.service.mapper.ApplicationStageMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.ApplicationStage}.
 */
@Service
@Transactional
public class ApplicationStageService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStageService.class);

    private final ApplicationStageRepository applicationStageRepository;

    private final ApplicationStageMapper applicationStageMapper;

    public ApplicationStageService(ApplicationStageRepository applicationStageRepository, ApplicationStageMapper applicationStageMapper) {
        this.applicationStageRepository = applicationStageRepository;
        this.applicationStageMapper = applicationStageMapper;
    }

    /**
     * Save a applicationStage.
     *
     * @param applicationStageDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationStageDTO save(ApplicationStageDTO applicationStageDTO) {
        LOG.debug("Request to save ApplicationStage : {}", applicationStageDTO);
        ApplicationStage applicationStage = applicationStageMapper.toEntity(applicationStageDTO);
        applicationStage = applicationStageRepository.save(applicationStage);
        return applicationStageMapper.toDto(applicationStage);
    }

    /**
     * Update a applicationStage.
     *
     * @param applicationStageDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationStageDTO update(ApplicationStageDTO applicationStageDTO) {
        LOG.debug("Request to update ApplicationStage : {}", applicationStageDTO);
        ApplicationStage applicationStage = applicationStageMapper.toEntity(applicationStageDTO);
        applicationStage = applicationStageRepository.save(applicationStage);
        return applicationStageMapper.toDto(applicationStage);
    }

    /**
     * Partially update a applicationStage.
     *
     * @param applicationStageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationStageDTO> partialUpdate(ApplicationStageDTO applicationStageDTO) {
        LOG.debug("Request to partially update ApplicationStage : {}", applicationStageDTO);

        return applicationStageRepository
            .findById(applicationStageDTO.getId())
            .map(existingApplicationStage -> {
                applicationStageMapper.partialUpdate(existingApplicationStage, applicationStageDTO);

                return existingApplicationStage;
            })
            .map(applicationStageRepository::save)
            .map(applicationStageMapper::toDto);
    }

    /**
     * Get one applicationStage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationStageDTO> findOne(Long id) {
        LOG.debug("Request to get ApplicationStage : {}", id);
        return applicationStageRepository.findById(id).map(applicationStageMapper::toDto);
    }

    /**
     * Delete the applicationStage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ApplicationStage : {}", id);
        applicationStageRepository.deleteById(id);
    }
}
