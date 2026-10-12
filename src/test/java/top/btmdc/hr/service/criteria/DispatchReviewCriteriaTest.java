package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DispatchReviewCriteriaTest {

    @Test
    void newDispatchReviewCriteriaHasAllFiltersNullTest() {
        var dispatchReviewCriteria = new DispatchReviewCriteria();
        assertThat(dispatchReviewCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dispatchReviewCriteriaFluentMethodsCreatesFiltersTest() {
        var dispatchReviewCriteria = new DispatchReviewCriteria();

        setAllFilters(dispatchReviewCriteria);

        assertThat(dispatchReviewCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dispatchReviewCriteriaCopyCreatesNullFilterTest() {
        var dispatchReviewCriteria = new DispatchReviewCriteria();
        var copy = dispatchReviewCriteria.copy();

        assertThat(dispatchReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dispatchReviewCriteria)
        );
    }

    @Test
    void dispatchReviewCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dispatchReviewCriteria = new DispatchReviewCriteria();
        setAllFilters(dispatchReviewCriteria);

        var copy = dispatchReviewCriteria.copy();

        assertThat(dispatchReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dispatchReviewCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dispatchReviewCriteria = new DispatchReviewCriteria();

        assertThat(dispatchReviewCriteria).hasToString("DispatchReviewCriteria{}");
    }

    private static void setAllFilters(DispatchReviewCriteria dispatchReviewCriteria) {
        dispatchReviewCriteria.id();
        dispatchReviewCriteria.reviewedAt();
        dispatchReviewCriteria.reviewerUsername();
        dispatchReviewCriteria.recommendation();
        dispatchReviewCriteria.extensionEndDate();
        dispatchReviewCriteria.finalDecision();
        dispatchReviewCriteria.dispatchAssignmentId();
        dispatchReviewCriteria.distinct();
    }

    private static Condition<DispatchReviewCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReviewedAt()) &&
                condition.apply(criteria.getReviewerUsername()) &&
                condition.apply(criteria.getRecommendation()) &&
                condition.apply(criteria.getExtensionEndDate()) &&
                condition.apply(criteria.getFinalDecision()) &&
                condition.apply(criteria.getDispatchAssignmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DispatchReviewCriteria> copyFiltersAre(
        DispatchReviewCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReviewedAt(), copy.getReviewedAt()) &&
                condition.apply(criteria.getReviewerUsername(), copy.getReviewerUsername()) &&
                condition.apply(criteria.getRecommendation(), copy.getRecommendation()) &&
                condition.apply(criteria.getExtensionEndDate(), copy.getExtensionEndDate()) &&
                condition.apply(criteria.getFinalDecision(), copy.getFinalDecision()) &&
                condition.apply(criteria.getDispatchAssignmentId(), copy.getDispatchAssignmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
