package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import top.btmdc.hr.domain.enumeration.AssessmentResult;

/**
 * A DTO for the {@link top.btmdc.hr.domain.SkillAssessment} entity.
 */
@Schema(description = "技能评估。对某个人某项技能的阶段评估结果。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillAssessmentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate assessmentDate;

    @NotNull
    private AssessmentResult result;

    @Lob
    private String evidence;

    @Lob
    private String comment;

    private PersonDTO person;

    private SkillDTO skill;

    private PersonDTO assessor;

    private SkillLevelDTO newLevel;

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

    public AssessmentResult getResult() {
        return result;
    }

    public void setResult(AssessmentResult result) {
        this.result = result;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public PersonDTO getAssessor() {
        return assessor;
    }

    public void setAssessor(PersonDTO assessor) {
        this.assessor = assessor;
    }

    public SkillLevelDTO getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(SkillLevelDTO newLevel) {
        this.newLevel = newLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillAssessmentDTO)) {
            return false;
        }

        SkillAssessmentDTO skillAssessmentDTO = (SkillAssessmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, skillAssessmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillAssessmentDTO{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", result='" + getResult() + "'" +
            ", evidence='" + getEvidence() + "'" +
            ", comment='" + getComment() + "'" +
            ", person=" + getPerson() +
            ", skill=" + getSkill() +
            ", assessor=" + getAssessor() +
            ", newLevel=" + getNewLevel() +
            "}";
    }
}
