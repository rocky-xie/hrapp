package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

public class SuccessionMapCandidateDTO implements Serializable {

    private Long candidateId;
    private String candidateName;
    private ReadinessLevel readiness;
    private int priority;
    private RiskLevel riskAfterTraining;
    private BigDecimal coverageRate;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public ReadinessLevel getReadiness() {
        return readiness;
    }

    public void setReadiness(ReadinessLevel readiness) {
        this.readiness = readiness;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RiskLevel getRiskAfterTraining() {
        return riskAfterTraining;
    }

    public void setRiskAfterTraining(RiskLevel riskAfterTraining) {
        this.riskAfterTraining = riskAfterTraining;
    }

    public BigDecimal getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(BigDecimal coverageRate) {
        this.coverageRate = coverageRate;
    }
}
