package com.instituna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.instituna.domain.enumeration.InstrumentType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Instrument.
 */
@Table("instrument")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Instrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("created_by")
    private Long createdBy;

    @Column("created_on")
    private Instant createdOn;

    @Column("updated_by")
    private Long updatedBy;

    @Column("updated_on")
    private Instant updatedOn;

    @Column("deleted_by")
    private Long deletedBy;

    @Column("deleted_on")
    private Instant deletedOn;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("type")
    private InstrumentType type;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("bought_date")
    private LocalDate boughtDate;

    @DecimalMin(value = "0")
    @Column("price")
    private BigDecimal price;

    @Transient
    @JsonIgnoreProperties(value = { "instrument", "member" }, allowSetters = true)
    private Set<InstrumentRequest> requests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Instrument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public Instrument createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Instrument createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public Instrument updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Instrument updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return this.deletedBy;
    }

    public Instrument deletedBy(Long deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedOn() {
        return this.deletedOn;
    }

    public Instrument deletedOn(Instant deletedOn) {
        this.setDeletedOn(deletedOn);
        return this;
    }

    public void setDeletedOn(Instant deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getName() {
        return this.name;
    }

    public Instrument name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstrumentType getType() {
        return this.type;
    }

    public Instrument type(InstrumentType type) {
        this.setType(type);
        return this;
    }

    public void setType(InstrumentType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Instrument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Instrument isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getBoughtDate() {
        return this.boughtDate;
    }

    public Instrument boughtDate(LocalDate boughtDate) {
        this.setBoughtDate(boughtDate);
        return this;
    }

    public void setBoughtDate(LocalDate boughtDate) {
        this.boughtDate = boughtDate;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Instrument price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price.stripTrailingZeros() : null;
    }

    public Set<InstrumentRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<InstrumentRequest> instrumentRequests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setInstrument(null));
        }
        if (instrumentRequests != null) {
            instrumentRequests.forEach(i -> i.setInstrument(this));
        }
        this.requests = instrumentRequests;
    }

    public Instrument requests(Set<InstrumentRequest> instrumentRequests) {
        this.setRequests(instrumentRequests);
        return this;
    }

    public Instrument addRequests(InstrumentRequest instrumentRequest) {
        this.requests.add(instrumentRequest);
        instrumentRequest.setInstrument(this);
        return this;
    }

    public Instrument removeRequests(InstrumentRequest instrumentRequest) {
        this.requests.remove(instrumentRequest);
        instrumentRequest.setInstrument(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrument)) {
            return false;
        }
        return id != null && id.equals(((Instrument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instrument{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", deletedBy=" + getDeletedBy() +
            ", deletedOn='" + getDeletedOn() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", boughtDate='" + getBoughtDate() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
