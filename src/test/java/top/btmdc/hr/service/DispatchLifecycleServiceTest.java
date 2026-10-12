package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.domain.PartnerOrganization;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.ApplicationDecision;
import top.btmdc.hr.repository.DispatchAssignmentRepository;
import top.btmdc.hr.repository.DispatchReviewRepository;
import top.btmdc.hr.repository.PartnerOrganizationRepository;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PositionAssignmentRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;

class DispatchLifecycleServiceTest {

    @Mock
    private DispatchAssignmentRepository assignments;

    @Mock
    private DispatchReviewRepository reviews;

    @Mock
    private PartnerOrganizationRepository partners;

    @Mock
    private RecruitmentApplicationRepository applications;

    @Mock
    private PersonRepository people;

    @Mock
    private PositionAssignmentRepository positionAssignments;

    private DispatchLifecycleService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DispatchLifecycleService(assignments, reviews, partners, applications, people, positionAssignments);
    }

    @Test
    void startsDispatchWithConfiguredObservationPeriod() {
        RecruitmentApplication application = new RecruitmentApplication()
            .decision(ApplicationDecision.ACCEPT_AS_DISPATCHED)
            .convertedPersonId(70L)
            .candidate(new ExternalCandidate().id(12L));
        PartnerOrganization partner = new PartnerOrganization().id(4L).active(true).dispatchEnabled(true).defaultObservationDays(90);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(assignments.existsByApplication_Id(3L)).thenReturn(false);
        when(partners.findById(4L)).thenReturn(Optional.of(partner));
        when(people.findById(70L)).thenReturn(Optional.of(new Person().id(70L)));
        when(assignments.save(any(DispatchAssignment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DispatchAssignment assignment = service.start(3L, 4L, "Engineering", 5L, LocalDate.of(2027, 1, 1), "hr");

        assertThat(assignment.getStatus()).isEqualTo("ACTIVE");
        assertThat(assignment.getExpectedEndDate()).isEqualTo(LocalDate.of(2027, 4, 1));
        verify(assignments).save(any(DispatchAssignment.class));
    }

    @Test
    void rejectsPartnersWithoutDispatchCapability() {
        RecruitmentApplication application = new RecruitmentApplication()
            .decision(ApplicationDecision.ACCEPT_AS_DISPATCHED)
            .convertedPersonId(70L);
        when(applications.findById(3L)).thenReturn(Optional.of(application));
        when(assignments.existsByApplication_Id(3L)).thenReturn(false);
        when(partners.findById(4L)).thenReturn(Optional.of(new PartnerOrganization().active(true).dispatchEnabled(false)));

        assertThatThrownBy(() -> service.start(3L, 4L, "Engineering", null, LocalDate.now(), "hr"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("not enabled");
        verify(assignments, never()).save(any());
    }

    @Test
    void regularizationChangesEmploymentTypeAndClosesDispatchAssignment() {
        DispatchAssignment assignment = new DispatchAssignment().id(8L).personId(70L).status("ACTIVE");
        Person person = new Person().id(70L).employmentType("DISPATCHED");
        when(assignments.findById(8L)).thenReturn(Optional.of(assignment));
        when(people.findById(70L)).thenReturn(Optional.of(person));
        when(reviews.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(assignments.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.review(8L, "REGULARIZE", "Approved for regular employment", null, "hr");

        assertThat(person.getEmploymentType()).isEqualTo("REGULAR");
        assertThat(assignment.getStatus()).isEqualTo("REGULARIZED");
        assertThat(assignment.getFinalDecision()).isEqualTo("REGULARIZE");
    }

    @Test
    void endingDispatchClosesItsActivePositionAssignment() {
        DispatchAssignment assignment = new DispatchAssignment().id(8L).personId(70L).status("ACTIVE");
        PositionAssignment position = new PositionAssignment().active(true);
        when(assignments.findById(8L)).thenReturn(Optional.of(assignment));
        when(reviews.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(assignments.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(positionAssignments.findByPerson_IdAndActiveTrue(70L)).thenReturn(List.of(position));

        service.review(8L, "END", "Dispatch ended", null, "hr");

        assertThat(assignment.getStatus()).isEqualTo("ENDED");
        assertThat(position.getActive()).isFalse();
        assertThat(position.getEndDate()).isEqualTo(LocalDate.now());
    }
}
