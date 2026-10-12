package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecruitmentEvaluationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RecruitmentEvaluation getRecruitmentEvaluationSample1() {
        return new RecruitmentEvaluation()
            .id(1L)
            .activityId(1L)
            .templateVersionId(1L)
            .evaluatorUsername("evaluatorUsername1")
            .recordedByUsername("recordedByUsername1")
            .status("status1")
            .overallRecommendation("overallRecommendation1")
            .correctionOfId(1L);
    }

    public static RecruitmentEvaluation getRecruitmentEvaluationSample2() {
        return new RecruitmentEvaluation()
            .id(2L)
            .activityId(2L)
            .templateVersionId(2L)
            .evaluatorUsername("evaluatorUsername2")
            .recordedByUsername("recordedByUsername2")
            .status("status2")
            .overallRecommendation("overallRecommendation2")
            .correctionOfId(2L);
    }

    public static RecruitmentEvaluation getRecruitmentEvaluationRandomSampleGenerator() {
        return new RecruitmentEvaluation()
            .id(longCount.incrementAndGet())
            .activityId(longCount.incrementAndGet())
            .templateVersionId(longCount.incrementAndGet())
            .evaluatorUsername(UUID.randomUUID().toString())
            .recordedByUsername(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .overallRecommendation(UUID.randomUUID().toString())
            .correctionOfId(longCount.incrementAndGet());
    }
}
