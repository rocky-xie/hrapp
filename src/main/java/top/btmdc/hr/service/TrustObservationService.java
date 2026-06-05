package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.TrustObservation;
import top.btmdc.hr.repository.TrustObservationRepository;
import top.btmdc.hr.service.dto.TrustObservationDTO;
import top.btmdc.hr.service.mapper.TrustObservationMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.TrustObservation}.
 */
@Service
@Transactional
public class TrustObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrustObservationService.class);

    private final TrustObservationRepository trustObservationRepository;

    private final TrustObservationMapper trustObservationMapper;

    public TrustObservationService(TrustObservationRepository trustObservationRepository, TrustObservationMapper trustObservationMapper) {
        this.trustObservationRepository = trustObservationRepository;
        this.trustObservationMapper = trustObservationMapper;
    }

    /**
     * Save a trustObservation.
     *
     * @param trustObservationDTO the entity to save.
     * @return the persisted entity.
     */
    public TrustObservationDTO save(TrustObservationDTO trustObservationDTO) {
        LOG.debug("Request to save TrustObservation : {}", trustObservationDTO);
        TrustObservation trustObservation = trustObservationMapper.toEntity(trustObservationDTO);
        trustObservation = trustObservationRepository.save(trustObservation);
        return trustObservationMapper.toDto(trustObservation);
    }

    /**
     * Update a trustObservation.
     *
     * @param trustObservationDTO the entity to save.
     * @return the persisted entity.
     */
    public TrustObservationDTO update(TrustObservationDTO trustObservationDTO) {
        LOG.debug("Request to update TrustObservation : {}", trustObservationDTO);
        TrustObservation trustObservation = trustObservationMapper.toEntity(trustObservationDTO);
        trustObservation = trustObservationRepository.save(trustObservation);
        return trustObservationMapper.toDto(trustObservation);
    }

    /**
     * Partially update a trustObservation.
     *
     * @param trustObservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrustObservationDTO> partialUpdate(TrustObservationDTO trustObservationDTO) {
        LOG.debug("Request to partially update TrustObservation : {}", trustObservationDTO);

        return trustObservationRepository
            .findById(trustObservationDTO.getId())
            .map(existingTrustObservation -> {
                trustObservationMapper.partialUpdate(existingTrustObservation, trustObservationDTO);

                return existingTrustObservation;
            })
            .map(trustObservationRepository::save)
            .map(trustObservationMapper::toDto);
    }

    /**
     * Get all the trustObservations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrustObservationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trustObservationRepository.findAllWithEagerRelationships(pageable).map(trustObservationMapper::toDto);
    }

    /**
     * Get one trustObservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrustObservationDTO> findOne(Long id) {
        LOG.debug("Request to get TrustObservation : {}", id);
        return trustObservationRepository.findOneWithEagerRelationships(id).map(trustObservationMapper::toDto);
    }

    /**
     * Delete the trustObservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrustObservation : {}", id);
        trustObservationRepository.deleteById(id);
    }
}
