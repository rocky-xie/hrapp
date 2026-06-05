package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.PlanStatus;

/**
 * A DTO for the {@link top.btmdc.hr.domain.TrainingGoal} entity.
 */
@Schema(description = "培训目标。为人员或群体设定的技能提升计划。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingGoalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String goalName;

    @Lob
    private String goalDescription;

    @Lob
    private String targetLevelDescription;

    private LocalDate startDate;

    private LocalDate targetDate;

    @NotNull
    private PlanStatus status;

    private PersonDTO person;

    private PositionDTO position;

    private SkillDTO skill;

    private SkillLevelDTO targetLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public void setStatus(PlanStatus status) {
        this.status = status;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
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

    public SkillLevelDTO getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(SkillLevelDTO targetLevel) {
        this.targetLevel = targetLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingGoalDTO)) {
            return false;
        }

        TrainingGoalDTO trainingGoalDTO = (TrainingGoalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingGoalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingGoalDTO{" +
            "id=" + getId() +
            ", goalName='" + getGoalName() + "'" +
            ", goalDescription='" + getGoalDescription() + "'" +
            ", targetLevelDescription='" + getTargetLevelDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            ", skill=" + getSkill() +
            ", targetLevel=" + getTargetLevel() +
            "}";
    }
}
