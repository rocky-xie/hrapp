package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.service.mapper.CandidateResumeMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.CandidateResume}.
 */
@Service
@Transactional
public class CandidateResumeService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateResumeService.class);

    private final CandidateResumeRepository candidateResumeRepository;

    private final CandidateResumeMapper candidateResumeMapper;

    public CandidateResumeService(CandidateResumeRepository candidateResumeRepository, CandidateResumeMapper candidateResumeMapper) {
        this.candidateResumeRepository = candidateResumeRepository;
        this.candidateResumeMapper = candidateResumeMapper;
    }

    /**
     * Save a candidateResume.
     *
     * @param candidateResumeDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidateResumeDTO save(CandidateResumeDTO candidateResumeDTO) {
        LOG.debug("Request to save CandidateResume : {}", candidateResumeDTO);
        CandidateResume candidateResume = candidateResumeMapper.toEntity(candidateResumeDTO);
        candidateResume = candidateResumeRepository.save(candidateResume);
        return candidateResumeMapper.toDto(candidateResume);
    }

    /**
     * Update a candidateResume.
     *
     * @param candidateResumeDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidateResumeDTO update(CandidateResumeDTO candidateResumeDTO) {
        LOG.debug("Request to update CandidateResume : {}", candidateResumeDTO);
        CandidateResume candidateResume = candidateResumeMapper.toEntity(candidateResumeDTO);
        candidateResume = candidateResumeRepository.save(candidateResume);
        return candidateResumeMapper.toDto(candidateResume);
    }

    /**
     * Partially update a candidateResume.
     *
     * @param candidateResumeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CandidateResumeDTO> partialUpdate(CandidateResumeDTO candidateResumeDTO) {
        LOG.debug("Request to partially update CandidateResume : {}", candidateResumeDTO);

        return candidateResumeRepository
            .findById(candidateResumeDTO.getId())
            .map(existingCandidateResume -> {
                candidateResumeMapper.partialUpdate(existingCandidateResume, candidateResumeDTO);

                return existingCandidateResume;
            })
            .map(candidateResumeRepository::save)
            .map(candidateResumeMapper::toDto);
    }

    /**
     * Get one candidateResume by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CandidateResumeDTO> findOne(Long id) {
        LOG.debug("Request to get CandidateResume : {}", id);
        return candidateResumeRepository.findById(id).map(candidateResumeMapper::toDto);
    }

    /**
     * Delete the candidateResume by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CandidateResume : {}", id);
        candidateResumeRepository.deleteById(id);
    }
}
