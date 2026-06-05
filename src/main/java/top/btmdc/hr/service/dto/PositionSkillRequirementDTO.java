package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PositionSkillRequirement} entity.
 */
@Schema(description = "职位技能要求。描述某职位要求哪些技能及等级门槛。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionSkillRequirementDTO implements Serializable {

    private Long id;

    @NotNull
    private RequirementImportance importance;

    @Lob
    private String remark;

    @NotNull
    private PositionDTO position;

    @NotNull
    private SkillDTO skill;

    @NotNull
    private SkillLevelDTO requiredLevel;

    private SkillLevelDTO preferredLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequirementImportance getImportance() {
        return importance;
    }

    public void setImportance(RequirementImportance importance) {
        this.importance = importance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public SkillDTO getSkill() {
        return skill;
    }

    public void setSkill(SkillDTO skill) {
        this.skill = skill;
    }

    public SkillLevelDTO getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(SkillLevelDTO requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public SkillLevelDTO getPreferredLevel() {
        return preferredLevel;
    }

    public void setPreferredLevel(SkillLevelDTO preferredLevel) {
        this.preferredLevel = preferredLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionSkillRequirementDTO)) {
            return false;
        }

        PositionSkillRequirementDTO positionSkillRequirementDTO = (PositionSkillRequirementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionSkillRequirementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionSkillRequirementDTO{" +
            "id=" + getId() +
            ", importance='" + getImportance() + "'" +
            ", remark='" + getRemark() + "'" +
            ", position=" + getPosition() +
            ", skill=" + getSkill() +
            ", requiredLevel=" + getRequiredLevel() +
            ", preferredLevel=" + getPreferredLevel() +
            "}";
    }
}
