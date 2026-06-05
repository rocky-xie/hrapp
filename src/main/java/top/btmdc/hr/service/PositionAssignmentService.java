package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.service.dto.PositionAssignmentDTO;
import top.btmdc.hr.service.mapper.PositionAssignmentMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PositionAssignment}.
 */
@Service
@Transactional
public class PositionAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionAssignmentService.class);

    private final PositionAssignmentRepository positionAssignmentRepository;

    private final PositionAssignmentMapper positionAssignmentMapper;

    public PositionAssignmentService(
        PositionAssignmentRepository positionAssignmentRepository,
        PositionAssignmentMapper positionAssignmentMapper
    ) {
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.positionAssignmentMapper = positionAssignmentMapper;
    }

    /**
     * Save a positionAssignment.
     *
     * @param positionAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionAssignmentDTO save(PositionAssignmentDTO positionAssignmentDTO) {
        LOG.debug("Request to save PositionAssignment : {}", positionAssignmentDTO);
        PositionAssignment positionAssignment = positionAssignmentMapper.toEntity(positionAssignmentDTO);
        positionAssignment = positionAssignmentRepository.save(positionAssignment);
        return positionAssignmentMapper.toDto(positionAssignment);
    }

    /**
     * Update a positionAssignment.
     *
     * @param positionAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionAssignmentDTO update(PositionAssignmentDTO positionAssignmentDTO) {
        LOG.debug("Request to update PositionAssignment : {}", positionAssignmentDTO);
        PositionAssignment positionAssignment = positionAssignmentMapper.toEntity(positionAssignmentDTO);
        positionAssignment = positionAssignmentRepository.save(positionAssignment);
        return positionAssignmentMapper.toDto(positionAssignment);
    }

    /**
     * Partially update a positionAssignment.
     *
     * @param positionAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionAssignmentDTO> partialUpdate(PositionAssignmentDTO positionAssignmentDTO) {
        LOG.debug("Request to partially update PositionAssignment : {}", positionAssignmentDTO);

        return positionAssignmentRepository
            .findById(positionAssignmentDTO.getId())
            .map(existingPositionAssignment -> {
                positionAssignmentMapper.partialUpdate(existingPositionAssignment, positionAssignmentDTO);

                return existingPositionAssignment;
            })
            .map(positionAssignmentRepository::save)
            .map(positionAssignmentMapper::toDto);
    }

    /**
     * Get all the positionAssignments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionAssignmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionAssignmentRepository.findAllWithEagerRelationships(pageable).map(positionAssignmentMapper::toDto);
    }

    /**
     * Get one positionAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get PositionAssignment : {}", id);
        return positionAssignmentRepository.findOneWithEagerRelationships(id).map(positionAssignmentMapper::toDto);
    }

    /**
     * Delete the positionAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PositionAssignment : {}", id);
        positionAssignmentRepository.deleteById(id);
    }
}
