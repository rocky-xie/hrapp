package top.btmdc.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PositionAssignment} entity.
 */
@Schema(description = "职位任职。记录职员与职位的多段任职历史。")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean primaryOwner;

    private LocalDate startDate;

    private LocalDate endDate;

    @Lob
    private String responsibilityScope;

    @NotNull
    private Boolean active;

    @NotNull
    private PersonDTO person;

    @NotNull
    private PositionDTO position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(Boolean primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getResponsibilityScope() {
        return responsibilityScope;
    }

    public void setResponsibilityScope(String responsibilityScope) {
        this.responsibilityScope = responsibilityScope;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionAssignmentDTO)) {
            return false;
        }

        PositionAssignmentDTO positionAssignmentDTO = (PositionAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, positionAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionAssignmentDTO{" +
            "id=" + getId() +
            ", primaryOwner='" + getPrimaryOwner() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", responsibilityScope='" + getResponsibilityScope() + "'" +
            ", active='" + getActive() + "'" +
            ", person=" + getPerson() +
            ", position=" + getPosition() +
            "}";
    }
}
