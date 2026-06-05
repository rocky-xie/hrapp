package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.ConfidenceLevel;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PersonSkill} entity.
 */
@Schema(description = "人员技能关联。记录某人掌握某项技能及当前等级。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonSkillDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate assessmentDate;

    private LocalDate nextReviewDate;

    @Lob
    private String evidence;

    private ConfidenceLevel confidence;

    @Lob
    private String growthDirection;

    private PersonDTO verifiedBy;

    private LocalDate verifiedDate;

    @NotNull
    private PersonDTO person;

    @NotNull
    private SkillDTO skill;

    @NotNull
    private SkillLevelDTO currentLevel;

    private SkillLevelDTO previousLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public ConfidenceLevel getConfidence() {
        return confidence;
    }

    public void setConfidence(ConfidenceLevel confidence) {
        this.confidence = confidence;
    }

    public String getGrowthDirection() {
        return growthDirection;
    }

    public void setGrowthDirection(String growthDirection) {
        this.growthDirection = growthDirection;
    }

    public PersonDTO getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(PersonDTO verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public SkillDTO getSkill() {
        return skill;
    }

    public void setSkill(SkillDTO skill) {
        this.skill = skill;
    }

    public SkillLevelDTO getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(SkillLevelDTO currentLevel) {
        this.currentLevel = currentLevel;
    }

    public SkillLevelDTO getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(SkillLevelDTO previousLevel) {
        this.previousLevel = previousLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonSkillDTO)) {
            return false;
        }

        PersonSkillDTO personSkillDTO = (PersonSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonSkillDTO{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", nextReviewDate='" + getNextReviewDate() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", confidence='" + getConfidence() + "'" +
            ", growthDirection='" + getGrowthDirection() + "'" +
            ", verifiedDate='" + getVerifiedDate() + "'" +
            ", person=" + getPerson() +
            ", skill=" + getSkill() +
            ", currentLevel=" + getCurrentLevel() +
            ", previousLevel=" + getPreviousLevel() +
            "}";
    }
}
