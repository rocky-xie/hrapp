package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImprovementPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ImprovementPlan getImprovementPlanSample1() {
        return new ImprovementPlan().id(1L).planName("planName1").ownerName("ownerName1");
    }

    public static ImprovementPlan getImprovementPlanSample2() {
        return new ImprovementPlan().id(2L).planName("planName2").ownerName("ownerName2");
    }

    public static ImprovementPlan getImprovementPlanRandomSampleGenerator() {
        return new ImprovementPlan()
            .id(longCount.incrementAndGet())
            .planName(UUID.randomUUID().toString())
            .ownerName(UUID.randomUUID().toString());
    }
}
