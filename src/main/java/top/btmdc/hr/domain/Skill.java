package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.EvidenceType;
import top.btmdc.hr.domain.enumeration.SkillType;

/**
 * 技能定义。可包含证书、技术、业务等类型。
 */
@Entity
@Table(name = "skill")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Skill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "skill_code", length = 50, nullable = false, unique = true)
    private String skillCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "skill_name", length = 100, nullable = false)
    private String skillName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false)
    private SkillType skillType;

    @NotNull
    @Column(name = "measurable_flag", nullable = false)
    private Boolean measurableFlag;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "evidence_type")
    private EvidenceType evidenceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Skill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillCode() {
        return this.skillCode;
    }

    public Skill skillCode(String skillCode) {
        this.setSkillCode(skillCode);
        return this;
    }

    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public Skill skillName(String skillName) {
        this.setSkillName(skillName);
        return this;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillType getSkillType() {
        return this.skillType;
    }

    public Skill skillType(SkillType skillType) {
        this.setSkillType(skillType);
        return this;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public Boolean getMeasurableFlag() {
        return this.measurableFlag;
    }

    public Skill measurableFlag(Boolean measurableFlag) {
        this.setMeasurableFlag(measurableFlag);
        return this;
    }

    public void setMeasurableFlag(Boolean measurableFlag) {
        this.measurableFlag = measurableFlag;
    }

    public String getDescription() {
        return this.description;
    }

    public Skill description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EvidenceType getEvidenceType() {
        return this.evidenceType;
    }

    public Skill evidenceType(EvidenceType evidenceType) {
        this.setEvidenceType(evidenceType);
        return this;
    }

    public void setEvidenceType(EvidenceType evidenceType) {
        this.evidenceType = evidenceType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return getId() != null && getId().equals(((Skill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", skillCode='" + getSkillCode() + "'" +
            ", skillName='" + getSkillName() + "'" +
            ", skillType='" + getSkillType() + "'" +
            ", measurableFlag='" + getMeasurableFlag() + "'" +
            ", description='" + getDescription() + "'" +
            ", evidenceType='" + getEvidenceType() + "'" +
            "}";
    }
}
