package top.btmdc.hr.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.enumeration.RequirementImportance;
import top.btmdc.hr.domain.enumeration.TrainingType;
import top.btmdc.hr.service.dto.report.PersonGapDTO;
import top.btmdc.hr.service.dto.report.PositionGapDTO;
import top.btmdc.hr.service.dto.report.SkillGapDTO;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO.SuggestionPriority;
import top.btmdc.hr.service.dto.report.TrainingSuggestionDTO.SuggestionStatus;

@Service
@Transactional
public class TrainingSuggestionService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingSuggestionService.class);

    public List<TrainingSuggestionDTO> suggest(List<PositionGapDTO> positionGaps) {
        LOG.debug("Generating training suggestions from {} positions", positionGaps.size());
        List<TrainingSuggestionDTO> suggestions = new ArrayList<>();

        for (PositionGapDTO posGap : positionGaps) {
            for (PersonGapDTO personGap : concat(posGap.getOwners(), posGap.getCandidates())) {
                for (SkillGapDTO skillGap : personGap.getGaps()) {
                    suggestions.add(buildSuggestion(posGap, personGap, skillGap));
                }
            }
        }

        suggestions.sort(Comparator.comparingInt(s -> s.getPriority().ordinal()));
        return suggestions;
    }

    private TrainingSuggestionDTO buildSuggestion(PositionGapDTO posGap, PersonGapDTO personGap, SkillGapDTO skillGap) {
        TrainingSuggestionDTO suggestion = new TrainingSuggestionDTO();
        suggestion.setPersonId(personGap.getPersonId());
        suggestion.setPersonName(personGap.getPersonName());
        suggestion.setSkillId(skillGap.getSkillId());
        suggestion.setSkillName(skillGap.getSkillName());
        suggestion.setPositionId(posGap.getPositionId());
        suggestion.setPositionName(posGap.getPositionName());
        suggestion.setCurrentLevelCode(skillGap.getCurrentLevelCode());
        suggestion.setTargetLevelCode(skillGap.getRequiredLevelCode());
        suggestion.setImportance(skillGap.getImportance());
        suggestion.setStatus(SuggestionStatus.PENDING);

        int personLevel = skillGap.getCurrentLevelSortOrder() != null ? skillGap.getCurrentLevelSortOrder() : -1;
        int reqLevel = skillGap.getRequiredLevelSortOrder() != null ? skillGap.getRequiredLevelSortOrder() : 0;
        int gapLevel = personLevel < 0 ? 99 : reqLevel - personLevel;
        suggestion.setGapLevel(gapLevel);

        RequirementImportance importance = skillGap.getImportance() != null ? skillGap.getImportance() : RequirementImportance.OPTIONAL;

        if (importance == RequirementImportance.REQUIRED && personLevel < 0) {
            suggestion.setPriority(SuggestionPriority.P0_CRITICAL);
            suggestion.setSuggestedTrainingType(TrainingType.ONBOARDING);
            suggestion.setSuggestionReason("Required skill not yet acquired — arrange foundational training");
        } else if (importance == RequirementImportance.REQUIRED && gapLevel >= 2) {
            suggestion.setPriority(SuggestionPriority.P1_HIGH);
            suggestion.setSuggestedTrainingType(TrainingType.PRACTICE);
            suggestion.setSuggestionReason("Required skill level gap ≥ 2 levels — arrange advanced training");
        } else if (importance == RequirementImportance.IMPORTANT && gapLevel >= 1) {
            suggestion.setPriority(SuggestionPriority.P2_MEDIUM);
            suggestion.setSuggestedTrainingType(TrainingType.CASE_REVIEW);
            suggestion.setSuggestionReason("Important skill gap — arrange practice or case review");
        } else {
            suggestion.setPriority(SuggestionPriority.P3_LOW);
            suggestion.setSuggestedTrainingType(TrainingType.DOCUMENTATION);
            suggestion.setSuggestionReason("Optional skill gap — recommend self-study via documentation");
        }

        return suggestion;
    }

    private List<PersonGapDTO> concat(List<PersonGapDTO> a, List<PersonGapDTO> b) {
        List<PersonGapDTO> result = new ArrayList<>();
        if (a != null) result.addAll(a);
        if (b != null) result.addAll(b);
        return result;
    }
}
