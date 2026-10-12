package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.DispatchReview} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchReviewDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant reviewedAt;

    @NotNull
    @Size(max = 100)
    private String reviewerUsername;

    @Lob
    private String result;

    @Size(max = 40)
    private String recommendation;

    private LocalDate extensionEndDate;

    @Size(max = 40)
    private String finalDecision;

    private DispatchAssignmentDTO dispatchAssignment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDate getExtensionEndDate() {
        return extensionEndDate;
    }

    public void setExtensionEndDate(LocalDate extensionEndDate) {
        this.extensionEndDate = extensionEndDate;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public DispatchAssignmentDTO getDispatchAssignment() {
        return dispatchAssignment;
    }

    public void setDispatchAssignment(DispatchAssignmentDTO dispatchAssignment) {
        this.dispatchAssignment = dispatchAssignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DispatchReviewDTO)) {
            return false;
        }

        DispatchReviewDTO dispatchReviewDTO = (DispatchReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dispatchReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchReviewDTO{" +
            "id=" + getId() +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", reviewerUsername='" + getReviewerUsername() + "'" +
            ", result='" + getResult() + "'" +
            ", recommendation='" + getRecommendation() + "'" +
            ", extensionEndDate='" + getExtensionEndDate() + "'" +
            ", finalDecision='" + getFinalDecision() + "'" +
            ", dispatchAssignment=" + getDispatchAssignment() +
            "}";
    }
}
