package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PersonGapDTO implements Serializable {

    private Long personId;
    private String personName;
    private Integer totalRequired;
    private Integer coveredCount;
    private BigDecimal coverageRate;
    private List<SkillGapDTO> gaps;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getTotalRequired() {
        return totalRequired;
    }

    public void setTotalRequired(Integer totalRequired) {
        this.totalRequired = totalRequired;
    }

    public Integer getCoveredCount() {
        return coveredCount;
    }

    public void setCoveredCount(Integer coveredCount) {
        this.coveredCount = coveredCount;
    }

    public BigDecimal getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(BigDecimal coverageRate) {
        this.coverageRate = coverageRate;
    }

    public List<SkillGapDTO> getGaps() {
        return gaps;
    }

    public void setGaps(List<SkillGapDTO> gaps) {
        this.gaps = gaps;
    }
}
