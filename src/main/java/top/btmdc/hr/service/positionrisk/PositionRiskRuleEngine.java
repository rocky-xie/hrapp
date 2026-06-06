package top.btmdc.hr.service.positionrisk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties.PositionRiskRuleCondition;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties.PositionRiskRuleDefinition;

@Service
public class PositionRiskRuleEngine {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRiskRuleEngine.class);

    private final PositionRiskRuleProperties properties;

    public PositionRiskRuleEngine(PositionRiskRuleProperties properties) {
        this.properties = properties;
    }

    public PositionRiskDecision evaluate(PositionRiskInput input) {
        List<String> missingData = findMissingData(input);
        List<PositionRiskRuleDefinition> sortedRules = properties
            .getRules()
            .stream()
            .filter(PositionRiskRuleDefinition::isEnabled)
            .sorted(Comparator.comparingInt(PositionRiskRuleDefinition::getPriority))
            .toList();

        for (PositionRiskRuleDefinition rule : sortedRules) {
            if (matches(input, rule.getWhen())) {
                return buildDecision(rule, input, missingData);
            }
        }

        return buildUnknownDecision(missingData, input);
    }

    private boolean matches(PositionRiskInput input, PositionRiskRuleCondition condition) {
        if (condition.getOwnerCountEquals() != null && input.getOwnerCount() != condition.getOwnerCountEquals()) {
            return false;
        }
        if (condition.getOwnerCountLessThanMinimum() != null) {
            boolean lessThan = input.getOwnerCount() < input.getMinimumOwnerCount();
            if (condition.getOwnerCountLessThanMinimum() != lessThan) {
                return false;
            }
        }
        if (condition.getKeyPosition() != null && input.isKeyPosition() != condition.getKeyPosition()) {
            return false;
        }
        if (condition.getHasSubstitute() != null && input.isHasSubstitute() != condition.getHasSubstitute()) {
            return false;
        }
        if (condition.getDocumentStatusIn() != null && input.getDocumentStatus() != null) {
            Set<String> accepted = splitCsv(condition.getDocumentStatusIn());
            if (!accepted.contains(input.getDocumentStatus().name())) {
                return false;
            }
        } else if (condition.getDocumentStatusIn() != null && input.getDocumentStatus() == null) {
            return false;
        }
        if (condition.getDependencyIn() != null && input.getCustomerOrSystemDependency() != null) {
            Set<String> accepted = splitCsv(condition.getDependencyIn());
            if (!accepted.contains(input.getCustomerOrSystemDependency().name())) {
                return false;
            }
        } else if (condition.getDependencyIn() != null && input.getCustomerOrSystemDependency() == null) {
            return false;
        }
        if (condition.getReadinessIn() != null && input.getSuccessionReadiness() != null) {
            Set<String> accepted = splitCsv(condition.getReadinessIn());
            if (!accepted.contains(input.getSuccessionReadiness().name())) {
                return false;
            }
        } else if (condition.getReadinessIn() != null && input.getSuccessionReadiness() == null) {
            return false;
        }
        return true;
    }

    private Set<String> splitCsv(String value) {
        if (value == null || value.isBlank()) return Set.of();
        String[] parts = value.split(",");
        Set<String> result = new java.util.LinkedHashSet<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private List<String> findMissingData(PositionRiskInput input) {
        List<String> missing = new ArrayList<>();
        if (input.getDocumentStatus() == null) missing.add("documentStatus");
        if (input.getCustomerOrSystemDependency() == null) missing.add("customerOrSystemDependency");
        if (input.getSuccessionReadiness() == null) missing.add("successionReadiness");
        return missing;
    }

    private PositionRiskDecision buildDecision(PositionRiskRuleDefinition rule, PositionRiskInput input, List<String> missingData) {
        String reason = buildExplanation(rule, input, missingData);
        return new PositionRiskDecision(
            rule.getRiskLevel(),
            rule.getCode(),
            reason,
            rule.getRecommendedAction() != null ? rule.getRecommendedAction() : defaultAction(rule.getRiskLevel()),
            rule.getContributingFactors(),
            missingData
        );
    }

    private PositionRiskDecision buildUnknownDecision(List<String> missingData, PositionRiskInput input) {
        String unknownReason;
        String action;
        if (!missingData.isEmpty()) {
            unknownReason =
                "未匹配任何风险规则，原因：缺少关键数据 [" + String.join(", ", missingData) + "]，无法完整评估。请补充后再评价。";
            action = "请补充缺失数据后重新评价。";
        } else {
            unknownReason = "未匹配任何风险规则，当前输入条件不满足已知风险规则，建议人工审核。";
            action = "联系管理员确认是否需要补充规则配置。";
        }
        return new PositionRiskDecision(RiskLevel.UNKNOWN, null, unknownReason, action, List.of(), missingData);
    }

    private String buildExplanation(PositionRiskRuleDefinition rule, PositionRiskInput input, List<String> missingData) {
        StringBuilder sb = new StringBuilder();
        sb.append("命中规则：").append(rule.getCode()).append("\n");
        sb.append("判定结果：").append(rule.getRiskLevel()).append("\n");
        sb.append("主要因素：\n");
        for (String factor : rule.getContributingFactors()) {
            sb.append("- ").append(factor).append("\n");
        }
        if (!missingData.isEmpty()) {
            sb.append("缺失数据：").append(String.join(", ", missingData)).append("\n");
        } else {
            sb.append("缺失数据：无\n");
        }
        sb.append("原因：").append(rule.getReason());
        return sb.toString();
    }

    private String defaultAction(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case HIGH -> "Immediately establish a second owner, minimum handover documentation, and a cultivation plan.";
            case MEDIUM -> "Arrange shadow learning, skill gap improvement, documentation cleanup, and periodic review.";
            case LOW -> "Review periodically to avoid stale skill, document, and substitution data.";
            case UNKNOWN -> "Collect more assignment, document, dependency, and successor readiness evidence before evaluation.";
        };
    }
}
