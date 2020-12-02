package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Comments.
 */
@Entity
@Table(name = "comments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "comments")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "date_pub")
    private LocalDate datePub;

    @Column(name = "time")
    private Instant time;

    @OneToMany(mappedBy = "comments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ApplicationUser> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Comments parent;

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentText() {
        return contentText;
    }

    public Comments contentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public LocalDate getDatePub() {
        return datePub;
    }

    public Comments datePub(LocalDate datePub) {
        this.datePub = datePub;
        return this;
    }

    public void setDatePub(LocalDate datePub) {
        this.datePub = datePub;
    }

    public Instant getTime() {
        return time;
    }

    public Comments time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Set<ApplicationUser> getUsers() {
        return users;
    }

    public Comments users(Set<ApplicationUser> applicationUsers) {
        this.users = applicationUsers;
        return this;
    }

    public Comments addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.setComments(this);
        return this;
    }

    public Comments removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.setComments(null);
        return this;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        this.users = applicationUsers;
    }

    public Comments getParent() {
        return parent;
    }

    public Comments parent(Comments comments) {
        this.parent = comments;
        return this;
    }

    public void setParent(Comments comments) {
        this.parent = comments;
    }

    public Post getPost() {
        return post;
    }

    public Comments post(Post post) {
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
        if (!(o instanceof Comments)) {
            return false;
        }
        return id != null && id.equals(((Comments) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comments{" +
            "id=" + getId() +
            ", contentText='" + getContentText() + "'" +
            ", datePub='" + getDatePub() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
