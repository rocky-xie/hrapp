package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ImprovementPlan;
import top.btmdc.hr.repository.ImprovementPlanRepository;
import top.btmdc.hr.service.dto.ImprovementPlanDTO;
import top.btmdc.hr.service.mapper.ImprovementPlanMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.ImprovementPlan}.
 */
@Service
@Transactional
public class ImprovementPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(ImprovementPlanService.class);

    private final ImprovementPlanRepository improvementPlanRepository;

    private final ImprovementPlanMapper improvementPlanMapper;

    public ImprovementPlanService(ImprovementPlanRepository improvementPlanRepository, ImprovementPlanMapper improvementPlanMapper) {
        this.improvementPlanRepository = improvementPlanRepository;
        this.improvementPlanMapper = improvementPlanMapper;
    }

    /**
     * Save a improvementPlan.
     *
     * @param improvementPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public ImprovementPlanDTO save(ImprovementPlanDTO improvementPlanDTO) {
        LOG.debug("Request to save ImprovementPlan : {}", improvementPlanDTO);
        ImprovementPlan improvementPlan = improvementPlanMapper.toEntity(improvementPlanDTO);
        improvementPlan = improvementPlanRepository.save(improvementPlan);
        return improvementPlanMapper.toDto(improvementPlan);
    }

    /**
     * Update a improvementPlan.
     *
     * @param improvementPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public ImprovementPlanDTO update(ImprovementPlanDTO improvementPlanDTO) {
        LOG.debug("Request to update ImprovementPlan : {}", improvementPlanDTO);
        ImprovementPlan improvementPlan = improvementPlanMapper.toEntity(improvementPlanDTO);
        improvementPlan = improvementPlanRepository.save(improvementPlan);
        return improvementPlanMapper.toDto(improvementPlan);
    }

    /**
     * Partially update a improvementPlan.
     *
     * @param improvementPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImprovementPlanDTO> partialUpdate(ImprovementPlanDTO improvementPlanDTO) {
        LOG.debug("Request to partially update ImprovementPlan : {}", improvementPlanDTO);

        return improvementPlanRepository
            .findById(improvementPlanDTO.getId())
            .map(existingImprovementPlan -> {
                improvementPlanMapper.partialUpdate(existingImprovementPlan, improvementPlanDTO);

                return existingImprovementPlan;
            })
            .map(improvementPlanRepository::save)
            .map(improvementPlanMapper::toDto);
    }

    /**
     * Get all the improvementPlans with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ImprovementPlanDTO> findAllWithEagerRelationships(Pageable pageable) {
        return improvementPlanRepository.findAllWithEagerRelationships(pageable).map(improvementPlanMapper::toDto);
    }

    /**
     * Get one improvementPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImprovementPlanDTO> findOne(Long id) {
        LOG.debug("Request to get ImprovementPlan : {}", id);
        return improvementPlanRepository.findOneWithEagerRelationships(id).map(improvementPlanMapper::toDto);
    }

    /**
     * Delete the improvementPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImprovementPlan : {}", id);
        improvementPlanRepository.deleteById(id);
    }
}
