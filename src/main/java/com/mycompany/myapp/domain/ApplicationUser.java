package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "applicationuser")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 42)
    @Max(value = 42)
    @Column(name = "additional_field")
    private Integer additionalField;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Post post;

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Comments comments;

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Reaction reaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdditionalField() {
        return additionalField;
    }

    public ApplicationUser additionalField(Integer additionalField) {
        this.additionalField = additionalField;
        return this;
    }

    public void setAdditionalField(Integer additionalField) {
        this.additionalField = additionalField;
    }

    public User getUser() {
        return user;
    }

    public ApplicationUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public ApplicationUser post(Post post) {
        this.post = post;
        return this;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comments getComments() {
        return comments;
    }

    public ApplicationUser comments(Comments comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public ApplicationUser reaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", additionalField=" + getAdditionalField() +
            "}";
    }
}
