package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecruitmentRequisitionVersionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RecruitmentRequisitionVersion getRecruitmentRequisitionVersionSample1() {
        return new RecruitmentRequisitionVersion().id(1L).versionNumber(1).lockedBy("lockedBy1");
    }

    public static RecruitmentRequisitionVersion getRecruitmentRequisitionVersionSample2() {
        return new RecruitmentRequisitionVersion().id(2L).versionNumber(2).lockedBy("lockedBy2");
    }

    public static RecruitmentRequisitionVersion getRecruitmentRequisitionVersionRandomSampleGenerator() {
        return new RecruitmentRequisitionVersion()
            .id(longCount.incrementAndGet())
            .versionNumber(intCount.incrementAndGet())
            .lockedBy(UUID.randomUUID().toString());
    }
}
