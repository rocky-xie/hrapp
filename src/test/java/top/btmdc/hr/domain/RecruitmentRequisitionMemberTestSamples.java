package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecruitmentRequisitionMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RecruitmentRequisitionMember getRecruitmentRequisitionMemberSample1() {
        return new RecruitmentRequisitionMember().id(1L).username("username1").memberType("memberType1");
    }

    public static RecruitmentRequisitionMember getRecruitmentRequisitionMemberSample2() {
        return new RecruitmentRequisitionMember().id(2L).username("username2").memberType("memberType2");
    }

    public static RecruitmentRequisitionMember getRecruitmentRequisitionMemberRandomSampleGenerator() {
        return new RecruitmentRequisitionMember()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .memberType(UUID.randomUUID().toString());
    }
}
