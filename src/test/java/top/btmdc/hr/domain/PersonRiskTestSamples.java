package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PersonRiskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PersonRisk getPersonRiskSample1() {
        return new PersonRisk().id(1L);
    }

    public static PersonRisk getPersonRiskSample2() {
        return new PersonRisk().id(2L);
    }

    public static PersonRisk getPersonRiskRandomSampleGenerator() {
        return new PersonRisk().id(longCount.incrementAndGet());
    }
}
