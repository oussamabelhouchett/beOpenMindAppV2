package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BeOpenMindAppV2App;
import com.mycompany.myapp.domain.Reaction;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.ApplicationUser;
import com.mycompany.myapp.repository.ReactionRepository;
import com.mycompany.myapp.repository.search.ReactionSearchRepository;
import com.mycompany.myapp.service.ReactionService;
import com.mycompany.myapp.service.dto.ReactionDTO;
import com.mycompany.myapp.service.mapper.ReactionMapper;
import com.mycompany.myapp.service.dto.ReactionCriteria;
import com.mycompany.myapp.service.ReactionQueryService;

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
 * Integration tests for the {@link ReactionResource} REST controller.
 */
@SpringBootTest(classes = BeOpenMindAppV2App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReactionResourceIT {

    private static final Integer DEFAULT_IS_COMMENT = 1;
    private static final Integer UPDATED_IS_COMMENT = 2;
    private static final Integer SMALLER_IS_COMMENT = 1 - 1;

    private static final Integer DEFAULT_IS_LIKE = 1;
    private static final Integer UPDATED_IS_LIKE = 2;
    private static final Integer SMALLER_IS_LIKE = 1 - 1;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private ReactionMapper reactionMapper;

    @Autowired
    private ReactionService reactionService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ReactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReactionSearchRepository mockReactionSearchRepository;

    @Autowired
    private ReactionQueryService reactionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity(EntityManager em) {
        Reaction reaction = new Reaction()
            .isComment(DEFAULT_IS_COMMENT)
            .isLike(DEFAULT_IS_LIKE);
        return reaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity(EntityManager em) {
        Reaction reaction = new Reaction()
            .isComment(UPDATED_IS_COMMENT)
            .isLike(UPDATED_IS_LIKE);
        return reaction;
    }

    @BeforeEach
    public void initTest() {
        reaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createReaction() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();
        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate + 1);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getIsComment()).isEqualTo(DEFAULT_IS_COMMENT);
        assertThat(testReaction.getIsLike()).isEqualTo(DEFAULT_IS_LIKE);

        // Validate the Reaction in Elasticsearch
        verify(mockReactionSearchRepository, times(1)).save(testReaction);
    }

    @Test
    @Transactional
    public void createReactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        // Create the Reaction with an existing ID
        reaction.setId(1L);
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reaction in Elasticsearch
        verify(mockReactionSearchRepository, times(0)).save(reaction);
    }


    @Test
    @Transactional
    public void getAllReactions() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc.perform(get("/api/reactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].isComment").value(hasItem(DEFAULT_IS_COMMENT)))
            .andExpect(jsonPath("$.[*].isLike").value(hasItem(DEFAULT_IS_LIKE)));
    }
    
    @Test
    @Transactional
    public void getReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()))
            .andExpect(jsonPath("$.isComment").value(DEFAULT_IS_COMMENT))
            .andExpect(jsonPath("$.isLike").value(DEFAULT_IS_LIKE));
    }


    @Test
    @Transactional
    public void getReactionsByIdFiltering() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        Long id = reaction.getId();

        defaultReactionShouldBeFound("id.equals=" + id);
        defaultReactionShouldNotBeFound("id.notEquals=" + id);

        defaultReactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReactionShouldNotBeFound("id.greaterThan=" + id);

        defaultReactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReactionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment equals to DEFAULT_IS_COMMENT
        defaultReactionShouldBeFound("isComment.equals=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment equals to UPDATED_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.equals=" + UPDATED_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment not equals to DEFAULT_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.notEquals=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment not equals to UPDATED_IS_COMMENT
        defaultReactionShouldBeFound("isComment.notEquals=" + UPDATED_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsInShouldWork() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment in DEFAULT_IS_COMMENT or UPDATED_IS_COMMENT
        defaultReactionShouldBeFound("isComment.in=" + DEFAULT_IS_COMMENT + "," + UPDATED_IS_COMMENT);

        // Get all the reactionList where isComment equals to UPDATED_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.in=" + UPDATED_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment is not null
        defaultReactionShouldBeFound("isComment.specified=true");

        // Get all the reactionList where isComment is null
        defaultReactionShouldNotBeFound("isComment.specified=false");
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment is greater than or equal to DEFAULT_IS_COMMENT
        defaultReactionShouldBeFound("isComment.greaterThanOrEqual=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment is greater than or equal to UPDATED_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.greaterThanOrEqual=" + UPDATED_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment is less than or equal to DEFAULT_IS_COMMENT
        defaultReactionShouldBeFound("isComment.lessThanOrEqual=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment is less than or equal to SMALLER_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.lessThanOrEqual=" + SMALLER_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsLessThanSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment is less than DEFAULT_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.lessThan=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment is less than UPDATED_IS_COMMENT
        defaultReactionShouldBeFound("isComment.lessThan=" + UPDATED_IS_COMMENT);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsCommentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isComment is greater than DEFAULT_IS_COMMENT
        defaultReactionShouldNotBeFound("isComment.greaterThan=" + DEFAULT_IS_COMMENT);

        // Get all the reactionList where isComment is greater than SMALLER_IS_COMMENT
        defaultReactionShouldBeFound("isComment.greaterThan=" + SMALLER_IS_COMMENT);
    }


    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike equals to DEFAULT_IS_LIKE
        defaultReactionShouldBeFound("isLike.equals=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike equals to UPDATED_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.equals=" + UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike not equals to DEFAULT_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.notEquals=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike not equals to UPDATED_IS_LIKE
        defaultReactionShouldBeFound("isLike.notEquals=" + UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsInShouldWork() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike in DEFAULT_IS_LIKE or UPDATED_IS_LIKE
        defaultReactionShouldBeFound("isLike.in=" + DEFAULT_IS_LIKE + "," + UPDATED_IS_LIKE);

        // Get all the reactionList where isLike equals to UPDATED_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.in=" + UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike is not null
        defaultReactionShouldBeFound("isLike.specified=true");

        // Get all the reactionList where isLike is null
        defaultReactionShouldNotBeFound("isLike.specified=false");
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike is greater than or equal to DEFAULT_IS_LIKE
        defaultReactionShouldBeFound("isLike.greaterThanOrEqual=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike is greater than or equal to UPDATED_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.greaterThanOrEqual=" + UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike is less than or equal to DEFAULT_IS_LIKE
        defaultReactionShouldBeFound("isLike.lessThanOrEqual=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike is less than or equal to SMALLER_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.lessThanOrEqual=" + SMALLER_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsLessThanSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike is less than DEFAULT_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.lessThan=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike is less than UPDATED_IS_LIKE
        defaultReactionShouldBeFound("isLike.lessThan=" + UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    public void getAllReactionsByIsLikeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where isLike is greater than DEFAULT_IS_LIKE
        defaultReactionShouldNotBeFound("isLike.greaterThan=" + DEFAULT_IS_LIKE);

        // Get all the reactionList where isLike is greater than SMALLER_IS_LIKE
        defaultReactionShouldBeFound("isLike.greaterThan=" + SMALLER_IS_LIKE);
    }


    @Test
    @Transactional
    public void getAllReactionsByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);
        Post post = PostResourceIT.createEntity(em);
        em.persist(post);
        em.flush();
        reaction.setPost(post);
        reactionRepository.saveAndFlush(reaction);
        Long postId = post.getId();

        // Get all the reactionList where post equals to postId
        defaultReactionShouldBeFound("postId.equals=" + postId);

        // Get all the reactionList where post equals to postId + 1
        defaultReactionShouldNotBeFound("postId.equals=" + (postId + 1));
    }


    @Test
    @Transactional
    public void getAllReactionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);
        ApplicationUser user = ApplicationUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        reaction.setUser(user);
        reactionRepository.saveAndFlush(reaction);
        Long userId = user.getId();

        // Get all the reactionList where user equals to userId
        defaultReactionShouldBeFound("userId.equals=" + userId);

        // Get all the reactionList where user equals to userId + 1
        defaultReactionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReactionShouldBeFound(String filter) throws Exception {
        restReactionMockMvc.perform(get("/api/reactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].isComment").value(hasItem(DEFAULT_IS_COMMENT)))
            .andExpect(jsonPath("$.[*].isLike").value(hasItem(DEFAULT_IS_LIKE)));

        // Check, that the count call also returns 1
        restReactionMockMvc.perform(get("/api/reactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReactionShouldNotBeFound(String filter) throws Exception {
        restReactionMockMvc.perform(get("/api/reactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReactionMockMvc.perform(get("/api/reactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).get();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);
        updatedReaction
            .isComment(UPDATED_IS_COMMENT)
            .isLike(UPDATED_IS_LIKE);
        ReactionDTO reactionDTO = reactionMapper.toDto(updatedReaction);

        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reactionDTO)))
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getIsComment()).isEqualTo(UPDATED_IS_COMMENT);
        assertThat(testReaction.getIsLike()).isEqualTo(UPDATED_IS_LIKE);

        // Validate the Reaction in Elasticsearch
        verify(mockReactionSearchRepository, times(1)).save(testReaction);
    }

    @Test
    @Transactional
    public void updateNonExistingReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reaction in Elasticsearch
        verify(mockReactionSearchRepository, times(0)).save(reaction);
    }

    @Test
    @Transactional
    public void deleteReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeDelete = reactionRepository.findAll().size();

        // Delete the reaction
        restReactionMockMvc.perform(delete("/api/reactions/{id}", reaction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reaction in Elasticsearch
        verify(mockReactionSearchRepository, times(1)).deleteById(reaction.getId());
    }

    @Test
    @Transactional
    public void searchReaction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);
        when(mockReactionSearchRepository.search(queryStringQuery("id:" + reaction.getId())))
            .thenReturn(Collections.singletonList(reaction));

        // Search the reaction
        restReactionMockMvc.perform(get("/api/_search/reactions?query=id:" + reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].isComment").value(hasItem(DEFAULT_IS_COMMENT)))
            .andExpect(jsonPath("$.[*].isLike").value(hasItem(DEFAULT_IS_LIKE)));
    }
}
