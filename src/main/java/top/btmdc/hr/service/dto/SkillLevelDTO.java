package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.LevelCode;

/**
 * A DTO for the {@link top.btmdc.hr.domain.SkillLevel} entity.
 */
@Schema(description = "技能等级。统一使用 L0–L4 五级体系。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillLevelDTO implements Serializable {

    private Long id;

    @NotNull
    private LevelCode code;

    @NotNull
    @Size(max = 100)
    private String levelName;

    @Lob
    private String definition;

    @Lob
    private String observableEvidence;

    @NotNull
    @Min(value = 0)
    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LevelCode getCode() {
        return code;
    }

    public void setCode(LevelCode code) {
        this.code = code;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getObservableEvidence() {
        return observableEvidence;
    }

    public void setObservableEvidence(String observableEvidence) {
        this.observableEvidence = observableEvidence;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillLevelDTO)) {
            return false;
        }

        SkillLevelDTO skillLevelDTO = (SkillLevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, skillLevelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillLevelDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", levelName='" + getLevelName() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", observableEvidence='" + getObservableEvidence() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
