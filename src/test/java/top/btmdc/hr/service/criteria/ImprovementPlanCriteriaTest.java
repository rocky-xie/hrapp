package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImprovementPlanCriteriaTest {

    @Test
    void newImprovementPlanCriteriaHasAllFiltersNullTest() {
        var improvementPlanCriteria = new ImprovementPlanCriteria();
        assertThat(improvementPlanCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void improvementPlanCriteriaFluentMethodsCreatesFiltersTest() {
        var improvementPlanCriteria = new ImprovementPlanCriteria();

        setAllFilters(improvementPlanCriteria);

        assertThat(improvementPlanCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void improvementPlanCriteriaCopyCreatesNullFilterTest() {
        var improvementPlanCriteria = new ImprovementPlanCriteria();
        var copy = improvementPlanCriteria.copy();

        assertThat(improvementPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(improvementPlanCriteria)
        );
    }

    @Test
    void improvementPlanCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var improvementPlanCriteria = new ImprovementPlanCriteria();
        setAllFilters(improvementPlanCriteria);

        var copy = improvementPlanCriteria.copy();

        assertThat(improvementPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(improvementPlanCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var improvementPlanCriteria = new ImprovementPlanCriteria();

        assertThat(improvementPlanCriteria).hasToString("ImprovementPlanCriteria{}");
    }

    private static void setAllFilters(ImprovementPlanCriteria improvementPlanCriteria) {
        improvementPlanCriteria.id();
        improvementPlanCriteria.planName();
        improvementPlanCriteria.planStatus();
        improvementPlanCriteria.ownerName();
        improvementPlanCriteria.startDate();
        improvementPlanCriteria.targetDate();
        improvementPlanCriteria.completionDate();
        improvementPlanCriteria.positionId();
        improvementPlanCriteria.skillId();
        improvementPlanCriteria.distinct();
    }

    private static Condition<ImprovementPlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPlanName()) &&
                condition.apply(criteria.getPlanStatus()) &&
                condition.apply(criteria.getOwnerName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getTargetDate()) &&
                condition.apply(criteria.getCompletionDate()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImprovementPlanCriteria> copyFiltersAre(
        ImprovementPlanCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPlanName(), copy.getPlanName()) &&
                condition.apply(criteria.getPlanStatus(), copy.getPlanStatus()) &&
                condition.apply(criteria.getOwnerName(), copy.getOwnerName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getTargetDate(), copy.getTargetDate()) &&
                condition.apply(criteria.getCompletionDate(), copy.getCompletionDate()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
