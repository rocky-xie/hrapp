package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 职位替代关系。某个候选人员对指定职位的替代能力评定结果。
 */
@Entity
@Table(name = "staff_substitution", uniqueConstraints = { @UniqueConstraint(columnNames = { "position_id", "candidate_person_id" }) })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaffSubstitution implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "coverage_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal coverageRate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "threshold_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal thresholdRate;

    @Min(value = 0)
    @Column(name = "total_skill_count")
    private Integer totalSkillCount;

    @Min(value = 0)
    @Column(name = "covered_skill_count")
    private Integer coveredSkillCount;

    @Lob
    @Column(name = "missing_skills")
    private String missingSkills;

    @NotNull
    @Column(name = "substitutable", nullable = false)
    private Boolean substitutable;

    @NotNull
    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Lob
    @Column(name = "reason")
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    @ManyToOne(optional = false)
    @NotNull
    private Person candidatePerson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StaffSubstitution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCoverageRate() {
        return this.coverageRate;
    }

    public StaffSubstitution coverageRate(BigDecimal coverageRate) {
        this.setCoverageRate(coverageRate);
        return this;
    }

    public void setCoverageRate(BigDecimal coverageRate) {
        this.coverageRate = coverageRate;
    }

    public BigDecimal getThresholdRate() {
        return this.thresholdRate;
    }

    public StaffSubstitution thresholdRate(BigDecimal thresholdRate) {
        this.setThresholdRate(thresholdRate);
        return this;
    }

    public void setThresholdRate(BigDecimal thresholdRate) {
        this.thresholdRate = thresholdRate;
    }

    public Integer getTotalSkillCount() {
        return this.totalSkillCount;
    }

    public StaffSubstitution totalSkillCount(Integer totalSkillCount) {
        this.setTotalSkillCount(totalSkillCount);
        return this;
    }

    public void setTotalSkillCount(Integer totalSkillCount) {
        this.totalSkillCount = totalSkillCount;
    }

    public Integer getCoveredSkillCount() {
        return this.coveredSkillCount;
    }

    public StaffSubstitution coveredSkillCount(Integer coveredSkillCount) {
        this.setCoveredSkillCount(coveredSkillCount);
        return this;
    }

    public void setCoveredSkillCount(Integer coveredSkillCount) {
        this.coveredSkillCount = coveredSkillCount;
    }

    public String getMissingSkills() {
        return this.missingSkills;
    }

    public StaffSubstitution missingSkills(String missingSkills) {
        this.setMissingSkills(missingSkills);
        return this;
    }

    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }

    public Boolean getSubstitutable() {
        return this.substitutable;
    }

    public StaffSubstitution substitutable(Boolean substitutable) {
        this.setSubstitutable(substitutable);
        return this;
    }

    public void setSubstitutable(Boolean substitutable) {
        this.substitutable = substitutable;
    }

    public LocalDate getEvaluationDate() {
        return this.evaluationDate;
    }

    public StaffSubstitution evaluationDate(LocalDate evaluationDate) {
        this.setEvaluationDate(evaluationDate);
        return this;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getReason() {
        return this.reason;
    }

    public StaffSubstitution reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public StaffSubstitution position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Person getCandidatePerson() {
        return this.candidatePerson;
    }

    public void setCandidatePerson(Person person) {
        this.candidatePerson = person;
    }

    public StaffSubstitution candidatePerson(Person person) {
        this.setCandidatePerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffSubstitution)) {
            return false;
        }
        return getId() != null && getId().equals(((StaffSubstitution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffSubstitution{" +
            "id=" + getId() +
            ", coverageRate=" + getCoverageRate() +
            ", thresholdRate=" + getThresholdRate() +
            ", totalSkillCount=" + getTotalSkillCount() +
            ", coveredSkillCount=" + getCoveredSkillCount() +
            ", missingSkills='" + getMissingSkills() + "'" +
            ", substitutable='" + getSubstitutable() + "'" +
            ", evaluationDate='" + getEvaluationDate() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
