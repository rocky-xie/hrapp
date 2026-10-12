package top.btmdc.hr.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.DispatchAssignment;
import top.btmdc.hr.domain.DispatchReview;
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

/** HR-managed dispatch observation and regularization lifecycle. */
@Service
@Transactional
public class DispatchLifecycleService {

    private final DispatchAssignmentRepository assignments;
    private final DispatchReviewRepository reviews;
    private final PartnerOrganizationRepository partners;
    private final RecruitmentApplicationRepository applications;
    private final PersonRepository people;
    private final PositionAssignmentRepository positionAssignments;

    public DispatchLifecycleService(
        DispatchAssignmentRepository assignments,
        DispatchReviewRepository reviews,
        PartnerOrganizationRepository partners,
        RecruitmentApplicationRepository applications,
        PersonRepository people,
        PositionAssignmentRepository positionAssignments
    ) {
        this.assignments = assignments;
        this.reviews = reviews;
        this.partners = partners;
        this.applications = applications;
        this.people = people;
        this.positionAssignments = positionAssignments;
    }

    public DispatchAssignment start(
        Long applicationId,
        Long partnerId,
        String department,
        Long positionId,
        LocalDate startDate,
        String actor
    ) {
        RecruitmentApplication application = applications
            .findById(applicationId)
            .orElseThrow(() -> new NoSuchElementException("Application not found"));
        if (application.getDecision() != ApplicationDecision.ACCEPT_AS_DISPATCHED || application.getConvertedPersonId() == null) {
            throw new IllegalStateException("Dispatch assignment requires a converted dispatch hire");
        }
        if (assignments.existsByApplication_Id(applicationId)) throw new IllegalStateException("Dispatch assignment already exists");
        PartnerOrganization partner = partners
            .findById(partnerId)
            .orElseThrow(() -> new NoSuchElementException("Partner organization not found"));
        if (!Boolean.TRUE.equals(partner.getActive()) || !Boolean.TRUE.equals(partner.getDispatchEnabled())) {
            throw new IllegalStateException("Partner is not enabled for dispatch hiring");
        }
        if (startDate == null || department == null || department.isBlank()) throw new IllegalArgumentException(
            "Start date and department are required"
        );
        Person person = people
            .findById(application.getConvertedPersonId())
            .orElseThrow(() -> new NoSuchElementException("Converted person not found"));
        person.setEmploymentType("DISPATCHED");
        return assignments.save(
            new DispatchAssignment()
                .candidateId(application.getCandidate().getId())
                .personId(person.getId())
                .partnerOrganizationId(partner.getId())
                .department(department)
                .positionId(positionId)
                .startDate(startDate)
                .expectedEndDate(
                    partner.getDefaultObservationDays() == null ? null : startDate.plusDays(partner.getDefaultObservationDays())
                )
                .observationDays(partner.getDefaultObservationDays())
                .status("ACTIVE")
                .note("Started by " + actor)
                .application(application)
        );
    }

    public DispatchReview review(Long assignmentId, String decision, String result, LocalDate extensionEndDate, String actor) {
        DispatchAssignment assignment = assignments
            .findById(assignmentId)
            .orElseThrow(() -> new NoSuchElementException("Dispatch assignment not found"));
        if (!"ACTIVE".equals(assignment.getStatus())) throw new IllegalStateException("Dispatch assignment is not active");
        if (!java.util.Set.of("CONTINUE", "REGULARIZE", "EXTEND", "END").contains(decision)) throw new IllegalArgumentException(
            "Unsupported dispatch review decision"
        );
        if ("EXTEND".equals(decision) && (extensionEndDate == null || !extensionEndDate.isAfter(LocalDate.now()))) {
            throw new IllegalArgumentException("A future extension end date is required");
        }
        DispatchReview review = reviews.save(
            new DispatchReview()
                .reviewedAt(Instant.now())
                .reviewerUsername(actor)
                .result(result)
                .recommendation(decision)
                .finalDecision(decision)
                .extensionEndDate(extensionEndDate)
                .dispatchAssignment(assignment)
        );
        if ("REGULARIZE".equals(decision)) {
            Person person = people
                .findById(assignment.getPersonId())
                .orElseThrow(() -> new NoSuchElementException("Dispatch person not found"));
            person.setEmploymentType("REGULAR");
            assignment.setStatus("REGULARIZED");
            assignment.setFinalDecision(decision);
        } else if ("EXTEND".equals(decision)) {
            assignment.setExpectedEndDate(extensionEndDate);
            assignment.setFinalDecision(decision);
        } else if ("END".equals(decision)) {
            assignment.setStatus("ENDED");
            assignment.setFinalDecision(decision);
            for (PositionAssignment positionAssignment : positionAssignments.findByPerson_IdAndActiveTrue(assignment.getPersonId())) {
                positionAssignment.setActive(false);
                positionAssignment.setEndDate(LocalDate.now());
            }
        }
        assignments.save(assignment);
        return review;
    }
}
