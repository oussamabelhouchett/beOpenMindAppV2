package com.mycompany.myapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ApplicationUser} entity.
 */
public class ApplicationUserDTO implements Serializable {
    
    private Long id;

    @Min(value = 42)
    @Max(value = 42)
    private Integer additionalField;


    private Long userId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdditionalField() {
        return additionalField;
    }

    public void setAdditionalField(Integer additionalField) {
        this.additionalField = additionalField;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserDTO)) {
            return false;
        }

        return id != null && id.equals(((ApplicationUserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserDTO{" +
            "id=" + getId() +
            ", additionalField=" + getAdditionalField() +
            ", userId=" + getUserId() +
            "}";
    }
}
