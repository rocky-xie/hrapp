package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ActionItemCriteriaTest {

    @Test
    void newActionItemCriteriaHasAllFiltersNullTest() {
        var actionItemCriteria = new ActionItemCriteria();
        assertThat(actionItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void actionItemCriteriaFluentMethodsCreatesFiltersTest() {
        var actionItemCriteria = new ActionItemCriteria();

        setAllFilters(actionItemCriteria);

        assertThat(actionItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void actionItemCriteriaCopyCreatesNullFilterTest() {
        var actionItemCriteria = new ActionItemCriteria();
        var copy = actionItemCriteria.copy();

        assertThat(actionItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(actionItemCriteria)
        );
    }

    @Test
    void actionItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var actionItemCriteria = new ActionItemCriteria();
        setAllFilters(actionItemCriteria);

        var copy = actionItemCriteria.copy();

        assertThat(actionItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(actionItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var actionItemCriteria = new ActionItemCriteria();

        assertThat(actionItemCriteria).hasToString("ActionItemCriteria{}");
    }

    private static void setAllFilters(ActionItemCriteria actionItemCriteria) {
        actionItemCriteria.id();
        actionItemCriteria.sourceType();
        actionItemCriteria.sourceId();
        actionItemCriteria.sourceEntityType();
        actionItemCriteria.description();
        actionItemCriteria.assignee();
        actionItemCriteria.dueDate();
        actionItemCriteria.status();
        actionItemCriteria.priority();
        actionItemCriteria.createdAt();
        actionItemCriteria.completedAt();
        actionItemCriteria.distinct();
    }

    private static Condition<ActionItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSourceType()) &&
                condition.apply(criteria.getSourceId()) &&
                condition.apply(criteria.getSourceEntityType()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAssignee()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ActionItemCriteria> copyFiltersAre(ActionItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSourceType(), copy.getSourceType()) &&
                condition.apply(criteria.getSourceId(), copy.getSourceId()) &&
                condition.apply(criteria.getSourceEntityType(), copy.getSourceEntityType()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAssignee(), copy.getAssignee()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
