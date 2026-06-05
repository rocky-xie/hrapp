package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrainingGoalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static TrainingGoal getTrainingGoalSample1() {
        return new TrainingGoal().id(1L).goalName("goalName1");
    }

    public static TrainingGoal getTrainingGoalSample2() {
        return new TrainingGoal().id(2L).goalName("goalName2");
    }

    public static TrainingGoal getTrainingGoalRandomSampleGenerator() {
        return new TrainingGoal().id(longCount.incrementAndGet()).goalName(UUID.randomUUID().toString());
    }
}
