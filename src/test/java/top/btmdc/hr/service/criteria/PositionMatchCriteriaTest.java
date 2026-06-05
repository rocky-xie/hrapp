package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionMatchCriteriaTest {

    @Test
    void newPositionMatchCriteriaHasAllFiltersNullTest() {
        var positionMatchCriteria = new PositionMatchCriteria();
        assertThat(positionMatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionMatchCriteriaFluentMethodsCreatesFiltersTest() {
        var positionMatchCriteria = new PositionMatchCriteria();

        setAllFilters(positionMatchCriteria);

        assertThat(positionMatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionMatchCriteriaCopyCreatesNullFilterTest() {
        var positionMatchCriteria = new PositionMatchCriteria();
        var copy = positionMatchCriteria.copy();

        assertThat(positionMatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionMatchCriteria)
        );
    }

    @Test
    void positionMatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionMatchCriteria = new PositionMatchCriteria();
        setAllFilters(positionMatchCriteria);

        var copy = positionMatchCriteria.copy();

        assertThat(positionMatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionMatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionMatchCriteria = new PositionMatchCriteria();

        assertThat(positionMatchCriteria).hasToString("PositionMatchCriteria{}");
    }

    private static void setAllFilters(PositionMatchCriteria positionMatchCriteria) {
        positionMatchCriteria.id();
        positionMatchCriteria.matchScore();
        positionMatchCriteria.readiness();
        positionMatchCriteria.recommendation();
        positionMatchCriteria.analysisDate();
        positionMatchCriteria.personId();
        positionMatchCriteria.positionId();
        positionMatchCriteria.distinct();
    }

    private static Condition<PositionMatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMatchScore()) &&
                condition.apply(criteria.getReadiness()) &&
                condition.apply(criteria.getRecommendation()) &&
                condition.apply(criteria.getAnalysisDate()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionMatchCriteria> copyFiltersAre(
        PositionMatchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMatchScore(), copy.getMatchScore()) &&
                condition.apply(criteria.getReadiness(), copy.getReadiness()) &&
                condition.apply(criteria.getRecommendation(), copy.getRecommendation()) &&
                condition.apply(criteria.getAnalysisDate(), copy.getAnalysisDate()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
