package com.mycompany.myapp.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Reaction} entity.
 */
public class ReactionDTO implements Serializable {
    
    private Long id;

    private Integer isComment;

    private Integer isLike;


    private Long postId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
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
        if (!(o instanceof ReactionDTO)) {
            return false;
        }

        return id != null && id.equals(((ReactionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionDTO{" +
            "id=" + getId() +
            ", isComment=" + getIsComment() +
            ", isLike=" + getIsLike() +
            ", postId=" + getPostId() +
            "}";
    }
}
