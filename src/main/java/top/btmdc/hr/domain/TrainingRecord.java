package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.TrainingType;

/**
 * 培训记录。实际发生的培训活动记录。
 */
@Entity
@Table(name = "training_record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type", nullable = false)
    private TrainingType trainingType;

    @NotNull
    @Size(max = 150)
    @Column(name = "topic", length = 150, nullable = false)
    private String topic;

    @Lob
    @Column(name = "task_description")
    private String taskDescription;

    @Lob
    @Column(name = "result_description")
    private String resultDescription;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @Lob
    @Column(name = "next_action")
    private String nextAction;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "person", "position", "skill", "targetLevel" }, allowSetters = true)
    private TrainingGoal trainingGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person mentor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainingRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTrainingDate() {
        return this.trainingDate;
    }

    public TrainingRecord trainingDate(LocalDate trainingDate) {
        this.setTrainingDate(trainingDate);
        return this;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingType getTrainingType() {
        return this.trainingType;
    }

    public TrainingRecord trainingType(TrainingType trainingType) {
        this.setTrainingType(trainingType);
        return this;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getTopic() {
        return this.topic;
    }

    public TrainingRecord topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTaskDescription() {
        return this.taskDescription;
    }

    public TrainingRecord taskDescription(String taskDescription) {
        this.setTaskDescription(taskDescription);
        return this;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getResultDescription() {
        return this.resultDescription;
    }

    public TrainingRecord resultDescription(String resultDescription) {
        this.setResultDescription(resultDescription);
        return this;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public TrainingRecord evidence(String evidence) {
        this.setEvidence(evidence);
        return this;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getNextAction() {
        return this.nextAction;
    }

    public TrainingRecord nextAction(String nextAction) {
        this.setNextAction(nextAction);
        return this;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TrainingRecord person(Person person) {
        this.setPerson(person);
        return this;
    }

    public TrainingGoal getTrainingGoal() {
        return this.trainingGoal;
    }

    public void setTrainingGoal(TrainingGoal trainingGoal) {
        this.trainingGoal = trainingGoal;
    }

    public TrainingRecord trainingGoal(TrainingGoal trainingGoal) {
        this.setTrainingGoal(trainingGoal);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TrainingRecord position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Person getMentor() {
        return this.mentor;
    }

    public void setMentor(Person person) {
        this.mentor = person;
    }

    public TrainingRecord mentor(Person person) {
        this.setMentor(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((TrainingRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingRecord{" +
            "id=" + getId() +
            ", trainingDate='" + getTrainingDate() + "'" +
            ", trainingType='" + getTrainingType() + "'" +
            ", topic='" + getTopic() + "'" +
            ", taskDescription='" + getTaskDescription() + "'" +
            ", resultDescription='" + getResultDescription() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", nextAction='" + getNextAction() + "'" +
            "}";
    }
}
