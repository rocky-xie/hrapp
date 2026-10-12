package top.btmdc.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.RecruitmentRequisitionMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String username;

    @NotNull
    @Size(max = 30)
    private String memberType;

    @NotNull
    private Boolean active;

    @NotNull
    private Instant createdAt;

    private Instant removedAt;

    private RecruitmentRequisitionDTO requisition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Instant removedAt) {
        this.removedAt = removedAt;
    }

    public RecruitmentRequisitionDTO getRequisition() {
        return requisition;
    }

    public void setRequisition(RecruitmentRequisitionDTO requisition) {
        this.requisition = requisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionMemberDTO)) {
            return false;
        }

        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO = (RecruitmentRequisitionMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentRequisitionMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionMemberDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            ", requisition=" + getRequisition() +
            "}";
    }
}
