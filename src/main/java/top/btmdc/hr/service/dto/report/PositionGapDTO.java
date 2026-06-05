package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.util.List;
import top.btmdc.hr.domain.enumeration.RiskLevel;

public class PositionGapDTO implements Serializable {

    private Long positionId;
    private String positionName;
    private RiskLevel riskLevel;
    private int totalRequiredSkills;
    private List<PersonGapDTO> owners;
    private List<PersonGapDTO> candidates;
    private List<AggregatedGapDTO> aggregatedGaps;

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getTotalRequiredSkills() {
        return totalRequiredSkills;
    }

    public void setTotalRequiredSkills(int totalRequiredSkills) {
        this.totalRequiredSkills = totalRequiredSkills;
    }

    public List<PersonGapDTO> getOwners() {
        return owners;
    }

    public void setOwners(List<PersonGapDTO> owners) {
        this.owners = owners;
    }

    public List<PersonGapDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<PersonGapDTO> candidates) {
        this.candidates = candidates;
    }

    public List<AggregatedGapDTO> getAggregatedGaps() {
        return aggregatedGaps;
    }

    public void setAggregatedGaps(List<AggregatedGapDTO> aggregatedGaps) {
        this.aggregatedGaps = aggregatedGaps;
    }
}
