package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.service.dto.PositionSkillRequirementDTO;
import top.btmdc.hr.service.mapper.PositionSkillRequirementMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PositionSkillRequirement}.
 */
@Service
@Transactional
public class PositionSkillRequirementService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionSkillRequirementService.class);

    private final PositionSkillRequirementRepository positionSkillRequirementRepository;

    private final PositionSkillRequirementMapper positionSkillRequirementMapper;

    public PositionSkillRequirementService(
        PositionSkillRequirementRepository positionSkillRequirementRepository,
        PositionSkillRequirementMapper positionSkillRequirementMapper
    ) {
        this.positionSkillRequirementRepository = positionSkillRequirementRepository;
        this.positionSkillRequirementMapper = positionSkillRequirementMapper;
    }

    /**
     * Save a positionSkillRequirement.
     *
     * @param positionSkillRequirementDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionSkillRequirementDTO save(PositionSkillRequirementDTO positionSkillRequirementDTO) {
        LOG.debug("Request to save PositionSkillRequirement : {}", positionSkillRequirementDTO);
        PositionSkillRequirement positionSkillRequirement = positionSkillRequirementMapper.toEntity(positionSkillRequirementDTO);
        positionSkillRequirement = positionSkillRequirementRepository.save(positionSkillRequirement);
        return positionSkillRequirementMapper.toDto(positionSkillRequirement);
    }

    /**
     * Update a positionSkillRequirement.
     *
     * @param positionSkillRequirementDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionSkillRequirementDTO update(PositionSkillRequirementDTO positionSkillRequirementDTO) {
        LOG.debug("Request to update PositionSkillRequirement : {}", positionSkillRequirementDTO);
        PositionSkillRequirement positionSkillRequirement = positionSkillRequirementMapper.toEntity(positionSkillRequirementDTO);
        positionSkillRequirement = positionSkillRequirementRepository.save(positionSkillRequirement);
        return positionSkillRequirementMapper.toDto(positionSkillRequirement);
    }

    /**
     * Partially update a positionSkillRequirement.
     *
     * @param positionSkillRequirementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionSkillRequirementDTO> partialUpdate(PositionSkillRequirementDTO positionSkillRequirementDTO) {
        LOG.debug("Request to partially update PositionSkillRequirement : {}", positionSkillRequirementDTO);

        return positionSkillRequirementRepository
            .findById(positionSkillRequirementDTO.getId())
            .map(existingPositionSkillRequirement -> {
                positionSkillRequirementMapper.partialUpdate(existingPositionSkillRequirement, positionSkillRequirementDTO);

                return existingPositionSkillRequirement;
            })
            .map(positionSkillRequirementRepository::save)
            .map(positionSkillRequirementMapper::toDto);
    }

    /**
     * Get all the positionSkillRequirements with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionSkillRequirementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionSkillRequirementRepository.findAllWithEagerRelationships(pageable).map(positionSkillRequirementMapper::toDto);
    }

    /**
     * Get one positionSkillRequirement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionSkillRequirementDTO> findOne(Long id) {
        LOG.debug("Request to get PositionSkillRequirement : {}", id);
        return positionSkillRequirementRepository.findOneWithEagerRelationships(id).map(positionSkillRequirementMapper::toDto);
    }

    /**
     * Delete the positionSkillRequirement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PositionSkillRequirement : {}", id);
        positionSkillRequirementRepository.deleteById(id);
    }
}
