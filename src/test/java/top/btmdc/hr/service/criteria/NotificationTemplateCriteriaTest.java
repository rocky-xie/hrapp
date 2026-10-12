package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationTemplateCriteriaTest {

    @Test
    void newNotificationTemplateCriteriaHasAllFiltersNullTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        assertThat(notificationTemplateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();

        setAllFilters(notificationTemplateCriteria);

        assertThat(notificationTemplateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationTemplateCriteriaCopyCreatesNullFilterTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        var copy = notificationTemplateCriteria.copy();

        assertThat(notificationTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationTemplateCriteria)
        );
    }

    @Test
    void notificationTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();
        setAllFilters(notificationTemplateCriteria);

        var copy = notificationTemplateCriteria.copy();

        assertThat(notificationTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationTemplateCriteria = new NotificationTemplateCriteria();

        assertThat(notificationTemplateCriteria).hasToString("NotificationTemplateCriteria{}");
    }

    private static void setAllFilters(NotificationTemplateCriteria notificationTemplateCriteria) {
        notificationTemplateCriteria.id();
        notificationTemplateCriteria.eventType();
        notificationTemplateCriteria.recruitmentType();
        notificationTemplateCriteria.language();
        notificationTemplateCriteria.versionNumber();
        notificationTemplateCriteria.subjectTemplate();
        notificationTemplateCriteria.externalSafe();
        notificationTemplateCriteria.published();
        notificationTemplateCriteria.distinct();
    }

    private static Condition<NotificationTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getRecruitmentType()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getVersionNumber()) &&
                condition.apply(criteria.getSubjectTemplate()) &&
                condition.apply(criteria.getExternalSafe()) &&
                condition.apply(criteria.getPublished()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationTemplateCriteria> copyFiltersAre(
        NotificationTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getRecruitmentType(), copy.getRecruitmentType()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getVersionNumber(), copy.getVersionNumber()) &&
                condition.apply(criteria.getSubjectTemplate(), copy.getSubjectTemplate()) &&
                condition.apply(criteria.getExternalSafe(), copy.getExternalSafe()) &&
                condition.apply(criteria.getPublished(), copy.getPublished()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
