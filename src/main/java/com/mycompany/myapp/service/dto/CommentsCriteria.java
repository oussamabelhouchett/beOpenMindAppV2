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
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Comments} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contentText;

    private LocalDateFilter datePub;

    private InstantFilter time;

    private LongFilter userId;

    private LongFilter parentId;

    private LongFilter postId;

    public CommentsCriteria() {
    }

    public CommentsCriteria(CommentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contentText = other.contentText == null ? null : other.contentText.copy();
        this.datePub = other.datePub == null ? null : other.datePub.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
    }

    @Override
    public CommentsCriteria copy() {
        return new CommentsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContentText() {
        return contentText;
    }

    public void setContentText(StringFilter contentText) {
        this.contentText = contentText;
    }

    public LocalDateFilter getDatePub() {
        return datePub;
    }

    public void setDatePub(LocalDateFilter datePub) {
        this.datePub = datePub;
    }

    public InstantFilter getTime() {
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
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
        final CommentsCriteria that = (CommentsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(contentText, that.contentText) &&
            Objects.equals(datePub, that.datePub) &&
            Objects.equals(time, that.time) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        contentText,
        datePub,
        time,
        userId,
        parentId,
        postId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (contentText != null ? "contentText=" + contentText + ", " : "") +
                (datePub != null ? "datePub=" + datePub + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (postId != null ? "postId=" + postId + ", " : "") +
            "}";
    }

}
