package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PreboardingFollowUp;
import top.btmdc.hr.repository.PreboardingFollowUpRepository;
import top.btmdc.hr.service.dto.PreboardingFollowUpDTO;
import top.btmdc.hr.service.mapper.PreboardingFollowUpMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PreboardingFollowUp}.
 */
@Service
@Transactional
public class PreboardingFollowUpService {

    private static final Logger LOG = LoggerFactory.getLogger(PreboardingFollowUpService.class);

    private final PreboardingFollowUpRepository preboardingFollowUpRepository;

    private final PreboardingFollowUpMapper preboardingFollowUpMapper;

    public PreboardingFollowUpService(
        PreboardingFollowUpRepository preboardingFollowUpRepository,
        PreboardingFollowUpMapper preboardingFollowUpMapper
    ) {
        this.preboardingFollowUpRepository = preboardingFollowUpRepository;
        this.preboardingFollowUpMapper = preboardingFollowUpMapper;
    }

    /**
     * Save a preboardingFollowUp.
     *
     * @param preboardingFollowUpDTO the entity to save.
     * @return the persisted entity.
     */
    public PreboardingFollowUpDTO save(PreboardingFollowUpDTO preboardingFollowUpDTO) {
        LOG.debug("Request to save PreboardingFollowUp : {}", preboardingFollowUpDTO);
        PreboardingFollowUp preboardingFollowUp = preboardingFollowUpMapper.toEntity(preboardingFollowUpDTO);
        preboardingFollowUp = preboardingFollowUpRepository.save(preboardingFollowUp);
        return preboardingFollowUpMapper.toDto(preboardingFollowUp);
    }

    /**
     * Update a preboardingFollowUp.
     *
     * @param preboardingFollowUpDTO the entity to save.
     * @return the persisted entity.
     */
    public PreboardingFollowUpDTO update(PreboardingFollowUpDTO preboardingFollowUpDTO) {
        LOG.debug("Request to update PreboardingFollowUp : {}", preboardingFollowUpDTO);
        PreboardingFollowUp preboardingFollowUp = preboardingFollowUpMapper.toEntity(preboardingFollowUpDTO);
        preboardingFollowUp = preboardingFollowUpRepository.save(preboardingFollowUp);
        return preboardingFollowUpMapper.toDto(preboardingFollowUp);
    }

    /**
     * Partially update a preboardingFollowUp.
     *
     * @param preboardingFollowUpDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PreboardingFollowUpDTO> partialUpdate(PreboardingFollowUpDTO preboardingFollowUpDTO) {
        LOG.debug("Request to partially update PreboardingFollowUp : {}", preboardingFollowUpDTO);

        return preboardingFollowUpRepository
            .findById(preboardingFollowUpDTO.getId())
            .map(existingPreboardingFollowUp -> {
                preboardingFollowUpMapper.partialUpdate(existingPreboardingFollowUp, preboardingFollowUpDTO);

                return existingPreboardingFollowUp;
            })
            .map(preboardingFollowUpRepository::save)
            .map(preboardingFollowUpMapper::toDto);
    }

    /**
     * Get one preboardingFollowUp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PreboardingFollowUpDTO> findOne(Long id) {
        LOG.debug("Request to get PreboardingFollowUp : {}", id);
        return preboardingFollowUpRepository.findById(id).map(preboardingFollowUpMapper::toDto);
    }

    /**
     * Delete the preboardingFollowUp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PreboardingFollowUp : {}", id);
        preboardingFollowUpRepository.deleteById(id);
    }
}
