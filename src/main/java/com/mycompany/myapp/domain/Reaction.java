package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

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

    @ManyToOne
    @JsonIgnoreProperties(value = "reactions", allowSetters = true)
    private Post post;

    @ManyToOne
    @JsonIgnoreProperties(value = "reactions", allowSetters = true)
    private ApplicationUser user;

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

    public ApplicationUser getUser() {
        return user;
    }

    public Reaction user(ApplicationUser applicationUser) {
        this.user = applicationUser;
        return this;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.user = applicationUser;
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
