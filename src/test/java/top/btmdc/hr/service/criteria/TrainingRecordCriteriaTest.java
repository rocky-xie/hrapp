package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrainingRecordCriteriaTest {

    @Test
    void newTrainingRecordCriteriaHasAllFiltersNullTest() {
        var trainingRecordCriteria = new TrainingRecordCriteria();
        assertThat(trainingRecordCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trainingRecordCriteriaFluentMethodsCreatesFiltersTest() {
        var trainingRecordCriteria = new TrainingRecordCriteria();

        setAllFilters(trainingRecordCriteria);

        assertThat(trainingRecordCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trainingRecordCriteriaCopyCreatesNullFilterTest() {
        var trainingRecordCriteria = new TrainingRecordCriteria();
        var copy = trainingRecordCriteria.copy();

        assertThat(trainingRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trainingRecordCriteria)
        );
    }

    @Test
    void trainingRecordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trainingRecordCriteria = new TrainingRecordCriteria();
        setAllFilters(trainingRecordCriteria);

        var copy = trainingRecordCriteria.copy();

        assertThat(trainingRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trainingRecordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trainingRecordCriteria = new TrainingRecordCriteria();

        assertThat(trainingRecordCriteria).hasToString("TrainingRecordCriteria{}");
    }

    private static void setAllFilters(TrainingRecordCriteria trainingRecordCriteria) {
        trainingRecordCriteria.id();
        trainingRecordCriteria.trainingDate();
        trainingRecordCriteria.trainingType();
        trainingRecordCriteria.topic();
        trainingRecordCriteria.personId();
        trainingRecordCriteria.trainingGoalId();
        trainingRecordCriteria.positionId();
        trainingRecordCriteria.mentorId();
        trainingRecordCriteria.distinct();
    }

    private static Condition<TrainingRecordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTrainingDate()) &&
                condition.apply(criteria.getTrainingType()) &&
                condition.apply(criteria.getTopic()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getTrainingGoalId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getMentorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrainingRecordCriteria> copyFiltersAre(
        TrainingRecordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTrainingDate(), copy.getTrainingDate()) &&
                condition.apply(criteria.getTrainingType(), copy.getTrainingType()) &&
                condition.apply(criteria.getTopic(), copy.getTopic()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getTrainingGoalId(), copy.getTrainingGoalId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getMentorId(), copy.getMentorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
