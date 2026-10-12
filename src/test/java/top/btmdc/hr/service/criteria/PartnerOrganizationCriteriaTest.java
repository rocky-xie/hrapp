package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartnerOrganizationCriteriaTest {

    @Test
    void newPartnerOrganizationCriteriaHasAllFiltersNullTest() {
        var partnerOrganizationCriteria = new PartnerOrganizationCriteria();
        assertThat(partnerOrganizationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partnerOrganizationCriteriaFluentMethodsCreatesFiltersTest() {
        var partnerOrganizationCriteria = new PartnerOrganizationCriteria();

        setAllFilters(partnerOrganizationCriteria);

        assertThat(partnerOrganizationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partnerOrganizationCriteriaCopyCreatesNullFilterTest() {
        var partnerOrganizationCriteria = new PartnerOrganizationCriteria();
        var copy = partnerOrganizationCriteria.copy();

        assertThat(partnerOrganizationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partnerOrganizationCriteria)
        );
    }

    @Test
    void partnerOrganizationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partnerOrganizationCriteria = new PartnerOrganizationCriteria();
        setAllFilters(partnerOrganizationCriteria);

        var copy = partnerOrganizationCriteria.copy();

        assertThat(partnerOrganizationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partnerOrganizationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partnerOrganizationCriteria = new PartnerOrganizationCriteria();

        assertThat(partnerOrganizationCriteria).hasToString("PartnerOrganizationCriteria{}");
    }

    private static void setAllFilters(PartnerOrganizationCriteria partnerOrganizationCriteria) {
        partnerOrganizationCriteria.id();
        partnerOrganizationCriteria.partnerCode();
        partnerOrganizationCriteria.name();
        partnerOrganizationCriteria.dispatchEnabled();
        partnerOrganizationCriteria.defaultObservationDays();
        partnerOrganizationCriteria.active();
        partnerOrganizationCriteria.distinct();
    }

    private static Condition<PartnerOrganizationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPartnerCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDispatchEnabled()) &&
                condition.apply(criteria.getDefaultObservationDays()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartnerOrganizationCriteria> copyFiltersAre(
        PartnerOrganizationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPartnerCode(), copy.getPartnerCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDispatchEnabled(), copy.getDispatchEnabled()) &&
                condition.apply(criteria.getDefaultObservationDays(), copy.getDefaultObservationDays()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
