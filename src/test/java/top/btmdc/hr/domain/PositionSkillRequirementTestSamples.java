package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PositionSkillRequirementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PositionSkillRequirement getPositionSkillRequirementSample1() {
        return new PositionSkillRequirement().id(1L);
    }

    public static PositionSkillRequirement getPositionSkillRequirementSample2() {
        return new PositionSkillRequirement().id(2L);
    }

    public static PositionSkillRequirement getPositionSkillRequirementRandomSampleGenerator() {
        return new PositionSkillRequirement().id(longCount.incrementAndGet());
    }
}
