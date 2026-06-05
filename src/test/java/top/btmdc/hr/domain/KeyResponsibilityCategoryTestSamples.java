package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KeyResponsibilityCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static KeyResponsibilityCategory getKeyResponsibilityCategorySample1() {
        return new KeyResponsibilityCategory().id(1L).categoryName("categoryName1");
    }

    public static KeyResponsibilityCategory getKeyResponsibilityCategorySample2() {
        return new KeyResponsibilityCategory().id(2L).categoryName("categoryName2");
    }

    public static KeyResponsibilityCategory getKeyResponsibilityCategoryRandomSampleGenerator() {
        return new KeyResponsibilityCategory().id(longCount.incrementAndGet()).categoryName(UUID.randomUUID().toString());
    }
}
