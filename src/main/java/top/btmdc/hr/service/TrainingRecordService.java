package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.TrainingRecord;
import top.btmdc.hr.repository.TrainingRecordRepository;
import top.btmdc.hr.service.dto.TrainingRecordDTO;
import top.btmdc.hr.service.mapper.TrainingRecordMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.TrainingRecord}.
 */
@Service
@Transactional
public class TrainingRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRecordService.class);

    private final TrainingRecordRepository trainingRecordRepository;

    private final TrainingRecordMapper trainingRecordMapper;

    public TrainingRecordService(TrainingRecordRepository trainingRecordRepository, TrainingRecordMapper trainingRecordMapper) {
        this.trainingRecordRepository = trainingRecordRepository;
        this.trainingRecordMapper = trainingRecordMapper;
    }

    /**
     * Save a trainingRecord.
     *
     * @param trainingRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingRecordDTO save(TrainingRecordDTO trainingRecordDTO) {
        LOG.debug("Request to save TrainingRecord : {}", trainingRecordDTO);
        TrainingRecord trainingRecord = trainingRecordMapper.toEntity(trainingRecordDTO);
        trainingRecord = trainingRecordRepository.save(trainingRecord);
        return trainingRecordMapper.toDto(trainingRecord);
    }

    /**
     * Update a trainingRecord.
     *
     * @param trainingRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingRecordDTO update(TrainingRecordDTO trainingRecordDTO) {
        LOG.debug("Request to update TrainingRecord : {}", trainingRecordDTO);
        TrainingRecord trainingRecord = trainingRecordMapper.toEntity(trainingRecordDTO);
        trainingRecord = trainingRecordRepository.save(trainingRecord);
        return trainingRecordMapper.toDto(trainingRecord);
    }

    /**
     * Partially update a trainingRecord.
     *
     * @param trainingRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrainingRecordDTO> partialUpdate(TrainingRecordDTO trainingRecordDTO) {
        LOG.debug("Request to partially update TrainingRecord : {}", trainingRecordDTO);

        return trainingRecordRepository
            .findById(trainingRecordDTO.getId())
            .map(existingTrainingRecord -> {
                trainingRecordMapper.partialUpdate(existingTrainingRecord, trainingRecordDTO);

                return existingTrainingRecord;
            })
            .map(trainingRecordRepository::save)
            .map(trainingRecordMapper::toDto);
    }

    /**
     * Get all the trainingRecords with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrainingRecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trainingRecordRepository.findAllWithEagerRelationships(pageable).map(trainingRecordMapper::toDto);
    }

    /**
     * Get one trainingRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrainingRecordDTO> findOne(Long id) {
        LOG.debug("Request to get TrainingRecord : {}", id);
        return trainingRecordRepository.findOneWithEagerRelationships(id).map(trainingRecordMapper::toDto);
    }

    /**
     * Delete the trainingRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrainingRecord : {}", id);
        trainingRecordRepository.deleteById(id);
    }
}
