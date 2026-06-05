package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 关键职责分类。岗位风险中按类型归类的职责类别。
 */
@Entity
@Table(name = "key_responsibility_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KeyResponsibilityCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "category_name", length = 100, nullable = false, unique = true)
    private String categoryName;

    @Lob
    @Column(name = "examples")
    private String examples;

    @Lob
    @Column(name = "risk_focus")
    private String riskFocus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KeyResponsibilityCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public KeyResponsibilityCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExamples() {
        return this.examples;
    }

    public KeyResponsibilityCategory examples(String examples) {
        this.setExamples(examples);
        return this;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getRiskFocus() {
        return this.riskFocus;
    }

    public KeyResponsibilityCategory riskFocus(String riskFocus) {
        this.setRiskFocus(riskFocus);
        return this;
    }

    public void setRiskFocus(String riskFocus) {
        this.riskFocus = riskFocus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyResponsibilityCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((KeyResponsibilityCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KeyResponsibilityCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", examples='" + getExamples() + "'" +
            ", riskFocus='" + getRiskFocus() + "'" +
            "}";
    }
}
