package top.btmdc.hr.web.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.btmdc.hr.service.DataQualityService;
import top.btmdc.hr.service.dto.DataQualityIssueDTO;

@RestController
@RequestMapping("/api")
public class DataQualityResource {

    private static final Logger LOG = LoggerFactory.getLogger(DataQualityResource.class);

    private final DataQualityService dataQualityService;

    public DataQualityResource(DataQualityService dataQualityService) {
        this.dataQualityService = dataQualityService;
    }

    @GetMapping("/data-quality/checks")
    public ResponseEntity<List<DataQualityIssueDTO>> runChecks() {
        LOG.debug("REST request to run all data quality checks");
        List<DataQualityIssueDTO> issues = dataQualityService.runAllChecks();
        return ResponseEntity.ok(issues);
    }
}
