package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.EvidenceType;
import top.btmdc.hr.domain.enumeration.SkillType;

/**
 * A DTO for the {@link top.btmdc.hr.domain.Skill} entity.
 */
@Schema(description = "技能定义。可包含证书、技术、业务等类型。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String skillCode;

    @NotNull
    @Size(max = 100)
    private String skillName;

    @NotNull
    private SkillType skillType;

    @NotNull
    private Boolean measurableFlag;

    @Lob
    private String description;

    private EvidenceType evidenceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public Boolean getMeasurableFlag() {
        return measurableFlag;
    }

    public void setMeasurableFlag(Boolean measurableFlag) {
        this.measurableFlag = measurableFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EvidenceType getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(EvidenceType evidenceType) {
        this.evidenceType = evidenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDTO)) {
            return false;
        }

        SkillDTO skillDTO = (SkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, skillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillDTO{" +
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
