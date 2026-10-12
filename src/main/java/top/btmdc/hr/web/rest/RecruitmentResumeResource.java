package top.btmdc.hr.web.rest;

import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.btmdc.hr.security.SecurityUtils;
import top.btmdc.hr.service.RecruitmentResumeService;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.service.mapper.CandidateResumeMapper;
import top.btmdc.hr.web.rest.errors.BadRequestAlertException;

/** Authorized upload and private download endpoints for candidate resumes. */
@RestController
@RequestMapping("/api/recruitment-resumes")
@PreAuthorize("hasAnyAuthority('ROLE_HR_RECRUITER', 'ROLE_ADMIN')")
public class RecruitmentResumeResource {

    private final RecruitmentResumeService service;
    private final CandidateResumeMapper mapper;

    public RecruitmentResumeResource(RecruitmentResumeService service, CandidateResumeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(value = "/candidates/{candidateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeUploadResponse> upload(@PathVariable Long candidateId, @RequestParam("file") MultipartFile file) {
        String actor = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("Authenticated actor is required"));
        RecruitmentResumeService.UploadResult result;
        try {
            result = service.upload(candidateId, file, actor);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestAlertException(exception.getMessage(), "candidateResume", "uploadinvalid");
        }
        return ResponseEntity.ok(new ResumeUploadResponse(mapper.toDto(result.resume()), result.duplicateResumeIds()));
    }

    @org.springframework.web.bind.annotation.GetMapping("/{resumeId}/content")
    public ResponseEntity<byte[]> download(@PathVariable Long resumeId) {
        RecruitmentResumeService.DownloadResult file;
        try {
            file = service.download(resumeId);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestAlertException(exception.getMessage(), "candidateResume", "resumenotfound");
        }
        MediaType mediaType;
        try {
            mediaType = file.mediaType() == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(file.mediaType());
        } catch (IllegalArgumentException exception) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(file.content().length);
        headers.setContentDisposition(ContentDisposition.attachment().filename(file.fileName(), StandardCharsets.UTF_8).build());
        headers.set("X-Content-Type-Options", "nosniff");
        return ResponseEntity.ok().headers(headers).body(file.content());
    }

    public record ResumeUploadResponse(CandidateResumeDTO resume, java.util.List<Long> duplicateResumeIds) {}
}
