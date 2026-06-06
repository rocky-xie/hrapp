package top.btmdc.hr.service.positionrisk;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.btmdc.hr.domain.enumeration.RiskLevel;

@Component
@ConfigurationProperties(prefix = "hrapp.position-risk", ignoreUnknownFields = false)
public class PositionRiskRuleProperties {

    private List<PositionRiskRuleDefinition> rules = new ArrayList<>();

    public List<PositionRiskRuleDefinition> getRules() {
        return rules;
    }

    public void setRules(List<PositionRiskRuleDefinition> rules) {
        this.rules = rules;
    }

    public static class PositionRiskRuleDefinition {

        private String code;
        private boolean enabled = true;
        private int priority;
        private RiskLevel riskLevel;
        private PositionRiskRuleCondition when = new PositionRiskRuleCondition();
        private String reason;
        private List<String> contributingFactors = new ArrayList<>();
        private String recommendedAction;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public RiskLevel getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
        }

        public PositionRiskRuleCondition getWhen() {
            return when;
        }

        public void setWhen(PositionRiskRuleCondition when) {
            this.when = when;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<String> getContributingFactors() {
            return contributingFactors;
        }

        public void setContributingFactors(List<String> contributingFactors) {
            this.contributingFactors = contributingFactors;
        }

        public String getRecommendedAction() {
            return recommendedAction;
        }

        public void setRecommendedAction(String recommendedAction) {
            this.recommendedAction = recommendedAction;
        }
    }

    public static class PositionRiskRuleCondition {

        private Integer ownerCountEquals;
        private Boolean ownerCountLessThanMinimum;
        private Boolean keyPosition;
        private Boolean hasSubstitute;
        private String documentStatusIn;
        private String dependencyIn;
        private String readinessIn;

        public Integer getOwnerCountEquals() {
            return ownerCountEquals;
        }

        public void setOwnerCountEquals(Integer ownerCountEquals) {
            this.ownerCountEquals = ownerCountEquals;
        }

        public Boolean getOwnerCountLessThanMinimum() {
            return ownerCountLessThanMinimum;
        }

        public void setOwnerCountLessThanMinimum(Boolean ownerCountLessThanMinimum) {
            this.ownerCountLessThanMinimum = ownerCountLessThanMinimum;
        }

        public Boolean getKeyPosition() {
            return keyPosition;
        }

        public void setKeyPosition(Boolean keyPosition) {
            this.keyPosition = keyPosition;
        }

        public Boolean getHasSubstitute() {
            return hasSubstitute;
        }

        public void setHasSubstitute(Boolean hasSubstitute) {
            this.hasSubstitute = hasSubstitute;
        }

        public String getDocumentStatusIn() {
            return documentStatusIn;
        }

        public void setDocumentStatusIn(String documentStatusIn) {
            this.documentStatusIn = documentStatusIn;
        }

        public String getDependencyIn() {
            return dependencyIn;
        }

        public void setDependencyIn(String dependencyIn) {
            this.dependencyIn = dependencyIn;
        }

        public String getReadinessIn() {
            return readinessIn;
        }

        public void setReadinessIn(String readinessIn) {
            this.readinessIn = readinessIn;
        }
    }
}
