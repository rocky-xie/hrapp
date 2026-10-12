package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A PartnerOrganization.
 */
@Entity
@Table(name = "partner_organization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerOrganization implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "partner_code", length = 40, nullable = false, unique = true)
    private String partnerCode;

    @NotNull
    @Size(max = 150)
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Lob
    @Column(name = "roles_json", nullable = false)
    private String rolesJson;

    @NotNull
    @Column(name = "dispatch_enabled", nullable = false)
    private Boolean dispatchEnabled;

    @Min(value = 0)
    @Column(name = "default_observation_days")
    private Integer defaultObservationDays;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartnerOrganization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public PartnerOrganization partnerCode(String partnerCode) {
        this.setPartnerCode(partnerCode);
        return this;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getName() {
        return this.name;
    }

    public PartnerOrganization name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRolesJson() {
        return this.rolesJson;
    }

    public PartnerOrganization rolesJson(String rolesJson) {
        this.setRolesJson(rolesJson);
        return this;
    }

    public void setRolesJson(String rolesJson) {
        this.rolesJson = rolesJson;
    }

    public Boolean getDispatchEnabled() {
        return this.dispatchEnabled;
    }

    public PartnerOrganization dispatchEnabled(Boolean dispatchEnabled) {
        this.setDispatchEnabled(dispatchEnabled);
        return this;
    }

    public void setDispatchEnabled(Boolean dispatchEnabled) {
        this.dispatchEnabled = dispatchEnabled;
    }

    public Integer getDefaultObservationDays() {
        return this.defaultObservationDays;
    }

    public PartnerOrganization defaultObservationDays(Integer defaultObservationDays) {
        this.setDefaultObservationDays(defaultObservationDays);
        return this;
    }

    public void setDefaultObservationDays(Integer defaultObservationDays) {
        this.defaultObservationDays = defaultObservationDays;
    }

    public Boolean getActive() {
        return this.active;
    }

    public PartnerOrganization active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerOrganization)) {
            return false;
        }
        return getId() != null && getId().equals(((PartnerOrganization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerOrganization{" +
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
