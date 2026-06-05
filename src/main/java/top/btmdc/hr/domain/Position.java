package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.PositionType;
import top.btmdc.hr.domain.enumeration.ReviewCycle;

/**
 * 职位 / 关键职责。组织架构中的基本单元。
 */
@Entity
@Table(name = "position")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Position implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "position_code", length = 50, nullable = false, unique = true)
    private String positionCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "position_name", length = 100, nullable = false)
    private String positionName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", nullable = false)
    private PositionType positionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "business_importance", nullable = false)
    private ImportanceLevel businessImportance;

    @NotNull
    @Column(name = "key_position", nullable = false)
    private Boolean keyPosition;

    @Lob
    @Column(name = "description")
    private String description;

    @Min(value = 0)
    @Column(name = "planned_headcount")
    private Integer plannedHeadcount;

    @Min(value = 0)
    @Column(name = "minimum_owner_count")
    private Integer minimumOwnerCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_cycle")
    private ReviewCycle reviewCycle;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Position id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositionCode() {
        return this.positionCode;
    }

    public Position positionCode(String positionCode) {
        this.setPositionCode(positionCode);
        return this;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public Position positionName(String positionName) {
        this.setPositionName(positionName);
        return this;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public PositionType getPositionType() {
        return this.positionType;
    }

    public Position positionType(PositionType positionType) {
        this.setPositionType(positionType);
        return this;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public ImportanceLevel getBusinessImportance() {
        return this.businessImportance;
    }

    public Position businessImportance(ImportanceLevel businessImportance) {
        this.setBusinessImportance(businessImportance);
        return this;
    }

    public void setBusinessImportance(ImportanceLevel businessImportance) {
        this.businessImportance = businessImportance;
    }

    public Boolean getKeyPosition() {
        return this.keyPosition;
    }

    public Position keyPosition(Boolean keyPosition) {
        this.setKeyPosition(keyPosition);
        return this;
    }

    public void setKeyPosition(Boolean keyPosition) {
        this.keyPosition = keyPosition;
    }

    public String getDescription() {
        return this.description;
    }

    public Position description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlannedHeadcount() {
        return this.plannedHeadcount;
    }

    public Position plannedHeadcount(Integer plannedHeadcount) {
        this.setPlannedHeadcount(plannedHeadcount);
        return this;
    }

    public void setPlannedHeadcount(Integer plannedHeadcount) {
        this.plannedHeadcount = plannedHeadcount;
    }

    public Integer getMinimumOwnerCount() {
        return this.minimumOwnerCount;
    }

    public Position minimumOwnerCount(Integer minimumOwnerCount) {
        this.setMinimumOwnerCount(minimumOwnerCount);
        return this;
    }

    public void setMinimumOwnerCount(Integer minimumOwnerCount) {
        this.minimumOwnerCount = minimumOwnerCount;
    }

    public ReviewCycle getReviewCycle() {
        return this.reviewCycle;
    }

    public Position reviewCycle(ReviewCycle reviewCycle) {
        this.setReviewCycle(reviewCycle);
        return this;
    }

    public void setReviewCycle(ReviewCycle reviewCycle) {
        this.reviewCycle = reviewCycle;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Position active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return getId() != null && getId().equals(((Position) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Position{" +
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
