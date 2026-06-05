package top.btmdc.hr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Person getPersonSample1() {
        return new Person()
            .id(1L)
            .employeeCode("employeeCode1")
            .personName("personName1")
            .age(1)
            .department("department1")
            .currentRole("currentRole1");
    }

    public static Person getPersonSample2() {
        return new Person()
            .id(2L)
            .employeeCode("employeeCode2")
            .personName("personName2")
            .age(2)
            .department("department2")
            .currentRole("currentRole2");
    }

    public static Person getPersonRandomSampleGenerator() {
        return new Person()
            .id(longCount.incrementAndGet())
            .employeeCode(UUID.randomUUID().toString())
            .personName(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .department(UUID.randomUUID().toString())
            .currentRole(UUID.randomUUID().toString());
    }
}
