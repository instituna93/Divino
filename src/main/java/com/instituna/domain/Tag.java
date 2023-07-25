package com.instituna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Tag.
 */
@Table("tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

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
    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "defaultTag", "tags" }, allowSetters = true)
    private TagType tagType;

    @Transient
    @JsonIgnoreProperties(value = { "tag", "member" }, allowSetters = true)
    private Set<MemberTag> members = new HashSet<>();

    @Column("tag_type_id")
    private Long tagTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public Tag createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Tag createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public Tag updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Tag updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return this.deletedBy;
    }

    public Tag deletedBy(Long deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedOn() {
        return this.deletedOn;
    }

    public Tag deletedOn(Instant deletedOn) {
        this.setDeletedOn(deletedOn);
        return this;
    }

    public void setDeletedOn(Instant deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getDescription() {
        return this.description;
    }

    public Tag description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TagType getTagType() {
        return this.tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
        this.tagTypeId = tagType != null ? tagType.getId() : null;
    }

    public Tag tagType(TagType tagType) {
        this.setTagType(tagType);
        return this;
    }

    public Set<MemberTag> getMembers() {
        return this.members;
    }

    public void setMembers(Set<MemberTag> memberTags) {
        if (this.members != null) {
            this.members.forEach(i -> i.setTag(null));
        }
        if (memberTags != null) {
            memberTags.forEach(i -> i.setTag(this));
        }
        this.members = memberTags;
    }

    public Tag members(Set<MemberTag> memberTags) {
        this.setMembers(memberTags);
        return this;
    }

    public Tag addMembers(MemberTag memberTag) {
        this.members.add(memberTag);
        memberTag.setTag(this);
        return this;
    }

    public Tag removeMembers(MemberTag memberTag) {
        this.members.remove(memberTag);
        memberTag.setTag(null);
        return this;
    }

    public Long getTagTypeId() {
        return this.tagTypeId;
    }

    public void setTagTypeId(Long tagType) {
        this.tagTypeId = tagType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", deletedBy=" + getDeletedBy() +
            ", deletedOn='" + getDeletedOn() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
