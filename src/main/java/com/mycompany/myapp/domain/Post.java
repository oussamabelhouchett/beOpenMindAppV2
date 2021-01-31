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
 * The Employee entity.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_pub")
    private LocalDate datePub;

    @Column(name = "time")
    private Instant time;

    @Column(name = "is_name_visibale")
    private Boolean isNameVisibale;

    @Column(name = "is_photo_visibale")
    private Boolean isPhotoVisibale;

    @Column(name = "nbre_like")
    private Integer nbreLike;

    @Column(name = "nbre_comments")
    private Integer nbreComments;

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Comments> comments = new HashSet<>();

    @OneToMany(mappedBy = "filesPost")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<FilesPost> filesPosts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "posts", allowSetters = true)
    private ApplicationUser user;

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

    public Boolean isIsNameVisibale() {
        return isNameVisibale;
    }

    public Post isNameVisibale(Boolean isNameVisibale) {
        this.isNameVisibale = isNameVisibale;
        return this;
    }

    public void setIsNameVisibale(Boolean isNameVisibale) {
        this.isNameVisibale = isNameVisibale;
    }

    public Boolean isIsPhotoVisibale() {
        return isPhotoVisibale;
    }

    public Post isPhotoVisibale(Boolean isPhotoVisibale) {
        this.isPhotoVisibale = isPhotoVisibale;
        return this;
    }

    public void setIsPhotoVisibale(Boolean isPhotoVisibale) {
        this.isPhotoVisibale = isPhotoVisibale;
    }

    public Integer getNbreLike() {
        return nbreLike;
    }

    public Post nbreLike(Integer nbreLike) {
        this.nbreLike = nbreLike;
        return this;
    }

    public void setNbreLike(Integer nbreLike) {
        this.nbreLike = nbreLike;
    }

    public Integer getNbreComments() {
        return nbreComments;
    }

    public Post nbreComments(Integer nbreComments) {
        this.nbreComments = nbreComments;
        return this;
    }

    public void setNbreComments(Integer nbreComments) {
        this.nbreComments = nbreComments;
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
        comments.setPost(this);
        return this;
    }

    public Post removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setPost(null);
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

    public ApplicationUser getUser() {
        return user;
    }

    public Post user(ApplicationUser applicationUser) {
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
            ", isNameVisibale='" + isIsNameVisibale() + "'" +
            ", isPhotoVisibale='" + isIsPhotoVisibale() + "'" +
            ", nbreLike=" + getNbreLike() +
            ", nbreComments=" + getNbreComments() +
            "}";
    }
}
