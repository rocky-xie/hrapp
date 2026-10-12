package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DispatchReviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static DispatchReview getDispatchReviewSample1() {
        return new DispatchReview()
            .id(1L)
            .reviewerUsername("reviewerUsername1")
            .recommendation("recommendation1")
            .finalDecision("finalDecision1");
    }

    public static DispatchReview getDispatchReviewSample2() {
        return new DispatchReview()
            .id(2L)
            .reviewerUsername("reviewerUsername2")
            .recommendation("recommendation2")
            .finalDecision("finalDecision2");
    }

    public static DispatchReview getDispatchReviewRandomSampleGenerator() {
        return new DispatchReview()
            .id(longCount.incrementAndGet())
            .reviewerUsername(UUID.randomUUID().toString())
            .recommendation(UUID.randomUUID().toString())
            .finalDecision(UUID.randomUUID().toString());
    }
}
