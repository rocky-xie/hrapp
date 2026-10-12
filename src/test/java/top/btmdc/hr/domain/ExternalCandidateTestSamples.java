package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExternalCandidateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ExternalCandidate getExternalCandidateSample1() {
        return new ExternalCandidate()
            .id(1L)
            .fullName("fullName1")
            .nameReading("nameReading1")
            .englishName("englishName1")
            .gender("gender1")
            .phone("phone1")
            .email("email1")
            .convertedPersonId(1L);
    }

    public static ExternalCandidate getExternalCandidateSample2() {
        return new ExternalCandidate()
            .id(2L)
            .fullName("fullName2")
            .nameReading("nameReading2")
            .englishName("englishName2")
            .gender("gender2")
            .phone("phone2")
            .email("email2")
            .convertedPersonId(2L);
    }

    public static ExternalCandidate getExternalCandidateRandomSampleGenerator() {
        return new ExternalCandidate()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .nameReading(UUID.randomUUID().toString())
            .englishName(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .convertedPersonId(longCount.incrementAndGet());
    }
}
