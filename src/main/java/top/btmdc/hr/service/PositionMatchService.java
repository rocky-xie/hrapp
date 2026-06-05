package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PositionMatch;
import top.btmdc.hr.repository.PositionMatchRepository;
import top.btmdc.hr.service.dto.PositionMatchDTO;
import top.btmdc.hr.service.mapper.PositionMatchMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PositionMatch}.
 */
@Service
@Transactional
public class PositionMatchService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionMatchService.class);

    private final PositionMatchRepository positionMatchRepository;

    private final PositionMatchMapper positionMatchMapper;

    public PositionMatchService(PositionMatchRepository positionMatchRepository, PositionMatchMapper positionMatchMapper) {
        this.positionMatchRepository = positionMatchRepository;
        this.positionMatchMapper = positionMatchMapper;
    }

    /**
     * Save a positionMatch.
     *
     * @param positionMatchDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionMatchDTO save(PositionMatchDTO positionMatchDTO) {
        LOG.debug("Request to save PositionMatch : {}", positionMatchDTO);
        PositionMatch positionMatch = positionMatchMapper.toEntity(positionMatchDTO);
        positionMatch = positionMatchRepository.save(positionMatch);
        return positionMatchMapper.toDto(positionMatch);
    }

    /**
     * Update a positionMatch.
     *
     * @param positionMatchDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionMatchDTO update(PositionMatchDTO positionMatchDTO) {
        LOG.debug("Request to update PositionMatch : {}", positionMatchDTO);
        PositionMatch positionMatch = positionMatchMapper.toEntity(positionMatchDTO);
        positionMatch = positionMatchRepository.save(positionMatch);
        return positionMatchMapper.toDto(positionMatch);
    }

    /**
     * Partially update a positionMatch.
     *
     * @param positionMatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionMatchDTO> partialUpdate(PositionMatchDTO positionMatchDTO) {
        LOG.debug("Request to partially update PositionMatch : {}", positionMatchDTO);

        return positionMatchRepository
            .findById(positionMatchDTO.getId())
            .map(existingPositionMatch -> {
                positionMatchMapper.partialUpdate(existingPositionMatch, positionMatchDTO);

                return existingPositionMatch;
            })
            .map(positionMatchRepository::save)
            .map(positionMatchMapper::toDto);
    }

    /**
     * Get all the positionMatches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionMatchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionMatchRepository.findAllWithEagerRelationships(pageable).map(positionMatchMapper::toDto);
    }

    /**
     * Get one positionMatch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionMatchDTO> findOne(Long id) {
        LOG.debug("Request to get PositionMatch : {}", id);
        return positionMatchRepository.findOneWithEagerRelationships(id).map(positionMatchMapper::toDto);
    }

    /**
     * Delete the positionMatch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PositionMatch : {}", id);
        positionMatchRepository.deleteById(id);
    }
}
