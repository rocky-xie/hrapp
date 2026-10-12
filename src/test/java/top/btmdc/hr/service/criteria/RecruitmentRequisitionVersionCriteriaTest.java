package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionVersionCriteriaTest {

    @Test
    void newRecruitmentRequisitionVersionCriteriaHasAllFiltersNullTest() {
        var recruitmentRequisitionVersionCriteria = new RecruitmentRequisitionVersionCriteria();
        assertThat(recruitmentRequisitionVersionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recruitmentRequisitionVersionCriteriaFluentMethodsCreatesFiltersTest() {
        var recruitmentRequisitionVersionCriteria = new RecruitmentRequisitionVersionCriteria();

        setAllFilters(recruitmentRequisitionVersionCriteria);

        assertThat(recruitmentRequisitionVersionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recruitmentRequisitionVersionCriteriaCopyCreatesNullFilterTest() {
        var recruitmentRequisitionVersionCriteria = new RecruitmentRequisitionVersionCriteria();
        var copy = recruitmentRequisitionVersionCriteria.copy();

        assertThat(recruitmentRequisitionVersionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionVersionCriteria)
        );
    }

    @Test
    void recruitmentRequisitionVersionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recruitmentRequisitionVersionCriteria = new RecruitmentRequisitionVersionCriteria();
        setAllFilters(recruitmentRequisitionVersionCriteria);

        var copy = recruitmentRequisitionVersionCriteria.copy();

        assertThat(recruitmentRequisitionVersionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionVersionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recruitmentRequisitionVersionCriteria = new RecruitmentRequisitionVersionCriteria();

        assertThat(recruitmentRequisitionVersionCriteria).hasToString("RecruitmentRequisitionVersionCriteria{}");
    }

    private static void setAllFilters(RecruitmentRequisitionVersionCriteria recruitmentRequisitionVersionCriteria) {
        recruitmentRequisitionVersionCriteria.id();
        recruitmentRequisitionVersionCriteria.versionNumber();
        recruitmentRequisitionVersionCriteria.lockedAt();
        recruitmentRequisitionVersionCriteria.lockedBy();
        recruitmentRequisitionVersionCriteria.applicationsId();
        recruitmentRequisitionVersionCriteria.requisitionId();
        recruitmentRequisitionVersionCriteria.distinct();
    }

    private static Condition<RecruitmentRequisitionVersionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVersionNumber()) &&
                condition.apply(criteria.getLockedAt()) &&
                condition.apply(criteria.getLockedBy()) &&
                condition.apply(criteria.getApplicationsId()) &&
                condition.apply(criteria.getRequisitionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecruitmentRequisitionVersionCriteria> copyFiltersAre(
        RecruitmentRequisitionVersionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVersionNumber(), copy.getVersionNumber()) &&
                condition.apply(criteria.getLockedAt(), copy.getLockedAt()) &&
                condition.apply(criteria.getLockedBy(), copy.getLockedBy()) &&
                condition.apply(criteria.getApplicationsId(), copy.getApplicationsId()) &&
                condition.apply(criteria.getRequisitionId(), copy.getRequisitionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
