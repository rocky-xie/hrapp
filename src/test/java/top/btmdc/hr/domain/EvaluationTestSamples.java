package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EvaluationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Evaluation getEvaluationSample1() {
        return new Evaluation().id(1L).evaluationName("evaluationName1").periodLabel("periodLabel1");
    }

    public static Evaluation getEvaluationSample2() {
        return new Evaluation().id(2L).evaluationName("evaluationName2").periodLabel("periodLabel2");
    }

    public static Evaluation getEvaluationRandomSampleGenerator() {
        return new Evaluation()
            .id(longCount.incrementAndGet())
            .evaluationName(UUID.randomUUID().toString())
            .periodLabel(UUID.randomUUID().toString());
    }
}
