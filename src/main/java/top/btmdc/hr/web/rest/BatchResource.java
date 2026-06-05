package top.btmdc.hr.web.rest;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.btmdc.hr.service.BatchService;

@RestController
@RequestMapping("/api/batch")
public class BatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(BatchResource.class);

    private final BatchService batchService;

    public BatchResource(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/export/persons")
    public ResponseEntity<byte[]> exportPersons() throws IOException {
        LOG.debug("REST request to export persons");
        byte[] data = batchService.exportPersons();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=persons.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(data);
    }

    @GetMapping("/export/positions")
    public ResponseEntity<byte[]> exportPositions() throws IOException {
        LOG.debug("REST request to export positions");
        byte[] data = batchService.exportPositions();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=positions.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(data);
    }

    @GetMapping("/export/skills")
    public ResponseEntity<byte[]> exportSkills() throws IOException {
        LOG.debug("REST request to export skills");
        byte[] data = batchService.exportSkills();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=skills.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(data);
    }

    @PostMapping("/import/persons")
    public ResponseEntity<String> importPersons(@RequestParam("file") MultipartFile file) throws IOException {
        LOG.debug("REST request to import persons from file: {}", file.getOriginalFilename());
        String result = batchService.importPersons(file.getInputStream());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/positions")
    public ResponseEntity<String> importPositions(@RequestParam("file") MultipartFile file) throws IOException {
        LOG.debug("REST request to import positions from file: {}", file.getOriginalFilename());
        String result = batchService.importPositions(file.getInputStream());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/skills")
    public ResponseEntity<String> importSkills(@RequestParam("file") MultipartFile file) throws IOException {
        LOG.debug("REST request to import skills from file: {}", file.getOriginalFilename());
        String result = batchService.importSkills(file.getInputStream());
        return ResponseEntity.ok(result);
    }
}
