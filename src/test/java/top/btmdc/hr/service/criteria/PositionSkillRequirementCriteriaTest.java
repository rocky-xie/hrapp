package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionSkillRequirementCriteriaTest {

    @Test
    void newPositionSkillRequirementCriteriaHasAllFiltersNullTest() {
        var positionSkillRequirementCriteria = new PositionSkillRequirementCriteria();
        assertThat(positionSkillRequirementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionSkillRequirementCriteriaFluentMethodsCreatesFiltersTest() {
        var positionSkillRequirementCriteria = new PositionSkillRequirementCriteria();

        setAllFilters(positionSkillRequirementCriteria);

        assertThat(positionSkillRequirementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionSkillRequirementCriteriaCopyCreatesNullFilterTest() {
        var positionSkillRequirementCriteria = new PositionSkillRequirementCriteria();
        var copy = positionSkillRequirementCriteria.copy();

        assertThat(positionSkillRequirementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionSkillRequirementCriteria)
        );
    }

    @Test
    void positionSkillRequirementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionSkillRequirementCriteria = new PositionSkillRequirementCriteria();
        setAllFilters(positionSkillRequirementCriteria);

        var copy = positionSkillRequirementCriteria.copy();

        assertThat(positionSkillRequirementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionSkillRequirementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionSkillRequirementCriteria = new PositionSkillRequirementCriteria();

        assertThat(positionSkillRequirementCriteria).hasToString("PositionSkillRequirementCriteria{}");
    }

    private static void setAllFilters(PositionSkillRequirementCriteria positionSkillRequirementCriteria) {
        positionSkillRequirementCriteria.id();
        positionSkillRequirementCriteria.importance();
        positionSkillRequirementCriteria.positionId();
        positionSkillRequirementCriteria.skillId();
        positionSkillRequirementCriteria.requiredLevelId();
        positionSkillRequirementCriteria.preferredLevelId();
        positionSkillRequirementCriteria.distinct();
    }

    private static Condition<PositionSkillRequirementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getImportance()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getRequiredLevelId()) &&
                condition.apply(criteria.getPreferredLevelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionSkillRequirementCriteria> copyFiltersAre(
        PositionSkillRequirementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getImportance(), copy.getImportance()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getRequiredLevelId(), copy.getRequiredLevelId()) &&
                condition.apply(criteria.getPreferredLevelId(), copy.getPreferredLevelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
