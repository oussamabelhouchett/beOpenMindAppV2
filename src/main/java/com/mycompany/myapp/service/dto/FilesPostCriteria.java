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
 * Criteria class for the {@link com.mycompany.myapp.domain.FilesPost} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.FilesPostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /files-posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FilesPostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter path;

    private StringFilter type;

    private LongFilter filesPostId;

    public FilesPostCriteria() {
    }

    public FilesPostCriteria(FilesPostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.filesPostId = other.filesPostId == null ? null : other.filesPostId.copy();
    }

    @Override
    public FilesPostCriteria copy() {
        return new FilesPostCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getFilesPostId() {
        return filesPostId;
    }

    public void setFilesPostId(LongFilter filesPostId) {
        this.filesPostId = filesPostId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FilesPostCriteria that = (FilesPostCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(path, that.path) &&
            Objects.equals(type, that.type) &&
            Objects.equals(filesPostId, that.filesPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        path,
        type,
        filesPostId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilesPostCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (filesPostId != null ? "filesPostId=" + filesPostId + ", " : "") +
            "}";
    }

}
