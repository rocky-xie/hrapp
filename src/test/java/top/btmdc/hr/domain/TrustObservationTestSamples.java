package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TrustObservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static TrustObservation getTrustObservationSample1() {
        return new TrustObservation().id(1L);
    }

    public static TrustObservation getTrustObservationSample2() {
        return new TrustObservation().id(2L);
    }

    public static TrustObservation getTrustObservationRandomSampleGenerator() {
        return new TrustObservation().id(longCount.incrementAndGet());
    }
}
