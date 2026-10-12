package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.DispatchAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Long candidateId;

    private Long personId;

    @NotNull
    private Long partnerOrganizationId;

    @NotNull
    @Size(max = 100)
    private String department;

    private Long positionId;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    @Min(value = 0)
    private Integer observationDays;

    @NotNull
    @Size(max = 30)
    private String status;

    @Size(max = 40)
    private String finalDecision;

    @Lob
    private String note;

    private RecruitmentApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPartnerOrganizationId() {
        return partnerOrganizationId;
    }

    public void setPartnerOrganizationId(Long partnerOrganizationId) {
        this.partnerOrganizationId = partnerOrganizationId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Integer getObservationDays() {
        return observationDays;
    }

    public void setObservationDays(Integer observationDays) {
        this.observationDays = observationDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RecruitmentApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(RecruitmentApplicationDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DispatchAssignmentDTO)) {
            return false;
        }

        DispatchAssignmentDTO dispatchAssignmentDTO = (DispatchAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dispatchAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchAssignmentDTO{" +
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
            ", application=" + getApplication() +
            "}";
    }
}
