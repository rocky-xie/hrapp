package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SelectionActivityCriteriaTest {

    @Test
    void newSelectionActivityCriteriaHasAllFiltersNullTest() {
        var selectionActivityCriteria = new SelectionActivityCriteria();
        assertThat(selectionActivityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void selectionActivityCriteriaFluentMethodsCreatesFiltersTest() {
        var selectionActivityCriteria = new SelectionActivityCriteria();

        setAllFilters(selectionActivityCriteria);

        assertThat(selectionActivityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void selectionActivityCriteriaCopyCreatesNullFilterTest() {
        var selectionActivityCriteria = new SelectionActivityCriteria();
        var copy = selectionActivityCriteria.copy();

        assertThat(selectionActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(selectionActivityCriteria)
        );
    }

    @Test
    void selectionActivityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var selectionActivityCriteria = new SelectionActivityCriteria();
        setAllFilters(selectionActivityCriteria);

        var copy = selectionActivityCriteria.copy();

        assertThat(selectionActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(selectionActivityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var selectionActivityCriteria = new SelectionActivityCriteria();

        assertThat(selectionActivityCriteria).hasToString("SelectionActivityCriteria{}");
    }

    private static void setAllFilters(SelectionActivityCriteria selectionActivityCriteria) {
        selectionActivityCriteria.id();
        selectionActivityCriteria.stageType();
        selectionActivityCriteria.activityType();
        selectionActivityCriteria.startAt();
        selectionActivityCriteria.endAt();
        selectionActivityCriteria.meetingRoomId();
        selectionActivityCriteria.location();
        selectionActivityCriteria.status();
        selectionActivityCriteria.scheduleVersion();
        selectionActivityCriteria.externalCalendarProvider();
        selectionActivityCriteria.externalEventId();
        selectionActivityCriteria.applicationId();
        selectionActivityCriteria.distinct();
    }

    private static Condition<SelectionActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStageType()) &&
                condition.apply(criteria.getActivityType()) &&
                condition.apply(criteria.getStartAt()) &&
                condition.apply(criteria.getEndAt()) &&
                condition.apply(criteria.getMeetingRoomId()) &&
                condition.apply(criteria.getLocation()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getScheduleVersion()) &&
                condition.apply(criteria.getExternalCalendarProvider()) &&
                condition.apply(criteria.getExternalEventId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SelectionActivityCriteria> copyFiltersAre(
        SelectionActivityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStageType(), copy.getStageType()) &&
                condition.apply(criteria.getActivityType(), copy.getActivityType()) &&
                condition.apply(criteria.getStartAt(), copy.getStartAt()) &&
                condition.apply(criteria.getEndAt(), copy.getEndAt()) &&
                condition.apply(criteria.getMeetingRoomId(), copy.getMeetingRoomId()) &&
                condition.apply(criteria.getLocation(), copy.getLocation()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getScheduleVersion(), copy.getScheduleVersion()) &&
                condition.apply(criteria.getExternalCalendarProvider(), copy.getExternalCalendarProvider()) &&
                condition.apply(criteria.getExternalEventId(), copy.getExternalEventId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
