package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SkillLevelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SkillLevel getSkillLevelSample1() {
        return new SkillLevel().id(1L).levelName("levelName1").sortOrder(1);
    }

    public static SkillLevel getSkillLevelSample2() {
        return new SkillLevel().id(2L).levelName("levelName2").sortOrder(2);
    }

    public static SkillLevel getSkillLevelRandomSampleGenerator() {
        return new SkillLevel()
            .id(longCount.incrementAndGet())
            .levelName(UUID.randomUUID().toString())
            .sortOrder(intCount.incrementAndGet());
    }
}
