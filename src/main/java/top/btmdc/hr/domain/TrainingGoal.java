package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.PlanStatus;

/**
 * 培训目标。为人员或群体设定的技能提升计划。
 */
@Entity
@Table(name = "training_goal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingGoal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "goal_name", length = 150, nullable = false)
    private String goalName;

    @Lob
    @Column(name = "goal_description")
    private String goalDescription;

    @Lob
    @Column(name = "target_level_description")
    private String targetLevelDescription;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlanStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillLevel targetLevel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainingGoal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoalName() {
        return this.goalName;
    }

    public TrainingGoal goalName(String goalName) {
        this.setGoalName(goalName);
        return this;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalDescription() {
        return this.goalDescription;
    }

    public TrainingGoal goalDescription(String goalDescription) {
        this.setGoalDescription(goalDescription);
        return this;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public String getTargetLevelDescription() {
        return this.targetLevelDescription;
    }

    public TrainingGoal targetLevelDescription(String targetLevelDescription) {
        this.setTargetLevelDescription(targetLevelDescription);
        return this;
    }

    public void setTargetLevelDescription(String targetLevelDescription) {
        this.targetLevelDescription = targetLevelDescription;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public TrainingGoal startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return this.targetDate;
    }

    public TrainingGoal targetDate(LocalDate targetDate) {
        this.setTargetDate(targetDate);
        return this;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public PlanStatus getStatus() {
        return this.status;
    }

    public TrainingGoal status(PlanStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PlanStatus status) {
        this.status = status;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TrainingGoal person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TrainingGoal position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public TrainingGoal skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public SkillLevel getTargetLevel() {
        return this.targetLevel;
    }

    public void setTargetLevel(SkillLevel skillLevel) {
        this.targetLevel = skillLevel;
    }

    public TrainingGoal targetLevel(SkillLevel skillLevel) {
        this.setTargetLevel(skillLevel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingGoal)) {
            return false;
        }
        return getId() != null && getId().equals(((TrainingGoal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingGoal{" +
            "id=" + getId() +
            ", goalName='" + getGoalName() + "'" +
            ", goalDescription='" + getGoalDescription() + "'" +
            ", targetLevelDescription='" + getTargetLevelDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
