package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Post} entity.
 */
@ApiModel(description = "The Employee entity.")
public class PostDTO implements Serializable {
    
    private Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    private String title;

    private String content;

    private LocalDate datePub;

    private Instant time;

    /**
     * A relationship
     */
    @ApiModelProperty(value = "A relationship")
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostDTO)) {
            return false;
        }

        return id != null && id.equals(((PostDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", datePub='" + getDatePub() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
