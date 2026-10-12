package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ApplicationStageCriteriaTest {

    @Test
    void newApplicationStageCriteriaHasAllFiltersNullTest() {
        var applicationStageCriteria = new ApplicationStageCriteria();
        assertThat(applicationStageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void applicationStageCriteriaFluentMethodsCreatesFiltersTest() {
        var applicationStageCriteria = new ApplicationStageCriteria();

        setAllFilters(applicationStageCriteria);

        assertThat(applicationStageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void applicationStageCriteriaCopyCreatesNullFilterTest() {
        var applicationStageCriteria = new ApplicationStageCriteria();
        var copy = applicationStageCriteria.copy();

        assertThat(applicationStageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationStageCriteria)
        );
    }

    @Test
    void applicationStageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var applicationStageCriteria = new ApplicationStageCriteria();
        setAllFilters(applicationStageCriteria);

        var copy = applicationStageCriteria.copy();

        assertThat(applicationStageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationStageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var applicationStageCriteria = new ApplicationStageCriteria();

        assertThat(applicationStageCriteria).hasToString("ApplicationStageCriteria{}");
    }

    private static void setAllFilters(ApplicationStageCriteria applicationStageCriteria) {
        applicationStageCriteria.id();
        applicationStageCriteria.stageType();
        applicationStageCriteria.sequenceNumber();
        applicationStageCriteria.stageStatus();
        applicationStageCriteria.startedAt();
        applicationStageCriteria.completedAt();
        applicationStageCriteria.selected();
        applicationStageCriteria.applicationId();
        applicationStageCriteria.distinct();
    }

    private static Condition<ApplicationStageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStageType()) &&
                condition.apply(criteria.getSequenceNumber()) &&
                condition.apply(criteria.getStageStatus()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getSelected()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ApplicationStageCriteria> copyFiltersAre(
        ApplicationStageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStageType(), copy.getStageType()) &&
                condition.apply(criteria.getSequenceNumber(), copy.getSequenceNumber()) &&
                condition.apply(criteria.getStageStatus(), copy.getStageStatus()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getSelected(), copy.getSelected()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
