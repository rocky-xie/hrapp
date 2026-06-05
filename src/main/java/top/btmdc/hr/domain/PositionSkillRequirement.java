package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

/**
 * 职位技能要求。描述某职位要求哪些技能及等级门槛。
 */
@Entity
@Table(name = "position_skill_requirement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionSkillRequirement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "importance", nullable = false)
    private RequirementImportance importance;

    @Lob
    @Column(name = "remark")
    private String remark;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    @ManyToOne(optional = false)
    @NotNull
    private Skill skill;

    @ManyToOne(optional = false)
    @NotNull
    private SkillLevel requiredLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillLevel preferredLevel;

    @Column(name = "certification_required")
    private Boolean certificationRequired;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionSkillRequirement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequirementImportance getImportance() {
        return this.importance;
    }

    public PositionSkillRequirement importance(RequirementImportance importance) {
        this.setImportance(importance);
        return this;
    }

    public void setImportance(RequirementImportance importance) {
        this.importance = importance;
    }

    public String getRemark() {
        return this.remark;
    }

    public PositionSkillRequirement remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionSkillRequirement position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public PositionSkillRequirement skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public SkillLevel getRequiredLevel() {
        return this.requiredLevel;
    }

    public void setRequiredLevel(SkillLevel skillLevel) {
        this.requiredLevel = skillLevel;
    }

    public PositionSkillRequirement requiredLevel(SkillLevel skillLevel) {
        this.setRequiredLevel(skillLevel);
        return this;
    }

    public SkillLevel getPreferredLevel() {
        return this.preferredLevel;
    }

    public void setPreferredLevel(SkillLevel skillLevel) {
        this.preferredLevel = skillLevel;
    }

    public PositionSkillRequirement preferredLevel(SkillLevel skillLevel) {
        this.setPreferredLevel(skillLevel);
        return this;
    }

    public Boolean getCertificationRequired() {
        return this.certificationRequired;
    }

    public void setCertificationRequired(Boolean certificationRequired) {
        this.certificationRequired = certificationRequired;
    }

    public PositionSkillRequirement certificationRequired(Boolean certificationRequired) {
        this.setCertificationRequired(certificationRequired);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionSkillRequirement)) {
            return false;
        }
        return getId() != null && getId().equals(((PositionSkillRequirement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionSkillRequirement{" +
            "id=" + getId() +
            ", importance='" + getImportance() + "'" +
            ", remark='" + getRemark() + "'" +
            ", certificationRequired='" + getCertificationRequired() + "'" +
            "}";
    }
}
