package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrainingGoalCriteriaTest {

    @Test
    void newTrainingGoalCriteriaHasAllFiltersNullTest() {
        var trainingGoalCriteria = new TrainingGoalCriteria();
        assertThat(trainingGoalCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trainingGoalCriteriaFluentMethodsCreatesFiltersTest() {
        var trainingGoalCriteria = new TrainingGoalCriteria();

        setAllFilters(trainingGoalCriteria);

        assertThat(trainingGoalCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trainingGoalCriteriaCopyCreatesNullFilterTest() {
        var trainingGoalCriteria = new TrainingGoalCriteria();
        var copy = trainingGoalCriteria.copy();

        assertThat(trainingGoalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trainingGoalCriteria)
        );
    }

    @Test
    void trainingGoalCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trainingGoalCriteria = new TrainingGoalCriteria();
        setAllFilters(trainingGoalCriteria);

        var copy = trainingGoalCriteria.copy();

        assertThat(trainingGoalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trainingGoalCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trainingGoalCriteria = new TrainingGoalCriteria();

        assertThat(trainingGoalCriteria).hasToString("TrainingGoalCriteria{}");
    }

    private static void setAllFilters(TrainingGoalCriteria trainingGoalCriteria) {
        trainingGoalCriteria.id();
        trainingGoalCriteria.goalName();
        trainingGoalCriteria.startDate();
        trainingGoalCriteria.targetDate();
        trainingGoalCriteria.status();
        trainingGoalCriteria.personId();
        trainingGoalCriteria.positionId();
        trainingGoalCriteria.skillId();
        trainingGoalCriteria.targetLevelId();
        trainingGoalCriteria.distinct();
    }

    private static Condition<TrainingGoalCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getGoalName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getTargetDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getTargetLevelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrainingGoalCriteria> copyFiltersAre(
        TrainingGoalCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getGoalName(), copy.getGoalName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getTargetDate(), copy.getTargetDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getTargetLevelId(), copy.getTargetLevelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
