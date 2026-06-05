package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SkillUpgradeRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SkillUpgradeRecord getSkillUpgradeRecordSample1() {
        return new SkillUpgradeRecord().id(1L).reason("reason1").beforeLevelLabel("beforeLevelLabel1").afterLevelLabel("afterLevelLabel1");
    }

    public static SkillUpgradeRecord getSkillUpgradeRecordSample2() {
        return new SkillUpgradeRecord().id(2L).reason("reason2").beforeLevelLabel("beforeLevelLabel2").afterLevelLabel("afterLevelLabel2");
    }

    public static SkillUpgradeRecord getSkillUpgradeRecordRandomSampleGenerator() {
        return new SkillUpgradeRecord()
            .id(longCount.incrementAndGet())
            .reason(UUID.randomUUID().toString())
            .beforeLevelLabel(UUID.randomUUID().toString())
            .afterLevelLabel(UUID.randomUUID().toString());
    }
}
