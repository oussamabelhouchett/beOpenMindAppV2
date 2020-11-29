package com.mycompany.myapp.domain;

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
 * The Employee entity.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The firstname attribute.
     */
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_pub")
    private LocalDate datePub;

    @Column(name = "time")
    private Instant time;

    @OneToMany(mappedBy = "comments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Comments> comments = new HashSet<>();

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "filesPost")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<FilesPost> filesPosts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Post title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Post content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDatePub() {
        return datePub;
    }

    public Post datePub(LocalDate datePub) {
        this.datePub = datePub;
        return this;
    }

    public void setDatePub(LocalDate datePub) {
        this.datePub = datePub;
    }

    public Instant getTime() {
        return time;
    }

    public Post time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Set<Comments> getComments() {
        return comments;
    }

    public Post comments(Set<Comments> comments) {
        this.comments = comments;
        return this;
    }

    public Post addComments(Comments comments) {
        this.comments.add(comments);
        comments.setComments(this);
        return this;
    }

    public Post removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setComments(null);
        return this;
    }

    public void setComments(Set<Comments> comments) {
        this.comments = comments;
    }

    public Set<FilesPost> getFilesPosts() {
        return filesPosts;
    }

    public Post filesPosts(Set<FilesPost> filesPosts) {
        this.filesPosts = filesPosts;
        return this;
    }

    public Post addFilesPost(FilesPost filesPost) {
        this.filesPosts.add(filesPost);
        filesPost.setFilesPost(this);
        return this;
    }

    public Post removeFilesPost(FilesPost filesPost) {
        this.filesPosts.remove(filesPost);
        filesPost.setFilesPost(null);
        return this;
    }

    public void setFilesPosts(Set<FilesPost> filesPosts) {
        this.filesPosts = filesPosts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", datePub='" + getDatePub() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
