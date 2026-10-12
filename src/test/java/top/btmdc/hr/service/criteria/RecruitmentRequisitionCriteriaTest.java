package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionCriteriaTest {

    @Test
    void newRecruitmentRequisitionCriteriaHasAllFiltersNullTest() {
        var recruitmentRequisitionCriteria = new RecruitmentRequisitionCriteria();
        assertThat(recruitmentRequisitionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recruitmentRequisitionCriteriaFluentMethodsCreatesFiltersTest() {
        var recruitmentRequisitionCriteria = new RecruitmentRequisitionCriteria();

        setAllFilters(recruitmentRequisitionCriteria);

        assertThat(recruitmentRequisitionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recruitmentRequisitionCriteriaCopyCreatesNullFilterTest() {
        var recruitmentRequisitionCriteria = new RecruitmentRequisitionCriteria();
        var copy = recruitmentRequisitionCriteria.copy();

        assertThat(recruitmentRequisitionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionCriteria)
        );
    }

    @Test
    void recruitmentRequisitionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recruitmentRequisitionCriteria = new RecruitmentRequisitionCriteria();
        setAllFilters(recruitmentRequisitionCriteria);

        var copy = recruitmentRequisitionCriteria.copy();

        assertThat(recruitmentRequisitionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentRequisitionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recruitmentRequisitionCriteria = new RecruitmentRequisitionCriteria();

        assertThat(recruitmentRequisitionCriteria).hasToString("RecruitmentRequisitionCriteria{}");
    }

    private static void setAllFilters(RecruitmentRequisitionCriteria recruitmentRequisitionCriteria) {
        recruitmentRequisitionCriteria.id();
        recruitmentRequisitionCriteria.requisitionCode();
        recruitmentRequisitionCriteria.title();
        recruitmentRequisitionCriteria.department();
        recruitmentRequisitionCriteria.positionId();
        recruitmentRequisitionCriteria.recruitmentType();
        recruitmentRequisitionCriteria.lifecycle();
        recruitmentRequisitionCriteria.editStatus();
        recruitmentRequisitionCriteria.plannedHeadcount();
        recruitmentRequisitionCriteria.hiredCount();
        recruitmentRequisitionCriteria.checkedInCount();
        recruitmentRequisitionCriteria.priority();
        recruitmentRequisitionCriteria.targetJoinDate();
        recruitmentRequisitionCriteria.desiredStartDate();
        recruitmentRequisitionCriteria.desiredEndDate();
        recruitmentRequisitionCriteria.currentVersion();
        recruitmentRequisitionCriteria.lockedAt();
        recruitmentRequisitionCriteria.lockedBy();
        recruitmentRequisitionCriteria.unlockedAt();
        recruitmentRequisitionCriteria.versionsId();
        recruitmentRequisitionCriteria.membersId();
        recruitmentRequisitionCriteria.applicationsId();
        recruitmentRequisitionCriteria.distinct();
    }

    private static Condition<RecruitmentRequisitionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRequisitionCode()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDepartment()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getRecruitmentType()) &&
                condition.apply(criteria.getLifecycle()) &&
                condition.apply(criteria.getEditStatus()) &&
                condition.apply(criteria.getPlannedHeadcount()) &&
                condition.apply(criteria.getHiredCount()) &&
                condition.apply(criteria.getCheckedInCount()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getTargetJoinDate()) &&
                condition.apply(criteria.getDesiredStartDate()) &&
                condition.apply(criteria.getDesiredEndDate()) &&
                condition.apply(criteria.getCurrentVersion()) &&
                condition.apply(criteria.getLockedAt()) &&
                condition.apply(criteria.getLockedBy()) &&
                condition.apply(criteria.getUnlockedAt()) &&
                condition.apply(criteria.getVersionsId()) &&
                condition.apply(criteria.getMembersId()) &&
                condition.apply(criteria.getApplicationsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecruitmentRequisitionCriteria> copyFiltersAre(
        RecruitmentRequisitionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRequisitionCode(), copy.getRequisitionCode()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDepartment(), copy.getDepartment()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getRecruitmentType(), copy.getRecruitmentType()) &&
                condition.apply(criteria.getLifecycle(), copy.getLifecycle()) &&
                condition.apply(criteria.getEditStatus(), copy.getEditStatus()) &&
                condition.apply(criteria.getPlannedHeadcount(), copy.getPlannedHeadcount()) &&
                condition.apply(criteria.getHiredCount(), copy.getHiredCount()) &&
                condition.apply(criteria.getCheckedInCount(), copy.getCheckedInCount()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getTargetJoinDate(), copy.getTargetJoinDate()) &&
                condition.apply(criteria.getDesiredStartDate(), copy.getDesiredStartDate()) &&
                condition.apply(criteria.getDesiredEndDate(), copy.getDesiredEndDate()) &&
                condition.apply(criteria.getCurrentVersion(), copy.getCurrentVersion()) &&
                condition.apply(criteria.getLockedAt(), copy.getLockedAt()) &&
                condition.apply(criteria.getLockedBy(), copy.getLockedBy()) &&
                condition.apply(criteria.getUnlockedAt(), copy.getUnlockedAt()) &&
                condition.apply(criteria.getVersionsId(), copy.getVersionsId()) &&
                condition.apply(criteria.getMembersId(), copy.getMembersId()) &&
                condition.apply(criteria.getApplicationsId(), copy.getApplicationsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
