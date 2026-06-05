package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PersonCriteriaTest {

    @Test
    void newPersonCriteriaHasAllFiltersNullTest() {
        var personCriteria = new PersonCriteria();
        assertThat(personCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void personCriteriaFluentMethodsCreatesFiltersTest() {
        var personCriteria = new PersonCriteria();

        setAllFilters(personCriteria);

        assertThat(personCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void personCriteriaCopyCreatesNullFilterTest() {
        var personCriteria = new PersonCriteria();
        var copy = personCriteria.copy();

        assertThat(personCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(personCriteria)
        );
    }

    @Test
    void personCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var personCriteria = new PersonCriteria();
        setAllFilters(personCriteria);

        var copy = personCriteria.copy();

        assertThat(personCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(personCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var personCriteria = new PersonCriteria();

        assertThat(personCriteria).hasToString("PersonCriteria{}");
    }

    private static void setAllFilters(PersonCriteria personCriteria) {
        personCriteria.id();
        personCriteria.employeeCode();
        personCriteria.personName();
        personCriteria.age();
        personCriteria.gender();
        personCriteria.department();
        personCriteria.currentRole();
        personCriteria.employmentStatus();
        personCriteria.joinDate();
        personCriteria.mentorFlag();
        personCriteria.coreCandidateFlag();
        personCriteria.distinct();
    }

    private static Condition<PersonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEmployeeCode()) &&
                condition.apply(criteria.getPersonName()) &&
                condition.apply(criteria.getAge()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getDepartment()) &&
                condition.apply(criteria.getCurrentRole()) &&
                condition.apply(criteria.getEmploymentStatus()) &&
                condition.apply(criteria.getJoinDate()) &&
                condition.apply(criteria.getMentorFlag()) &&
                condition.apply(criteria.getCoreCandidateFlag()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PersonCriteria> copyFiltersAre(PersonCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEmployeeCode(), copy.getEmployeeCode()) &&
                condition.apply(criteria.getPersonName(), copy.getPersonName()) &&
                condition.apply(criteria.getAge(), copy.getAge()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getDepartment(), copy.getDepartment()) &&
                condition.apply(criteria.getCurrentRole(), copy.getCurrentRole()) &&
                condition.apply(criteria.getEmploymentStatus(), copy.getEmploymentStatus()) &&
                condition.apply(criteria.getJoinDate(), copy.getJoinDate()) &&
                condition.apply(criteria.getMentorFlag(), copy.getMentorFlag()) &&
                condition.apply(criteria.getCoreCandidateFlag(), copy.getCoreCandidateFlag()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
