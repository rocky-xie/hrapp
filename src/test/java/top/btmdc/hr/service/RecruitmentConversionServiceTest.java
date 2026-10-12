package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.domain.enumeration.ApplicationStatus;
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.ApplicationEventRepository;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;

@ExtendWith(MockitoExtension.class)
class RecruitmentConversionServiceTest {

    @Mock
    private RecruitmentApplicationRepository applications;

    @Mock
    private RecruitmentRequisitionRepository requisitions;

    @Mock
    private PersonRepository people;

    @Mock
    private PositionRepository positions;

    @Mock
    private PositionAssignmentRepository assignments;

    @Mock
    private ApplicationEventRepository events;

    private RecruitmentConversionService service;
    private RecruitmentApplication application;
    private RecruitmentRequisition requisition;
    private ExternalCandidate candidate;

    @BeforeEach
    void setUp() {
        service = new RecruitmentConversionService(applications, requisitions, people, positions, assignments, events);
        requisition = new RecruitmentRequisition().id(12L).department("Engineering").hiredCount(0).checkedInCount(0);
        candidate = new ExternalCandidate().id(22L).fullName("Lin Candidate");
        application = new RecruitmentApplication()
            .id(33L)
            .recruitmentType(RecruitmentType.GRADUATE)
            .status(ApplicationStatus.DECIDED)
            .decision(ApplicationDecision.HIRED)
            .candidate(candidate)
            .requisition(requisition);
    }

    @Test
    void approvedGraduateCanBeConvertedIntoNewcomerPerson() {
        when(applications.findById(33L)).thenReturn(Optional.of(application));
        when(people.save(any())).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            person.setId(77L);
            return person;
        });
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(requisitions.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication converted = service.convertGraduate(33L, "hr-user");

        assertThat(converted.getConvertedPersonId()).isEqualTo(77L);
        assertThat(converted.getConvertedAt()).isNotNull();
        assertThat(candidate.getConvertedPersonId()).isEqualTo(77L);
        assertThat(requisition.getHiredCount()).isEqualTo(1);
        verify(people).save(
            argThat(
                person ->
                    person.getPersonName().equals("Lin Candidate") &&
                    person.getDepartment().equals("Engineering") &&
                    person.getEmploymentStatus() == EmploymentStatus.NEWCOMER
            )
        );
        verify(events).save(any());
        verifyNoInteractions(assignments);
    }

    @Test
    void nonGraduateOrUnapprovedCandidateCannotBeConverted() {
        application.setRecruitmentType(RecruitmentType.MID_CAREER);
        when(applications.findById(33L)).thenReturn(Optional.of(application));

        assertThatThrownBy(() -> service.convertGraduate(33L, "hr-user")).isInstanceOf(IllegalStateException.class);
        verifyNoInteractions(people, events);
    }

    @Test
    void midCareerHireIsConvertedAtCheckInAndCountsAreUpdated() {
        application.setRecruitmentType(RecruitmentType.MID_CAREER);
        when(applications.findById(33L)).thenReturn(Optional.of(application));
        when(people.save(any())).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            person.setId(78L);
            return person;
        });
        when(applications.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(requisitions.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentApplication checkedIn = service.checkIn(33L, LocalDate.of(2027, 4, 1), "hr-user");

        assertThat(checkedIn.getConvertedPersonId()).isEqualTo(78L);
        assertThat(checkedIn.getConvertedAt()).isNotNull();
        assertThat(checkedIn.getStatus()).isEqualTo(ApplicationStatus.CLOSED);
        assertThat(checkedIn.getRetentionUntil()).isNull();
        assertThat(requisition.getHiredCount()).isEqualTo(1);
        assertThat(requisition.getCheckedInCount()).isEqualTo(1);
        verify(events).save(any());
        verifyNoInteractions(assignments);
    }
}
