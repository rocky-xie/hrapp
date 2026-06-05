package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.TrainingType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.TrainingRecord} entity.
 */
@Schema(description = "培训记录。实际发生的培训活动记录。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate trainingDate;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    @Size(max = 150)
    private String topic;

    @Lob
    private String taskDescription;

    @Lob
    private String resultDescription;

    @Lob
    private String evidence;

    @Lob
    private String nextAction;

    @NotNull
    private PersonDTO person;

    private TrainingGoalDTO trainingGoal;

    private PositionDTO position;

    private PersonDTO mentor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public TrainingGoalDTO getTrainingGoal() {
        return trainingGoal;
    }

    public void setTrainingGoal(TrainingGoalDTO trainingGoal) {
        this.trainingGoal = trainingGoal;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public PersonDTO getMentor() {
        return mentor;
    }

    public void setMentor(PersonDTO mentor) {
        this.mentor = mentor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingRecordDTO)) {
            return false;
        }

        TrainingRecordDTO trainingRecordDTO = (TrainingRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingRecordDTO{" +
            "id=" + getId() +
            ", trainingDate='" + getTrainingDate() + "'" +
            ", trainingType='" + getTrainingType() + "'" +
            ", topic='" + getTopic() + "'" +
            ", taskDescription='" + getTaskDescription() + "'" +
            ", resultDescription='" + getResultDescription() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", nextAction='" + getNextAction() + "'" +
            ", person=" + getPerson() +
            ", trainingGoal=" + getTrainingGoal() +
            ", position=" + getPosition() +
            ", mentor=" + getMentor() +
            "}";
    }
}
