package top.btmdc.hr.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
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

/** Converts an externally hired candidate into the existing employee domain and records check-in. */
@Service
@Transactional
public class RecruitmentConversionService {

    private final RecruitmentApplicationRepository applications;
    private final RecruitmentRequisitionRepository requisitions;
    private final PersonRepository people;
    private final PositionRepository positions;
    private final PositionAssignmentRepository assignments;
    private final ApplicationEventRepository events;

    public RecruitmentConversionService(
        RecruitmentApplicationRepository applications,
        RecruitmentRequisitionRepository requisitions,
        PersonRepository people,
        PositionRepository positions,
        PositionAssignmentRepository assignments,
        ApplicationEventRepository events
    ) {
        this.applications = applications;
        this.requisitions = requisitions;
        this.people = people;
        this.positions = positions;
        this.assignments = assignments;
        this.events = events;
    }

    public RecruitmentApplication convertGraduate(Long applicationId, String actor) {
        RecruitmentApplication application = get(applicationId);
        if (
            application.getRecruitmentType() != RecruitmentType.GRADUATE ||
            application.getStatus() != ApplicationStatus.DECIDED ||
            application.getDecision() != ApplicationDecision.HIRED
        ) {
            throw new IllegalStateException("Only externally approved graduate hires can be converted");
        }
        if (application.getConvertedPersonId() != null) throw new IllegalStateException("Application is already converted");
        Person person = createPerson(application);
        application.setConvertedPersonId(person.getId());
        application.setConvertedAt(Instant.now());
        if (application.getCandidate() != null) application.getCandidate().setConvertedPersonId(person.getId());
        incrementHiredCount(application);
        events.save(event(application, actor, "EMPLOYEE_PREBOARDING_CREATED", person.getId().toString(), null));
        return applications.save(application);
    }

    public RecruitmentApplication checkIn(Long applicationId, LocalDate joinDate, String actor) {
        RecruitmentApplication application = get(applicationId);
        if (
            application.getStatus() != ApplicationStatus.DECIDED ||
            (application.getDecision() != ApplicationDecision.HIRED &&
                application.getDecision() != ApplicationDecision.ACCEPT_AS_DISPATCHED)
        ) {
            throw new IllegalStateException("Only final hires can check in");
        }
        if (joinDate == null) throw new IllegalArgumentException("Join date is required");
        Person person =
            application.getConvertedPersonId() == null
                ? createPerson(application)
                : people
                      .findById(application.getConvertedPersonId())
                      .orElseThrow(() -> new NoSuchElementException("Converted person not found"));
        if (person.getJoinDate() != null) throw new IllegalStateException("Person has already checked in");
        person.setJoinDate(joinDate);
        people.save(person);
        createPositionAssignment(application, person, joinDate);
        if (application.getConvertedPersonId() == null) {
            application.setConvertedPersonId(person.getId());
            application.setConvertedAt(Instant.now());
            if (application.getCandidate() != null) application.getCandidate().setConvertedPersonId(person.getId());
            incrementHiredCount(application);
        }
        RecruitmentRequisition requisition = application.getRequisition();
        if (requisition != null) {
            requisition.setCheckedInCount(valueOrZero(requisition.getCheckedInCount()) + 1);
            requisitions.save(requisition);
        }
        application.setStatus(ApplicationStatus.CLOSED);
        application.setClosedAt(Instant.now());
        application.setRetentionUntil(null);
        events.save(event(application, actor, "EMPLOYEE_CHECKED_IN", joinDate.toString(), null));
        return applications.save(application);
    }

    private Person createPerson(RecruitmentApplication application) {
        if (application.getCandidate() == null) throw new IllegalStateException("Application has no candidate");
        RecruitmentRequisition requisition = application.getRequisition();
        Person person = new Person()
            .personName(application.getCandidate().getFullName())
            .department(requisition == null ? null : requisition.getDepartment())
            .employmentStatus(EmploymentStatus.NEWCOMER)
            .employmentType(application.getDecision() == ApplicationDecision.ACCEPT_AS_DISPATCHED ? "DISPATCHED" : "REGULAR")
            .mentorFlag(false)
            .coreCandidateFlag(false);
        return people.save(person);
    }

    private void createPositionAssignment(RecruitmentApplication application, Person person, LocalDate joinDate) {
        RecruitmentRequisition requisition = application.getRequisition();
        if (requisition == null || requisition.getPositionId() == null) return;
        Position position = positions
            .findById(requisition.getPositionId())
            .orElseThrow(() -> new NoSuchElementException("Requisition position not found"));
        assignments.save(new PositionAssignment().primaryOwner(true).startDate(joinDate).active(true).person(person).position(position));
    }

    private void incrementHiredCount(RecruitmentApplication application) {
        RecruitmentRequisition requisition = application.getRequisition();
        if (requisition != null) {
            requisition.setHiredCount(valueOrZero(requisition.getHiredCount()) + 1);
            requisitions.save(requisition);
        }
    }

    private RecruitmentApplication get(Long id) {
        return applications.findById(id).orElseThrow(() -> new NoSuchElementException("Application not found: " + id));
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private ApplicationEvent event(RecruitmentApplication application, String actor, String eventType, String newValue, String reason) {
        return new ApplicationEvent()
            .eventType(eventType)
            .newValue(newValue)
            .actor(actor)
            .occurredAt(Instant.now())
            .reason(reason)
            .application(application);
    }
}
