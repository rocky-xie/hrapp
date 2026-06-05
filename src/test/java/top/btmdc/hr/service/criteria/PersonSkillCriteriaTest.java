package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PersonSkillCriteriaTest {

    @Test
    void newPersonSkillCriteriaHasAllFiltersNullTest() {
        var personSkillCriteria = new PersonSkillCriteria();
        assertThat(personSkillCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void personSkillCriteriaFluentMethodsCreatesFiltersTest() {
        var personSkillCriteria = new PersonSkillCriteria();

        setAllFilters(personSkillCriteria);

        assertThat(personSkillCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void personSkillCriteriaCopyCreatesNullFilterTest() {
        var personSkillCriteria = new PersonSkillCriteria();
        var copy = personSkillCriteria.copy();

        assertThat(personSkillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(personSkillCriteria)
        );
    }

    @Test
    void personSkillCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var personSkillCriteria = new PersonSkillCriteria();
        setAllFilters(personSkillCriteria);

        var copy = personSkillCriteria.copy();

        assertThat(personSkillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(personSkillCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var personSkillCriteria = new PersonSkillCriteria();

        assertThat(personSkillCriteria).hasToString("PersonSkillCriteria{}");
    }

    private static void setAllFilters(PersonSkillCriteria personSkillCriteria) {
        personSkillCriteria.id();
        personSkillCriteria.assessmentDate();
        personSkillCriteria.nextReviewDate();
        personSkillCriteria.confidence();
        personSkillCriteria.personId();
        personSkillCriteria.skillId();
        personSkillCriteria.currentLevelId();
        personSkillCriteria.previousLevelId();
        personSkillCriteria.distinct();
    }

    private static Condition<PersonSkillCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssessmentDate()) &&
                condition.apply(criteria.getNextReviewDate()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getCurrentLevelId()) &&
                condition.apply(criteria.getPreviousLevelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PersonSkillCriteria> copyFiltersAre(PersonSkillCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssessmentDate(), copy.getAssessmentDate()) &&
                condition.apply(criteria.getNextReviewDate(), copy.getNextReviewDate()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getCurrentLevelId(), copy.getCurrentLevelId()) &&
                condition.apply(criteria.getPreviousLevelId(), copy.getPreviousLevelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
