package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DispatchAssignmentCriteriaTest {

    @Test
    void newDispatchAssignmentCriteriaHasAllFiltersNullTest() {
        var dispatchAssignmentCriteria = new DispatchAssignmentCriteria();
        assertThat(dispatchAssignmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dispatchAssignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var dispatchAssignmentCriteria = new DispatchAssignmentCriteria();

        setAllFilters(dispatchAssignmentCriteria);

        assertThat(dispatchAssignmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dispatchAssignmentCriteriaCopyCreatesNullFilterTest() {
        var dispatchAssignmentCriteria = new DispatchAssignmentCriteria();
        var copy = dispatchAssignmentCriteria.copy();

        assertThat(dispatchAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dispatchAssignmentCriteria)
        );
    }

    @Test
    void dispatchAssignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dispatchAssignmentCriteria = new DispatchAssignmentCriteria();
        setAllFilters(dispatchAssignmentCriteria);

        var copy = dispatchAssignmentCriteria.copy();

        assertThat(dispatchAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dispatchAssignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dispatchAssignmentCriteria = new DispatchAssignmentCriteria();

        assertThat(dispatchAssignmentCriteria).hasToString("DispatchAssignmentCriteria{}");
    }

    private static void setAllFilters(DispatchAssignmentCriteria dispatchAssignmentCriteria) {
        dispatchAssignmentCriteria.id();
        dispatchAssignmentCriteria.candidateId();
        dispatchAssignmentCriteria.personId();
        dispatchAssignmentCriteria.partnerOrganizationId();
        dispatchAssignmentCriteria.department();
        dispatchAssignmentCriteria.positionId();
        dispatchAssignmentCriteria.startDate();
        dispatchAssignmentCriteria.expectedEndDate();
        dispatchAssignmentCriteria.observationDays();
        dispatchAssignmentCriteria.status();
        dispatchAssignmentCriteria.finalDecision();
        dispatchAssignmentCriteria.reviewsId();
        dispatchAssignmentCriteria.applicationId();
        dispatchAssignmentCriteria.distinct();
    }

    private static Condition<DispatchAssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCandidateId()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPartnerOrganizationId()) &&
                condition.apply(criteria.getDepartment()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getExpectedEndDate()) &&
                condition.apply(criteria.getObservationDays()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getFinalDecision()) &&
                condition.apply(criteria.getReviewsId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DispatchAssignmentCriteria> copyFiltersAre(
        DispatchAssignmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCandidateId(), copy.getCandidateId()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPartnerOrganizationId(), copy.getPartnerOrganizationId()) &&
                condition.apply(criteria.getDepartment(), copy.getDepartment()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getExpectedEndDate(), copy.getExpectedEndDate()) &&
                condition.apply(criteria.getObservationDays(), copy.getObservationDays()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getFinalDecision(), copy.getFinalDecision()) &&
                condition.apply(criteria.getReviewsId(), copy.getReviewsId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
