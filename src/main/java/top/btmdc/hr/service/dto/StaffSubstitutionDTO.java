package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.StaffSubstitution} entity.
 */
@Schema(description = "职位替代关系。某个候选人员对指定职位的替代能力评定结果。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaffSubstitutionDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal coverageRate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal thresholdRate;

    @Min(value = 0)
    private Integer totalSkillCount;

    @Min(value = 0)
    private Integer coveredSkillCount;

    @Lob
    private String missingSkills;

    @NotNull
    private Boolean substitutable;

    @NotNull
    private LocalDate evaluationDate;

    @Lob
    private String reason;

    private LocalDate reviewDate;

    private LocalDate expiryDate;

    @NotNull
    private PositionDTO position;

    @NotNull
    private PersonDTO candidatePerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(BigDecimal coverageRate) {
        this.coverageRate = coverageRate;
    }

    public BigDecimal getThresholdRate() {
        return thresholdRate;
    }

    public void setThresholdRate(BigDecimal thresholdRate) {
        this.thresholdRate = thresholdRate;
    }

    public Integer getTotalSkillCount() {
        return totalSkillCount;
    }

    public void setTotalSkillCount(Integer totalSkillCount) {
        this.totalSkillCount = totalSkillCount;
    }

    public Integer getCoveredSkillCount() {
        return coveredSkillCount;
    }

    public void setCoveredSkillCount(Integer coveredSkillCount) {
        this.coveredSkillCount = coveredSkillCount;
    }

    public String getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }

    public Boolean getSubstitutable() {
        return substitutable;
    }

    public void setSubstitutable(Boolean substitutable) {
        this.substitutable = substitutable;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public PersonDTO getCandidatePerson() {
        return candidatePerson;
    }

    public void setCandidatePerson(PersonDTO candidatePerson) {
        this.candidatePerson = candidatePerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffSubstitutionDTO)) {
            return false;
        }

        StaffSubstitutionDTO staffSubstitutionDTO = (StaffSubstitutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffSubstitutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffSubstitutionDTO{" +
            "id=" + getId() +
            ", coverageRate=" + getCoverageRate() +
            ", thresholdRate=" + getThresholdRate() +
            ", totalSkillCount=" + getTotalSkillCount() +
            ", coveredSkillCount=" + getCoveredSkillCount() +
            ", missingSkills='" + getMissingSkills() + "'" +
            ", substitutable='" + getSubstitutable() + "'" +
            ", evaluationDate='" + getEvaluationDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", position=" + getPosition() +
            ", candidatePerson=" + getCandidatePerson() +
            "}";
    }
}
