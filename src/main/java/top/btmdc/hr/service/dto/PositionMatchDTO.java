package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.Recommendation;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PositionMatch} entity.
 */
@Schema(description = "人岗匹配。分析某人是否适任某个职位的评估记录。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionMatchDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    private Integer matchScore;

    @Lob
    private String matchedSkills;

    @Lob
    private String gapSkills;

    @NotNull
    private ReadinessLevel readiness;

    @NotNull
    private Recommendation recommendation;

    @NotNull
    private LocalDate analysisDate;

    @Lob
    private String remark;

    @NotNull
    private PersonDTO person;

    @NotNull
    private PositionDTO position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(String matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public String getGapSkills() {
        return gapSkills;
    }

    public void setGapSkills(String gapSkills) {
        this.gapSkills = gapSkills;
    }

    public ReadinessLevel getReadiness() {
        return readiness;
    }

    public void setReadiness(ReadinessLevel readiness) {
        this.readiness = readiness;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDate getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDate analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionMatchDTO)) {
            return false;
        }

        PositionMatchDTO positionMatchDTO = (PositionMatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionMatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionMatchDTO{" +
            "id=" + getId() +
            ", matchScore=" + getMatchScore() +
            ", matchedSkills='" + getMatchedSkills() + "'" +
            ", gapSkills='" + getGapSkills() + "'" +
            ", readiness='" + getReadiness() + "'" +
            ", recommendation='" + getRecommendation() + "'" +
            ", analysisDate='" + getAnalysisDate() + "'" +
            ", remark='" + getRemark() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            "}";
    }
}
