package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.ExternalCandidateRepository;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;
import top.btmdc.hr.service.mapper.ExternalCandidateMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.ExternalCandidate}.
 */
@Service
@Transactional
public class ExternalCandidateService {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalCandidateService.class);

    private final ExternalCandidateRepository externalCandidateRepository;

    private final ExternalCandidateMapper externalCandidateMapper;

    public ExternalCandidateService(
        ExternalCandidateRepository externalCandidateRepository,
        ExternalCandidateMapper externalCandidateMapper
    ) {
        this.externalCandidateRepository = externalCandidateRepository;
        this.externalCandidateMapper = externalCandidateMapper;
    }

    /**
     * Save a externalCandidate.
     *
     * @param externalCandidateDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalCandidateDTO save(ExternalCandidateDTO externalCandidateDTO) {
        LOG.debug("Request to save ExternalCandidate : {}", externalCandidateDTO);
        ExternalCandidate externalCandidate = externalCandidateMapper.toEntity(externalCandidateDTO);
        externalCandidate = externalCandidateRepository.save(externalCandidate);
        return externalCandidateMapper.toDto(externalCandidate);
    }

    /**
     * Update a externalCandidate.
     *
     * @param externalCandidateDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalCandidateDTO update(ExternalCandidateDTO externalCandidateDTO) {
        LOG.debug("Request to update ExternalCandidate : {}", externalCandidateDTO);
        ExternalCandidate externalCandidate = externalCandidateMapper.toEntity(externalCandidateDTO);
        externalCandidate = externalCandidateRepository.save(externalCandidate);
        return externalCandidateMapper.toDto(externalCandidate);
    }

    /**
     * Partially update a externalCandidate.
     *
     * @param externalCandidateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExternalCandidateDTO> partialUpdate(ExternalCandidateDTO externalCandidateDTO) {
        LOG.debug("Request to partially update ExternalCandidate : {}", externalCandidateDTO);

        return externalCandidateRepository
            .findById(externalCandidateDTO.getId())
            .map(existingExternalCandidate -> {
                externalCandidateMapper.partialUpdate(existingExternalCandidate, externalCandidateDTO);

                return existingExternalCandidate;
            })
            .map(externalCandidateRepository::save)
            .map(externalCandidateMapper::toDto);
    }

    /**
     * Get one externalCandidate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExternalCandidateDTO> findOne(Long id) {
        LOG.debug("Request to get ExternalCandidate : {}", id);
        return externalCandidateRepository.findById(id).map(externalCandidateMapper::toDto);
    }

    /**
     * Delete the externalCandidate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExternalCandidate : {}", id);
        externalCandidateRepository.deleteById(id);
    }
}
