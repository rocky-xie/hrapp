package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PreboardingFollowUpCriteriaTest {

    @Test
    void newPreboardingFollowUpCriteriaHasAllFiltersNullTest() {
        var preboardingFollowUpCriteria = new PreboardingFollowUpCriteria();
        assertThat(preboardingFollowUpCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void preboardingFollowUpCriteriaFluentMethodsCreatesFiltersTest() {
        var preboardingFollowUpCriteria = new PreboardingFollowUpCriteria();

        setAllFilters(preboardingFollowUpCriteria);

        assertThat(preboardingFollowUpCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void preboardingFollowUpCriteriaCopyCreatesNullFilterTest() {
        var preboardingFollowUpCriteria = new PreboardingFollowUpCriteria();
        var copy = preboardingFollowUpCriteria.copy();

        assertThat(preboardingFollowUpCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(preboardingFollowUpCriteria)
        );
    }

    @Test
    void preboardingFollowUpCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var preboardingFollowUpCriteria = new PreboardingFollowUpCriteria();
        setAllFilters(preboardingFollowUpCriteria);

        var copy = preboardingFollowUpCriteria.copy();

        assertThat(preboardingFollowUpCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(preboardingFollowUpCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var preboardingFollowUpCriteria = new PreboardingFollowUpCriteria();

        assertThat(preboardingFollowUpCriteria).hasToString("PreboardingFollowUpCriteria{}");
    }

    private static void setAllFilters(PreboardingFollowUpCriteria preboardingFollowUpCriteria) {
        preboardingFollowUpCriteria.id();
        preboardingFollowUpCriteria.candidateId();
        preboardingFollowUpCriteria.personId();
        preboardingFollowUpCriteria.plannedAt();
        preboardingFollowUpCriteria.contactedAt();
        preboardingFollowUpCriteria.ownerUsername();
        preboardingFollowUpCriteria.contactMethod();
        preboardingFollowUpCriteria.candidateIntent();
        preboardingFollowUpCriteria.riskLevel();
        preboardingFollowUpCriteria.nextContactAt();
        preboardingFollowUpCriteria.applicationId();
        preboardingFollowUpCriteria.distinct();
    }

    private static Condition<PreboardingFollowUpCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCandidateId()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPlannedAt()) &&
                condition.apply(criteria.getContactedAt()) &&
                condition.apply(criteria.getOwnerUsername()) &&
                condition.apply(criteria.getContactMethod()) &&
                condition.apply(criteria.getCandidateIntent()) &&
                condition.apply(criteria.getRiskLevel()) &&
                condition.apply(criteria.getNextContactAt()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PreboardingFollowUpCriteria> copyFiltersAre(
        PreboardingFollowUpCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCandidateId(), copy.getCandidateId()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPlannedAt(), copy.getPlannedAt()) &&
                condition.apply(criteria.getContactedAt(), copy.getContactedAt()) &&
                condition.apply(criteria.getOwnerUsername(), copy.getOwnerUsername()) &&
                condition.apply(criteria.getContactMethod(), copy.getContactMethod()) &&
                condition.apply(criteria.getCandidateIntent(), copy.getCandidateIntent()) &&
                condition.apply(criteria.getRiskLevel(), copy.getRiskLevel()) &&
                condition.apply(criteria.getNextContactAt(), copy.getNextContactAt()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
