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
 * Criteria class for the {@link com.mycompany.myapp.domain.ApplicationUser} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ApplicationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter additionalField;

    private LongFilter userId;

    private LongFilter postId;

    private LongFilter commentsId;

    private LongFilter reactionId;

    public ApplicationUserCriteria() {
    }

    public ApplicationUserCriteria(ApplicationUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.additionalField = other.additionalField == null ? null : other.additionalField.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.reactionId = other.reactionId == null ? null : other.reactionId.copy();
    }

    @Override
    public ApplicationUserCriteria copy() {
        return new ApplicationUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAdditionalField() {
        return additionalField;
    }

    public void setAdditionalField(IntegerFilter additionalField) {
        this.additionalField = additionalField;
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

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public LongFilter getReactionId() {
        return reactionId;
    }

    public void setReactionId(LongFilter reactionId) {
        this.reactionId = reactionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationUserCriteria that = (ApplicationUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(additionalField, that.additionalField) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(reactionId, that.reactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        additionalField,
        userId,
        postId,
        commentsId,
        reactionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (additionalField != null ? "additionalField=" + additionalField + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (postId != null ? "postId=" + postId + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
                (reactionId != null ? "reactionId=" + reactionId + ", " : "") +
            "}";
    }

}
