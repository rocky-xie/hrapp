package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecruitmentApplicationCriteriaTest {

    @Test
    void newRecruitmentApplicationCriteriaHasAllFiltersNullTest() {
        var recruitmentApplicationCriteria = new RecruitmentApplicationCriteria();
        assertThat(recruitmentApplicationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recruitmentApplicationCriteriaFluentMethodsCreatesFiltersTest() {
        var recruitmentApplicationCriteria = new RecruitmentApplicationCriteria();

        setAllFilters(recruitmentApplicationCriteria);

        assertThat(recruitmentApplicationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recruitmentApplicationCriteriaCopyCreatesNullFilterTest() {
        var recruitmentApplicationCriteria = new RecruitmentApplicationCriteria();
        var copy = recruitmentApplicationCriteria.copy();

        assertThat(recruitmentApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentApplicationCriteria)
        );
    }

    @Test
    void recruitmentApplicationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recruitmentApplicationCriteria = new RecruitmentApplicationCriteria();
        setAllFilters(recruitmentApplicationCriteria);

        var copy = recruitmentApplicationCriteria.copy();

        assertThat(recruitmentApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentApplicationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recruitmentApplicationCriteria = new RecruitmentApplicationCriteria();

        assertThat(recruitmentApplicationCriteria).hasToString("RecruitmentApplicationCriteria{}");
    }

    private static void setAllFilters(RecruitmentApplicationCriteria recruitmentApplicationCriteria) {
        recruitmentApplicationCriteria.id();
        recruitmentApplicationCriteria.applicationCode();
        recruitmentApplicationCriteria.recruitmentType();
        recruitmentApplicationCriteria.currentStage();
        recruitmentApplicationCriteria.status();
        recruitmentApplicationCriteria.decision();
        recruitmentApplicationCriteria.sourceType();
        recruitmentApplicationCriteria.sourceDetail();
        recruitmentApplicationCriteria.batchName();
        recruitmentApplicationCriteria.appliedAt();
        recruitmentApplicationCriteria.closedAt();
        recruitmentApplicationCriteria.retentionUntil();
        recruitmentApplicationCriteria.convertedPersonId();
        recruitmentApplicationCriteria.convertedAt();
        recruitmentApplicationCriteria.stagesId();
        recruitmentApplicationCriteria.eventsId();
        recruitmentApplicationCriteria.activitiesId();
        recruitmentApplicationCriteria.evaluationsId();
        recruitmentApplicationCriteria.decisionsId();
        recruitmentApplicationCriteria.messagesId();
        recruitmentApplicationCriteria.followUpsId();
        recruitmentApplicationCriteria.dispatchAssignmentsId();
        recruitmentApplicationCriteria.candidateId();
        recruitmentApplicationCriteria.requisitionId();
        recruitmentApplicationCriteria.requisitionVersionId();
        recruitmentApplicationCriteria.distinct();
    }

    private static Condition<RecruitmentApplicationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getApplicationCode()) &&
                condition.apply(criteria.getRecruitmentType()) &&
                condition.apply(criteria.getCurrentStage()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDecision()) &&
                condition.apply(criteria.getSourceType()) &&
                condition.apply(criteria.getSourceDetail()) &&
                condition.apply(criteria.getBatchName()) &&
                condition.apply(criteria.getAppliedAt()) &&
                condition.apply(criteria.getClosedAt()) &&
                condition.apply(criteria.getRetentionUntil()) &&
                condition.apply(criteria.getConvertedPersonId()) &&
                condition.apply(criteria.getConvertedAt()) &&
                condition.apply(criteria.getStagesId()) &&
                condition.apply(criteria.getEventsId()) &&
                condition.apply(criteria.getActivitiesId()) &&
                condition.apply(criteria.getEvaluationsId()) &&
                condition.apply(criteria.getDecisionsId()) &&
                condition.apply(criteria.getMessagesId()) &&
                condition.apply(criteria.getFollowUpsId()) &&
                condition.apply(criteria.getDispatchAssignmentsId()) &&
                condition.apply(criteria.getCandidateId()) &&
                condition.apply(criteria.getRequisitionId()) &&
                condition.apply(criteria.getRequisitionVersionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecruitmentApplicationCriteria> copyFiltersAre(
        RecruitmentApplicationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getApplicationCode(), copy.getApplicationCode()) &&
                condition.apply(criteria.getRecruitmentType(), copy.getRecruitmentType()) &&
                condition.apply(criteria.getCurrentStage(), copy.getCurrentStage()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDecision(), copy.getDecision()) &&
                condition.apply(criteria.getSourceType(), copy.getSourceType()) &&
                condition.apply(criteria.getSourceDetail(), copy.getSourceDetail()) &&
                condition.apply(criteria.getBatchName(), copy.getBatchName()) &&
                condition.apply(criteria.getAppliedAt(), copy.getAppliedAt()) &&
                condition.apply(criteria.getClosedAt(), copy.getClosedAt()) &&
                condition.apply(criteria.getRetentionUntil(), copy.getRetentionUntil()) &&
                condition.apply(criteria.getConvertedPersonId(), copy.getConvertedPersonId()) &&
                condition.apply(criteria.getConvertedAt(), copy.getConvertedAt()) &&
                condition.apply(criteria.getStagesId(), copy.getStagesId()) &&
                condition.apply(criteria.getEventsId(), copy.getEventsId()) &&
                condition.apply(criteria.getActivitiesId(), copy.getActivitiesId()) &&
                condition.apply(criteria.getEvaluationsId(), copy.getEvaluationsId()) &&
                condition.apply(criteria.getDecisionsId(), copy.getDecisionsId()) &&
                condition.apply(criteria.getMessagesId(), copy.getMessagesId()) &&
                condition.apply(criteria.getFollowUpsId(), copy.getFollowUpsId()) &&
                condition.apply(criteria.getDispatchAssignmentsId(), copy.getDispatchAssignmentsId()) &&
                condition.apply(criteria.getCandidateId(), copy.getCandidateId()) &&
                condition.apply(criteria.getRequisitionId(), copy.getRequisitionId()) &&
                condition.apply(criteria.getRequisitionVersionId(), copy.getRequisitionVersionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
