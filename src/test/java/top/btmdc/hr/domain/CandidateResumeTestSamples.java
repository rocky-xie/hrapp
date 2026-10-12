package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CandidateResumeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CandidateResume getCandidateResumeSample1() {
        return new CandidateResume()
            .id(1L)
            .versionNumber(1)
            .originalFileName("originalFileName1")
            .mediaType("mediaType1")
            .fileSize(1L)
            .sha256("sha2561")
            .storageKey("storageKey1")
            .uploadedBy("uploadedBy1");
    }

    public static CandidateResume getCandidateResumeSample2() {
        return new CandidateResume()
            .id(2L)
            .versionNumber(2)
            .originalFileName("originalFileName2")
            .mediaType("mediaType2")
            .fileSize(2L)
            .sha256("sha2562")
            .storageKey("storageKey2")
            .uploadedBy("uploadedBy2");
    }

    public static CandidateResume getCandidateResumeRandomSampleGenerator() {
        return new CandidateResume()
            .id(longCount.incrementAndGet())
            .versionNumber(intCount.incrementAndGet())
            .originalFileName(UUID.randomUUID().toString())
            .mediaType(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .sha256(UUID.randomUUID().toString())
            .storageKey(UUID.randomUUID().toString())
            .uploadedBy(UUID.randomUUID().toString());
    }
}
