package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.PlanStatus;

/**
 * A DTO for the {@link top.btmdc.hr.domain.ImprovementPlan} entity.
 */
@Schema(description = "改善计划。针对问题制定的改善行动计划。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImprovementPlanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String planName;

    @NotNull
    private PlanStatus planStatus;

    @Lob
    private String problemSummary;

    @Lob
    private String improvementAction;

    @Size(max = 100)
    private String ownerName;

    private LocalDate startDate;

    private LocalDate targetDate;

    private LocalDate completionDate;

    @Lob
    private String reviewResult;

    private PositionDTO position;

    private SkillDTO skill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public PlanStatus getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(PlanStatus planStatus) {
        this.planStatus = planStatus;
    }

    public String getProblemSummary() {
        return problemSummary;
    }

    public void setProblemSummary(String problemSummary) {
        this.problemSummary = problemSummary;
    }

    public String getImprovementAction() {
        return improvementAction;
    }

    public void setImprovementAction(String improvementAction) {
        this.improvementAction = improvementAction;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImprovementPlanDTO)) {
            return false;
        }

        ImprovementPlanDTO improvementPlanDTO = (ImprovementPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, improvementPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImprovementPlanDTO{" +
            "id=" + getId() +
            ", planName='" + getPlanName() + "'" +
            ", planStatus='" + getPlanStatus() + "'" +
            ", problemSummary='" + getProblemSummary() + "'" +
            ", improvementAction='" + getImprovementAction() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", completionDate='" + getCompletionDate() + "'" +
            ", reviewResult='" + getReviewResult() + "'" +
            ", position=" + getPosition() +
            ", skill=" + getSkill() +
            "}";
    }
}
