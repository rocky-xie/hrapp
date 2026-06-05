package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SkillAssessmentCriteriaTest {

    @Test
    void newSkillAssessmentCriteriaHasAllFiltersNullTest() {
        var skillAssessmentCriteria = new SkillAssessmentCriteria();
        assertThat(skillAssessmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void skillAssessmentCriteriaFluentMethodsCreatesFiltersTest() {
        var skillAssessmentCriteria = new SkillAssessmentCriteria();

        setAllFilters(skillAssessmentCriteria);

        assertThat(skillAssessmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void skillAssessmentCriteriaCopyCreatesNullFilterTest() {
        var skillAssessmentCriteria = new SkillAssessmentCriteria();
        var copy = skillAssessmentCriteria.copy();

        assertThat(skillAssessmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(skillAssessmentCriteria)
        );
    }

    @Test
    void skillAssessmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var skillAssessmentCriteria = new SkillAssessmentCriteria();
        setAllFilters(skillAssessmentCriteria);

        var copy = skillAssessmentCriteria.copy();

        assertThat(skillAssessmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(skillAssessmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var skillAssessmentCriteria = new SkillAssessmentCriteria();

        assertThat(skillAssessmentCriteria).hasToString("SkillAssessmentCriteria{}");
    }

    private static void setAllFilters(SkillAssessmentCriteria skillAssessmentCriteria) {
        skillAssessmentCriteria.id();
        skillAssessmentCriteria.assessmentDate();
        skillAssessmentCriteria.result();
        skillAssessmentCriteria.personId();
        skillAssessmentCriteria.skillId();
        skillAssessmentCriteria.assessorId();
        skillAssessmentCriteria.newLevelId();
        skillAssessmentCriteria.distinct();
    }

    private static Condition<SkillAssessmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssessmentDate()) &&
                condition.apply(criteria.getResult()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getAssessorId()) &&
                condition.apply(criteria.getNewLevelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SkillAssessmentCriteria> copyFiltersAre(
        SkillAssessmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssessmentDate(), copy.getAssessmentDate()) &&
                condition.apply(criteria.getResult(), copy.getResult()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getAssessorId(), copy.getAssessorId()) &&
                condition.apply(criteria.getNewLevelId(), copy.getNewLevelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
