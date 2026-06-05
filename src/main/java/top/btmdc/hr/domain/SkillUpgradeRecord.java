package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.SkillChangeType;

/**
 * 技能升级记录。追踪某人某项技能的变动历史（新增、升级、降级等）。
 */
@Entity
@Table(name = "skill_upgrade_record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillUpgradeRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private SkillChangeType changeType;

    @NotNull
    @Column(name = "change_date", nullable = false)
    private LocalDate changeDate;

    @NotNull
    @Size(max = 200)
    @Column(name = "reason", length = 200, nullable = false)
    private String reason;

    @Size(max = 50)
    @Column(name = "before_level_label", length = 50)
    private String beforeLevelLabel;

    @Size(max = 50)
    @Column(name = "after_level_label", length = 50)
    private String afterLevelLabel;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @Lob
    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillLevel oldLevel;

    @ManyToOne(optional = false)
    @NotNull
    private SkillLevel newLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person assessor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SkillUpgradeRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillChangeType getChangeType() {
        return this.changeType;
    }

    public SkillUpgradeRecord changeType(SkillChangeType changeType) {
        this.setChangeType(changeType);
        return this;
    }

    public void setChangeType(SkillChangeType changeType) {
        this.changeType = changeType;
    }

    public LocalDate getChangeDate() {
        return this.changeDate;
    }

    public SkillUpgradeRecord changeDate(LocalDate changeDate) {
        this.setChangeDate(changeDate);
        return this;
    }

    public void setChangeDate(LocalDate changeDate) {
        this.changeDate = changeDate;
    }

    public String getReason() {
        return this.reason;
    }

    public SkillUpgradeRecord reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBeforeLevelLabel() {
        return this.beforeLevelLabel;
    }

    public SkillUpgradeRecord beforeLevelLabel(String beforeLevelLabel) {
        this.setBeforeLevelLabel(beforeLevelLabel);
        return this;
    }

    public void setBeforeLevelLabel(String beforeLevelLabel) {
        this.beforeLevelLabel = beforeLevelLabel;
    }

    public String getAfterLevelLabel() {
        return this.afterLevelLabel;
    }

    public SkillUpgradeRecord afterLevelLabel(String afterLevelLabel) {
        this.setAfterLevelLabel(afterLevelLabel);
        return this;
    }

    public void setAfterLevelLabel(String afterLevelLabel) {
        this.afterLevelLabel = afterLevelLabel;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public SkillUpgradeRecord evidence(String evidence) {
        this.setEvidence(evidence);
        return this;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getComment() {
        return this.comment;
    }

    public SkillUpgradeRecord comment(String comment) {
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

    public SkillUpgradeRecord person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public SkillUpgradeRecord skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public SkillLevel getOldLevel() {
        return this.oldLevel;
    }

    public void setOldLevel(SkillLevel skillLevel) {
        this.oldLevel = skillLevel;
    }

    public SkillUpgradeRecord oldLevel(SkillLevel skillLevel) {
        this.setOldLevel(skillLevel);
        return this;
    }

    public SkillLevel getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(SkillLevel skillLevel) {
        this.newLevel = skillLevel;
    }

    public SkillUpgradeRecord newLevel(SkillLevel skillLevel) {
        this.setNewLevel(skillLevel);
        return this;
    }

    public Person getAssessor() {
        return this.assessor;
    }

    public void setAssessor(Person person) {
        this.assessor = person;
    }

    public SkillUpgradeRecord assessor(Person person) {
        this.setAssessor(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillUpgradeRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((SkillUpgradeRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillUpgradeRecord{" +
            "id=" + getId() +
            ", changeType='" + getChangeType() + "'" +
            ", changeDate='" + getChangeDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", beforeLevelLabel='" + getBeforeLevelLabel() + "'" +
            ", afterLevelLabel='" + getAfterLevelLabel() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
