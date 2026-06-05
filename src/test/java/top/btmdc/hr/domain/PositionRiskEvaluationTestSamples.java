package top.btmdc.hr.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PositionRiskEvaluationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PositionRiskEvaluation getPositionRiskEvaluationSample1() {
        return new PositionRiskEvaluation().id(1L).ownerCount(1).substitutableOwnerCount(1);
    }

    public static PositionRiskEvaluation getPositionRiskEvaluationSample2() {
        return new PositionRiskEvaluation().id(2L).ownerCount(2).substitutableOwnerCount(2);
    }

    public static PositionRiskEvaluation getPositionRiskEvaluationRandomSampleGenerator() {
        return new PositionRiskEvaluation()
            .id(longCount.incrementAndGet())
            .ownerCount(intCount.incrementAndGet())
            .substitutableOwnerCount(intCount.incrementAndGet());
    }
}
