package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.SkillChangeType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.SkillUpgradeRecord} entity.
 */
@Schema(description = "技能升级记录。追踪某人某项技能的变动历史（新增、升级、降级等）。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillUpgradeRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private SkillChangeType changeType;

    @NotNull
    private LocalDate changeDate;

    @NotNull
    @Size(max = 200)
    private String reason;

    @Size(max = 50)
    private String beforeLevelLabel;

    @Size(max = 50)
    private String afterLevelLabel;

    @Lob
    private String evidence;

    @Lob
    private String comment;

    @NotNull
    private PersonDTO person;

    @NotNull
    private SkillDTO skill;

    private SkillLevelDTO oldLevel;

    @NotNull
    private SkillLevelDTO newLevel;

    private PersonDTO assessor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(SkillChangeType changeType) {
        this.changeType = changeType;
    }

    public LocalDate getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDate changeDate) {
        this.changeDate = changeDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBeforeLevelLabel() {
        return beforeLevelLabel;
    }

    public void setBeforeLevelLabel(String beforeLevelLabel) {
        this.beforeLevelLabel = beforeLevelLabel;
    }

    public String getAfterLevelLabel() {
        return afterLevelLabel;
    }

    public void setAfterLevelLabel(String afterLevelLabel) {
        this.afterLevelLabel = afterLevelLabel;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public SkillDTO getSkill() {
        return skill;
    }

    public void setSkill(SkillDTO skill) {
        this.skill = skill;
    }

    public SkillLevelDTO getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(SkillLevelDTO oldLevel) {
        this.oldLevel = oldLevel;
    }

    public SkillLevelDTO getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(SkillLevelDTO newLevel) {
        this.newLevel = newLevel;
    }

    public PersonDTO getAssessor() {
        return assessor;
    }

    public void setAssessor(PersonDTO assessor) {
        this.assessor = assessor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillUpgradeRecordDTO)) {
            return false;
        }

        SkillUpgradeRecordDTO skillUpgradeRecordDTO = (SkillUpgradeRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, skillUpgradeRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillUpgradeRecordDTO{" +
            "id=" + getId() +
            ", changeType='" + getChangeType() + "'" +
            ", changeDate='" + getChangeDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", beforeLevelLabel='" + getBeforeLevelLabel() + "'" +
            ", afterLevelLabel='" + getAfterLevelLabel() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", comment='" + getComment() + "'" +
            ", person=" + getPerson() +
            ", skill=" + getSkill() +
            ", oldLevel=" + getOldLevel() +
            ", newLevel=" + getNewLevel() +
            ", assessor=" + getAssessor() +
            "}";
    }
}
