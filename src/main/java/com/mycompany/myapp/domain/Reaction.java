package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Reaction.
 */
@Entity
@Table(name = "reaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reaction")
public class Reaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_comment")
    private Integer isComment;

    @Column(name = "is_like")
    private Integer isLike;

    @OneToMany(mappedBy = "reaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ApplicationUser> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "reactions", allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public Reaction isComment(Integer isComment) {
        this.isComment = isComment;
        return this;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public Reaction isLike(Integer isLike) {
        this.isLike = isLike;
        return this;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public Set<ApplicationUser> getUsers() {
        return users;
    }

    public Reaction users(Set<ApplicationUser> applicationUsers) {
        this.users = applicationUsers;
        return this;
    }

    public Reaction addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.setReaction(this);
        return this;
    }

    public Reaction removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.setReaction(null);
        return this;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        this.users = applicationUsers;
    }

    public Post getPost() {
        return post;
    }

    public Reaction post(Post post) {
        this.post = post;
        return this;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reaction)) {
            return false;
        }
        return id != null && id.equals(((Reaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reaction{" +
            "id=" + getId() +
            ", isComment=" + getIsComment() +
            ", isLike=" + getIsLike() +
            "}";
    }
}
