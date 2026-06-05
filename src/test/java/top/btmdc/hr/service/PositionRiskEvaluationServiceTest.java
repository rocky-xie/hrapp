package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionRiskEvaluationRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;
import top.btmdc.hr.service.mapper.PositionRiskEvaluationMapper;

@ExtendWith(MockitoExtension.class)
class PositionRiskEvaluationServiceTest {

    @Mock
    private PositionRiskEvaluationRepository positionRiskEvaluationRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionAssignmentRepository positionAssignmentRepository;

    @Mock
    private StaffSubstitutionRepository staffSubstitutionRepository;

    @Mock
    private PositionRiskEvaluationMapper positionRiskEvaluationMapper;

    private PositionRiskEvaluationService positionRiskEvaluationService;

    @BeforeEach
    void setUp() {
        positionRiskEvaluationService = new PositionRiskEvaluationService(
            positionRiskEvaluationRepository,
            positionRepository,
            positionAssignmentRepository,
            staffSubstitutionRepository,
            positionRiskEvaluationMapper
        );
    }

    @Test
    void evaluateShouldMarkPositionWithoutOwnersAsHighRisk() {
        Position position = position(1L, "Critical Support", true);

        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionAssignmentRepository.findActiveByPositionIdWithPerson(1L)).thenReturn(List.of());
        when(positionRiskEvaluationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(positionRiskEvaluationMapper.toDto(any(PositionRiskEvaluation.class))).thenAnswer(invocation ->
            toDto(invocation.getArgument(0))
        );

        PositionRiskEvaluationDTO result = positionRiskEvaluationService.evaluate(
            1L,
            DocumentStatus.MISSING,
            ImportanceLevel.HIGH,
            ReadinessLevel.NONE
        );

        assertThat(result.getOwnerCount()).isZero();
        assertThat(result.getHasSubstitute()).isFalse();
        assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(result.getRecommendedAction()).contains("Immediately establish");
    }

    @Test
    void evaluateShouldMarkMultipleOwnersWithSubstituteAsLowRisk() {
        Position position = position(1L, "Critical Support", true);
        Person firstOwner = person(10L, "A");
        Person secondOwner = person(11L, "B");

        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionAssignmentRepository.findActiveByPositionIdWithPerson(1L)).thenReturn(
            List.of(assignment(firstOwner, position), assignment(secondOwner, position))
        );
        when(staffSubstitutionRepository.countByPositionIdAndSubstitutableTrue(1L)).thenReturn(1L);
        when(positionRiskEvaluationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(positionRiskEvaluationMapper.toDto(any(PositionRiskEvaluation.class))).thenAnswer(invocation ->
            toDto(invocation.getArgument(0))
        );

        PositionRiskEvaluationDTO result = positionRiskEvaluationService.evaluate(
            1L,
            DocumentStatus.AVAILABLE,
            ImportanceLevel.LOW,
            ReadinessLevel.IMMEDIATE
        );

        assertThat(result.getOwnerCount()).isEqualTo(2);
        assertThat(result.getSubstitutableOwnerCount()).isEqualTo(1);
        assertThat(result.getHasSubstitute()).isTrue();
        assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.LOW);
    }

    private Position position(Long id, String name, boolean keyPosition) {
        return new Position().id(id).positionName(name).keyPosition(keyPosition).minimumOwnerCount(1);
    }

    private Person person(Long id, String name) {
        return new Person().id(id).personName(name);
    }

    private PositionAssignment assignment(Person person, Position position) {
        return new PositionAssignment().person(person).position(position).active(true);
    }

    private PositionRiskEvaluationDTO toDto(PositionRiskEvaluation positionRiskEvaluation) {
        PositionRiskEvaluationDTO dto = new PositionRiskEvaluationDTO();
        dto.setOwnerCount(positionRiskEvaluation.getOwnerCount());
        dto.setSubstitutableOwnerCount(positionRiskEvaluation.getSubstitutableOwnerCount());
        dto.setHasSubstitute(positionRiskEvaluation.getHasSubstitute());
        dto.setDocumentStatus(positionRiskEvaluation.getDocumentStatus());
        dto.setCustomerOrSystemDependency(positionRiskEvaluation.getCustomerOrSystemDependency());
        dto.setSuccessionReadiness(positionRiskEvaluation.getSuccessionReadiness());
        dto.setRiskLevel(positionRiskEvaluation.getRiskLevel());
        dto.setRecommendedAction(positionRiskEvaluation.getRecommendedAction());
        return dto;
    }
}
