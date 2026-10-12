package top.btmdc.hr.service;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Local private-directory implementation; stored filenames are generated and never user-controlled. */
@Component
public class LocalRecruitmentFileStorage implements RecruitmentFileStorage {

    private final Path root;

    public LocalRecruitmentFileStorage(@Value("${hr.recruitment.resume-storage-path:${java.io.tmpdir}/hrapp-resumes}") String root) {
        this.root = Path.of(root).toAbsolutePath().normalize();
    }

    @Override
    public String save(byte[] content) throws IOException {
        Files.createDirectories(root);
        String key = UUID.randomUUID().toString();
        Path temporary = Files.createTempFile(root, ".upload-", ".tmp");
        Path target = resolve(key);
        try {
            Files.write(temporary, content);
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, target);
            }
            return key;
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    @Override
    public byte[] read(String storageKey) throws IOException {
        return Files.readAllBytes(resolve(storageKey));
    }

    @Override
    public boolean exists(String storageKey) {
        return Files.isRegularFile(resolve(storageKey));
    }

    @Override
    public void delete(String storageKey) throws IOException {
        Files.deleteIfExists(resolve(storageKey));
    }

    private Path resolve(String storageKey) {
        if (storageKey == null || !storageKey.matches("[a-fA-F0-9-]{36}")) {
            throw new IllegalArgumentException("Invalid recruitment storage key");
        }
        Path resolved = root.resolve(storageKey).normalize();
        if (!resolved.getParent().equals(root)) throw new IllegalArgumentException("Invalid recruitment storage key");
        return resolved;
    }
}
