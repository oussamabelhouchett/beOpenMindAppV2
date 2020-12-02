package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Reaction} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ReactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter isComment;

    private IntegerFilter isLike;

    private LongFilter userId;

    private LongFilter postId;

    public ReactionCriteria() {
    }

    public ReactionCriteria(ReactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.isComment = other.isComment == null ? null : other.isComment.copy();
        this.isLike = other.isLike == null ? null : other.isLike.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
    }

    @Override
    public ReactionCriteria copy() {
        return new ReactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getIsComment() {
        return isComment;
    }

    public void setIsComment(IntegerFilter isComment) {
        this.isComment = isComment;
    }

    public IntegerFilter getIsLike() {
        return isLike;
    }

    public void setIsLike(IntegerFilter isLike) {
        this.isLike = isLike;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getPostId() {
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReactionCriteria that = (ReactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(isComment, that.isComment) &&
            Objects.equals(isLike, that.isLike) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        isComment,
        isLike,
        userId,
        postId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (isComment != null ? "isComment=" + isComment + ", " : "") +
                (isLike != null ? "isLike=" + isLike + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (postId != null ? "postId=" + postId + ", " : "") +
            "}";
    }

}
