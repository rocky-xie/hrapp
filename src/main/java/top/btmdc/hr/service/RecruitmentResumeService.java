package top.btmdc.hr.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.repository.ExternalCandidateRepository;

/** Uploads original resumes privately, versions candidate files, and reports exact duplicate hashes. */
@Service
@Transactional
public class RecruitmentResumeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentResumeService.class);

    private final CandidateResumeRepository resumes;
    private final ExternalCandidateRepository candidates;
    private final RecruitmentFileStorage storage;
    private final long maxFileBytes;

    public RecruitmentResumeService(
        CandidateResumeRepository resumes,
        ExternalCandidateRepository candidates,
        RecruitmentFileStorage storage,
        @Value("${hr.recruitment.max-resume-bytes:10485760}") long maxFileBytes
    ) {
        this.resumes = resumes;
        this.candidates = candidates;
        this.storage = storage;
        this.maxFileBytes = maxFileBytes;
    }

    public UploadResult upload(Long candidateId, MultipartFile file, String actor) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Resume file is required");
        if (file.getSize() > maxFileBytes) throw new IllegalArgumentException("Resume file exceeds the configured size limit");
        ExternalCandidate candidate = candidates
            .findById(candidateId)
            .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        String name = sanitizeName(file.getOriginalFilename());
        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read uploaded resume", exception);
        }
        String hash = sha256(content);
        List<Long> duplicateResumeIds = resumes.findBySha256(hash).stream().map(CandidateResume::getId).toList();
        CandidateResume previous = resumes.findFirstByCandidate_IdOrderByVersionNumberDesc(candidateId).orElse(null);
        if (previous != null) previous.setActiveVersion(false);
        String key;
        try {
            key = storage.save(content);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store uploaded resume", exception);
        }
        CandidateResume resume = new CandidateResume()
            .versionNumber(previous == null ? 1 : previous.getVersionNumber() + 1)
            .receivedAt(Instant.now())
            .originalFileName(name)
            .mediaType(file.getContentType())
            .fileSize((long) content.length)
            .sha256(hash)
            .storageKey(key)
            .activeVersion(true)
            .uploadedBy(actor)
            .candidate(candidate);
        try {
            CandidateResume saved = resumes.save(resume);
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status != STATUS_COMMITTED) deleteQuietly(key);
                        }
                    }
                );
            }
            return new UploadResult(saved, duplicateResumeIds);
        } catch (RuntimeException exception) {
            try {
                storage.delete(key);
            } catch (IOException cleanupFailure) {
                exception.addSuppressed(cleanupFailure);
            }
            throw exception;
        }
    }

    private void deleteQuietly(String key) {
        try {
            storage.delete(key);
        } catch (IOException exception) {
            // A later storage reconciliation can retry the orphan cleanup.
            LOG.error("Could not clean up resume file after transaction rollback: {}", key, exception);
        }
    }

    @Transactional(readOnly = true)
    public DownloadResult download(Long resumeId) {
        CandidateResume resume = resumes.findById(resumeId).orElseThrow(() -> new IllegalArgumentException("Resume not found"));
        try {
            return new DownloadResult(resume.getOriginalFileName(), resume.getMediaType(), storage.read(resume.getStorageKey()));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read stored resume", exception);
        }
    }

    private String sanitizeName(String name) {
        String safeName = name == null ? "resume" : name.replace('\\', '/').substring(name.replace('\\', '/').lastIndexOf('/') + 1);
        if (safeName.isBlank() || safeName.length() > 255) safeName = "resume";
        return safeName.replaceAll("[\\p{Cntrl}]", "_");
    }

    private String sha256(byte[] content) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(content));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    public record UploadResult(CandidateResume resume, List<Long> duplicateResumeIds) {}

    public record DownloadResult(String fileName, String mediaType, byte[] content) {}
}
