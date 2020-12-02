package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BeOpenMindAppV2App;
import com.mycompany.myapp.domain.Comments;
import com.mycompany.myapp.domain.Comments;
import com.mycompany.myapp.domain.ApplicationUser;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.repository.CommentsRepository;
import com.mycompany.myapp.repository.search.CommentsSearchRepository;
import com.mycompany.myapp.service.CommentsService;
import com.mycompany.myapp.service.dto.CommentsDTO;
import com.mycompany.myapp.service.mapper.CommentsMapper;
import com.mycompany.myapp.service.dto.CommentsCriteria;
import com.mycompany.myapp.service.CommentsQueryService;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommentsResource} REST controller.
 */
@SpringBootTest(classes = BeOpenMindAppV2App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommentsResourceIT {

    private static final String DEFAULT_CONTENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_PUB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PUB = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsService commentsService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CommentsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommentsSearchRepository mockCommentsSearchRepository;

    @Autowired
    private CommentsQueryService commentsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentsMockMvc;

    private Comments comments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comments createEntity(EntityManager em) {
        Comments comments = new Comments()
            .contentText(DEFAULT_CONTENT_TEXT)
            .datePub(DEFAULT_DATE_PUB)
            .time(DEFAULT_TIME);
        return comments;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comments createUpdatedEntity(EntityManager em) {
        Comments comments = new Comments()
            .contentText(UPDATED_CONTENT_TEXT)
            .datePub(UPDATED_DATE_PUB)
            .time(UPDATED_TIME);
        return comments;
    }

    @BeforeEach
    public void initTest() {
        comments = createEntity(em);
    }

    @Test
    @Transactional
    public void createComments() throws Exception {
        int databaseSizeBeforeCreate = commentsRepository.findAll().size();
        // Create the Comments
        CommentsDTO commentsDTO = commentsMapper.toDto(comments);
        restCommentsMockMvc.perform(post("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Comments in the database
        List<Comments> commentsList = commentsRepository.findAll();
        assertThat(commentsList).hasSize(databaseSizeBeforeCreate + 1);
        Comments testComments = commentsList.get(commentsList.size() - 1);
        assertThat(testComments.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testComments.getDatePub()).isEqualTo(DEFAULT_DATE_PUB);
        assertThat(testComments.getTime()).isEqualTo(DEFAULT_TIME);

        // Validate the Comments in Elasticsearch
        verify(mockCommentsSearchRepository, times(1)).save(testComments);
    }

    @Test
    @Transactional
    public void createCommentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentsRepository.findAll().size();

        // Create the Comments with an existing ID
        comments.setId(1L);
        CommentsDTO commentsDTO = commentsMapper.toDto(comments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentsMockMvc.perform(post("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comments in the database
        List<Comments> commentsList = commentsRepository.findAll();
        assertThat(commentsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Comments in Elasticsearch
        verify(mockCommentsSearchRepository, times(0)).save(comments);
    }


    @Test
    @Transactional
    public void getAllComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList
        restCommentsMockMvc.perform(get("/api/comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comments.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get the comments
        restCommentsMockMvc.perform(get("/api/comments/{id}", comments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comments.getId().intValue()))
            .andExpect(jsonPath("$.contentText").value(DEFAULT_CONTENT_TEXT))
            .andExpect(jsonPath("$.datePub").value(DEFAULT_DATE_PUB.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()));
    }


    @Test
    @Transactional
    public void getCommentsByIdFiltering() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        Long id = comments.getId();

        defaultCommentsShouldBeFound("id.equals=" + id);
        defaultCommentsShouldNotBeFound("id.notEquals=" + id);

        defaultCommentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentsShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCommentsByContentTextIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText equals to DEFAULT_CONTENT_TEXT
        defaultCommentsShouldBeFound("contentText.equals=" + DEFAULT_CONTENT_TEXT);

        // Get all the commentsList where contentText equals to UPDATED_CONTENT_TEXT
        defaultCommentsShouldNotBeFound("contentText.equals=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    public void getAllCommentsByContentTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText not equals to DEFAULT_CONTENT_TEXT
        defaultCommentsShouldNotBeFound("contentText.notEquals=" + DEFAULT_CONTENT_TEXT);

        // Get all the commentsList where contentText not equals to UPDATED_CONTENT_TEXT
        defaultCommentsShouldBeFound("contentText.notEquals=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    public void getAllCommentsByContentTextIsInShouldWork() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText in DEFAULT_CONTENT_TEXT or UPDATED_CONTENT_TEXT
        defaultCommentsShouldBeFound("contentText.in=" + DEFAULT_CONTENT_TEXT + "," + UPDATED_CONTENT_TEXT);

        // Get all the commentsList where contentText equals to UPDATED_CONTENT_TEXT
        defaultCommentsShouldNotBeFound("contentText.in=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    public void getAllCommentsByContentTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText is not null
        defaultCommentsShouldBeFound("contentText.specified=true");

        // Get all the commentsList where contentText is null
        defaultCommentsShouldNotBeFound("contentText.specified=false");
    }
                @Test
    @Transactional
    public void getAllCommentsByContentTextContainsSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText contains DEFAULT_CONTENT_TEXT
        defaultCommentsShouldBeFound("contentText.contains=" + DEFAULT_CONTENT_TEXT);

        // Get all the commentsList where contentText contains UPDATED_CONTENT_TEXT
        defaultCommentsShouldNotBeFound("contentText.contains=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    public void getAllCommentsByContentTextNotContainsSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where contentText does not contain DEFAULT_CONTENT_TEXT
        defaultCommentsShouldNotBeFound("contentText.doesNotContain=" + DEFAULT_CONTENT_TEXT);

        // Get all the commentsList where contentText does not contain UPDATED_CONTENT_TEXT
        defaultCommentsShouldBeFound("contentText.doesNotContain=" + UPDATED_CONTENT_TEXT);
    }


    @Test
    @Transactional
    public void getAllCommentsByDatePubIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub equals to DEFAULT_DATE_PUB
        defaultCommentsShouldBeFound("datePub.equals=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub equals to UPDATED_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.equals=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub not equals to DEFAULT_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.notEquals=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub not equals to UPDATED_DATE_PUB
        defaultCommentsShouldBeFound("datePub.notEquals=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsInShouldWork() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub in DEFAULT_DATE_PUB or UPDATED_DATE_PUB
        defaultCommentsShouldBeFound("datePub.in=" + DEFAULT_DATE_PUB + "," + UPDATED_DATE_PUB);

        // Get all the commentsList where datePub equals to UPDATED_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.in=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub is not null
        defaultCommentsShouldBeFound("datePub.specified=true");

        // Get all the commentsList where datePub is null
        defaultCommentsShouldNotBeFound("datePub.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub is greater than or equal to DEFAULT_DATE_PUB
        defaultCommentsShouldBeFound("datePub.greaterThanOrEqual=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub is greater than or equal to UPDATED_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.greaterThanOrEqual=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub is less than or equal to DEFAULT_DATE_PUB
        defaultCommentsShouldBeFound("datePub.lessThanOrEqual=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub is less than or equal to SMALLER_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.lessThanOrEqual=" + SMALLER_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsLessThanSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub is less than DEFAULT_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.lessThan=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub is less than UPDATED_DATE_PUB
        defaultCommentsShouldBeFound("datePub.lessThan=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllCommentsByDatePubIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where datePub is greater than DEFAULT_DATE_PUB
        defaultCommentsShouldNotBeFound("datePub.greaterThan=" + DEFAULT_DATE_PUB);

        // Get all the commentsList where datePub is greater than SMALLER_DATE_PUB
        defaultCommentsShouldBeFound("datePub.greaterThan=" + SMALLER_DATE_PUB);
    }


    @Test
    @Transactional
    public void getAllCommentsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where time equals to DEFAULT_TIME
        defaultCommentsShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the commentsList where time equals to UPDATED_TIME
        defaultCommentsShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCommentsByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where time not equals to DEFAULT_TIME
        defaultCommentsShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the commentsList where time not equals to UPDATED_TIME
        defaultCommentsShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCommentsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where time in DEFAULT_TIME or UPDATED_TIME
        defaultCommentsShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the commentsList where time equals to UPDATED_TIME
        defaultCommentsShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCommentsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentsList where time is not null
        defaultCommentsShouldBeFound("time.specified=true");

        // Get all the commentsList where time is null
        defaultCommentsShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommentsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);
        Comments parent = CommentsResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        comments.setParent(parent);
        commentsRepository.saveAndFlush(comments);
        Long parentId = parent.getId();

        // Get all the commentsList where parent equals to parentId
        defaultCommentsShouldBeFound("parentId.equals=" + parentId);

        // Get all the commentsList where parent equals to parentId + 1
        defaultCommentsShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllCommentsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);
        ApplicationUser user = ApplicationUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        comments.setUser(user);
        commentsRepository.saveAndFlush(comments);
        Long userId = user.getId();

        // Get all the commentsList where user equals to userId
        defaultCommentsShouldBeFound("userId.equals=" + userId);

        // Get all the commentsList where user equals to userId + 1
        defaultCommentsShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllCommentsByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);
        Post post = PostResourceIT.createEntity(em);
        em.persist(post);
        em.flush();
        comments.setPost(post);
        commentsRepository.saveAndFlush(comments);
        Long postId = post.getId();

        // Get all the commentsList where post equals to postId
        defaultCommentsShouldBeFound("postId.equals=" + postId);

        // Get all the commentsList where post equals to postId + 1
        defaultCommentsShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentsShouldBeFound(String filter) throws Exception {
        restCommentsMockMvc.perform(get("/api/comments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comments.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));

        // Check, that the count call also returns 1
        restCommentsMockMvc.perform(get("/api/comments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentsShouldNotBeFound(String filter) throws Exception {
        restCommentsMockMvc.perform(get("/api/comments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentsMockMvc.perform(get("/api/comments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingComments() throws Exception {
        // Get the comments
        restCommentsMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        int databaseSizeBeforeUpdate = commentsRepository.findAll().size();

        // Update the comments
        Comments updatedComments = commentsRepository.findById(comments.getId()).get();
        // Disconnect from session so that the updates on updatedComments are not directly saved in db
        em.detach(updatedComments);
        updatedComments
            .contentText(UPDATED_CONTENT_TEXT)
            .datePub(UPDATED_DATE_PUB)
            .time(UPDATED_TIME);
        CommentsDTO commentsDTO = commentsMapper.toDto(updatedComments);

        restCommentsMockMvc.perform(put("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commentsDTO)))
            .andExpect(status().isOk());

        // Validate the Comments in the database
        List<Comments> commentsList = commentsRepository.findAll();
        assertThat(commentsList).hasSize(databaseSizeBeforeUpdate);
        Comments testComments = commentsList.get(commentsList.size() - 1);
        assertThat(testComments.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testComments.getDatePub()).isEqualTo(UPDATED_DATE_PUB);
        assertThat(testComments.getTime()).isEqualTo(UPDATED_TIME);

        // Validate the Comments in Elasticsearch
        verify(mockCommentsSearchRepository, times(1)).save(testComments);
    }

    @Test
    @Transactional
    public void updateNonExistingComments() throws Exception {
        int databaseSizeBeforeUpdate = commentsRepository.findAll().size();

        // Create the Comments
        CommentsDTO commentsDTO = commentsMapper.toDto(comments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentsMockMvc.perform(put("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comments in the database
        List<Comments> commentsList = commentsRepository.findAll();
        assertThat(commentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Comments in Elasticsearch
        verify(mockCommentsSearchRepository, times(0)).save(comments);
    }

    @Test
    @Transactional
    public void deleteComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        int databaseSizeBeforeDelete = commentsRepository.findAll().size();

        // Delete the comments
        restCommentsMockMvc.perform(delete("/api/comments/{id}", comments.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comments> commentsList = commentsRepository.findAll();
        assertThat(commentsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Comments in Elasticsearch
        verify(mockCommentsSearchRepository, times(1)).deleteById(comments.getId());
    }

    @Test
    @Transactional
    public void searchComments() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commentsRepository.saveAndFlush(comments);
        when(mockCommentsSearchRepository.search(queryStringQuery("id:" + comments.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(comments), PageRequest.of(0, 1), 1));

        // Search the comments
        restCommentsMockMvc.perform(get("/api/_search/comments?query=id:" + comments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comments.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }
}
