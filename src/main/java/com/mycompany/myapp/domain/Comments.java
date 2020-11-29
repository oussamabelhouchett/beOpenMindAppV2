package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

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

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Comments parent;

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Post comments;

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

    public Post getComments() {
        return comments;
    }

    public Comments comments(Post post) {
        this.comments = post;
        return this;
    }

    public void setComments(Post post) {
        this.comments = post;
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
