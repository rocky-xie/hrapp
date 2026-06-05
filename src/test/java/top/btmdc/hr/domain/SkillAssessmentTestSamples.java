package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SkillAssessmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SkillAssessment getSkillAssessmentSample1() {
        return new SkillAssessment().id(1L);
    }

    public static SkillAssessment getSkillAssessmentSample2() {
        return new SkillAssessment().id(2L);
    }

    public static SkillAssessment getSkillAssessmentRandomSampleGenerator() {
        return new SkillAssessment().id(longCount.incrementAndGet());
    }
}
