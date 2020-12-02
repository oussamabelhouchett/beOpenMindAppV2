package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BeOpenMindAppV2App;
import com.mycompany.myapp.domain.ApplicationUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ApplicationUserRepository;
import com.mycompany.myapp.repository.search.ApplicationUserSearchRepository;
import com.mycompany.myapp.service.ApplicationUserService;
import com.mycompany.myapp.service.dto.ApplicationUserDTO;
import com.mycompany.myapp.service.mapper.ApplicationUserMapper;
import com.mycompany.myapp.service.dto.ApplicationUserCriteria;
import com.mycompany.myapp.service.ApplicationUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@SpringBootTest(classes = BeOpenMindAppV2App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApplicationUserResourceIT {

    private static final Integer DEFAULT_ADDITIONAL_FIELD = 42;
    private static final Integer UPDATED_ADDITIONAL_FIELD = 43;
    private static final Integer SMALLER_ADDITIONAL_FIELD = 42 - 1;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Autowired
    private ApplicationUserService applicationUserService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ApplicationUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicationUserSearchRepository mockApplicationUserSearchRepository;

    @Autowired
    private ApplicationUserQueryService applicationUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .additionalField(DEFAULT_ADDITIONAL_FIELD);
        return applicationUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .additionalField(UPDATED_ADDITIONAL_FIELD);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getAdditionalField()).isEqualTo(DEFAULT_ADDITIONAL_FIELD);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).save(testApplicationUser);
    }

    @Test
    @Transactional
    public void createApplicationUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }


    @Test
    @Transactional
    public void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].additionalField").value(hasItem(DEFAULT_ADDITIONAL_FIELD)));
    }
    
    @Test
    @Transactional
    public void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.additionalField").value(DEFAULT_ADDITIONAL_FIELD));
    }


    @Test
    @Transactional
    public void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserShouldBeFound("id.equals=" + id);
        defaultApplicationUserShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField equals to DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.equals=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField equals to UPDATED_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.equals=" + UPDATED_ADDITIONAL_FIELD);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField not equals to DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.notEquals=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField not equals to UPDATED_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.notEquals=" + UPDATED_ADDITIONAL_FIELD);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField in DEFAULT_ADDITIONAL_FIELD or UPDATED_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.in=" + DEFAULT_ADDITIONAL_FIELD + "," + UPDATED_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField equals to UPDATED_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.in=" + UPDATED_ADDITIONAL_FIELD);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField is not null
        defaultApplicationUserShouldBeFound("additionalField.specified=true");

        // Get all the applicationUserList where additionalField is null
        defaultApplicationUserShouldNotBeFound("additionalField.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField is greater than or equal to DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.greaterThanOrEqual=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField is greater than or equal to (DEFAULT_ADDITIONAL_FIELD + 1)
        defaultApplicationUserShouldNotBeFound("additionalField.greaterThanOrEqual=" + (DEFAULT_ADDITIONAL_FIELD + 1));
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField is less than or equal to DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.lessThanOrEqual=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField is less than or equal to SMALLER_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.lessThanOrEqual=" + SMALLER_ADDITIONAL_FIELD);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField is less than DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.lessThan=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField is less than (DEFAULT_ADDITIONAL_FIELD + 1)
        defaultApplicationUserShouldBeFound("additionalField.lessThan=" + (DEFAULT_ADDITIONAL_FIELD + 1));
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdditionalFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where additionalField is greater than DEFAULT_ADDITIONAL_FIELD
        defaultApplicationUserShouldNotBeFound("additionalField.greaterThan=" + DEFAULT_ADDITIONAL_FIELD);

        // Get all the applicationUserList where additionalField is greater than SMALLER_ADDITIONAL_FIELD
        defaultApplicationUserShouldBeFound("additionalField.greaterThan=" + SMALLER_ADDITIONAL_FIELD);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicationUser.setUser(user);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long userId = user.getId();

        // Get all the applicationUserList where user equals to userId
        defaultApplicationUserShouldBeFound("userId.equals=" + userId);

        // Get all the applicationUserList where user equals to userId + 1
        defaultApplicationUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].additionalField").value(hasItem(DEFAULT_ADDITIONAL_FIELD)));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc.perform(get("/api/application-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc.perform(get("/api/application-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .additionalField(UPDATED_ADDITIONAL_FIELD);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(updatedApplicationUser);

        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getAdditionalField()).isEqualTo(UPDATED_ADDITIONAL_FIELD);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).save(testApplicationUser);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    public void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc.perform(delete("/api/application-users/{id}", applicationUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).deleteById(applicationUser.getId());
    }

    @Test
    @Transactional
    public void searchApplicationUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        when(mockApplicationUserSearchRepository.search(queryStringQuery("id:" + applicationUser.getId())))
            .thenReturn(Collections.singletonList(applicationUser));

        // Search the applicationUser
        restApplicationUserMockMvc.perform(get("/api/_search/application-users?query=id:" + applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].additionalField").value(hasItem(DEFAULT_ADDITIONAL_FIELD)));
    }
}
