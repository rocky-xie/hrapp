package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.repository.ExternalCandidateRepository;

@ExtendWith(MockitoExtension.class)
class RecruitmentResumeServiceTest {

    @Mock
    private CandidateResumeRepository resumes;

    @Mock
    private ExternalCandidateRepository candidates;

    @Mock
    private RecruitmentFileStorage storage;

    private RecruitmentResumeService service;

    @BeforeEach
    void setUp() {
        service = new RecruitmentResumeService(resumes, candidates, storage, 1024);
    }

    @Test
    void uploadHashesVersionsAndReportsMatchingResume() throws Exception {
        ExternalCandidate candidate = new ExternalCandidate().id(5L).fullName("Candidate");
        when(candidates.findById(5L)).thenReturn(Optional.of(candidate));
        when(resumes.findBySha256(any())).thenReturn(List.of(new CandidateResume().id(3L)));
        when(resumes.findFirstByCandidate_IdOrderByVersionNumberDesc(5L)).thenReturn(Optional.empty());
        when(storage.save(any())).thenReturn("9d36ce2e-16dc-4ce5-8fbb-491b8f07bdab");
        when(resumes.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile file = new MockMultipartFile("file", "../candidate.pdf", "application/pdf", "resume".getBytes());

        RecruitmentResumeService.UploadResult result = service.upload(5L, file, "hr-user");

        assertThat(result.resume().getOriginalFileName()).isEqualTo("candidate.pdf");
        assertThat(result.resume().getVersionNumber()).isEqualTo(1);
        assertThat(result.resume().getActiveVersion()).isTrue();
        assertThat(result.resume().getSha256()).hasSize(64);
        assertThat(result.duplicateResumeIds()).containsExactly(3L);
        assertThat(result.resume().getUploadedBy()).isEqualTo("hr-user");
    }

    @Test
    void oversizedFileIsRejectedBeforeStorage() {
        MockMultipartFile file = new MockMultipartFile("file", "large.pdf", "application/pdf", new byte[2048]);

        assertThatThrownBy(() -> service.upload(5L, file, "hr-user"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("configured size limit");
        verifyNoInteractions(storage, candidates);
    }
}
