package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

public class AggregatedGapDTO implements Serializable {

    private Long skillId;
    private String skillName;
    private RequirementImportance importance;
    private String requiredLevelCode;
    private Integer requiredLevelSortOrder;
    private int totalDeficient;
    private int maxDeficitLevel;

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

    public RequirementImportance getImportance() {
        return importance;
    }

    public void setImportance(RequirementImportance importance) {
        this.importance = importance;
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

    public int getTotalDeficient() {
        return totalDeficient;
    }

    public void setTotalDeficient(int totalDeficient) {
        this.totalDeficient = totalDeficient;
    }

    public int getMaxDeficitLevel() {
        return maxDeficitLevel;
    }

    public void setMaxDeficitLevel(int maxDeficitLevel) {
        this.maxDeficitLevel = maxDeficitLevel;
    }
}
