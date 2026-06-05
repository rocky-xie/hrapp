package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PositionMatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PositionMatch getPositionMatchSample1() {
        return new PositionMatch().id(1L).matchScore(1);
    }

    public static PositionMatch getPositionMatchSample2() {
        return new PositionMatch().id(2L).matchScore(2);
    }

    public static PositionMatch getPositionMatchRandomSampleGenerator() {
        return new PositionMatch().id(longCount.incrementAndGet()).matchScore(intCount.incrementAndGet());
    }
}
