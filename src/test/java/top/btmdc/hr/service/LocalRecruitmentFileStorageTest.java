package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LocalRecruitmentFileStorageTest {

    @TempDir
    Path root;

    @Test
    void storesReadsAndDeletesWithGeneratedKey() throws Exception {
        LocalRecruitmentFileStorage storage = new LocalRecruitmentFileStorage(root.toString());
        byte[] content = "resume bytes".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String key = storage.save(content);

        assertThat(key).matches("[a-fA-F0-9-]{36}");
        assertThat(storage.exists(key)).isTrue();
        assertThat(storage.read(key)).containsExactly(content);
        storage.delete(key);
        assertThat(storage.exists(key)).isFalse();
    }

    @Test
    void rejectsUserControlledPathKeys() {
        LocalRecruitmentFileStorage storage = new LocalRecruitmentFileStorage(root.toString());

        assertThatThrownBy(() -> storage.read("../../secret"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid recruitment storage key");
    }
}
