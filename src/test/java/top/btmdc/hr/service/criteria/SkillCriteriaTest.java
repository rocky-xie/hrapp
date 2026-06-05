package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SkillCriteriaTest {

    @Test
    void newSkillCriteriaHasAllFiltersNullTest() {
        var skillCriteria = new SkillCriteria();
        assertThat(skillCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void skillCriteriaFluentMethodsCreatesFiltersTest() {
        var skillCriteria = new SkillCriteria();

        setAllFilters(skillCriteria);

        assertThat(skillCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void skillCriteriaCopyCreatesNullFilterTest() {
        var skillCriteria = new SkillCriteria();
        var copy = skillCriteria.copy();

        assertThat(skillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(skillCriteria)
        );
    }

    @Test
    void skillCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var skillCriteria = new SkillCriteria();
        setAllFilters(skillCriteria);

        var copy = skillCriteria.copy();

        assertThat(skillCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(skillCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var skillCriteria = new SkillCriteria();

        assertThat(skillCriteria).hasToString("SkillCriteria{}");
    }

    private static void setAllFilters(SkillCriteria skillCriteria) {
        skillCriteria.id();
        skillCriteria.skillCode();
        skillCriteria.skillName();
        skillCriteria.skillType();
        skillCriteria.measurableFlag();
        skillCriteria.evidenceType();
        skillCriteria.distinct();
    }

    private static Condition<SkillCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSkillCode()) &&
                condition.apply(criteria.getSkillName()) &&
                condition.apply(criteria.getSkillType()) &&
                condition.apply(criteria.getMeasurableFlag()) &&
                condition.apply(criteria.getEvidenceType()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SkillCriteria> copyFiltersAre(SkillCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSkillCode(), copy.getSkillCode()) &&
                condition.apply(criteria.getSkillName(), copy.getSkillName()) &&
                condition.apply(criteria.getSkillType(), copy.getSkillType()) &&
                condition.apply(criteria.getMeasurableFlag(), copy.getMeasurableFlag()) &&
                condition.apply(criteria.getEvidenceType(), copy.getEvidenceType()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
