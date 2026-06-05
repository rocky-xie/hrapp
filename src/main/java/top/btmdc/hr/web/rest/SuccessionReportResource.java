package top.btmdc.hr.web.rest;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.domain.TrainingRecord;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.SuccessionCandidateRepository;
import top.btmdc.hr.repository.TrainingRecordRepository;
import top.btmdc.hr.service.dto.report.SuccessionMapCandidateDTO;
import top.btmdc.hr.service.dto.report.SuccessionMapDTO;

@RestController
@RequestMapping("/api/reports")
public class SuccessionReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(SuccessionReportResource.class);

    private final SuccessionCandidateRepository successionCandidateRepository;
    private final PositionAssignmentRepository positionAssignmentRepository;
    private final TrainingRecordRepository trainingRecordRepository;

    public SuccessionReportResource(
        SuccessionCandidateRepository successionCandidateRepository,
        PositionAssignmentRepository positionAssignmentRepository,
        TrainingRecordRepository trainingRecordRepository
    ) {
        this.successionCandidateRepository = successionCandidateRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
        this.trainingRecordRepository = trainingRecordRepository;
    }

    @GetMapping("/succession-map")
    public ResponseEntity<List<SuccessionMapDTO>> getSuccessionMap() {
        LOG.debug("REST request to get succession map");
        List<SuccessionCandidate> allCandidates = successionCandidateRepository.findAllWithEagerRelationships();
        java.util.Map<Long, List<SuccessionCandidate>> grouped = allCandidates
            .stream()
            .collect(Collectors.groupingBy(sc -> sc.getPosition().getId()));

        List<SuccessionMapDTO> result = grouped
            .entrySet()
            .stream()
            .map(entry -> {
                Long positionId = entry.getKey();
                List<SuccessionCandidate> candidates = entry.getValue();
                SuccessionCandidate first = candidates.get(0);

                SuccessionMapDTO dto = new SuccessionMapDTO();
                dto.setPositionId(positionId);
                dto.setPositionName(first.getPosition().getPositionName());
                dto.setCurrentOwnerName(findCurrentOwner(positionId));
                dto.setTotalCandidates(candidates.size());

                List<SuccessionMapCandidateDTO> candidateDTOs = candidates
                    .stream()
                    .map(sc -> {
                        SuccessionMapCandidateDTO cd = new SuccessionMapCandidateDTO();
                        cd.setCandidateId(sc.getCandidate().getId());
                        cd.setCandidateName(sc.getCandidate().getPersonName());
                        cd.setReadiness(sc.getSuccessionReadiness());
                        cd.setPriority(sc.getPriority());
                        cd.setRiskAfterTraining(sc.getRiskAfterTraining());
                        return cd;
                    })
                    .toList();
                dto.setCandidates(candidateDTOs);
                return dto;
            })
            .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/person-training-history/{personId}")
    public ResponseEntity<List<TrainingRecord>> getPersonTrainingHistory(@PathVariable Long personId) {
        LOG.debug("REST request to get training history for person: {}", personId);
        List<TrainingRecord> records = trainingRecordRepository.findByPersonIdWithEagerRelationships(personId);
        return ResponseEntity.ok(records);
    }

    private String findCurrentOwner(Long positionId) {
        List<top.btmdc.hr.domain.PositionAssignment> assignments = positionAssignmentRepository.findActiveByPositionIdWithPerson(
            positionId
        );
        return assignments
            .stream()
            .filter(pa -> Boolean.TRUE.equals(pa.getPrimaryOwner()))
            .findFirst()
            .map(pa -> pa.getPerson().getPersonName())
            .orElseGet(() -> assignments.isEmpty() ? null : assignments.get(0).getPerson().getPersonName());
    }
}
