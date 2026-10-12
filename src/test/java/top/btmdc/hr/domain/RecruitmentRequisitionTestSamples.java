package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecruitmentRequisitionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RecruitmentRequisition getRecruitmentRequisitionSample1() {
        return new RecruitmentRequisition()
            .id(1L)
            .requisitionCode("requisitionCode1")
            .title("title1")
            .department("department1")
            .positionId(1L)
            .plannedHeadcount(1)
            .hiredCount(1)
            .checkedInCount(1)
            .priority("priority1")
            .currentVersion(1)
            .lockedBy("lockedBy1");
    }

    public static RecruitmentRequisition getRecruitmentRequisitionSample2() {
        return new RecruitmentRequisition()
            .id(2L)
            .requisitionCode("requisitionCode2")
            .title("title2")
            .department("department2")
            .positionId(2L)
            .plannedHeadcount(2)
            .hiredCount(2)
            .checkedInCount(2)
            .priority("priority2")
            .currentVersion(2)
            .lockedBy("lockedBy2");
    }

    public static RecruitmentRequisition getRecruitmentRequisitionRandomSampleGenerator() {
        return new RecruitmentRequisition()
            .id(longCount.incrementAndGet())
            .requisitionCode(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .positionId(longCount.incrementAndGet())
            .plannedHeadcount(intCount.incrementAndGet())
            .hiredCount(intCount.incrementAndGet())
            .checkedInCount(intCount.incrementAndGet())
            .priority(UUID.randomUUID().toString())
            .currentVersion(intCount.incrementAndGet())
            .lockedBy(UUID.randomUUID().toString());
    }
}
