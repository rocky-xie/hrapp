package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SuccessionCandidateCriteriaTest {

    @Test
    void newSuccessionCandidateCriteriaHasAllFiltersNullTest() {
        var successionCandidateCriteria = new SuccessionCandidateCriteria();
        assertThat(successionCandidateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void successionCandidateCriteriaFluentMethodsCreatesFiltersTest() {
        var successionCandidateCriteria = new SuccessionCandidateCriteria();

        setAllFilters(successionCandidateCriteria);

        assertThat(successionCandidateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void successionCandidateCriteriaCopyCreatesNullFilterTest() {
        var successionCandidateCriteria = new SuccessionCandidateCriteria();
        var copy = successionCandidateCriteria.copy();

        assertThat(successionCandidateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(successionCandidateCriteria)
        );
    }

    @Test
    void successionCandidateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var successionCandidateCriteria = new SuccessionCandidateCriteria();
        setAllFilters(successionCandidateCriteria);

        var copy = successionCandidateCriteria.copy();

        assertThat(successionCandidateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(successionCandidateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var successionCandidateCriteria = new SuccessionCandidateCriteria();

        assertThat(successionCandidateCriteria).hasToString("SuccessionCandidateCriteria{}");
    }

    private static void setAllFilters(SuccessionCandidateCriteria successionCandidateCriteria) {
        successionCandidateCriteria.id();
        successionCandidateCriteria.successionReadiness();
        successionCandidateCriteria.estimatedTimeToReady();
        successionCandidateCriteria.riskAfterTraining();
        successionCandidateCriteria.reviewDate();
        successionCandidateCriteria.priority();
        successionCandidateCriteria.positionId();
        successionCandidateCriteria.currentOwnerId();
        successionCandidateCriteria.candidateId();
        successionCandidateCriteria.distinct();
    }

    private static Condition<SuccessionCandidateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSuccessionReadiness()) &&
                condition.apply(criteria.getEstimatedTimeToReady()) &&
                condition.apply(criteria.getRiskAfterTraining()) &&
                condition.apply(criteria.getReviewDate()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getCurrentOwnerId()) &&
                condition.apply(criteria.getCandidateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SuccessionCandidateCriteria> copyFiltersAre(
        SuccessionCandidateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSuccessionReadiness(), copy.getSuccessionReadiness()) &&
                condition.apply(criteria.getEstimatedTimeToReady(), copy.getEstimatedTimeToReady()) &&
                condition.apply(criteria.getRiskAfterTraining(), copy.getRiskAfterTraining()) &&
                condition.apply(criteria.getReviewDate(), copy.getReviewDate()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getCurrentOwnerId(), copy.getCurrentOwnerId()) &&
                condition.apply(criteria.getCandidateId(), copy.getCandidateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
