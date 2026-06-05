package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SkillLevelCriteriaTest {

    @Test
    void newSkillLevelCriteriaHasAllFiltersNullTest() {
        var skillLevelCriteria = new SkillLevelCriteria();
        assertThat(skillLevelCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void skillLevelCriteriaFluentMethodsCreatesFiltersTest() {
        var skillLevelCriteria = new SkillLevelCriteria();

        setAllFilters(skillLevelCriteria);

        assertThat(skillLevelCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void skillLevelCriteriaCopyCreatesNullFilterTest() {
        var skillLevelCriteria = new SkillLevelCriteria();
        var copy = skillLevelCriteria.copy();

        assertThat(skillLevelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(skillLevelCriteria)
        );
    }

    @Test
    void skillLevelCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var skillLevelCriteria = new SkillLevelCriteria();
        setAllFilters(skillLevelCriteria);

        var copy = skillLevelCriteria.copy();

        assertThat(skillLevelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(skillLevelCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var skillLevelCriteria = new SkillLevelCriteria();

        assertThat(skillLevelCriteria).hasToString("SkillLevelCriteria{}");
    }

    private static void setAllFilters(SkillLevelCriteria skillLevelCriteria) {
        skillLevelCriteria.id();
        skillLevelCriteria.code();
        skillLevelCriteria.levelName();
        skillLevelCriteria.sortOrder();
        skillLevelCriteria.distinct();
    }

    private static Condition<SkillLevelCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getLevelName()) &&
                condition.apply(criteria.getSortOrder()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SkillLevelCriteria> copyFiltersAre(SkillLevelCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getLevelName(), copy.getLevelName()) &&
                condition.apply(criteria.getSortOrder(), copy.getSortOrder()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
