package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

public class SkillGapDTO implements Serializable {

    private Long skillId;
    private String skillName;
    private String requiredLevelCode;
    private Integer requiredLevelSortOrder;
    private String currentLevelCode;
    private Integer currentLevelSortOrder;
    private RequirementImportance importance;

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

    public String getRequiredLevelCode() {
        return requiredLevelCode;
    }

    public void setRequiredLevelCode(String requiredLevelCode) {
        this.requiredLevelCode = requiredLevelCode;
    }

    public Integer getRequiredLevelSortOrder() {
        return requiredLevelSortOrder;
    }

    public void setRequiredLevelSortOrder(Integer requiredLevelSortOrder) {
        this.requiredLevelSortOrder = requiredLevelSortOrder;
    }

    public String getCurrentLevelCode() {
        return currentLevelCode;
    }

    public void setCurrentLevelCode(String currentLevelCode) {
        this.currentLevelCode = currentLevelCode;
    }

    public Integer getCurrentLevelSortOrder() {
        return currentLevelSortOrder;
    }

    public void setCurrentLevelSortOrder(Integer currentLevelSortOrder) {
        this.currentLevelSortOrder = currentLevelSortOrder;
    }

    public RequirementImportance getImportance() {
        return importance;
    }

    public void setImportance(RequirementImportance importance) {
        this.importance = importance;
    }
}
