package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrustObservationCriteriaTest {

    @Test
    void newTrustObservationCriteriaHasAllFiltersNullTest() {
        var trustObservationCriteria = new TrustObservationCriteria();
        assertThat(trustObservationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trustObservationCriteriaFluentMethodsCreatesFiltersTest() {
        var trustObservationCriteria = new TrustObservationCriteria();

        setAllFilters(trustObservationCriteria);

        assertThat(trustObservationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trustObservationCriteriaCopyCreatesNullFilterTest() {
        var trustObservationCriteria = new TrustObservationCriteria();
        var copy = trustObservationCriteria.copy();

        assertThat(trustObservationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trustObservationCriteria)
        );
    }

    @Test
    void trustObservationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trustObservationCriteria = new TrustObservationCriteria();
        setAllFilters(trustObservationCriteria);

        var copy = trustObservationCriteria.copy();

        assertThat(trustObservationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trustObservationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trustObservationCriteria = new TrustObservationCriteria();

        assertThat(trustObservationCriteria).hasToString("TrustObservationCriteria{}");
    }

    private static void setAllFilters(TrustObservationCriteria trustObservationCriteria) {
        trustObservationCriteria.id();
        trustObservationCriteria.observationDate();
        trustObservationCriteria.trustStage();
        trustObservationCriteria.personId();
        trustObservationCriteria.observerId();
        trustObservationCriteria.distinct();
    }

    private static Condition<TrustObservationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getObservationDate()) &&
                condition.apply(criteria.getTrustStage()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getObserverId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrustObservationCriteria> copyFiltersAre(
        TrustObservationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getObservationDate(), copy.getObservationDate()) &&
                condition.apply(criteria.getTrustStage(), copy.getTrustStage()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getObserverId(), copy.getObserverId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
