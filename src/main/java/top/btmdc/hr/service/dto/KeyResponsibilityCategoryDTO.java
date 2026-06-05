package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.KeyResponsibilityCategory} entity.
 */
@Schema(description = "关键职责分类。岗位风险中按类型归类的职责类别。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KeyResponsibilityCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String categoryName;

    @Lob
    private String examples;

    @Lob
    private String riskFocus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getRiskFocus() {
        return riskFocus;
    }

    public void setRiskFocus(String riskFocus) {
        this.riskFocus = riskFocus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyResponsibilityCategoryDTO)) {
            return false;
        }

        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO = (KeyResponsibilityCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, keyResponsibilityCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KeyResponsibilityCategoryDTO{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", examples='" + getExamples() + "'" +
            ", riskFocus='" + getRiskFocus() + "'" +
            "}";
    }
}
