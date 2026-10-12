package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HiringDecisionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static HiringDecision getHiringDecisionSample1() {
        return new HiringDecision().id(1L).decisionSource("decisionSource1").decidedBy("decidedBy1").correctionOfId(1L);
    }

    public static HiringDecision getHiringDecisionSample2() {
        return new HiringDecision().id(2L).decisionSource("decisionSource2").decidedBy("decidedBy2").correctionOfId(2L);
    }

    public static HiringDecision getHiringDecisionRandomSampleGenerator() {
        return new HiringDecision()
            .id(longCount.incrementAndGet())
            .decisionSource(UUID.randomUUID().toString())
            .decidedBy(UUID.randomUUID().toString())
            .correctionOfId(longCount.incrementAndGet());
    }
}
