package com.instituna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A InstrumentRequest.
 */
@Table("instrument_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InstrumentRequest implements Serializable {

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

    @Column("description")
    private String description;

    @Column("is_returned")
    private Boolean isReturned;

    @Transient
    @JsonIgnoreProperties(value = { "requests" }, allowSetters = true)
    private Instrument instrument;

    @Transient
    @JsonIgnoreProperties(value = { "user", "tags", "requests" }, allowSetters = true)
    private Member member;

    @Column("instrument_id")
    private Long instrumentId;

    @Column("member_id")
    private Long memberId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InstrumentRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public InstrumentRequest createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public InstrumentRequest createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public InstrumentRequest updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public InstrumentRequest updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return this.deletedBy;
    }

    public InstrumentRequest deletedBy(Long deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedOn() {
        return this.deletedOn;
    }

    public InstrumentRequest deletedOn(Instant deletedOn) {
        this.setDeletedOn(deletedOn);
        return this;
    }

    public void setDeletedOn(Instant deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getDescription() {
        return this.description;
    }

    public InstrumentRequest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsReturned() {
        return this.isReturned;
    }

    public InstrumentRequest isReturned(Boolean isReturned) {
        this.setIsReturned(isReturned);
        return this;
    }

    public void setIsReturned(Boolean isReturned) {
        this.isReturned = isReturned;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
        this.instrumentId = instrument != null ? instrument.getId() : null;
    }

    public InstrumentRequest instrument(Instrument instrument) {
        this.setInstrument(instrument);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
        this.memberId = member != null ? member.getId() : null;
    }

    public InstrumentRequest member(Member member) {
        this.setMember(member);
        return this;
    }

    public Long getInstrumentId() {
        return this.instrumentId;
    }

    public void setInstrumentId(Long instrument) {
        this.instrumentId = instrument;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long member) {
        this.memberId = member;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstrumentRequest)) {
            return false;
        }
        return id != null && id.equals(((InstrumentRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstrumentRequest{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", deletedBy=" + getDeletedBy() +
            ", deletedOn='" + getDeletedOn() + "'" +
            ", description='" + getDescription() + "'" +
            ", isReturned='" + getIsReturned() + "'" +
            "}";
    }
}
