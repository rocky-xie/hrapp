package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NotificationMessage getNotificationMessageSample1() {
        return new NotificationMessage()
            .id(1L)
            .eventType("eventType1")
            .templateId(1L)
            .sendMode("sendMode1")
            .finalSubject("finalSubject1")
            .retryCount(1)
            .maxRetries(1)
            .createdBy("createdBy1")
            .confirmedBy("confirmedBy1");
    }

    public static NotificationMessage getNotificationMessageSample2() {
        return new NotificationMessage()
            .id(2L)
            .eventType("eventType2")
            .templateId(2L)
            .sendMode("sendMode2")
            .finalSubject("finalSubject2")
            .retryCount(2)
            .maxRetries(2)
            .createdBy("createdBy2")
            .confirmedBy("confirmedBy2");
    }

    public static NotificationMessage getNotificationMessageRandomSampleGenerator() {
        return new NotificationMessage()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .templateId(longCount.incrementAndGet())
            .sendMode(UUID.randomUUID().toString())
            .finalSubject(UUID.randomUUID().toString())
            .retryCount(intCount.incrementAndGet())
            .maxRetries(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .confirmedBy(UUID.randomUUID().toString());
    }
}
