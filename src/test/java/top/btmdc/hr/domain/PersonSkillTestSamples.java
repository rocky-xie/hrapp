package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PersonSkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PersonSkill getPersonSkillSample1() {
        return new PersonSkill().id(1L);
    }

    public static PersonSkill getPersonSkillSample2() {
        return new PersonSkill().id(2L);
    }

    public static PersonSkill getPersonSkillRandomSampleGenerator() {
        return new PersonSkill().id(longCount.incrementAndGet());
    }
}
