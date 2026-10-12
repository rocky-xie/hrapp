package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActionItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ActionItem getActionItemSample1() {
        return new ActionItem().id(1L).sourceId(1L).sourceEntityType("sourceEntityType1").description("description1").assignee("assignee1");
    }

    public static ActionItem getActionItemSample2() {
        return new ActionItem().id(2L).sourceId(2L).sourceEntityType("sourceEntityType2").description("description2").assignee("assignee2");
    }

    public static ActionItem getActionItemRandomSampleGenerator() {
        return new ActionItem()
            .id(longCount.incrementAndGet())
            .sourceId(longCount.incrementAndGet())
            .sourceEntityType(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .assignee(UUID.randomUUID().toString());
    }
}
