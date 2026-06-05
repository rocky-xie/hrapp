package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrainingRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static TrainingRecord getTrainingRecordSample1() {
        return new TrainingRecord().id(1L).topic("topic1");
    }

    public static TrainingRecord getTrainingRecordSample2() {
        return new TrainingRecord().id(2L).topic("topic2");
    }

    public static TrainingRecord getTrainingRecordRandomSampleGenerator() {
        return new TrainingRecord().id(longCount.incrementAndGet()).topic(UUID.randomUUID().toString());
    }
}
