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
 * A TagType.
 */
@Table("tag_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagType implements Serializable {

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

    @Column("restricted")
    private Boolean restricted;

    @Transient
    @JsonIgnoreProperties(value = { "tagType", "members" }, allowSetters = true)
    private Tag defaultTag;

    @Transient
    @JsonIgnoreProperties(value = { "tagType", "members" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @Column("default_tag_id")
    private Long defaultTagId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TagType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public TagType createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public TagType createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public TagType updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public TagType updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return this.deletedBy;
    }

    public TagType deletedBy(Long deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedOn() {
        return this.deletedOn;
    }

    public TagType deletedOn(Instant deletedOn) {
        this.setDeletedOn(deletedOn);
        return this;
    }

    public void setDeletedOn(Instant deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getDescription() {
        return this.description;
    }

    public TagType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRestricted() {
        return this.restricted;
    }

    public TagType restricted(Boolean restricted) {
        this.setRestricted(restricted);
        return this;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Tag getDefaultTag() {
        return this.defaultTag;
    }

    public void setDefaultTag(Tag tag) {
        this.defaultTag = tag;
        this.defaultTagId = tag != null ? tag.getId() : null;
    }

    public TagType defaultTag(Tag tag) {
        this.setDefaultTag(tag);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setTagType(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setTagType(this));
        }
        this.tags = tags;
    }

    public TagType tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public TagType addTags(Tag tag) {
        this.tags.add(tag);
        tag.setTagType(this);
        return this;
    }

    public TagType removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setTagType(null);
        return this;
    }

    public Long getDefaultTagId() {
        return this.defaultTagId;
    }

    public void setDefaultTagId(Long tag) {
        this.defaultTagId = tag;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagType)) {
            return false;
        }
        return id != null && id.equals(((TagType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagType{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", deletedBy=" + getDeletedBy() +
            ", deletedOn='" + getDeletedOn() + "'" +
            ", description='" + getDescription() + "'" +
            ", restricted='" + getRestricted() + "'" +
            "}";
    }
}
