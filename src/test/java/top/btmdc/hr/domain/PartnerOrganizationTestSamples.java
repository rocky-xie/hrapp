package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PartnerOrganizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PartnerOrganization getPartnerOrganizationSample1() {
        return new PartnerOrganization().id(1L).partnerCode("partnerCode1").name("name1").defaultObservationDays(1);
    }

    public static PartnerOrganization getPartnerOrganizationSample2() {
        return new PartnerOrganization().id(2L).partnerCode("partnerCode2").name("name2").defaultObservationDays(2);
    }

    public static PartnerOrganization getPartnerOrganizationRandomSampleGenerator() {
        return new PartnerOrganization()
            .id(longCount.incrementAndGet())
            .partnerCode(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .defaultObservationDays(intCount.incrementAndGet());
    }
}
