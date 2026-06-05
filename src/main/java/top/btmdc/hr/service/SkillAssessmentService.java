package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.SkillAssessment;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.repository.SkillAssessmentRepository;
import top.btmdc.hr.service.dto.SkillAssessmentDTO;
import top.btmdc.hr.service.mapper.SkillAssessmentMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.SkillAssessment}.
 */
@Service
@Transactional
public class SkillAssessmentService {

    private static final Logger LOG = LoggerFactory.getLogger(SkillAssessmentService.class);

    private final SkillAssessmentRepository skillAssessmentRepository;

    private final SkillAssessmentMapper skillAssessmentMapper;

    private final PersonSkillRepository personSkillRepository;

    public SkillAssessmentService(
        SkillAssessmentRepository skillAssessmentRepository,
        SkillAssessmentMapper skillAssessmentMapper,
        PersonSkillRepository personSkillRepository
    ) {
        this.skillAssessmentRepository = skillAssessmentRepository;
        this.skillAssessmentMapper = skillAssessmentMapper;
        this.personSkillRepository = personSkillRepository;
    }

    /**
     * Save a skillAssessment.
     *
     * @param skillAssessmentDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillAssessmentDTO save(SkillAssessmentDTO skillAssessmentDTO) {
        LOG.debug("Request to save SkillAssessment : {}", skillAssessmentDTO);
        SkillAssessment skillAssessment = skillAssessmentMapper.toEntity(skillAssessmentDTO);
        syncPersonSkill(skillAssessment);
        skillAssessment = skillAssessmentRepository.save(skillAssessment);
        return skillAssessmentMapper.toDto(skillAssessment);
    }

    /**
     * Update a skillAssessment.
     *
     * @param skillAssessmentDTO the entity to save.
     * @return the persisted entity.
     */
    public SkillAssessmentDTO update(SkillAssessmentDTO skillAssessmentDTO) {
        LOG.debug("Request to update SkillAssessment : {}", skillAssessmentDTO);
        SkillAssessment skillAssessment = skillAssessmentMapper.toEntity(skillAssessmentDTO);
        syncPersonSkill(skillAssessment);
        skillAssessment = skillAssessmentRepository.save(skillAssessment);
        return skillAssessmentMapper.toDto(skillAssessment);
    }

    /**
     * Partially update a skillAssessment.
     *
     * @param skillAssessmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SkillAssessmentDTO> partialUpdate(SkillAssessmentDTO skillAssessmentDTO) {
        LOG.debug("Request to partially update SkillAssessment : {}", skillAssessmentDTO);

        return skillAssessmentRepository
            .findById(skillAssessmentDTO.getId())
            .map(existingSkillAssessment -> {
                skillAssessmentMapper.partialUpdate(existingSkillAssessment, skillAssessmentDTO);

                return existingSkillAssessment;
            })
            .map(skillAssessmentRepository::save)
            .map(skillAssessmentMapper::toDto);
    }

    /**
     * Get all the skillAssessments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SkillAssessmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return skillAssessmentRepository.findAllWithEagerRelationships(pageable).map(skillAssessmentMapper::toDto);
    }

    /**
     * Get one skillAssessment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SkillAssessmentDTO> findOne(Long id) {
        LOG.debug("Request to get SkillAssessment : {}", id);
        return skillAssessmentRepository.findOneWithEagerRelationships(id).map(skillAssessmentMapper::toDto);
    }

    /**
     * Delete the skillAssessment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SkillAssessment : {}", id);
        skillAssessmentRepository.deleteById(id);
    }

    private void syncPersonSkill(SkillAssessment skillAssessment) {
        if (
            skillAssessment.getPerson() == null ||
            skillAssessment.getPerson().getId() == null ||
            skillAssessment.getSkill() == null ||
            skillAssessment.getSkill().getId() == null ||
            skillAssessment.getNewLevel() == null
        ) {
            return;
        }

        PersonSkill personSkill = personSkillRepository
            .findOneByPersonIdAndSkillIdWithLevel(skillAssessment.getPerson().getId(), skillAssessment.getSkill().getId())
            .orElseGet(() ->
                new PersonSkill()
                    .person(skillAssessment.getPerson())
                    .skill(skillAssessment.getSkill())
                    .assessmentDate(skillAssessment.getAssessmentDate())
            );
        personSkill.previousLevel(personSkill.getCurrentLevel());
        personSkill.currentLevel(skillAssessment.getNewLevel());
        personSkill.assessmentDate(skillAssessment.getAssessmentDate());
        personSkillRepository.save(personSkill);
    }
}
