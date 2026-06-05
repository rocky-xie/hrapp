package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CandidateProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CandidateProfile getCandidateProfileSample1() {
        return new CandidateProfile().id(1L).cultivateDirection("cultivateDirection1");
    }

    public static CandidateProfile getCandidateProfileSample2() {
        return new CandidateProfile().id(2L).cultivateDirection("cultivateDirection2");
    }

    public static CandidateProfile getCandidateProfileRandomSampleGenerator() {
        return new CandidateProfile().id(longCount.incrementAndGet()).cultivateDirection(UUID.randomUUID().toString());
    }
}
