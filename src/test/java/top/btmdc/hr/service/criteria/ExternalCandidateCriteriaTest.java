package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExternalCandidateCriteriaTest {

    @Test
    void newExternalCandidateCriteriaHasAllFiltersNullTest() {
        var externalCandidateCriteria = new ExternalCandidateCriteria();
        assertThat(externalCandidateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void externalCandidateCriteriaFluentMethodsCreatesFiltersTest() {
        var externalCandidateCriteria = new ExternalCandidateCriteria();

        setAllFilters(externalCandidateCriteria);

        assertThat(externalCandidateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void externalCandidateCriteriaCopyCreatesNullFilterTest() {
        var externalCandidateCriteria = new ExternalCandidateCriteria();
        var copy = externalCandidateCriteria.copy();

        assertThat(externalCandidateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(externalCandidateCriteria)
        );
    }

    @Test
    void externalCandidateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var externalCandidateCriteria = new ExternalCandidateCriteria();
        setAllFilters(externalCandidateCriteria);

        var copy = externalCandidateCriteria.copy();

        assertThat(externalCandidateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(externalCandidateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var externalCandidateCriteria = new ExternalCandidateCriteria();

        assertThat(externalCandidateCriteria).hasToString("ExternalCandidateCriteria{}");
    }

    private static void setAllFilters(ExternalCandidateCriteria externalCandidateCriteria) {
        externalCandidateCriteria.id();
        externalCandidateCriteria.fullName();
        externalCandidateCriteria.nameReading();
        externalCandidateCriteria.englishName();
        externalCandidateCriteria.birthDate();
        externalCandidateCriteria.gender();
        externalCandidateCriteria.phone();
        externalCandidateCriteria.email();
        externalCandidateCriteria.convertedPersonId();
        externalCandidateCriteria.createdAt();
        externalCandidateCriteria.updatedAt();
        externalCandidateCriteria.resumesId();
        externalCandidateCriteria.applicationsId();
        externalCandidateCriteria.distinct();
    }

    private static Condition<ExternalCandidateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getNameReading()) &&
                condition.apply(criteria.getEnglishName()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getConvertedPersonId()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getResumesId()) &&
                condition.apply(criteria.getApplicationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExternalCandidateCriteria> copyFiltersAre(
        ExternalCandidateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getNameReading(), copy.getNameReading()) &&
                condition.apply(criteria.getEnglishName(), copy.getEnglishName()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getConvertedPersonId(), copy.getConvertedPersonId()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getResumesId(), copy.getResumesId()) &&
                condition.apply(criteria.getApplicationsId(), copy.getApplicationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
