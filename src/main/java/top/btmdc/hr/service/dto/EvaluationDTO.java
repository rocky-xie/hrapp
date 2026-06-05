package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.domain.enumeration.ProgressStatus;

/**
 * A DTO for the {@link top.btmdc.hr.domain.Evaluation} entity.
 */
@Schema(description = "评价考核。周期性的综合评价。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String evaluationName;

    @NotNull
    private LocalDate evaluationDate;

    @Size(max = 100)
    private String periodLabel;

    private ProgressStatus progressStatus;

    private AssessmentResult result;

    @Lob
    private String strengths;

    @Lob
    private String weaknesses;

    @Lob
    private String supportNeeded;

    @Lob
    private String nextTrainingFocus;

    private Boolean positionAdjustmentNeeded;

    @NotNull
    private PersonDTO person;

    private PositionDTO position;

    private TrainingGoalDTO trainingGoal;

    private PersonDTO evaluator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvaluationName() {
        return evaluationName;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public AssessmentResult getResult() {
        return result;
    }

    public void setResult(AssessmentResult result) {
        this.result = result;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getSupportNeeded() {
        return supportNeeded;
    }

    public void setSupportNeeded(String supportNeeded) {
        this.supportNeeded = supportNeeded;
    }

    public String getNextTrainingFocus() {
        return nextTrainingFocus;
    }

    public void setNextTrainingFocus(String nextTrainingFocus) {
        this.nextTrainingFocus = nextTrainingFocus;
    }

    public Boolean getPositionAdjustmentNeeded() {
        return positionAdjustmentNeeded;
    }

    public void setPositionAdjustmentNeeded(Boolean positionAdjustmentNeeded) {
        this.positionAdjustmentNeeded = positionAdjustmentNeeded;
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

    public TrainingGoalDTO getTrainingGoal() {
        return trainingGoal;
    }

    public void setTrainingGoal(TrainingGoalDTO trainingGoal) {
        this.trainingGoal = trainingGoal;
    }

    public PersonDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(PersonDTO evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationDTO)) {
            return false;
        }

        EvaluationDTO evaluationDTO = (EvaluationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evaluationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationDTO{" +
            "id=" + getId() +
            ", evaluationName='" + getEvaluationName() + "'" +
            ", evaluationDate='" + getEvaluationDate() + "'" +
            ", periodLabel='" + getPeriodLabel() + "'" +
            ", progressStatus='" + getProgressStatus() + "'" +
            ", result='" + getResult() + "'" +
            ", strengths='" + getStrengths() + "'" +
            ", weaknesses='" + getWeaknesses() + "'" +
            ", supportNeeded='" + getSupportNeeded() + "'" +
            ", nextTrainingFocus='" + getNextTrainingFocus() + "'" +
            ", positionAdjustmentNeeded='" + getPositionAdjustmentNeeded() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            ", trainingGoal=" + getTrainingGoal() +
            ", evaluator=" + getEvaluator() +
            "}";
    }
}
