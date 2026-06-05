package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.ConfidenceLevel;

/**
 * 人员技能关联。记录某人掌握某项技能及当前等级。
 */
@Entity
@Table(name = "person_skill")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonSkill implements Serializable {

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

    @Column(name = "next_review_date")
    private LocalDate nextReviewDate;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "confidence")
    private ConfidenceLevel confidence;

    @Lob
    @Column(name = "growth_direction")
    private String growthDirection;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    private Skill skill;

    @ManyToOne(optional = false)
    @NotNull
    private SkillLevel currentLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillLevel previousLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person verifiedBy;

    @Column(name = "verified_date")
    private LocalDate verifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonSkill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssessmentDate() {
        return this.assessmentDate;
    }

    public PersonSkill assessmentDate(LocalDate assessmentDate) {
        this.setAssessmentDate(assessmentDate);
        return this;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public LocalDate getNextReviewDate() {
        return this.nextReviewDate;
    }

    public PersonSkill nextReviewDate(LocalDate nextReviewDate) {
        this.setNextReviewDate(nextReviewDate);
        return this;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public PersonSkill evidence(String evidence) {
        this.setEvidence(evidence);
        return this;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public ConfidenceLevel getConfidence() {
        return this.confidence;
    }

    public PersonSkill confidence(ConfidenceLevel confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(ConfidenceLevel confidence) {
        this.confidence = confidence;
    }

    public String getGrowthDirection() {
        return this.growthDirection;
    }

    public PersonSkill growthDirection(String growthDirection) {
        this.setGrowthDirection(growthDirection);
        return this;
    }

    public void setGrowthDirection(String growthDirection) {
        this.growthDirection = growthDirection;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonSkill person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public PersonSkill skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public SkillLevel getCurrentLevel() {
        return this.currentLevel;
    }

    public void setCurrentLevel(SkillLevel skillLevel) {
        this.currentLevel = skillLevel;
    }

    public PersonSkill currentLevel(SkillLevel skillLevel) {
        this.setCurrentLevel(skillLevel);
        return this;
    }

    public SkillLevel getPreviousLevel() {
        return this.previousLevel;
    }

    public void setPreviousLevel(SkillLevel skillLevel) {
        this.previousLevel = skillLevel;
    }

    public PersonSkill previousLevel(SkillLevel skillLevel) {
        this.setPreviousLevel(skillLevel);
        return this;
    }

    public Person getVerifiedBy() {
        return this.verifiedBy;
    }

    public void setVerifiedBy(Person person) {
        this.verifiedBy = person;
    }

    public PersonSkill verifiedBy(Person person) {
        this.setVerifiedBy(person);
        return this;
    }

    public LocalDate getVerifiedDate() {
        return this.verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public PersonSkill verifiedDate(LocalDate verifiedDate) {
        this.setVerifiedDate(verifiedDate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonSkill)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonSkill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonSkill{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", nextReviewDate='" + getNextReviewDate() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", confidence='" + getConfidence() + "'" +
            ", growthDirection='" + getGrowthDirection() + "'" +
            ", verifiedDate='" + getVerifiedDate() + "'" +
            "}";
    }
}
