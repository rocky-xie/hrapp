package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SuccessionCandidateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SuccessionCandidate getSuccessionCandidateSample1() {
        return new SuccessionCandidate().id(1L).estimatedTimeToReady("estimatedTimeToReady1").priority(1);
    }

    public static SuccessionCandidate getSuccessionCandidateSample2() {
        return new SuccessionCandidate().id(2L).estimatedTimeToReady("estimatedTimeToReady2").priority(2);
    }

    public static SuccessionCandidate getSuccessionCandidateRandomSampleGenerator() {
        return new SuccessionCandidate()
            .id(longCount.incrementAndGet())
            .estimatedTimeToReady(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet());
    }
}
