package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ApplicationEvent getApplicationEventSample1() {
        return new ApplicationEvent().id(1L).eventType("eventType1").previousValue("previousValue1").newValue("newValue1").actor("actor1");
    }

    public static ApplicationEvent getApplicationEventSample2() {
        return new ApplicationEvent().id(2L).eventType("eventType2").previousValue("previousValue2").newValue("newValue2").actor("actor2");
    }

    public static ApplicationEvent getApplicationEventRandomSampleGenerator() {
        return new ApplicationEvent()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .previousValue(UUID.randomUUID().toString())
            .newValue(UUID.randomUUID().toString())
            .actor(UUID.randomUUID().toString());
    }
}
