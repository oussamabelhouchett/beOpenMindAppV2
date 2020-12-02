package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BeOpenMindAppV2App;
import com.mycompany.myapp.domain.FilesPost;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.repository.FilesPostRepository;
import com.mycompany.myapp.repository.search.FilesPostSearchRepository;
import com.mycompany.myapp.service.FilesPostService;
import com.mycompany.myapp.service.dto.FilesPostDTO;
import com.mycompany.myapp.service.mapper.FilesPostMapper;
import com.mycompany.myapp.service.dto.FilesPostCriteria;
import com.mycompany.myapp.service.FilesPostQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
 * Integration tests for the {@link FilesPostResource} REST controller.
 */
@SpringBootTest(classes = BeOpenMindAppV2App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FilesPostResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private FilesPostRepository filesPostRepository;

    @Autowired
    private FilesPostMapper filesPostMapper;

    @Autowired
    private FilesPostService filesPostService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.FilesPostSearchRepositoryMockConfiguration
     */
    @Autowired
    private FilesPostSearchRepository mockFilesPostSearchRepository;

    @Autowired
    private FilesPostQueryService filesPostQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilesPostMockMvc;

    private FilesPost filesPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilesPost createEntity(EntityManager em) {
        FilesPost filesPost = new FilesPost()
            .path(DEFAULT_PATH)
            .type(DEFAULT_TYPE);
        return filesPost;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilesPost createUpdatedEntity(EntityManager em) {
        FilesPost filesPost = new FilesPost()
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE);
        return filesPost;
    }

    @BeforeEach
    public void initTest() {
        filesPost = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilesPost() throws Exception {
        int databaseSizeBeforeCreate = filesPostRepository.findAll().size();
        // Create the FilesPost
        FilesPostDTO filesPostDTO = filesPostMapper.toDto(filesPost);
        restFilesPostMockMvc.perform(post("/api/files-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filesPostDTO)))
            .andExpect(status().isCreated());

        // Validate the FilesPost in the database
        List<FilesPost> filesPostList = filesPostRepository.findAll();
        assertThat(filesPostList).hasSize(databaseSizeBeforeCreate + 1);
        FilesPost testFilesPost = filesPostList.get(filesPostList.size() - 1);
        assertThat(testFilesPost.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testFilesPost.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the FilesPost in Elasticsearch
        verify(mockFilesPostSearchRepository, times(1)).save(testFilesPost);
    }

    @Test
    @Transactional
    public void createFilesPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filesPostRepository.findAll().size();

        // Create the FilesPost with an existing ID
        filesPost.setId(1L);
        FilesPostDTO filesPostDTO = filesPostMapper.toDto(filesPost);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilesPostMockMvc.perform(post("/api/files-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filesPostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FilesPost in the database
        List<FilesPost> filesPostList = filesPostRepository.findAll();
        assertThat(filesPostList).hasSize(databaseSizeBeforeCreate);

        // Validate the FilesPost in Elasticsearch
        verify(mockFilesPostSearchRepository, times(0)).save(filesPost);
    }


    @Test
    @Transactional
    public void getAllFilesPosts() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList
        restFilesPostMockMvc.perform(get("/api/files-posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filesPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getFilesPost() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get the filesPost
        restFilesPostMockMvc.perform(get("/api/files-posts/{id}", filesPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filesPost.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getFilesPostsByIdFiltering() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        Long id = filesPost.getId();

        defaultFilesPostShouldBeFound("id.equals=" + id);
        defaultFilesPostShouldNotBeFound("id.notEquals=" + id);

        defaultFilesPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFilesPostShouldNotBeFound("id.greaterThan=" + id);

        defaultFilesPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFilesPostShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFilesPostsByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path equals to DEFAULT_PATH
        defaultFilesPostShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the filesPostList where path equals to UPDATED_PATH
        defaultFilesPostShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path not equals to DEFAULT_PATH
        defaultFilesPostShouldNotBeFound("path.notEquals=" + DEFAULT_PATH);

        // Get all the filesPostList where path not equals to UPDATED_PATH
        defaultFilesPostShouldBeFound("path.notEquals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByPathIsInShouldWork() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path in DEFAULT_PATH or UPDATED_PATH
        defaultFilesPostShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the filesPostList where path equals to UPDATED_PATH
        defaultFilesPostShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path is not null
        defaultFilesPostShouldBeFound("path.specified=true");

        // Get all the filesPostList where path is null
        defaultFilesPostShouldNotBeFound("path.specified=false");
    }
                @Test
    @Transactional
    public void getAllFilesPostsByPathContainsSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path contains DEFAULT_PATH
        defaultFilesPostShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the filesPostList where path contains UPDATED_PATH
        defaultFilesPostShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByPathNotContainsSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where path does not contain DEFAULT_PATH
        defaultFilesPostShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the filesPostList where path does not contain UPDATED_PATH
        defaultFilesPostShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }


    @Test
    @Transactional
    public void getAllFilesPostsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type equals to DEFAULT_TYPE
        defaultFilesPostShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the filesPostList where type equals to UPDATED_TYPE
        defaultFilesPostShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type not equals to DEFAULT_TYPE
        defaultFilesPostShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the filesPostList where type not equals to UPDATED_TYPE
        defaultFilesPostShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFilesPostShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the filesPostList where type equals to UPDATED_TYPE
        defaultFilesPostShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type is not null
        defaultFilesPostShouldBeFound("type.specified=true");

        // Get all the filesPostList where type is null
        defaultFilesPostShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllFilesPostsByTypeContainsSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type contains DEFAULT_TYPE
        defaultFilesPostShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the filesPostList where type contains UPDATED_TYPE
        defaultFilesPostShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFilesPostsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        // Get all the filesPostList where type does not contain DEFAULT_TYPE
        defaultFilesPostShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the filesPostList where type does not contain UPDATED_TYPE
        defaultFilesPostShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllFilesPostsByFilesPostIsEqualToSomething() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);
        Post filesPost = PostResourceIT.createEntity(em);
        em.persist(filesPost);
        em.flush();
        filesPost.setFilesPost(filesPost);
        filesPostRepository.saveAndFlush(filesPost);
        Long filesPostId = filesPost.getId();

        // Get all the filesPostList where filesPost equals to filesPostId
        defaultFilesPostShouldBeFound("filesPostId.equals=" + filesPostId);

        // Get all the filesPostList where filesPost equals to filesPostId + 1
        defaultFilesPostShouldNotBeFound("filesPostId.equals=" + (filesPostId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFilesPostShouldBeFound(String filter) throws Exception {
        restFilesPostMockMvc.perform(get("/api/files-posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filesPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restFilesPostMockMvc.perform(get("/api/files-posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFilesPostShouldNotBeFound(String filter) throws Exception {
        restFilesPostMockMvc.perform(get("/api/files-posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFilesPostMockMvc.perform(get("/api/files-posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFilesPost() throws Exception {
        // Get the filesPost
        restFilesPostMockMvc.perform(get("/api/files-posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilesPost() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        int databaseSizeBeforeUpdate = filesPostRepository.findAll().size();

        // Update the filesPost
        FilesPost updatedFilesPost = filesPostRepository.findById(filesPost.getId()).get();
        // Disconnect from session so that the updates on updatedFilesPost are not directly saved in db
        em.detach(updatedFilesPost);
        updatedFilesPost
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE);
        FilesPostDTO filesPostDTO = filesPostMapper.toDto(updatedFilesPost);

        restFilesPostMockMvc.perform(put("/api/files-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filesPostDTO)))
            .andExpect(status().isOk());

        // Validate the FilesPost in the database
        List<FilesPost> filesPostList = filesPostRepository.findAll();
        assertThat(filesPostList).hasSize(databaseSizeBeforeUpdate);
        FilesPost testFilesPost = filesPostList.get(filesPostList.size() - 1);
        assertThat(testFilesPost.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFilesPost.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the FilesPost in Elasticsearch
        verify(mockFilesPostSearchRepository, times(1)).save(testFilesPost);
    }

    @Test
    @Transactional
    public void updateNonExistingFilesPost() throws Exception {
        int databaseSizeBeforeUpdate = filesPostRepository.findAll().size();

        // Create the FilesPost
        FilesPostDTO filesPostDTO = filesPostMapper.toDto(filesPost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilesPostMockMvc.perform(put("/api/files-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(filesPostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FilesPost in the database
        List<FilesPost> filesPostList = filesPostRepository.findAll();
        assertThat(filesPostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FilesPost in Elasticsearch
        verify(mockFilesPostSearchRepository, times(0)).save(filesPost);
    }

    @Test
    @Transactional
    public void deleteFilesPost() throws Exception {
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);

        int databaseSizeBeforeDelete = filesPostRepository.findAll().size();

        // Delete the filesPost
        restFilesPostMockMvc.perform(delete("/api/files-posts/{id}", filesPost.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FilesPost> filesPostList = filesPostRepository.findAll();
        assertThat(filesPostList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FilesPost in Elasticsearch
        verify(mockFilesPostSearchRepository, times(1)).deleteById(filesPost.getId());
    }

    @Test
    @Transactional
    public void searchFilesPost() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        filesPostRepository.saveAndFlush(filesPost);
        when(mockFilesPostSearchRepository.search(queryStringQuery("id:" + filesPost.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(filesPost), PageRequest.of(0, 1), 1));

        // Search the filesPost
        restFilesPostMockMvc.perform(get("/api/_search/files-posts?query=id:" + filesPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filesPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
}
