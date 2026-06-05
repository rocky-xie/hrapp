package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.LevelCode;

/**
 * 技能等级。统一使用 L0–L4 五级体系。
 */
@Entity
@Table(name = "skill_level")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillLevel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true)
    private LevelCode code;

    @NotNull
    @Size(max = 100)
    @Column(name = "level_name", length = 100, nullable = false)
    private String levelName;

    @Lob
    @Column(name = "definition")
    private String definition;

    @Lob
    @Column(name = "observable_evidence")
    private String observableEvidence;

    @NotNull
    @Min(value = 0)
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SkillLevel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LevelCode getCode() {
        return this.code;
    }

    public SkillLevel code(LevelCode code) {
        this.setCode(code);
        return this;
    }

    public void setCode(LevelCode code) {
        this.code = code;
    }

    public String getLevelName() {
        return this.levelName;
    }

    public SkillLevel levelName(String levelName) {
        this.setLevelName(levelName);
        return this;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDefinition() {
        return this.definition;
    }

    public SkillLevel definition(String definition) {
        this.setDefinition(definition);
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getObservableEvidence() {
        return this.observableEvidence;
    }

    public SkillLevel observableEvidence(String observableEvidence) {
        this.setObservableEvidence(observableEvidence);
        return this;
    }

    public void setObservableEvidence(String observableEvidence) {
        this.observableEvidence = observableEvidence;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public SkillLevel sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillLevel)) {
            return false;
        }
        return getId() != null && getId().equals(((SkillLevel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillLevel{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", levelName='" + getLevelName() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", observableEvidence='" + getObservableEvidence() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
