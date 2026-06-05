package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionRiskEvaluationCriteriaTest {

    @Test
    void newPositionRiskEvaluationCriteriaHasAllFiltersNullTest() {
        var positionRiskEvaluationCriteria = new PositionRiskEvaluationCriteria();
        assertThat(positionRiskEvaluationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionRiskEvaluationCriteriaFluentMethodsCreatesFiltersTest() {
        var positionRiskEvaluationCriteria = new PositionRiskEvaluationCriteria();

        setAllFilters(positionRiskEvaluationCriteria);

        assertThat(positionRiskEvaluationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionRiskEvaluationCriteriaCopyCreatesNullFilterTest() {
        var positionRiskEvaluationCriteria = new PositionRiskEvaluationCriteria();
        var copy = positionRiskEvaluationCriteria.copy();

        assertThat(positionRiskEvaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionRiskEvaluationCriteria)
        );
    }

    @Test
    void positionRiskEvaluationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionRiskEvaluationCriteria = new PositionRiskEvaluationCriteria();
        setAllFilters(positionRiskEvaluationCriteria);

        var copy = positionRiskEvaluationCriteria.copy();

        assertThat(positionRiskEvaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionRiskEvaluationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionRiskEvaluationCriteria = new PositionRiskEvaluationCriteria();

        assertThat(positionRiskEvaluationCriteria).hasToString("PositionRiskEvaluationCriteria{}");
    }

    private static void setAllFilters(PositionRiskEvaluationCriteria positionRiskEvaluationCriteria) {
        positionRiskEvaluationCriteria.id();
        positionRiskEvaluationCriteria.evaluationDate();
        positionRiskEvaluationCriteria.ownerCount();
        positionRiskEvaluationCriteria.substitutableOwnerCount();
        positionRiskEvaluationCriteria.hasSubstitute();
        positionRiskEvaluationCriteria.documentStatus();
        positionRiskEvaluationCriteria.customerOrSystemDependency();
        positionRiskEvaluationCriteria.successionReadiness();
        positionRiskEvaluationCriteria.riskLevel();
        positionRiskEvaluationCriteria.positionId();
        positionRiskEvaluationCriteria.distinct();
    }

    private static Condition<PositionRiskEvaluationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEvaluationDate()) &&
                condition.apply(criteria.getOwnerCount()) &&
                condition.apply(criteria.getSubstitutableOwnerCount()) &&
                condition.apply(criteria.getHasSubstitute()) &&
                condition.apply(criteria.getDocumentStatus()) &&
                condition.apply(criteria.getCustomerOrSystemDependency()) &&
                condition.apply(criteria.getSuccessionReadiness()) &&
                condition.apply(criteria.getRiskLevel()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionRiskEvaluationCriteria> copyFiltersAre(
        PositionRiskEvaluationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEvaluationDate(), copy.getEvaluationDate()) &&
                condition.apply(criteria.getOwnerCount(), copy.getOwnerCount()) &&
                condition.apply(criteria.getSubstitutableOwnerCount(), copy.getSubstitutableOwnerCount()) &&
                condition.apply(criteria.getHasSubstitute(), copy.getHasSubstitute()) &&
                condition.apply(criteria.getDocumentStatus(), copy.getDocumentStatus()) &&
                condition.apply(criteria.getCustomerOrSystemDependency(), copy.getCustomerOrSystemDependency()) &&
                condition.apply(criteria.getSuccessionReadiness(), copy.getSuccessionReadiness()) &&
                condition.apply(criteria.getRiskLevel(), copy.getRiskLevel()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
