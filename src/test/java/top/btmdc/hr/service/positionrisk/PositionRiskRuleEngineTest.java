package top.btmdc.hr.service.positionrisk;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties.PositionRiskRuleCondition;
import top.btmdc.hr.service.positionrisk.PositionRiskRuleProperties.PositionRiskRuleDefinition;

class PositionRiskRuleEngineTest {

    private PositionRiskRuleEngine engine;

    @BeforeEach
    void setUp() {
        engine = new PositionRiskRuleEngine(createDefaultRules());
    }

    @Test
    void noActiveOwnerReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            0,
            1,
            0,
            false,
            DocumentStatus.MISSING,
            ImportanceLevel.HIGH,
            ReadinessLevel.NONE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("NO_ACTIVE_OWNER");
        assertThat(decision.getRiskReason()).contains("当前职位没有有效任职人员");
    }

    @Test
    void keyPositionUnderstaffedReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            true,
            1,
            2,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("KEY_POSITION_UNDERSTAFFED");
    }

    @Test
    void keyPositionWithoutSubstituteReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            true,
            2,
            1,
            0,
            false,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("KEY_POSITION_WITHOUT_SUBSTITUTE");
    }

    @Test
    void documentMissingWithHighDependencyReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.MISSING,
            ImportanceLevel.HIGH,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("DOCUMENT_MISSING_WITH_HIGH_DEPENDENCY");
    }

    @Test
    void documentOutdatedWithHighDependencyReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.OUTDATED,
            ImportanceLevel.HIGH,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("DOCUMENT_MISSING_WITH_HIGH_DEPENDENCY");
    }

    @Test
    void noReadinessWithHighDependencyReturnsHigh() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.HIGH,
            ReadinessLevel.NONE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("NO_READINESS_WITH_HIGH_DEPENDENCY");
    }

    @Test
    void nonKeyUnderstaffedReturnsMedium() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            1,
            2,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("NON_KEY_UNDERSTAFFED");
    }

    @Test
    void withoutSubstituteReturnsMedium() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            0,
            false,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("WITHOUT_SUBSTITUTE");
    }

    @Test
    void readinessNotImmediateReturnsMedium() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.THREE_MONTHS
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("READINESS_NOT_IMMEDIATE");
    }

    @Test
    void partialDocumentReturnsMedium() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.PARTIAL,
            ImportanceLevel.LOW,
            ReadinessLevel.SIX_TO_TWELVE_MONTHS
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("READINESS_NOT_IMMEDIATE");
    }

    @Test
    void mediumDependencyReturnsMedium() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.MEDIUM,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("MEDIUM_DEPENDENCY");
    }

    @Test
    void stableConditionsReturnsLow() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            2,
            1,
            1,
            true,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.LOW);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("STABLE_CONDITIONS");
    }

    @Test
    void missingCriticalDataReturnsUnknown() {
        PositionRiskInput input = input(1L, "Test", false, 2, 1, 1, true, null, null, null);
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.UNKNOWN);
        assertThat(decision.getMatchedRuleCode()).isNull();
        assertThat(decision.getMissingData()).contains("documentStatus", "customerOrSystemDependency", "successionReadiness");
    }

    @Test
    void noRuleMatchedReturnsUnknown() {
        PositionRiskInput input = input(1L, "Test", false, 2, 0, 1, true, DocumentStatus.MISSING, ImportanceLevel.LOW, ReadinessLevel.NONE);
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.UNKNOWN);
        assertThat(decision.getMatchedRuleCode()).isNull();
    }

    @Test
    void disabledRuleDoesNotMatch() {
        PositionRiskRuleProperties props = new PositionRiskRuleProperties();
        PositionRiskRuleDefinition disabledRule = new PositionRiskRuleDefinition();
        disabledRule.setCode("DISABLED_HIGH");
        disabledRule.setEnabled(false);
        disabledRule.setPriority(10);
        disabledRule.setRiskLevel(RiskLevel.HIGH);
        PositionRiskRuleCondition condition = new PositionRiskRuleCondition();
        condition.setOwnerCountEquals(0);
        disabledRule.setWhen(condition);
        disabledRule.setReason("Disabled rule should not fire");
        props.setRules(List.of(disabledRule));

        PositionRiskRuleEngine localEngine = new PositionRiskRuleEngine(props);
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            0,
            1,
            0,
            false,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );

        PositionRiskDecision decision = localEngine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.UNKNOWN);
    }

    @Test
    void priorityDeterminesMatchOrder() {
        PositionRiskRuleProperties props = new PositionRiskRuleProperties();

        PositionRiskRuleDefinition lowPrio = new PositionRiskRuleDefinition();
        lowPrio.setCode("LOW_PRIORITY");
        lowPrio.setEnabled(true);
        lowPrio.setPriority(100);
        lowPrio.setRiskLevel(RiskLevel.LOW);
        PositionRiskRuleCondition lowCondition = new PositionRiskRuleCondition();
        lowCondition.setOwnerCountEquals(0);
        lowPrio.setWhen(lowCondition);
        lowPrio.setReason("Low priority");

        PositionRiskRuleDefinition highPrio = new PositionRiskRuleDefinition();
        highPrio.setCode("HIGH_PRIORITY");
        highPrio.setEnabled(true);
        highPrio.setPriority(10);
        highPrio.setRiskLevel(RiskLevel.HIGH);
        PositionRiskRuleCondition highCondition = new PositionRiskRuleCondition();
        highCondition.setOwnerCountEquals(0);
        highPrio.setWhen(highCondition);
        highPrio.setReason("High priority");
        highPrio.setContributingFactors(List.of("ownerCount=0"));

        props.setRules(List.of(lowPrio, highPrio));

        PositionRiskRuleEngine localEngine = new PositionRiskRuleEngine(props);
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            0,
            1,
            0,
            false,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );

        PositionRiskDecision decision = localEngine.evaluate(input);
        assertThat(decision.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(decision.getMatchedRuleCode()).isEqualTo("HIGH_PRIORITY");
    }

    @Test
    void contributingFactorsArePopulated() {
        PositionRiskInput input = input(
            1L,
            "Test",
            true,
            2,
            1,
            0,
            false,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getContributingFactors()).contains("keyPosition=true", "hasSubstitute=false");
    }

    @Test
    void riskReasonContainsRuleCodeAndExplanation() {
        PositionRiskInput input = input(
            1L,
            "Test",
            false,
            0,
            1,
            0,
            false,
            DocumentStatus.MISSING,
            ImportanceLevel.HIGH,
            ReadinessLevel.NONE
        );
        PositionRiskDecision decision = engine.evaluate(input);
        assertThat(decision.getRiskReason()).contains("命中规则：NO_ACTIVE_OWNER");
        assertThat(decision.getRiskReason()).contains("判定结果：HIGH");
        assertThat(decision.getRiskReason()).contains("原因：");
    }

    private PositionRiskInput input(
        Long positionId,
        String positionName,
        boolean keyPosition,
        int ownerCount,
        int minimumOwnerCount,
        int substitutableOwnerCount,
        boolean hasSubstitute,
        DocumentStatus documentStatus,
        ImportanceLevel dependency,
        ReadinessLevel readiness
    ) {
        return new PositionRiskInput(
            positionId,
            positionName,
            keyPosition,
            ownerCount,
            minimumOwnerCount,
            substitutableOwnerCount,
            hasSubstitute,
            documentStatus,
            dependency,
            readiness
        );
    }

    private PositionRiskRuleProperties createDefaultRules() {
        PositionRiskRuleProperties props = new PositionRiskRuleProperties();

        props.setRules(
            List.of(
                rule(
                    "NO_ACTIVE_OWNER",
                    10,
                    RiskLevel.HIGH,
                    cond(0, null, null, null, null, null, null),
                    "当前职位没有有效任职人员。",
                    List.of("ownerCount=0"),
                    "立即指定负责人，并补充岗位交接文档。"
                ),

                rule(
                    "KEY_POSITION_UNDERSTAFFED",
                    20,
                    RiskLevel.HIGH,
                    cond(null, true, true, null, null, null, null),
                    "关键职位的实际负责人数量未达到最低要求。",
                    List.of("keyPosition=true", "ownerCount < minimumOwnerCount"),
                    "尽快增配负责人，确保关键岗位人力充沛。"
                ),

                rule(
                    "KEY_POSITION_WITHOUT_SUBSTITUTE",
                    30,
                    RiskLevel.HIGH,
                    cond(null, null, true, false, null, null, null),
                    "关键职位缺少有效替代人员。",
                    List.of("keyPosition=true", "hasSubstitute=false"),
                    "安排候选替代人，制定影子学习和交接计划。"
                ),

                rule(
                    "DOCUMENT_MISSING_WITH_HIGH_DEPENDENCY",
                    40,
                    RiskLevel.HIGH,
                    cond(null, null, null, null, "MISSING,OUTDATED", "HIGH", null),
                    "文档缺失或过期，且业务依赖度高。",
                    List.of("documentStatus in (MISSING,OUTDATED)", "customerOrSystemDependency=HIGH"),
                    "更新文档，降低系统依赖度。"
                ),

                rule(
                    "NO_READINESS_WITH_HIGH_DEPENDENCY",
                    50,
                    RiskLevel.HIGH,
                    cond(null, null, null, null, null, "HIGH", "NONE"),
                    "无继任准备度，且业务依赖度高。",
                    List.of("successionReadiness=NONE", "customerOrSystemDependency=HIGH"),
                    "制定继任者培养计划，降低依赖风险。"
                ),

                rule(
                    "NON_KEY_UNDERSTAFFED",
                    60,
                    RiskLevel.MEDIUM,
                    cond(null, true, false, null, null, null, null),
                    "非关键职位负责人数量低于最低要求。",
                    List.of("keyPosition=false", "ownerCount < minimumOwnerCount"),
                    "评估是否需要增配负责人。"
                ),

                rule(
                    "WITHOUT_SUBSTITUTE",
                    70,
                    RiskLevel.MEDIUM,
                    cond(null, null, null, false, null, null, null),
                    "职位缺少有效替代人员。",
                    List.of("hasSubstitute=false"),
                    "安排候选替代人，降低岗位风险。"
                ),

                rule(
                    "READINESS_NOT_IMMEDIATE",
                    80,
                    RiskLevel.MEDIUM,
                    cond(null, null, null, null, null, null, "THREE_MONTHS,SIX_TO_TWELVE_MONTHS"),
                    "继任准备度不足（需3-12个月）。",
                    List.of("successionReadiness in (THREE_MONTHS,SIX_TO_TWELVE_MONTHS)"),
                    "加速继任者培养计划。"
                ),

                rule(
                    "PARTIAL_DOCUMENT",
                    89,
                    RiskLevel.MEDIUM,
                    cond(null, null, null, null, "PARTIAL", null, null),
                    "文档部分可用。",
                    List.of("documentStatus=PARTIAL"),
                    "完善文档，确保关键流程有据可查。"
                ),

                rule(
                    "MEDIUM_DEPENDENCY",
                    90,
                    RiskLevel.MEDIUM,
                    cond(null, null, null, null, null, "MEDIUM", null),
                    "业务依赖度为中等。",
                    List.of("customerOrSystemDependency=MEDIUM"),
                    "持续跟踪依赖变化，降低系统耦合。"
                ),

                rule(
                    "STABLE_CONDITIONS",
                    100,
                    RiskLevel.LOW,
                    cond(null, false, null, true, "AVAILABLE", null, null),
                    "负责人充足、有替代人员、文档齐全。",
                    List.of("ownerCount >= minimumOwnerCount", "hasSubstitute=true", "documentStatus=AVAILABLE"),
                    "定期复查，防止数据过期。"
                )
            )
        );

        return props;
    }

    private PositionRiskRuleCondition cond(
        Integer ownerCountEquals,
        Boolean ownerCountLessThanMinimum,
        Boolean keyPosition,
        Boolean hasSubstitute,
        String documentStatusIn,
        String dependencyIn,
        String readinessIn
    ) {
        PositionRiskRuleCondition c = new PositionRiskRuleCondition();
        c.setOwnerCountEquals(ownerCountEquals);
        c.setOwnerCountLessThanMinimum(ownerCountLessThanMinimum);
        c.setKeyPosition(keyPosition);
        c.setHasSubstitute(hasSubstitute);
        c.setDocumentStatusIn(documentStatusIn);
        c.setDependencyIn(dependencyIn);
        c.setReadinessIn(readinessIn);
        return c;
    }

    private PositionRiskRuleDefinition rule(
        String code,
        int priority,
        RiskLevel riskLevel,
        PositionRiskRuleCondition when,
        String reason,
        List<String> contributingFactors,
        String recommendedAction
    ) {
        PositionRiskRuleDefinition r = new PositionRiskRuleDefinition();
        r.setCode(code);
        r.setEnabled(true);
        r.setPriority(priority);
        r.setRiskLevel(riskLevel);
        r.setWhen(when);
        r.setReason(reason);
        r.setContributingFactors(contributingFactors);
        r.setRecommendedAction(recommendedAction);
        return r;
    }
}
