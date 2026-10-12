package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SelectionActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SelectionActivity getSelectionActivitySample1() {
        return new SelectionActivity()
            .id(1L)
            .meetingRoomId(1L)
            .location("location1")
            .status("status1")
            .scheduleVersion(1)
            .externalCalendarProvider("externalCalendarProvider1")
            .externalEventId("externalEventId1");
    }

    public static SelectionActivity getSelectionActivitySample2() {
        return new SelectionActivity()
            .id(2L)
            .meetingRoomId(2L)
            .location("location2")
            .status("status2")
            .scheduleVersion(2)
            .externalCalendarProvider("externalCalendarProvider2")
            .externalEventId("externalEventId2");
    }

    public static SelectionActivity getSelectionActivityRandomSampleGenerator() {
        return new SelectionActivity()
            .id(longCount.incrementAndGet())
            .meetingRoomId(longCount.incrementAndGet())
            .location(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .scheduleVersion(intCount.incrementAndGet())
            .externalCalendarProvider(UUID.randomUUID().toString())
            .externalEventId(UUID.randomUUID().toString());
    }
}
