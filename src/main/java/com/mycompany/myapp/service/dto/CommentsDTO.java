package com.mycompany.myapp.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Comments} entity.
 */
public class CommentsDTO implements Serializable {
    
    private Long id;

    private String contentText;

    private LocalDate datePub;

    private Instant time;


    private Long parentId;

    private Long userId;

    private Long postId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public LocalDate getDatePub() {
        return datePub;
    }

    public void setDatePub(LocalDate datePub) {
        this.datePub = datePub;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long commentsId) {
        this.parentId = commentsId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long applicationUserId) {
        this.userId = applicationUserId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentsDTO)) {
            return false;
        }

        return id != null && id.equals(((CommentsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentsDTO{" +
            "id=" + getId() +
            ", contentText='" + getContentText() + "'" +
            ", datePub='" + getDatePub() + "'" +
            ", time='" + getTime() + "'" +
            ", parentId=" + getParentId() +
            ", userId=" + getUserId() +
            ", postId=" + getPostId() +
            "}";
    }
}
