package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.domain.enumeration.ProgressStatus;

/**
 * 评价考核。周期性的综合评价。
 */
@Entity
@Table(name = "evaluation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evaluation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "evaluation_name", length = 150, nullable = false)
    private String evaluationName;

    @NotNull
    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Size(max = 100)
    @Column(name = "period_label", length = 100)
    private String periodLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status")
    private ProgressStatus progressStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private AssessmentResult result;

    @Lob
    @Column(name = "strengths")
    private String strengths;

    @Lob
    @Column(name = "weaknesses")
    private String weaknesses;

    @Lob
    @Column(name = "support_needed")
    private String supportNeeded;

    @Lob
    @Column(name = "next_training_focus")
    private String nextTrainingFocus;

    @Column(name = "position_adjustment_needed")
    private Boolean positionAdjustmentNeeded;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "person", "position", "skill", "targetLevel" }, allowSetters = true)
    private TrainingGoal trainingGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person evaluator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvaluationName() {
        return this.evaluationName;
    }

    public Evaluation evaluationName(String evaluationName) {
        this.setEvaluationName(evaluationName);
        return this;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public LocalDate getEvaluationDate() {
        return this.evaluationDate;
    }

    public Evaluation evaluationDate(LocalDate evaluationDate) {
        this.setEvaluationDate(evaluationDate);
        return this;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getPeriodLabel() {
        return this.periodLabel;
    }

    public Evaluation periodLabel(String periodLabel) {
        this.setPeriodLabel(periodLabel);
        return this;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public ProgressStatus getProgressStatus() {
        return this.progressStatus;
    }

    public Evaluation progressStatus(ProgressStatus progressStatus) {
        this.setProgressStatus(progressStatus);
        return this;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public AssessmentResult getResult() {
        return this.result;
    }

    public Evaluation result(AssessmentResult result) {
        this.setResult(result);
        return this;
    }

    public void setResult(AssessmentResult result) {
        this.result = result;
    }

    public String getStrengths() {
        return this.strengths;
    }

    public Evaluation strengths(String strengths) {
        this.setStrengths(strengths);
        return this;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return this.weaknesses;
    }

    public Evaluation weaknesses(String weaknesses) {
        this.setWeaknesses(weaknesses);
        return this;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getSupportNeeded() {
        return this.supportNeeded;
    }

    public Evaluation supportNeeded(String supportNeeded) {
        this.setSupportNeeded(supportNeeded);
        return this;
    }

    public void setSupportNeeded(String supportNeeded) {
        this.supportNeeded = supportNeeded;
    }

    public String getNextTrainingFocus() {
        return this.nextTrainingFocus;
    }

    public Evaluation nextTrainingFocus(String nextTrainingFocus) {
        this.setNextTrainingFocus(nextTrainingFocus);
        return this;
    }

    public void setNextTrainingFocus(String nextTrainingFocus) {
        this.nextTrainingFocus = nextTrainingFocus;
    }

    public Boolean getPositionAdjustmentNeeded() {
        return this.positionAdjustmentNeeded;
    }

    public Evaluation positionAdjustmentNeeded(Boolean positionAdjustmentNeeded) {
        this.setPositionAdjustmentNeeded(positionAdjustmentNeeded);
        return this;
    }

    public void setPositionAdjustmentNeeded(Boolean positionAdjustmentNeeded) {
        this.positionAdjustmentNeeded = positionAdjustmentNeeded;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Evaluation person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Evaluation position(Position position) {
        this.setPosition(position);
        return this;
    }

    public TrainingGoal getTrainingGoal() {
        return this.trainingGoal;
    }

    public void setTrainingGoal(TrainingGoal trainingGoal) {
        this.trainingGoal = trainingGoal;
    }

    public Evaluation trainingGoal(TrainingGoal trainingGoal) {
        this.setTrainingGoal(trainingGoal);
        return this;
    }

    public Person getEvaluator() {
        return this.evaluator;
    }

    public void setEvaluator(Person person) {
        this.evaluator = person;
    }

    public Evaluation evaluator(Person person) {
        this.setEvaluator(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaluation)) {
            return false;
        }
        return getId() != null && getId().equals(((Evaluation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaluation{" +
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
            "}";
    }
}
