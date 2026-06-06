package top.btmdc.hr.service.positionrisk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import top.btmdc.hr.domain.enumeration.RiskLevel;

public class PositionRiskDecision {

    private final RiskLevel riskLevel;
    private final String matchedRuleCode;
    private final String riskReason;
    private final String recommendedAction;
    private final List<String> contributingFactors;
    private final List<String> missingData;

    public PositionRiskDecision(
        RiskLevel riskLevel,
        String matchedRuleCode,
        String riskReason,
        String recommendedAction,
        List<String> contributingFactors,
        List<String> missingData
    ) {
        this.riskLevel = riskLevel;
        this.matchedRuleCode = matchedRuleCode;
        this.riskReason = riskReason;
        this.recommendedAction = recommendedAction;
        this.contributingFactors =
            contributingFactors != null ? Collections.unmodifiableList(new ArrayList<>(contributingFactors)) : List.of();
        this.missingData = missingData != null ? Collections.unmodifiableList(new ArrayList<>(missingData)) : List.of();
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getMatchedRuleCode() {
        return matchedRuleCode;
    }

    public String getRiskReason() {
        return riskReason;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public List<String> getContributingFactors() {
        return contributingFactors;
    }

    public List<String> getMissingData() {
        return missingData;
    }
}
