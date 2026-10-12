package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DispatchReview.
 */
@Entity
@Table(name = "dispatch_review")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DispatchReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reviewed_at", nullable = false)
    private Instant reviewedAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "reviewer_username", length = 100, nullable = false)
    private String reviewerUsername;

    @Lob
    @Column(name = "result")
    private String result;

    @Size(max = 40)
    @Column(name = "recommendation", length = 40)
    private String recommendation;

    @Column(name = "extension_end_date")
    private LocalDate extensionEndDate;

    @Size(max = 40)
    @Column(name = "final_decision", length = 40)
    private String finalDecision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewses", "application" }, allowSetters = true)
    private DispatchAssignment dispatchAssignment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DispatchReview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReviewedAt() {
        return this.reviewedAt;
    }

    public DispatchReview reviewedAt(Instant reviewedAt) {
        this.setReviewedAt(reviewedAt);
        return this;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewerUsername() {
        return this.reviewerUsername;
    }

    public DispatchReview reviewerUsername(String reviewerUsername) {
        this.setReviewerUsername(reviewerUsername);
        return this;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public String getResult() {
        return this.result;
    }

    public DispatchReview result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRecommendation() {
        return this.recommendation;
    }

    public DispatchReview recommendation(String recommendation) {
        this.setRecommendation(recommendation);
        return this;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDate getExtensionEndDate() {
        return this.extensionEndDate;
    }

    public DispatchReview extensionEndDate(LocalDate extensionEndDate) {
        this.setExtensionEndDate(extensionEndDate);
        return this;
    }

    public void setExtensionEndDate(LocalDate extensionEndDate) {
        this.extensionEndDate = extensionEndDate;
    }

    public String getFinalDecision() {
        return this.finalDecision;
    }

    public DispatchReview finalDecision(String finalDecision) {
        this.setFinalDecision(finalDecision);
        return this;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public DispatchAssignment getDispatchAssignment() {
        return this.dispatchAssignment;
    }

    public void setDispatchAssignment(DispatchAssignment dispatchAssignment) {
        this.dispatchAssignment = dispatchAssignment;
    }

    public DispatchReview dispatchAssignment(DispatchAssignment dispatchAssignment) {
        this.setDispatchAssignment(dispatchAssignment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DispatchReview)) {
            return false;
        }
        return getId() != null && getId().equals(((DispatchReview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchReview{" +
            "id=" + getId() +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", reviewerUsername='" + getReviewerUsername() + "'" +
            ", result='" + getResult() + "'" +
            ", recommendation='" + getRecommendation() + "'" +
            ", extensionEndDate='" + getExtensionEndDate() + "'" +
            ", finalDecision='" + getFinalDecision() + "'" +
            "}";
    }
}
