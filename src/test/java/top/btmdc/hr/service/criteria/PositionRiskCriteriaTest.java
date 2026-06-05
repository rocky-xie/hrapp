package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PositionRiskCriteriaTest {

    @Test
    void newPositionRiskCriteriaHasAllFiltersNullTest() {
        var positionRiskCriteria = new PositionRiskCriteria();
        assertThat(positionRiskCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void positionRiskCriteriaFluentMethodsCreatesFiltersTest() {
        var positionRiskCriteria = new PositionRiskCriteria();

        setAllFilters(positionRiskCriteria);

        assertThat(positionRiskCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void positionRiskCriteriaCopyCreatesNullFilterTest() {
        var positionRiskCriteria = new PositionRiskCriteria();
        var copy = positionRiskCriteria.copy();

        assertThat(positionRiskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(positionRiskCriteria)
        );
    }

    @Test
    void positionRiskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var positionRiskCriteria = new PositionRiskCriteria();
        setAllFilters(positionRiskCriteria);

        var copy = positionRiskCriteria.copy();

        assertThat(positionRiskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(positionRiskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var positionRiskCriteria = new PositionRiskCriteria();

        assertThat(positionRiskCriteria).hasToString("PositionRiskCriteria{}");
    }

    private static void setAllFilters(PositionRiskCriteria positionRiskCriteria) {
        positionRiskCriteria.id();
        positionRiskCriteria.riskType();
        positionRiskCriteria.riskLevel();
        positionRiskCriteria.documentStatus();
        positionRiskCriteria.backupStatus();
        positionRiskCriteria.customerOrSystemDependency();
        positionRiskCriteria.identifiedDate();
        positionRiskCriteria.targetDate();
        positionRiskCriteria.closedDate();
        positionRiskCriteria.positionId();
        positionRiskCriteria.categoryId();
        positionRiskCriteria.distinct();
    }

    private static Condition<PositionRiskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRiskType()) &&
                condition.apply(criteria.getRiskLevel()) &&
                condition.apply(criteria.getDocumentStatus()) &&
                condition.apply(criteria.getBackupStatus()) &&
                condition.apply(criteria.getCustomerOrSystemDependency()) &&
                condition.apply(criteria.getIdentifiedDate()) &&
                condition.apply(criteria.getTargetDate()) &&
                condition.apply(criteria.getClosedDate()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PositionRiskCriteria> copyFiltersAre(
        PositionRiskCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRiskType(), copy.getRiskType()) &&
                condition.apply(criteria.getRiskLevel(), copy.getRiskLevel()) &&
                condition.apply(criteria.getDocumentStatus(), copy.getDocumentStatus()) &&
                condition.apply(criteria.getBackupStatus(), copy.getBackupStatus()) &&
                condition.apply(criteria.getCustomerOrSystemDependency(), copy.getCustomerOrSystemDependency()) &&
                condition.apply(criteria.getIdentifiedDate(), copy.getIdentifiedDate()) &&
                condition.apply(criteria.getTargetDate(), copy.getTargetDate()) &&
                condition.apply(criteria.getClosedDate(), copy.getClosedDate()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
