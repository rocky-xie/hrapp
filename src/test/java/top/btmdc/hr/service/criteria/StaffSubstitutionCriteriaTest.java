package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StaffSubstitutionCriteriaTest {

    @Test
    void newStaffSubstitutionCriteriaHasAllFiltersNullTest() {
        var staffSubstitutionCriteria = new StaffSubstitutionCriteria();
        assertThat(staffSubstitutionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void staffSubstitutionCriteriaFluentMethodsCreatesFiltersTest() {
        var staffSubstitutionCriteria = new StaffSubstitutionCriteria();

        setAllFilters(staffSubstitutionCriteria);

        assertThat(staffSubstitutionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void staffSubstitutionCriteriaCopyCreatesNullFilterTest() {
        var staffSubstitutionCriteria = new StaffSubstitutionCriteria();
        var copy = staffSubstitutionCriteria.copy();

        assertThat(staffSubstitutionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(staffSubstitutionCriteria)
        );
    }

    @Test
    void staffSubstitutionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var staffSubstitutionCriteria = new StaffSubstitutionCriteria();
        setAllFilters(staffSubstitutionCriteria);

        var copy = staffSubstitutionCriteria.copy();

        assertThat(staffSubstitutionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(staffSubstitutionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var staffSubstitutionCriteria = new StaffSubstitutionCriteria();

        assertThat(staffSubstitutionCriteria).hasToString("StaffSubstitutionCriteria{}");
    }

    private static void setAllFilters(StaffSubstitutionCriteria staffSubstitutionCriteria) {
        staffSubstitutionCriteria.id();
        staffSubstitutionCriteria.coverageRate();
        staffSubstitutionCriteria.thresholdRate();
        staffSubstitutionCriteria.totalSkillCount();
        staffSubstitutionCriteria.coveredSkillCount();
        staffSubstitutionCriteria.substitutable();
        staffSubstitutionCriteria.evaluationDate();
        staffSubstitutionCriteria.positionId();
        staffSubstitutionCriteria.candidatePersonId();
        staffSubstitutionCriteria.distinct();
    }

    private static Condition<StaffSubstitutionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCoverageRate()) &&
                condition.apply(criteria.getThresholdRate()) &&
                condition.apply(criteria.getTotalSkillCount()) &&
                condition.apply(criteria.getCoveredSkillCount()) &&
                condition.apply(criteria.getSubstitutable()) &&
                condition.apply(criteria.getEvaluationDate()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getCandidatePersonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StaffSubstitutionCriteria> copyFiltersAre(
        StaffSubstitutionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCoverageRate(), copy.getCoverageRate()) &&
                condition.apply(criteria.getThresholdRate(), copy.getThresholdRate()) &&
                condition.apply(criteria.getTotalSkillCount(), copy.getTotalSkillCount()) &&
                condition.apply(criteria.getCoveredSkillCount(), copy.getCoveredSkillCount()) &&
                condition.apply(criteria.getSubstitutable(), copy.getSubstitutable()) &&
                condition.apply(criteria.getEvaluationDate(), copy.getEvaluationDate()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getCandidatePersonId(), copy.getCandidatePersonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
