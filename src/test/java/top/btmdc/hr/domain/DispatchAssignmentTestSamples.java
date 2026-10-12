package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DispatchAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DispatchAssignment getDispatchAssignmentSample1() {
        return new DispatchAssignment()
            .id(1L)
            .candidateId(1L)
            .personId(1L)
            .partnerOrganizationId(1L)
            .department("department1")
            .positionId(1L)
            .observationDays(1)
            .status("status1")
            .finalDecision("finalDecision1");
    }

    public static DispatchAssignment getDispatchAssignmentSample2() {
        return new DispatchAssignment()
            .id(2L)
            .candidateId(2L)
            .personId(2L)
            .partnerOrganizationId(2L)
            .department("department2")
            .positionId(2L)
            .observationDays(2)
            .status("status2")
            .finalDecision("finalDecision2");
    }

    public static DispatchAssignment getDispatchAssignmentRandomSampleGenerator() {
        return new DispatchAssignment()
            .id(longCount.incrementAndGet())
            .candidateId(longCount.incrementAndGet())
            .personId(longCount.incrementAndGet())
            .partnerOrganizationId(longCount.incrementAndGet())
            .department(UUID.randomUUID().toString())
            .positionId(longCount.incrementAndGet())
            .observationDays(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .finalDecision(UUID.randomUUID().toString());
    }
}
