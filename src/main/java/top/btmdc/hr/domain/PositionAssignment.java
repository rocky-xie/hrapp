package top.btmdc.hr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 职位任职。记录职员与职位的多段任职历史。
 */
@Entity
@Table(name = "position_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionAssignment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "primary_owner", nullable = false)
    private Boolean primaryOwner;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Lob
    @Column(name = "responsibility_scope")
    private String responsibilityScope;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPrimaryOwner() {
        return this.primaryOwner;
    }

    public PositionAssignment primaryOwner(Boolean primaryOwner) {
        this.setPrimaryOwner(primaryOwner);
        return this;
    }

    public void setPrimaryOwner(Boolean primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public PositionAssignment startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PositionAssignment endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getResponsibilityScope() {
        return this.responsibilityScope;
    }

    public PositionAssignment responsibilityScope(String responsibilityScope) {
        this.setResponsibilityScope(responsibilityScope);
        return this;
    }

    public void setResponsibilityScope(String responsibilityScope) {
        this.responsibilityScope = responsibilityScope;
    }

    public Boolean getActive() {
        return this.active;
    }

    public PositionAssignment active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PositionAssignment person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionAssignment position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((PositionAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionAssignment{" +
            "id=" + getId() +
            ", primaryOwner='" + getPrimaryOwner() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", responsibilityScope='" + getResponsibilityScope() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
