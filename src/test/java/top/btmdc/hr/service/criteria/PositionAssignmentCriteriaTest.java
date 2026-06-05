package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionAssignmentCriteriaTest {

    @Test
    void newPositionAssignmentCriteriaHasAllFiltersNullTest() {
        var positionAssignmentCriteria = new PositionAssignmentCriteria();
        assertThat(positionAssignmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionAssignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var positionAssignmentCriteria = new PositionAssignmentCriteria();

        setAllFilters(positionAssignmentCriteria);

        assertThat(positionAssignmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionAssignmentCriteriaCopyCreatesNullFilterTest() {
        var positionAssignmentCriteria = new PositionAssignmentCriteria();
        var copy = positionAssignmentCriteria.copy();

        assertThat(positionAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionAssignmentCriteria)
        );
    }

    @Test
    void positionAssignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionAssignmentCriteria = new PositionAssignmentCriteria();
        setAllFilters(positionAssignmentCriteria);

        var copy = positionAssignmentCriteria.copy();

        assertThat(positionAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionAssignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionAssignmentCriteria = new PositionAssignmentCriteria();

        assertThat(positionAssignmentCriteria).hasToString("PositionAssignmentCriteria{}");
    }

    private static void setAllFilters(PositionAssignmentCriteria positionAssignmentCriteria) {
        positionAssignmentCriteria.id();
        positionAssignmentCriteria.primaryOwner();
        positionAssignmentCriteria.startDate();
        positionAssignmentCriteria.endDate();
        positionAssignmentCriteria.active();
        positionAssignmentCriteria.personId();
        positionAssignmentCriteria.positionId();
        positionAssignmentCriteria.distinct();
    }

    private static Condition<PositionAssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrimaryOwner()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionAssignmentCriteria> copyFiltersAre(
        PositionAssignmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrimaryOwner(), copy.getPrimaryOwner()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
