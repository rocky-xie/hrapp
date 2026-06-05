package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.Recommendation;

/**
 * 人岗匹配。分析某人是否适任某个职位的评估记录。
 */
@Entity
@Table(name = "position_match")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionMatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "match_score")
    private Integer matchScore;

    @Lob
    @Column(name = "matched_skills")
    private String matchedSkills;

    @Lob
    @Column(name = "gap_skills")
    private String gapSkills;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "readiness", nullable = false)
    private ReadinessLevel readiness;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private Recommendation recommendation;

    @NotNull
    @Column(name = "analysis_date", nullable = false)
    private LocalDate analysisDate;

    @Lob
    @Column(name = "remark")
    private String remark;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionMatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatchScore() {
        return this.matchScore;
    }

    public PositionMatch matchScore(Integer matchScore) {
        this.setMatchScore(matchScore);
        return this;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchedSkills() {
        return this.matchedSkills;
    }

    public PositionMatch matchedSkills(String matchedSkills) {
        this.setMatchedSkills(matchedSkills);
        return this;
    }

    public void setMatchedSkills(String matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public String getGapSkills() {
        return this.gapSkills;
    }

    public PositionMatch gapSkills(String gapSkills) {
        this.setGapSkills(gapSkills);
        return this;
    }

    public void setGapSkills(String gapSkills) {
        this.gapSkills = gapSkills;
    }

    public ReadinessLevel getReadiness() {
        return this.readiness;
    }

    public PositionMatch readiness(ReadinessLevel readiness) {
        this.setReadiness(readiness);
        return this;
    }

    public void setReadiness(ReadinessLevel readiness) {
        this.readiness = readiness;
    }

    public Recommendation getRecommendation() {
        return this.recommendation;
    }

    public PositionMatch recommendation(Recommendation recommendation) {
        this.setRecommendation(recommendation);
        return this;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDate getAnalysisDate() {
        return this.analysisDate;
    }

    public PositionMatch analysisDate(LocalDate analysisDate) {
        this.setAnalysisDate(analysisDate);
        return this;
    }

    public void setAnalysisDate(LocalDate analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getRemark() {
        return this.remark;
    }

    public PositionMatch remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PositionMatch person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionMatch position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionMatch)) {
            return false;
        }
        return getId() != null && getId().equals(((PositionMatch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionMatch{" +
            "id=" + getId() +
            ", matchScore=" + getMatchScore() +
            ", matchedSkills='" + getMatchedSkills() + "'" +
            ", gapSkills='" + getGapSkills() + "'" +
            ", readiness='" + getReadiness() + "'" +
            ", recommendation='" + getRecommendation() + "'" +
            ", analysisDate='" + getAnalysisDate() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
