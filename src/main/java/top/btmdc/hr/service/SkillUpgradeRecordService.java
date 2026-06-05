package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.SkillUpgradeRecord;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.repository.SkillUpgradeRecordRepository;
import top.btmdc.hr.service.dto.SkillUpgradeRecordDTO;
import top.btmdc.hr.service.mapper.SkillUpgradeRecordMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.SkillUpgradeRecord}.
 */
@Service
@Transactional
public class SkillUpgradeRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillUpgradeRecordService.class);

    private final SkillUpgradeRecordRepository skillUpgradeRecordRepository;

    private final SkillUpgradeRecordMapper skillUpgradeRecordMapper;

    private final PersonSkillRepository personSkillRepository;

    public SkillUpgradeRecordService(
        SkillUpgradeRecordRepository skillUpgradeRecordRepository,
        SkillUpgradeRecordMapper skillUpgradeRecordMapper,
        PersonSkillRepository personSkillRepository
    ) {
        this.skillUpgradeRecordRepository = skillUpgradeRecordRepository;
        this.skillUpgradeRecordMapper = skillUpgradeRecordMapper;
        this.personSkillRepository = personSkillRepository;
    }

    /**
     * Save a skillUpgradeRecord.
     *
     * @param skillUpgradeRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillUpgradeRecordDTO save(SkillUpgradeRecordDTO skillUpgradeRecordDTO) {
        LOG.debug("Request to save SkillUpgradeRecord : {}", skillUpgradeRecordDTO);
        SkillUpgradeRecord skillUpgradeRecord = skillUpgradeRecordMapper.toEntity(skillUpgradeRecordDTO);
        syncPersonSkill(skillUpgradeRecord);
        skillUpgradeRecord = skillUpgradeRecordRepository.save(skillUpgradeRecord);
        return skillUpgradeRecordMapper.toDto(skillUpgradeRecord);
    }

    /**
     * Update a skillUpgradeRecord.
     *
     * @param skillUpgradeRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillUpgradeRecordDTO update(SkillUpgradeRecordDTO skillUpgradeRecordDTO) {
        LOG.debug("Request to update SkillUpgradeRecord : {}", skillUpgradeRecordDTO);
        SkillUpgradeRecord skillUpgradeRecord = skillUpgradeRecordMapper.toEntity(skillUpgradeRecordDTO);
        syncPersonSkill(skillUpgradeRecord);
        skillUpgradeRecord = skillUpgradeRecordRepository.save(skillUpgradeRecord);
        return skillUpgradeRecordMapper.toDto(skillUpgradeRecord);
    }

    /**
     * Partially update a skillUpgradeRecord.
     *
     * @param skillUpgradeRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SkillUpgradeRecordDTO> partialUpdate(SkillUpgradeRecordDTO skillUpgradeRecordDTO) {
        LOG.debug("Request to partially update SkillUpgradeRecord : {}", skillUpgradeRecordDTO);

        return skillUpgradeRecordRepository
            .findById(skillUpgradeRecordDTO.getId())
            .map(existingSkillUpgradeRecord -> {
                skillUpgradeRecordMapper.partialUpdate(existingSkillUpgradeRecord, skillUpgradeRecordDTO);

                return existingSkillUpgradeRecord;
            })
            .map(skillUpgradeRecordRepository::save)
            .map(skillUpgradeRecordMapper::toDto);
    }

    /**
     * Get all the skillUpgradeRecords with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SkillUpgradeRecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return skillUpgradeRecordRepository.findAllWithEagerRelationships(pageable).map(skillUpgradeRecordMapper::toDto);
    }

    /**
     * Get one skillUpgradeRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SkillUpgradeRecordDTO> findOne(Long id) {
        LOG.debug("Request to get SkillUpgradeRecord : {}", id);
        return skillUpgradeRecordRepository.findOneWithEagerRelationships(id).map(skillUpgradeRecordMapper::toDto);
    }

    /**
     * Delete the skillUpgradeRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SkillUpgradeRecord : {}", id);
        skillUpgradeRecordRepository.deleteById(id);
    }

    private void syncPersonSkill(SkillUpgradeRecord skillUpgradeRecord) {
        if (
            skillUpgradeRecord.getPerson() == null ||
            skillUpgradeRecord.getPerson().getId() == null ||
            skillUpgradeRecord.getSkill() == null ||
            skillUpgradeRecord.getSkill().getId() == null ||
            skillUpgradeRecord.getNewLevel() == null
        ) {
            return;
        }

        PersonSkill personSkill = personSkillRepository
            .findOneByPersonIdAndSkillIdWithLevel(skillUpgradeRecord.getPerson().getId(), skillUpgradeRecord.getSkill().getId())
            .orElseGet(() ->
                new PersonSkill()
                    .person(skillUpgradeRecord.getPerson())
                    .skill(skillUpgradeRecord.getSkill())
                    .assessmentDate(skillUpgradeRecord.getChangeDate())
            );
        personSkill.previousLevel(personSkill.getCurrentLevel());
        personSkill.currentLevel(skillUpgradeRecord.getNewLevel());
        personSkill.assessmentDate(skillUpgradeRecord.getChangeDate());
        personSkillRepository.save(personSkill);
    }
}
