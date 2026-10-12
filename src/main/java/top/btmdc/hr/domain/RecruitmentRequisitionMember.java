package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A RecruitmentRequisitionMember.
 */
@Entity
@Table(name = "recruitment_requisition_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @NotNull
    @Size(max = 30)
    @Column(name = "member_type", length = 30, nullable = false)
    private String memberType;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "removed_at")
    private Instant removedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "versionses", "memberses", "applicationses" }, allowSetters = true)
    private RecruitmentRequisition requisition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentRequisitionMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public RecruitmentRequisitionMember username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemberType() {
        return this.memberType;
    }

    public RecruitmentRequisitionMember memberType(String memberType) {
        this.setMemberType(memberType);
        return this;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Boolean getActive() {
        return this.active;
    }

    public RecruitmentRequisitionMember active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public RecruitmentRequisitionMember createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRemovedAt() {
        return this.removedAt;
    }

    public RecruitmentRequisitionMember removedAt(Instant removedAt) {
        this.setRemovedAt(removedAt);
        return this;
    }

    public void setRemovedAt(Instant removedAt) {
        this.removedAt = removedAt;
    }

    public RecruitmentRequisition getRequisition() {
        return this.requisition;
    }

    public void setRequisition(RecruitmentRequisition recruitmentRequisition) {
        this.requisition = recruitmentRequisition;
    }

    public RecruitmentRequisitionMember requisition(RecruitmentRequisition recruitmentRequisition) {
        this.setRequisition(recruitmentRequisition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionMember)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentRequisitionMember) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionMember{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
