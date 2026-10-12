package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecruitmentEvaluationCriteriaTest {

    @Test
    void newRecruitmentEvaluationCriteriaHasAllFiltersNullTest() {
        var recruitmentEvaluationCriteria = new RecruitmentEvaluationCriteria();
        assertThat(recruitmentEvaluationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recruitmentEvaluationCriteriaFluentMethodsCreatesFiltersTest() {
        var recruitmentEvaluationCriteria = new RecruitmentEvaluationCriteria();

        setAllFilters(recruitmentEvaluationCriteria);

        assertThat(recruitmentEvaluationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recruitmentEvaluationCriteriaCopyCreatesNullFilterTest() {
        var recruitmentEvaluationCriteria = new RecruitmentEvaluationCriteria();
        var copy = recruitmentEvaluationCriteria.copy();

        assertThat(recruitmentEvaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentEvaluationCriteria)
        );
    }

    @Test
    void recruitmentEvaluationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recruitmentEvaluationCriteria = new RecruitmentEvaluationCriteria();
        setAllFilters(recruitmentEvaluationCriteria);

        var copy = recruitmentEvaluationCriteria.copy();

        assertThat(recruitmentEvaluationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recruitmentEvaluationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recruitmentEvaluationCriteria = new RecruitmentEvaluationCriteria();

        assertThat(recruitmentEvaluationCriteria).hasToString("RecruitmentEvaluationCriteria{}");
    }

    private static void setAllFilters(RecruitmentEvaluationCriteria recruitmentEvaluationCriteria) {
        recruitmentEvaluationCriteria.id();
        recruitmentEvaluationCriteria.stageType();
        recruitmentEvaluationCriteria.activityId();
        recruitmentEvaluationCriteria.templateVersionId();
        recruitmentEvaluationCriteria.evaluatorUsername();
        recruitmentEvaluationCriteria.recordedByUsername();
        recruitmentEvaluationCriteria.status();
        recruitmentEvaluationCriteria.overallRecommendation();
        recruitmentEvaluationCriteria.totalScore();
        recruitmentEvaluationCriteria.evaluatedAt();
        recruitmentEvaluationCriteria.submittedAt();
        recruitmentEvaluationCriteria.publishedAt();
        recruitmentEvaluationCriteria.correctionOfId();
        recruitmentEvaluationCriteria.applicationId();
        recruitmentEvaluationCriteria.distinct();
    }

    private static Condition<RecruitmentEvaluationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStageType()) &&
                condition.apply(criteria.getActivityId()) &&
                condition.apply(criteria.getTemplateVersionId()) &&
                condition.apply(criteria.getEvaluatorUsername()) &&
                condition.apply(criteria.getRecordedByUsername()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getOverallRecommendation()) &&
                condition.apply(criteria.getTotalScore()) &&
                condition.apply(criteria.getEvaluatedAt()) &&
                condition.apply(criteria.getSubmittedAt()) &&
                condition.apply(criteria.getPublishedAt()) &&
                condition.apply(criteria.getCorrectionOfId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecruitmentEvaluationCriteria> copyFiltersAre(
        RecruitmentEvaluationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStageType(), copy.getStageType()) &&
                condition.apply(criteria.getActivityId(), copy.getActivityId()) &&
                condition.apply(criteria.getTemplateVersionId(), copy.getTemplateVersionId()) &&
                condition.apply(criteria.getEvaluatorUsername(), copy.getEvaluatorUsername()) &&
                condition.apply(criteria.getRecordedByUsername(), copy.getRecordedByUsername()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getOverallRecommendation(), copy.getOverallRecommendation()) &&
                condition.apply(criteria.getTotalScore(), copy.getTotalScore()) &&
                condition.apply(criteria.getEvaluatedAt(), copy.getEvaluatedAt()) &&
                condition.apply(criteria.getSubmittedAt(), copy.getSubmittedAt()) &&
                condition.apply(criteria.getPublishedAt(), copy.getPublishedAt()) &&
                condition.apply(criteria.getCorrectionOfId(), copy.getCorrectionOfId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
