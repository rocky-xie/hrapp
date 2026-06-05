package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PositionAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PositionAssignment getPositionAssignmentSample1() {
        return new PositionAssignment().id(1L);
    }

    public static PositionAssignment getPositionAssignmentSample2() {
        return new PositionAssignment().id(2L);
    }

    public static PositionAssignment getPositionAssignmentRandomSampleGenerator() {
        return new PositionAssignment().id(longCount.incrementAndGet());
    }
}
