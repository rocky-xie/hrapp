package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecruitmentApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RecruitmentApplication getRecruitmentApplicationSample1() {
        return new RecruitmentApplication()
            .id(1L)
            .applicationCode("applicationCode1")
            .sourceType("sourceType1")
            .sourceDetail("sourceDetail1")
            .batchName("batchName1")
            .convertedPersonId(1L);
    }

    public static RecruitmentApplication getRecruitmentApplicationSample2() {
        return new RecruitmentApplication()
            .id(2L)
            .applicationCode("applicationCode2")
            .sourceType("sourceType2")
            .sourceDetail("sourceDetail2")
            .batchName("batchName2")
            .convertedPersonId(2L);
    }

    public static RecruitmentApplication getRecruitmentApplicationRandomSampleGenerator() {
        return new RecruitmentApplication()
            .id(longCount.incrementAndGet())
            .applicationCode(UUID.randomUUID().toString())
            .sourceType(UUID.randomUUID().toString())
            .sourceDetail(UUID.randomUUID().toString())
            .batchName(UUID.randomUUID().toString())
            .convertedPersonId(longCount.incrementAndGet());
    }
}
