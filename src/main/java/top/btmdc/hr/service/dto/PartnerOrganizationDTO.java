package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.PartnerOrganization} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerOrganizationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String partnerCode;

    @NotNull
    @Size(max = 150)
    private String name;

    @Lob
    private String rolesJson;

    @NotNull
    private Boolean dispatchEnabled;

    @Min(value = 0)
    private Integer defaultObservationDays;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRolesJson() {
        return rolesJson;
    }

    public void setRolesJson(String rolesJson) {
        this.rolesJson = rolesJson;
    }

    public Boolean getDispatchEnabled() {
        return dispatchEnabled;
    }

    public void setDispatchEnabled(Boolean dispatchEnabled) {
        this.dispatchEnabled = dispatchEnabled;
    }

    public Integer getDefaultObservationDays() {
        return defaultObservationDays;
    }

    public void setDefaultObservationDays(Integer defaultObservationDays) {
        this.defaultObservationDays = defaultObservationDays;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerOrganizationDTO)) {
            return false;
        }

        PartnerOrganizationDTO partnerOrganizationDTO = (PartnerOrganizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partnerOrganizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerOrganizationDTO{" +
            "id=" + getId() +
            ", partnerCode='" + getPartnerCode() + "'" +
            ", name='" + getName() + "'" +
            ", rolesJson='" + getRolesJson() + "'" +
            ", dispatchEnabled='" + getDispatchEnabled() + "'" +
            ", defaultObservationDays=" + getDefaultObservationDays() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
