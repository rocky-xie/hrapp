package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SkillUpgradeRecordCriteriaTest {

    @Test
    void newSkillUpgradeRecordCriteriaHasAllFiltersNullTest() {
        var skillUpgradeRecordCriteria = new SkillUpgradeRecordCriteria();
        assertThat(skillUpgradeRecordCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void skillUpgradeRecordCriteriaFluentMethodsCreatesFiltersTest() {
        var skillUpgradeRecordCriteria = new SkillUpgradeRecordCriteria();

        setAllFilters(skillUpgradeRecordCriteria);

        assertThat(skillUpgradeRecordCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void skillUpgradeRecordCriteriaCopyCreatesNullFilterTest() {
        var skillUpgradeRecordCriteria = new SkillUpgradeRecordCriteria();
        var copy = skillUpgradeRecordCriteria.copy();

        assertThat(skillUpgradeRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(skillUpgradeRecordCriteria)
        );
    }

    @Test
    void skillUpgradeRecordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var skillUpgradeRecordCriteria = new SkillUpgradeRecordCriteria();
        setAllFilters(skillUpgradeRecordCriteria);

        var copy = skillUpgradeRecordCriteria.copy();

        assertThat(skillUpgradeRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(skillUpgradeRecordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var skillUpgradeRecordCriteria = new SkillUpgradeRecordCriteria();

        assertThat(skillUpgradeRecordCriteria).hasToString("SkillUpgradeRecordCriteria{}");
    }

    private static void setAllFilters(SkillUpgradeRecordCriteria skillUpgradeRecordCriteria) {
        skillUpgradeRecordCriteria.id();
        skillUpgradeRecordCriteria.changeType();
        skillUpgradeRecordCriteria.changeDate();
        skillUpgradeRecordCriteria.reason();
        skillUpgradeRecordCriteria.beforeLevelLabel();
        skillUpgradeRecordCriteria.afterLevelLabel();
        skillUpgradeRecordCriteria.personId();
        skillUpgradeRecordCriteria.skillId();
        skillUpgradeRecordCriteria.oldLevelId();
        skillUpgradeRecordCriteria.newLevelId();
        skillUpgradeRecordCriteria.assessorId();
        skillUpgradeRecordCriteria.distinct();
    }

    private static Condition<SkillUpgradeRecordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getChangeType()) &&
                condition.apply(criteria.getChangeDate()) &&
                condition.apply(criteria.getReason()) &&
                condition.apply(criteria.getBeforeLevelLabel()) &&
                condition.apply(criteria.getAfterLevelLabel()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getOldLevelId()) &&
                condition.apply(criteria.getNewLevelId()) &&
                condition.apply(criteria.getAssessorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SkillUpgradeRecordCriteria> copyFiltersAre(
        SkillUpgradeRecordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getChangeType(), copy.getChangeType()) &&
                condition.apply(criteria.getChangeDate(), copy.getChangeDate()) &&
                condition.apply(criteria.getReason(), copy.getReason()) &&
                condition.apply(criteria.getBeforeLevelLabel(), copy.getBeforeLevelLabel()) &&
                condition.apply(criteria.getAfterLevelLabel(), copy.getAfterLevelLabel()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getOldLevelId(), copy.getOldLevelId()) &&
                condition.apply(criteria.getNewLevelId(), copy.getNewLevelId()) &&
                condition.apply(criteria.getAssessorId(), copy.getAssessorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
