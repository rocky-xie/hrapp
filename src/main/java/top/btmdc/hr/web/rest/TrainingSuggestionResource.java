package top.btmdc.hr.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.repository.TrainingGoalRepository;
import top.btmdc.hr.service.TrainingSuggestionService;
import top.btmdc.hr.service.dto.report.PositionGapDTO;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO;

@RestController
@RequestMapping("/api/reports")
public class TrainingSuggestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingSuggestionResource.class);

    private final TrainingSuggestionService trainingSuggestionService;
    private final TrainingGoalRepository trainingGoalRepository;

    public TrainingSuggestionResource(TrainingSuggestionService trainingSuggestionService, TrainingGoalRepository trainingGoalRepository) {
        this.trainingSuggestionService = trainingSuggestionService;
        this.trainingGoalRepository = trainingGoalRepository;
    }

    @PostMapping("/training-suggestions")
    public ResponseEntity<List<TrainingSuggestionDTO>> getTrainingSuggestions(@RequestBody List<PositionGapDTO> positionGaps) {
        LOG.debug("REST request to get training suggestions");
        List<TrainingSuggestionDTO> suggestions = trainingSuggestionService.suggest(positionGaps);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/training-goals/from-suggestion")
    public ResponseEntity<TrainingGoal> createTrainingGoalFromSuggestion(@RequestBody TrainingSuggestionDTO suggestion)
        throws URISyntaxException {
        LOG.debug(
            "REST request to create TrainingGoal from suggestion: personId={}, skillId={}",
            suggestion.getPersonId(),
            suggestion.getSkillId()
        );

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
        goal.setStatus(top.btmdc.hr.domain.enumeration.PlanStatus.DRAFT);

        if (suggestion.getPersonId() != null) {
            top.btmdc.hr.domain.Person person = new top.btmdc.hr.domain.Person();
            person.setId(suggestion.getPersonId());
            goal.setPerson(person);
        }
        if (suggestion.getSkillId() != null) {
            top.btmdc.hr.domain.Skill skill = new top.btmdc.hr.domain.Skill();
            skill.setId(suggestion.getSkillId());
            goal.setSkill(skill);
        }
        if (suggestion.getPositionId() != null) {
            top.btmdc.hr.domain.Position position = new top.btmdc.hr.domain.Position();
            position.setId(suggestion.getPositionId());
            goal.setPosition(position);
        }

        TrainingGoal result = trainingGoalRepository.save(goal);
        return ResponseEntity.created(new URI("/api/training-goals/" + result.getId())).body(result);
    }
}
