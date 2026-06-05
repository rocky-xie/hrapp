package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.CandidateProfile;
import top.btmdc.hr.repository.CandidateProfileRepository;
import top.btmdc.hr.service.dto.CandidateProfileDTO;
import top.btmdc.hr.service.mapper.CandidateProfileMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.CandidateProfile}.
 */
@Service
@Transactional
public class CandidateProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateProfileService.class);

    private final CandidateProfileRepository candidateProfileRepository;

    private final CandidateProfileMapper candidateProfileMapper;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository, CandidateProfileMapper candidateProfileMapper) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.candidateProfileMapper = candidateProfileMapper;
    }

    /**
     * Save a candidateProfile.
     *
     * @param candidateProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidateProfileDTO save(CandidateProfileDTO candidateProfileDTO) {
        LOG.debug("Request to save CandidateProfile : {}", candidateProfileDTO);
        CandidateProfile candidateProfile = candidateProfileMapper.toEntity(candidateProfileDTO);
        candidateProfile = candidateProfileRepository.save(candidateProfile);
        return candidateProfileMapper.toDto(candidateProfile);
    }

    /**
     * Update a candidateProfile.
     *
     * @param candidateProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidateProfileDTO update(CandidateProfileDTO candidateProfileDTO) {
        LOG.debug("Request to update CandidateProfile : {}", candidateProfileDTO);
        CandidateProfile candidateProfile = candidateProfileMapper.toEntity(candidateProfileDTO);
        candidateProfile = candidateProfileRepository.save(candidateProfile);
        return candidateProfileMapper.toDto(candidateProfile);
    }

    /**
     * Partially update a candidateProfile.
     *
     * @param candidateProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CandidateProfileDTO> partialUpdate(CandidateProfileDTO candidateProfileDTO) {
        LOG.debug("Request to partially update CandidateProfile : {}", candidateProfileDTO);

        return candidateProfileRepository
            .findById(candidateProfileDTO.getId())
            .map(existingCandidateProfile -> {
                candidateProfileMapper.partialUpdate(existingCandidateProfile, candidateProfileDTO);

                return existingCandidateProfile;
            })
            .map(candidateProfileRepository::save)
            .map(candidateProfileMapper::toDto);
    }

    /**
     * Get all the candidateProfiles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CandidateProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return candidateProfileRepository.findAllWithEagerRelationships(pageable).map(candidateProfileMapper::toDto);
    }

    /**
     * Get one candidateProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CandidateProfileDTO> findOne(Long id) {
        LOG.debug("Request to get CandidateProfile : {}", id);
        return candidateProfileRepository.findOneWithEagerRelationships(id).map(candidateProfileMapper::toDto);
    }

    /**
     * Delete the candidateProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CandidateProfile : {}", id);
        candidateProfileRepository.deleteById(id);
    }
}
