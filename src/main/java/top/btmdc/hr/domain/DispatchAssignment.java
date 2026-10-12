package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A DispatchAssignment.
 */
@Entity
@Table(name = "dispatch_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchAssignment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "person_id")
    private Long personId;

    @NotNull
    @Column(name = "partner_organization_id", nullable = false)
    private Long partnerOrganizationId;

    @NotNull
    @Size(max = 100)
    @Column(name = "department", length = 100, nullable = false)
    private String department;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "expected_end_date")
    private LocalDate expectedEndDate;

    @Min(value = 0)
    @Column(name = "observation_days")
    private Integer observationDays;

    @NotNull
    @Size(max = 30)
    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Size(max = 40)
    @Column(name = "final_decision", length = 40)
    private String finalDecision;

    @Lob
    @Column(name = "note")
    private String note;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dispatchAssignment")
    @JsonIgnoreProperties(value = { "dispatchAssignment" }, allowSetters = true)
    private Set<DispatchReview> reviewses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "stageses",
            "eventses",
            "activitieses",
            "evaluationses",
            "decisionses",
            "messageses",
            "followUpses",
            "dispatchAssignmentses",
            "candidate",
            "requisition",
            "requisitionVersion",
        },
        allowSetters = true
    )
    private RecruitmentApplication application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DispatchAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return this.candidateId;
    }

    public DispatchAssignment candidateId(Long candidateId) {
        this.setCandidateId(candidateId);
        return this;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getPersonId() {
        return this.personId;
    }

    public DispatchAssignment personId(Long personId) {
        this.setPersonId(personId);
        return this;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPartnerOrganizationId() {
        return this.partnerOrganizationId;
    }

    public DispatchAssignment partnerOrganizationId(Long partnerOrganizationId) {
        this.setPartnerOrganizationId(partnerOrganizationId);
        return this;
    }

    public void setPartnerOrganizationId(Long partnerOrganizationId) {
        this.partnerOrganizationId = partnerOrganizationId;
    }

    public String getDepartment() {
        return this.department;
    }

    public DispatchAssignment department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getPositionId() {
        return this.positionId;
    }

    public DispatchAssignment positionId(Long positionId) {
        this.setPositionId(positionId);
        return this;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public DispatchAssignment startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return this.expectedEndDate;
    }

    public DispatchAssignment expectedEndDate(LocalDate expectedEndDate) {
        this.setExpectedEndDate(expectedEndDate);
        return this;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Integer getObservationDays() {
        return this.observationDays;
    }

    public DispatchAssignment observationDays(Integer observationDays) {
        this.setObservationDays(observationDays);
        return this;
    }

    public void setObservationDays(Integer observationDays) {
        this.observationDays = observationDays;
    }

    public String getStatus() {
        return this.status;
    }

    public DispatchAssignment status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalDecision() {
        return this.finalDecision;
    }

    public DispatchAssignment finalDecision(String finalDecision) {
        this.setFinalDecision(finalDecision);
        return this;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public String getNote() {
        return this.note;
    }

    public DispatchAssignment note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<DispatchReview> getReviewses() {
        return this.reviewses;
    }

    public void setReviewses(Set<DispatchReview> dispatchReviews) {
        if (this.reviewses != null) {
            this.reviewses.forEach(i -> i.setDispatchAssignment(null));
        }
        if (dispatchReviews != null) {
            dispatchReviews.forEach(i -> i.setDispatchAssignment(this));
        }
        this.reviewses = dispatchReviews;
    }

    public DispatchAssignment reviewses(Set<DispatchReview> dispatchReviews) {
        this.setReviewses(dispatchReviews);
        return this;
    }

    public DispatchAssignment addReviews(DispatchReview dispatchReview) {
        this.reviewses.add(dispatchReview);
        dispatchReview.setDispatchAssignment(this);
        return this;
    }

    public DispatchAssignment removeReviews(DispatchReview dispatchReview) {
        this.reviewses.remove(dispatchReview);
        dispatchReview.setDispatchAssignment(null);
        return this;
    }

    public RecruitmentApplication getApplication() {
        return this.application;
    }

    public void setApplication(RecruitmentApplication recruitmentApplication) {
        this.application = recruitmentApplication;
    }

    public DispatchAssignment application(RecruitmentApplication recruitmentApplication) {
        this.setApplication(recruitmentApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DispatchAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((DispatchAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchAssignment{" +
            "id=" + getId() +
            ", candidateId=" + getCandidateId() +
            ", personId=" + getPersonId() +
            ", partnerOrganizationId=" + getPartnerOrganizationId() +
            ", department='" + getDepartment() + "'" +
            ", positionId=" + getPositionId() +
            ", startDate='" + getStartDate() + "'" +
            ", expectedEndDate='" + getExpectedEndDate() + "'" +
            ", observationDays=" + getObservationDays() +
            ", status='" + getStatus() + "'" +
            ", finalDecision='" + getFinalDecision() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
