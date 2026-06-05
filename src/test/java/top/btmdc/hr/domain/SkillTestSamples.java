package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Skill getSkillSample1() {
        return new Skill().id(1L).skillCode("skillCode1").skillName("skillName1");
    }

    public static Skill getSkillSample2() {
        return new Skill().id(2L).skillCode("skillCode2").skillName("skillName2");
    }

    public static Skill getSkillRandomSampleGenerator() {
        return new Skill().id(longCount.incrementAndGet()).skillCode(UUID.randomUUID().toString()).skillName(UUID.randomUUID().toString());
    }
}
