package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PositionRiskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PositionRisk getPositionRiskSample1() {
        return new PositionRisk().id(1L);
    }

    public static PositionRisk getPositionRiskSample2() {
        return new PositionRisk().id(2L);
    }

    public static PositionRisk getPositionRiskRandomSampleGenerator() {
        return new PositionRisk().id(longCount.incrementAndGet());
    }
}
