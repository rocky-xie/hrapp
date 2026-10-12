package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationStageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApplicationStage getApplicationStageSample1() {
        return new ApplicationStage().id(1L).sequenceNumber(1).stageStatus("stageStatus1");
    }

    public static ApplicationStage getApplicationStageSample2() {
        return new ApplicationStage().id(2L).sequenceNumber(2).stageStatus("stageStatus2");
    }

    public static ApplicationStage getApplicationStageRandomSampleGenerator() {
        return new ApplicationStage()
            .id(longCount.incrementAndGet())
            .sequenceNumber(intCount.incrementAndGet())
            .stageStatus(UUID.randomUUID().toString());
    }
}
