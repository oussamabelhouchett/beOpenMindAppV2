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
 * Criteria class for the {@link com.mycompany.myapp.domain.Post} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter content;

    private LocalDateFilter datePub;

    private InstantFilter time;

    private BooleanFilter isNameVisibale;

    private BooleanFilter isPhotoVisibale;

    private IntegerFilter nbreLike;

    private IntegerFilter nbreComments;

    private LongFilter commentsId;

    private LongFilter filesPostId;

    private LongFilter userId;

    public PostCriteria() {
    }

    public PostCriteria(PostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.datePub = other.datePub == null ? null : other.datePub.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.isNameVisibale = other.isNameVisibale == null ? null : other.isNameVisibale.copy();
        this.isPhotoVisibale = other.isPhotoVisibale == null ? null : other.isPhotoVisibale.copy();
        this.nbreLike = other.nbreLike == null ? null : other.nbreLike.copy();
        this.nbreComments = other.nbreComments == null ? null : other.nbreComments.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.filesPostId = other.filesPostId == null ? null : other.filesPostId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PostCriteria copy() {
        return new PostCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
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

    public BooleanFilter getIsNameVisibale() {
        return isNameVisibale;
    }

    public void setIsNameVisibale(BooleanFilter isNameVisibale) {
        this.isNameVisibale = isNameVisibale;
    }

    public BooleanFilter getIsPhotoVisibale() {
        return isPhotoVisibale;
    }

    public void setIsPhotoVisibale(BooleanFilter isPhotoVisibale) {
        this.isPhotoVisibale = isPhotoVisibale;
    }

    public IntegerFilter getNbreLike() {
        return nbreLike;
    }

    public void setNbreLike(IntegerFilter nbreLike) {
        this.nbreLike = nbreLike;
    }

    public IntegerFilter getNbreComments() {
        return nbreComments;
    }

    public void setNbreComments(IntegerFilter nbreComments) {
        this.nbreComments = nbreComments;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public LongFilter getFilesPostId() {
        return filesPostId;
    }

    public void setFilesPostId(LongFilter filesPostId) {
        this.filesPostId = filesPostId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostCriteria that = (PostCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(content, that.content) &&
            Objects.equals(datePub, that.datePub) &&
            Objects.equals(time, that.time) &&
            Objects.equals(isNameVisibale, that.isNameVisibale) &&
            Objects.equals(isPhotoVisibale, that.isPhotoVisibale) &&
            Objects.equals(nbreLike, that.nbreLike) &&
            Objects.equals(nbreComments, that.nbreComments) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(filesPostId, that.filesPostId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        content,
        datePub,
        time,
        isNameVisibale,
        isPhotoVisibale,
        nbreLike,
        nbreComments,
        commentsId,
        filesPostId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (datePub != null ? "datePub=" + datePub + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (isNameVisibale != null ? "isNameVisibale=" + isNameVisibale + ", " : "") +
                (isPhotoVisibale != null ? "isPhotoVisibale=" + isPhotoVisibale + ", " : "") +
                (nbreLike != null ? "nbreLike=" + nbreLike + ", " : "") +
                (nbreComments != null ? "nbreComments=" + nbreComments + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
                (filesPostId != null ? "filesPostId=" + filesPostId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
