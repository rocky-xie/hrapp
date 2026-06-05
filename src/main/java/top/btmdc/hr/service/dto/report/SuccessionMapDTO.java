package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.util.List;
import top.btmdc.hr.domain.enumeration.RiskLevel;

public class SuccessionMapDTO implements Serializable {

    private Long positionId;
    private String positionName;
    private RiskLevel riskLevel;
    private String currentOwnerName;
    private List<SuccessionMapCandidateDTO> candidates;
    private int totalCandidates;

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

    public String getCurrentOwnerName() {
        return currentOwnerName;
    }

    public void setCurrentOwnerName(String currentOwnerName) {
        this.currentOwnerName = currentOwnerName;
    }

    public List<SuccessionMapCandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<SuccessionMapCandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public int getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(int totalCandidates) {
        this.totalCandidates = totalCandidates;
    }
}
