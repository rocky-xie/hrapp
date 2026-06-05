package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EvaluationCriteriaTest {

    @Test
    void newEvaluationCriteriaHasAllFiltersNullTest() {
        var evaluationCriteria = new EvaluationCriteria();
        assertThat(evaluationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void evaluationCriteriaFluentMethodsCreatesFiltersTest() {
        var evaluationCriteria = new EvaluationCriteria();

        setAllFilters(evaluationCriteria);

        assertThat(evaluationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void evaluationCriteriaCopyCreatesNullFilterTest() {
        var evaluationCriteria = new EvaluationCriteria();
        var copy = evaluationCriteria.copy();

        assertThat(evaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationCriteria)
        );
    }

    @Test
    void evaluationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var evaluationCriteria = new EvaluationCriteria();
        setAllFilters(evaluationCriteria);

        var copy = evaluationCriteria.copy();

        assertThat(evaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var evaluationCriteria = new EvaluationCriteria();

        assertThat(evaluationCriteria).hasToString("EvaluationCriteria{}");
    }

    private static void setAllFilters(EvaluationCriteria evaluationCriteria) {
        evaluationCriteria.id();
        evaluationCriteria.evaluationName();
        evaluationCriteria.evaluationDate();
        evaluationCriteria.periodLabel();
        evaluationCriteria.progressStatus();
        evaluationCriteria.result();
        evaluationCriteria.positionAdjustmentNeeded();
        evaluationCriteria.personId();
        evaluationCriteria.positionId();
        evaluationCriteria.trainingGoalId();
        evaluationCriteria.evaluatorId();
        evaluationCriteria.distinct();
    }

    private static Condition<EvaluationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEvaluationName()) &&
                condition.apply(criteria.getEvaluationDate()) &&
                condition.apply(criteria.getPeriodLabel()) &&
                condition.apply(criteria.getProgressStatus()) &&
                condition.apply(criteria.getResult()) &&
                condition.apply(criteria.getPositionAdjustmentNeeded()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getTrainingGoalId()) &&
                condition.apply(criteria.getEvaluatorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EvaluationCriteria> copyFiltersAre(EvaluationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEvaluationName(), copy.getEvaluationName()) &&
                condition.apply(criteria.getEvaluationDate(), copy.getEvaluationDate()) &&
                condition.apply(criteria.getPeriodLabel(), copy.getPeriodLabel()) &&
                condition.apply(criteria.getProgressStatus(), copy.getProgressStatus()) &&
                condition.apply(criteria.getResult(), copy.getResult()) &&
                condition.apply(criteria.getPositionAdjustmentNeeded(), copy.getPositionAdjustmentNeeded()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getTrainingGoalId(), copy.getTrainingGoalId()) &&
                condition.apply(criteria.getEvaluatorId(), copy.getEvaluatorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
