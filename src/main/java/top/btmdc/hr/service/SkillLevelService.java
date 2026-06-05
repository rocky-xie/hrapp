package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.repository.SkillLevelRepository;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.service.mapper.SkillLevelMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.SkillLevel}.
 */
@Service
@Transactional
public class SkillLevelService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillLevelService.class);

    private final SkillLevelRepository skillLevelRepository;

    private final SkillLevelMapper skillLevelMapper;

    public SkillLevelService(SkillLevelRepository skillLevelRepository, SkillLevelMapper skillLevelMapper) {
        this.skillLevelRepository = skillLevelRepository;
        this.skillLevelMapper = skillLevelMapper;
    }

    /**
     * Save a skillLevel.
     *
     * @param skillLevelDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillLevelDTO save(SkillLevelDTO skillLevelDTO) {
        LOG.debug("Request to save SkillLevel : {}", skillLevelDTO);
        SkillLevel skillLevel = skillLevelMapper.toEntity(skillLevelDTO);
        skillLevel = skillLevelRepository.save(skillLevel);
        return skillLevelMapper.toDto(skillLevel);
    }

    /**
     * Update a skillLevel.
     *
     * @param skillLevelDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillLevelDTO update(SkillLevelDTO skillLevelDTO) {
        LOG.debug("Request to update SkillLevel : {}", skillLevelDTO);
        SkillLevel skillLevel = skillLevelMapper.toEntity(skillLevelDTO);
        skillLevel = skillLevelRepository.save(skillLevel);
        return skillLevelMapper.toDto(skillLevel);
    }

    /**
     * Partially update a skillLevel.
     *
     * @param skillLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SkillLevelDTO> partialUpdate(SkillLevelDTO skillLevelDTO) {
        LOG.debug("Request to partially update SkillLevel : {}", skillLevelDTO);

        return skillLevelRepository
            .findById(skillLevelDTO.getId())
            .map(existingSkillLevel -> {
                skillLevelMapper.partialUpdate(existingSkillLevel, skillLevelDTO);

                return existingSkillLevel;
            })
            .map(skillLevelRepository::save)
            .map(skillLevelMapper::toDto);
    }

    /**
     * Get one skillLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SkillLevelDTO> findOne(Long id) {
        LOG.debug("Request to get SkillLevel : {}", id);
        return skillLevelRepository.findById(id).map(skillLevelMapper::toDto);
    }

    /**
     * Delete the skillLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SkillLevel : {}", id);
        skillLevelRepository.deleteById(id);
    }
}
