package top.btmdc.hr.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.btmdc.hr.service.TrainingGoalService;
import top.btmdc.hr.service.TrainingSuggestionService;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.dto.report.PositionGapDTO;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO;

@RestController
@RequestMapping("/api/reports")
public class TrainingSuggestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingSuggestionResource.class);

    private final TrainingSuggestionService trainingSuggestionService;
    private final TrainingGoalService trainingGoalService;

    public TrainingSuggestionResource(TrainingSuggestionService trainingSuggestionService, TrainingGoalService trainingGoalService) {
        this.trainingSuggestionService = trainingSuggestionService;
        this.trainingGoalService = trainingGoalService;
    }

    @PostMapping("/training-suggestions")
    public ResponseEntity<List<TrainingSuggestionDTO>> getTrainingSuggestions(@RequestBody List<PositionGapDTO> positionGaps) {
        LOG.debug("REST request to get training suggestions");
        List<TrainingSuggestionDTO> suggestions = trainingSuggestionService.suggest(positionGaps);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/training-goals/from-suggestion")
    public ResponseEntity<TrainingGoalDTO> createTrainingGoalFromSuggestion(@RequestBody TrainingSuggestionDTO suggestion)
        throws URISyntaxException {
        LOG.debug(
            "REST request to create TrainingGoal from suggestion: personId={}, skillId={}",
            suggestion.getPersonId(),
            suggestion.getSkillId()
        );

        TrainingGoalDTO result = trainingGoalService.createFromSuggestion(suggestion);
        return ResponseEntity.created(new URI("/api/training-goals/" + result.getId())).body(result);
    }
}
