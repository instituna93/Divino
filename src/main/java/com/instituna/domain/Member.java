package com.instituna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Member.
 */
@Table("member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

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

    @Column("nickname")
    private String nickname;

    @Column("birthday")
    private LocalDate birthday;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "tag", "member" }, allowSetters = true)
    private Set<MemberTag> tags = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "instrument", "member" }, allowSetters = true)
    private Set<InstrumentRequest> requests = new HashSet<>();

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public Member createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Member createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public Member updatedBy(Long updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Member updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return this.deletedBy;
    }

    public Member deletedBy(Long deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedOn() {
        return this.deletedOn;
    }

    public Member deletedOn(Instant deletedOn) {
        this.setDeletedOn(deletedOn);
        return this;
    }

    public void setDeletedOn(Instant deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Member nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public Member birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Member user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<MemberTag> getTags() {
        return this.tags;
    }

    public void setTags(Set<MemberTag> memberTags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setMember(null));
        }
        if (memberTags != null) {
            memberTags.forEach(i -> i.setMember(this));
        }
        this.tags = memberTags;
    }

    public Member tags(Set<MemberTag> memberTags) {
        this.setTags(memberTags);
        return this;
    }

    public Member addTags(MemberTag memberTag) {
        this.tags.add(memberTag);
        memberTag.setMember(this);
        return this;
    }

    public Member removeTags(MemberTag memberTag) {
        this.tags.remove(memberTag);
        memberTag.setMember(null);
        return this;
    }

    public Set<InstrumentRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<InstrumentRequest> instrumentRequests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setMember(null));
        }
        if (instrumentRequests != null) {
            instrumentRequests.forEach(i -> i.setMember(this));
        }
        this.requests = instrumentRequests;
    }

    public Member requests(Set<InstrumentRequest> instrumentRequests) {
        this.setRequests(instrumentRequests);
        return this;
    }

    public Member addRequests(InstrumentRequest instrumentRequest) {
        this.requests.add(instrumentRequest);
        instrumentRequest.setMember(this);
        return this;
    }

    public Member removeRequests(InstrumentRequest instrumentRequest) {
        this.requests.remove(instrumentRequest);
        instrumentRequest.setMember(null);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", deletedBy=" + getDeletedBy() +
            ", deletedOn='" + getDeletedOn() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", birthday='" + getBirthday() + "'" +
            "}";
    }
}
