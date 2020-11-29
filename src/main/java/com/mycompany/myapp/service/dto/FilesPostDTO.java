package com.mycompany.myapp.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.FilesPost} entity.
 */
public class FilesPostDTO implements Serializable {
    
    private Long id;

    private String path;

    private String type;


    private Long filesPostId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFilesPostId() {
        return filesPostId;
    }

    public void setFilesPostId(Long postId) {
        this.filesPostId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilesPostDTO)) {
            return false;
        }

        return id != null && id.equals(((FilesPostDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilesPostDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", filesPostId=" + getFilesPostId() +
            "}";
    }
}
