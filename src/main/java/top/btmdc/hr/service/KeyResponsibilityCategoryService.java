package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.repository.KeyResponsibilityCategoryRepository;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;
import top.btmdc.hr.service.mapper.KeyResponsibilityCategoryMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.KeyResponsibilityCategory}.
 */
@Service
@Transactional
public class KeyResponsibilityCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(KeyResponsibilityCategoryService.class);

    private final KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository;

    private final KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper;

    public KeyResponsibilityCategoryService(
        KeyResponsibilityCategoryRepository keyResponsibilityCategoryRepository,
        KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper
    ) {
        this.keyResponsibilityCategoryRepository = keyResponsibilityCategoryRepository;
        this.keyResponsibilityCategoryMapper = keyResponsibilityCategoryMapper;
    }

    /**
     * Save a keyResponsibilityCategory.
     *
     * @param keyResponsibilityCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public KeyResponsibilityCategoryDTO save(KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO) {
        LOG.debug("Request to save KeyResponsibilityCategory : {}", keyResponsibilityCategoryDTO);
        KeyResponsibilityCategory keyResponsibilityCategory = keyResponsibilityCategoryMapper.toEntity(keyResponsibilityCategoryDTO);
        keyResponsibilityCategory = keyResponsibilityCategoryRepository.save(keyResponsibilityCategory);
        return keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);
    }

    /**
     * Update a keyResponsibilityCategory.
     *
     * @param keyResponsibilityCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public KeyResponsibilityCategoryDTO update(KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO) {
        LOG.debug("Request to update KeyResponsibilityCategory : {}", keyResponsibilityCategoryDTO);
        KeyResponsibilityCategory keyResponsibilityCategory = keyResponsibilityCategoryMapper.toEntity(keyResponsibilityCategoryDTO);
        keyResponsibilityCategory = keyResponsibilityCategoryRepository.save(keyResponsibilityCategory);
        return keyResponsibilityCategoryMapper.toDto(keyResponsibilityCategory);
    }

    /**
     * Partially update a keyResponsibilityCategory.
     *
     * @param keyResponsibilityCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KeyResponsibilityCategoryDTO> partialUpdate(KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO) {
        LOG.debug("Request to partially update KeyResponsibilityCategory : {}", keyResponsibilityCategoryDTO);

        return keyResponsibilityCategoryRepository
            .findById(keyResponsibilityCategoryDTO.getId())
            .map(existingKeyResponsibilityCategory -> {
                keyResponsibilityCategoryMapper.partialUpdate(existingKeyResponsibilityCategory, keyResponsibilityCategoryDTO);

                return existingKeyResponsibilityCategory;
            })
            .map(keyResponsibilityCategoryRepository::save)
            .map(keyResponsibilityCategoryMapper::toDto);
    }

    /**
     * Get one keyResponsibilityCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KeyResponsibilityCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get KeyResponsibilityCategory : {}", id);
        return keyResponsibilityCategoryRepository.findById(id).map(keyResponsibilityCategoryMapper::toDto);
    }

    /**
     * Delete the keyResponsibilityCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KeyResponsibilityCategory : {}", id);
        keyResponsibilityCategoryRepository.deleteById(id);
    }
}
