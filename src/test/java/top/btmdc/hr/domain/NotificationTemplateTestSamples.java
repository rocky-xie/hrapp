package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NotificationTemplate getNotificationTemplateSample1() {
        return new NotificationTemplate()
            .id(1L)
            .eventType("eventType1")
            .language("language1")
            .versionNumber(1)
            .subjectTemplate("subjectTemplate1");
    }

    public static NotificationTemplate getNotificationTemplateSample2() {
        return new NotificationTemplate()
            .id(2L)
            .eventType("eventType2")
            .language("language2")
            .versionNumber(2)
            .subjectTemplate("subjectTemplate2");
    }

    public static NotificationTemplate getNotificationTemplateRandomSampleGenerator() {
        return new NotificationTemplate()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .versionNumber(intCount.incrementAndGet())
            .subjectTemplate(UUID.randomUUID().toString());
    }
}
