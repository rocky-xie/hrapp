package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.SelectionActivity;
import top.btmdc.hr.repository.SelectionActivityRepository;
import top.btmdc.hr.service.dto.SelectionActivityDTO;
import top.btmdc.hr.service.mapper.SelectionActivityMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.SelectionActivity}.
 */
@Service
@Transactional
public class SelectionActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(SelectionActivityService.class);

    private final SelectionActivityRepository selectionActivityRepository;

    private final SelectionActivityMapper selectionActivityMapper;

    public SelectionActivityService(
        SelectionActivityRepository selectionActivityRepository,
        SelectionActivityMapper selectionActivityMapper
    ) {
        this.selectionActivityRepository = selectionActivityRepository;
        this.selectionActivityMapper = selectionActivityMapper;
    }

    /**
     * Save a selectionActivity.
     *
     * @param selectionActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public SelectionActivityDTO save(SelectionActivityDTO selectionActivityDTO) {
        LOG.debug("Request to save SelectionActivity : {}", selectionActivityDTO);
        SelectionActivity selectionActivity = selectionActivityMapper.toEntity(selectionActivityDTO);
        selectionActivity = selectionActivityRepository.save(selectionActivity);
        return selectionActivityMapper.toDto(selectionActivity);
    }

    /**
     * Update a selectionActivity.
     *
     * @param selectionActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public SelectionActivityDTO update(SelectionActivityDTO selectionActivityDTO) {
        LOG.debug("Request to update SelectionActivity : {}", selectionActivityDTO);
        SelectionActivity selectionActivity = selectionActivityMapper.toEntity(selectionActivityDTO);
        selectionActivity = selectionActivityRepository.save(selectionActivity);
        return selectionActivityMapper.toDto(selectionActivity);
    }

    /**
     * Partially update a selectionActivity.
     *
     * @param selectionActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SelectionActivityDTO> partialUpdate(SelectionActivityDTO selectionActivityDTO) {
        LOG.debug("Request to partially update SelectionActivity : {}", selectionActivityDTO);

        return selectionActivityRepository
            .findById(selectionActivityDTO.getId())
            .map(existingSelectionActivity -> {
                selectionActivityMapper.partialUpdate(existingSelectionActivity, selectionActivityDTO);

                return existingSelectionActivity;
            })
            .map(selectionActivityRepository::save)
            .map(selectionActivityMapper::toDto);
    }

    /**
     * Get one selectionActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SelectionActivityDTO> findOne(Long id) {
        LOG.debug("Request to get SelectionActivity : {}", id);
        return selectionActivityRepository.findById(id).map(selectionActivityMapper::toDto);
    }

    /**
     * Delete the selectionActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SelectionActivity : {}", id);
        selectionActivityRepository.deleteById(id);
    }
}
