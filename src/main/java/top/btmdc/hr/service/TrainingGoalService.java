package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.mapper.TrainingGoalMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.TrainingGoal}.
 */
@Service
@Transactional
public class TrainingGoalService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingGoalService.class);

    private final TrainingGoalRepository trainingGoalRepository;

    private final TrainingGoalMapper trainingGoalMapper;

    public TrainingGoalService(TrainingGoalRepository trainingGoalRepository, TrainingGoalMapper trainingGoalMapper) {
        this.trainingGoalRepository = trainingGoalRepository;
        this.trainingGoalMapper = trainingGoalMapper;
    }

    /**
     * Save a trainingGoal.
     *
     * @param trainingGoalDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingGoalDTO save(TrainingGoalDTO trainingGoalDTO) {
        LOG.debug("Request to save TrainingGoal : {}", trainingGoalDTO);
        TrainingGoal trainingGoal = trainingGoalMapper.toEntity(trainingGoalDTO);
        trainingGoal = trainingGoalRepository.save(trainingGoal);
        return trainingGoalMapper.toDto(trainingGoal);
    }

    /**
     * Update a trainingGoal.
     *
     * @param trainingGoalDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingGoalDTO update(TrainingGoalDTO trainingGoalDTO) {
        LOG.debug("Request to update TrainingGoal : {}", trainingGoalDTO);
        TrainingGoal trainingGoal = trainingGoalMapper.toEntity(trainingGoalDTO);
        trainingGoal = trainingGoalRepository.save(trainingGoal);
        return trainingGoalMapper.toDto(trainingGoal);
    }

    /**
     * Partially update a trainingGoal.
     *
     * @param trainingGoalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrainingGoalDTO> partialUpdate(TrainingGoalDTO trainingGoalDTO) {
        LOG.debug("Request to partially update TrainingGoal : {}", trainingGoalDTO);

        return trainingGoalRepository
            .findById(trainingGoalDTO.getId())
            .map(existingTrainingGoal -> {
                trainingGoalMapper.partialUpdate(existingTrainingGoal, trainingGoalDTO);

                return existingTrainingGoal;
            })
            .map(trainingGoalRepository::save)
            .map(trainingGoalMapper::toDto);
    }

    /**
     * Get all the trainingGoals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrainingGoalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trainingGoalRepository.findAllWithEagerRelationships(pageable).map(trainingGoalMapper::toDto);
    }

    /**
     * Get one trainingGoal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrainingGoalDTO> findOne(Long id) {
        LOG.debug("Request to get TrainingGoal : {}", id);
        return trainingGoalRepository.findOneWithEagerRelationships(id).map(trainingGoalMapper::toDto);
    }

    /**
     * Delete the trainingGoal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrainingGoal : {}", id);
        trainingGoalRepository.deleteById(id);
    }
}
