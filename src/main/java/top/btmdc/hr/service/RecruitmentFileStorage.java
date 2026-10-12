package top.btmdc.hr.service;

import java.io.IOException;

/** Private storage boundary for original recruitment documents. */
public interface RecruitmentFileStorage {
    String save(byte[] content) throws IOException;

    byte[] read(String storageKey) throws IOException;

    boolean exists(String storageKey);

    void delete(String storageKey) throws IOException;
}
