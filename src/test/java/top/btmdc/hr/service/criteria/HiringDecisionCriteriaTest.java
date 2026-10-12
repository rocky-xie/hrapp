package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HiringDecisionCriteriaTest {

    @Test
    void newHiringDecisionCriteriaHasAllFiltersNullTest() {
        var hiringDecisionCriteria = new HiringDecisionCriteria();
        assertThat(hiringDecisionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void hiringDecisionCriteriaFluentMethodsCreatesFiltersTest() {
        var hiringDecisionCriteria = new HiringDecisionCriteria();

        setAllFilters(hiringDecisionCriteria);

        assertThat(hiringDecisionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void hiringDecisionCriteriaCopyCreatesNullFilterTest() {
        var hiringDecisionCriteria = new HiringDecisionCriteria();
        var copy = hiringDecisionCriteria.copy();

        assertThat(hiringDecisionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(hiringDecisionCriteria)
        );
    }

    @Test
    void hiringDecisionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hiringDecisionCriteria = new HiringDecisionCriteria();
        setAllFilters(hiringDecisionCriteria);

        var copy = hiringDecisionCriteria.copy();

        assertThat(hiringDecisionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(hiringDecisionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hiringDecisionCriteria = new HiringDecisionCriteria();

        assertThat(hiringDecisionCriteria).hasToString("HiringDecisionCriteria{}");
    }

    private static void setAllFilters(HiringDecisionCriteria hiringDecisionCriteria) {
        hiringDecisionCriteria.id();
        hiringDecisionCriteria.decision();
        hiringDecisionCriteria.decisionSource();
        hiringDecisionCriteria.decidedBy();
        hiringDecisionCriteria.decidedAt();
        hiringDecisionCriteria.correctionOfId();
        hiringDecisionCriteria.active();
        hiringDecisionCriteria.applicationId();
        hiringDecisionCriteria.distinct();
    }

    private static Condition<HiringDecisionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDecision()) &&
                condition.apply(criteria.getDecisionSource()) &&
                condition.apply(criteria.getDecidedBy()) &&
                condition.apply(criteria.getDecidedAt()) &&
                condition.apply(criteria.getCorrectionOfId()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HiringDecisionCriteria> copyFiltersAre(
        HiringDecisionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDecision(), copy.getDecision()) &&
                condition.apply(criteria.getDecisionSource(), copy.getDecisionSource()) &&
                condition.apply(criteria.getDecidedBy(), copy.getDecidedBy()) &&
                condition.apply(criteria.getDecidedAt(), copy.getDecidedAt()) &&
                condition.apply(criteria.getCorrectionOfId(), copy.getCorrectionOfId()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
