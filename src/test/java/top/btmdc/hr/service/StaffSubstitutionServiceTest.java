package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.domain.enumeration.LevelCode;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.PositionSkillRequirementRepository;
import top.btmdc.hr.repository.StaffSubstitutionRepository;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;
import top.btmdc.hr.service.mapper.StaffSubstitutionMapper;

@ExtendWith(MockitoExtension.class)
class StaffSubstitutionServiceTest {

    @Mock
    private StaffSubstitutionRepository staffSubstitutionRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PositionSkillRequirementRepository positionSkillRequirementRepository;

    @Mock
    private PersonSkillRepository personSkillRepository;

    @Mock
    private StaffSubstitutionMapper staffSubstitutionMapper;

    @Captor
    private ArgumentCaptor<StaffSubstitution> substitutionCaptor;

    private StaffSubstitutionService staffSubstitutionService;

    @BeforeEach
    void setUp() {
        staffSubstitutionService = new StaffSubstitutionService(
            staffSubstitutionRepository,
            positionRepository,
            personRepository,
            positionSkillRequirementRepository,
            personSkillRepository,
            staffSubstitutionMapper
        );
    }

    @Test
    void calculateShouldMarkSubstitutableWhenCoverageReachesThreshold() {
        Position position = position(1L, "Support");
        Person candidate = person(2L, "B");
        Skill java = skill(10L, "Java");
        Skill support = skill(11L, "Support");

        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(personRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(personSkillRepository.findByPersonIdWithSkillAndLevel(2L)).thenReturn(
            List.of(personSkill(candidate, java, 3), personSkill(candidate, support, 2))
        );
        when(positionSkillRequirementRepository.findByPositionIdWithSkillAndRequiredLevel(1L)).thenReturn(
            List.of(requirement(position, java, 2), requirement(position, support, 2))
        );
        when(staffSubstitutionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(staffSubstitutionMapper.toDto(any(StaffSubstitution.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        StaffSubstitutionDTO result = staffSubstitutionService.calculate(1L, 2L, BigDecimal.valueOf(80));

        assertThat(result.getCoverageRate()).isEqualByComparingTo("100.00");
        assertThat(result.getSubstitutable()).isTrue();
        assertThat(result.getTotalSkillCount()).isEqualTo(2);
        assertThat(result.getCoveredSkillCount()).isEqualTo(2);
        assertThat(result.getMissingSkills()).isEmpty();
    }

    @Test
    void calculateShouldRecordMissingSkillsWhenCoverageIsBelowThreshold() {
        Position position = position(1L, "Support");
        Person candidate = person(2L, "B");
        Skill java = skill(10L, "Java");
        Skill support = skill(11L, "Support");

        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(personRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(personSkillRepository.findByPersonIdWithSkillAndLevel(2L)).thenReturn(List.of(personSkill(candidate, java, 1)));
        when(positionSkillRequirementRepository.findByPositionIdWithSkillAndRequiredLevel(1L)).thenReturn(
            List.of(requirement(position, java, 2), requirement(position, support, 1))
        );
        when(staffSubstitutionRepository.save(substitutionCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        when(staffSubstitutionMapper.toDto(any(StaffSubstitution.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        StaffSubstitutionDTO result = staffSubstitutionService.calculate(1L, 2L, null);

        assertThat(result.getCoverageRate()).isEqualByComparingTo("0.00");
        assertThat(result.getThresholdRate()).isEqualByComparingTo("80");
        assertThat(result.getSubstitutable()).isFalse();
        assertThat(substitutionCaptor.getValue().getMissingSkills()).contains("Java").contains("Support");
    }

    private Person person(Long id, String name) {
        return new Person().id(id).personName(name);
    }

    private Position position(Long id, String name) {
        return new Position().id(id).positionName(name);
    }

    private Skill skill(Long id, String name) {
        return new Skill().id(id).skillName(name);
    }

    private PersonSkill personSkill(Person person, Skill skill, int sortOrder) {
        return new PersonSkill().person(person).skill(skill).currentLevel(level(sortOrder));
    }

    private PositionSkillRequirement requirement(Position position, Skill skill, int sortOrder) {
        return new PositionSkillRequirement().position(position).skill(skill).requiredLevel(level(sortOrder));
    }

    private SkillLevel level(int sortOrder) {
        return new SkillLevel().code(LevelCode.values()[sortOrder]).sortOrder(sortOrder);
    }

    private StaffSubstitutionDTO toDto(StaffSubstitution staffSubstitution) {
        StaffSubstitutionDTO dto = new StaffSubstitutionDTO();
        dto.setCoverageRate(staffSubstitution.getCoverageRate());
        dto.setThresholdRate(staffSubstitution.getThresholdRate());
        dto.setTotalSkillCount(staffSubstitution.getTotalSkillCount());
        dto.setCoveredSkillCount(staffSubstitution.getCoveredSkillCount());
        dto.setMissingSkills(staffSubstitution.getMissingSkills());
        dto.setSubstitutable(staffSubstitution.getSubstitutable());
        return dto;
    }
}
