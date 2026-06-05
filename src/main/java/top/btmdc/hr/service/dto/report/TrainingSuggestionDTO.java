package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RequirementImportance;
import top.btmdc.hr.domain.enumeration.TrainingType;

public class TrainingSuggestionDTO implements Serializable {

    private Long personId;
    private String personName;
    private Long skillId;
    private String skillName;
    private Long positionId;
    private String positionName;
    private String currentLevelCode;
    private String targetLevelCode;
    private int gapLevel;
    private RequirementImportance importance;
    private SuggestionPriority priority;
    private TrainingType suggestedTrainingType;
    private String suggestionReason;
    private String goalName;
    private String goalDescription;
    private String targetLevelDescription;
    private SuggestionStatus status;

    public enum SuggestionPriority {
        P0_CRITICAL,
        P1_HIGH,
        P2_MEDIUM,
        P3_LOW,
    }

    public enum SuggestionStatus {
        PENDING,
        CONVERTED,
        DISMISSED,
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCurrentLevelCode() {
        return currentLevelCode;
    }

    public void setCurrentLevelCode(String currentLevelCode) {
        this.currentLevelCode = currentLevelCode;
    }

    public String getTargetLevelCode() {
        return targetLevelCode;
    }

    public void setTargetLevelCode(String targetLevelCode) {
        this.targetLevelCode = targetLevelCode;
    }

    public int getGapLevel() {
        return gapLevel;
    }

    public void setGapLevel(int gapLevel) {
        this.gapLevel = gapLevel;
    }

    public RequirementImportance getImportance() {
        return importance;
    }

    public void setImportance(RequirementImportance importance) {
        this.importance = importance;
    }

    public SuggestionPriority getPriority() {
        return priority;
    }

    public void setPriority(SuggestionPriority priority) {
        this.priority = priority;
    }

    public TrainingType getSuggestedTrainingType() {
        return suggestedTrainingType;
    }

    public void setSuggestedTrainingType(TrainingType suggestedTrainingType) {
        this.suggestedTrainingType = suggestedTrainingType;
    }

    public String getSuggestionReason() {
        return suggestionReason;
    }

    public void setSuggestionReason(String suggestionReason) {
        this.suggestionReason = suggestionReason;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public String getTargetLevelDescription() {
        return targetLevelDescription;
    }

    public void setTargetLevelDescription(String targetLevelDescription) {
        this.targetLevelDescription = targetLevelDescription;
    }

    public SuggestionStatus getStatus() {
        return status;
    }

    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }
}
