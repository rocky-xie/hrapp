package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.service.dto.SuccessionCandidateDTO;
import top.btmdc.hr.service.mapper.SuccessionCandidateMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.SuccessionCandidate}.
 */
@Service
@Transactional
public class SuccessionCandidateService {

    private static final Logger LOG = LoggerFactory.getLogger(SuccessionCandidateService.class);

    private final SuccessionCandidateRepository successionCandidateRepository;

    private final SuccessionCandidateMapper successionCandidateMapper;

    public SuccessionCandidateService(
        SuccessionCandidateRepository successionCandidateRepository,
        SuccessionCandidateMapper successionCandidateMapper
    ) {
        this.successionCandidateRepository = successionCandidateRepository;
        this.successionCandidateMapper = successionCandidateMapper;
    }

    /**
     * Save a successionCandidate.
     *
     * @param successionCandidateDTO the entity to save.
     * @return the persisted entity.
     */
    public SuccessionCandidateDTO save(SuccessionCandidateDTO successionCandidateDTO) {
        LOG.debug("Request to save SuccessionCandidate : {}", successionCandidateDTO);
        SuccessionCandidate successionCandidate = successionCandidateMapper.toEntity(successionCandidateDTO);
        successionCandidate = successionCandidateRepository.save(successionCandidate);
        return successionCandidateMapper.toDto(successionCandidate);
    }

    /**
     * Update a successionCandidate.
     *
     * @param successionCandidateDTO the entity to save.
     * @return the persisted entity.
     */
    public SuccessionCandidateDTO update(SuccessionCandidateDTO successionCandidateDTO) {
        LOG.debug("Request to update SuccessionCandidate : {}", successionCandidateDTO);
        SuccessionCandidate successionCandidate = successionCandidateMapper.toEntity(successionCandidateDTO);
        successionCandidate = successionCandidateRepository.save(successionCandidate);
        return successionCandidateMapper.toDto(successionCandidate);
    }

    /**
     * Partially update a successionCandidate.
     *
     * @param successionCandidateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuccessionCandidateDTO> partialUpdate(SuccessionCandidateDTO successionCandidateDTO) {
        LOG.debug("Request to partially update SuccessionCandidate : {}", successionCandidateDTO);

        return successionCandidateRepository
            .findById(successionCandidateDTO.getId())
            .map(existingSuccessionCandidate -> {
                successionCandidateMapper.partialUpdate(existingSuccessionCandidate, successionCandidateDTO);

                return existingSuccessionCandidate;
            })
            .map(successionCandidateRepository::save)
            .map(successionCandidateMapper::toDto);
    }

    /**
     * Get all the successionCandidates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SuccessionCandidateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return successionCandidateRepository.findAllWithEagerRelationships(pageable).map(successionCandidateMapper::toDto);
    }

    /**
     * Get one successionCandidate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuccessionCandidateDTO> findOne(Long id) {
        LOG.debug("Request to get SuccessionCandidate : {}", id);
        return successionCandidateRepository.findOneWithEagerRelationships(id).map(successionCandidateMapper::toDto);
    }

    /**
     * Delete the successionCandidate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SuccessionCandidate : {}", id);
        successionCandidateRepository.deleteById(id);
    }
}
