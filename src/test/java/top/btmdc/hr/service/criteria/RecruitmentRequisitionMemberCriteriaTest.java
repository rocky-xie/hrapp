package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionMemberCriteriaTest {

    @Test
    void newRecruitmentRequisitionMemberCriteriaHasAllFiltersNullTest() {
        var recruitmentRequisitionMemberCriteria = new RecruitmentRequisitionMemberCriteria();
        assertThat(recruitmentRequisitionMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recruitmentRequisitionMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var recruitmentRequisitionMemberCriteria = new RecruitmentRequisitionMemberCriteria();

        setAllFilters(recruitmentRequisitionMemberCriteria);

        assertThat(recruitmentRequisitionMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recruitmentRequisitionMemberCriteriaCopyCreatesNullFilterTest() {
        var recruitmentRequisitionMemberCriteria = new RecruitmentRequisitionMemberCriteria();
        var copy = recruitmentRequisitionMemberCriteria.copy();

        assertThat(recruitmentRequisitionMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionMemberCriteria)
        );
    }

    @Test
    void recruitmentRequisitionMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recruitmentRequisitionMemberCriteria = new RecruitmentRequisitionMemberCriteria();
        setAllFilters(recruitmentRequisitionMemberCriteria);

        var copy = recruitmentRequisitionMemberCriteria.copy();

        assertThat(recruitmentRequisitionMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recruitmentRequisitionMemberCriteria = new RecruitmentRequisitionMemberCriteria();

        assertThat(recruitmentRequisitionMemberCriteria).hasToString("RecruitmentRequisitionMemberCriteria{}");
    }

    private static void setAllFilters(RecruitmentRequisitionMemberCriteria recruitmentRequisitionMemberCriteria) {
        recruitmentRequisitionMemberCriteria.id();
        recruitmentRequisitionMemberCriteria.username();
        recruitmentRequisitionMemberCriteria.memberType();
        recruitmentRequisitionMemberCriteria.active();
        recruitmentRequisitionMemberCriteria.createdAt();
        recruitmentRequisitionMemberCriteria.removedAt();
        recruitmentRequisitionMemberCriteria.requisitionId();
        recruitmentRequisitionMemberCriteria.distinct();
    }

    private static Condition<RecruitmentRequisitionMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getMemberType()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getRemovedAt()) &&
                condition.apply(criteria.getRequisitionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecruitmentRequisitionMemberCriteria> copyFiltersAre(
        RecruitmentRequisitionMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getMemberType(), copy.getMemberType()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getRemovedAt(), copy.getRemovedAt()) &&
                condition.apply(criteria.getRequisitionId(), copy.getRequisitionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
