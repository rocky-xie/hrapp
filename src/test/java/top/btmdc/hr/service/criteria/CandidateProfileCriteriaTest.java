package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CandidateProfileCriteriaTest {

    @Test
    void newCandidateProfileCriteriaHasAllFiltersNullTest() {
        var candidateProfileCriteria = new CandidateProfileCriteria();
        assertThat(candidateProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void candidateProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var candidateProfileCriteria = new CandidateProfileCriteria();

        setAllFilters(candidateProfileCriteria);

        assertThat(candidateProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void candidateProfileCriteriaCopyCreatesNullFilterTest() {
        var candidateProfileCriteria = new CandidateProfileCriteria();
        var copy = candidateProfileCriteria.copy();

        assertThat(candidateProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(candidateProfileCriteria)
        );
    }

    @Test
    void candidateProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var candidateProfileCriteria = new CandidateProfileCriteria();
        setAllFilters(candidateProfileCriteria);

        var copy = candidateProfileCriteria.copy();

        assertThat(candidateProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(candidateProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var candidateProfileCriteria = new CandidateProfileCriteria();

        assertThat(candidateProfileCriteria).hasToString("CandidateProfileCriteria{}");
    }

    private static void setAllFilters(CandidateProfileCriteria candidateProfileCriteria) {
        candidateProfileCriteria.id();
        candidateProfileCriteria.candidateDate();
        candidateProfileCriteria.cultivateDirection();
        candidateProfileCriteria.stability();
        candidateProfileCriteria.learningAbility();
        candidateProfileCriteria.communicationCoordination();
        candidateProfileCriteria.businessUnderstanding();
        candidateProfileCriteria.responsibility();
        candidateProfileCriteria.riskAwareness();
        candidateProfileCriteria.judgement();
        candidateProfileCriteria.personId();
        candidateProfileCriteria.positionId();
        candidateProfileCriteria.observerId();
        candidateProfileCriteria.distinct();
    }

    private static Condition<CandidateProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCandidateDate()) &&
                condition.apply(criteria.getCultivateDirection()) &&
                condition.apply(criteria.getStability()) &&
                condition.apply(criteria.getLearningAbility()) &&
                condition.apply(criteria.getCommunicationCoordination()) &&
                condition.apply(criteria.getBusinessUnderstanding()) &&
                condition.apply(criteria.getResponsibility()) &&
                condition.apply(criteria.getRiskAwareness()) &&
                condition.apply(criteria.getJudgement()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getObserverId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CandidateProfileCriteria> copyFiltersAre(
        CandidateProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCandidateDate(), copy.getCandidateDate()) &&
                condition.apply(criteria.getCultivateDirection(), copy.getCultivateDirection()) &&
                condition.apply(criteria.getStability(), copy.getStability()) &&
                condition.apply(criteria.getLearningAbility(), copy.getLearningAbility()) &&
                condition.apply(criteria.getCommunicationCoordination(), copy.getCommunicationCoordination()) &&
                condition.apply(criteria.getBusinessUnderstanding(), copy.getBusinessUnderstanding()) &&
                condition.apply(criteria.getResponsibility(), copy.getResponsibility()) &&
                condition.apply(criteria.getRiskAwareness(), copy.getRiskAwareness()) &&
                condition.apply(criteria.getJudgement(), copy.getJudgement()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getObserverId(), copy.getObserverId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
