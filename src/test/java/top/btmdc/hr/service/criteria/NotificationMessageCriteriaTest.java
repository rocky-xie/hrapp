package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationMessageCriteriaTest {

    @Test
    void newNotificationMessageCriteriaHasAllFiltersNullTest() {
        var notificationMessageCriteria = new NotificationMessageCriteria();
        assertThat(notificationMessageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void notificationMessageCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationMessageCriteria = new NotificationMessageCriteria();

        setAllFilters(notificationMessageCriteria);

        assertThat(notificationMessageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void notificationMessageCriteriaCopyCreatesNullFilterTest() {
        var notificationMessageCriteria = new NotificationMessageCriteria();
        var copy = notificationMessageCriteria.copy();

        assertThat(notificationMessageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationMessageCriteria)
        );
    }

    @Test
    void notificationMessageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationMessageCriteria = new NotificationMessageCriteria();
        setAllFilters(notificationMessageCriteria);

        var copy = notificationMessageCriteria.copy();

        assertThat(notificationMessageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(notificationMessageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationMessageCriteria = new NotificationMessageCriteria();

        assertThat(notificationMessageCriteria).hasToString("NotificationMessageCriteria{}");
    }

    private static void setAllFilters(NotificationMessageCriteria notificationMessageCriteria) {
        notificationMessageCriteria.id();
        notificationMessageCriteria.eventType();
        notificationMessageCriteria.templateId();
        notificationMessageCriteria.status();
        notificationMessageCriteria.sendMode();
        notificationMessageCriteria.finalSubject();
        notificationMessageCriteria.retryCount();
        notificationMessageCriteria.maxRetries();
        notificationMessageCriteria.createdBy();
        notificationMessageCriteria.confirmedBy();
        notificationMessageCriteria.createdAt();
        notificationMessageCriteria.confirmedAt();
        notificationMessageCriteria.sentAt();
        notificationMessageCriteria.applicationId();
        notificationMessageCriteria.distinct();
    }

    private static Condition<NotificationMessageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getTemplateId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSendMode()) &&
                condition.apply(criteria.getFinalSubject()) &&
                condition.apply(criteria.getRetryCount()) &&
                condition.apply(criteria.getMaxRetries()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getConfirmedBy()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getConfirmedAt()) &&
                condition.apply(criteria.getSentAt()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationMessageCriteria> copyFiltersAre(
        NotificationMessageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getTemplateId(), copy.getTemplateId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSendMode(), copy.getSendMode()) &&
                condition.apply(criteria.getFinalSubject(), copy.getFinalSubject()) &&
                condition.apply(criteria.getRetryCount(), copy.getRetryCount()) &&
                condition.apply(criteria.getMaxRetries(), copy.getMaxRetries()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getConfirmedBy(), copy.getConfirmedBy()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getConfirmedAt(), copy.getConfirmedAt()) &&
                condition.apply(criteria.getSentAt(), copy.getSentAt()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
