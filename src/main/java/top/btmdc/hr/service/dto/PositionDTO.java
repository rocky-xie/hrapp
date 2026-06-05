package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.PositionType;
import top.btmdc.hr.domain.enumeration.ReviewCycle;

/**
 * A DTO for the {@link top.btmdc.hr.domain.Position} entity.
 */
@Schema(description = "职位 / 关键职责。组织架构中的基本单元。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String positionCode;

    @NotNull
    @Size(max = 100)
    private String positionName;

    @NotNull
    private PositionType positionType;

    @NotNull
    private ImportanceLevel businessImportance;

    @NotNull
    private Boolean keyPosition;

    @Lob
    private String description;

    @Min(value = 0)
    private Integer plannedHeadcount;

    @Min(value = 0)
    private Integer minimumOwnerCount;

    private ReviewCycle reviewCycle;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public ImportanceLevel getBusinessImportance() {
        return businessImportance;
    }

    public void setBusinessImportance(ImportanceLevel businessImportance) {
        this.businessImportance = businessImportance;
    }

    public Boolean getKeyPosition() {
        return keyPosition;
    }

    public void setKeyPosition(Boolean keyPosition) {
        this.keyPosition = keyPosition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlannedHeadcount() {
        return plannedHeadcount;
    }

    public void setPlannedHeadcount(Integer plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public Integer getMinimumOwnerCount() {
        return minimumOwnerCount;
    }

    public void setMinimumOwnerCount(Integer minimumOwnerCount) {
        this.minimumOwnerCount = minimumOwnerCount;
    }

    public ReviewCycle getReviewCycle() {
        return reviewCycle;
    }

    public void setReviewCycle(ReviewCycle reviewCycle) {
        this.reviewCycle = reviewCycle;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionDTO)) {
            return false;
        }

        PositionDTO positionDTO = (PositionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionDTO{" +
            "id=" + getId() +
            ", positionCode='" + getPositionCode() + "'" +
            ", positionName='" + getPositionName() + "'" +
            ", positionType='" + getPositionType() + "'" +
            ", businessImportance='" + getBusinessImportance() + "'" +
            ", keyPosition='" + getKeyPosition() + "'" +
            ", description='" + getDescription() + "'" +
            ", plannedHeadcount=" + getPlannedHeadcount() +
            ", minimumOwnerCount=" + getMinimumOwnerCount() +
            ", reviewCycle='" + getReviewCycle() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
