package top.btmdc.hr.service;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.enumeration.PlanStatus;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.SkillRepository;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO;
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

    private final PersonRepository personRepository;

    private final SkillRepository skillRepository;

    private final PositionRepository positionRepository;

    public TrainingGoalService(
        TrainingGoalRepository trainingGoalRepository,
        TrainingGoalMapper trainingGoalMapper,
        PersonRepository personRepository,
        SkillRepository skillRepository,
        PositionRepository positionRepository
    ) {
        this.trainingGoalRepository = trainingGoalRepository;
        this.trainingGoalMapper = trainingGoalMapper;
        this.personRepository = personRepository;
        this.skillRepository = skillRepository;
        this.positionRepository = positionRepository;
    }

    public TrainingGoalDTO createFromSuggestion(TrainingSuggestionDTO suggestion) {
        LOG.debug(
            "Request to create TrainingGoal from suggestion: personId={}, skillId={}",
            suggestion.getPersonId(),
            suggestion.getSkillId()
        );

        Person person = null;
        if (suggestion.getPersonId() != null) {
            person = personRepository
                .findById(suggestion.getPersonId())
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + suggestion.getPersonId()));
        }
        Skill skill = null;
        if (suggestion.getSkillId() != null) {
            skill = skillRepository
                .findById(suggestion.getSkillId())
                .orElseThrow(() -> new IllegalArgumentException("Skill not found: " + suggestion.getSkillId()));
        }
        Position position = null;
        if (suggestion.getPositionId() != null) {
            position = positionRepository
                .findById(suggestion.getPositionId())
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + suggestion.getPositionId()));
        }

        TrainingGoal goal = new TrainingGoal();
        goal.setGoalName(
            suggestion.getGoalName() != null
                ? suggestion.getGoalName()
                : "Train " + suggestion.getSkillName() + " for " + suggestion.getPersonName()
        );
        goal.setGoalDescription(
            suggestion.getGoalDescription() != null
                ? suggestion.getGoalDescription()
                : "Generated from skill gap report — " + suggestion.getSuggestionReason()
        );
        goal.setTargetLevelDescription(
            suggestion.getTargetLevelDescription() != null
                ? suggestion.getTargetLevelDescription()
                : "Target: " + suggestion.getTargetLevelCode()
        );
        goal.setStartDate(LocalDate.now());
        goal.setStatus(PlanStatus.DRAFT);
        goal.setPerson(person);
        goal.setSkill(skill);
        goal.setPosition(position);

        return trainingGoalMapper.toDto(trainingGoalRepository.save(goal));
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
