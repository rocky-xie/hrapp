package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PreboardingFollowUpTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PreboardingFollowUp getPreboardingFollowUpSample1() {
        return new PreboardingFollowUp()
            .id(1L)
            .candidateId(1L)
            .personId(1L)
            .ownerUsername("ownerUsername1")
            .contactMethod("contactMethod1")
            .candidateIntent("candidateIntent1")
            .riskLevel("riskLevel1");
    }

    public static PreboardingFollowUp getPreboardingFollowUpSample2() {
        return new PreboardingFollowUp()
            .id(2L)
            .candidateId(2L)
            .personId(2L)
            .ownerUsername("ownerUsername2")
            .contactMethod("contactMethod2")
            .candidateIntent("candidateIntent2")
            .riskLevel("riskLevel2");
    }

    public static PreboardingFollowUp getPreboardingFollowUpRandomSampleGenerator() {
        return new PreboardingFollowUp()
            .id(longCount.incrementAndGet())
            .candidateId(longCount.incrementAndGet())
            .personId(longCount.incrementAndGet())
            .ownerUsername(UUID.randomUUID().toString())
            .contactMethod(UUID.randomUUID().toString())
            .candidateIntent(UUID.randomUUID().toString())
            .riskLevel(UUID.randomUUID().toString());
    }
}
