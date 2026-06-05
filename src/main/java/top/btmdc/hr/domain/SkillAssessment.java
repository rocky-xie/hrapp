package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.AssessmentResult;
import top.btmdc.hr.domain.enumeration.AssessmentSource;

/**
 * 技能评估。对某个人某项技能的阶段评估结果。
 */
@Entity
@Table(name = "skill_assessment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillAssessment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private AssessmentResult result;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @Lob
    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person assessor;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillLevel newLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private AssessmentSource source;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SkillAssessment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssessmentDate() {
        return this.assessmentDate;
    }

    public SkillAssessment assessmentDate(LocalDate assessmentDate) {
        this.setAssessmentDate(assessmentDate);
        return this;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public AssessmentResult getResult() {
        return this.result;
    }

    public SkillAssessment result(AssessmentResult result) {
        this.setResult(result);
        return this;
    }

    public void setResult(AssessmentResult result) {
        this.result = result;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public SkillAssessment evidence(String evidence) {
        this.setEvidence(evidence);
        return this;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getComment() {
        return this.comment;
    }

    public SkillAssessment comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public SkillAssessment person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public SkillAssessment skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public Person getAssessor() {
        return this.assessor;
    }

    public void setAssessor(Person person) {
        this.assessor = person;
    }

    public SkillAssessment assessor(Person person) {
        this.setAssessor(person);
        return this;
    }

    public SkillLevel getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(SkillLevel skillLevel) {
        this.newLevel = skillLevel;
    }

    public SkillAssessment newLevel(SkillLevel skillLevel) {
        this.setNewLevel(skillLevel);
        return this;
    }

    public AssessmentSource getSource() {
        return this.source;
    }

    public void setSource(AssessmentSource source) {
        this.source = source;
    }

    public SkillAssessment source(AssessmentSource source) {
        this.setSource(source);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillAssessment)) {
            return false;
        }
        return getId() != null && getId().equals(((SkillAssessment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillAssessment{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", result='" + getResult() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", comment='" + getComment() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
