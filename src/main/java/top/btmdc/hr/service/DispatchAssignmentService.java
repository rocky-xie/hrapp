package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.repository.DispatchAssignmentRepository;
import top.btmdc.hr.service.dto.DispatchAssignmentDTO;
import top.btmdc.hr.service.mapper.DispatchAssignmentMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.DispatchAssignment}.
 */
@Service
@Transactional
public class DispatchAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchAssignmentService.class);

    private final DispatchAssignmentRepository dispatchAssignmentRepository;

    private final DispatchAssignmentMapper dispatchAssignmentMapper;

    public DispatchAssignmentService(
        DispatchAssignmentRepository dispatchAssignmentRepository,
        DispatchAssignmentMapper dispatchAssignmentMapper
    ) {
        this.dispatchAssignmentRepository = dispatchAssignmentRepository;
        this.dispatchAssignmentMapper = dispatchAssignmentMapper;
    }

    /**
     * Save a dispatchAssignment.
     *
     * @param dispatchAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public DispatchAssignmentDTO save(DispatchAssignmentDTO dispatchAssignmentDTO) {
        LOG.debug("Request to save DispatchAssignment : {}", dispatchAssignmentDTO);
        DispatchAssignment dispatchAssignment = dispatchAssignmentMapper.toEntity(dispatchAssignmentDTO);
        dispatchAssignment = dispatchAssignmentRepository.save(dispatchAssignment);
        return dispatchAssignmentMapper.toDto(dispatchAssignment);
    }

    /**
     * Update a dispatchAssignment.
     *
     * @param dispatchAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public DispatchAssignmentDTO update(DispatchAssignmentDTO dispatchAssignmentDTO) {
        LOG.debug("Request to update DispatchAssignment : {}", dispatchAssignmentDTO);
        DispatchAssignment dispatchAssignment = dispatchAssignmentMapper.toEntity(dispatchAssignmentDTO);
        dispatchAssignment = dispatchAssignmentRepository.save(dispatchAssignment);
        return dispatchAssignmentMapper.toDto(dispatchAssignment);
    }

    /**
     * Partially update a dispatchAssignment.
     *
     * @param dispatchAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DispatchAssignmentDTO> partialUpdate(DispatchAssignmentDTO dispatchAssignmentDTO) {
        LOG.debug("Request to partially update DispatchAssignment : {}", dispatchAssignmentDTO);

        return dispatchAssignmentRepository
            .findById(dispatchAssignmentDTO.getId())
            .map(existingDispatchAssignment -> {
                dispatchAssignmentMapper.partialUpdate(existingDispatchAssignment, dispatchAssignmentDTO);

                return existingDispatchAssignment;
            })
            .map(dispatchAssignmentRepository::save)
            .map(dispatchAssignmentMapper::toDto);
    }

    /**
     * Get one dispatchAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DispatchAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get DispatchAssignment : {}", id);
        return dispatchAssignmentRepository.findById(id).map(dispatchAssignmentMapper::toDto);
    }

    /**
     * Delete the dispatchAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DispatchAssignment : {}", id);
        dispatchAssignmentRepository.deleteById(id);
    }
}
