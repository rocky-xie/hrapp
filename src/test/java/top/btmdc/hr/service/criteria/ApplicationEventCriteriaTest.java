package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ApplicationEventCriteriaTest {

    @Test
    void newApplicationEventCriteriaHasAllFiltersNullTest() {
        var applicationEventCriteria = new ApplicationEventCriteria();
        assertThat(applicationEventCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void applicationEventCriteriaFluentMethodsCreatesFiltersTest() {
        var applicationEventCriteria = new ApplicationEventCriteria();

        setAllFilters(applicationEventCriteria);

        assertThat(applicationEventCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void applicationEventCriteriaCopyCreatesNullFilterTest() {
        var applicationEventCriteria = new ApplicationEventCriteria();
        var copy = applicationEventCriteria.copy();

        assertThat(applicationEventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationEventCriteria)
        );
    }

    @Test
    void applicationEventCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var applicationEventCriteria = new ApplicationEventCriteria();
        setAllFilters(applicationEventCriteria);

        var copy = applicationEventCriteria.copy();

        assertThat(applicationEventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationEventCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var applicationEventCriteria = new ApplicationEventCriteria();

        assertThat(applicationEventCriteria).hasToString("ApplicationEventCriteria{}");
    }

    private static void setAllFilters(ApplicationEventCriteria applicationEventCriteria) {
        applicationEventCriteria.id();
        applicationEventCriteria.stageType();
        applicationEventCriteria.eventType();
        applicationEventCriteria.previousValue();
        applicationEventCriteria.newValue();
        applicationEventCriteria.actor();
        applicationEventCriteria.occurredAt();
        applicationEventCriteria.applicationId();
        applicationEventCriteria.distinct();
    }

    private static Condition<ApplicationEventCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStageType()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getPreviousValue()) &&
                condition.apply(criteria.getNewValue()) &&
                condition.apply(criteria.getActor()) &&
                condition.apply(criteria.getOccurredAt()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ApplicationEventCriteria> copyFiltersAre(
        ApplicationEventCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStageType(), copy.getStageType()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getPreviousValue(), copy.getPreviousValue()) &&
                condition.apply(criteria.getNewValue(), copy.getNewValue()) &&
                condition.apply(criteria.getActor(), copy.getActor()) &&
                condition.apply(criteria.getOccurredAt(), copy.getOccurredAt()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
