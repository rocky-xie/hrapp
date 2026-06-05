package top.btmdc.hr.web.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.btmdc.hr.domain.enumeration.RequirementImportance;
import top.btmdc.hr.service.SkillGapReportService;
import top.btmdc.hr.service.dto.report.ReportCriteria;
import top.btmdc.hr.service.dto.report.SkillGapReportDTO;

@RestController
@RequestMapping("/api/reports")
public class SkillGapReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(SkillGapReportResource.class);

    private final SkillGapReportService skillGapReportService;

    public SkillGapReportResource(SkillGapReportService skillGapReportService) {
        this.skillGapReportService = skillGapReportService;
    }

    @GetMapping("/position-skill-gaps")
    public ResponseEntity<SkillGapReportDTO> getPositionSkillGaps(
        @RequestParam List<Long> positionIds,
        @RequestParam(required = false) RequirementImportance minImportance,
        @RequestParam(defaultValue = "true") boolean includeOwners,
        @RequestParam(defaultValue = "true") boolean includeCandidates
    ) {
        LOG.debug("REST request to get skill gap report for positions: {}", positionIds);
        ReportCriteria criteria = new ReportCriteria();
        criteria.setIncludeOwners(includeOwners);
        criteria.setIncludeCandidates(includeCandidates);
        criteria.setMinImportance(minImportance);
        SkillGapReportDTO report = skillGapReportService.generateReport(positionIds, criteria);
        return ResponseEntity.ok(report);
    }
}
