package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PositionRisk;
import top.btmdc.hr.repository.PositionRiskRepository;
import top.btmdc.hr.service.dto.PositionRiskDTO;
import top.btmdc.hr.service.mapper.PositionRiskMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PositionRisk}.
 */
@Service
@Transactional
public class PositionRiskService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskService.class);

    private final PositionRiskRepository positionRiskRepository;

    private final PositionRiskMapper positionRiskMapper;

    public PositionRiskService(PositionRiskRepository positionRiskRepository, PositionRiskMapper positionRiskMapper) {
        this.positionRiskRepository = positionRiskRepository;
        this.positionRiskMapper = positionRiskMapper;
    }

    /**
     * Save a positionRisk.
     *
     * @param positionRiskDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionRiskDTO save(PositionRiskDTO positionRiskDTO) {
        LOG.debug("Request to save PositionRisk : {}", positionRiskDTO);
        PositionRisk positionRisk = positionRiskMapper.toEntity(positionRiskDTO);
        positionRisk = positionRiskRepository.save(positionRisk);
        return positionRiskMapper.toDto(positionRisk);
    }

    /**
     * Update a positionRisk.
     *
     * @param positionRiskDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionRiskDTO update(PositionRiskDTO positionRiskDTO) {
        LOG.debug("Request to update PositionRisk : {}", positionRiskDTO);
        PositionRisk positionRisk = positionRiskMapper.toEntity(positionRiskDTO);
        positionRisk = positionRiskRepository.save(positionRisk);
        return positionRiskMapper.toDto(positionRisk);
    }

    /**
     * Partially update a positionRisk.
     *
     * @param positionRiskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionRiskDTO> partialUpdate(PositionRiskDTO positionRiskDTO) {
        LOG.debug("Request to partially update PositionRisk : {}", positionRiskDTO);

        return positionRiskRepository
            .findById(positionRiskDTO.getId())
            .map(existingPositionRisk -> {
                positionRiskMapper.partialUpdate(existingPositionRisk, positionRiskDTO);

                return existingPositionRisk;
            })
            .map(positionRiskRepository::save)
            .map(positionRiskMapper::toDto);
    }

    /**
     * Get all the positionRisks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionRiskDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionRiskRepository.findAllWithEagerRelationships(pageable).map(positionRiskMapper::toDto);
    }

    /**
     * Get one positionRisk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionRiskDTO> findOne(Long id) {
        LOG.debug("Request to get PositionRisk : {}", id);
        return positionRiskRepository.findOneWithEagerRelationships(id).map(positionRiskMapper::toDto);
    }

    /**
     * Delete the positionRisk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PositionRisk : {}", id);
        positionRiskRepository.deleteById(id);
    }
}
