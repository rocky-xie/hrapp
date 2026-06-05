package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class KeyResponsibilityCategoryCriteriaTest {

    @Test
    void newKeyResponsibilityCategoryCriteriaHasAllFiltersNullTest() {
        var keyResponsibilityCategoryCriteria = new KeyResponsibilityCategoryCriteria();
        assertThat(keyResponsibilityCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void keyResponsibilityCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var keyResponsibilityCategoryCriteria = new KeyResponsibilityCategoryCriteria();

        setAllFilters(keyResponsibilityCategoryCriteria);

        assertThat(keyResponsibilityCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void keyResponsibilityCategoryCriteriaCopyCreatesNullFilterTest() {
        var keyResponsibilityCategoryCriteria = new KeyResponsibilityCategoryCriteria();
        var copy = keyResponsibilityCategoryCriteria.copy();

        assertThat(keyResponsibilityCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(keyResponsibilityCategoryCriteria)
        );
    }

    @Test
    void keyResponsibilityCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var keyResponsibilityCategoryCriteria = new KeyResponsibilityCategoryCriteria();
        setAllFilters(keyResponsibilityCategoryCriteria);

        var copy = keyResponsibilityCategoryCriteria.copy();

        assertThat(keyResponsibilityCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(keyResponsibilityCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var keyResponsibilityCategoryCriteria = new KeyResponsibilityCategoryCriteria();

        assertThat(keyResponsibilityCategoryCriteria).hasToString("KeyResponsibilityCategoryCriteria{}");
    }

    private static void setAllFilters(KeyResponsibilityCategoryCriteria keyResponsibilityCategoryCriteria) {
        keyResponsibilityCategoryCriteria.id();
        keyResponsibilityCategoryCriteria.categoryName();
        keyResponsibilityCategoryCriteria.distinct();
    }

    private static Condition<KeyResponsibilityCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getCategoryName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<KeyResponsibilityCategoryCriteria> copyFiltersAre(
        KeyResponsibilityCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryName(), copy.getCategoryName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
