package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PersonRiskCriteriaTest {

    @Test
    void newPersonRiskCriteriaHasAllFiltersNullTest() {
        var personRiskCriteria = new PersonRiskCriteria();
        assertThat(personRiskCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void personRiskCriteriaFluentMethodsCreatesFiltersTest() {
        var personRiskCriteria = new PersonRiskCriteria();

        setAllFilters(personRiskCriteria);

        assertThat(personRiskCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void personRiskCriteriaCopyCreatesNullFilterTest() {
        var personRiskCriteria = new PersonRiskCriteria();
        var copy = personRiskCriteria.copy();

        assertThat(personRiskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(personRiskCriteria)
        );
    }

    @Test
    void personRiskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var personRiskCriteria = new PersonRiskCriteria();
        setAllFilters(personRiskCriteria);

        var copy = personRiskCriteria.copy();

        assertThat(personRiskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(personRiskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var personRiskCriteria = new PersonRiskCriteria();

        assertThat(personRiskCriteria).hasToString("PersonRiskCriteria{}");
    }

    private static void setAllFilters(PersonRiskCriteria personRiskCriteria) {
        personRiskCriteria.id();
        personRiskCriteria.riskType();
        personRiskCriteria.riskLevel();
        personRiskCriteria.identifiedDate();
        personRiskCriteria.targetDate();
        personRiskCriteria.closedDate();
        personRiskCriteria.personId();
        personRiskCriteria.positionId();
        personRiskCriteria.distinct();
    }

    private static Condition<PersonRiskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRiskType()) &&
                condition.apply(criteria.getRiskLevel()) &&
                condition.apply(criteria.getIdentifiedDate()) &&
                condition.apply(criteria.getTargetDate()) &&
                condition.apply(criteria.getClosedDate()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PersonRiskCriteria> copyFiltersAre(PersonRiskCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRiskType(), copy.getRiskType()) &&
                condition.apply(criteria.getRiskLevel(), copy.getRiskLevel()) &&
                condition.apply(criteria.getIdentifiedDate(), copy.getIdentifiedDate()) &&
                condition.apply(criteria.getTargetDate(), copy.getTargetDate()) &&
                condition.apply(criteria.getClosedDate(), copy.getClosedDate()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
