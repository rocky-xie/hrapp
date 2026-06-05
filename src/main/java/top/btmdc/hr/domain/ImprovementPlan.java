package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.PlanStatus;

/**
 * 改善计划。针对问题制定的改善行动计划。
 */
@Entity
@Table(name = "improvement_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImprovementPlan implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "plan_name", length = 150, nullable = false)
    private String planName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status", nullable = false)
    private PlanStatus planStatus;

    @Lob
    @Column(name = "problem_summary")
    private String problemSummary;

    @Lob
    @Column(name = "improvement_action")
    private String improvementAction;

    @Size(max = 100)
    @Column(name = "owner_name", length = 100)
    private String ownerName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Lob
    @Column(name = "review_result")
    private String reviewResult;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImprovementPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return this.planName;
    }

    public ImprovementPlan planName(String planName) {
        this.setPlanName(planName);
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public PlanStatus getPlanStatus() {
        return this.planStatus;
    }

    public ImprovementPlan planStatus(PlanStatus planStatus) {
        this.setPlanStatus(planStatus);
        return this;
    }

    public void setPlanStatus(PlanStatus planStatus) {
        this.planStatus = planStatus;
    }

    public String getProblemSummary() {
        return this.problemSummary;
    }

    public ImprovementPlan problemSummary(String problemSummary) {
        this.setProblemSummary(problemSummary);
        return this;
    }

    public void setProblemSummary(String problemSummary) {
        this.problemSummary = problemSummary;
    }

    public String getImprovementAction() {
        return this.improvementAction;
    }

    public ImprovementPlan improvementAction(String improvementAction) {
        this.setImprovementAction(improvementAction);
        return this;
    }

    public void setImprovementAction(String improvementAction) {
        this.improvementAction = improvementAction;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public ImprovementPlan ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ImprovementPlan startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return this.targetDate;
    }

    public ImprovementPlan targetDate(LocalDate targetDate) {
        this.setTargetDate(targetDate);
        return this;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getCompletionDate() {
        return this.completionDate;
    }

    public ImprovementPlan completionDate(LocalDate completionDate) {
        this.setCompletionDate(completionDate);
        return this;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getReviewResult() {
        return this.reviewResult;
    }

    public ImprovementPlan reviewResult(String reviewResult) {
        this.setReviewResult(reviewResult);
        return this;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ImprovementPlan position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public ImprovementPlan skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImprovementPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((ImprovementPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImprovementPlan{" +
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
            "}";
    }
}
