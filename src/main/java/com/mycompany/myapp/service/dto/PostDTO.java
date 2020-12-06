package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Post} entity.
 */
@ApiModel(description = "The Employee entity.")
public class PostDTO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private LocalDate datePub;

    private Instant time;

    private Boolean isNameVisibale;

    private Boolean isPhotoVisibale;

    private Integer nbreLike;

    private Integer nbreComments;


    private Long userId;
    private Long userAppId;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public Long getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(Long userAppId) {
        this.userAppId = userAppId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public Boolean isIsNameVisibale() {
        return isNameVisibale;
    }

    public void setIsNameVisibale(Boolean isNameVisibale) {
        this.isNameVisibale = isNameVisibale;
    }

    public Boolean isIsPhotoVisibale() {
        return isPhotoVisibale;
    }

    public void setIsPhotoVisibale(Boolean isPhotoVisibale) {
        this.isPhotoVisibale = isPhotoVisibale;
    }

    public Integer getNbreLike() {
        return nbreLike;
    }

    public void setNbreLike(Integer nbreLike) {
        this.nbreLike = nbreLike;
    }

    public Integer getNbreComments() {
        return nbreComments;
    }

    public void setNbreComments(Integer nbreComments) {
        this.nbreComments = nbreComments;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long applicationUserId) {
        this.userId = applicationUserId;
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
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", datePub=" + datePub +
            ", time=" + time +
            ", isNameVisibale=" + isNameVisibale +
            ", isPhotoVisibale=" + isPhotoVisibale +
            ", nbreLike=" + nbreLike +
            ", nbreComments=" + nbreComments +
            ", userId=" + userId +
            ", userAppId=" + userAppId +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
    }
}
