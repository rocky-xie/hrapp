package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionCriteriaTest {

    @Test
    void newPositionCriteriaHasAllFiltersNullTest() {
        var positionCriteria = new PositionCriteria();
        assertThat(positionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionCriteriaFluentMethodsCreatesFiltersTest() {
        var positionCriteria = new PositionCriteria();

        setAllFilters(positionCriteria);

        assertThat(positionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionCriteriaCopyCreatesNullFilterTest() {
        var positionCriteria = new PositionCriteria();
        var copy = positionCriteria.copy();

        assertThat(positionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionCriteria)
        );
    }

    @Test
    void positionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionCriteria = new PositionCriteria();
        setAllFilters(positionCriteria);

        var copy = positionCriteria.copy();

        assertThat(positionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionCriteria = new PositionCriteria();

        assertThat(positionCriteria).hasToString("PositionCriteria{}");
    }

    private static void setAllFilters(PositionCriteria positionCriteria) {
        positionCriteria.id();
        positionCriteria.positionCode();
        positionCriteria.positionName();
        positionCriteria.positionType();
        positionCriteria.businessImportance();
        positionCriteria.keyPosition();
        positionCriteria.plannedHeadcount();
        positionCriteria.minimumOwnerCount();
        positionCriteria.reviewCycle();
        positionCriteria.active();
        positionCriteria.distinct();
    }

    private static Condition<PositionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPositionCode()) &&
                condition.apply(criteria.getPositionName()) &&
                condition.apply(criteria.getPositionType()) &&
                condition.apply(criteria.getBusinessImportance()) &&
                condition.apply(criteria.getKeyPosition()) &&
                condition.apply(criteria.getPlannedHeadcount()) &&
                condition.apply(criteria.getMinimumOwnerCount()) &&
                condition.apply(criteria.getReviewCycle()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionCriteria> copyFiltersAre(PositionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPositionCode(), copy.getPositionCode()) &&
                condition.apply(criteria.getPositionName(), copy.getPositionName()) &&
                condition.apply(criteria.getPositionType(), copy.getPositionType()) &&
                condition.apply(criteria.getBusinessImportance(), copy.getBusinessImportance()) &&
                condition.apply(criteria.getKeyPosition(), copy.getKeyPosition()) &&
                condition.apply(criteria.getPlannedHeadcount(), copy.getPlannedHeadcount()) &&
                condition.apply(criteria.getMinimumOwnerCount(), copy.getMinimumOwnerCount()) &&
                condition.apply(criteria.getReviewCycle(), copy.getReviewCycle()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
