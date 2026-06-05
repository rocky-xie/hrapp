package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PositionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Position getPositionSample1() {
        return new Position().id(1L).positionCode("positionCode1").positionName("positionName1").plannedHeadcount(1).minimumOwnerCount(1);
    }

    public static Position getPositionSample2() {
        return new Position().id(2L).positionCode("positionCode2").positionName("positionName2").plannedHeadcount(2).minimumOwnerCount(2);
    }

    public static Position getPositionRandomSampleGenerator() {
        return new Position()
            .id(longCount.incrementAndGet())
            .positionCode(UUID.randomUUID().toString())
            .positionName(UUID.randomUUID().toString())
            .plannedHeadcount(intCount.incrementAndGet())
            .minimumOwnerCount(intCount.incrementAndGet());
    }
}
